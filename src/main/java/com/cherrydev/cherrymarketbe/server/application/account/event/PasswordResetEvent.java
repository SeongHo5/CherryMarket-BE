package com.cherrydev.cherrymarketbe.server.application.account.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class PasswordResetEvent extends ApplicationEvent {

    private final String email;
    public PasswordResetEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
