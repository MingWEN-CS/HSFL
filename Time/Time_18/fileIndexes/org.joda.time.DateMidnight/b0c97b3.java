/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2005 Stephen Colebourne.  
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
import java.util.Locale;

import org.joda.time.base.BaseDateTime;
import org.joda.time.field.AbstractReadableInstantFieldProperty;

/**
 * DateMidnight defines a date where the time component is fixed at midnight.
 * The class uses a time zone, thus midnight is local unless a UTC time zone is used.
 * <p>
 * It is important to emphasise that this class represents the time of midnight on
 * any given day.
 * Note that midnight is defined as 00:00, which is at the very start of a day.
 * <p>
 * This class does not represent a day, but the millisecond instant at midnight.
 * If you need a class that represents the whole day, then an {@link Interval} or
 * a {@link YearMonthDay} may be more suitable.
 * <p>
 * This class uses a Chronology internally. The Chronology determines how the
 * millisecond instant value is converted into the date time fields.
 * The default Chronology is <code>ISOChronology</code> which is the agreed
 * international standard and compatable with the modern Gregorian calendar.
 *
 * <p>Each individual field can be queried in two ways:
 * <ul>
 * <li><code>getDayOfMonth()</code>
 * <li><code>dayOfMonth().get()</code>
 * </ul>
 * The second technique also provides access to other useful methods on the
 * field:
 * <ul>
 * <li>numeric value
 * <li>text value
 * <li>short text value
 * <li>maximum/minimum values
 * <li>add/subtract
 * <li>set
 * <li>rounding
 * </ul>
 *
 * <p>
 * DateMidnight is thread-safe and immutable, provided that the Chronology is as well.
 * All standard Chronology classes supplied are thread-safe and immutable.
 *
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class DateMidnight
        extends BaseDateTime
        implements ReadableDateTime, Serializable {
    
    /** Serialization lock */
    private static final long serialVersionUID = 156371964018738L;

    // Constructors
    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the current system millisecond time
     * using <code>ISOChronology</code> in the default time zone.
     * The constructed object will have a local time of midnight.
     */
    public DateMidnight() {
        super();
    }

    /**
     * Constructs an instance set to the current system millisecond time
     * using <code>ISOChronology</code> in the specified time zone.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the specified time zone is null, the default zone is used.
     *
     * @param zone  the time zone, null means default zone
     */
    public DateMidnight(DateTimeZone zone) {
        super(zone);
    }

    /**
     * Constructs an instance set to the current system millisecond time
     * using the specified chronology.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the chronology is null, <code>ISOChronology</code>
     * in the default time zone is used.
     *
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public DateMidnight(Chronology chronology) {
        super(chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z
     * using <code>ISOChronology</code> in the default time zone.
     * The constructed object will have a local time of midnight.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     */
    public DateMidnight(long instant) {
        super(instant);
    }

    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z
     * using <code>ISOChronology</code> in the specified time zone.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the specified time zone is null, the default zone is used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param zone  the time zone, null means default zone
     */
    public DateMidnight(long instant, DateTimeZone zone) {
        super(instant, zone);
    }

    /**
     * Constructs an instance set to the milliseconds from 1970-01-01T00:00:00Z
     * using the specified chronology.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the chronology is null, <code>ISOChronology</code>
     * in the default time zone is used.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public DateMidnight(long instant, Chronology chronology) {
        super(instant, chronology);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance from an Object that represents a datetime.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the object implies a chronology (such as GregorianCalendar does),
     * then that chronology will be used. Otherwise, ISO default is used.
     * Thus if a GregorianCalendar is passed in, the chronology used will
     * be GJ, but if a Date is passed in the chronology will be ISO.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, null means now
     * @throws IllegalArgumentException if the instant is invalid
     */
    public DateMidnight(Object instant) {
        super(instant, (Chronology) null);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * forcing the time zone to that specified.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the object implies a chronology (such as GregorianCalendar does),
     * then that chronology will be used, but with the time zone adjusted.
     * Otherwise, ISO is used in the specified time zone.
     * If the specified time zone is null, the default zone is used.
     * Thus if a GregorianCalendar is passed in, the chronology used will
     * be GJ, but if a Date is passed in the chronology will be ISO.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, null means now
     * @param zone  the time zone, null means default time zone
     * @throws IllegalArgumentException if the instant is invalid
     */
    public DateMidnight(Object instant, DateTimeZone zone) {
        super(instant, zone);
    }

    /**
     * Constructs an instance from an Object that represents a datetime,
     * using the specified chronology.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the chronology is null, ISO in the default time zone is used.
     * Any chronology implied by the object (such as GregorianCalendar does)
     * is ignored.
     * <p>
     * The recognised object types are defined in
     * {@link org.joda.time.convert.ConverterManager ConverterManager} and
     * include ReadableInstant, String, Calendar and Date.
     *
     * @param instant  the datetime object, null means now
     * @param chronology  the chronology, null means ISOChronology in default zone
     * @throws IllegalArgumentException if the instant is invalid
     */
    public DateMidnight(Object instant, Chronology chronology) {
        super(instant, DateTimeUtils.getChronology(chronology));
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance from datetime field values
     * using <code>ISOChronology</code> in the default time zone.
     * The constructed object will have a local time of midnight.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     */
    public DateMidnight(int year, int monthOfYear, int dayOfMonth) {
        super(year, monthOfYear, dayOfMonth, 0, 0, 0, 0);
    }

    /**
     * Constructs an instance from datetime field values
     * using <code>ISOChronology</code> in the specified time zone.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the specified time zone is null, the default zone is used.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param zone  the time zone, null means default time zone
     */
    public DateMidnight(int year, int monthOfYear, int dayOfMonth, DateTimeZone zone) {
        super(year, monthOfYear, dayOfMonth, 0, 0, 0, 0, zone);
    }

    /**
     * Constructs an instance from datetime field values
     * using the specified chronology.
     * The constructed object will have a local time of midnight.
     * <p>
     * If the chronology is null, <code>ISOChronology</code>
     * in the default time zone is used.
     *
     * @param year  the year
     * @param monthOfYear  the month of the year
     * @param dayOfMonth  the day of the month
     * @param chronology  the chronology, null means ISOChronology in default zone
     */
    public DateMidnight(int year, int monthOfYear, int dayOfMonth, Chronology chronology) {
        super(year, monthOfYear, dayOfMonth, 0, 0, 0, 0, chronology);
    }

    /**
     * Rounds the specified instant as required by the subclass.
     * This method must not access instance variables.
     * <p>
     * This implementation performs no rounding and returns the instant.
     *
     * @param instant  the milliseconds from 1970-01-01T00:00:00Z to round
     * @param chronology  the chronology to use, not null
     * @return the updated instant, rounded to midnight
     */
    protected long checkInstant(long instant, Chronology chronology) {
        return chronology.dayOfMonth().roundFloor(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this instant with different millis.
     * The returned object will have a local time of midnight.
     * <p>
     * Only the millis will change, the chronology and time zone are kept.
     * The returned object will be either be a new instance or <code>this</code>.
     *
     * @param newMillis  the new millis, from 1970-01-01T00:00:00Z
     * @return a copy of this instant with different millis
     */
    public DateMidnight withMillis(long newMillis) {
        Chronology chrono = getChronology();
        newMillis = checkInstant(newMillis, chrono);
        return (newMillis == getMillis() ? this : new DateMidnight(newMillis, chrono));
    }

    /**
     * Gets a copy of this instant with a different chronology, potentially
     * changing the day in unexpected ways.
     * <p>
     * This method creates a new DateMidnight using the midnight millisecond value
     * and the new chronology. If the same or similar chronology is specified, but
     * with a different time zone, the day may change. This occurs because the new
     * DateMidnight rounds down the millisecond value to get to midnight, and the
     * time zone change may result in a rounding down to a different day.
     * <p>
     * For example, changing time zone from London (+00:00) to Paris (+01:00) will
     * retain the same day, but changing from Paris to London will change the day.
     * (When its midnight in London its the same day in Paris, but when its midnight
     * in Paris its still the previous day in London)
     * <p>
     * To avoid these unusual effects, use {@link #withZoneRetainFields(DateTimeZone)}
     * to change time zones.
     *
     * @param newChronology  the new chronology
     * @return a copy of this instant with a different chronology
     */
    public DateMidnight withChronology(Chronology newChronology) {
        return (newChronology == getChronology() ? this : new DateMidnight(getMillis(), newChronology));
    }

    /**
     * Gets a copy of this instant with a different time zone, preserving the day
     * The returned object will have a local time of midnight in the new zone on
     * the same day as the original instant.
     *
     * @param newZone  the new time zone, null means default
     * @return a copy of this instant with a different time zone
     */
    public DateMidnight withZoneRetainFields(DateTimeZone newZone) {
        newZone = DateTimeUtils.getZone(newZone);
        DateTimeZone originalZone = DateTimeUtils.getZone(getZone());
        if (newZone == originalZone) {
            return this;
        }
        
        long millis = originalZone.getMillisKeepLocal(newZone, getMillis());
        return new DateMidnight(millis, getChronology().withZone(newZone));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the partial set of fields replacing those
     * from this instance.
     * <p>
     * For example, if the partial is a <code>YearMonthDay</code> then the date fields
     * would be changed in the returned instance.
     * If the partial is null, then <code>this</code> is returned.
     *
     * @param partial  the partial set of fields to apply to this datetime, null ignored
     * @return a copy of this datetime with a different set of fields
     * @throws IllegalArgumentException if any value is invalid
     */
    public DateMidnight withFields(ReadablePartial partial) {
        if (partial == null) {
            return this;
        }
        return withMillis(getChronology().set(partial, getMillis()));
    }

    /**
     * Gets a copy of this datetime with the specified field set to a new value.
     * <p>
     * For example, if the field type is <code>dayOfMonth</code> then the day of month
     * field would be changed in the returned instance.
     * If the field type is null, then <code>this</code> is returned.
     * <p>
     * These three lines are equivalent:
     * <pre>
     * DateTime updated = dt.withField(DateTimeFieldType.dayOfMonth(), 6);
     * DateTime updated = dt.dayOfMonth().setCopy(6);
     * DateTime updated = dt.property(DateTimeFieldType.dayOfMonth()).setCopy(6);
     * </pre>
     *
     * @param fieldType  the field type to set, not null
     * @param value  the value to set
     * @return a copy of this datetime with the field set
     * @throws IllegalArgumentException if the value is null or invalid
     */
    public DateMidnight withField(DateTimeFieldType fieldType, int value) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        long instant = fieldType.getField(getChronology()).set(getMillis(), value);
        return withMillis(instant);
    }

    /**
     * Gets a copy of this datetime with the value of the specified field increased.
     * <p>
     * If the addition is zero or the field is null, then <code>this</code> is returned.
     * These three lines are equivalent:
     * <pre>
     * DateTime added = dt.withFieldAdded(DateTimeFieldType.dayOfMonth(), 6);
     * DateTime added = dt.dayOfMonth().addToCopy(6);
     * DateTime added = dt.property(DateTimeFieldType.dayOfMonth()).addToCopy(6);
     * </pre>
     * 
     * @param fieldType  the field type to add to, not null
     * @param amount  the amount to add
     * @return a copy of this datetime with the field updated
     * @throws IllegalArgumentException if the value is null or invalid
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight withFieldAdded(DurationFieldType fieldType, int amount) {
        if (fieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (amount == 0) {
            return this;
        }
        long instant = fieldType.getField(getChronology()).add(getMillis(), amount);
        return withMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the specified duration added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * 
     * @param durationToAdd  the duration to add to this one
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this datetime with the duration added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight withDurationAdded(long durationToAdd, int scalar) {
        if (durationToAdd == 0 || scalar == 0) {
            return this;
        }
        long instant = getChronology().add(getMillis(), durationToAdd, scalar);
        return withMillis(instant);
    }

    /**
     * Gets a copy of this datetime with the specified duration added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * 
     * @param durationToAdd  the duration to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this datetime with the duration added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight withDurationAdded(ReadableDuration durationToAdd, int scalar) {
        if (durationToAdd == null || scalar == 0) {
            return this;
        }
        return withDurationAdded(durationToAdd.getMillis(), scalar);
    }

    /**
     * Gets a copy of this datetime with the specified period added.
     * <p>
     * If the addition is zero, then <code>this</code> is returned.
     * <p>
     * To add or subtract on a single field use the properties, for example:
     * <pre>
     * DateTime added = dt.dayOfMonth().addToCopy(6);
     * </pre>
     * 
     * @param period  the period to add to this one, null means zero
     * @param scalar  the amount of times to add, such as -1 to subtract once
     * @return a copy of this datetime with the period added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight withPeriodAdded(ReadablePeriod period, int scalar) {
        if (period == null || scalar == 0) {
            return this;
        }
        long instant = getChronology().add(period, getMillis(), scalar);
        return withMillis(instant);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the specified duration added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to add to this one
     * @return a copy of this datetime with the duration added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight plus(long duration) {
        return withDurationAdded(duration, 1);
    }

    /**
     * Gets a copy of this datetime with the specified duration added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to add to this one, null means zero
     * @return a copy of this datetime with the duration added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight plus(ReadableDuration duration) {
        return withDurationAdded(duration, 1);
    }

    /**
     * Gets a copy of this datetime with the specified period added.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * The following two lines are identical in effect:
     * <pre>
     * DateTime added = dt.hourOfDay().addToCopy(6);
     * DateTime added = dt.plus(Period.hours(6));
     * </pre>
     * 
     * @param period  the duration to add to this one, null means zero
     * @return a copy of this datetime with the period added
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight plus(ReadablePeriod period) {
        return withPeriodAdded(period, 1);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a copy of this datetime with the specified duration take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to reduce this instant by
     * @return a copy of this datetime with the duration taken away
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight minus(long duration) {
        return withDurationAdded(duration, -1);
    }

    /**
     * Gets a copy of this datetime with the specified duration take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * 
     * @param duration  the duration to reduce this instant by
     * @return a copy of this datetime with the duration taken away
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight minus(ReadableDuration duration) {
        return withDurationAdded(duration, -1);
    }

    /**
     * Gets a copy of this datetime with the specified period take away.
     * <p>
     * If the amount is zero or null, then <code>this</code> is returned.
     * <p>
     * The following two lines are identical in effect:
     * <pre>
     * DateTime added = dt.hourOfDay().addToCopy(-6);
     * DateTime added = dt.minus(Period.hours(6));
     * </pre>
     * 
     * @param period  the period to reduce this instant by
     * @return a copy of this datetime with the period taken away
     * @throws ArithmeticException if the new datetime exceeds the capacity of a long
     */
    public DateMidnight minus(ReadablePeriod period) {
        return withPeriodAdded(period, -1);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the property object for the specified type, which contains many useful methods.
     *
     * @param type  the field type to get the chronology for
     * @return the property object
     * @throws IllegalArgumentException if the field is null or unsupported
     */
    public Property property(DateTimeFieldType type) {
        if (type == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        DateTimeField field = type.getField(getChronology());
        if (field.isSupported() == false) {
            throw new IllegalArgumentException("Field '" + type + "' is not supported");
        }
        return new Property(this, field);
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a YearMonthDay using the same millis and chronology.
     * 
     * @return a YearMonthDay using the same millis and chronology
     */
    public YearMonthDay toYearMonthDay() {
        return new YearMonthDay(getMillis(), getChronology());
    }

    /**
     * Converts this object to an Interval encompassing the whole of this day.
     * <p>
     * The interval starts at midnight 00:00 and ends at 00:00 the following day,
     * (which is not included in the interval, as intervals are half-open).
     * 
     * @return an interval over the day
     */
    public Interval toInterval() {
        Chronology chrono = getChronology();
        long start = getMillis();
        long end = DurationFieldType.days().getField(chrono).add(start, 1);
        return new Interval(start, end, chrono);
    }

    // Date properties
    //-----------------------------------------------------------------------
    /**
     * Get the era property.
     * 
     * @return the era property
     */
    public Property era() {
        return new Property(this, getChronology().era());
    }

    /**
     * Get the century of era property.
     * 
     * @return the year of era property
     */
    public Property centuryOfEra() {
        return new Property(this, getChronology().centuryOfEra());
    }

    /**
     * Get the year of century property.
     * 
     * @return the year of era property
     */
    public Property yearOfCentury() {
        return new Property(this, getChronology().yearOfCentury());
    }

    /**
     * Get the year of era property.
     * 
     * @return the year of era property
     */
    public Property yearOfEra() {
        return new Property(this, getChronology().yearOfEra());
    }

    /**
     * Get the year property.
     * 
     * @return the year property
     */
    public Property year() {
        return new Property(this, getChronology().year());
    }

    /**
     * Get the year of a week based year property.
     * 
     * @return the year of a week based year property
     */
    public Property weekyear() {
        return new Property(this, getChronology().weekyear());
    }

    /**
     * Get the month of year property.
     * 
     * @return the month of year property
     */
    public Property monthOfYear() {
        return new Property(this, getChronology().monthOfYear());
    }

    /**
     * Get the week of a week based year property.
     * 
     * @return the week of a week based year property
     */
    public Property weekOfWeekyear() {
        return new Property(this, getChronology().weekOfWeekyear());
    }

    /**
     * Get the day of year property.
     * 
     * @return the day of year property
     */
    public Property dayOfYear() {
        return new Property(this, getChronology().dayOfYear());
    }

    /**
     * Get the day of month property.
     * 
     * @return the day of month property
     */
    public Property dayOfMonth() {
        return new Property(this, getChronology().dayOfMonth());
    }

    /**
     * Get the day of week property.
     * 
     * @return the day of week property
     */
    public Property dayOfWeek() {
        return new Property(this, getChronology().dayOfWeek());
    }

    //-----------------------------------------------------------------------
    /**
     * DateMidnight.Property binds a DateMidnight to a DateTimeField allowing powerful
     * datetime functionality to be easily accessed.
     * <p>
     * The simplest use of this class is as an alternative get method, here used to
     * get the year '1972' (as an int) and the month 'December' (as a String).
     * <pre>
     * DateMidnight dt = new DateMidnight(1972, 12, 3);
     * int year = dt.year().get();
     * String monthStr = dt.monthOfYear().getAsText();
     * </pre>
     * <p>
     * Methods are also provided that allow date modification. These return new instances
     * of DateMidnight - they do not modify the original. The example below yields two
     * independent immutable date objects 20 years apart.
     * <pre>
     * DateMidnight dt = new DateMidnight(1972, 12, 3);
     * DateMidnight dt20 = dt.year().addToCopy(20);
     * </pre>
     * Serious modification of dates (ie. more than just changing one or two fields)
     * should use the {@link org.joda.time.MutableDateTime MutableDateTime} class.
     * <p>
     * DateMidnight.Property itself is thread-safe and immutable.
     *
     * @author Stephen Colebourne
     * @author Brian S O'Neill
     * @since 1.0
     */
    public static final class Property extends AbstractReadableInstantFieldProperty {
    
        /** Serialization lock */
        private static final long serialVersionUID = 257629620L;
        
        /** The instant this property is working against */
        private final DateMidnight iInstant;
        /** The field this property is working against */
        private final DateTimeField iField;

        /**
         * Constructor.
         * 
         * @param instant  the instant to set
         * @param field  the field to use
         */
        Property(DateMidnight instant, DateTimeField field) {
            super();
            iInstant = instant;
            iField = field;
        }

        //-----------------------------------------------------------------------
        /**
         * Gets the field being used.
         * 
         * @return the field
         */
        public DateTimeField getField() {
            return iField;
        }

        /**
         * Gets the milliseconds of the datetime that this property is linked to.
         * 
         * @return the milliseconds
         */
        protected long getMillis() {
            return iInstant.getMillis();
        }

        /**
         * Gets the datetime being used.
         * 
         * @return the datetime
         */
        public DateMidnight getDateMidnight() {
            return iInstant;
        }

        //-----------------------------------------------------------------------
        /**
         * Adds to this field in a copy of this DateMidnight.
         * <p>
         * The DateMidnight attached to this property is unchanged by this call.
         * This operation is faster than converting a DateMidnight to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the DateMidnight with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DateMidnight addToCopy(int value) {
            return iInstant.withMillis(iField.add(iInstant.getMillis(), value));
        }

        /**
         * Adds to this field in a copy of this DateMidnight.
         * <p>
         * The DateMidnight attached to this property is unchanged by this call.
         * This operation is faster than converting a DateMidnight to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the DateMidnight with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DateMidnight addToCopy(long value) {
            return iInstant.withMillis(iField.add(iInstant.getMillis(), value));
        }

        /**
         * Adds to this field, possibly wrapped, in a copy of this DateMidnight.
         * A wrapped operation only changes this field.
         * Thus 31st January addWrapField one day goes to the 1st January.
         * <p>
         * The DateMidnight attached to this property is unchanged by this call.
         * This operation is faster than converting a DateMidnight to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param value  the value to add to the field in the copy
         * @return a copy of the DateMidnight with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DateMidnight addWrapFieldToCopy(int value) {
            return iInstant.withMillis(iField.addWrapField(iInstant.getMillis(), value));
        }

        //-----------------------------------------------------------------------
        /**
         * Sets this field in a copy of the DateMidnight.
         * <p>
         * The DateMidnight attached to this property is unchanged by this call.
         * This operation is faster than converting a DateMidnight to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param value  the value to set the field in the copy to
         * @return a copy of the DateMidnight with the field value changed
         * @throws IllegalArgumentException if the value isn't valid
         */
        public DateMidnight setCopy(int value) {
            return iInstant.withMillis(iField.set(iInstant.getMillis(), value));
        }
    
        /**
         * Sets this field in a copy of the DateMidnight to a parsed text value.
         * <p>
         * The DateMidnight attached to this property is unchanged by this call.
         * This operation is faster than converting a DateMidnight to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param text  the text value to set
         * @param locale  optional locale to use for selecting a text symbol
         * @return a copy of the DateMidnight with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public DateMidnight setCopy(String text, Locale locale) {
            return iInstant.withMillis(iField.set(iInstant.getMillis(), text, locale));
        }

        /**
         * Sets this field in a copy of the DateMidnight to a parsed text value.
         * <p>
         * The DateMidnight attached to this property is unchanged by this call.
         * This operation is faster than converting a DateMidnight to a MutableDateTime
         * and back again when setting one field. When setting multiple fields,
         * it is generally quicker to make the conversion to MutableDateTime.
         * 
         * @param text  the text value to set
         * @return a copy of the DateMidnight with the field value changed
         * @throws IllegalArgumentException if the text value isn't valid
         */
        public DateMidnight setCopy(String text) {
            return setCopy(text, null);
        }

        //-----------------------------------------------------------------------
        /**
         * Rounds to the lowest whole unit of this field on a copy of this DateMidnight.
         *
         * @return a copy of the DateMidnight with the field value changed
         */
        public DateMidnight roundFloorCopy() {
            return iInstant.withMillis(iField.roundFloor(iInstant.getMillis()));
        }

        /**
         * Rounds to the highest whole unit of this field on a copy of this DateMidnight.
         *
         * @return a copy of the DateMidnight with the field value changed
         */
        public DateMidnight roundCeilingCopy() {
            return iInstant.withMillis(iField.roundCeiling(iInstant.getMillis()));
        }

        /**
         * Rounds to the nearest whole unit of this field on a copy of this DateMidnight,
         * favoring the floor if halfway.
         *
         * @return a copy of the DateMidnight with the field value changed
         */
        public DateMidnight roundHalfFloorCopy() {
            return iInstant.withMillis(iField.roundHalfFloor(iInstant.getMillis()));
        }

        /**
         * Rounds to the nearest whole unit of this field on a copy of this DateMidnight,
         * favoring the ceiling if halfway.
         *
         * @return a copy of the DateMidnight with the field value changed
         */
        public DateMidnight roundHalfCeilingCopy() {
            return iInstant.withMillis(iField.roundHalfCeiling(iInstant.getMillis()));
        }

        /**
         * Rounds to the nearest whole unit of this field on a copy of this DateMidnight.
         * If halfway, the ceiling is favored over the floor only if it makes this field's value even.
         *
         * @return a copy of the DateMidnight with the field value changed
         */
        public DateMidnight roundHalfEvenCopy() {
            return iInstant.withMillis(iField.roundHalfEven(iInstant.getMillis()));
        }

    }
}
