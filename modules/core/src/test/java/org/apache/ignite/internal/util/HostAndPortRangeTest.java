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

package org.apache.ignite.internal.util;

import java.net.UnknownHostException;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.testframework.junits.common.GridCommonAbstractTest;
import org.apache.ignite.testframework.junits.common.GridCommonTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests HostAndPortRange parse method.
 */
@GridCommonTest(group = "Utils")
public class HostAndPortRangeTest extends GridCommonAbstractTest {
    /**
     * Tests correct input address with IPv4 host and port range.
     *
     * @throws IgniteCheckedException on incorrect host/port
     */
    @Test
    public void testParseIPv4WithPortRange() throws IgniteCheckedException {
        String addrStr = "127.0.0.1:8080..8090";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        HostAndPortRange actual = HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix);
        HostAndPortRange expected = new HostAndPortRange("127.0.0.1", 8080, 8090);
        assertEquals(expected, actual);
    }

    /**
     * Tests correct input address with IPv4 host and single port.
     *
     * @throws IgniteCheckedException on incorrect host/port
     */
    @Test
    public void testParseIPv4WithSinglePort() throws IgniteCheckedException {
        String addrStr = "127.0.0.1:8080";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        HostAndPortRange actual = HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix);
        HostAndPortRange expected = new HostAndPortRange("127.0.0.1", 8080, 8080);
        assertEquals(expected, actual);
    }

    /**
     * Tests correct input address with IPv4 host and no port.
     *
     * @throws IgniteCheckedException on incorrect host/port
     */
    @Test
    public void testParseIPv4NoPort() throws IgniteCheckedException {
        String addrStr = "127.0.0.1";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        HostAndPortRange actual = HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix);
        HostAndPortRange expected = new HostAndPortRange("127.0.0.1", 18360, 18362);
        assertEquals(expected, actual);
    }

    /**
     * Tests correct input address with IPv6 host and port range.
     *
     * @throws IgniteCheckedException on incorrect host/port
     */
    @Test
    public void testParseIPv6WithPortRange() throws IgniteCheckedException {
        String addrStr = "[::1]:8080..8090";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        HostAndPortRange actual = HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix);
        HostAndPortRange expected = new HostAndPortRange("::1", 8080, 8090);
        assertEquals(expected, actual);
    }

    /**
     * Tests correct input address with IPv6 host and single port.
     *
     * @throws IgniteCheckedException on incorrect host/port
     */
    @Test
    public void testParseIPv6WithSinglePort() throws IgniteCheckedException {
        String addrStr = "[3ffe:2a00:100:7031::]:8080";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        HostAndPortRange actual = HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix);
        HostAndPortRange expected = new HostAndPortRange("3ffe:2a00:100:7031::", 8080, 8080);
        assertEquals(expected, actual);
    }

    /**
     * Tests correct input address with IPv6 host and no port.
     *
     * @throws IgniteCheckedException on incorrect host/port
     */
    @Test
    public void testParseIPv6NoPort() throws IgniteCheckedException {
        String addrStr = "::FFFF:129.144.52.38";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        HostAndPortRange actual = HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix);
        HostAndPortRange expected = new HostAndPortRange("::FFFF:129.144.52.38", 18360, 18362);
        assertEquals(expected, actual);
    }

    /**
     * Tests incorrect input address with IPv6 host (no brackets) and port.
     *
     */
    @Test
    public void testParseIPv6IncorrectHost() {
        String addrStr = "3ffe:2a00:100:7031";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        IgniteCheckedException exception = Assertions.assertThrows(IgniteCheckedException.class,
                () -> HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix));
        Assertions.assertTrue(exception.getMessage().contains("IPv6 is incorrect"));
        Assertions.assertTrue(exception.getCause() instanceof UnknownHostException);
    }

    /**
     * Tests empty host and port.
     *
     */
    @Test
    public void testParseNoHost() {
        String addrStr = ":8080";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        IgniteCheckedException exception = Assertions.assertThrows(IgniteCheckedException.class,
                () -> HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix));
        Assertions.assertTrue(exception.getMessage().contains("Host name is empty"));
    }

    /**
     * Tests empty address string.
     *
     */
    @Test
    public void testParseNoAddress() {
        String addrStr = "";
        String errMsgPrefix = "";
        int dfltPortFrom = 18360;
        int dfltPortTo = 18362;
        IgniteCheckedException exception = Assertions.assertThrows(IgniteCheckedException.class,
                () -> HostAndPortRange.parse(addrStr, dfltPortFrom, dfltPortTo, errMsgPrefix));
        Assertions.assertTrue(exception.getMessage().contains("Address is empty"));
    }
}
