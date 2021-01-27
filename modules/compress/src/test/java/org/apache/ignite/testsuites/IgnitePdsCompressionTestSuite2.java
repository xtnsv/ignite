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

import org.apache.ignite.internal.processors.cache.distributed.dht.preloader.HistoricalRebalanceHeuristicsTest;
import org.apache.ignite.internal.processors.cache.persistence.IgniteDataStorageMetricsSelfTest;
import org.apache.ignite.internal.processors.cache.persistence.IgnitePdsCorruptedStoreTest;
import org.apache.ignite.internal.processors.cache.persistence.IgnitePdsExchangeDuringCheckpointTest;
import org.apache.ignite.internal.processors.cache.persistence.IgnitePdsPageSizesTest;
import org.apache.ignite.internal.processors.cache.persistence.IgnitePdsPartitionsStateRecoveryTest;
import org.apache.ignite.internal.processors.cache.persistence.IgnitePersistentStoreDataStructuresTest;
import org.apache.ignite.internal.processors.cache.persistence.IgniteRebalanceScheduleResendPartitionsTest;
import org.apache.ignite.internal.processors.cache.persistence.WALPreloadingWithCompactionTest;
import org.apache.ignite.internal.processors.cache.persistence.WalPreloadingConcurrentTest;
import org.apache.ignite.internal.processors.cache.persistence.db.FullHistRebalanceOnClientStopTest;
import org.apache.ignite.internal.processors.cache.persistence.db.IgnitePdsRebalancingOnNotStableTopologyTest;
import org.apache.ignite.internal.processors.cache.persistence.db.IgnitePdsReserveWalSegmentsTest;
import org.apache.ignite.internal.processors.cache.persistence.db.IgnitePdsReserveWalSegmentsWithCompactionTest;
import org.apache.ignite.internal.processors.cache.persistence.db.IgnitePdsWholeClusterRestartTest;
import org.apache.ignite.internal.processors.cache.persistence.db.IgniteShutdownOnSupplyMessageFailureTest;
import org.apache.ignite.internal.processors.cache.persistence.db.SlowHistoricalRebalanceSmallHistoryTest;
import org.apache.ignite.internal.processors.cache.persistence.db.checkpoint.CheckpointFailBeforeWriteMarkTest;
import org.apache.ignite.internal.processors.cache.persistence.db.checkpoint.CheckpointFreeListTest;
import org.apache.ignite.internal.processors.cache.persistence.db.checkpoint.CheckpointListenerForRegionTest;
import org.apache.ignite.internal.processors.cache.persistence.db.checkpoint.CheckpointStartLoggingTest;
import org.apache.ignite.internal.processors.cache.persistence.db.checkpoint.IgniteCheckpointDirtyPagesForLowLoadTest;
import org.apache.ignite.internal.processors.cache.persistence.db.checkpoint.LightweightCheckpointTest;
import org.apache.ignite.internal.processors.cache.persistence.db.filename.IgniteUidAsConsistentIdMigrationTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.FsyncWalRolloverDoesNotBlockTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteLocalWalSizeTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteNodeStoppedDuringDisableWALTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWALTailIsReachedDuringIterationOverArchiveTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalFlushBackgroundSelfTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalFlushBackgroundWithMmapBufferSelfTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalFlushFailoverTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalFlushLogOnlySelfTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalFlushLogOnlyWithMmapBufferSelfTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalFormatFileFailoverTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalHistoryReservationsTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalIteratorExceptionDuringReadTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalIteratorSwitchSegmentTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalRebalanceLoggingTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalRecoverySeveralRestartsTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalReplayingAfterRestartTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.IgniteWalSerializerVersionTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalCompactionNoArchiverTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalCompactionSwitchOnTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalCompactionTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalDeletionArchiveFsyncTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalDeletionArchiveLogOnlyTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WalRolloverTypesTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.WriteAheadLogManagerSelfTest;
import org.apache.ignite.internal.processors.cache.persistence.db.wal.reader.IgniteWalReaderTest;
import org.apache.ignite.internal.processors.cache.persistence.freelist.FreeListCachingTest;
import org.apache.ignite.internal.processors.cache.persistence.wal.reader.FilteredWalIteratorTest;
import org.apache.ignite.internal.processors.cache.persistence.wal.reader.StandaloneWalRecordsIteratorTest;
import org.apache.ignite.internal.processors.cache.persistence.wal.scanner.WalScannerTest;
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
        IgnitePdsPageSizesTest.class,

        // Metrics test.
        IgniteDataStorageMetricsSelfTest.class,
        IgnitePdsRebalancingOnNotStableTopologyTest.class,
        IgnitePdsWholeClusterRestartTest.class,

        // Rebalancing test
        IgniteWalHistoryReservationsTest.class,
        SlowHistoricalRebalanceSmallHistoryTest.class,
        IgniteShutdownOnSupplyMessageFailureTest.class,
        IgnitePersistentStoreDataStructuresTest.class,
        FullHistRebalanceOnClientStopTest.class,

        // Failover test
        IgniteWalFlushFailoverTest.class,
        IgniteWalFlushBackgroundSelfTest.class,
        IgniteWalFlushBackgroundWithMmapBufferSelfTest.class,
        IgniteWalFlushLogOnlySelfTest.class,
        IgniteWalFlushLogOnlyWithMmapBufferSelfTest.class,
        IgniteWalFormatFileFailoverTest.class,

        // Test suite uses Standalone WAL iterator to verify PDS content.
        IgniteWalReaderTest.class,
        IgnitePdsExchangeDuringCheckpointTest.class,
        IgnitePdsReserveWalSegmentsTest.class,
        IgnitePdsReserveWalSegmentsWithCompactionTest.class,
        IgniteWalReplayingAfterRestartTest.class,

        // new style folders with generated consistent ID test
        IgniteUidAsConsistentIdMigrationTest.class,
        IgniteWalSerializerVersionTest.class,
        WalCompactionTest.class,
        WalCompactionNoArchiverTest.class,
        WalCompactionSwitchOnTest.class,
        WalDeletionArchiveFsyncTest.class,
        WalDeletionArchiveLogOnlyTest.class,

        IgniteCheckpointDirtyPagesForLowLoadTest.class,
        IgnitePdsCorruptedStoreTest.class,
        CheckpointFailBeforeWriteMarkTest.class,
        CheckpointFreeListTest.class,
        CheckpointListenerForRegionTest.class,
        LightweightCheckpointTest.class,
        CheckpointStartLoggingTest.class,
        FreeListCachingTest.class,
        IgniteWalIteratorSwitchSegmentTest.class,
        IgniteWalIteratorExceptionDuringReadTest.class,
        IgniteNodeStoppedDuringDisableWALTest.class,
        StandaloneWalRecordsIteratorTest.class,
        FilteredWalIteratorTest.class,
        WalScannerTest.class,
        IgniteWalRecoverySeveralRestartsTest.class,
        IgniteRebalanceScheduleResendPartitionsTest.class,
        IgniteWALTailIsReachedDuringIterationOverArchiveTest.class,
        WalRolloverTypesTest.class,
        FsyncWalRolloverDoesNotBlockTest.class,
        IgnitePdsPartitionsStateRecoveryTest.class,
        WalPreloadingConcurrentTest.class,
        WALPreloadingWithCompactionTest.class,
        IgniteWalRebalanceLoggingTest.class,
        HistoricalRebalanceHeuristicsTest.class,
        IgniteLocalWalSizeTest.class,
        WriteAheadLogManagerSelfTest.class,
})
public class IgnitePdsCompressionTestSuite2 {

    @BeforeAll
    public static void enableCompressionByDefault() {
        System.setProperty(IGNITE_DEFAULT_DISK_PAGE_COMPRESSION, ZSTD.name());
        System.setProperty(IGNITE_DEFAULT_DATA_STORAGE_PAGE_SIZE, String.valueOf(8 * 1024));
    }
}
