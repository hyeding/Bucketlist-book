package TockTiMan.dto.sign;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "로그아웃 요청")
public class LogoutRequestDto {
    @NotEmpty(message = "잘못된 요청입니다.")
    private String accessToken;

    @NotBlank(message = "잘못된 요청입니다.")
    private String refreshToken;
}
