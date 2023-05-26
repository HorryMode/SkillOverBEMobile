package slcd.boost.boost.Certification.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Certification.Entities.CertificationEducationEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CertificationEducationDTO {

    @NotNull
    private String completedEducation;

    @NotNull
    private String managedDoBest;

    @NotNull
    private String wantedDoButFailed;

    @NotNull
    private String interestInTasks;

    @NotNull
    private String gotSkills;

    @NotNull
    private String educationPlans;

    public CertificationEducationEntity mapToEntity(){
        return new CertificationEducationEntity(
                this.getCompletedEducation(),
                this.getManagedDoBest(),
                this.getWantedDoButFailed(),
                this.getInterestInTasks(),
                this.getGotSkills(),
                this.getEducationPlans()
        );
    }

    public static CertificationEducationDTO mapFromEntity(CertificationEducationEntity educationEntity){
        return new CertificationEducationDTO(
                educationEntity.getCompletedEducation(),
                educationEntity.getManagedDoBest(),
                educationEntity.getWantedDoButFailed(),
                educationEntity.getInterestInTasks(),
                educationEntity.getGotSkills(),
                educationEntity.getEducationPlans()
        );
    }
}
