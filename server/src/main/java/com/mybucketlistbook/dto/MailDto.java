package com.mybucketlistbook.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("MailRequest")
public class MailDto {

    private String email;

    private String nickname;

}
