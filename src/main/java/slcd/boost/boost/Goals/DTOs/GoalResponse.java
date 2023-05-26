package slcd.boost.boost.Goals.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Goals.Entities.GoalEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalResponse {
    private String uuid;
    private String name;
    private String certificationUuid;
    private String description;
    private String gradeThree;
    private String statusThree;
    private String gradeFour;
    private String statusFour;
    private String gradeFive;
    private String statusFive;
    private String result;
    private String status;
    private LocalDateTime created;
    private LocalDateTime updated;

    public static GoalResponse mapFromEntity(GoalEntity goalEntity){
        return new GoalResponse(
                goalEntity.getUuid().toString(),
                goalEntity.getName(),
                goalEntity.getCertification().getUuid().toString(),
                goalEntity.getDescription(),
                goalEntity.getGradeThree(),
                goalEntity.getStatusThree().toString(),
                goalEntity.getGradeFour(),
                goalEntity.getStatusFour().toString(),
                goalEntity.getGradeFive(),
                goalEntity.getStatusFive().toString(),
                goalEntity.getResult(),
                goalEntity.getStatus().toString(),
                goalEntity.getCreated(),
                goalEntity.getUpdated()
        );
    }
}
