package com.schooldevops.springboot.event.eventdemo.async;

import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JoinAsyncEvent extends ApplicationEvent {

    private JoinInfo joinInfo;

    public JoinAsyncEvent(Object source, JoinInfo joinInfo) {
        super(source);

        this.joinInfo = joinInfo;
    }
}
