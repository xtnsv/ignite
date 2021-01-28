package org.apache.ignite.testframework.junits;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestNameExtension implements BeforeEachCallback {

    private volatile String testName;

    /**
     * @return the name of the currently-running test method
     */
    public String getMethodName() {
        return testName;
    }

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        testName = context.getDisplayName();
    }
}
