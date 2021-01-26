/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.testframework.junits;

import org.junit.jupiter.api.Assertions;

/**
 * Provides the basic functionality of {@link Assertions} methods in org.junit package.
 * Corresponding methods must be used in all ignite tests where necessary.
 */
class JUnitAssertAware {
    /** See {@link Assertions#assertTrue(boolean, String)} javadocs. */
    protected static void assertTrue(String msg, boolean cond) {
        Assertions.assertTrue(cond, msg);
    }

    /** See {@link Assertions#assertTrue(boolean)} javadocs. */
    protected static void assertTrue(boolean cond) {
        Assertions.assertTrue(cond);
    }

    /** See {@link Assertions#assertFalse(boolean, String)} javadocs. */
    protected static void assertFalse(String msg, boolean cond) {
        Assertions.assertFalse(cond, msg);
    }

    /** See {@link Assertions#assertFalse(boolean)} javadocs. */
    protected static void assertFalse(boolean cond) {
        Assertions.assertFalse(cond);
    }

    /** See {@link Assertions#fail(String)} javadocs. */
    protected static void fail(String msg) {
        Assertions.fail(msg);
    }

    /** See {@link Assertions#fail()} javadocs. */
    protected static void fail() {
        Assertions.fail();
    }

    /** See {@link Assertions#assertEquals(Object, Object)} javadocs. */
    protected static void assertEquals(Object exp, Object actual) {
        Assertions.assertEquals(exp, actual);
    }

    /** See {@link Assertions#assertEquals(Object, Object)} javadocs. */
    protected static void assertEquals(String exp, String actual) {
        Assertions.assertEquals(exp, actual);
    }

    /** See {@link Assertions#assertEquals(Object, Object)} javadocs. */
    protected static void assertEquals(boolean exp, boolean actual) {
        Assertions.assertEquals(exp, actual);
    }

    /** See {@link Assertions#assertEquals(Object, Object, String)} javadocs. */
    protected static void assertEquals(String msg, Object exp, Object actual) {
        Assertions.assertEquals(exp, actual, msg);
    }

    /** See {@link Assertions#assertEquals(Object, Object, String)} javadocs. */
    protected static void assertEquals(String msg, String exp, String actual) {
        Assertions.assertEquals(exp, actual, msg);
    }

    /** See {@link Assertions#assertEquals(Object, Object, String)} javadocs. */
    protected static void assertEquals(String msg, boolean exp, boolean actual) {
        Assertions.assertEquals(exp, actual, msg);
    }

    /** See {@link Assertions#assertEquals(long, long)} javadocs. */
    protected static void assertEquals(long exp, long actual) {
        Assertions.assertEquals(exp, actual);
    }

    /** See {@link Assertions#assertEquals(long, long)} javadocs. */
    protected static void assertEquals(int exp, int actual) {
        Assertions.assertEquals(exp, actual);
    }

    /** See {@link Assertions#assertEquals(long, long)} javadocs. */
    protected static void assertEquals(byte exp, byte actual) {
        Assertions.assertEquals(exp, actual);
    }

    /** See {@link Assertions#assertEquals(long, long)} javadocs. */
    protected static void assertEquals(char exp, char actual) {
        Assertions.assertEquals(exp, actual);
    }

    /** See {@link Assertions#assertEquals(long, long)} javadocs. */
    protected static void assertEquals(short exp, short actual) {
        Assertions.assertEquals(exp, actual);
    }

    /** See {@link Assertions#assertEquals(long, long, String)} javadocs. */
    protected static void assertEquals(String msg, long exp, long actual) {
        Assertions.assertEquals(exp, actual, msg);
    }

    /** See {@link Assertions#assertEquals(long, long, String)} javadocs. */
    protected static void assertEquals(String msg, int exp, int actual) {
        Assertions.assertEquals(exp, actual, msg);
    }

    /** See {@link Assertions#assertEquals(double, double, double)} javadocs. */
    protected static void assertEquals(double exp, double actual, double delta) {
        Assertions.assertEquals(exp, actual, delta);
    }

    /** See {@link Assertions#assertEquals(double, double, double)} javadocs. */
    protected static void assertEquals(double exp, Double actual) {
        Assertions.assertEquals(exp, actual, 0);
    }

    /** See {@link Assertions#assertEquals(double, double, double)} javadocs. */
    protected static void assertEquals(Double exp, double actual) {
        Assertions.assertEquals(exp, actual, 0);
    }

    /** See {@link Assertions#assertEquals(double, double, double)} javadocs. */
    protected static void assertEquals(double exp, double actual) {
        Assertions.assertEquals(exp, actual, 0);
    }

    /** See {@link Assertions#assertEquals(double, double, double, String)} javadocs. */
    protected static void assertEquals(String msg, double exp, double actual, double delta) {
        Assertions.assertEquals(exp, actual, delta, msg);
    }

    /** See {@link Assertions#assertEquals(double, double, double, String)} javadocs. */
    protected static void assertEquals(String msg, double exp, double actual) {
        Assertions.assertEquals(exp, actual, 0, msg);
    }

    /** See {@link Assertions#assertEquals(float, float, float)} javadocs. */
    protected static void assertEquals(float exp, float actual, float delta) {
        Assertions.assertEquals(exp, actual, delta);
    }

    /** See {@link Assertions#assertEquals(float, float, float)} javadocs. */
    protected static void assertEquals(float exp, float actual) {
        Assertions.assertEquals(exp, actual, 0);
    }

    /** See {@link Assertions#assertEquals(float, float, float)} javadocs. */
    protected static void assertEquals(float exp, Float actual) {
        Assertions.assertEquals(exp, actual, 0);
    }

    /** See {@link Assertions#assertEquals(long, long, String)} javadocs. */
    protected static void assertEquals(String msg, byte exp, byte actual) {
        Assertions.assertEquals(exp, actual, msg);
    }

    /** See {@link Assertions#assertNull(Object)} javadocs. */
    protected static void assertNull(Object obj) {
        Assertions.assertNull(obj);
    }

    /** See {@link Assertions#assertNull(Object)} javadocs. */
    protected static void assertNotNull(Object obj) {
        Assertions.assertNotNull(obj);
    }

    /** See {@link Assertions#assertNotNull(Object, String)} javadocs. */
    protected static void assertNotNull(String msg, Object obj) {
        Assertions.assertNotNull(obj, msg);
    }

    /** See {@link Assertions#assertNull(Object, String)} javadocs. */
    protected static void assertNull(String msg, Object obj) {
        Assertions.assertNull(obj, msg);
    }

    /** See {@link Assertions#assertSame(Object, Object)} javadocs. */
    protected static void assertSame(Object exp, Object actual) {
        Assertions.assertSame(exp, actual);
    }

    /** See {@link Assertions#assertNotSame(Object, Object)} javadocs. */
    protected static void assertNotSame(Object unexpected, Object actual) {
        Assertions.assertNotSame(unexpected, actual);
    }

    /** See {@link Assertions#assertNotSame(Object, Object, String)} javadocs. */
    protected static void assertNotSame(String msg, Object exp, Object actual) {
        Assertions.assertNotSame(exp, actual, msg);
    }
}
