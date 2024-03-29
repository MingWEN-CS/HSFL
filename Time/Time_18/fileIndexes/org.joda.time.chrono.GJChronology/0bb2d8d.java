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
package org.joda.time.chrono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.Chronology;
// Import for @link support
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.Instant;
import org.joda.time.ReadableInstant;
import org.joda.time.field.AbstractDateTimeField;
import org.joda.time.field.DecoratedDurationField;
import org.joda.time.format.DateTimePrinter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Implements the Gregorian/Julian calendar system which is the calendar system
 * used in most of the world. Wherever possible, it is recommended to use the
 * {@link ISOChronology} instead.
 * <p>
 * The Gregorian calendar replaced the Julian calendar, and the point in time
 * when this chronology switches can be controlled using the second parameter
 * of the getInstance method. By default this cutover is set to the date the
 * Gregorian calendar was first instituted, October 15, 1582.
 * <p>
 * Before this date, this chronology uses the proleptic Julian calendar
 * (proleptic means extending indefinitely). The Julian calendar has leap years
 * every four years, whereas the Gregorian has special rules for 100 and 400
 * years. A meaningful result will thus be obtained for all input values.
 * However before 8 CE, Julian leap years were irregular, and before 45 BCE
 * there was no Julian calendar.
 * <p>
 * This chronology differs from
 * {@link java.util.GregorianCalendar GregorianCalendar} in that years
 * in BCE are returned correctly. Thus year 1 BCE is returned as -1 instead of 1.
 * The yearOfEra field produces results compatible with GregorianCalendar.
 * <p>
 * The Julian calendar does not have a year zero, and so year -1 is followed by
 * year 1. If the Gregorian cutover date is specified at or before year -1
 * (Julian), year zero is defined. In other words, the proleptic Gregorian
 * chronology used by this class has a year zero.
 * <p>
 * To create a pure proleptic Julian chronology, use {@link JulianChronology},
 * and to create a pure proleptic Gregorian chronology, use
 * {@link GregorianChronology}.
 * <p>
 * GJChronology is thread-safe and immutable.
 * 
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class GJChronology extends AssembledChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -2545574827706931671L;

    /**
     * Convert a datetime from one chronology to another.
     */
    private static long convertByYear(long instant, Chronology from, Chronology to) {
        return to.getDateTimeMillis
            (from.year().get(instant),
             from.monthOfYear().get(instant),
             from.dayOfMonth().get(instant),
             from.millisOfDay().get(instant));
    }

    /**
     * Convert a datetime from one chronology to another.
     */
    private static long convertByWeekyear(final long instant, Chronology from, Chronology to) {
        long newInstant;
        newInstant = to.weekyear().set(0, from.weekyear().get(instant));
        newInstant = to.weekOfWeekyear().set(newInstant, from.weekOfWeekyear().get(instant));
        newInstant = to.dayOfWeek().set(newInstant, from.dayOfWeek().get(instant));
        newInstant = to.millisOfDay().set(newInstant, from.millisOfDay().get(instant));
        return newInstant;
    }

    /**
     * The default GregorianJulian cutover point
     */
    static final Instant DEFAULT_CUTOVER = new Instant(-12219292800000L);

    /** Cache of zone to chronology list */
    private static final Map cCache = new HashMap();

    /**
     * Factory method returns instances of the default GJ cutover
     * chronology. This uses a cutover date of October 15, 1582 (Gregorian)
     * 00:00:00 UTC. For this value, October 4, 1582 (Julian) is followed by
     * October 15, 1582 (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * <p>The time zone of the returned instance is UTC.
     */
    public static GJChronology getInstanceUTC() {
        return getInstance(DateTimeZone.UTC, DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the default GJ cutover
     * chronology. This uses a cutover date of October 15, 1582 (Gregorian)
     * 00:00:00 UTC. For this value, October 4, 1582 (Julian) is followed by
     * October 15, 1582 (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * <p>The returned chronology is in the default time zone.
     */
    public static GJChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. This uses
     * a cutover date of October 15, 1582 (Gregorian) 00:00:00 UTC. For this
     * value, October 4, 1582 (Julian) is followed by October 15, 1582
     * (Gregorian).
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     */
    public static GJChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, DEFAULT_CUTOVER, 4);
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * <p>The first day of the week is designated to be
     * {@link DateTimeConstants#MONDAY Monday}, and the minimum days in the
     * first week of the year is 4.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use, null means default
     */
    public static GJChronology getInstance(DateTimeZone zone,
                                           ReadableInstant gregorianCutover) 
    {
        return getInstance(zone, gregorianCutover, 4);
    }
    
    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use, null means default
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     */
    public static synchronized GJChronology getInstance(DateTimeZone zone,
                                                        ReadableInstant gregorianCutover,
                                                        int minDaysInFirstWeek)
    {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        Instant cutoverInstant;
        if (gregorianCutover == null) {
            cutoverInstant = DEFAULT_CUTOVER;
        } else {
            cutoverInstant = gregorianCutover.toInstant();
        }

        GJChronology chrono;

        ArrayList chronos = (ArrayList)cCache.get(zone);
        if (chronos == null) {
            chronos = new ArrayList(2);
            cCache.put(zone, chronos);
        } else {
            for (int i=chronos.size(); --i>=0; ) {
                chrono = (GJChronology)chronos.get(i);
                if (minDaysInFirstWeek == chrono.getMinimumDaysInFirstWeek() &&
                    cutoverInstant.equals(chrono.getGregorianCutover())) {
                    
                    return chrono;
                }
            }
        }

        if (zone == DateTimeZone.UTC) {
            chrono = new GJChronology
                (JulianChronology.getInstance(zone, minDaysInFirstWeek),
                 GregorianChronology.getInstance(zone, minDaysInFirstWeek),
                 cutoverInstant);
        } else {
            chrono = getInstance(DateTimeZone.UTC, cutoverInstant, minDaysInFirstWeek);
            chrono = new GJChronology
                (ZonedChronology.getInstance(chrono, zone),
                 chrono.iJulianChronology,
                 chrono.iGregorianChronology,
                 chrono.iCutoverInstant);
        }

        chronos.add(chrono);

        return chrono;
    }

    /**
     * Factory method returns instances of the GJ cutover chronology. Any
     * cutover date may be specified.
     *
     * @param zone  the time zone to use, null is default
     * @param gregorianCutover  the cutover to use
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     */
    public static synchronized GJChronology getInstance(DateTimeZone zone,
                                                        long gregorianCutover,
                                                        int minDaysInFirstWeek)
    {
        Instant cutoverInstant;
        if (gregorianCutover == DEFAULT_CUTOVER.getMillis()) {
            cutoverInstant = null;
        } else {
            cutoverInstant = new Instant(gregorianCutover);
        }
        return getInstance(zone, cutoverInstant, minDaysInFirstWeek);
    }

    private JulianChronology iJulianChronology;
    private GregorianChronology iGregorianChronology;
    private Instant iCutoverInstant;

    long iCutoverMillis;
    long iGapDuration;

    /**
     * @param julian chronology used before the cutover instant
     * @param gregorian chronology used at and after the cutover instant
     * @param cutoverInstant instant when the gregorian chronology began
     */
    private GJChronology(JulianChronology julian,
                         GregorianChronology gregorian,
                         Instant cutoverInstant) {
        super(null, new Object[] {julian, gregorian, cutoverInstant});
    }

    /**
     * Called when applying a time zone.
     */
    private GJChronology(Chronology base,
                         JulianChronology julian,
                         GregorianChronology gregorian,
                         Instant cutoverInstant) {
        super(base, new Object[] {julian, gregorian, cutoverInstant});
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        return getInstance(getDateTimeZone(), iCutoverInstant, getMinimumDaysInFirstWeek());
    }

    public DateTimeZone getDateTimeZone() {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeZone();
        }
        return DateTimeZone.UTC;
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return withDateTimeZone(DateTimeZone.UTC);
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withDateTimeZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getDateTimeZone()) {
            return this;
        }
        return getInstance(zone, iCutoverInstant, getMinimumDaysInFirstWeek());
    }

    public long getDateOnlyMillis(int year, int monthOfYear, int dayOfMonth)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateOnlyMillis(year, monthOfYear, dayOfMonth);
        }

        return getDateTimeMillis(year, monthOfYear, dayOfMonth, 0);
    }

    public long getTimeOnlyMillis(int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }

        // Time fields are same for Julian and Gregorian.
        return iGregorianChronology.getTimeOnlyMillis
            (hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, millisOfDay);
        }

        // Assume date is Gregorian.
        long instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth, millisOfDay);
        if (instant < iCutoverMillis) {
            // Maybe it's Julian.
            instant = iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth, millisOfDay);
            if (instant >= iCutoverMillis) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
        return instant;
    }

    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis
                (instant, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }

        return getDateOnlyMillis(instant)
            + getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        Chronology base;
        if ((base = getBase()) != null) {
            return base.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }

        return getDateTimeMillis(year, monthOfYear, dayOfMonth, 0)
            + getTimeOnlyMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    /**
     * Gets the cutover instant between Gregorian and Julian chronologies.
     * @return the cutover instant
     */
    public Instant getGregorianCutover() {
        return iCutoverInstant;
    }

    public final int getMinimumDaysInFirstWeek() {
        return iGregorianChronology.getMinimumDaysInFirstWeek();
    }

    // Output
    //-----------------------------------------------------------------------
    /**
     * Gets a debugging toString.
     * 
     * @return a debugging string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(60);
        sb.append("GJCutoverChronology");
        sb.append('[');
        sb.append(getDateTimeZone().getID());
        sb.append(", ");

        sb.append("cutover=");
        ISODateTimeFormat format = ISODateTimeFormat.getInstance(withUTC());
        DateTimePrinter printer;
        if (withUTC().getTimeOnlyMillis(iCutoverMillis) == 0) {
            printer = format.date();
        } else {
            printer = format.dateTime();
        }
        printer.printTo(sb, iCutoverMillis);

        sb.append(", mdfw=");
        sb.append(getMinimumDaysInFirstWeek());
        sb.append(']');

        return sb.toString();
    }

    protected void assemble(Fields fields) {
        Object[] params = (Object[])getParam();

        JulianChronology julian = (JulianChronology)params[0];
        GregorianChronology gregorian = (GregorianChronology)params[1];
        Instant cutoverInstant = (Instant)params[2];
        iCutoverMillis = cutoverInstant.getMillis();

        iJulianChronology = julian;
        iGregorianChronology = gregorian;
        iCutoverInstant = cutoverInstant;

        if (getBase() != null) {
            return;
        }

        if (julian.getMinimumDaysInFirstWeek() != gregorian.getMinimumDaysInFirstWeek()) {
            throw new IllegalArgumentException();
        }

        // Compute difference between the chronologies at the cutover instant
        iGapDuration = iCutoverMillis - julianToGregorianByYear(iCutoverMillis);

        // Begin field definitions.

        // First just copy all the Gregorian fields and then override those
        // that need special attention.
        fields.copyFieldsFrom(gregorian);
        
        // Assuming cutover is at midnight, all time of day fields can be
        // gregorian since they are unaffected by cutover.

        // Verify assumption.
        if (gregorian.millisOfDay().get(iCutoverMillis) == 0) {
            // Cutover is sometime in the day, so cutover fields are required
            // for time of day.

            fields.millisOfSecond = new CutoverField(julian.millisOfSecond(), fields.millisOfSecond);
            fields.millisOfDay = new CutoverField(julian.millisOfDay(), fields.millisOfDay);
            fields.secondOfMinute = new CutoverField(julian.secondOfMinute(), fields.secondOfMinute);
            fields.secondOfDay = new CutoverField(julian.secondOfDay(), fields.secondOfDay);
            fields.minuteOfHour = new CutoverField(julian.minuteOfHour(), fields.minuteOfHour);
            fields.minuteOfDay = new CutoverField(julian.minuteOfDay(), fields.minuteOfDay);
            fields.hourOfDay = new CutoverField(julian.hourOfDay(), fields.hourOfDay);
            fields.hourOfHalfday = new CutoverField(julian.hourOfHalfday(), fields.hourOfHalfday);
            fields.clockhourOfDay = new CutoverField(julian.clockhourOfDay(), fields.clockhourOfDay);
            fields.clockhourOfHalfday = new CutoverField(julian.clockhourOfHalfday(),
                                                         fields.clockhourOfHalfday);
            fields.halfdayOfDay = new CutoverField(julian.halfdayOfDay(), fields.halfdayOfDay);
        }

        // These fields just require basic cutover support.
        {
            fields.era = new CutoverField(julian.era(), fields.era);
            fields.dayOfMonth = new CutoverField(julian.dayOfMonth(), fields.dayOfMonth);
        }

        // DayOfYear and weekOfWeekyear require special handling since cutover
        // year has fewer days and weeks. Extend the cutover to the start of
        // the next year or weekyear. This keeps the sequence unbroken during
        // the cutover year.

        {
            long cutover = gregorian.year().roundCeiling(iCutoverMillis);
            fields.dayOfYear = new CutoverField
                (julian.dayOfYear(), fields.dayOfYear, cutover);
        }

        {
            long cutover = gregorian.weekyear().roundCeiling(iCutoverMillis);
            fields.weekOfWeekyear = new CutoverField
                (julian.weekOfWeekyear(), fields.weekOfWeekyear, cutover, true);
        }

        // These fields are special because they have imprecise durations. The
        // family of addition methods need special attention. Override affected
        // duration fields as well.
        {
            fields.year = new ImpreciseCutoverField(julian.year(), fields.year);
            fields.years = fields.year.getDurationField();
            fields.yearOfEra = new ImpreciseCutoverField
                (julian.yearOfEra(), fields.yearOfEra, fields.years);
            fields.yearOfCentury = new ImpreciseCutoverField
                (julian.yearOfCentury(), fields.yearOfCentury, fields.years);
            
            fields.centuryOfEra = new ImpreciseCutoverField
                (julian.centuryOfEra(), fields.centuryOfEra);
            fields.centuries = fields.centuryOfEra.getDurationField();
            
            fields.monthOfYear = new ImpreciseCutoverField
                (julian.monthOfYear(), fields.monthOfYear);
            fields.months = fields.monthOfYear.getDurationField();
            
            fields.weekyear = new ImpreciseCutoverField(julian.weekyear(), fields.weekyear, true);
            fields.weekyears = fields.weekyear.getDurationField();
        }
    }

    long julianToGregorianByYear(long instant) {
        return convertByYear(instant, iJulianChronology, iGregorianChronology);
    }

    long gregorianToJulianByYear(long instant) {
        return convertByYear(instant, iGregorianChronology, iJulianChronology);
    }

    long julianToGregorianByWeekyear(long instant) {
        return convertByWeekyear(instant, iJulianChronology, iGregorianChronology);
    }

    long gregorianToJulianByWeekyear(long instant) {
        return convertByWeekyear(instant, iGregorianChronology, iJulianChronology);
    }

    /**
     * This basic cutover field adjusts calls to 'get' and 'set' methods, and
     * assumes that calls to add and addWrapped are unaffected by the cutover.
     */
    private class CutoverField extends AbstractDateTimeField {
        static final long serialVersionUID = 3528501219481026402L;

        final DateTimeField iJulianField;
        final DateTimeField iGregorianField;
        final long iCutover;
        final boolean iConvertByWeekyear;

        protected DurationField iDurationField;

        /**
         * @param julianField field from the chronology used before the cutover instant
         * @param gregorianField field from the chronology used at and after the cutover
         */
        CutoverField(DateTimeField julianField, DateTimeField gregorianField) {
            this(julianField, gregorianField, iCutoverMillis, false);
        }

        /**
         * @param julianField field from the chronology used before the cutover instant
         * @param gregorianField field from the chronology used at and after the cutover
         * @param convertByWeekyear
         */
        CutoverField(DateTimeField julianField, DateTimeField gregorianField, boolean convertByWeekyear) {
            this(julianField, gregorianField, iCutoverMillis, convertByWeekyear);
        }

        CutoverField(DateTimeField julianField, DateTimeField gregorianField, long cutoverMillis) {
            this(julianField, gregorianField, cutoverMillis, false);
        }

        CutoverField(DateTimeField julianField, DateTimeField gregorianField,
                     long cutoverMillis, boolean convertByWeekyear) {
            super(gregorianField.getName());
            iJulianField = julianField;
            iGregorianField = gregorianField;
            iCutover = cutoverMillis;
            iConvertByWeekyear = convertByWeekyear;
            // Although average length of Julian and Gregorian years differ,
            // use the Gregorian duration field because it is more accurate.
            iDurationField = gregorianField.getDurationField();
        }

        public boolean isLenient() {
            return false;
        }

        public int get(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.get(instant);
            } else {
                return iJulianField.get(instant);
            }
        }

        public String getAsText(long instant, Locale locale) {
            if (instant >= iCutover) {
                return iGregorianField.getAsText(instant, locale);
            } else {
                return iJulianField.getAsText(instant, locale);
            }
        }

        public String getAsShortText(long instant, Locale locale) {
            if (instant >= iCutover) {
                return iGregorianField.getAsShortText(instant, locale);
            } else {
                return iJulianField.getAsShortText(instant, locale);
            }
        }

        public long add(long instant, int value) {
            return iGregorianField.add(instant, value);
        }

        public long add(long instant, long value) {
            return iGregorianField.add(instant, value);
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }

        public long set(long instant, int value) {
            if (instant >= iCutover) {
                instant = iGregorianField.set(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                    // Verify that new value stuck.
                    if (get(instant) != value) {
                        throw new IllegalArgumentException
                            ("Illegal value for " + iGregorianField.getName() + ": " + value);
                    }
                }
            } else {
                instant = iJulianField.set(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                    // Verify that new value stuck.
                    if (get(instant) != value) {
                        throw new IllegalArgumentException
                            ("Illegal value for " + iJulianField.getName() + ": " + value);
                    }
                }
            }
            return instant;
        }

        public long set(long instant, String text, Locale locale) {
            if (instant >= iCutover) {
                instant = iGregorianField.set(instant, text, locale);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                    // Cannot verify that new value stuck because set may be lenient.
                }
            } else {
                instant = iJulianField.set(instant, text, locale);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                    // Cannot verify that new value stuck because set may be lenient.
                }
            }
            return instant;
        }

        public DurationField getDurationField() {
            return iDurationField;
        }

        public DurationField getRangeDurationField() {
            DurationField rangeField = iGregorianField.getRangeDurationField();
            if (rangeField == null) {
                rangeField = iJulianField.getRangeDurationField();
            }
            return rangeField;
        }

        public boolean isLeap(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.isLeap(instant);
            } else {
                return iJulianField.isLeap(instant);
            }
        }

        public int getLeapAmount(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getLeapAmount(instant);
            } else {
                return iJulianField.getLeapAmount(instant);
            }
        }

        public DurationField getLeapDurationField() {
            return iGregorianField.getLeapDurationField();
        }


        public int getMinimumValue() {
            // For all precise fields, the Julian and Gregorian limits are
            // identical. Choose Julian to tighten up the year limits.
            return iJulianField.getMinimumValue();
        }
        
        public int getMinimumValue(long instant) {
            if (instant < iCutover) {
                return iJulianField.getMinimumValue(instant);
            }

            int min = iGregorianField.getMinimumValue(instant);

            // Because the cutover may reduce the length of this field, verify
            // the minimum by setting it.
            instant = iGregorianField.set(instant, min);
            if (instant < iCutover) {
                min = iGregorianField.get(iCutover);
            }

            return min;
        }

        public int getMaximumValue() {
            // For all precise fields, the Julian and Gregorian limits are
            // identical.
            return iGregorianField.getMaximumValue();
        }

        public int getMaximumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMaximumValue(instant);
            }

            int max = iJulianField.getMaximumValue(instant);

            // Because the cutover may reduce the length of this field, verify
            // the maximum by setting it.
            instant = iJulianField.set(instant, max);
            if (instant >= iCutover) {
                max = iJulianField.get(iJulianField.add(iCutover, -1));
            }

            return max;
        }

        public long roundFloor(long instant) {
            if (instant >= iCutover) {
                instant = iGregorianField.roundFloor(instant);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.roundFloor(instant);
            }
            return instant;
        }

        public long roundCeiling(long instant) {
            if (instant >= iCutover) {
                instant = iGregorianField.roundCeiling(instant);
            } else {
                instant = iJulianField.roundCeiling(instant);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }

        public int getMaximumTextLength(Locale locale) {
            return Math.max(iJulianField.getMaximumTextLength(locale),
                            iGregorianField.getMaximumTextLength(locale));
        }

        public int getMaximumShortTextLength(Locale locale) {
            return Math.max(iJulianField.getMaximumShortTextLength(locale),
                            iGregorianField.getMaximumShortTextLength(locale));
        }

        protected long julianToGregorian(long instant) {
            if (iConvertByWeekyear) {
                return julianToGregorianByWeekyear(instant);
            } else {
                return julianToGregorianByYear(instant);
            }
        }

        protected long gregorianToJulian(long instant) {
            if (iConvertByWeekyear) {
                return gregorianToJulianByWeekyear(instant);
            } else {
                return gregorianToJulianByYear(instant);
            }
        }
    }

    /**
     * Cutover field for variable length fields. These fields internally call
     * set whenever add is called. As a result, the same correction applied to
     * set must be applied to add and addWrapped. Knowing when to use this
     * field requires specific knowledge of how the GJ fields are implemented.
     */
    private final class ImpreciseCutoverField extends CutoverField {
        static final long serialVersionUID = 3410248757173576441L;

        /**
         * Creates a duration field that links back to this.
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField) {
            this(julianField, gregorianField, null, false);
        }

        /**
         * Creates a duration field that links back to this.
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField,
                              boolean convertByWeekyear) {
            this(julianField, gregorianField, null, convertByWeekyear);
        }

        /**
         * Uses a shared duration field rather than creating a new one.
         *
         * @param durationField shared duration field
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField,
                              DurationField durationField)
        {
            this(julianField, gregorianField, durationField, false);
        }

        /**
         * Uses a shared duration field rather than creating a new one.
         *
         * @param durationField shared duration field
         */
        ImpreciseCutoverField(DateTimeField julianField, DateTimeField gregorianField,
                              DurationField durationField, boolean convertByWeekyear)
        {
            super(julianField, gregorianField, convertByWeekyear);
            if (durationField == null) {
                durationField = new LinkedDurationField(iDurationField, this);
            }
            iDurationField = durationField;
        }

        public long add(long instant, int value) {
            if (instant >= iCutover) {
                instant = iGregorianField.add(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.add(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }
        
        public long add(long instant, long value) {
            if (instant >= iCutover) {
                instant = iGregorianField.add(instant, value);
                if (instant < iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant + iGapDuration < iCutover) {
                        instant = gregorianToJulian(instant);
                    }
                }
            } else {
                instant = iJulianField.add(instant, value);
                if (instant >= iCutover) {
                    // Only adjust if gap fully crossed.
                    if (instant - iGapDuration >= iCutover) {
                        instant = julianToGregorian(instant);
                    }
                }
            }
            return instant;
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            if (minuendInstant >= iCutover) {
                if (subtrahendInstant >= iCutover) {
                    return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Julian, convert minuend to Julian to match.
                minuendInstant = gregorianToJulian(minuendInstant);
                return iJulianField.getDifference(minuendInstant, subtrahendInstant);
            } else {
                if (subtrahendInstant < iCutover) {
                    return iJulianField.getDifference(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Gregorian, convert minuend to Gregorian to match.
                minuendInstant = julianToGregorian(minuendInstant);
                return iGregorianField.getDifference(minuendInstant, subtrahendInstant);
            }
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            if (minuendInstant >= iCutover) {
                if (subtrahendInstant >= iCutover) {
                    return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Julian, convert minuend to Julian to match.
                minuendInstant = gregorianToJulian(minuendInstant);
                return iJulianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
            } else {
                if (subtrahendInstant < iCutover) {
                    return iJulianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
                }
                // Remember, the add is being reversed. Since subtrahend is
                // Gregorian, convert minuend to Gregorian to match.
                minuendInstant = julianToGregorian(minuendInstant);
                return iGregorianField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
            }
        }

        // Since the imprecise fields have durations longer than the gap
        // duration, keep these methods simple. The inherited implementations
        // produce incorrect results.
        //
        // Degenerate case: If this field is a month, and the cutover is set
        // far into the future, then the gap duration may be so large as to
        // reduce the number of months in a year. If the missing month(s) are
        // at the beginning or end of the year, then the minimum and maximum
        // values are not 1 and 12. I don't expect this case to ever occur.

        public int getMinimumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMinimumValue(instant);
            } else {
                return iJulianField.getMinimumValue(instant);
            }
        }

        public int getMaximumValue(long instant) {
            if (instant >= iCutover) {
                return iGregorianField.getMaximumValue(instant);
            } else {
                return iJulianField.getMaximumValue(instant);
            }
        }
    }

    /**
     * Links the duration back to a ImpreciseCutoverField.
     */
    private static class LinkedDurationField extends DecoratedDurationField {
        static final long serialVersionUID = 4097975388007713084L;

        private final ImpreciseCutoverField iField;

        LinkedDurationField(DurationField durationField, ImpreciseCutoverField dateTimeField) {
            super(durationField, durationField.getName());
            iField = dateTimeField;
        }

        public long add(long instant, int value) {
            return iField.add(instant, value);
        }

        public long add(long instant, long value) {
            return iField.add(instant, value);
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            return iField.getDifference(minuendInstant, subtrahendInstant);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            return iField.getDifferenceAsLong(minuendInstant, subtrahendInstant);
        }
    }

}
