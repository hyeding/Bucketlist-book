package TockTiMan.service.board;

import TockTiMan.dto.board.*;
import TockTiMan.entity.board.Board;
import TockTiMan.entity.board.Favorite;
import TockTiMan.entity.board.Image;
import TockTiMan.entity.board.LikeBoard;
import TockTiMan.entity.category.Category;
import TockTiMan.entity.user.User;
import TockTiMan.exception.BoardNotFoundException;
import TockTiMan.exception.CategoryNotFoundException;
import TockTiMan.exception.MemberNotEqualsException;
import TockTiMan.repository.board.BoardRepository;
import TockTiMan.repository.board.FavoriteRepository;
import TockTiMan.repository.board.LikeBoardRepository;
import TockTiMan.repository.category.CategoryRepository;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final LikeBoardRepository likeBoardRepository;
    private final FavoriteRepository favoriteRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BoardCreateResponse create(BoardCreateRequest req, int categoryId, User user) {
        List<Image> images = req.getImages().stream().map(i -> new Image(i.getOriginalFilename())).collect(toList());
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        Board board = boardRepository.save(new Board(req.getTitle(), req.getContent(), user, category, images));

        uploadImages(board.getImages(), req.getImages());
        return new BoardCreateResponse(board.getId(), board.getTitle(), board.getContent());
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findAllBoards(Pageable pageable, int categoryId) {
        Page<Board> boards = boardRepository.findAllByCategoryId(pageable, categoryId);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().forEach(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }

    @Transactional(readOnly = true)
    public BoardDto findBoard(int id) {
        return BoardDto.toDto(boardRepository.findById(id).orElseThrow(BoardNotFoundException::new));
    }

    @Transactional
    public String likeBoard(int id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        if (likeBoardRepository.findByBoardAndUser(board, user) == null) {
            // ???????????? ????????? ????????? LikeBoard ?????? ???, ????????? ??????
            board.setLiked(board.getLiked() + 1);
            LikeBoard likeBoard = new LikeBoard(board, user); // true ??????
            likeBoardRepository.save(likeBoard);
            return "????????? ?????? ??????";
        } else {
            // ???????????? ????????? ????????? ?????? ?????? ??? ????????? ??????
            LikeBoard likeBoard = likeBoardRepository.findByBoardAndUser(board, user);
            likeBoard.unLikeBoard(board);
            likeBoardRepository.delete(likeBoard);
            return "????????? ??????";
        }
    }


    @Transactional
    public String favoriteBoard(int id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        if (favoriteRepository.findByBoardAndUser(board, user) == null) {
            // ???????????? ????????? ????????? Favorite ?????? ???, ???????????? ??????
            board.setFavorited(board.getFavorited() + 1);
            Favorite favorite = new Favorite(board, user); // true ??????
            favoriteRepository.save(favorite);
            return "???????????? ?????? ??????";
        } else {
            // ???????????? ????????? ????????? ???????????? ?????? ??? ????????? ??????
            Favorite favorite = favoriteRepository.findFavoriteByBoard(board);
            favorite.unFavoriteBoard(board);
            favoriteRepository.delete(favorite);
            return "???????????? ??????";
        }
    }


    @Transactional(readOnly = true)
    public List<BoardSimpleDto> findBestBoards(Pageable pageable) {
        // 10 ????????? ?????????
        Page<Board> boards = boardRepository.findByLikedGreaterThanEqual(pageable, 10);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().forEach(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }

    @Transactional
    public BoardDto editBoard(int id, BoardUpdateRequest req, User user) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        if (!user.equals(board.getUser())) {
            throw new MemberNotEqualsException();
        }

        Board.ImageUpdatedResult result = board.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return BoardDto.toDto(board);
    }

    @Transactional
    public void deleteBoard(int id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);

        if (!user.equals(board.getUser())) {
            throw new MemberNotEqualsException();
        }

        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public List<BoardSimpleDto> search(String keyword, Pageable pageable) {
        Page<Board> boards = boardRepository.findByTitleContaining(keyword, pageable);
        List<BoardSimpleDto> boardSimpleDtoList = new ArrayList<>();
        boards.stream().forEach(i -> boardSimpleDtoList.add(new BoardSimpleDto().toDto(i)));
        return boardSimpleDtoList;
    }


    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }
}
