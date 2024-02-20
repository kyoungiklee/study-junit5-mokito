package org.opennuri.study.junit5.mokito.adapter.out.persistence.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.opennuri.study.junit5.mokito.adapter.out.persistence.audit.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class MemberEntity extends BaseEntity {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;    
}
