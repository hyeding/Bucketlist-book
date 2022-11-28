package TockTiMan.dto.report;

import TockTiMan.entity.report.BoardReport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardReportResponse {
    private int id;
    private int reportedBoardId;
    private String content;
    private LocalDate createdAt;

    public BoardReportResponse toDto(BoardReport boardReport) {
        return new BoardReportResponse(
                boardReport.getId(),
                boardReport.getReportedBoardId(),
                boardReport.getContent(),
                boardReport.getCreateDate()
        );
    }
}
