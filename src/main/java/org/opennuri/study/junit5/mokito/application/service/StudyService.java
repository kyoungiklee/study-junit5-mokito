package org.opennuri.study.junit5.mokito.application.service;

import org.opennuri.study.junit5.mokito.application.port.member.in.MemberUseCase;
import org.opennuri.study.junit5.mokito.application.port.study.in.RegisterStudyCommand;
import org.opennuri.study.junit5.mokito.application.port.study.in.StudyUseCase;
import org.opennuri.study.junit5.mokito.application.port.study.out.StudyPersistencePort;
import org.opennuri.study.junit5.mokito.common.annotation.UseCase;
import org.opennuri.study.junit5.mokito.domain.Member;
import org.opennuri.study.junit5.mokito.domain.Study;
import org.opennuri.study.junit5.mokito.domain.StudyStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@UseCase
public class StudyService implements StudyUseCase {
    private final MemberUseCase memberUseCase;
    private final StudyPersistencePort studyPersistencePort;
    public StudyService(MemberUseCase memberUseCase, StudyPersistencePort studyPersistencePort) {
        assert memberUseCase != null;
        assert studyPersistencePort != null;
        this.memberUseCase = memberUseCase;
        this.studyPersistencePort = studyPersistencePort;
    }

    @Override
    public Study createNewStudy(Long ownerId, RegisterStudyCommand command) {
        Optional<Member> member = memberUseCase.findById(ownerId);
        Study study;
        if (member.isPresent()) {
            study = Study.from(
                    new Study.Id(null)
                    , new Study.Status(StudyStatus.DRAFT)
                    , new Study.LimitCount(command.getLimitCount())
                    , new Study.Name(command.getName())
                    , new Study.OpenDateTime(command.getOpenDateTime())
                    , new Study.OwnerId(member.get().getId())
            );
        } else {
            throw new IllegalArgumentException(String.format("member doesn't exist for id: %s", ownerId));
        }

        Study savedStudy = studyPersistencePort.save(study);
        memberUseCase.notify(savedStudy);
        memberUseCase.notify(member.orElseThrow());

        return savedStudy;

    }

    @Override
    public Study openStudy(Study study) {
        Study updateStudy = Study.from(
                new Study.Id(study.getId())
                , new Study.Status(StudyStatus.OPENED)
                , new Study.LimitCount(study.getLimitCount())
                , new Study.Name(study.getName())
                , new Study.OpenDateTime(LocalDateTime.now())
                , new Study.OwnerId(study.getOwnerId())
        );
        Study updated = studyPersistencePort.update(updateStudy);
        memberUseCase.notify(updated);
        return updated;
    }

    @Override
    public Optional<Study> findStudyById(Long id) {
        return studyPersistencePort.findById(id);
    }
}
