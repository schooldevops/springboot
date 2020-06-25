package com.schooldevops.springboot.event.eventdemo.generic;

import com.schooldevops.springboot.event.eventdemo.async.JoinAsyncEvent;
import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenericEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public GenericEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void fireJoinEvent(JoinInfo info, boolean success) {
        log.info("fire Generic Event: " + info + " : " + success);
        GenericJoinEvent genericJoinEvent = new GenericJoinEvent(this, info, success);
        applicationEventPublisher.publishEvent(genericJoinEvent);
    }

}
