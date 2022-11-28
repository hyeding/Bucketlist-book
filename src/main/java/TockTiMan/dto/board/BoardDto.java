package TockTiMan.dto.board;

import TockTiMan.dto.user.UserDto;
import TockTiMan.entity.board.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {
    private int id;
    private String title;
    private String content;
    private UserDto userDto;
    private int liked;
    private int favorited;
    private List<ImageDto> images;
    private LocalDate createdAt;

    public static BoardDto toDto(Board board) {
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                UserDto.toDto(board.getUser()),
                board.getLiked(),
                board.getFavorited(),
                board.getImages().stream().map(i -> ImageDto.toDto(i)).collect(toList()),
                board.getCreateDate()
        );
    }
}
