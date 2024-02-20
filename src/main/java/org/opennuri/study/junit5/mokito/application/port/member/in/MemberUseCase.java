package org.opennuri.study.junit5.mokito.application.port.member.in;

import org.opennuri.study.junit5.mokito.domain.Member;
import org.opennuri.study.junit5.mokito.domain.Study;

import java.util.Optional;

public interface MemberUseCase {
    Optional<Member> findById(Long memberId);

    boolean validate(Long memberId);

    void notify(Member member);

    void notify(Study study);
}
