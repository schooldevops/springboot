package com.schooldevops.springboot.event.eventdemo.generic;

import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GenericEventListener {

    @EventListener(condition = "#event.success")
    public void onApplicationEvent(GenericEvent<JoinInfo> event) {
        log.info("Generic Event Success: " + event.getEvent());
    }

    @EventListener(condition = "#event.success != true")
    public void onApplicationEventFail(GenericEvent<JoinInfo> event) {
        log.info("Generic Event Fail: " + event.getEvent());
    }
}
