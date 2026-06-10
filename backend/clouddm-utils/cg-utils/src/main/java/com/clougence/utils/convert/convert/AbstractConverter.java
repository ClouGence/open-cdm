/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.utils.convert.convert;

import java.lang.reflect.Array;
import java.util.Collection;

import com.clougence.utils.BeanUtils;
import com.clougence.utils.convert.ConversionException;
import com.clougence.utils.convert.Converter;

/**
 * Base {@link Converter} implementation that provides the structure
 * for handling conversion <b>to</b> and <b>from</b> a specified type.
 * <p>
 * This implementation provides the basic structure for
 * converting to/from a specified type optionally using a default
 * value or throwing a {@link ConversionException} if a
 * conversion error occurs.
 * <p>
 * Implementations should provide conversion to the specified
 * type and from the specified type to a <code>String</code> value
 * by implementing the following methods:
 * <ul>
 *     <li><code>convertToString(value)</code> - convert to a String
 *        (default implementation uses the objects <code>toString()</code>
 *        method).</li>
 *     <li><code>convertToType(Class, value)</code> - convert
 *         to the specified type</li>
 * </ul>
 *
 * @version $Revision: 640131 $ $Date: 2008-03-23 02:10:31 +0000 (Sun, 23 Mar 2008) $
 * @since 1.8.0
 */
public abstract class AbstractConverter implements Converter {

    /** Returns the default value when the conversion is wrong. */
    private boolean useDefault   = false;
    /**Default value */
    private Object  defaultValue = null;
    // ----------------------------------------------------------- Constructors

    /** Creates a converter that throws {@link ConversionException} on conversion errors. */
    public AbstractConverter(){
    }

    /** Creates a converter that returns the default value on conversion errors. */
    public AbstractConverter(final Object defaultValue){
        this.setDefaultValue(defaultValue);
    }
    // --------------------------------------------------------- Public Methods

    /**
     * Whether to use the default value when an exception occurs during conversion.
     * @return if <code>true</code>, the configured default value is returned when an error is encountered. If <code>false</code>, {@link ConversionException} is thrown.
     */
    public boolean isUseDefault() { return this.useDefault; }

    /**
     * Convert the input object into an output object of the
     * specified type.
     *
     * @param type Data type to which this value should be converted
     * @param value The input value to be converted
     * @return The converted value.
     * @throws ConversionException if conversion cannot be performed
     * successfully and no default is specified.
     */
    @Override
    public Object convert(final Class<?> type, Object value) {
        Class<?> sourceType = value == null ? null : value.getClass();
        value = this.convertArray(value);//If the data source is an Array or a collection then get the first element.
        //Missing Value
        if (value == null) {
            return this.handleMissing(type);
        }
        //
        sourceType = value.getClass();
        try {
            /*Convert --> String*/
            if (type.equals(String.class)) {
                return this.convertToString(value);
            } else if (type.equals(sourceType)) {
                return value;
                /*Convert --> Type*/
            } else {
                return this.convertToType(type, value);
            }
        } catch (Throwable t) {
            return this.handleError(type, value, t);
        }
    }

    /**
     * Handles conversion errors.
     * Returns the default value when one is configured; otherwise throws {@link ConversionException}.
     */
    protected Object handleError(final Class<?> type, final Object value, final Throwable cause) {
        if (this.useDefault) {
            return this.handleMissing(type);
        }
        if (cause instanceof ConversionException) {
            throw (ConversionException) cause;
        } else {
            String msg = "Error converting from '" + value.getClass() + "' to '" + type + "' " + cause.getMessage();
            throw new ConversionException(msg, cause);
        }
    }

    /**
     * Converts the object to a String representation.
     * Note: this method simply uses {@link Object#toString()}, and subclasses should override it for specialized conversion logic.
     */
    protected String convertToString(final Object value) throws Throwable {
        return value.toString();
    }

    /**Execute type conversion code. */
    protected abstract Object convertToType(Class<?> type, Object value) throws Throwable;

    /**
     * Return the first element from an Array (or Collection)
     * or the value unchanged if not an Array (or Collection).
     *
     * N.B. This needs to be overridden for array/Collection converters.
     *
     * @param value The value to convert
     * @return The first element in an Array (or Collection)
     * or the value unchanged if not an Array (or Collection)
     */
    protected Object convertArray(final Object value) {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray()) {
            if (Array.getLength(value) > 0) {
                return Array.get(value, 0);
            } else {
                return null;
            }
        }
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) value;
            if (collection.size() > 0) {
                return collection.iterator().next();
            } else {
                return null;
            }
        }
        return value;
    }

    /**Set Default */
    protected void setDefaultValue(final Object defaultValue) {
        this.useDefault = false;
        if (defaultValue == null) {
            this.defaultValue = null;
        } else {
            this.defaultValue = this.convert(this.getDefaultType(), defaultValue);
        }
        this.useDefault = true;
    }

    /**Get Defaults */
    protected abstract Class<?> getDefaultType();

    /**Returns the default value for the specified type. */
    protected Object getDefault(final Class<?> type) {
        if (type.equals(String.class)) {
            return null;
        } else {
            return this.defaultValue;
        }
    }

    /**
     * Provide a String representation of this converter.
     * @return A String representation of this converter
     */
    @Override
    public String toString() {
        return this.toString(this.getClass()) + "[UseDefault=" + this.useDefault + "]";
    }

    /**When empty values are passed in or returned empty */
    protected Object handleMissing(final Class<?> type) {
        if (this.useDefault || type.equals(String.class)) {
            Object value = this.getDefault(type);
            if (this.useDefault && value != null && !type.equals(value.getClass())) {
                try {
                    value = this.convertToType(type, this.defaultValue);
                } catch (Throwable t) {
                    //log().error("    Default conversion to " + toString(type) + "failed: " + t);// TODO Log
                }
            }
            return value;
        }
        return BeanUtils.getDefaultValue(type);
    }
    // ----------------------------------------------------------- Package Methods

    /**
     * Provide a String representation of a <code>java.lang.Class</code>.
     * @param type The <code>java.lang.Class</code>.
     * @return The String representation.
     */
    public String toString(final Class<?> type) {
        String typeName = null;
        if (type == null) {
            typeName = "null";
        } else if (type.isArray()) {
            Class<?> elementType = type.getComponentType();
            int count = 1;
            while (elementType.isArray()) {
                elementType = elementType.getComponentType();
                count++;
            }
            typeName = elementType.getName();
            for (int i = 0; i < count; i++) {
                typeName += "[]";
            }
        } else {
            typeName = type.getName();
        }
        /* org.more.convert.convert. */
        final String PACKAGE = AbstractConverter.class.getPackage().getName() + ".";
        if (typeName.startsWith("java.lang.") || typeName.startsWith("java.util.") || typeName.startsWith("java.math.")) {
            typeName = typeName.substring("java.lang.".length());
        } else if (typeName.startsWith(PACKAGE)) {
            typeName = typeName.substring(PACKAGE.length());
        }
        return typeName;
    }
}
