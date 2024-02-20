package org.opennuri.study.junit5.mokito.adapter.in.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opennuri.study.junit5.mokito.application.port.study.in.RegisterStudyCommand;
import org.opennuri.study.junit5.mokito.application.port.study.in.StudyUseCase;
import org.opennuri.study.junit5.mokito.domain.Study;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StudyController {

    private final StudyUseCase studyUseCase;
    @GetMapping("/study/{id}")
    public ResponseEntity<StudyResponse> getStudy(@PathVariable Long id) {
        Optional<Study> optStudy;

        try {
            optStudy = studyUseCase.findStudyById(id);
        } catch (Exception e) {
            log.error("An error occurred on the {}. Error message : {}", this.getClass().getName(), e.getMessage());
            return ResponseEntity.internalServerError().body(new StudyResponse());
        }

        if (optStudy.isPresent()) {
            Study study = optStudy.get();
            StudyResponse response = StudyResponse.builder()
                    .id(study.getId())
                    .name(study.getName())
                    .status(study.getStatus().name())
                    .openDateTime(study.getOpenDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .ownerId(study.getOwnerId())
                    .limitCount(study.getLimitCount())
                    .build();
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(new StudyResponse());
        }
    }

    @PostMapping("/study")
    public ResponseEntity<StudyResponse> createStudy(@RequestBody StudyRequest request) {
        log.info("createStudy request : {}", request);
        try {
            Study study = studyUseCase.createNewStudy(request.getOwnerId(), RegisterStudyCommand.builder()
                    .name(request.getName())
                    .openDateTime(LocalDateTime.parse(request.getOpenDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .ownerId(request.getOwnerId())
                    .limitCount(request.getLimitCount())
                    .build());
            return ResponseEntity.ok().body(study.toResponse());
        } catch (Exception e) {
            log.error("Error creating study : {}", e.getMessage());
            return ResponseEntity.badRequest().body(new StudyResponse());
        }
    }
}
