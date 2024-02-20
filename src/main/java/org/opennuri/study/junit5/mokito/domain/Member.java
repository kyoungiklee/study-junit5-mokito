package org.opennuri.study.junit5.mokito.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor(access= AccessLevel.PRIVATE)
public class Member {
    private final Long id;
    private final String name;
    private final String email;
    private final String password;

    public static Member from(Id id, Name name, Email email, Password password){
        return new Member(id.value(), name.value(), email.value(), password.value());
    }
    public record Id(Long value){}
    public record Name(String value){}
    public record Email(String value){}
    public record Password(String value){}

}
