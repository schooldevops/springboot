package com.schooldevops.springboot.event.eventdemo.aware;

import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JoinEventForAware extends ApplicationEvent {

    private final JoinInfo joinInfo;

    public JoinEventForAware(Object source, JoinInfo joinInfo) {
        super(source);
        this.joinInfo = joinInfo;
    }
}
