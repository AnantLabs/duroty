/* CVS ID: $Id: AttributedExpireableCache.java,v 1.1 2006/03/08 09:07:01 durot Exp $ */
package com.duroty.utils.misc;

import java.util.*;


/**
 * AttributedExpireableCache.java
 *
 *
 * Created: Tue Apr 25 14:57:22 2000
 *
 * @author Sebastian Schaffert
 * @version
 */
public class AttributedExpireableCache extends ExpireableCache {
    /**
     * DOCUMENT ME!
     */
    protected Hashtable attributes;

    /**
     * Creates a new AttributedExpireableCache object.
     *
     * @param capacity DOCUMENT ME!
     * @param expire_factor DOCUMENT ME!
     */
    public AttributedExpireableCache(int capacity, float expire_factor) {
        super(capacity, expire_factor);
        attributes = new Hashtable(capacity);
    }

    /**
     * Creates a new AttributedExpireableCache object.
     *
     * @param capacity DOCUMENT ME!
     */
    public AttributedExpireableCache(int capacity) {
        super(capacity);
        attributes = new Hashtable(capacity);
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     * @param object DOCUMENT ME!
     * @param attribs DOCUMENT ME!
     */
    public synchronized void put(Object id, Object object, Object attribs) {
        attributes.put(id, attribs);
        super.put(id, object);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getAttributes(Object key) {
        return attributes.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public synchronized void remove(Object key) {
        attributes.remove(key);
        super.remove(key);
    }
} // AttributedExpireableCache
