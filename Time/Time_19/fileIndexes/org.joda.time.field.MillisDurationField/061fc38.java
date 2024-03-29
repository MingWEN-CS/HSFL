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
package org.joda.time.field;

import java.io.Serializable;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;

/**
 * Duration field class representing a field with a fixed unit length of one
 * millisecond.
 * <p>
 * MillisDurationField is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public final class MillisDurationField extends DurationField implements Serializable {

    /** Serialization lock. */
    private static final long serialVersionUID = 2656707858124633367L;

    /** Singleton instance. */
    public static final DurationField INSTANCE = new MillisDurationField();

    /**
     * Restricted constructor.
     */
    private MillisDurationField() {
        super();
    }
    
    //------------------------------------------------------------------------
    public DurationFieldType getType() {
        return DurationFieldType.millis();
    }

    public String getName() {
        return "millis";
    }

    /**
     * Returns true as this field is supported.
     * 
     * @return true always
     */
    public boolean isSupported() {
        return true;
    }

    /**
     * Returns true as this field is precise.
     * 
     * @return true always
     */
    public final boolean isPrecise() {
        return true;
    }

    /**
     * Returns the amount of milliseconds per unit value of this field.
     *
     * @return one always
     */
    public final long getUnitMillis() {
        return 1;
    }

    //------------------------------------------------------------------------
    public int getValue(long duration) {
        return FieldUtils.safeToInt(duration);
    }

    public long getValueAsLong(long duration) {
        return duration;
    }

    public int getValue(long duration, long instant) {
        return FieldUtils.safeToInt(duration);
    }

    public long getValueAsLong(long duration, long instant) {
        return duration;
    }

    public long getMillis(int value) {
        return value;
    }

    public long getMillis(long value) {
        return value;
    }

    public long getMillis(int value, long instant) {
        return value;
    }

    public long getMillis(long value, long instant) {
        return value;
    }

    public long add(long instant, int value) {
        return FieldUtils.safeAdd(instant, value);
    }

    public long add(long instant, long value) {
        return FieldUtils.safeAdd(instant, value);
    }

    public int getDifference(long minuendInstant, long subtrahendInstant) {
        return FieldUtils.safeToInt(FieldUtils.safeSubtract(minuendInstant, subtrahendInstant));
    }

    public long getDifferenceAsLong(long minuendInstant, long subtrahendInstant) {
        return FieldUtils.safeSubtract(minuendInstant, subtrahendInstant);
    }

    //------------------------------------------------------------------------
    public int compareTo(Object durationField) {
        DurationField otherField = (DurationField) durationField;
        long otherMillis = otherField.getUnitMillis();
        long thisMillis = getUnitMillis();
        // cannot do (thisMillis - otherMillis) as can overflow
        if (thisMillis == otherMillis) {
            return 0;
        }
        if (thisMillis < otherMillis) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Get a suitable debug string.
     * 
     * @return debug string
     */
    public String toString() {
        return "DurationField[millis]";
    }

    /**
     * Deserialize to the singleton.
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
