package org.opennuri.study.junit5.mokito.application.port.member.out;

import org.opennuri.study.junit5.mokito.domain.Member;

import java.util.Optional;

public interface MemberPersistencePort {
    Optional<Member> findById(Long memberId);

}
