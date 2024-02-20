package org.opennuri.study.junit5.mokito.adapter.out.persistence.study;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyJpaRepository extends JpaRepository<StudyEntity, Long> {
}
