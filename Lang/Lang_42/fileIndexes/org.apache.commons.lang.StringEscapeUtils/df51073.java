/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
 * reserved.
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
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
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
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.lang;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * <p>Common <code>String</code> escaping routines.</p>
 *
 * <p>Originally from
 * <a href="http://jakarta.apache.org/turbine/">Turbine</a> and the
 * GenerationJavaCore library and from
 * <a href="http://www.purpletech.com/code/">Purple Technology</a>
 * </p>
 *
 * @author <a href="mailto:bayard@generationjava.com">Henri Yandell</a>
 * @author <a href="mailto:alex@purpletech.com">Alexander Day Chaffee</a>
 * @author <a href="mailto:cybertiger@cyberiantiger.org">cybertiger@cyberiantiger.org</a>
 * @author Helge Tesgaard
 * @author <a href="sean@boohai.com">Sean Brown</a>
 * @since 2.0
 * @version $Id: StringEscapeUtils.java,v 1.5 2003/04/09 18:45:28 alex Exp $
 */
public class StringEscapeUtils {

    /**
     * <p><code>StringEscapeUtils</code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>StringEscapeUtils.escapeJava("foo");</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public StringEscapeUtils() {
    }

    // Java and JavaScript
    //--------------------------------------------------------------------------
    /**
     * <p>Escapes any values it finds into their Java String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     *
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote must be escaped.</p>
     *
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, \"Stop!\"
     * </pre>
     * </p>
     *
     * @param str String to escape values in
     * @return String with escaped values
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String escapeJava(String str) {
        return escapeJavaStyleString(str, false);
    }

    /**
     * @see #escapeJava(java.lang.String)
     * @param out Writer to write escaped string into
     * @param str String to escape values in
     * @throws NullPointerException if str is <code>null</code>
     * @throws IOException if error occurs on undelying Writer
     */
    public static void escapeJava(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, false);
    }

    /**
     * <p>Escapes any values it finds into their JavaScript String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     *
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote must be escaped.</p>
     *
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     * </p>
     *
     * @param str String to escape values in
     * @return String with escaped values
     * @throws NullPointerException if str is <code>null</code>
     */
    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true);
    }

    /**
     * @see #escapeJavaScript(java.lang.String)
     * @param out Writer to write escaped string into
     * @param str String to escape values in
     * @throws NullPointerException if str is <code>null</code>
     * @throws IOException if error occurs on undelying Writer
     **/
    public static void escapeJavaScript(Writer out, String str) throws IOException {
        escapeJavaStyleString(out, str, true);
    }

    private static String escapeJavaStyleString(String str, boolean escapeSingleQuotes) {
        try {
            StringPrintWriter writer = new StringPrintWriter(str.length() * 2);
            escapeJavaStyleString(writer, str, escapeSingleQuotes);
            return writer.getString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }

    private static void escapeJavaStyleString(Writer out, String str, boolean escapeSingleQuote) throws IOException {
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                out.write("\\u" + hex(ch));
            } else if (ch > 0xff) {
                out.write("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                out.write("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default :
                        if (ch > 0xf) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) out.write('\\');
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    default :
                        out.write(ch);
                        break;
                }
            }
        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    /**
     * Unescapes any Java literals found in the String. For example,
     * it will turn a sequence of '\' and 'n' into a newline character,
     * unless the '\' is preceded by another '\'.
     */
    public static String unescapeJava(String str) {
        try {
            StringPrintWriter writer = new StringPrintWriter(str.length());
            unescapeJava(writer, str);
            return writer.getString();
        } catch (IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            ioe.printStackTrace();
            return null;
        }
    }

    public static void unescapeJava(Writer out, String str) throws IOException {
        int sz = str.length();
        StringBuffer unicode = new StringBuffer(4);
        boolean hadSlash = false;
        boolean inUnicode = false;
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            if (inUnicode) {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);
                if (unicode.length() == 4) {
                    // unicode now contains the four hex digits
                    // which represents our unicode chacater
                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);
                        out.write((char) value);
                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;
                    } catch (NumberFormatException nfe) {
                        throw new NestableRuntimeException("Unable to parse unicode value: " + unicode, nfe);
                    }
                }
                continue;
            }
            if (hadSlash) {
                // handle an escaped value
                hadSlash = false;
                switch (ch) {
                    case '\\':
                        out.write('\\');
                        break;
                    case '\'':
                        out.write('\'');
                        break;
                    case '\"':
                        out.write('"');
                        break;
                    case 'r':
                        out.write('\r');
                        break;
                    case 'f':
                        out.write('\f');
                        break;
                    case 't':
                        out.write('\t');
                        break;
                    case 'n':
                        out.write('\n');
                        break;
                    case 'b':
                        out.write('\b');
                        break;
                    case 'u':
                        {
                            // uh-oh, we're in unicode country....
                            inUnicode = true;
                            break;
                        }
                    default :
                        out.write(ch);
                        break;
                }
                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }
            out.write(ch);
        }
        if (hadSlash) {
            // then we're in the weird case of a \ at the end of the
            // string, let's output it anyway.
            out.write('\\');
        }
    }

    public static String unescapeJavaScript(String str) {
        return unescapeJava(str);
    }

    public static void unescapeJavaScript(Writer out, String str) throws IOException {
        unescapeJava(out, str);
    }

    // HTML and XML
    //--------------------------------------------------------------------------

    private static class Entities {
        // see http://hotwired.lycos.com/webmonkey/reference/special_characters/
        // see http://www.w3.org/TR/REC-html40/sgml/entities.html
        static Object[][] entities = {
            // {"#39", new Integer(39)},       // ' - apostrophe
            {"quot", new Integer(34)}, // " - double-quote
            {"amp", new Integer(38)}, // & - ampersand
            {"lt", new Integer(60)}, // < - less-than
            {"gt", new Integer(62)}, // > - greater-than
            {"nbsp", new Integer(160)}, // non-breaking space
            {"iexcl", new Integer(161)}, //inverted exclamation mark
            {"cent", new Integer(162)}, //cent sign
            {"pound", new Integer(163)}, //pound sign
            {"curren", new Integer(164)}, //currency sign
            {"yen", new Integer(165)}, //yen sign = yuan sign
            {"brvbar", new Integer(166)}, //broken bar = broken vertical bar
            {"sect", new Integer(167)}, //section sign
            {"uml", new Integer(168)}, //diaeresis = spacing diaeresis
            {"copy", new Integer(169)}, // � - copyright sign
            {"ordf", new Integer(170)}, //feminine ordinal indicator
            {"laquo", new Integer(171)}, //left-pointing double angle quotation mark = left pointing guillemet
            {"not", new Integer(172)}, //not sign
            {"shy", new Integer(173)}, //soft hyphen = discretionary hyphen
            {"reg", new Integer(174)}, // � - registered trademark sign
            {"macr", new Integer(175)}, //macron = spacing macron = overline = APL overbar
            {"deg", new Integer(176)}, //degree sign
            {"plusmn", new Integer(177)}, //plus-minus sign = plus-or-minus sign
            {"sup2", new Integer(178)}, //superscript two = superscript digit two = squared
            {"sup3", new Integer(179)}, //superscript three = superscript digit three = cubed
            {"acute", new Integer(180)}, //acute accent = spacing acute
            {"micro", new Integer(181)}, //micro sign
            {"para", new Integer(182)}, //pilcrow sign = paragraph sign
            {"middot", new Integer(183)}, //middle dot = Georgian comma = Greek middle dot
            {"cedil", new Integer(184)}, //cedilla = spacing cedilla
            {"sup1", new Integer(185)}, //superscript one = superscript digit one
            {"ordm", new Integer(186)}, //masculine ordinal indicator
            {"raquo", new Integer(187)}, //right-pointing double angle quotation mark = right pointing guillemet
            {"frac14", new Integer(188)}, //vulgar fraction one quarter = fraction one quarter
            {"frac12", new Integer(189)}, //vulgar fraction one half = fraction one half
            {"frac34", new Integer(190)}, //vulgar fraction three quarters = fraction three quarters
            {"iquest", new Integer(191)}, //inverted question mark = turned question mark
            {"Agrave", new Integer(192)}, // � - uppercase A, grave accent
            {"Aacute", new Integer(193)}, // � - uppercase A, acute accent
            {"Acirc", new Integer(194)}, // � - uppercase A, circumflex accent
            {"Atilde", new Integer(195)}, // � - uppercase A, tilde
            {"Auml", new Integer(196)}, // � - uppercase A, umlaut
            {"Aring", new Integer(197)}, // � - uppercase A, ring
            {"AElig", new Integer(198)}, // � - uppercase AE
            {"Ccedil", new Integer(199)}, // � - uppercase C, cedilla
            {"Egrave", new Integer(200)}, // � - uppercase E, grave accent
            {"Eacute", new Integer(201)}, // � - uppercase E, acute accent
            {"Ecirc", new Integer(202)}, // � - uppercase E, circumflex accent
            {"Euml", new Integer(203)}, // � - uppercase E, umlaut
            {"Igrave", new Integer(204)}, // � - uppercase I, grave accent
            {"Iacute", new Integer(205)}, // � - uppercase I, acute accent
            {"Icirc", new Integer(206)}, // � - uppercase I, circumflex accent
            {"Iuml", new Integer(207)}, // � - uppercase I, umlaut
            {"ETH", new Integer(208)}, // � - uppercase Eth, Icelandic
            {"Ntilde", new Integer(209)}, // � - uppercase N, tilde
            {"Ograve", new Integer(210)}, // � - uppercase O, grave accent
            {"Oacute", new Integer(211)}, // � - uppercase O, acute accent
            {"Ocirc", new Integer(212)}, // � - uppercase O, circumflex accent
            {"Otilde", new Integer(213)}, // � - uppercase O, tilde
            {"Ouml", new Integer(214)}, // � - uppercase O, umlaut
            {"times", new Integer(215)}, //multiplication sign
            {"Oslash", new Integer(216)}, // � - uppercase O, slash
            {"Ugrave", new Integer(217)}, // � - uppercase U, grave accent
            {"Uacute", new Integer(218)}, // � - uppercase U, acute accent
            {"Ucirc", new Integer(219)}, // � - uppercase U, circumflex accent
            {"Uuml", new Integer(220)}, // � - uppercase U, umlaut
            {"Yacute", new Integer(221)}, // � - uppercase Y, acute accent
            {"THORN", new Integer(222)}, // � - uppercase THORN, Icelandic
            {"szlig", new Integer(223)}, // � - lowercase sharps, German
            {"agrave", new Integer(224)}, // � - lowercase a, grave accent
            {"aacute", new Integer(225)}, // � - lowercase a, acute accent
            {"acirc", new Integer(226)}, // � - lowercase a, circumflex accent
            {"atilde", new Integer(227)}, // � - lowercase a, tilde
            {"auml", new Integer(228)}, // � - lowercase a, umlaut
            {"aring", new Integer(229)}, // � - lowercase a, ring
            {"aelig", new Integer(230)}, // � - lowercase ae
            {"ccedil", new Integer(231)}, // � - lowercase c, cedilla
            {"egrave", new Integer(232)}, // � - lowercase e, grave accent
            {"eacute", new Integer(233)}, // � - lowercase e, acute accent
            {"ecirc", new Integer(234)}, // � - lowercase e, circumflex accent
            {"euml", new Integer(235)}, // � - lowercase e, umlaut
            {"igrave", new Integer(236)}, // � - lowercase i, grave accent
            {"iacute", new Integer(237)}, // � - lowercase i, acute accent
            {"icirc", new Integer(238)}, // � - lowercase i, circumflex accent
            {"iuml", new Integer(239)}, // � - lowercase i, umlaut
            {"eth", new Integer(240)}, // � - lowercase eth, Icelandic
            {"ntilde", new Integer(241)}, // � - lowercase n, tilde
            {"ograve", new Integer(242)}, // � - lowercase o, grave accent
            {"oacute", new Integer(243)}, // � - lowercase o, acute accent
            {"ocirc", new Integer(244)}, // � - lowercase o, circumflex accent
            {"otilde", new Integer(245)}, // � - lowercase o, tilde
            {"ouml", new Integer(246)}, // � - lowercase o, umlaut
            {"divide", new Integer(247)}, // division sign
            {"oslash", new Integer(248)}, // � - lowercase o, slash
            {"ugrave", new Integer(249)}, // � - lowercase u, grave accent
            {"uacute", new Integer(250)}, // � - lowercase u, acute accent
            {"ucirc", new Integer(251)}, // � - lowercase u, circumflex accent
            {"uuml", new Integer(252)}, // � - lowercase u, umlaut
            {"yacute", new Integer(253)}, // � - lowercase y, acute accent
            {"thorn", new Integer(254)}, // � - lowercase thorn, Icelandic
            {"yuml", new Integer(255)}, // � - lowercase y, umlaut
            {"euro", new Integer(8364)}, // Euro symbol
        };

        private Map mapNameToValue;
        private Map mapValueToName;

        public Entities() {
            mapNameToValue = new HashMap();
            mapValueToName = new HashMap();
            for (int i = 0; i < entities.length; ++i) {
                mapNameToValue.put(entities[i][0], entities[i][1]);
                mapValueToName.put(entities[i][1], entities[i][0]);
            }
        }

        public String entityName(int value) {
            return (String) mapValueToName.get(new Integer(value));
        }

        public Integer entityValue(String name) {
            return (Integer) mapNameToValue.get(name);
        }
    }

    private static Entities entities;

    private static void initEntities() {
        if (entities == null)
            entities = new Entities();
    }

    private static String entityName(char ch) {
        initEntities();
        return entities.entityName(ch);
    }

    private static Integer entityValue(String entity) {
        initEntities();
        return entities.entityValue(entity);
    }

    /**
     * <p>Turns funky characters into HTML entity equivalents.</p>
     * For example: <tt>"bread" & "butter"</tt> => <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
     * Supports all known HTML entities, including funky accents. See the source code for more detail.
     * see http://hotwired.lycos.com/webmonkey/reference/special_characters/
     * @see #unescapeHtml(String)
     **/
    public static String escapeHtml(String str) {
        StringBuffer buf = new StringBuffer(str.length() * 2);
        int i;
        for (i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            String entity = entityName(ch);
            if (entity == null) {
                if (((int) ch) > 128) {   // should this be 127 or 128?
                    int intValue = ((int) ch);
                    buf.append("&#" + intValue + ";");
                } else {
                    buf.append(ch);
                }
            } else {
                buf.append("&" + entity + ";");
            }
        }
        return buf.toString();
    }

    /**
     * Given a string containing entity escapes, returns a string
     * containing the actual Unicode characters corresponding to the
     * escapes.
     *
     * @see #escapeHtml(String)
     **/
    public static String unescapeHtml(String str) {
        StringBuffer buf = new StringBuffer(str.length());
        int i;
        for (i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (ch == '&') {
                int semi = str.indexOf(';', i + 1);
                if (semi == -1) {
                    buf.append(ch);
                    continue;
                }
                String entity = str.substring(i + 1, semi);
                Integer iso;
                if (entity.charAt(0) == '#') {
                    iso = new Integer(entity.substring(1));
                } else {
                    iso = entityValue(entity);
                }
                if (iso == null) {
                    buf.append("&" + entity + ";");
                } else {
                    buf.append((char) (iso.intValue()));
                }
                i = semi;
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

}

