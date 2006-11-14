package com.duroty.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class ReadObjectFromFile {
    /** DOCUMENT ME! */
    private String fileName;

    /**
     * DOCUMENT ME!
     */
    private File file;

    /** DOCUMENT ME! */
    private Object obj;

    /**
     * Creates a new ReadObjectFromFile object.
     *
     * @param fileName DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public ReadObjectFromFile(String fileName) {
        file = new File(fileName);
    }

    /**
     * Creates a new ReadObjectFromFile object.
     *
     * @param file DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public ReadObjectFromFile(File file) {
        this.file = file;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public void read()
        throws IOException, FileNotFoundException, FileNotReadableException, 
            ClassNotFoundException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            if (!file.exists()) {
                throw new FileNotFoundException("The file > " + fileName +
                    " < don't open");
            }

            if (!file.canRead()) {
                throw new FileNotReadableException("The file > " + fileName +
                    " < don't read");
            }

            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            obj = ois.readObject();
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        } catch (FileNotReadableException fnre) {
            throw fnre;
        } catch (ClassNotFoundException cnfe) {
            throw cnfe;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                }

                fis = null;
            }

            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception ex) {
                }

                ois = null;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getObj() {
        return obj;
    }
}
