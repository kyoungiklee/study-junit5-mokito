package org.opennuri.study.junit5.mokito.adapter.in.web;

import lombok.*;
import org.opennuri.study.junit5.mokito.common.annotation.DateTimeFormat;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StudyRequest {
    private int limitCount;
    private String name;
    @DateTimeFormat
    private String openDateTime;
    private Long ownerId;

    @Override
    public String toString() {
        return "StudyRequest{" +
                ", limitCount=" + limitCount +
                ", name='" + name + '\'' +
                ", openDateTime='" + openDateTime + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}
