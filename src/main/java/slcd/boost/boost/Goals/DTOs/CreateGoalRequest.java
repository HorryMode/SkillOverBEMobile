package slcd.boost.boost.Goals.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Certification.Entities.CertificationEntity;
import slcd.boost.boost.Goals.Entities.GoalEntity;
import slcd.boost.boost.Goals.Enums.EGoalStatus;
import slcd.boost.boost.Goals.Enums.EGradeStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGoalRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Long ownerId;

    @NotNull
    private String gradeThree;

    @NotNull
    private String gradeFour;

    @NotNull
    private String gradeFive;

    public GoalEntity mapToEntity(CertificationEntity certification){
        GoalEntity goalEntity = new GoalEntity();

        goalEntity.setUuid(UUID.randomUUID());
        goalEntity.setCertification(certification);
        goalEntity.setName(this.getName());
        goalEntity.setDescription(this.getDescription());
        goalEntity.setGradeThree(this.getGradeThree());
        goalEntity.setStatusThree(EGradeStatus.IN_PROGRESS);
        goalEntity.setGradeFour(this.getGradeFour());
        goalEntity.setStatusFour(EGradeStatus.IN_PROGRESS);
        goalEntity.setGradeFive(this.getGradeFive());
        goalEntity.setStatusFive(EGradeStatus.IN_PROGRESS);
        goalEntity.setStatus(EGoalStatus.IN_PROGRESS);
        goalEntity.setCreated(LocalDateTime.now());
        goalEntity.setUpdated(LocalDateTime.now());

        return goalEntity;
    }
}
