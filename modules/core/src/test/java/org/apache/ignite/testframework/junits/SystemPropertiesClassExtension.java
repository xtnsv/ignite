package org.apache.ignite.testframework.junits;

import org.apache.ignite.internal.util.typedef.T2;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;
import java.util.Objects;

public class SystemPropertiesClassExtension implements BeforeAllCallback, AfterAllCallback {

    private static final SystemPropertiesRule RULE = new SystemPropertiesRule();
    private List<T2<String, String>> cachedProps;
    /**
     * Callback that is invoked once <em>after</em> all tests in the current
     * container.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterAll(ExtensionContext context) {
        Objects.requireNonNull(cachedProps);

        RULE.clearSystemProperties(cachedProps);
    }

    /**
     * Callback that is invoked once <em>before</em> all tests in the current
     * container.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeAll(ExtensionContext context) {
        context.getTestClass()
                .ifPresent(this::setSystemPropertiesBeforeClass);
    }

    /**
     * Set system properties before class.
     *
     * @param testCls Current test class.
     */
    private void setSystemPropertiesBeforeClass(Class<?> testCls) {
        cachedProps = RULE.setSystemPropertiesBeforeClass(testCls);
    }
}
