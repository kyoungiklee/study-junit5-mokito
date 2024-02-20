package org.opennuri.study.junit5.mokito.adapter.out.persistence.member;

import lombok.RequiredArgsConstructor;
import org.opennuri.study.junit5.mokito.common.annotation.Adapter;
import org.opennuri.study.junit5.mokito.domain.Member;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class MemberJpaAdapter {
    public Optional<Member> findById(Long memberId) {
        return Optional.empty();
    }
}
