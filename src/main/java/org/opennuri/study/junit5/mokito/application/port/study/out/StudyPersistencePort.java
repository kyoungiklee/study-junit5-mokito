package org.opennuri.study.junit5.mokito.application.port.study.out;

import org.opennuri.study.junit5.mokito.domain.Study;

import java.util.Optional;

public interface StudyPersistencePort {
    Study save(Study study);

    Study update(Study study);

    Optional<Study> findById(Long id);
}
