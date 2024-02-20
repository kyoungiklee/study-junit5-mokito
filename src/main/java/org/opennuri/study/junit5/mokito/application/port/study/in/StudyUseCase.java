package org.opennuri.study.junit5.mokito.application.port.study.in;

import org.opennuri.study.junit5.mokito.domain.Study;

import java.util.Optional;

public interface StudyUseCase {

    Study createNewStudy(Long ownerId, RegisterStudyCommand command);

    Study openStudy(Study study);

    Optional<Study> findStudyById(Long id);
}
