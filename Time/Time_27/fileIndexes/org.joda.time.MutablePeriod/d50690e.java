/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
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

import java.io.Serializable;

import org.joda.time.base.BasePeriod;
import org.joda.time.field.FieldUtils;

/**
 * Standard mutable time period implementation.
 * <p>
 * MutablePeriod is mutable and not thread-safe, unless concurrent threads
 * are not invoking mutator methods.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 * @see Period
 */
public class MutablePeriod
        extends BasePeriod
        implements ReadWritablePeriod, Cloneable, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 3436451121567212165L;

    /**
     * Creates a zero-length period using AllType.
     */
    public MutablePeriod() {
        super(0L, null);
    }

    /**
     * Creates a zero-length period using the specified period type.
     *
     * @param type  which set of fields this period supports
     */
    public MutablePeriod(PeriodType type) {
        super(0L, type);
    }

    /**
     * Creates a period from the given millisecond duration using AllType.
     * <p>
     * Only precise fields in the period type will be used.
     * For AllType, this is the time fields only.
     * The year, month, week and day fields will not be populated.
     * The period constructed will always be precise.
     * <p>
     * If the duration is small, less than one day, then this method will perform
     * as you might expect and split the fields evenly.
     * <p>
     * If the duration is larger than one day then all the remaining duration will
     * be stored in the largest available precise field, hours in this case.
     * <p>
     * For example, a duration equal to (365 + 60 + 5) days will be converted to
     * ((365 + 60 + 5) * 24) hours by this constructor.
     * <p>
     * For more control over the conversion process, you have two options:
     * <ul>
     * <li>convert the duration to an {@link Interval}, and from there obtain the period
     * <li>specify a period type that contains precise definitions of the day and larger
     * fields, such as the UTC or precise types.
     * </ul>
     *
     * @param duration  the duration, in milliseconds
     */
    public MutablePeriod(long duration) {
        super(duration, null);
    }

    /**
     * Creates a period from the given millisecond duration.
     * <p>
     * Only precise fields in the period type will be used.
     * Imprecise fields will not be populated.
     * The period constructed will always be precise.
     * <p>
     * If the duration is small then this method will perform
     * as you might expect and split the fields evenly.
     * <p>
     * If the duration is large then all the remaining duration will
     * be stored in the largest available precise field.
     * For details as to which fields are precise, review the period type javadoc.
     *
     * @param duration  the duration, in milliseconds
     * @param type  which set of fields this duration supports
     */
    public MutablePeriod(long duration, PeriodType type) {
        super(duration, type);
    }

    /**
     * Create a period from a set of field values using AllType.
     * This constructor creates a precise period.
     *
     * @param hours  amount of hours in this period
     * @param minutes  amount of minutes in this period
     * @param seconds  amount of seconds in this period
     * @param millis  amount of milliseconds in this period
     */
    public MutablePeriod(int hours, int minutes, int seconds, int millis) {
        super(0, 0, 0, 0, hours, minutes, seconds, millis, null);
    }

    /**
     * Create a period from a set of field values using AllType.
     *
     * @param years  amount of years in this period
     * @param months  amount of months in this period
     * @param weeks  amount of weeks in this period
     * @param days  amount of days in this period
     * @param hours  amount of hours in this period
     * @param minutes  amount of minutes in this period
     * @param seconds  amount of seconds in this period
     * @param millis  amount of milliseconds in this period
     */
    public MutablePeriod(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, null);
    }

    /**
     * Create a period from a set of field values.
     *
     * @param years  amount of years in this period, which must be zero if unsupported
     * @param months  amount of months in this period, which must be zero if unsupported
     * @param weeks  amount of weeks in this period, which must be zero if unsupported
     * @param days  amount of days in this period, which must be zero if unsupported
     * @param hours  amount of hours in this period, which must be zero if unsupported
     * @param minutes  amount of minutes in this period, which must be zero if unsupported
     * @param seconds  amount of seconds in this period, which must be zero if unsupported
     * @param millis  amount of milliseconds in this period, which must be zero if unsupported
     * @param type  which set of fields this period supports, null means AllType
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public MutablePeriod(int years, int months, int weeks, int days,
                    int hours, int minutes, int seconds, int millis, PeriodType type) {
        super(years, months, weeks, days, hours, minutes, seconds, millis, type);
    }

    /**
     * Creates a period from the given interval endpoints using AllType.
     * This constructor creates a precise period.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public MutablePeriod(long startInstant, long endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Creates a period from the given interval endpoints.
     * This constructor creates a precise period.
     *
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     * @param type  which set of fields this period supports, null means AllType
     */
    public MutablePeriod(long startInstant, long endInstant, PeriodType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a period from the given interval endpoints using AllType.
     * This constructor creates a precise period.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     */
    public MutablePeriod(ReadableInstant startInstant, ReadableInstant endInstant) {
        super(startInstant, endInstant, null);
    }

    /**
     * Creates a period from the given interval endpoints.
     * This constructor creates a precise period.
     *
     * @param startInstant  interval start, null means now
     * @param endInstant  interval end, null means now
     * @param type  which set of fields this period supports, null means AllType
     */
    public MutablePeriod(ReadableInstant startInstant, ReadableInstant endInstant, PeriodType type) {
        super(startInstant, endInstant, type);
    }

    /**
     * Creates a period from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param period  period to convert
     * @throws IllegalArgumentException if period is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public MutablePeriod(Object period) {
        super(period, null);
    }

    /**
     * Creates a period from the specified object using the
     * {@link org.joda.time.convert.ConverterManager ConverterManager}.
     *
     * @param period  period to convert
     * @param type  which set of fields this period supports, null means use converter
     * @throws IllegalArgumentException if period is invalid
     * @throws UnsupportedOperationException if an unsupported field's value is non-zero
     */
    public MutablePeriod(Object period, PeriodType type) {
        super(period, type);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets all the fields in one go from another ReadablePeriod.
     * 
     * @param period  the period to set, null means zero length period
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public void setPeriod(ReadablePeriod period) {
        if (period == null) {
            setPeriod(0L);
        } else {
            setPeriod(
                period.getYears(), period.getMonths(),
                period.getWeeks(), period.getDays(),
                period.getHours(), period.getMinutes(),
                period.getSeconds(), period.getMillis());
        }
    }

    /**
     * Sets all the fields in one go.
     * 
     * @param years  amount of years in this period, which must be zero if unsupported
     * @param months  amount of months in this period, which must be zero if unsupported
     * @param weeks  amount of weeks in this period, which must be zero if unsupported
     * @param days  amount of days in this period, which must be zero if unsupported
     * @param hours  amount of hours in this period, which must be zero if unsupported
     * @param minutes  amount of minutes in this period, which must be zero if unsupported
     * @param seconds  amount of seconds in this period, which must be zero if unsupported
     * @param millis  amount of milliseconds in this period, which must be zero if unsupported
     * @throws IllegalArgumentException if an unsupported field's value is non-zero
     */
    public void setPeriod(int years, int months, int weeks, int days,
                            int hours, int minutes, int seconds, int millis) {
        super.setPeriod(years, months, weeks, days,
                          hours, minutes, seconds, millis);
    }

    /**
     * Sets all the fields in one go from an interval dividing the
     * fields using the period type.
     * 
     * @param interval  the interval to set, null means zero length
     */
    public void setPeriod(ReadableInterval interval) {
        if (interval == null) {
            setPeriod(0L);
        } else {
            setPeriod(interval.getStartMillis(), interval.getEndMillis());
        }
    }

    /**
     * Sets all the fields in one go from a millisecond interval dividing the
     * fields using the period type.
     * 
     * @param startInstant  interval start, in milliseconds
     * @param endInstant  interval end, in milliseconds
     */
    public void setPeriod(long startInstant, long endInstant) {
        super.setPeriod(startInstant, endInstant);
    }

    /**
     * Sets all the fields in one go from a duration dividing the
     * fields using the period type.
     * <p>
     * When dividing the duration, only precise fields in the period type will be used.
     * For large durations, all the remaining duration will be stored in the largest
     * available precise field.
     * 
     * @param duration  the duration to set, null means zero length
     */
    public void setPeriod(ReadableDuration duration) {
        long durationMillis = DateTimeUtils.getDurationMillis(duration);
        setPeriod(durationMillis);
    }

    /**
     * Sets all the fields in one go from a millisecond duration dividing the
     * fields using the period type.
     * <p>
     * When dividing the duration, only precise fields in the period type will be used.
     * For large durations, all the remaining duration will be stored in the largest
     * available precise field.
     * 
     * @param duration  the duration, in milliseconds
     */
    public void setPeriod(long duration) {
        super.setPeriod(duration);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Adds a period to this one by adding each field in turn.
     * 
     * @param period  the period to add, null means add nothing
     * @throws IllegalArgumentException if the period being added contains a field
     * not supported by this period
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(ReadablePeriod period) {
        if (period != null) {
            setPeriod(
                FieldUtils.safeAdd(getYears(), period.getYears()),
                FieldUtils.safeAdd(getMonths(), period.getMonths()),
                FieldUtils.safeAdd(getWeeks(), period.getWeeks()),
                FieldUtils.safeAdd(getDays(), period.getDays()),
                FieldUtils.safeAdd(getHours(), period.getHours()),
                FieldUtils.safeAdd(getMinutes(), period.getMinutes()),
                FieldUtils.safeAdd(getSeconds(), period.getSeconds()),
                FieldUtils.safeAdd(getMillis(), period.getMillis())
            );
        }
    }

    /**
     * Adds to each field of this period.
     * 
     * @param years  amount of years to add to this period, which must be zero if unsupported
     * @param months  amount of months to add to this period, which must be zero if unsupported
     * @param weeks  amount of weeks to add to this period, which must be zero if unsupported
     * @param days  amount of days to add to this period, which must be zero if unsupported
     * @param hours  amount of hours to add to this period, which must be zero if unsupported
     * @param minutes  amount of minutes to add to this period, which must be zero if unsupported
     * @param seconds  amount of seconds to add to this period, which must be zero if unsupported
     * @param millis  amount of milliseconds to add to this period, which must be zero if unsupported
     * @throws IllegalArgumentException if the period being added contains a field
     * not supported by this period
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(int years, int months, int weeks, int days,
                       int hours, int minutes, int seconds, int millis) {
        setPeriod(
            FieldUtils.safeAdd(getYears(), years),
            FieldUtils.safeAdd(getMonths(), months),
            FieldUtils.safeAdd(getWeeks(), weeks),
            FieldUtils.safeAdd(getDays(), days),
            FieldUtils.safeAdd(getHours(), hours),
            FieldUtils.safeAdd(getMinutes(), minutes),
            FieldUtils.safeAdd(getSeconds(), seconds),
            FieldUtils.safeAdd(getMillis(), millis)
        );
    }

    /**
     * Adds an interval to this one by dividing the interval into
     * fields and calling {@link #add(ReadablePeriod)}.
     * 
     * @param interval  the interval to add, null means add nothing
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(ReadableInterval interval) {
        if (interval != null) {
            add(interval.toPeriod(getPeriodType()));
        }
    }

    /**
     * Adds a duration to this one by dividing the duration into
     * fields and calling {@link #add(ReadablePeriod)}.
     * 
     * @param duration  the duration to add, null means add nothing
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(ReadableDuration duration) {
        if (duration != null) {
            add(new Period(duration.getMillis(), getPeriodType()));
        }
    }

    /**
     * Adds a millisecond duration to this one by dividing the duration into
     * fields and calling {@link #add(ReadablePeriod)}.
     * 
     * @param duration  the duration, in milliseconds
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void add(long duration) {
        add(new Period(duration, getPeriodType()));
    }

    /**
     * Normalizes all the field values in this period.
     * <p>
     * This method converts to a milliecond duration and back again.
     *
     * @throws IllegalStateException if this period is imprecise
     */
    public void normalize() {
        setPeriod(toDurationMillis());
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of years of the period.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setYears(int years) {
        super.setYears(years);
    }

    /**
     * Adds the specified years to the number of years in the period.
     * 
     * @param years  the number of years
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addYears(int years) {
        if (years != 0) {
            setYears(FieldUtils.safeAdd(getYears(), years));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of months of the period.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMonths(int months) {
        super.setMonths(months);
    }

    /**
     * Adds the specified months to the number of months in the period.
     * 
     * @param months  the number of months
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addMonths(int months) {
        if (months != 0) {
            setMonths(FieldUtils.safeAdd(getMonths(), months));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of weeks of the period.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setWeeks(int weeks) {
        super.setWeeks(weeks);
    }

    /**
     * Adds the specified weeks to the number of weeks in the period.
     * 
     * @param weeks  the number of weeks
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addWeeks(int weeks) {
        if (weeks != 0) {
            setWeeks(FieldUtils.safeAdd(getWeeks(), weeks));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of days of the period.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setDays(int days) {
        super.setDays(days);
    }

    /**
     * Adds the specified days to the number of days in the period.
     * 
     * @param days  the number of days
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addDays(int days) {
        if (days != 0) {
            setDays(FieldUtils.safeAdd(getDays(), days));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of hours of the period.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setHours(int hours) {
        super.setHours(hours);
    }

    /**
     * Adds the specified hours to the number of hours in the period.
     * 
     * @param hours  the number of hours
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addHours(int hours) {
        if (hours != 0) {
            setHours(FieldUtils.safeAdd(getHours(), hours));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of minutes of the period.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMinutes(int minutes) {
        super.setMinutes(minutes);
    }

    /**
     * Adds the specified minutes to the number of minutes in the period.
     * 
     * @param minutes  the number of minutes
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addMinutes(int minutes) {
        if (minutes != 0) {
            setMinutes(FieldUtils.safeAdd(getMinutes(), minutes));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of seconds of the period.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setSeconds(int seconds) {
        super.setSeconds(seconds);
    }

    /**
     * Adds the specified seconds to the number of seconds in the period.
     * 
     * @param seconds  the number of seconds
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addSeconds(int seconds) {
        if (seconds != 0) {
            setSeconds(FieldUtils.safeAdd(getSeconds(), seconds));
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of millis of the period.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     */
    public void setMillis(int millis) {
        super.setMillis(millis);
    }

    /**
     * Adds the specified millis to the number of millis in the period.
     * 
     * @param millis  the number of millis
     * @throws UnsupportedOperationException if field is not supported.
     * @throws ArithmeticException if the addition exceeds the capacity of the period
     */
    public void addMillis(int millis) {
        if (millis != 0) {
            setMillis(FieldUtils.safeAdd(getMillis(), millis));
        }
    }

    // Misc
    //-----------------------------------------------------------------------
    /**
     * Clone this object without having to cast the returned object.
     *
     * @return a clone of the this object.
     */
    public MutablePeriod copy() {
        return (MutablePeriod)clone();
    }

    /**
     * Clone this object.
     *
     * @return a clone of this object.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError("Clone error");
        }
    }

}
