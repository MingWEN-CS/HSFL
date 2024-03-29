/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-03 Stephen Colebourne.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time;

/**
 * Defines the calculation engine for duration fields.
 * The interface defines a set of methods that manipulate a millisecond duration
 * with regards to a single field, such as months or seconds.
 * <p>
 * This design is extensible so, if you wish, you can extract a different field from
 * the millisecond duration. A number of standard implementations are provided to assist.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public interface DurationField extends Comparable {

    /**
     * Get the name of the field.
     * 
     * @return field name
     */
    String getName();

    /**
     * Returns true if this field is supported.
     * 
     * @return true if this field is supported
     */
    boolean isSupported();

    /**
     * Is this field precise. A precise field can calculate its value from
     * milliseconds without needing a reference date. Put another way, a
     * precise field's unit size is not variable.
     * 
     * @return true if precise
     * @see #getUnitMillis()
     */
    boolean isPrecise();
    
    /**
     * Returns the amount of milliseconds per unit value of this field. For
     * example, if this field represents "seconds", then this returns the
     * milliseconds in one second.
     * <p>
     * For imprecise fields, the unit size is variable, and so this method
     * returns a suitable average value.
     *
     * @return the unit size of this field, in milliseconds
     * @see #isPrecise()
     */
    long getUnitMillis();

    //------------------------------------------------------------------------
    /**
     * Get the value of this field from the milliseconds, which is approximate
     * if this field is imprecise.
     *
     * @param duration  the milliseconds to query, which may be negative
     * @return the value of the field, in the units of the field, which may be
     * negative
     * @throws ArithmeticException if the value is too large for an int
     */
    int getValue(long duration);

    /**
     * Get the value of this field from the milliseconds, which is approximate
     * if this field is imprecise.
     *
     * @param duration  the milliseconds to query, which may be negative
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    long getValueAsLong(long duration);

    /**
     * Get the value of this field from the milliseconds relative to an
     * instant. For precise fields this method produces the same result as for
     * the single argument get method.
     * <p>
     * If the millisecond duration is positive, then the instant is treated as a
     * "start instant". If negative, the instant is treated as an "end instant".
     * 
     * @param duration  the milliseconds to query, which may be negative
     * @param instant  the start instant to calculate relative to
     * @return the value of the field, in the units of the field, which may be
     * negative
     * @throws ArithmeticException if the value is too large for an int
     */
    int getValue(long duration, long instant);

    /**
     * Get the value of this field from the milliseconds relative to an
     * instant. For precise fields this method produces the same result as for
     * the single argument get method.
     * <p>
     * If the millisecond duration is positive, then the instant is treated as a
     * "start instant". If negative, the instant is treated as an "end instant".
     * 
     * @param duration  the milliseconds to query, which may be negative
     * @param instant  the start instant to calculate relative to
     * @return the value of the field, in the units of the field, which may be
     * negative
     */
    long getValueAsLong(long duration, long instant);

    //------------------------------------------------------------------------
    /**
     * Get the millisecond duration of this field from its value, which is
     * approximate if this field is imprecise.
     * 
     * @param value  the value of the field, which may be negative
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    long getMillis(int value);

    /**
     * Get the millisecond duration of this field from its value, which is
     * approximate if this field is imprecise.
     * 
     * @param value  the value of the field, which may be negative
     * @return the milliseconds that the field represents, which may be
     * negative
     */
    long getMillis(long value);

    /**
     * Get the millisecond duration of this field from its value relative to an
     * instant. For precise fields this method produces the same result as for
     * the single argument getMillis method.
     * <p>
     * If the value is positive, then the instant is treated as a "start
     * instant". If negative, the instant is treated as an "end instant".
     *
     * @param value  the value of the field, which may be negative
     * @param instant  the instant to calculate relative to
     * @return the millisecond duration that the field represents, which may be
     * negative
     */
    long getMillis(int value, long instant);

    /**
     * Get the millisecond duration of this field from its value relative to an
     * instant. For precise fields this method produces the same result as for
     * the single argument getMillis method.
     * <p>
     * If the value is positive, then the instant is treated as a "start
     * instant". If negative, the instant is treated as an "end instant".
     *
     * @param value  the value of the field, which may be negative
     * @param instant  the instant to calculate relative to
     * @return the millisecond duration that the field represents, which may be
     * negative
     */
    long getMillis(long value, long instant);

    /**
     * Adds a duration value (which may be negative) to the instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     */
    long add(long instant, int value);

    /**
     * Adds a duration value (which may be negative) to the instant.
     * 
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to add to
     * @param value  the value to add, in the units of the field
     * @return the updated milliseconds
     */
    long add(long instant, long value);

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long instant = ...
     * int v = ...
     * int age = getDifference(add(instant, v), instant);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     *
     * @param minuendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    int getDifference(long minuendInstant, long subtrahendInstant);

    /**
     * Computes the difference between two instants, as measured in the units
     * of this field. Any fractional units are dropped from the result. Calling
     * getDifference reverses the effect of calling add. In the following code:
     *
     * <pre>
     * long instant = ...
     * long v = ...
     * long age = getDifferenceAsLong(add(instant, v), instant);
     * </pre>
     *
     * The value 'age' is the same as the value 'v'.
     *
     * @param minuendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract from
     * @param subtrahendInstant the milliseconds from 1970-01-01T00:00:00Z to
     * subtract off the minuend
     * @return the difference in the units of this field
     */
    long getDifferenceAsLong(long minuendInstant, long subtrahendInstant);

    /**
     * Compares this duration field with another duration field for ascending
     * unit millisecond order. This ordering is inconsistent with equals, as it
     * ignores name and precision.
     *
     * @param durationField  a duration field to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object type is not supported
     */
    int compareTo(Object durationField);

    /**
     * Returns a localized unit name of this field, using the given value as an
     * aid. For example, the unit name may differ if it is plural.
     *
     * @param value the duration value to use for selecting a unit name
     * @param locale the locale to use for selecting a name, null for default
     */
    //String getUnitName(long value, Locale locale);

    /**
     * Returns a localized unit name of this field, using the given value as an
     * aid. For example, the unit name may differ if it is plural.
     *
     * @param value the duration value to use for selecting a unit name
     */
    //String getUnitName(long value);

    /**
     * Get the maximum length string returned by getUnitName.
     * 
     * @param locale the locale to use for selecting a unit name, null for
     * default
     * @return the maximum name length
     */
    //int getMaximumUnitNameLength(Locale locale);

    //------------------------------------------------------------------------
    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    String toString();
    
}
