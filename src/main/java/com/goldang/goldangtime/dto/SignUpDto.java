package com.goldang.goldangtime.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SignUpDto {
    private String email;
    private String password;
    private String nickname;
}
