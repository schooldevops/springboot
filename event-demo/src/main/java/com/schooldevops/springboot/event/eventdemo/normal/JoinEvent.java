package com.schooldevops.springboot.event.eventdemo.normal;

import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JoinEvent extends ApplicationEvent {

    private JoinInfo joinInfo;

    public JoinEvent(Object source, JoinInfo joinInfo) {
        super(source);
        this.joinInfo = joinInfo;
    }
}
