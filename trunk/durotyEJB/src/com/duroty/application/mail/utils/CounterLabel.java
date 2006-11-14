/**
 *
 */
package com.duroty.application.mail.utils;

import java.io.Serializable;


/**
 * @author durot
 *
 */
public class CounterLabel implements Serializable {
    /**
         *
         */
    private static final long serialVersionUID = -8916935097736859729L;

    /**
    * DOCUMENT ME!
    */
    private int idint;

    /**
     * DOCUMENT ME!
     */
    private int count;

    /**
     * Creates a new CounterLabel object.
     *
     * @param idint DOCUMENT ME!
     * @param count DOCUMENT ME!
     */
    public CounterLabel(int idint, int count) {
        super();

        // TODO Auto-generated constructor stub
        this.idint = idint;
        this.count = count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCount() {
        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @param count DOCUMENT ME!
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIdint() {
        return idint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setIdint(int idint) {
        this.idint = idint;
    }
}
