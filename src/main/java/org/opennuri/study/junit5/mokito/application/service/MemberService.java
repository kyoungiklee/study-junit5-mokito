package org.opennuri.study.junit5.mokito.application.service;

import org.opennuri.study.junit5.mokito.application.port.member.in.MemberUseCase;
import org.opennuri.study.junit5.mokito.common.annotation.UseCase;
import org.opennuri.study.junit5.mokito.domain.Member;
import org.opennuri.study.junit5.mokito.domain.Study;

import java.util.Optional;

@UseCase
public class MemberService implements MemberUseCase {
    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.empty();
    }

    @Override
    public boolean validate(Long memberId) {
        return false;
    }

    @Override
    public void notify(Member member) {

    }

    @Override
    public void notify(Study study) {

    }
}
