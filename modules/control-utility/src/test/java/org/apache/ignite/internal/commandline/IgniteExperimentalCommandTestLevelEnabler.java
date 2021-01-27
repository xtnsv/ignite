package org.apache.ignite.internal.commandline;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.apache.ignite.IgniteSystemProperties.IGNITE_ENABLE_EXPERIMENTAL_COMMAND;

public class IgniteExperimentalCommandTestLevelEnabler implements AfterEachCallback, BeforeEachCallback {

    private static final String KEY = IGNITE_ENABLE_EXPERIMENTAL_COMMAND;
    private static final String ENABLE = "true";
    private String cachedBeforeEachPropValue;

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        if (cachedBeforeEachPropValue != null)
            System.setProperty(KEY, cachedBeforeEachPropValue);
    }

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        cachedBeforeEachPropValue = System.getProperty(KEY);
        System.setProperty(KEY, ENABLE);
    }
}
