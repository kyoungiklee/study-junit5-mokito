package org.opennuri.study.junit5.mokito.application.port.study.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.opennuri.study.junit5.mokito.common.SelfValidation;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@Builder
public class RegisterStudyCommand extends SelfValidation<RegisterStudyCommand> {
    @NotNull
    private int limitCount;
    @NotBlank
    private String name;
    private LocalDateTime openDateTime;
    private Long ownerId;

    public RegisterStudyCommand(int limitCount, String name, LocalDateTime openDateTime, Long ownerId) {
        this.limitCount = limitCount;
        this.name = name;
        this.openDateTime = openDateTime;
        this.ownerId = ownerId;
        this.validateSelf();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterStudyCommand command = (RegisterStudyCommand) o;
        return limitCount == command.limitCount && Objects.equals(name, command.name) && Objects.equals(openDateTime, command.openDateTime) && Objects.equals(ownerId, command.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limitCount, name, openDateTime, ownerId);
    }
}
