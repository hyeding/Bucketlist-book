package TockTiMan.dto.report;

import TockTiMan.dto.user.UserDto;
import TockTiMan.entity.report.UserReport;
import TockTiMan.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {
    private int id;
    private UserDto reportedUser;
    private String content;
    private LocalDate createdAt;


    public UserReportResponse toDto(UserReport userReport, User reportedUser) {
        return new UserReportResponse(
                userReport.getId(),
                UserDto.toDto(reportedUser),
                userReport.getContent(),
                userReport.getCreateDate()
        );
    }
}
