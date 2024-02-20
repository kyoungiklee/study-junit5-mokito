package org.opennuri.study.junit5.mokito.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opennuri.study.junit5.mokito.application.port.study.in.RegisterStudyCommand;
import org.opennuri.study.junit5.mokito.application.port.study.in.StudyUseCase;
import org.opennuri.study.junit5.mokito.domain.Study;
import org.opennuri.study.junit5.mokito.domain.StudyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Mockito를 사용하여 테스트를 진행 할 수 있도록
 * Mockito 모듈을 가져온다
 */
@ExtendWith(value = {
        MockitoExtension.class,
})

/*
 * slice test (web layer 테스트만 진행 할 수 있도록 한다.
 */
@WebMvcTest(controllers = StudyController.class)
@ActiveProfiles("test")
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class StudyControllerTest {

    private static Study study;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private StudyUseCase studyUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() {
        study = Study.from(
                new Study.Id(1L)
                , new Study.Status(StudyStatus.DRAFT)
                , new Study.LimitCount(100)
                , new Study.Name("test")
                , new Study.OpenDateTime(LocalDateTime.now())
                , new Study.OwnerId(1L)
        );
    }

    @Test
    @Order(1)
    @DisplayName("아디디로 스터디 조회 성공 케이스")
    void getStudy_find_a_study() throws Exception {
        given(studyUseCase.findStudyById(1L)).willReturn(Optional.ofNullable(study));

        mockMvc.perform(get("/study/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value(StudyStatus.DRAFT.name()))
                .andExpect(jsonPath("$.limitCount").value(100))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.openDateTime").exists())
        ;
    }

    @Test
    @Order(2)
    @DisplayName("아이디로 스터디 조회 실패 케이스")
    void getStudy_not_find_a_study() throws Exception {
        given(studyUseCase.findStudyById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/study/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isEmpty())
        ;
    }

    @Test
    @Order(3)
    @DisplayName("아이디로 스터디 조회 오류 케이스")
    void getStudy_exist_exception() throws Exception {
        given(studyUseCase.findStudyById(1L)).willThrow(RuntimeException.class);

        mockMvc.perform(get("/study/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isInternalServerError())
        ;

    }

    @Test
    void createStudy() throws Exception {
        StudyRequest studyRequest = StudyRequest.builder()
                .name("test")
                .ownerId(1L)
                .openDateTime("2024-02-20 10:49:24")
                .limitCount(100)
                .build();

        RegisterStudyCommand command = RegisterStudyCommand.builder()
                .name(studyRequest.getName())
                .openDateTime(LocalDateTime.parse(studyRequest.getOpenDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .ownerId(studyRequest.getOwnerId())
                .limitCount(studyRequest.getLimitCount())
                .build();

        // study가 정상적으로 등록된 상태 테스트
        given(studyUseCase.createNewStudy(studyRequest.getOwnerId(), command)).willReturn(study);

        mockMvc.perform(post("/study")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(studyRequest))
        ).andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("study 생성 중 오류 발생 케이스")
    void createStudy_occurred_exception() throws Exception {
        StudyRequest studyRequest = StudyRequest.builder()
                .name("test")
                .ownerId(1L)
                .openDateTime("2024-02-20 10:49:24")
                .limitCount(100)
                .build();

        RegisterStudyCommand command = RegisterStudyCommand.builder()
                .name(studyRequest.getName())
                .openDateTime(LocalDateTime.parse(studyRequest.getOpenDateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .ownerId(studyRequest.getOwnerId())
                .limitCount(studyRequest.getLimitCount())
                .build();

        // sutdy 등록 중 RuntimeException이 발생한 상태 테스트
        given(studyUseCase.createNewStudy(studyRequest.getOwnerId(), command)).willThrow(RuntimeException.class);

        mockMvc.perform(post("/study")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(studyRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").isEmpty())
                ;
    }

    @Test
    void local_datetime_parser_test() {
        LocalDateTime parse = LocalDateTime.parse("2024-02-20 10:49:24", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(parse.getYear());
    }
}