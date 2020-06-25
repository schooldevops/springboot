package com.schooldevops.springboot.event.eventdemo.normal;

import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoinEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public JoinEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void fireJoinEvent(JoinInfo info) {
        log.info("fireJoinEvent: " + info);
        JoinEvent joinEvent = new JoinEvent(this, info);
        applicationEventPublisher.publishEvent(joinEvent);
    }
}
