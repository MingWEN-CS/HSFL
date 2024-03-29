/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.chrono;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;

/**
 * Implements a pure proleptic Gregorian calendar system, which defines every
 * fourth year as leap, unless the year is divisible by 100 and not by 400.
 * This improves upon the Julian calendar leap year rule.
 * <p>
 * Although the Gregorian calendar did not exist before 1582 CE, this
 * chronology assumes it did, thus it is proleptic. This implementation also
 * fixes the start of the year at January 1, and defines the year zero.
 * <p>
 * GregorianChronology is thread-safe and immutable.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Gregorian_calendar">Wikipedia</a>
 * @see JulianChronology
 * @see GJChronology
 * 
 * @author Guy Allard
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class GregorianChronology extends CommonGJChronology {

    /** Serialization lock */
    private static final long serialVersionUID = -861407383323710522L;

    private static final long MILLIS_PER_YEAR =
        (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY);

    private static final long MILLIS_PER_MONTH =
        (long) (365.2425 * DateTimeConstants.MILLIS_PER_DAY / 12);

    /** Singleton instance of a UTC GregorianChronology */
    private static final GregorianChronology INSTANCE_UTC;

    /** Cache of zone to chronology arrays */
    private static final Map cCache = new HashMap();

    static {
        INSTANCE_UTC = getInstance(DateTimeZone.UTC);
    }

    /**
     * Gets an instance of the GregorianChronology.
     * The time zone of the returned instance is UTC.
     * 
     * @return a singleton UTC instance of the chronology
     */
    public static GregorianChronology getInstanceUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets an instance of the GregorianChronology in the default time zone.
     * 
     * @return a chronology in the default time zone
     */
    public static GregorianChronology getInstance() {
        return getInstance(DateTimeZone.getDefault(), 4);
    }

    /**
     * Gets an instance of the GregorianChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @return a chronology in the specified time zone
     */
    public static GregorianChronology getInstance(DateTimeZone zone) {
        return getInstance(zone, 4);
    }

    /**
     * Gets an instance of the GregorianChronology in the given time zone.
     * 
     * @param zone  the time zone to get the chronology in, null is default
     * @param minDaysInFirstWeek  minimum number of days in first week of the year; default is 4
     * @return a chronology in the specified time zone
     */
    public static GregorianChronology getInstance(DateTimeZone zone, int minDaysInFirstWeek) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        GregorianChronology chrono;
        synchronized (cCache) {
            GregorianChronology[] chronos = (GregorianChronology[]) cCache.get(zone);
            if (chronos == null) {
                chronos = new GregorianChronology[7];
                cCache.put(zone, chronos);
            }
            try {
                chrono = chronos[minDaysInFirstWeek - 1];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IllegalArgumentException
                    ("Invalid min days in first week: " + minDaysInFirstWeek);
            }
            if (chrono == null) {
                if (zone == DateTimeZone.UTC) {
                    chrono = new GregorianChronology(null, null, minDaysInFirstWeek);
                } else {
                    chrono = getInstance(DateTimeZone.UTC, minDaysInFirstWeek);
                    chrono = new GregorianChronology
                        (ZonedChronology.getInstance(chrono, zone), null, minDaysInFirstWeek);
                }
                chronos[minDaysInFirstWeek - 1] = chrono;
            }
        }
        return chrono;
    }

    // Constructors and instance variables
    //-----------------------------------------------------------------------

    /**
     * Restricted constructor
     */
    private GregorianChronology(Chronology base, Object param, int minDaysInFirstWeek) {
        super(base, param, minDaysInFirstWeek);
    }

    /**
     * Serialization singleton
     */
    private Object readResolve() {
        Chronology base = getBase();
        return base == null ? getInstanceUTC() : getInstance(base.getZone());
    }

    // Conversion
    //-----------------------------------------------------------------------
    /**
     * Gets the Chronology in the UTC time zone.
     * 
     * @return the chronology in UTC
     */
    public Chronology withUTC() {
        return INSTANCE_UTC;
    }

    /**
     * Gets the Chronology in a specific time zone.
     * 
     * @param zone  the zone to get the chronology in, null is default
     * @return the chronology
     */
    public Chronology withZone(DateTimeZone zone) {
        if (zone == null) {
            zone = DateTimeZone.getDefault();
        }
        if (zone == getZone()) {
            return this;
        }
        return getInstance(zone);
    }

    protected void assemble(Fields fields) {
        if (getBase() == null) {
            super.assemble(fields);
        }
    }

    boolean isLeapYear(int year) {
        return ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);
    }

    long calculateFirstDayOfYearMillis(int year) {
        // Calculate relative to 2000 as that is on a 400 year boundary
        // and that makes the sum easier
        int relativeYear = year - 2000;
        // Initial value is just temporary.
        int leapYears = relativeYear / 100;
        if (relativeYear <= 0) {
            // Add 3 before shifting right since /4 and >>2 behave differently
            // on negative numbers. When the expression is written as
            // (relativeYear / 4) - (relativeYear / 100) + (relativeYear / 400),
            // it works for both positive and negative values, except this optimization
            // eliminates two divisions.
            leapYears = ((relativeYear + 3) >> 2) - leapYears + ((leapYears + 3) >> 2);
        } else {
            leapYears = (relativeYear >> 2) - leapYears + (leapYears >> 2);
            // For post 2000 an adjustment is needed as jan1st is before leap day
            if (!isLeapYear(year)) {
                leapYears++;
            }
        }
        
        long millis = (relativeYear * 365L + leapYears)
            * (long)DateTimeConstants.MILLIS_PER_DAY;
        
        // Previous line was reduced from this to eliminate a multiplication.
        // millis = ((relativeYear - leapYears) * 365L + leapYears * 366) * MILLIS_PER_DAY;
        // (x - y)*c + y*(c + 1) => x*c - y*c + y*c + y => x*c + y
        
        return millis + MILLIS_1970_TO_2000;
    }

    int getMinYear() {
        // The lowest year that can be fully supported.
        return -292275054;
    }

    int getMaxYear() {
        // The highest year that can be fully supported.
        return 292277023;
    }

    long getAverageMillisPerYear() {
        return MILLIS_PER_YEAR;
    }

    long getAverageMillisPerMonth() {
        return MILLIS_PER_MONTH;
    }

    long getApproxMillisAtEpoch() {
        return 1970L * MILLIS_PER_YEAR;
    }

}
