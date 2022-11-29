package TockTiMan.factory;

import TockTiMan.entity.comment.Comment;

import static TockTiMan.factory.BoardFactory.createBoard;
import static TockTiMan.factory.UserFactory.createUser;

public class CommentFactory {

    public static Comment createComment() {
        return new Comment("content", createUser(), createBoard());
    }
}
