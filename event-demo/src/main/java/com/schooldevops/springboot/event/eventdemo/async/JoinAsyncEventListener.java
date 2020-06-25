package com.schooldevops.springboot.event.eventdemo.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JoinAsyncEventListener {

    @Async
    @EventListener
    public void onApplicationEvent(JoinAsyncEvent joinEvent) {
        log.info("Receive Async joinEvent " + joinEvent);
    }
}
