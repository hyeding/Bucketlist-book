package TockTiMan.dto.user;

import TockTiMan.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String username; // 로그인 아이디
    private String name; // 유저 실명
    private String nickname; // 유저 닉네임
    private String email; //유저 이메일

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getName(), user.getNickname());
    }

}