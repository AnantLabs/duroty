package com.duroty.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class WriteObjectToFile {
    /** DOCUMENT ME! */
    private File file;

    /** DOCUMENT ME! */
    private Object obj;

    /**
     * Creates a new WriteObjectToFile object.
     *
     * @param fileName DOCUMENT ME!
     * @param obj DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public WriteObjectToFile(String fileName, Object obj) {
        File file = new File(fileName);
        this.file = file;
        this.obj = obj;
    }

    /**
     * Creates a new WriteObjectToFile object.
     *
     * @param file DOCUMENT ME!
     * @param obj DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public WriteObjectToFile(File file, Object obj) {
        this.file = file;
        this.obj = obj;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void write() throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception ex) {
                }

                fos = null;
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (Exception ex) {
                }

                oos = null;
            }
        }
    }
}
