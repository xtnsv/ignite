package org.apache.ignite.internal.commandline;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.apache.ignite.IgniteSystemProperties.IGNITE_ENABLE_EXPERIMENTAL_COMMAND;

public class IgniteExperimentalCommandClassLevelEnabler implements AfterAllCallback, BeforeAllCallback {

    private static final String KEY = IGNITE_ENABLE_EXPERIMENTAL_COMMAND;
    private static final String ENABLE = "true";
    private String cachedBeforeAllPropValue;

    /**
     * Callback that is invoked once <em>after</em> all tests in the current
     * container.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterAll(ExtensionContext context) {
        if (cachedBeforeAllPropValue != null)
            System.setProperty(KEY, cachedBeforeAllPropValue);
    }

    /**
     * Callback that is invoked once <em>before</em> all tests in the current
     * container.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        cachedBeforeAllPropValue = System.getProperty(KEY);
        System.setProperty(KEY, ENABLE);
    }

}
