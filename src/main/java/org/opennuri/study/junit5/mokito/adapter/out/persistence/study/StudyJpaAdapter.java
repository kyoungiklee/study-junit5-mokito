package org.opennuri.study.junit5.mokito.adapter.out.persistence.study;

import lombok.RequiredArgsConstructor;
import org.opennuri.study.junit5.mokito.application.port.study.out.StudyPersistencePort;
import org.opennuri.study.junit5.mokito.common.annotation.Adapter;
import org.opennuri.study.junit5.mokito.domain.Study;

import java.util.Optional;
import java.util.function.Supplier;

@Adapter
@RequiredArgsConstructor
public class StudyJpaAdapter implements StudyPersistencePort {
    private final StudyJpaRepository repository;
    @Override
    public Study save(Study study) {
        StudyEntity entity = repository.save(StudyEntity.builder()
                .name(study.getName())
                .openDateTime(study.getOpenDateTime())
                .ownerId(study.getOwnerId())
                .limitCount(study.getLimitCount())
                .status(study.getStatus())
                .build());
        return entity.toDomain();
    }

    @Override
    public Study update(Study study) {
        Optional<StudyEntity> studyEntity = repository.findById(study.getId());

        return studyEntity.map(entity -> {
            entity.setOpenDateTime(study.getOpenDateTime());
            entity.setName(study.getName());
            entity.setLimitCount(study.getLimitCount());
            entity.setStatus(study.getStatus());
            entity.setOwnerId(study.getOwnerId());
            StudyEntity saved = repository.save(entity);
            return saved.toDomain();
        }).orElseThrow(() -> new IllegalArgumentException(String.format("study doesn't exist for id: %s", study.getId())));
    }

    @Override
    public Optional<Study> findById(Long id) {
        Optional<StudyEntity> studyEntity = repository.findById(id);
        Study study = studyEntity.map(StudyEntity::toDomain).orElseGet(new Supplier<Study>() {
            @Override
            public Study get() {
                return null;
            }
        });
        return Optional.ofNullable(study);
    }
}
