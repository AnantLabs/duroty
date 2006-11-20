package com.duroty.utils.io;


/**
 * Copyright (c) 2001 by Daniel Matuschek
 */
import java.io.Writer;


/**
 * This class implements a simple Writer that ignores everything it is like a
 * /dev/null for Java
 *
 * @author Daniel Matuschek
 * @version $Id $
 */
public class NullWriter extends Writer {
    /**
     * Creates a new NullWriter object.
     */
    public NullWriter() {
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
    }

    /**
     * DOCUMENT ME!
     */
    public void flush() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param cbuf DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param len DOCUMENT ME!
     */
    public void write(char[] cbuf, int off, int len) {
    }
}
