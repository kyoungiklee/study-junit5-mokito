package org.opennuri.study.junit5.mokito.adapter.in.web;

import lombok.*;
import org.opennuri.study.junit5.mokito.domain.StudyStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StudyResponse {

    private Long id;
    private String status;
    private int limitCount;
    private String name;
    private String openDateTime;
    private Long ownerId;

    @Override
    public String toString() {
        return "StudyResponse{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", limitCount=" + limitCount +
                ", name='" + name + '\'' +
                ", openDateTime='" + openDateTime + '\'' +
                ", ownerId=" + ownerId +
                '}';
    }
}
