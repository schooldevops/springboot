package com.schooldevops.springboot.event.eventdemo.async;

import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import com.schooldevops.springboot.event.eventdemo.normal.JoinEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoinAsyncEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public JoinAsyncEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void fireJoinEvent(JoinInfo info) {
        log.info("fireJoinEvent: " + info);
        JoinAsyncEvent joinEvent = new JoinAsyncEvent(this, info);
        applicationEventPublisher.publishEvent(joinEvent);
    }
}
