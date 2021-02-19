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

package org.apache.ignite.cache.store.jdbc.dialect;

import java.util.Collection;
import org.apache.ignite.internal.util.typedef.C1;
import org.apache.ignite.internal.util.typedef.F;

/**
 * A dialect compatible with the Oracle database.
 */
public class OracleDialect extends BasicJdbcDialect {
    /** */
    private static final long serialVersionUID = 0L;

    private static final String MERGE_ON = "MERGE INTO %s t USING (SELECT %s FROM dual) v ON %s";

    private static final String WHEN_MATCHED = " WHEN MATCHED THEN UPDATE SET %s";

    private static final String WHEN_NOT_MATCHED = " WHEN NOT MATCHED THEN INSERT (%s) VALUES (%s)";

    /** {@inheritDoc} */
    @Override public boolean hasMerge() {
        return true;
    }

    /** {@inheritDoc} */
    @Override public String loadCacheSelectRangeQuery(String fullTblName, Collection<String> keyCols) {
        String cols = mkString(keyCols, ",");

        return String.format("SELECT %1$s FROM (SELECT %1$s, ROWNUM AS rn FROM (SELECT %1$s FROM %2$s ORDER BY %1$s)) WHERE mod(rn, ?) = 0",
            cols, fullTblName);
    }

    /** {@inheritDoc} */
    @Override public String mergeQuery(String fullTblName, Collection<String> keyCols, Collection<String> uniqCols) {
        Collection<String> cols = F.concat(false, keyCols, uniqCols);

        String colsLst = mkString(cols, ", ");

        String selCols = mkString(cols, new C1<String, String>() {
            @Override public String apply(String col) {
                return String.format("? AS %s", col);
            }
        }, "", ", ", "");

        String match = mkString(keyCols, new C1<String, String>() {
            @Override public String apply(String col) {
                return String.format("t.%s=v.%s", col, col);
            }
        }, "(", " AND ", ")");

        String valuesCols = mkString(cols, new C1<String, String>() {
            @Override public String apply(String col) {
                return "v." + col;
            }
        }, "", ", ", "");

        String setCols = mkString(uniqCols, new C1<String, String>() {
            @Override public String apply(String col) {
                return String.format("t.%s = v.%s", col, col);
            }
        }, "", ", ", "");

        String resulting = String.format(MERGE_ON, fullTblName, selCols, match);

        String whenNotMatched = String.format(WHEN_NOT_MATCHED, colsLst, valuesCols);

        return resulting + (uniqCols.isEmpty() ? whenNotMatched :
                String.format(WHEN_MATCHED, setCols) + whenNotMatched);

    }
}
