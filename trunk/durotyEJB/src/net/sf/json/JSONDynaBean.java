/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.json;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class JSONDynaBean implements DynaBean, Serializable {
    /**
     * DOCUMENT ME!
     */
    private static final long serialVersionUID = -3152084265262514977L;

    /**
     * DOCUMENT ME!
     */
    protected JSONDynaClass dynaClass;

    /**
     * DOCUMENT ME!
     */
    protected Map dynaValues = new HashMap();

    /**
     * Creates a new JSONDynaBean object.
     */
    public JSONDynaBean() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(String name, String key) {
        Object value = dynaValues.get(name);

        if (value == null) {
            throw new NullPointerException("Unmapped property name: " + name +
                " key: " + key);
        } else if (!(value instanceof Map)) {
            throw new IllegalArgumentException("Non-Mapped property name: " +
                name + " key: " + key);
        }

        return ((Map) value).containsKey(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof JSONDynaBean)) {
            return false;
        }

        JSONDynaBean other = (JSONDynaBean) obj;
        EqualsBuilder builder = new EqualsBuilder().append(this.dynaClass,
                other.dynaClass);
        DynaProperty[] props = dynaClass.getDynaProperties();

        for (int i = 0; i < props.length; i++) {
            DynaProperty prop = props[i];
            builder.append(dynaValues.get(prop.getName()),
                dynaValues.get(prop.getName()));
        }

        return builder.isEquals();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(String name) {
        Object value = dynaValues.get(name);

        if (value != null) {
            return value;
        }

        Class type = getDynaProperty(name).getType();

        if (type == null) {
            throw new NullPointerException("Unspecified property type for " +
                name);
        }

        if (!type.isPrimitive()) {
            return value;
        }

        if (type == Boolean.TYPE) {
            return Boolean.FALSE;
        } else if (type == Byte.TYPE) {
            return new Byte((byte) 0);
        } else if (type == Character.TYPE) {
            return new Character((char) 0);
        } else if (type == Short.TYPE) {
            return new Short((short) 0);
        } else if (type == Integer.TYPE) {
            return new Integer(0);
        } else if (type == Long.TYPE) {
            return new Long(0);
        } else if (type == Float.TYPE) {
            return new Float(0.0);
        } else if (type == Double.TYPE) {
            return new Double(0);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(String name, int index) {
        Object value = dynaValues.get(name);

        if (value == null) {
            throw new NullPointerException("Unindexed property name: " + name +
                " index: " + index);
        } else if (!(value instanceof List) || !(value.getClass().isArray())) {
            throw new IllegalArgumentException("Non-Indexed property name: " +
                name + " index: " + index);
        }

        if (value.getClass().isArray()) {
            value = Array.get(value, index);
        } else if (value instanceof List) {
            value = ((List) value).get(index);
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(String name, String key) {
        Object value = dynaValues.get(name);

        if (value == null) {
            throw new NullPointerException("Unmapped property name: " + name +
                " key: " + key);
        } else if (!(value instanceof Map)) {
            throw new IllegalArgumentException("Non-Mapped property name: " +
                name + " key: " + key);
        }

        return ((Map) value).get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DynaClass getDynaClass() {
        return this.dynaClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder().append(dynaClass);
        DynaProperty[] props = dynaClass.getDynaProperties();

        for (int i = 0; i < props.length; i++) {
            DynaProperty prop = props[i];
            builder.append(dynaValues.get(prop.getName()));
        }

        return builder.toHashCode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param key DOCUMENT ME!
     */
    public void remove(String name, String key) {
        Object value = dynaValues.get(name);

        if (value == null) {
            throw new NullPointerException("Unmapped property name: " + name +
                " key: " + key);
        } else if (!(value instanceof Map)) {
            throw new IllegalArgumentException("Non-Mapped property name: " +
                name + " key: " + key);
        }

        ((Map) value).remove(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param index DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void set(String name, int index, Object value) {
        Object prop = dynaValues.get(name);

        if (prop == null) {
            throw new NullPointerException("Unindexed property name: " + name +
                " index: " + index);
        } else if (!(prop instanceof List) || !(prop.getClass().isArray())) {
            throw new IllegalArgumentException("Non-Indexed property name: " +
                name + " index: " + index);
        }

        if (prop.getClass().isArray()) {
            Array.set(prop, index, value);
        } else if (value instanceof List) {
            ((List) prop).set(index, value);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void set(String name, Object value) {
        DynaProperty property = getDynaProperty(name);

        if (property.getType() == null) {
            throw new NullPointerException("Unspecified property type for " +
                name);
        }

        if (value == null) {
            if (property.getType().isPrimitive()) {
                throw new NullPointerException("Property type for " + name +
                    " is primitive");
            }
        } else if (!isDynaAssignable(property.getType(), value.getClass())) {
            try {
                value = ConvertUtils.convert(String.valueOf(value),
                        value.getClass());
            } catch (Exception e) {
                throw new IllegalArgumentException(
                    "Unassignable property type for " + name);
            }
        }

        // log.debug( name + " = " + value );
        dynaValues.put(name, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void set(String name, String key, Object value) {
        Object prop = dynaValues.get(name);

        if (prop == null) {
            throw new IllegalArgumentException("Unmapped property name: " +
                name + " key: " + key);
        } else if (!(prop instanceof Map)) {
            throw new IllegalArgumentException("Non-Mapped property name: " +
                name + " key: " + key);
        }

        ((Map) prop).put(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dynaClass DOCUMENT ME!
     */
    public void setDynamicFormClass(JSONDynaClass dynaClass) {
        if (this.dynaClass == null) {
            this.dynaClass = dynaClass;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(dynaValues)
                                                                        .toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected DynaProperty getDynaProperty(String name) {
        DynaProperty property = getDynaClass().getDynaProperty(name);

        if (property == null) {
            throw new IllegalArgumentException("Unspecified property for " +
                name);
        }

        return property;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dest DOCUMENT ME!
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean isDynaAssignable(Class dest, Class src) {
        boolean assignable = dest.isAssignableFrom(src);
        assignable = ((dest == Boolean.TYPE) && (src == Boolean.class)) ? true
                                                                        : assignable;
        assignable = ((dest == Byte.TYPE) && (src == Byte.class)) ? true
                                                                  : assignable;
        assignable = ((dest == Character.TYPE) && (src == Character.class))
            ? true : assignable;
        assignable = ((dest == Short.TYPE) && (src == Short.class)) ? true
                                                                    : assignable;
        assignable = ((dest == Integer.TYPE) && (src == Integer.class)) ? true
                                                                        : assignable;
        assignable = ((dest == Long.TYPE) && (src == Long.class)) ? true
                                                                  : assignable;
        assignable = ((dest == Float.TYPE) && (src == Float.class)) ? true
                                                                    : assignable;
        assignable = ((dest == Double.TYPE) && (src == Double.class)) ? true
                                                                      : assignable;

        if ((src == Double.TYPE) || Double.class.isAssignableFrom(src)) {
            assignable = (isByte(dest) || isShort(dest) || isInteger(dest) ||
                isLong(dest) || isFloat(dest)) ? true : assignable;
        }

        if ((src == Float.TYPE) || Float.class.isAssignableFrom(src)) {
            assignable = (isByte(dest) || isShort(dest) || isInteger(dest) ||
                isLong(dest)) ? true : assignable;
        }

        if ((src == Long.TYPE) || Long.class.isAssignableFrom(src)) {
            assignable = (isByte(dest) || isShort(dest) || isInteger(dest))
                ? true : assignable;
        }

        if ((src == Integer.TYPE) || Integer.class.isAssignableFrom(src)) {
            assignable = (isByte(dest) || isShort(dest)) ? true : assignable;
        }

        if ((src == Short.TYPE) || Short.class.isAssignableFrom(src)) {
            assignable = (isByte(dest)) ? true : assignable;
        }

        return assignable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isByte(Class clazz) {
        return Byte.class.isAssignableFrom(clazz) || (clazz == Byte.TYPE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isFloat(Class clazz) {
        return Float.class.isAssignableFrom(clazz) || (clazz == Float.TYPE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isInteger(Class clazz) {
        return Integer.class.isAssignableFrom(clazz) ||
        (clazz == Integer.TYPE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isLong(Class clazz) {
        return Long.class.isAssignableFrom(clazz) || (clazz == Long.TYPE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param clazz DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isShort(Class clazz) {
        return Short.class.isAssignableFrom(clazz) || (clazz == Short.TYPE);
    }
}
