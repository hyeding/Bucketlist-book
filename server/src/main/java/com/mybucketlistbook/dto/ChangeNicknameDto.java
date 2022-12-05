package com.mybucketlistbook.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ChangeNicknameDto")
public class ChangeNicknameDto {

    @ApiModelProperty(name = "변경할 유저 nickname", example = "홈동짱")
    @NotNull(message = "changePassword may not be empty")
    @Pattern(regexp = "^[0-9a-zA-Z가-힣]*$",
            message = "닉네임은 숫자, 영어, 한글만 가능합니다.")
    @Size(min = 1, max = 6)
    private String changeNickname;
}
