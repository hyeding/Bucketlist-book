package TockTiMan.repository.board;

import TockTiMan.entity.board.Board;
import TockTiMan.entity.board.LikeBoard;
import TockTiMan.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Integer> {

    LikeBoard findByBoardAndUser(Board board, User user);
}
