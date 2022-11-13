package TockTiMan.repository.user;

import TockTiMan.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String username, String password);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByUsername(String username);
    public boolean existsByUsername(String username);
    public boolean existsByNickname(String nickname);

    List<User> findByReportedIsTrue();
}
