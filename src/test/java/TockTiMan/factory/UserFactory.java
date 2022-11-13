package TockTiMan.factory;

import TockTiMan.entity.user.Authority;
import TockTiMan.entity.user.User;

public class UserFactory {

    public static User createUserWithAdminRole() {
        User user = new User("username", "password", "name","nickname",Authority.ROLE_ADMIN);
        user.setName("hyeding");
        user.setNickname("hyeding");
        return user;
    }

    public static User createUser() {
        User user = new User("username", "password", "name","nickname",Authority.ROLE_ADMIN);
        user.setName("hyeding");
        user.setNickname("hyeding");
        return user;
    }

    public static User createUser2() {
        User user = new User("username2", "password2","name2","nickname2", Authority.ROLE_ADMIN);
        user.setName("hyeding");
        user.setNickname("hyeding");
        return user;
    }
}
