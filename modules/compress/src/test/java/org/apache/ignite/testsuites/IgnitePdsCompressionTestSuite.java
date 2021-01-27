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

package org.apache.ignite.testsuites;

import org.apache.ignite.internal.processors.cache.*;
import org.apache.ignite.internal.processors.cache.persistence.*;
import org.apache.ignite.internal.processors.cache.persistence.db.IgnitePdsCacheRestoreTest;
import org.apache.ignite.internal.processors.cache.persistence.db.IgnitePdsDataRegionMetricsTest;
import org.apache.ignite.internal.processors.cache.persistence.db.IgnitePdsWithTtlTest;
import org.apache.ignite.internal.processors.cache.persistence.db.IgnitePdsWithTtlTest2;
import org.apache.ignite.internal.processors.cache.persistence.db.file.DefaultPageSizeBackwardsCompatibilityTest;
import org.apache.ignite.internal.processors.cache.persistence.db.file.IgnitePdsCheckpointSimpleTest;
import org.apache.ignite.internal.processors.cache.persistence.db.file.IgnitePdsCheckpointSimulationWithRealCpDisabledTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgnitePdsCheckpointSimulationWithRealCpDisabledAndWalCompressionTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalCompactionAndPageCompressionTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalRecoveryWithPageCompressionAndTdeTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalRecoveryWithPageCompressionTest;
import org.apache.ignite.internal.processors.cache.persistence.metastorage.IgniteMetaStorageBasicTest;
import org.apache.ignite.internal.processors.compress.CompressionConfigurationTest;
import org.apache.ignite.internal.processors.compress.CompressionProcessorTest;
import org.apache.ignite.internal.processors.compress.DiskPageCompressionConfigValidationTest;
import org.apache.ignite.internal.processors.compress.DiskPageCompressionIntegrationAsyncTest;
import org.apache.ignite.internal.processors.compress.DiskPageCompressionIntegrationTest;
import org.apache.ignite.internal.processors.compress.FileSystemUtilsTest;
import org.apache.ignite.internal.processors.compress.WalPageCompressionIntegrationTest;
import org.apache.ignite.internal.processors.configuration.distributed.DistributedConfigurationPersistentTest;
import org.apache.ignite.internal.processors.database.*;
import org.apache.ignite.internal.processors.diagnostic.DiagnosticProcessorTest;
import org.apache.ignite.internal.processors.metastorage.DistributedMetaStoragePersistentTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

import static org.apache.ignite.IgniteSystemProperties.IGNITE_DEFAULT_DATA_STORAGE_PAGE_SIZE;
import static org.apache.ignite.IgniteSystemProperties.IGNITE_DEFAULT_DISK_PAGE_COMPRESSION;
import static org.apache.ignite.configuration.DiskPageCompression.ZSTD;

/** */
@RunWith(JUnitPlatform.class)
@SelectClasses(value = {
        CompressionConfigurationTest.class,
        CompressionProcessorTest.class,
        FileSystemUtilsTest.class,
        DiskPageCompressionIntegrationTest.class,
        DiskPageCompressionConfigValidationTest.class,
        DiskPageCompressionIntegrationAsyncTest.class,

        // WAL page records compression.
        WalPageCompressionIntegrationTest.class,
        WalRecoveryWithPageCompressionTest.class,
        WalRecoveryWithPageCompressionAndTdeTest.class,
        IgnitePdsCheckpointSimulationWithRealCpDisabledAndWalCompressionTest.class,
        WalCompactionAndPageCompressionTest.class,

        // Checkpointing smoke-test.
        IgnitePdsCheckpointSimulationWithRealCpDisabledTest.class,
        IgnitePdsCheckpointSimpleTest.class,

        // Basic API tests.
        IgniteDbSingleNodePutGetTest.class,
        IgniteDbMultiNodePutGetTest.class,
        IgniteDbSingleNodeTinyPutGetTest.class,
        IgniteDbDynamicCacheSelfTest.class,

        // Persistence-enabled.
        IgnitePdsSingleNodePutGetPersistenceTest.class,
        IgnitePdsDynamicCacheTest.class,
        IgnitePdsClientNearCachePutGetTest.class,
        IgniteDbPutGetWithCacheStoreTest.class,
        IgnitePdsWithTtlTest.class,
        IgnitePdsWithTtlTest2.class,
        IgnitePdsSporadicDataRecordsOnBackupTest.class,
        IgniteClusterActivateDeactivateTestWithPersistence.class,
        IgnitePdsCacheRestoreTest.class,
        IgnitePdsDataRegionMetricsTest.class,
        IgnitePdsDataRegionMetricsTxTest.class,
        IgnitePdsDestroyCacheTest.class,
        IgnitePdsRemoveDuringRebalancingTest.class,
        IgnitePdsDestroyCacheWithoutCheckpointsTest.class,
        IgnitePdsCacheConfigurationFileConsistencyCheckTest.class,
        DefaultPageSizeBackwardsCompatibilityTest.class,

        //MetaStorage
        IgniteMetaStorageBasicTest.class,
        DistributedMetaStoragePersistentTest.class,
        DistributedConfigurationPersistentTest.class,

        //Diagnostic
        DiagnosticProcessorTest.class,

        ActiveOnStartPropertyTest.class,
        AutoActivationPropertyTest.class,
        ClusterStateOnStartPropertyTest.class
})
public class IgnitePdsCompressionTestSuite {

    /**
     */
    @BeforeAll
    public static void enableCompressionByDefault() {
        System.setProperty(IGNITE_DEFAULT_DISK_PAGE_COMPRESSION, ZSTD.name());
        System.setProperty(IGNITE_DEFAULT_DATA_STORAGE_PAGE_SIZE, String.valueOf(8 * 1024));
    }
}
