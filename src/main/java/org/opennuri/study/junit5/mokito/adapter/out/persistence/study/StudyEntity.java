package org.opennuri.study.junit5.mokito.adapter.out.persistence.study;

import jakarta.persistence.*;
import lombok.*;
import org.opennuri.study.junit5.mokito.adapter.out.persistence.audit.BaseEntity;
import org.opennuri.study.junit5.mokito.domain.Study;
import org.opennuri.study.junit5.mokito.domain.StudyStatus;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, of="id")
@AllArgsConstructor
public class StudyEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private StudyStatus status;
    private int limitCount;
    @Column(length = 100)
    private String name;
    private LocalDateTime openDateTime;
    private Long ownerId;

    public Study toDomain() {
        return Study.from(
                new Study.Id(this.id)
                , new Study.Status(this.status)
                , new Study.LimitCount(this.limitCount)
                , new Study.Name(this.name)
                , new Study.OpenDateTime(this.openDateTime)
                , new Study.OwnerId(this.ownerId)
        );
    }
}
