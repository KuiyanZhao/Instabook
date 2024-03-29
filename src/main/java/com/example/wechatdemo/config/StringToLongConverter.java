package com.example.wechatdemo.config;

import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;

public class StringToLongConverter extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            if (StringUtils.isBlank(text)) {
                setValue(null);
            } else {
                setValue(Long.parseLong(text));
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Long format");
        }
    }
}
