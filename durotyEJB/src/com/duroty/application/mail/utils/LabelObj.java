/**
 *
 */
package com.duroty.application.mail.utils;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;


/**
 * @author durot
 *
 */
public class LabelObj implements Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = -4404848477546576934L;

    /**
    * DOCUMENT ME!
    */
    private int idint;

    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * DOCUMENT ME!
     */
    private Set filters = new HashSet(0);

    /**
     * DOCUMENT ME!
     */
    private int count;

    /**
    * Creates a new LabelObj object.
    *
    * @param idint DOCUMENT ME!
    * @param name DOCUMENT ME!
    */
    public LabelObj(int idint, String name) {
        super();
        this.idint = idint;
        this.name = name;
    }

    /**
     * Creates a new LabelObj object.
     */
    public LabelObj() {
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void addFilter(FilterObj filter) {
        this.filters.add(filter);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getFilters() {
        return filters;
    }
}
