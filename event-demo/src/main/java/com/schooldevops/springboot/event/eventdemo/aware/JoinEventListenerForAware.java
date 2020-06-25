package com.schooldevops.springboot.event.eventdemo.aware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoinEventListenerForAware implements ApplicationListener<JoinEventForAware> {

    @Override
    public void onApplicationEvent(JoinEventForAware joinEventForAware) {
        log.info("JoinEvent Receive Aware: " + joinEventForAware.getJoinInfo());
    }
}
