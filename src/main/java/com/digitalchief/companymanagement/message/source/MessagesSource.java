package com.digitalchief.companymanagement.message.source;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessagesSource {
    private final MessageSource messageSource;

    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}
