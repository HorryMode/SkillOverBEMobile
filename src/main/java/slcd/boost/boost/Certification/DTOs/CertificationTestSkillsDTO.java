package slcd.boost.boost.Certification.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Certification.Entities.CertificationTestSkillsEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificationTestSkillsDTO {

    @NotNull
    private String hardSkills;

    @NotNull
    private String competencies;

    @NotNull
    private String testDocumentationWork;

    @NotNull
    private String testExperience;

    @NotNull
    private String productSupport;

    @NotNull
    private String softSkills;

    public CertificationTestSkillsEntity mapToEntity(){
        return new CertificationTestSkillsEntity(
                this.getHardSkills(),
                this.getCompetencies(),
                this.getTestDocumentationWork(),
                this.getTestExperience(),
                this.getProductSupport(),
                this.getSoftSkills()
        );
    }

    public static CertificationTestSkillsDTO mapFromEntity(CertificationTestSkillsEntity testSkillsEntity){
        return new CertificationTestSkillsDTO(
                testSkillsEntity.getHardSkills(),
                testSkillsEntity.getCompetencies(),
                testSkillsEntity.getTestDocumentationWork(),
                testSkillsEntity.getTestExperience(),
                testSkillsEntity.getProductSupport(),
                testSkillsEntity.getSoftSkills()
        );
    }
}
