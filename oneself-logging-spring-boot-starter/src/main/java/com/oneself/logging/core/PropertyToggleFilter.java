package com.oneself.logging.core;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class PropertyToggleFilter extends Filter<ILoggingEvent> {

    private String propertyName;
    private String expectedValue = "true";

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (propertyName == null || propertyName.isBlank()) {
            return FilterReply.DENY;
        }
        String value = getContext() == null ? null : getContext().getProperty(propertyName);
        if (value == null) {
            return FilterReply.DENY;
        }
        return value.equalsIgnoreCase(expectedValue) ? FilterReply.NEUTRAL : FilterReply.DENY;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }
}
