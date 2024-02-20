package org.opennuri.study.junit5.mokito.adapter.out.persistence.member;

import org.opennuri.study.junit5.mokito.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {
}
