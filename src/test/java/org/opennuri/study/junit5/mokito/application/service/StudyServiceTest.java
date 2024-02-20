package org.opennuri.study.junit5.mokito.application.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opennuri.study.junit5.mokito.application.port.member.in.MemberUseCase;
import org.opennuri.study.junit5.mokito.application.port.study.in.RegisterStudyCommand;
import org.opennuri.study.junit5.mokito.application.port.study.in.StudyUseCase;
import org.opennuri.study.junit5.mokito.application.port.study.out.StudyPersistencePort;
import org.opennuri.study.junit5.mokito.domain.Member;
import org.opennuri.study.junit5.mokito.domain.Study;
import org.opennuri.study.junit5.mokito.domain.StudyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ConditionalOnBean(value = StudyPersistencePort.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@Transactional(readOnly = true)
class StudyServiceTest {

    private Member member;
    private MemberUseCase memberUseCase;
    private RegisterStudyCommand registerStudyCommand;
    private StudyUseCase studyUseCase;

    @Autowired
    StudyPersistencePort studyPersistencePort;

    @BeforeEach
    void setUp() {
        member = Member.from(
                new Member.Id(1L)
                , new Member.Name("test")
                , new Member.Email("mail@gmail.com")
                , new Member.Password("test"));

        registerStudyCommand = RegisterStudyCommand.builder()
                .name("test")
                .limitCount(100)
                .ownerId(1L)
                .openDateTime(LocalDateTime.now())
                .build();

        memberUseCase = Mockito.mock(MemberUseCase.class);
        studyUseCase = new StudyService(memberUseCase, studyPersistencePort);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void crate_study_service() {
        StudyService service = new StudyService(memberUseCase, studyPersistencePort);
        assertThat(service).isNotNull();
    }

    @Test
    @Order(2)
    void create_study_mock_parameter(@Mock MemberUseCase memberUseCase, @Mock StudyPersistencePort studyPersistencePort) {
        StudyService service = new StudyService(memberUseCase, studyPersistencePort);
        assertThat(service).isNotNull();
    }

    @Test
    @Order(5)
    void create_member_stubbing() {
        Member member = Member.from(
                new Member.Id(2L)
                , new Member.Name("test")
                , new Member.Email("mail@gmail.com")
                , new Member.Password("test"));

        MemberUseCase memberUseCase1 = Mockito.mock(MemberUseCase.class);
        when(memberUseCase1.findById(2L)).thenReturn(Optional.of(member));
        assertThat(member.getEmail()).isEqualTo(memberUseCase1.findById(2L).map(Member::getEmail).orElse(""));

    }

    @Test
    @Order(6)
    void create_member_stubbing_then_return() {

        when(memberUseCase.findById(1L)).thenReturn(Optional.of(member));

        assertThat(member.getEmail()).isEqualTo(memberUseCase.findById(1L).map(Member::getEmail).orElse(""));
    }

    @Test
    @Order(7)
    void throws_error_test_member_when_then_throw() {
        when(memberUseCase.findById(1L)).thenThrow(new IllegalAccessError());

        assertThatThrownBy(() -> memberUseCase.findById(1L)).isExactlyInstanceOf(IllegalAccessError.class);
    }

    @Test
    @Order(8)
    @DisplayName("member service의 validate 메서드에 1L을 입력하면 예외발생")
    void throws_error_test_when_call_member_validate() {
        doThrow(new IllegalArgumentException()).when(memberUseCase).validate(1L);
        assertThatThrownBy(() -> memberUseCase.validate(1L)).isInstanceOf(IllegalArgumentException.class);
        // 2L을 입력하면 예외가 발생하지 않음
        assertThatCode(()-> memberUseCase.validate(2L)).doesNotThrowAnyException();
    }

    @Test
    @Order(9)
    @DisplayName("any() 메서드를 이용하면, 모든 매개변수에 대해 같은 행동을 하는 Mock 객체 생성")
    void create_member_test_using_any() {
        when(memberUseCase.findById(any())).thenReturn(Optional.ofNullable(member));

        assertThat(member.getEmail()).isEqualTo(memberUseCase.findById(1L).map(Member::getEmail).orElse(""));
        assertThat(member.getEmail()).isEqualTo(memberUseCase.findById(999L).map(Member::getEmail).orElse(""));
    }

    @Test
    @Order(10)
    @DisplayName("when()에서 메서드 체이닝을 통해 메서드가 동일한 매개 변수로 여러번 호출될 때 각각 다른 행동 구현")
    void create_member_test_method_channing() {
        when(memberUseCase.findById(1L)).thenReturn(Optional.of(member))
                .thenThrow(new IllegalArgumentException())
                .thenReturn(Optional.empty());
        Optional<Member> optionalMember = memberUseCase.findById(1L);

        assertThat(optionalMember.map(Member::getEmail).orElse("")).isEqualTo(member.getEmail());
        assertThatThrownBy(() -> memberUseCase.findById(1L)).isInstanceOf(IllegalArgumentException.class);
        assertThat(memberUseCase.findById(1L)).isEqualTo(Optional.empty());
    }

    @Test
    @Order(11)
    @DisplayName("Mock 객체가 어떻게 사용되었는 지 확인")
    void verify_mock_object_method_called() {
        when(memberUseCase.findById(1L)).thenReturn(Optional.ofNullable(member));

        Study createdStudy = studyUseCase.createNewStudy(member.getId(), registerStudyCommand);

        verify(memberUseCase, times(1)).notify(createdStudy);
    }

    @Test
    @Order(12)
    @DisplayName("Mock 객체에서 특정 메소드가 호출되지 않았는지 확인")
    void verify_mock_object_method_not_called() {

        when(memberUseCase.findById(1L)).thenReturn(Optional.of(member));

        studyUseCase.createNewStudy(member.getId(), registerStudyCommand);

        verify(memberUseCase, never()).validate(any());

    }

    @Test
    @Order(13)
    @DisplayName("Mock 객체에서 메소드가 순차적으로 호출되었는지 확인")
    void verify_mock_object_method_sequential_called() {
        when(memberUseCase.findById(1L)).thenReturn(Optional.of(member));

        Study study = studyUseCase.createNewStudy(member.getId(), registerStudyCommand);
        InOrder inOrder = inOrder(memberUseCase);
        inOrder.verify(memberUseCase).notify(study);
        inOrder.verify(memberUseCase).notify(member);
    }

    @Test
    @Order(14)
    @DisplayName("BDD style mock test")
    void bdd_style_mock_test() {
        given(memberUseCase.findById(1L)).willReturn(Optional.of(member));

        Study createdNewStudy = studyUseCase.createNewStudy(member.getId(), registerStudyCommand);

        then(memberUseCase).should(times(1)).notify(createdNewStudy);

    }

    /**
     * member service는 interface만 확정된 경우 member service를 mock 객체 생성 후
     * 테스트를 진행한다.
     */
    @Test
    @Transactional
    void creteNewStudy() {
        BDDMockito.given(memberUseCase.findById(1L)).willReturn(Optional.of(member));

        Study study = studyUseCase.createNewStudy(member.getId(), registerStudyCommand);

        assertThat(study.getId()).isNotNull();
        assertThat(study).hasFieldOrPropertyWithValue("name", registerStudyCommand.getName());
        assertThat(study).hasFieldOrPropertyWithValue("limitCount", registerStudyCommand.getLimitCount());
        assertThat(study).hasFieldOrPropertyWithValue("status", StudyStatus.DRAFT);
        assertThat(study).hasFieldOrPropertyWithValue("ownerId", registerStudyCommand.getOwnerId());
        assertThat(study.getOpenDateTime()).isNotNull();

        BDDMockito.then(memberUseCase).should(times(1)).notify(study);

    }

    /**
     * member service가 개발 중에 있는 상태에서 study service를 개발하고 테스트할 수 있다
     * 상호 interface는 정의 되어야 한다.
     */
    @Test
    @Transactional
    void openStudy() {

        BDDMockito.given(memberUseCase.findById(1L)).willReturn(Optional.ofNullable(member));

        Study study = studyUseCase.createNewStudy(member.getId(), registerStudyCommand);

        Study openStudy = studyUseCase.openStudy(study);

        assertThat(openStudy.getStatus().name()).isEqualTo("OPENED");

        BDDMockito.then(memberUseCase).should(times(1)).notify(openStudy);

    }

    @Test
    void findStudyById() {
        given(memberUseCase.findById(1L)).willReturn(Optional.ofNullable(member));
        Study study = studyUseCase.createNewStudy(member.getId(), registerStudyCommand);

        Optional<Study> studyById = studyUseCase.findStudyById(study.getId());
        assertThat(studyById.orElse(null)).isNotNull();
    }
}