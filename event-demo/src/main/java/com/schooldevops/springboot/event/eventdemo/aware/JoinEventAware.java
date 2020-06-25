package com.schooldevops.springboot.event.eventdemo.aware;

import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoinEventAware implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void sendMessage(JoinInfo joinInfo) {
        JoinEventForAware event = new JoinEventForAware(this, joinInfo);
        log.info("Send JoinEventForAware: " + event);
        applicationEventPublisher.publishEvent(event);
    }


}
