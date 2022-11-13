package TockTiMan.service;

import TockTiMan.dto.comment.CommentCreateRequest;
import TockTiMan.dto.comment.CommentDto;
import TockTiMan.dto.comment.CommentReadCondition;
import TockTiMan.entity.board.Board;
import TockTiMan.entity.comment.Comment;
import TockTiMan.entity.user.User;
import TockTiMan.repository.board.BoardRepository;
import TockTiMan.repository.comment.CommentRepository;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.comment.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static TockTiMan.factory.BoardFactory.createBoard;
import static TockTiMan.factory.CommentFactory.createComment;
import static TockTiMan.factory.UserFactory.createUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BoardRepository boardRepository;


    @Test
    @DisplayName("findAll 서비스 테스트")
    void findAllTest() {
        // given
        List<Comment> commentList = new ArrayList<>();
        commentList.add(createComment());
        CommentReadCondition commentReadCondition = new CommentReadCondition(anyInt());
        given(commentRepository.findByBoardId(commentReadCondition.getBoardId())).willReturn(commentList);

        // when
        List<CommentDto> result = commentService.findAll(commentReadCondition);

        // then
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("create 서비스 테스트")
    void createTest() {
        // given
        Board board = createBoard();
        CommentCreateRequest req = new CommentCreateRequest("content", board.getId());
        given(boardRepository.findById(anyInt())).willReturn(Optional.of(board));
        Comment comment = createComment();

        // when
        CommentDto result = commentService.create(req, createUser());

        // then
        verify(commentRepository).save(any());
    }

    @Test
    @DisplayName("delete 서비스 테스트")
    void deleteTest() {
        // given
        User user = createUser();
        given(commentRepository.findById(anyInt())).willReturn(Optional.of(createComment()));
        given(boardRepository.findById(anyInt())).willReturn(Optional.of(createBoard()));

        // when
        commentService.delete(anyInt(), user);

        // then
        verify(commentRepository).delete(any());
    }
}
