/**
 *
 */
package com.duroty.utils.http;

import org.apache.commons.httpclient.ConnectMethod;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.protocol.Protocol;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

import java.util.HashMap;
import java.util.Map;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class HttpContent {
    /** Logger. */
    private final static Logger logger = Logger.getLogger(HttpContent.class.getName());

    /**
    * To obtain the http headers only
    */
    private static final int HEAD = 1;

    /**
    * To obtain the response body
    */
    private static final int GET = 2;

    /**
    * Max. number of redirects
    */
    private static final int MAX_NO_REDIRECTS = 10;

    /** The URI version of the URL */
    private URI httpUri;

    /**
    * A map containing any format-specific blank node mappings from previous
    * parses of this file.
    */
    private Map blankNodeMap = new HashMap();

    /**
    * Connection host host
    */
    private String host;

    /**
    * port to make connection to
    */
    private int port;

    /**
    * Schema for connection schema
    */
    private String schema;

    /**
    * A container for HTTP attributes that may persist from request to request
    */
    private HttpState state = new HttpState();

    /**
    * Http connection
    */
    private HttpConnection connection = null;

    /**
     * DOCUMENT ME!
     */
    private String charset = null;

    /**
    * Creates a new HttpContent object.
    *
    * @param uri DOCUMENT ME!
    *
    * @throws URISyntaxException DOCUMENT ME!
    * @throws MalformedURLException DOCUMENT ME!
    */
    public HttpContent(URI uri)
        throws URISyntaxException, MalformedURLException {
        this(uri.toURL());
    }

    /**
    * Constructor.
    *
    * @param url The URL this object will be representing
    * the content of
    */
    public HttpContent(URL url) throws URISyntaxException {
        // Validate "url" parameter
        if (url == null) {
            throw new IllegalArgumentException("Null \"url\" parameter");
        }

        initialiseSettings(url);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCharset() {
        return charset;
    }

    /**
    * Initialise the basic settings for a connection
    *
    * @param url
    * location of source
    * @throws URISyntaxException
    * invalid URI
    */
    private void initialiseSettings(URL url) throws URISyntaxException {
        // Convert the URL to a Uri
        httpUri = new URI(url.toExternalForm());

        // obtain basic details for connections
        host = httpUri.getHost();
        port = httpUri.getPort();
        schema = httpUri.getScheme();
    }

    /**
    * Retrieves the node map used to ensure that blank nodes are consistent.
    *
    * @return The node map used to ensure that blank nodes are consistent
    */
    public Map getBlankNodeMap() {
        return blankNodeMap;
    }

    /**
    * Obtain the approrpriate connection method
    *
    * @param methodType
    * can be HEAD or GET
    * @return HttpMethodBase method
    */
    private HttpMethod getConnectionMethod(int methodType) {
        if ((methodType != GET) && (methodType != HEAD)) {
            throw new IllegalArgumentException(
                "Invalid method base supplied for connection");
        }

        Protocol protocol = Protocol.getProtocol(schema);

        connection = new HttpConnection(host, port, protocol);

        String proxyHost = System.getProperty("duroty.httpcontent.proxyHost");

        if ((proxyHost != null) && (proxyHost.length() > 0)) {
            connection.setProxyHost(proxyHost);
        }

        String proxyPort = System.getProperty("duroty.httpcontent.proxyPort");

        if ((proxyPort != null) && (proxyPort.length() > 0)) {
            connection.setProxyPort(Integer.parseInt(proxyPort));
        }

        // default timeout to 30 seconds
        connection.setConnectionTimeout(Integer.parseInt(System.getProperty(
                    "duroty.httpcontent.timeout", "30000")));

        String proxyUserName = System.getProperty(
                "duroty.httpcontent.proxyUserName");

        if (proxyUserName != null) {
            state.setCredentials(System.getProperty(
                    "duroty.httpcontent.proxyRealm"),
                System.getProperty("duroty.httpcontent.proxyRealmHost"),
                new UsernamePasswordCredentials(proxyUserName,
                    System.getProperty("duroty.httpcontent.proxyPassword")));
        }

        HttpMethod method = null;

        if (methodType == HEAD) {
            method = new HeadMethod(httpUri.toString());
        } else {
            method = new GetMethod(httpUri.toString());
        }

        if (connection.isProxied() && connection.isSecure()) {
            method = new ConnectMethod(method);
        }

        // manually follow redirects due to the
        // strictness of http client implementation
        method.setFollowRedirects(false);

        return method;
    }

    /**
    * Obtain a valid connection and follow redirects if neccessary
    *
    * @param methodType
    * request the headders (HEAD) or body (GET)
    * @return valid connection method. Can be null.
    * @throws IOException
    * @throws URISyntaxException
    */
    private HttpMethod establishConnection(int methodType)
        throws IOException {
        HttpMethod method = this.getConnectionMethod(methodType);
        Header header = null;

        if (method != null) {
            method.execute(state, connection);

            if (!isValidStatusCode(method.getStatusCode())) {
                throw new UnknownHostException(
                    "Unable to obtain connection to " + httpUri +
                    ". Returned status code " + method.getStatusCode());
            } else {
                // has a redirection been issued
                int numberOfRedirection = 0;

                while (isRedirected(method.getStatusCode()) &&
                        (numberOfRedirection <= MAX_NO_REDIRECTS)) {
                    // release the existing connection
                    method.releaseConnection();

                    //attempt to follow the redirects
                    numberOfRedirection++;

                    // obtain the new location
                    header = method.getResponseHeader("location");

                    if (header != null) {
                        try {
                            initialiseSettings(new URL(header.getValue()));

                            if (logger.isInfoEnabled()) {
                                logger.info("Redirecting to " +
                                    header.getValue());
                            }

                            // attempt a new connection to this location
                            method = this.getConnectionMethod(methodType);
                            method.execute(state, connection);

                            if (!isValidStatusCode(method.getStatusCode())) {
                                throw new UnknownHostException(
                                    "Unable to obtain connection to " +
                                    " the redirected site " + httpUri +
                                    ". Returned status code " +
                                    method.getStatusCode());
                            }
                        } catch (URISyntaxException ex) {
                            throw new IOException(
                                "Unable to follow redirection to " +
                                header.getValue() + " Not a valid URI");
                        }
                    } else {
                        throw new IOException(
                            "Unable to obtain redirecting detaild from " +
                            httpUri);
                    }
                }
            }
        }

        return method;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MimeType getContentType() {
        MimeType mimeType = null;
        HeadMethod method = null;
        String contentType = null;

        try {
            // obtain connection and retrieve the headers
            method = (HeadMethod) establishConnection(HEAD);

            /*Header[] headers = method.getRequestHeaders();

            for (int i = 0; i < headers.length; i++) {
                    System.out.println(headers[i].getName() + " >> " + headers[i].getValue());
            }*/
            Header header = method.getResponseHeader("Content-Type");

            if (header != null) {
                contentType = header.getValue();
                mimeType = new MimeType(contentType);

                if (logger.isInfoEnabled()) {
                    logger.info("Obtain content type " + mimeType + " from " +
                        httpUri);
                }
            }
        } catch (MimeTypeParseException e) {
            logger.warn("Unable to parse " + contentType +
                " as a content type for " + httpUri);
        } catch (IOException e) {
            logger.info("Unable to obtain content type for " + httpUri);
        } catch (java.lang.IllegalStateException e) {
            logger.info("Unable to obtain content type for " + httpUri);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }

            if (connection != null) {
                connection.close();
            }
        }

        return mimeType;
    }

    /**
    * Retrieves the URI for the actual content.
    *
    * @return The URI for the actual content
    */
    public URI getURI() {
        return httpUri;
    }

    /**
    * Creates an input stream to the resource whose content we are representing.
    *
    * @return An input stream to the resource whose content we are representing
    * @throws IOException
    */
    public InputStream newInputStream() throws IOException {
        // Create an input stream by opening the URL's input stream
        GetMethod method = null;
        InputStream inputStream = null;

        // obtain connection and retrieve the headers
        method = (GetMethod) establishConnection(GET);
        inputStream = method.getResponseBodyAsStream();
        this.charset = method.getRequestCharSet();

        if (inputStream == null) {
            throw new IOException("Unable to obtain inputstream from " +
                httpUri);
        }

        return inputStream;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public String getResponseBodyAsString() throws IOException {
        //    	 Create an input stream by opening the URL's input stream
        GetMethod method = null;
        String contents = null;

        // obtain connection and retrieve the headers
        method = (GetMethod) establishConnection(GET);
        contents = method.getResponseBodyAsString();
        this.charset = method.getRequestCharSet();

        if (contents == null) {
            throw new IOException("Unable to obtain inputstream from " +
                httpUri);
        }

        return contents;
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isValidStatusCode(int status) {
        return ((status == HttpStatus.SC_OK) || isRedirected(status));
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isRedirected(int status) {
        return ((status == HttpStatus.SC_TEMPORARY_REDIRECT) ||
        (status == HttpStatus.SC_MOVED_TEMPORARILY) ||
        (status == HttpStatus.SC_MOVED_PERMANENTLY) ||
        (status == HttpStatus.SC_SEE_OTHER));
    }
}
