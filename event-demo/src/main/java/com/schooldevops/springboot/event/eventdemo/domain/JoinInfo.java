package com.schooldevops.springboot.event.eventdemo.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JoinInfo {

    private String name;
    private String description;
    private LocalDateTime joinedAt;
}
