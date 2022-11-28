package TockTiMan.repository.report;

import TockTiMan.entity.report.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardReportRepository extends JpaRepository<BoardReport, Integer> {
    BoardReport findByReporterIdAndReportedBoardId(int reporterId, int reportedBoardId);
    List<BoardReport> findByReportedBoardId(int reportedBoardId);

    void deleteAllByReportedBoardId(int id);
}
