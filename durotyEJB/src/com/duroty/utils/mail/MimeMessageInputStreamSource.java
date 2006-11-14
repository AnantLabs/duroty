package com.duroty.utils.mail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * Takes an input stream and creates a repeatable input stream source
 * for a MimeMessageWrapper.  It does this by completely reading the
 * input stream and saving that to a temporary file that should delete on exit,
 * or when this object is GC'd.
 *
 * @see MimeMessageWrapper
 *
 *
 */
public class MimeMessageInputStreamSource extends MimeMessageSource implements Disposable {
    /**
     * A temporary file used to hold the message stream
     */
    File file = null;

    /**
     * The full path of the temporary file
     */
    String sourceId = null;
    
    boolean tempFile = false;    

    /**
     * Construct a new MimeMessageInputStreamSource from an
     * <code>InputStream</code> that contains the bytes of a
     * MimeMessage.
     *
     * @param key the prefix for the name of the temp file
     * @param in the stream containing the MimeMessage
     *
     * @throws MessagingException if an error occurs while trying to store
     *                            the stream
     */
    public MimeMessageInputStreamSource(String path, String key, InputStream in, boolean tempFile)
        throws MessagingException {
        //We want to immediately read this into a temporary file
        //Create a temp file and channel the input stream into it
        OutputStream fout = null;

        try {
        	if (!path.endsWith(File.separator)) {
        		path = path + File.separator;
        	}
        	
        	File dir = new File(path);
        	if (!dir.exists()) {
        		dir.mkdirs();
        	}
        	
        	if (tempFile) {
        		file = File.createTempFile(key, ".m64");
        	} else {        	
        		file = new File(dir, key + ".m64");
        	}
            fout = new BufferedOutputStream(new FileOutputStream(file));

            int b = -1;

            while ((b = in.read()) != -1) {
                fout.write(b);
            }

            fout.flush();

            sourceId = file.getCanonicalPath();
        } catch (IOException ioe) {
            throw new MessagingException("Unable to retrieve the data: " +
                ioe.getMessage(), ioe);
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException ioe) {
                // Ignored - logging unavailable to log this non-fatal error.
            }

            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ioe) {
                // Ignored - logging unavailable to log this non-fatal error.
            }
        }
    }
    
    /**
     * Construct a new MimeMessageInputStreamSource from an
     * <code>InputStream</code> that contains the bytes of a
     * MimeMessage.
     *
     * @param key the prefix for the name of the temp file
     * @param in the stream containing the MimeMessage
     *
     * @throws MessagingException if an error occurs while trying to store
     *                            the stream
     */
    public MimeMessageInputStreamSource(String path, String key, MimeMessage mime, boolean tempFile)
        throws MessagingException {
        //We want to immediately read this into a temporary file
        //Create a temp file and channel the input stream into it
        OutputStream fout = null;

        try {
        	if (!path.endsWith(File.separator)) {
        		path = path + File.separator;
        	}
        	
        	File dir = new File(path);
        	if (!dir.exists()) {
        		dir.mkdirs();
        	}
        	
        	if (tempFile) {
        		file = File.createTempFile(key, ".m64");
        	} else {        	
        		file = new File(dir, key + ".m64");
        	}
        	
            fout = new BufferedOutputStream(new FileOutputStream(file));

            mime.writeTo(fout);
            
            fout.flush();

            sourceId = file.getCanonicalPath();
        } catch (IOException ioe) {
            throw new MessagingException("Unable to retrieve the data: " +
                ioe.getMessage(), ioe);
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException ioe) {
                // Ignored - logging unavailable to log this non-fatal error.
            }
        }
    }

    /**
     * Returns the unique identifier of this input stream source
     *
     * @return the unique identifier for this MimeMessageInputStreamSource
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * Get an input stream to retrieve the data stored in the temporary file
     *
     * @return a <code>BufferedInputStream</code> containing the data
     */
    public synchronized InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * Get the size of the temp file
     *
     * @return the size of the temp file
     *
     * @throws IOException if an error is encoutered while computing the size of the message
     */
    public long getMessageSize() throws IOException {
        return file.length();
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
    	if (tempFile) {
    		try {
    			if ((file != null) && file.exists()) {
    				file.delete();
    			}
    		} catch (Exception e) {
    			//ignore
    		}
    	}

        file = null;
    }

    /**
     * <p>Finalizer that closes and deletes the temp file.  Very bad.</p>
     * We're leaving this in temporarily, while also establishing a more
     * formal mechanism for cleanup through use of the dispose() method.
     *
     */
    public void finalize() {
        dispose();
    }
}
