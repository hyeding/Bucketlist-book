package TockTiMan.repository.board;

import TockTiMan.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    Page<Board> findByTitleContaining(String keyword, Pageable pageable);
    Page<Board> findAll(Pageable pageable);
    Page<Board> findAllByCategoryId(Pageable pageable, int id);
    Page<Board> findByLikedGreaterThanEqual(Pageable pageable, int number);
    List<Board> findByReportedIsTrue();
}
