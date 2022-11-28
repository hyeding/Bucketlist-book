package TockTiMan.dto.comment;

import TockTiMan.dto.user.UserDto;
import TockTiMan.entity.comment.Comment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private int id;
    private String content;
    private UserDto userDto;
    private LocalDate createdAt;

    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                UserDto.toDto(comment.getUser()),
                comment.getCreateDate()
        );
    }
}
