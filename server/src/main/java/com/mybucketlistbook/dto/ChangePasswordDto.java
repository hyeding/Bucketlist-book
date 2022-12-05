package com.mybucketlistbook.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@ApiModel("ChangePasswordRequest")
public class ChangePasswordDto {

    @ApiModelProperty(name = "변경할 유저 password", example = "yesyes")
    @NotNull(message = "changePassword may not be empty")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$",
            message = "비밀번호는 영문, 숫자, 특수문자가 적어도 1개 이상씩 포함된 8자 ~ 16자의 비밀번호여야 합니다.")
    private String changePassword;

}
