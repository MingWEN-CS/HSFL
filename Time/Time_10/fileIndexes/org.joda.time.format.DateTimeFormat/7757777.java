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
package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableInstant;
import org.joda.time.ReadableInstant;
import org.joda.time.chrono.RemainderDateTimeField;
import org.joda.time.chrono.iso.ISOChronology;

/**
 * DateTimeFormat provides localized printing and parsing capabilities for all
 * dates and times.
 * <p>
 * This class provides access to the actual DateTimeFormatter instances in two ways:
 * <ul>
 * <li>{@link #forPattern(String) Pattern} provides a DateTimeFormatter based on
 * a pattern string that is compatible with the JDK date patterns.
 * <li>{@link #forStyle(String) Style} provides a DateTimeFormatter based on a
 * two character style, representing short, medium, long and full.
 * </ul>
 * <p>
 * For example, to use a patterm:
 * <pre>
 * DateTime dt = new DateTime();
 * DateTimeFormatter fmt = DateTimeFormat.getInstance().forPattern("MMMM, yyyy");
 * String str = fmt.print(dt);
 * </pre>
 *
 * The pattern syntax is compatible with java.text.SimpleDateFormat, but a few
 * more symbols are also supported. All ASCII letters are reserved as pattern
 * letters, which are defined as the following:
 * <blockquote>
 * <pre>
 * Symbol  Meaning                      Presentation  Examples
 * ------  -------                      ------------  -------
 * G       era                          text          AD
 * C       century of era (&gt;=0)         number        20
 * Y       year of era (&gt;=0)            year          1996
 *
 * x       weekyear                     year          1996
 * w       week of weekyear             number        27
 * e       day of week                  number        2
 * E       day of week                  text          Tuesday; Tue
 *
 * y       year                         year          1996
 * D       day of year                  number        189
 * M       month of year                month         July; Jul; 07
 * d       day of month                 number        10
 *
 * a       halfday of day               text          PM
 * K       hour of halfday (0~11)       number        0
 * h       clockhour of halfday (1~12)  number        12
 *
 * H       hour of day (0~23)           number        0
 * k       clockhour of day (1~24)      number        24
 * m       minute of hour               number        30
 * s       second of minute             number        55
 * S       fraction of second           number        978
 *
 * z       time zone                    text          Pacific Standard Time; PST
 * Z       time zone offset             text          -08:00; -0800
 *
 * '       escape for text              delimiter
 * ''      single quote                 literal       '
 * </pre>
 * </blockquote>
 * The count of pattern letters determine the format.
 * <p>
 * <strong>Text</strong>: If the number of pattern letters is 4 or more,
 * the full form is used; otherwise a short or abbreviated form is used if
 * available.
 * <p>
 * <strong>Number</strong>: The minimum number of digits. Shorter numbers
 * are zero-padded to this amount.
 * <p>
 * <strong>Year</strong>: Numeric presentation for year and weekyear fields
 * are handled specially. For example, if the count of 'y' is 2, the year
 * will be displayed as the zero-based year of the century, which is two
 * digits.
 * <p>
 * <strong>Month</strong>: 3 or over, use text, otherwise use number.
 * <p>
 * Any characters in the pattern that are not in the ranges of ['a'..'z']
 * and ['A'..'Z'] will be treated as quoted text. For instance, characters
 * like ':', '.', ' ', '#' and '@' will appear in the resulting time text
 * even they are not embraced within single quotes.
 * <p>
 * DateTimeFormat is thread-safe and immutable, and the formatters it returns
 * are as well.
 *
 * @author Brian S O'Neill
 * @since 1.0
 * @see ISODateTimeFormat
 * @see DateTimeFormatterBuilder
 */
public class DateTimeFormat {

    /**
     * Cache that maps Chronology instances to maps that map
     * Locales to DateTimeFormat instances.
     */
    private static Map cInstanceCache = new HashMap(7);

    /**
     * Gets a formatter provider that works using ISOChronology with UTC in the
     * default locale.
     * 
     * @return a format provider
     */
    public static DateTimeFormat getInstanceUTC() {
        return getInstance(ISOChronology.getInstanceUTC(), Locale.getDefault());
    }

    /**
     * Gets a formatter provider that works using ISOChronology with the default
     * time zone and the default locale.
     * 
     * @return a format provider
     */
    public static DateTimeFormat getInstance() {
        return getInstance(ISOChronology.getInstance(), Locale.getDefault());
    }

    /**
     * Gets a formatter provider that works using ISOChronology with the
     * specified time zone and the default locale.
     * 
     * @param zone  the time zone to use, null for default zone
     * @return a format provider
     */
    public static DateTimeFormat getInstance(final DateTimeZone zone) {
        return getInstance(ISOChronology.getInstance(zone), Locale.getDefault());
    }

    /**
     * Gets a formatter provider that works using ISOChronology with the
     * specified time zone and locale.
     * 
     * @param zone  the time zone to use, null for default zone
     * @param locale  the Locale to use, null for default locale
     * @return a format provider
     */
    public static DateTimeFormat getInstance(final DateTimeZone zone, final Locale locale) {
        return getInstance(ISOChronology.getInstance(zone), locale);
    }

    /**
     * Gets a formatter provider that works using the specified chronology and
     * the default locale.
     * 
     * @param chrono  the chronology to use, null means ISOChronology in the default time zone
     * @return a format provider
     */
    public static DateTimeFormat getInstance(final Chronology chrono) {
        return getInstance(chrono, Locale.getDefault());
    }

    /**
     * Gets a formatter provider that works using the specified chronology and
     * locale.
     * 
     * @param chrono  the chronology to use, null means ISOChronology in the default time zone
     * @param locale  the Locale to use, null for default locale
     * @return a format provider
     */
    public static synchronized DateTimeFormat getInstance(Chronology chrono, Locale locale) {
        if (chrono == null) {
            chrono = ISOChronology.getInstance();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        Map map = (Map)cInstanceCache.get(chrono);
        if (map == null) {
            map = new HashMap(7);
            cInstanceCache.put(chrono, map);
        }
        DateTimeFormat dtf = (DateTimeFormat)map.get(locale);
        if (dtf == null) {
            dtf = new DateTimeFormat(chrono, locale);
            map.put(locale, dtf);
        }
        return dtf;
    }

    /**
     * Parses the given pattern and appends the rules to the given
     * DateTimeFormatterBuilder.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     * @see #forPattern
     */
    public static void appendPatternTo(DateTimeFormatterBuilder builder, String pattern) {
        int length = pattern.length();
        int[] indexRef = new int[1];

        for (int i=0; i<length; i++) {
            indexRef[0] = i;
            String token = parseToken(pattern, indexRef);
            i = indexRef[0];

            int tokenLen = token.length();
            if (tokenLen == 0) {
                break;
            }
            char c = token.charAt(0);

            switch (c) {
            case 'G': // era designator (text)
                builder.appendEraText();
                break;
            case 'C': // century of era (number)
                builder.appendCenturyOfEra(tokenLen, tokenLen);
                break;
            case 'x': // weekyear (number)
            case 'y': // year (number)
            case 'Y': // year of era (number)
                if (tokenLen == 2) {
                    // Use a new RemainderDateTimeField to ensure that the year
                    // of century is zero-based.
                    DateTimeField field;
                    Chronology chronoUTC = builder.getChronology().withUTC();
                    switch (c) {
                    case 'x':
                        field = new RemainderDateTimeField
                            (chronoUTC.weekyear(), "weekyearOfCentury", "centuries", 100);
                        break;
                    case 'y': default:
                        field = new RemainderDateTimeField
                            (chronoUTC.year(), "yearOfCentury", "centuries", 100);
                        break;
                    case 'Y':
                        field = new RemainderDateTimeField
                            (chronoUTC.yearOfEra(), "yearOfCentury", "centuries", 100);
                        break;
                    }
                    builder.appendDecimal(field, 2, 2);
                } else {
                    // Try to support long year values.
                    int maxDigits = 9;

                    // Peek ahead to next token.
                    if (i + 1 < length) {
                        indexRef[0]++;
                        if (isNumericToken(parseToken(pattern, indexRef))) {
                            // If next token is a number, cannot support long years.
                            maxDigits = tokenLen;
                        }
                        indexRef[0]--;
                    }

                    switch (c) {
                    case 'x':
                        builder.appendWeekyear(tokenLen, maxDigits);
                        break;
                    case 'y':
                        builder.appendYear(tokenLen, maxDigits);
                        break;
                    case 'Y':
                        builder.appendYearOfEra(tokenLen, maxDigits);
                        break;
                    }
                }
                break;
            case 'M': // month of year (text and number)
                if (tokenLen >= 3) {
                    if (tokenLen >= 4) {
                        builder.appendMonthOfYearText();
                    } else {
                        builder.appendMonthOfYearShortText();
                    }
                } else {
                    builder.appendMonthOfYear(tokenLen);
                }
                break;
            case 'd': // day of month (number)
                builder.appendDayOfMonth(tokenLen);
                break;
            case 'h': // hour of day (number, 1..12)
                builder.appendClockhourOfHalfday(tokenLen);
                break;
            case 'H': // hour of day (number, 0..23)
                builder.appendHourOfDay(tokenLen);
                break;
            case 'm': // minute of hour (number)
                builder.appendMinuteOfHour(tokenLen);
                break;
            case 's': // second of minute (number)
                builder.appendSecondOfMinute(tokenLen);
                break;
            case 'S': // fraction of second (number)
                builder.appendFractionOfSecond(tokenLen, tokenLen);
                break;
            case 'e': // day of week (number)
                builder.appendDayOfWeek(tokenLen);
                break;
            case 'E': // dayOfWeek (text)
                if (tokenLen >= 4) {
                    builder.appendDayOfWeekText();
                } else {
                    builder.appendDayOfWeekShortText();
                }
                break;
            case 'D': // day of year (number)
                builder.appendDayOfYear(tokenLen);
                break;
            case 'w': // week of weekyear (number)
                builder.appendWeekOfWeekyear(tokenLen);
                break;
            case 'a': // am/pm marker (text)
                builder.appendHalfdayOfDayText();
                break;
            case 'k': // hour of day (1..24)
                builder.appendClockhourOfDay(tokenLen);
                break;
            case 'K': // hour of day (0..11)
                builder.appendClockhourOfHalfday(tokenLen);
                break;
            case 'z': // time zone (text)
                if (tokenLen >= 4) {
                    builder.appendTimeZoneName();
                } else {
                    builder.appendTimeZoneShortName();
                }
                break;
            case 'Z': // time zone offset
                if (tokenLen >= 4) {
                    builder.appendTimeZoneOffset(null, true, 2, 2);
                } else {
                    builder.appendTimeZoneOffset(null, false, 2, 2);
                }
                break;
            case '\'': // literal text
                String sub = token.substring(1);
                if (sub.length() == 1) {
                    builder.appendLiteral(sub.charAt(0));
                } else {
                    // Create copy of sub since otherwise the temporary quoted
                    // string would still be referenced internally.
                    builder.appendLiteral(new String(sub));
                }
                break;
            default:
                throw new IllegalArgumentException
                    ("Illegal pattern component: " + token);
            }
        }
    }

    private static String parseToken(final String pattern, final int[] indexRef) {
        StringBuffer buf = new StringBuffer();

        int i = indexRef[0];
        int length = pattern.length();

        char c = pattern.charAt(i);
        if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
            // Scan a run of the same character, which indicates a time
            // pattern.
            buf.append(c);

            while (i + 1 < length) {
                char peek = pattern.charAt(i + 1);
                if (peek == c) {
                    buf.append(c);
                    i++;
                } else {
                    break;
                }
            }
        } else {
            // This will identify token as text.
            buf.append('\'');

            boolean inLiteral = false;

            for (; i < length; i++) {
                c = pattern.charAt(i);
                
                if (c == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        // '' is treated as escaped '
                        i++;
                        buf.append(c);
                    } else {
                        inLiteral = !inLiteral;
                    }
                } else if (!inLiteral &&
                           (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')) {
                    i--;
                    break;
                } else {
                    buf.append(c);
                }
            }
        }

        indexRef[0] = i;
        return buf.toString();
    }

    // Returns true if token should be parsed as a numeric field.
    private static boolean isNumericToken(final String token) {
        int tokenLen = token.length();
        if (tokenLen > 0) {
            char c = token.charAt(0);
            switch (c) {
            case 'c': // century (number)
            case 'C': // century of era (number)
            case 'x': // weekyear (number)
            case 'y': // year (number)
            case 'Y': // year of era (number)
            case 'd': // day of month (number)
            case 'h': // hour of day (number, 1..12)
            case 'H': // hour of day (number, 0..23)
            case 'm': // minute of hour (number)
            case 's': // second of minute (number)
            case 'S': // fraction of second (number)
            case 'e': // day of week (number)
            case 'D': // day of year (number)
            case 'F': // day of week in month (number)
            case 'w': // week of year (number)
            case 'W': // week of month (number)
            case 'k': // hour of day (1..24)
            case 'K': // hour of day (0..11)
                return true;
            case 'M': // month of year (text and number)
                if (tokenLen <= 2) {
                    return true;
                }
            }
        }
            
        return false;
    }

    //-----------------------------------------------------------------------
    /** The chronology to use */
    private final Chronology iChrono;
    /** The locale to use */
    private final Locale iLocale;

    /** Maps patterns to formatters */
    private transient Map iPatternedCache = new HashMap(7);

    /** Maps styles to formatters */
    private transient Map iStyledCache = new HashMap(7);

    /**
     * Constructor.
     * 
     * @param chrono  the chronology to use, must not be null
     * @param locale  the locale to use, must not be null
     */
    private DateTimeFormat(final Chronology chrono, final Locale locale) {
        super();
        iChrono = chrono;
        iLocale = locale;
    }

    //-----------------------------------------------------------------------
    /**
     * Select a format from a custom pattern.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     * @see #appendPatternTo
     */
    public synchronized DateTimeFormatter forPattern(final String pattern) {
        DateTimeFormatter formatter = (DateTimeFormatter) iPatternedCache.get(pattern);
        if (formatter != null) {
            return formatter;
        }

        if (pattern == null) {
            throw new IllegalArgumentException("Invalid pattern specification");
        }

        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder(iChrono, iLocale);
        appendPatternTo(builder, pattern);

        if (builder.canBuildFormatter()) {
            formatter = builder.toFormatter();
        } else if (builder.canBuildPrinter()) {
            formatter = new FPrinter(builder.toPrinter());
        } else if (builder.canBuildParser()) {
            // I don't expect this case to ever occur.
            formatter = new FParser(builder.toParser());
        } else {
            throw new UnsupportedOperationException("Pattern unsupported: " + pattern);
        }

        iPatternedCache.put(pattern, formatter);
        return formatter;
    }

    /**
     * Select a format from a two character style pattern. The first character
     * is the date style, and the second character is the time style. Specify a
     * character of 'S' for short style, 'M' for medium, 'L' for long, and 'F'
     * for full. A date or time may be ommitted by specifying a style character '-'.
     *
     * @param style  two characters from the set {"S", "M", "L", "F", "-"}
     * @throws IllegalArgumentException if the style is invalid
     */
    public synchronized DateTimeFormatter forStyle(final String style) {
        DateTimeFormatter formatter = (DateTimeFormatter)iStyledCache.get(style);
        if (formatter == null) {
            formatter = forPattern(getPatternForStyle(style));
            iStyledCache.put(style, formatter);
        }
        return formatter;
    }

    /**
     * Returns a pattern specification from a two character style. The first
     * character is the date style, and the second character is the time
     * style. Specify a character of 'S' for short style, 'M' for medium, 'L'
     * for long, and 'F' for full. A date or time may be ommitted by specifying
     * a style character '-'.
     *
     * @param style  two characters from the set {"S", "M", "L", "F", "-"}
     * @throws IllegalArgumentException if the style is invalid
     */
    public String getPatternForStyle(final String style) {
        if (style == null || style.length() != 2) {
            throw new IllegalArgumentException("Invalid style specification: " + style);
        }

        if (style.charAt(1) == '-') {
            // date only
            return getDatePattern(style.charAt(0));
        } else if (style.charAt(0) == '-') {
            // time only
            return getTimePattern(style.charAt(1));
        } else {
            // datetime
            return getDateTimePattern(style.charAt(0), style.charAt(1));
        }
    }

    private String getDatePattern(final char style) {
        int istyle = selectStyle(style);
        try {
            return ((SimpleDateFormat)DateFormat.getDateInstance(istyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No date pattern for locale: " + iLocale);
        }
    }

    private String getTimePattern(final char style) {
        int istyle = selectStyle(style);
        try {
            return ((SimpleDateFormat)DateFormat.getTimeInstance(istyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No time pattern for locale: " + iLocale);
        }
    }

    private String getDateTimePattern(final char dateStyle, final char timeStyle) {
        int idateStyle = selectStyle(dateStyle);
        int itimeStyle = selectStyle(dateStyle);
        try {
            return ((SimpleDateFormat)DateFormat.getDateTimeInstance
                    (idateStyle, itimeStyle, iLocale)).toPattern();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("No datetime pattern for locale: " + iLocale);
        }
    }

    private int selectStyle(final char c) {
        switch (c) {
        case 'S':
            return DateFormat.SHORT;
        case 'M':
            return DateFormat.MEDIUM;
        case 'L':
            return DateFormat.LONG;
        case 'F':
            return DateFormat.FULL;
        default:
            throw new IllegalArgumentException("Invalid style character: " + c);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * A fake formatter that can only print.
     */
    static class FPrinter implements DateTimeFormatter {
        private final DateTimePrinter mPrinter;

        FPrinter(final DateTimePrinter printer) {
            super();
            mPrinter = printer;
        }

        public Chronology getChronology() {
            return mPrinter.getChronology();
        }

        public int estimatePrintedLength() {
            return mPrinter.estimatePrintedLength();
        }

        public void printTo(final StringBuffer buf, final ReadableInstant instant) {
            mPrinter.printTo(buf, instant);
        }

        public void printTo(final Writer out, final ReadableInstant instant) throws IOException {
            mPrinter.printTo(out, instant);
        }

        public void printTo(final StringBuffer buf, final long instant) {
            mPrinter.printTo(buf, instant);
        }

        public void printTo(final Writer out, final long instant) throws IOException {
            mPrinter.printTo(out, instant);
        }

        public void printTo(final StringBuffer buf, final long instant, final DateTimeZone zone) {
            mPrinter.printTo(buf, instant, zone);
        }

        public void printTo(final Writer out, final long instant, final DateTimeZone zone)
            throws IOException {
            mPrinter.printTo(out, instant, zone);
        }

        public void printTo(final StringBuffer buf, final long instant,
                            final DateTimeZone zone, final long instantLocal) {
            mPrinter.printTo(buf, instant, zone, instantLocal);
        }

        public void printTo(final Writer out, final long instant,
                            final DateTimeZone zone, final long instantLocal)
            throws IOException {
            mPrinter.printTo(out, instant, zone, instantLocal);
        }

        public String print(final ReadableInstant instant) {
            return mPrinter.print(instant);
        }

        public String print(final long instant) {
            return mPrinter.print(instant);
        }

        public String print(final long instant, final DateTimeZone zone) {
            return mPrinter.print(instant, zone);
        }

        public String print(final long instant, final DateTimeZone zone, final long instantLocal) {
            return mPrinter.print(instant, zone, instantLocal);
        }

        public int estimateParsedLength() {
            return 0;
        }

        public int parseInto(final DateTimeParserBucket bucket, final String text, final int position) {
            throw unsupported();
        }

        public int parseInto(final ReadWritableInstant instant, final String text, final int position) {
            throw unsupported();
        }

        public long parseMillis(final String text) {
            throw unsupported();
        }

        public long parseMillis(final String text, final long instantLocal) {
            throw unsupported();
        }

        public DateTime parseDateTime(final String text) {
            throw unsupported();
        }

        public DateTime parseDateTime(final String text, final ReadableInstant instant) {
            throw unsupported();
        }

        public MutableDateTime parseMutableDateTime(final String text) {
            throw unsupported();
        }

        public MutableDateTime parseMutableDateTime(final String text,
                                                    final ReadableInstant instant) {
            throw unsupported();
        }

        private UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("Parsing not supported");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * A fake formatter that can only parse.
     */
    static class FParser implements DateTimeFormatter {
        private final DateTimeParser mParser;

        FParser(final DateTimeParser parser) {
            super();
            mParser = parser;
        }

        public Chronology getChronology() {
            return mParser.getChronology();
        }

        public int estimatePrintedLength() {
            return 0;
        }

        public void printTo(final StringBuffer buf, final ReadableInstant instant) {
            throw unsupported();
        }

        public void printTo(final Writer out, final ReadableInstant instant) throws IOException {
            throw unsupported();
        }

        public void printTo(final StringBuffer buf, final long instant) {
            throw unsupported();
        }

        public void printTo(final Writer out, final long instant) throws IOException {
            throw unsupported();
        }

        public void printTo(final StringBuffer buf, final long instant, final DateTimeZone zone) {
            throw unsupported();
        }

        public void printTo(final Writer out, final long instant, final DateTimeZone zone) {
            throw unsupported();
        }

        public void printTo(final StringBuffer buf, final long instant,
                            final DateTimeZone zone, final long instantLocal) {
            throw unsupported();
        }

        public void printTo(final Writer out, final long instant,
                            final DateTimeZone zone, final long instantLocal) {
            throw unsupported();
        }

        public String print(final ReadableInstant instant) {
            throw unsupported();
        }

        public String print(final long instant) {
            throw unsupported();
        }

        public String print(final long instant, final DateTimeZone zone) {
            throw unsupported();
        }

        public String print(final long instant, final DateTimeZone zone, final long instantLocal) {
            throw unsupported();
        }

        public int estimateParsedLength() {
            return mParser.estimateParsedLength();
        }

        public int parseInto(final DateTimeParserBucket bucket, final String text, final int position) {
            return mParser.parseInto(bucket, text, position);
        }

        public int parseInto(final ReadWritableInstant instant, final String text, final int position) {
            return mParser.parseInto(instant, text, position);
        }

        public long parseMillis(final String text) {
            return mParser.parseMillis(text);
        }

        public long parseMillis(final String text, final long instantLocal) {
            return mParser.parseMillis(text, instantLocal);
        }

        public DateTime parseDateTime(final String text) {
            return mParser.parseDateTime(text);
        }

        public DateTime parseDateTime(final String text, final ReadableInstant instant) {
            return mParser.parseDateTime(text, instant);
        }

        public MutableDateTime parseMutableDateTime(final String text) {
            return mParser.parseMutableDateTime(text);
        }

        public MutableDateTime parseMutableDateTime(final String text,
                                                    final ReadableInstant instant) {
            return mParser.parseMutableDateTime(text, instant);
        }

        private UnsupportedOperationException unsupported() {
            return new UnsupportedOperationException("Printing not supported");
        }
    }
}
