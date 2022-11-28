package TockTiMan.repository.board;

import TockTiMan.entity.board.Board;
import TockTiMan.entity.board.Favorite;
import TockTiMan.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Favorite findFavoriteByBoard(Board board);
    Favorite findByBoardAndUser(Board board, User user);
    List<Favorite> findAllByUser(User user);
}
