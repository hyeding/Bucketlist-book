package com.mybucketlistbook.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ApiModel("SignUpRequest")
public class SignUpDto {

    @ApiModelProperty(name="유저 email")
    @NotNull(message = "email may not be empty")
    @Email(message = "이메일 형식이 아닙니다.")
    @Size(min = 3, max = 50)
    private String email;

    @ApiModelProperty(name="유저 password")
    @NotNull(message = "password may not be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",
            message = "비밀번호는 영문, 숫자, 특수문자가 적어도 1개 이상씩 포함된 8자 ~ 16자의 비밀번호여야 합니다.")
    private String password;

    @ApiModelProperty(name="유저 nickname")
    @NotNull(message = "nickname may not be empty")

    @Pattern(regexp = "^[0-9a-zA-Z가-힣]*$",
            message = "닉네임은 숫자, 영어, 한글만 가능합니다.")
    @Size(min = 1, max = 6)
    private String nickname;

}
