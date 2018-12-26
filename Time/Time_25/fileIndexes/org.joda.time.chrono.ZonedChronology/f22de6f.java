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

import java.util.HashMap;
import java.util.Locale;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.field.BaseDateTimeField;
import org.joda.time.field.BaseDurationField;

/**
 * Wraps another Chronology to add support for time zones.
 * <p>
 * ZonedChronology is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @author Stephen Colebourne
 * @since 1.0
 */
public final class ZonedChronology extends AssembledChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -1079258847191166848L;

    /**
     * Create a ZonedChronology for any chronology, overriding any time zone it
     * may already have.
     *
     * @param base base chronology to wrap
     * @param zone the time zone
     * @throws IllegalArgumentException if chronology or time zone is null
     */
    public static ZonedChronology getInstance(Chronology base, DateTimeZone zone) {
        if (base == null) {
            throw new IllegalArgumentException("Must supply a chronology");
        }
        base = base.withUTC();
        if (base == null) {
            throw new IllegalArgumentException("UTC chronology must not be null");
        }
        if (zone == null) {
            throw new IllegalArgumentException("DateTimeZone must not be null");
        }
        return new ZonedChronology(base, zone);
    }

    static boolean useTimeArithmetic(DurationField field) {
        // Use time of day arithmetic rules for unit durations less than
        // typical time zone offsets.
        return field != null && field.getUnitMillis() < DateTimeConstants.MILLIS_PER_HOUR * 12;
    }

    /**
     * Restricted constructor
     *
     * @param base base chronology to wrap
     * @param zone the time zone
     */
    private ZonedChronology(Chronology base, DateTimeZone zone) {
        super(base, zone);
    }

    public DateTimeZone getZone() {
        return (DateTimeZone)getParam();
    }

    public Chronology withUTC() {
        return getBase();
    }

    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getParam()) {
            return this;
        }
        if (zone == DateTimeZone.UTC) {
            return getBase();
        }
        return new ZonedChronology(getBase(), zone);
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int millisOfDay)
        throws IllegalArgumentException
    {
        return localToUTC(getBase().getDateTimeMillis
                          (year, monthOfYear, dayOfMonth, millisOfDay));
    }

    public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return localToUTC(getBase().getDateTimeMillis
                          (year, monthOfYear, dayOfMonth, 
                           hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    public long getDateTimeMillis(long instant,
                                  int hourOfDay, int minuteOfHour,
                                  int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException
    {
        return localToUTC(getBase().getDateTimeMillis
                          (instant + getZone().getOffset(instant),
                           hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
    }

    /**
     * @param instant instant from 1970-01-01T00:00:00 local time
     * @return instant from 1970-01-01T00:00:00Z
     */
    private long localToUTC(long instant) {
        DateTimeZone zone = getZone();
        int offset = zone.getOffsetFromLocal(instant);
        instant -= offset;
        if (offset != zone.getOffset(instant)) {
            throw new IllegalArgumentException
                ("Illegal instant due to time zone offset transition");
        }
        return instant;
    }

    protected void assemble(Fields fields) {
        // Keep a local cache of converted fields so as not to create redundant
        // objects.
        HashMap converted = new HashMap();

        // Convert duration fields...

        fields.eras = convertField(fields.eras, converted);
        fields.centuries = convertField(fields.centuries, converted);
        fields.years = convertField(fields.years, converted);
        fields.months = convertField(fields.months, converted);
        fields.weekyears = convertField(fields.weekyears, converted);
        fields.weeks = convertField(fields.weeks, converted);
        fields.days = convertField(fields.days, converted);

        fields.halfdays = convertField(fields.halfdays, converted);
        fields.hours = convertField(fields.hours, converted);
        fields.minutes = convertField(fields.minutes, converted);
        fields.seconds = convertField(fields.seconds, converted);
        fields.millis = convertField(fields.millis, converted);

        // Convert datetime fields...

        fields.year = convertField(fields.year, converted);
        fields.yearOfEra = convertField(fields.yearOfEra, converted);
        fields.yearOfCentury = convertField(fields.yearOfCentury, converted);
        fields.centuryOfEra = convertField(fields.centuryOfEra, converted);
        fields.era = convertField(fields.era, converted);
        fields.dayOfWeek = convertField(fields.dayOfWeek, converted);
        fields.dayOfMonth = convertField(fields.dayOfMonth, converted);
        fields.dayOfYear = convertField(fields.dayOfYear, converted);
        fields.monthOfYear = convertField(fields.monthOfYear, converted);
        fields.weekOfWeekyear = convertField(fields.weekOfWeekyear, converted);
        fields.weekyear = convertField(fields.weekyear, converted);
        fields.weekyearOfCentury = convertField(fields.weekyearOfCentury, converted);

        fields.millisOfSecond = convertField(fields.millisOfSecond, converted);
        fields.millisOfDay = convertField(fields.millisOfDay, converted);
        fields.secondOfMinute = convertField(fields.secondOfMinute, converted);
        fields.secondOfDay = convertField(fields.secondOfDay, converted);
        fields.minuteOfHour = convertField(fields.minuteOfHour, converted);
        fields.minuteOfDay = convertField(fields.minuteOfDay, converted);
        fields.hourOfDay = convertField(fields.hourOfDay, converted);
        fields.hourOfHalfday = convertField(fields.hourOfHalfday, converted);
        fields.clockhourOfDay = convertField(fields.clockhourOfDay, converted);
        fields.clockhourOfHalfday = convertField(fields.clockhourOfHalfday, converted);
        fields.halfdayOfDay = convertField(fields.halfdayOfDay, converted);
    }

    private DurationField convertField(DurationField field, HashMap converted) {
        if (field == null || !field.isSupported()) {
            return field;
        }
        if (converted.containsKey(field)) {
            return (DurationField)converted.get(field);
        }
        ZonedDurationField zonedField = new ZonedDurationField(field, getZone());
        converted.put(field, zonedField);
        return zonedField;
    }

    private DateTimeField convertField(DateTimeField field, HashMap converted) {
        if (field == null || !field.isSupported()) {
            return field;
        }
        if (converted.containsKey(field)) {
            return (DateTimeField)converted.get(field);
        }
        ZonedDateTimeField zonedField =
            new ZonedDateTimeField(field, getZone(),
                                   convertField(field.getDurationField(), converted),
                                   convertField(field.getRangeDurationField(), converted),
                                   convertField(field.getLeapDurationField(), converted));
        converted.put(field, zonedField);
        return zonedField;
    }

    public String toString() {
        return "ZonedChronology[" + getBase() + ", " + getZone().getID() + ']';
    }

    /*
     * Because time durations are typically smaller than time zone offsets, the
     * arithmetic methods subtract the original offset. This produces a more
     * expected behavior when crossing time zone offset transitions. For dates,
     * the new offset is subtracted off. This behavior, if applied to time
     * fields, can nullify or reverse an add when crossing a transition.
     */

    static class ZonedDurationField extends BaseDurationField {
        private static final long serialVersionUID = -485345310999208286L;

        final DurationField iField;
        final boolean iTimeField;
        final DateTimeZone iZone;

        ZonedDurationField(DurationField field, DateTimeZone zone) {
            super(field.getType());
            if (!field.isSupported()) {
                throw new IllegalArgumentException();
            }
            iField = field;
            iTimeField = useTimeArithmetic(field);
            this.iZone = zone;
        }

        public boolean isPrecise() {
            return iTimeField ? iField.isPrecise() : iZone.isFixed();
        }

        public long getUnitMillis() {
            return iField.getUnitMillis();
        }

        public int getValue(long duration, long instant) {
            return iField.getValue(duration, instant + this.iZone.getOffset(instant));
        }

        public long getValueAsLong(long duration, long instant) {
            return iField.getValueAsLong(duration, instant + this.iZone.getOffset(instant));
        }

        public long getMillis(int value, long instant) {
            return iField.getMillis(value, instant + this.iZone.getOffset(instant));
        }

        public long getMillis(long value, long instant) {
            return iField.getMillis(value, instant + this.iZone.getOffset(instant));
        }

        public long add(long instant, int value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long add(long instant, long value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            int offset = this.iZone.getOffset(subtrahendInstant);
            return iField.getDifference
                (minuendInstant + (iTimeField ? offset : this.iZone.getOffset(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            int offset = this.iZone.getOffset(subtrahendInstant);
            return iField.getDifferenceAsLong
                (minuendInstant + (iTimeField ? offset : this.iZone.getOffset(minuendInstant)),
                 subtrahendInstant + offset);
        }
    }

    /**
     * A DateTimeField that decorates another to add timezone behaviour.
     * <p>
     * This class converts passed in instants to local wall time, and vice
     * versa on output.
     */
    static final class ZonedDateTimeField extends BaseDateTimeField {
        private static final long serialVersionUID = -3968986277775529794L;

        final DateTimeField iField;
        final DateTimeZone iZone;
        final DurationField iDurationField;
        final boolean iTimeField;
        final DurationField iRangeDurationField;
        final DurationField iLeapDurationField;

        ZonedDateTimeField(DateTimeField field,
                           DateTimeZone zone,
                           DurationField durationField,
                           DurationField rangeDurationField,
                           DurationField leapDurationField) {
            super(field.getType());
            if (!field.isSupported()) {
                throw new IllegalArgumentException();
            }
            iField = field;
            this.iZone = zone;
            iDurationField = durationField;
            iTimeField = useTimeArithmetic(durationField);
            iRangeDurationField = rangeDurationField;
            iLeapDurationField = leapDurationField;
        }

        public boolean isLenient() {
            return iField.isLenient();
        }

        public int get(long instant) {
            return iField.get(instant + this.iZone.getOffset(instant));
        }

        public String getAsText(long instant, Locale locale) {
            return iField.getAsText(instant + this.iZone.getOffset(instant), locale);
        }

        public String getAsShortText(long instant, Locale locale) {
            return iField.getAsShortText(instant + this.iZone.getOffset(instant), locale);
        }

        public long add(long instant, int value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long add(long instant, long value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.add(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long addWrapField(long instant, int value) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.addWrapField(instant + offset, value);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long set(long instant, int value) {
            long offset = this.iZone.getOffset(instant);

            instant = iField.set(instant + offset, value);
            long offsetFromLocal = this.iZone.getOffsetFromLocal(instant);
            instant -= offsetFromLocal;

            if (offset != offsetFromLocal) {
                if (get(instant) != value) {
                    throw new IllegalArgumentException
                        ("Illegal value for " + iField.getName() + ": " + value);
                }
            }

            return instant;
        }

        public long set(long instant, String text, Locale locale) {
            instant = iField.set(instant + this.iZone.getOffset(instant), text, locale);
            // Cannot verify that new value stuck because set may be lenient.
            return instant - this.iZone.getOffsetFromLocal(instant);
        }

        public int getDifference(long minuendInstant, long subtrahendInstant) {
            int offset = this.iZone.getOffset(subtrahendInstant);
            return iField.getDifference
                (minuendInstant + (iTimeField ? offset : this.iZone.getOffset(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
            int offset = this.iZone.getOffset(subtrahendInstant);
            return iField.getDifferenceAsLong
                (minuendInstant + (iTimeField ? offset : this.iZone.getOffset(minuendInstant)),
                 subtrahendInstant + offset);
        }

        public final DurationField getDurationField() {
            return iDurationField;
        }

        public final DurationField getRangeDurationField() {
            return iRangeDurationField;
        }

        public boolean isLeap(long instant) {
            return iField.isLeap(instant + this.iZone.getOffset(instant));
        }

        public int getLeapAmount(long instant) {
            return iField.getLeapAmount(instant + this.iZone.getOffset(instant));
        }

        public final DurationField getLeapDurationField() {
            return iLeapDurationField;
        }

        public long roundFloor(long instant) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.roundFloor(instant + offset);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long roundCeiling(long instant) {
            int offset = this.iZone.getOffset(instant);
            instant = iField.roundCeiling(instant + offset);
            return instant - (iTimeField ? offset : this.iZone.getOffsetFromLocal(instant));
        }

        public long remainder(long instant) {
            return iField.remainder(instant + this.iZone.getOffset(instant));
        }

        public int getMinimumValue() {
            return iField.getMinimumValue();
        }

        public int getMinimumValue(long instant) {
            return iField.getMinimumValue(instant + this.iZone.getOffset(instant));
        }

        public int getMaximumValue() {
            return iField.getMaximumValue();
        }

        public int getMaximumValue(long instant) {
            return iField.getMaximumValue(instant + this.iZone.getOffset(instant));
        }

        public int getMaximumTextLength(Locale locale) {
            return iField.getMaximumTextLength(locale);
        }

        public int getMaximumShortTextLength(Locale locale) {
            return iField.getMaximumShortTextLength(locale);
        }
    }

}
