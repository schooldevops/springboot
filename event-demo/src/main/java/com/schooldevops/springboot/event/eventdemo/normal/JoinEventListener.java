package com.schooldevops.springboot.event.eventdemo.normal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoinEventListener implements ApplicationListener<JoinEvent> {
    @Override
    public void onApplicationEvent(JoinEvent joinEvent) {
        log.info("Receive joinEvent " + joinEvent);
    }
}
