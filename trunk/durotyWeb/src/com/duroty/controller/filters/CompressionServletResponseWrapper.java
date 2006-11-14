package com.duroty.controller.filters;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


/**
 * Implementation of <b>HttpServletResponseWrapper</b> that works with
 * the CompressionServletResponseStream implementation..
 *
 * @author Amy Roh
 * @author Dmitri Valdin
 * @version $Revision: 1.1 $, $Date: 2006/07/18 07:44:27 $
 */
public class CompressionServletResponseWrapper
    extends HttpServletResponseWrapper {
    /**
     * Descriptive information about this Response implementation.
     */
    protected static final String info = "CompressionServletResponseWrapper";

    // ----------------------------------------------------- Instance Variables

    /**
     * Original response
     */
    protected HttpServletResponse origResponse = null;

    /**
     * The ServletOutputStream that has been returned by
     * <code>getOutputStream()</code>, if any.
     */
    protected ServletOutputStream stream = null;

    /**
     * The PrintWriter that has been returned by
     * <code>getWriter()</code>, if any.
     */
    protected PrintWriter writer = null;

    /**
     * The threshold number to compress
     */
    protected int threshold = 0;

    /**
     * Debug level
     */
    private int debug = 0;

    /**
     * Content type
     */
    protected String contentType = null;

    // ----------------------------------------------------- Constructor

    /**
     * Calls the parent constructor which creates a ServletResponse adaptor
     * wrapping the given response object.
     */
    public CompressionServletResponseWrapper(HttpServletResponse response) {
        super(response);
        origResponse = response;

        if (debug > 1) {
            System.out.println(
                "CompressionServletResponseWrapper constructor gets called");
        }
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Set content type
     */
    public void setContentType(String contentType) {
        if (debug > 1) {
            System.out.println("setContentType to " + contentType);
        }

        this.contentType = contentType;
        origResponse.setContentType(contentType);
    }

    /**
     * Set threshold number
     */
    public void setCompressionThreshold(int threshold) {
        if (debug > 1) {
            System.out.println("setCompressionThreshold to " + threshold);
        }

        this.threshold = threshold;
    }

    /**
     * Set debug level
     */
    public void setDebugLevel(int debug) {
        this.debug = debug;
    }

    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {
        if (debug > 1) {
            System.out.println("createOutputStream gets called");
        }

        CompressionResponseStream stream = new CompressionResponseStream(origResponse);
        stream.setDebugLevel(debug);
        stream.setBuffer(threshold);

        return stream;
    }

    /**
     * Finish a response.
     */
    public void finishResponse() {
        try {
            if (writer != null) {
                writer.close();
            } else {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
        }
    }

    // ------------------------------------------------ ServletResponse Methods

    /**
     * Flush the buffer and commit this response.
     *
     * @exception IOException if an input/output error occurs
     */
    public void flushBuffer() throws IOException {
        if (debug > 1) {
            System.out.println(
                "flush buffer @ CompressionServletResponseWrapper");
        }

        ((CompressionResponseStream) stream).flush();
    }

    /**
     * Return the servlet output stream associated with this Response.
     *
     * @exception IllegalStateException if <code>getWriter</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException(
                "getWriter() has already been called for this response");
        }

        if (stream == null) {
            stream = createOutputStream();
        }

        if (debug > 1) {
            System.out.println("stream is set to " + stream +
                " in getOutputStream");
        }

        return (stream);
    }

    /**
     * Return the writer associated with this Response.
     *
     * @exception IllegalStateException if <code>getOutputStream</code> has
     *  already been called for this response
     * @exception IOException if an input/output error occurs
     */
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return (writer);
        }

        if (stream != null) {
            throw new IllegalStateException(
                "getOutputStream() has already been called for this response");
        }

        stream = createOutputStream();

        if (debug > 1) {
            System.out.println("stream is set to " + stream + " in getWriter");
        }

        //String charset = getCharsetFromContentType(contentType);
        String charEnc = origResponse.getCharacterEncoding();

        if (debug > 1) {
            System.out.println("character encoding is " + charEnc);
        }

        // HttpServletResponse.getCharacterEncoding() shouldn't return null
        // according the spec, so feel free to remove that "if"
        if (charEnc != null) {
            writer = new PrintWriter(new OutputStreamWriter(stream, charEnc));
        } else {
            writer = new PrintWriter(stream);
        }

        return (writer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param length DOCUMENT ME!
     */
    public void setContentLength(int length) {
    }

    /**
     * Returns character from content type. This method was taken from tomcat.
     * @author rajo
     */
    private static String getCharsetFromContentType(String type) {
        if (type == null) {
            return null;
        }

        int semi = type.indexOf(";");

        if (semi == -1) {
            return null;
        }

        String afterSemi = type.substring(semi + 1);
        int charsetLocation = afterSemi.indexOf("charset=");

        if (charsetLocation == -1) {
            return null;
        } else {
            String afterCharset = afterSemi.substring(charsetLocation + 8);
            String encoding = afterCharset.trim();

            return encoding;
        }
    }
}
