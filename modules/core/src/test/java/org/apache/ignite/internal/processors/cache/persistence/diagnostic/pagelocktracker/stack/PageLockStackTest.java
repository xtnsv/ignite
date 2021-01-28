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

package org.apache.ignite.internal.processors.cache.persistence.diagnostic.pagelocktracker.stack;

import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.internal.IgniteInternalFuture;
import org.apache.ignite.internal.processors.cache.persistence.diagnostic.pagelocktracker.AbstractPageLockTest;
import org.apache.ignite.internal.processors.cache.persistence.diagnostic.pagelocktracker.LockTrackerFactory;
import org.apache.ignite.internal.processors.cache.persistence.diagnostic.pagelocktracker.PageLockTracker;
import org.apache.ignite.testframework.GridTestUtils;
import org.apache.ignite.testframework.GridTestUtils.SF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.time.Duration.ofMinutes;
import static java.util.stream.IntStream.range;
import static org.apache.ignite.internal.processors.cache.persistence.diagnostic.pagelocktracker.PageLockTracker.BEFORE_READ_LOCK;

/** */
public abstract class PageLockStackTest extends AbstractPageLockTest {
    /** */
    protected static final int STRUCTURE_ID = 123;

    /** */
    protected abstract LockStack createLockStackTracer(String name);

    /** */
    @Test
    public void testOneReadPageLock() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId = 1;
        long page = 2;
        long pageAddr = 3;

        PageLockStackSnapshot dump;

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId, page);

        dump = lockStack.dump();

        Assertions.assertEquals(0, dump.headIdx);
        Assertions.assertTrue(dump.pageIdLocksStack.isEmpty(), String.valueOf(dump.pageIdLocksStack));
        Assertions.assertEquals(pageId, dump.nextOpPageId);
        Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId, page, pageAddr);

        dump = lockStack.dump();

        Assertions.assertEquals(1, dump.headIdx);
        Assertions.assertEquals(pageId, dump.pageIdLocksStack.getPageId(0));
        Assertions.assertEquals(0, dump.nextOpPageId);
        Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId, page, pageAddr);

        dump = lockStack.dump();

        Assertions.assertEquals(0, dump.headIdx);
        Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
        Assertions.assertEquals(0, dump.nextOpPageId);
        Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testTwoReadPageLock() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId1 = 1;
        long pageId2 = 11;
        long page1 = 2;
        long page2 = 12;
        long pageAddr1 = 3;
        long pageAddr2 = 13;

        PageLockStackSnapshot dump;

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId1, page1);

        dump = lockStack.dump();

        Assertions.assertEquals(0, dump.headIdx);
        Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
        Assertions.assertEquals(pageId1, dump.nextOpPageId);
        Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        dump = lockStack.dump();

        Assertions.assertEquals(1, dump.headIdx);
        Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
        Assertions.assertEquals(0, dump.nextOpPageId);
        Assertions.assertEquals(0, dump.nextOp);

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId2, page2);

        dump = lockStack.dump();

        Assertions.assertEquals(1, dump.headIdx);
        Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
        Assertions.assertEquals(pageId2, dump.nextOpPageId);
        Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

        Assertions.assertEquals(2, dump.headIdx);
        Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
        Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
        Assertions.assertEquals(0, dump.nextOpPageId);
        Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

        Assertions.assertEquals(1, dump.headIdx);
        Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
        Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(1));
        Assertions.assertEquals(0, dump.nextOpPageId);
        Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        dump = lockStack.dump();

        Assertions.assertEquals(0, dump.headIdx);
        Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
        Assertions.assertEquals(0, dump.nextOpPageId);
        Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testThreeReadPageLock_1() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId1 = 1;
        long pageId2 = 11;
        long pageId3 = 111;
        long page1 = 2;
        long page2 = 12;
        long page3 = 122;
        long pageAddr1 = 3;
        long pageAddr2 = 13;
        long pageAddr3 = 133;

        PageLockStackSnapshot dump;

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId1, page1);

        dump = lockStack.dump();

        Assertions.assertEquals(0, dump.headIdx);
        Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
        Assertions.assertEquals(pageId1, dump.nextOpPageId);
        Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        dump = lockStack.dump();

        Assertions.assertEquals(1, dump.headIdx);
        Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
        Assertions.assertEquals(0, dump.nextOpPageId);
        Assertions.assertEquals(0, dump.nextOp);

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId2, page2);

        dump = lockStack.dump();

        Assertions.assertEquals(1, dump.headIdx);
        Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
        Assertions.assertEquals(pageId2, dump.nextOpPageId);
        Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

        Assertions.assertEquals(2, dump.headIdx);
        Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
        Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
        Assertions.assertEquals(0, dump.nextOpPageId);
        Assertions.assertEquals(0, dump.nextOp);

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId3, page3);

        dump = lockStack.dump();

        Assertions.assertEquals(2, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(pageId3, dump.nextOpPageId);
		Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId3, page3, pageAddr3);

        dump = lockStack.dump();

		Assertions.assertEquals(3, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(pageId3, dump.pageIdLocksStack.getPageId(2));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId3, page3, pageAddr3);

        dump = lockStack.dump();

		Assertions.assertEquals(2, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(2));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(2));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        dump = lockStack.dump();

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testThreeReadPageLock_2() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId1 = 1;
        long pageId2 = 11;
        long pageId3 = 111;
        long page1 = 2;
        long page2 = 12;
        long page3 = 122;
        long pageAddr1 = 3;
        long pageAddr2 = 13;
        long pageAddr3 = 133;

        PageLockStackSnapshot dump;

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId1, page1);

        dump = lockStack.dump();

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(pageId1, dump.nextOpPageId);
		Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId2, page2);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.nextOpPageId);
		Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

		Assertions.assertEquals(2, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId3, page3);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId3, dump.nextOpPageId);
		Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId3, page3, pageAddr3);

        dump = lockStack.dump();

		Assertions.assertEquals(2, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId3, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId3, page3, pageAddr3);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        dump = lockStack.dump();

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testThreeReadPageLock_3() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId1 = 1;
        long pageId2 = 11;
        long pageId3 = 111;
        long page1 = 2;
        long page2 = 12;
        long page3 = 122;
        long pageAddr1 = 3;
        long pageAddr2 = 13;
        long pageAddr3 = 133;

        PageLockStackSnapshot dump;

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId1, page1);

        dump = lockStack.dump();

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(pageId1, dump.nextOpPageId);
		Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId2, page2);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.nextOpPageId);
		Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

		Assertions.assertEquals(2, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId3, page3);

        dump = lockStack.dump();

		Assertions.assertEquals(2, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(pageId3, dump.nextOpPageId);
		Assertions.assertEquals(BEFORE_READ_LOCK, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId3, page3, pageAddr3);

        dump = lockStack.dump();

		Assertions.assertEquals(3, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(pageId3, dump.pageIdLocksStack.getPageId(2));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

		Assertions.assertEquals(3, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(pageId3, dump.pageIdLocksStack.getPageId(2));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId3, page3, pageAddr3);

        dump = lockStack.dump();

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(0, dump.pageIdLocksStack.getPageId(2));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        dump = lockStack.dump();

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testUnlockUnexcpected() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId = 1;
        long page = 2;
        long pageAddr = 3;

        PageLockStackSnapshot dump;

        // Lock stack should be invalid after this operation because we can not unlock page
        // which was not locked.
        lockStack.onReadUnlock(STRUCTURE_ID, pageId, page, pageAddr);

        dump = lockStack.dump();

		Assertions.assertTrue(lockStack.isInvalid());
        String msg = lockStack.invalidContext().msg;

		Assertions.assertTrue(msg.contains("Stack is empty"), msg);

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testUnlockUnexcpectedOnNotEmptyStack() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId1 = 1;
        long pageId2 = 11;
        long page1 = 2;
        long page2 = 12;
        long pageAddr1 = 3;
        long pageAddr2 = 13;

        PageLockStackSnapshot dump;

        lockStack.onReadLock(STRUCTURE_ID, pageId1, page1, pageAddr1);

        // Lock stack should be invalid after this operation because we can not unlock page
        // which was not locked.
        lockStack.onReadUnlock(STRUCTURE_ID, pageId2, page2, pageAddr2);

        dump = lockStack.dump();

		Assertions.assertTrue(lockStack.isInvalid());
        String msg = lockStack.invalidContext().msg;

		Assertions.assertTrue(msg.contains("Can not find pageId in stack"), msg);

		Assertions.assertEquals(1, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testUnlockUnexcpectedOnNotEmptyStackMultiLocks() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId1 = 1;
        long pageId2 = 11;
        long pageId3 = 111;
        long pageId4 = 1111;
        long page1 = 2;
        long page2 = 12;
        long page3 = 122;
        long page4 = 1222;
        long pageAddr1 = 3;
        long pageAddr2 = 13;
        long pageAddr3 = 133;
        long pageAddr4 = 1333;

        PageLockStackSnapshot dump;

        lockStack.onReadLock(STRUCTURE_ID, pageId1, page1, pageAddr1);
        lockStack.onReadLock(STRUCTURE_ID, pageId2, page2, pageAddr2);
        lockStack.onReadLock(STRUCTURE_ID, pageId3, page3, pageAddr3);

        // Lock stack should be invalid after this operation because we can not unlock page
        // which was not locked.
        lockStack.onReadUnlock(STRUCTURE_ID, pageId4, page4, pageAddr4);

        dump = lockStack.dump();

		Assertions.assertTrue(lockStack.isInvalid());
        String msg = lockStack.invalidContext().msg;

		Assertions.assertTrue(msg.contains("Can not find pageId in stack"), msg);

		Assertions.assertEquals(3, dump.headIdx);
		Assertions.assertEquals(pageId1, dump.pageIdLocksStack.getPageId(0));
		Assertions.assertEquals(pageId2, dump.pageIdLocksStack.getPageId(1));
		Assertions.assertEquals(pageId3, dump.pageIdLocksStack.getPageId(2));
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testStackOverFlow() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId = 1;
        long page = 2;
        long pageAddr = 3;

        PageLockStackSnapshot dump;

        // Lock stack should be invalid after this operation because we can get lock more that
        // stack capacity, +1 for overflow.
        range(0, LockTrackerFactory.DEFAULT_CAPACITY + 1).forEach((i) -> {
            lockStack.onReadLock(STRUCTURE_ID, pageId, page, pageAddr);
        });

        dump = lockStack.dump();

		Assertions.assertTrue(lockStack.isInvalid());
		Assertions.assertTrue(lockStack.invalidContext().msg.contains("Stack overflow"));

		Assertions.assertEquals(LockTrackerFactory.DEFAULT_CAPACITY, dump.headIdx);
		Assertions.assertFalse(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testStackOperationAfterInvalid() {
        LockStack lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId = 1;
        long page = 2;
        long pageAddr = 3;

        PageLockStackSnapshot dump;

        // Lock stack should be invalid after this operation because we can not unlock page
        // which was not locked.
        lockStack.onReadUnlock(STRUCTURE_ID, pageId, page, pageAddr);

        dump = lockStack.dump();

		Assertions.assertTrue(lockStack.isInvalid());
        String msg = lockStack.invalidContext().msg;

		Assertions.assertTrue(msg.contains("Stack is empty"), msg);

        System.out.println(lockStack.invalidContext());

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onBeforeReadLock(STRUCTURE_ID, pageId, page);

		Assertions.assertTrue(lockStack.isInvalid());
        msg = lockStack.invalidContext().msg;

		Assertions.assertTrue(msg.contains("Stack is empty"), msg);

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadLock(STRUCTURE_ID, pageId, page, pageAddr);

		Assertions.assertTrue(lockStack.isInvalid());
        msg = lockStack.invalidContext().msg;

		Assertions.assertTrue(msg.contains("Stack is empty"), msg);

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);

        lockStack.onReadUnlock(STRUCTURE_ID, pageId, page, pageAddr);

		Assertions.assertTrue(lockStack.isInvalid());
        msg = lockStack.invalidContext().msg;

		Assertions.assertTrue(msg.contains("Stack is empty"), msg);

		Assertions.assertEquals(0, dump.headIdx);
		Assertions.assertTrue(dump.pageIdLocksStack.isEmpty());
		Assertions.assertEquals(0, dump.nextOpPageId);
		Assertions.assertEquals(0, dump.nextOp);
    }

    /** */
    @Test
    public void testThreadDump() throws IgniteCheckedException {
        PageLockTracker<PageLockStackSnapshot> lockStack = createLockStackTracer(Thread.currentThread().getName());

        long pageId = 1;
        long page = 2;
        long pageAddr = 3;

        int cntDumps = SF.applyLB(5_000, 1_000);

        AtomicBoolean done = new AtomicBoolean();

        int maxWaitTime = 500;

        int maxdeep = 16;

        IgniteInternalFuture f = GridTestUtils.runAsync(() -> {
            while (!done.get()) {
                int iter = nextRandomWaitTimeout(maxdeep);

                doRunnable(iter, () -> {
                    awaitRandom(100);

                    lockStack.onBeforeReadLock(STRUCTURE_ID, pageId, page);

                    awaitRandom(100);

                    lockStack.onReadLock(STRUCTURE_ID, pageId, page, pageAddr);
                });

                try {
                    awaitRandom(maxWaitTime);
                }
                finally {
                    doRunnable(iter, () -> {
                        lockStack.onReadUnlock(STRUCTURE_ID, pageId, page, pageAddr);
                    });
                }
            }
        });

        long totalExecutionTime = 0L;

        for (int i = 0; i < cntDumps; i++) {
            awaitRandom(50);

            long time = System.nanoTime();

            PageLockStackSnapshot dump = lockStack.dump();

            long dumpTime = System.nanoTime() - time;

            if (dump.nextOp != 0)
		Assertions.assertTrue(dump.nextOpPageId != 0);

		Assertions.assertTrue(dump.time != 0);
		Assertions.assertNotNull(dump.name);

            if (dump.headIdx > 0) {
                for (int itemIdx = 0; itemIdx < dump.headIdx; itemIdx++) {
		Assertions.assertTrue(dump.pageIdLocksStack.getPageId(itemIdx) != 0, String.valueOf(dump.headIdx));
		Assertions.assertTrue(dump.pageIdLocksStack.getOperation(itemIdx) != 0);
		Assertions.assertTrue(dump.pageIdLocksStack.getStructureId(itemIdx) != 0);
		Assertions.assertTrue(dump.pageIdLocksStack.getPageAddrHeader(itemIdx) != 0);
		Assertions.assertTrue(dump.pageIdLocksStack.getPageAddr(itemIdx) != 0);
                }
            }

		Assertions.assertNotNull(dump);

            totalExecutionTime += dumpTime;

		Assertions.assertTrue(dumpTime <= ofMinutes((long)(maxWaitTime + (maxWaitTime * 0.1))).toNanos());

            if (i != 0 && i % 100 == 0)
                System.out.println(">>> Dump:" + i);
        }

        done.set(true);

        f.get();

        System.out.println(">>> Avarage time dump creation:" + (totalExecutionTime / cntDumps) + " ns");
    }
}
