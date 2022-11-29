package TockTiMan.service.report;

import TockTiMan.dto.report.BoardReportRequest;
import TockTiMan.dto.report.BoardReportResponse;
import TockTiMan.dto.report.UserReportRequest;
import TockTiMan.dto.report.UserReportResponse;
import TockTiMan.dto.user.UserDto;
import TockTiMan.entity.board.Board;
import TockTiMan.entity.report.BoardReport;
import TockTiMan.entity.report.UserReport;
import TockTiMan.entity.user.User;
import TockTiMan.exception.AlreadyReportException;
import TockTiMan.exception.BoardNotFoundException;
import TockTiMan.exception.MemberNotFoundException;
import TockTiMan.exception.type.NotSelfReportException;
import TockTiMan.repository.board.BoardRepository;
import TockTiMan.repository.report.BoardReportRepository;
import TockTiMan.repository.report.UserReportRepository;
import TockTiMan.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReportService {

    public final BoardReportRepository boardReportRepository;
    public final UserReportRepository userReportRepository;
    public final UserRepository userRepository;
    public final BoardRepository boardRepository;

    @Transactional
    public UserReportResponse reportUser(User reporter, UserReportRequest req) {
        User reportedUser = userRepository.findById(req.getReportedUserId()).orElseThrow(MemberNotFoundException::new);

        if (reporter.getId() == req.getReportedUserId()) {
            // 자기 자신을 신고한 경우
            throw new NotSelfReportException();
        }

        if (userReportRepository.findByReporterIdAndReportedUserId(reporter.getId(), req.getReportedUserId()) == null) {
            // 신고 한 적이 없다면, 테이블 생성 후 신고 처리 (ReportedUser의 User테이블 boolean 값 true 변경 ==> 신고처리)
            UserReport userReport = new UserReport(reporter.getId(), reportedUser.getId(), req.getContent());
            userReportRepository.save(userReport);

            if (userReportRepository.findByReportedUserId(req.getReportedUserId()).size() >= 10) {
                // 신고 수 10 이상일 시 true 설정
                reportedUser.setReported(true);
            }

            UserReportResponse res = new UserReportResponse(userReport.getId(), UserDto.toDto(reportedUser), req.getContent(), userReport.getCreateDate());
            return res;
        } else {
            // 이미 신고를 했다면 리턴
            throw new AlreadyReportException();
        }
    }

    @Transactional
    public BoardReportResponse reportBoard(User reporter, BoardReportRequest req) {
        Board reportedBoard = boardRepository.findById(req.getReportedBoardId()).orElseThrow(BoardNotFoundException::new);

        if (reporter.getId() == reportedBoard.getUser().getId()) {
            throw new NotSelfReportException();
        }

        if (boardReportRepository.findByReporterIdAndReportedBoardId(reporter.getId(), req.getReportedBoardId()) == null) {
            // 신고 한 적이 없다면, 테이블 생성 후 신고 처리
            BoardReport boardReport = new BoardReport(reporter.getId(), reportedBoard.getId(), req.getContent());
            boardReportRepository.save(boardReport);

            if (boardReportRepository.findByReportedBoardId(req.getReportedBoardId()).size() >= 10) {
                // 신고 수 10 이상일 시 true 설정
                reportedBoard.setReported(true);
            }

            BoardReportResponse res = new BoardReportResponse(boardReport.getId(), req.getReportedBoardId(), req.getContent(), boardReport.getCreateDate());
            return res;
        } else {
            throw new AlreadyReportException();
        }
    }
}
