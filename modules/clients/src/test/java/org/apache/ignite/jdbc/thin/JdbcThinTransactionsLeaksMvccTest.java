/*
 * Copyright 2019 GridGain Systems, Inc. and Contributors.
 * 
 * Licensed under the GridGain Community Edition License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gridgain.com/products/software/community-edition/gridgain-community-edition-license
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.jdbc.thin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentMap;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.internal.IgniteEx;
import org.apache.ignite.internal.processors.query.NestedTxMode;
import org.apache.ignite.internal.processors.query.h2.ConnectionManager;
import org.apache.ignite.internal.processors.query.h2.IgniteH2Indexing;
import org.apache.ignite.testframework.GridTestUtils;
import org.junit.Test;

/**
 * Tests to check leaks at the ConnectionManager#detachedConns map.
 */
public class JdbcThinTransactionsLeaksMvccTest extends JdbcThinAbstractSelfTest {
    /** */
    private static final String URL = "jdbc:ignite:thin://127.0.0.1";

    /** Keys count. */
    private static final int KEYS = 10;

    /** Iterations count. */
    private static final int ITERATIONS = 1_000;

    /** {@inheritDoc} */
    @Override protected IgniteConfiguration getConfiguration(String igniteInstanceName) throws Exception {
        return super.getConfiguration(igniteInstanceName)
            .setSystemWorkerBlockedTimeout(Long.MAX_VALUE);
    }

    /** {@inheritDoc} */
    @Override protected void beforeTest() throws Exception {
        super.beforeTest();

        startGrids(3);

        try (Connection c = c(true, NestedTxMode.ERROR)) {
            try (Statement s = c.createStatement()) {
                s.execute("CREATE TABLE TEST (k int primary key, v int) WITH \"atomicity=transactional_snapshot\"");

                for (int i = 0; i < KEYS; ++i)
                    s.execute("INSERT INTO TEST VALUES (" + i +", " + i + ")");

            }
        }
    }

    /** {@inheritDoc} */
    @Override protected void afterTest() throws Exception {
        stopAllGrids();
    }

    /**
     * @param autoCommit Auto commit mode.
     * @param nestedTxMode Nested transactions mode.
     * @return Connection.
     * @throws SQLException if failed.
     */
    private static Connection c(boolean autoCommit, NestedTxMode nestedTxMode) throws SQLException {
        Connection res = DriverManager.getConnection(URL + "/?nestedTransactionsMode=" + nestedTxMode.name());

        res.setAutoCommit(autoCommit);

        return res;
    }

    /**
     *
     */
    @Test
    public void testLeaks() {
        runQueries(ITERATIONS);

        int prevDetachedConns = detachedConnectionCount(grid(0));

        runQueries(ITERATIONS * 2);

        int curDetachedConns = detachedConnectionCount(grid(0));

        assertTrue("Detached connection leaks: prevSize=" + prevDetachedConns + ", curSize=" + curDetachedConns,
            curDetachedConns < prevDetachedConns * 2 + 1);
    }

    /**
     * @param iters Count of queries.
     */
    private void runQueries(int iters) {
        for (int i = 0; i < iters; ++i) {
            try (Connection c = c(false, NestedTxMode.ERROR)) {
                try (Statement s = c.createStatement()) {
                    s.execute("BEGIN");

                    s.execute("SELECT * FROM TEST");

                    ResultSet rs = s.getResultSet();

                    int cnt = 0;

                    while (rs.next())
                        ++cnt;

                    assertEquals(KEYS, cnt);

                    c.commit();
                }
            }
            catch (SQLException e) {
                throw new AssertionError(e);
            }
        }
    }

    /**
     * @param igx Node.
     * @return Count of detached connections.
     */
    private int detachedConnectionCount(IgniteEx igx) {
        ConnectionManager connMgr = ((IgniteH2Indexing)igx.context().query().getIndexing()).connections();

        ConcurrentMap detachedConns = GridTestUtils.getFieldValue(connMgr, "detachedConns");

        return detachedConns.size();
    }
}