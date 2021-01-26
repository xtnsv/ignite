package org.apache.ignite.jdbc.thin;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class JdbcThinCacheToJdbcDataTypesNoExpectedExceptionsExtension implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) {
        StringBuilder msg = new StringBuilder();
        context.getTestClass().map(Class::getSimpleName).ifPresent(className -> msg.append(className).append("."));
        appendToStringBuilder(msg, context.getDisplayName());
        appendToStringBuilder(msg, " has thrown an unexpected exception: ");
        throw new RuntimeException(msg.toString(), throwable);
    }

    private void appendToStringBuilder(StringBuilder stringBuilder, String stringToAppend) {
        stringBuilder.append(stringToAppend);
    }
}
