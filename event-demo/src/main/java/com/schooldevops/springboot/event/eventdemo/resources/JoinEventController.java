package com.schooldevops.springboot.event.eventdemo.resources;

import com.schooldevops.springboot.event.eventdemo.async.JoinAsyncEventPublisher;
import com.schooldevops.springboot.event.eventdemo.generic.GenericEventPublisher;
import com.schooldevops.springboot.event.eventdemo.normal.JoinEventPublisher;
import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class JoinEventController {

    private final JoinEventPublisher joinEventPublisher;
    private final JoinAsyncEventPublisher joinAsyncEventPublisher;
    private final GenericEventPublisher genericEventPublisher;

    public JoinEventController(JoinEventPublisher joinEventPublisher, JoinAsyncEventPublisher joinAsyncEventPublisher, GenericEventPublisher genericEventPublisher) {
        this.joinEventPublisher = joinEventPublisher;
        this.joinAsyncEventPublisher = joinAsyncEventPublisher;
        this.genericEventPublisher = genericEventPublisher;
    }

    @PostMapping("/normal")
    public String normalEvent() {
        JoinInfo build = JoinInfo.builder()
                .name("Normal Join")
                .description("Normal Join Event")
                .joinedAt(LocalDateTime.now())
                .build();

        joinEventPublisher.fireJoinEvent(build);

        return "OK sync";
    }

    @PostMapping("/async")
    public String asyncEvent() {
        JoinInfo build = JoinInfo.builder()
                .name("Async Join")
                .description("Async Join Event")
                .joinedAt(LocalDateTime.now())
                .build();

        joinAsyncEventPublisher.fireJoinEvent(build);

        return "OK async";
    }


    @PostMapping("/generic/{success}")
    public String genericEvent(@PathVariable("success") Boolean success) {
        JoinInfo build = JoinInfo.builder()
                .name("Generic Event")
                .description("Generic Event: " + success)
                .joinedAt(LocalDateTime.now())
                .build();

        genericEventPublisher.fireJoinEvent(build, success);

        return "OK generic " + success;
    }

}
