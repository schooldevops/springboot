package com.schooldevops.springboot.event.eventdemo.generic;

import com.schooldevops.springboot.event.eventdemo.domain.JoinInfo;
import lombok.Getter;

@Getter
public class GenericJoinEvent extends GenericEvent<JoinInfo> {

    public GenericJoinEvent(Object source, JoinInfo event, boolean success) {
        super(source, event, success);
    }
}
