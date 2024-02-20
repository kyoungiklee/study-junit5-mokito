package org.opennuri.study.junit5.mokito.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.opennuri.study.junit5.mokito.adapter.in.web.StudyResponse;
import org.opennuri.study.junit5.mokito.common.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Study {
    private final Long id;
    private final StudyStatus status;
    private final int limitCount;
    private final String name;
    private final LocalDateTime openDateTime;
    private final Long ownerId;

    public static Study from(Id id, Status status, LimitCount limitCount, Name name, OpenDateTime openDateTime, OwnerId ownerId) {
        return new Study(id.value, status.value, limitCount.value, name.value, openDateTime.value, ownerId.value);
    }

    public StudyResponse toResponse() {
        return StudyResponse.builder()
                .id(this.id)
                .status(this.status.name())
                .limitCount(this.limitCount)
                .name(this.name)
                .openDateTime(this.openDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd")))
                .ownerId(this.getOwnerId())
                .build();
    }

    /**
     * Study 식별자
     * @param value
     */
    public record Id(Long value) {}

    /**
     * Study 상태 필드(enum) - DRAFT, OPENED, STARTED, ENDED
     * Study 등록 시 초기값은 DRAFT 이다
     * @param value
     */
    public record Status(StudyStatus value){}

    /**
     * Study 최대 인원 값을 나타내는 필드
     * @param value
     */
    public record LimitCount(int value){}

    /**
     * Study 명 100자 제한
     * @param value
     */
    public record Name(String value){}

    /**
     * Study 시작일
     * @param value
     */
    public record OpenDateTime(LocalDateTime value){}

    /**
     * Study 소유자 아이디(Long)
     * @param value
     */
    public record OwnerId(Long value){}
}
