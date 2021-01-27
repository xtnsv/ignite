package org.apache.ignite.testframework.junits;

import org.apache.ignite.internal.util.typedef.T2;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class SystemPropertiesMethodExtension implements BeforeEachCallback, AfterEachCallback {

    private final SystemPropertiesRule rule = new SystemPropertiesRule();
    private List<T2<String, String>> cachedProps;

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        Objects.requireNonNull(cachedProps);

        rule.clearSystemProperties(cachedProps);
    }

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context) {
        context.getTestMethod()
                .ifPresent(this::setSystemPropertiesBeforeMethod);
    }

    private void setSystemPropertiesBeforeMethod(Method method) {
        cachedProps = rule.setSystemPropertiesBeforeTestMethod(method);
    }
}
