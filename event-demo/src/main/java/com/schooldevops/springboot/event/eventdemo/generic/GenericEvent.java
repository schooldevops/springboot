package com.schooldevops.springboot.event.eventdemo.generic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class GenericEvent<T> extends ApplicationEvent {

    private T event;
    protected boolean success;

    public GenericEvent(Object source, T event, boolean success) {
        super(source);
        this.event = event;
        this.success = success;
    }
}
