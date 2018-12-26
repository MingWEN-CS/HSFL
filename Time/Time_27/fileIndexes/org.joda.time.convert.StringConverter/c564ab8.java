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
package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodParser;

/**
 * StringConverter converts from a String to an instant, partial,
 * duration, period or interval..
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
class StringConverter extends AbstractConverter
        implements InstantConverter, PartialConverter, DurationConverter, PeriodConverter, IntervalConverter {

    /**
     * Singleton instance.
     */
    static final StringConverter INSTANCE = new StringConverter();

    /**
     * Restricted constructor.
     */
    protected StringConverter() {
        super();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the millis, which is the ISO parsed string value.
     * 
     * @param object  the String to convert, must not be null
     * @param chrono  the chronology to use, non-null result of getChronology
     * @return the millisecond value
     * @throws IllegalArgumentException if the value if invalid
     */
    public long getInstantMillis(Object object, Chronology chrono) {
        String str = (String) object;
        DateTimeParser p = ISODateTimeFormat.getInstance().dateTimeParser();
        return p.parseMillis(str, chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the duration of the string using the standard type.
     * This matches the toString() method of ReadableDuration.
     * 
     * @param object  the String to convert, must not be null
     * @throws ClassCastException if the object is invalid
     */
    public long getDurationMillis(Object object) {
        // parse here because duration could be bigger than the int supported
        // by the period parser
        String original = (String) object;
        String str = original;
        int len = str.length();
        if (len >= 4 &&
            (str.charAt(0) == 'P' || str.charAt(0) == 'p') &&
            (str.charAt(1) == 'T' || str.charAt(1) == 't') &&
            (str.charAt(len - 1) == 'S' || str.charAt(len - 1) == 's')) {
            // ok
        } else {
            throw new IllegalArgumentException("Invalid format: \"" + original + '"');
        }
        str = str.substring(2, len - 1);
        int dot = -1;
        for (int i = 0; i < str.length(); i++) {
            if ((str.charAt(i) >= '0' && str.charAt(i) <= '9') ||
                (i == 0 && str.charAt(0) == '-')) {
                // ok
            } else if (i > 0 && str.charAt(i) == '.' && dot == -1) {
                // ok
                dot = i;
            } else {
                throw new IllegalArgumentException("Invalid format: \"" + original + '"');
            }
        }
        long millis = 0, seconds = 0;
        if (dot > 0) {
            seconds = Long.parseLong(str.substring(0, dot));
            str = str.substring(dot + 1);
            if (str.length() != 3) {
                str = (str + "000").substring(0, 3);
            }
            millis = Integer.parseInt(str);
        } else {
            seconds = Long.parseLong(str);
        }
        if (seconds < 0) {
            return FieldUtils.safeAdd(FieldUtils.safeMultiply(seconds, 1000), -millis);
        } else {
            return FieldUtils.safeAdd(FieldUtils.safeMultiply(seconds, 1000), millis);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Extracts duration values from an object of this converter's type, and
     * sets them into the given ReadWritableDuration.
     *
     * @param period  period to get modified
     * @param object  the String to convert, must not be null
     * @param chrono  the chronology to use
     * @return the millisecond duration
     * @throws ClassCastException if the object is invalid
     */
    public void setInto(ReadWritablePeriod period, Object object, Chronology chrono) {
        String str = (String) object;
        PeriodParser parser = ISOPeriodFormat.getInstance().standard();
        period.clear();
        int pos = parser.parseInto(period, str, 0);
        if (pos < str.length()) {
            if (pos < 0) {
                // Parse again to get a better exception thrown.
                parser.parseMutablePeriod(period.getPeriodType(), str);
            }
            throw new IllegalArgumentException("Invalid format: \"" + str + '"');
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the value of the mutable interval from the string.
     * 
     * @param writableInterval  the interval to set
     * @param object  the String to convert, must not be null
     * @param chrono  the chronology to use, may be null
     */
    public void setInto(ReadWritableInterval writableInterval, Object object, Chronology chrono) {
        String str = (String) object;

        int separator = str.indexOf('/');
        if (separator < 0) {
            throw new IllegalArgumentException("Format requires a '/' separator: " + str);
        }

        String leftStr = str.substring(0, separator);
        if (leftStr.length() <= 0) {
            throw new IllegalArgumentException("Format invalid: " + str);
        }
        String rightStr = str.substring(separator + 1);
        if (rightStr.length() <= 0) {
            throw new IllegalArgumentException("Format invalid: " + str);
        }

        DateTimeParser dateTimeParser = ISODateTimeFormat.getInstance().dateTimeParser();
        PeriodFormatter periodParser = ISOPeriodFormat.getInstance().standard();
        long startInstant = 0, endInstant = 0;
        Period period = null;
        Chronology parsedChrono = null;
        
        // before slash
        char c = leftStr.charAt(0);
        if (c == 'P' || c == 'p') {
            period = periodParser.parsePeriod(getPeriodType(leftStr), leftStr);
        } else {
            DateTime start = dateTimeParser.parseDateTime(leftStr, chrono);
            startInstant = start.getMillis();
            parsedChrono = start.getChronology();
        }
        
        // after slash
        c = rightStr.charAt(0);
        if (c == 'P' || c == 'p') {
            if (period != null) {
                throw new IllegalArgumentException("Interval composed of two durations: " + str);
            }
            period = periodParser.parsePeriod(getPeriodType(rightStr), rightStr);
            chrono = (chrono != null ? chrono : parsedChrono);
            endInstant = chrono.add(period, startInstant, 1);
        } else {
            DateTime end = dateTimeParser.parseDateTime(rightStr, chrono);
            endInstant = end.getMillis();
            parsedChrono = (parsedChrono != null ? parsedChrono : end.getChronology());
            chrono = (chrono != null ? chrono : parsedChrono);
            if (period != null) {
                startInstant = chrono.add(period, endInstant, -1);
            }
        }
        
        writableInterval.setInterval(startInstant, endInstant);
        writableInterval.setChronology(chrono);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns String.class.
     * 
     * @return String.class
     */
    public Class getSupportedType() {
        return String.class;
    }

}
