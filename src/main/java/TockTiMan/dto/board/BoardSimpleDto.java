package TockTiMan.dto.board;

import TockTiMan.entity.board.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSimpleDto {
    // 메인 페이지에서 보여지는 게시글 정보
    private int id;
    private String title;
    private String nickname;
    private int liked;
    private int favorited;

    public BoardSimpleDto toDto(Board board) {
        return new BoardSimpleDto(board.getId() ,board.getTitle(), board.getUser().getNickname(), board.getLiked(), board.getFavorited());
    }

}