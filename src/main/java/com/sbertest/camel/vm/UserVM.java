package com.sbertest.camel.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sbertest.camel.configuration.UserRole;
import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserVM {

    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("role")
    private UserRole role;

}
