package TockTiMan.repository.comment;

import TockTiMan.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByBoardId(int boardId);
}
