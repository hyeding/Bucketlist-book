package com.mybucketlistbook.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("PasswordRequest")
public class PasswordDto {

    @ApiModelProperty(name = "현재 유저 password", example = "yesyes")
    @NotEmpty(message = "password may not be empty")
    private String password;

}
