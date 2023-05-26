package slcd.boost.boost.Certification.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCertificationRequest {

    @NotNull
    private String certificationDateString;

    private String duration;

    private String venue;

    @Valid
    @NonNull
    private CertificationTestSkillsDTO testSkills;

    @Valid
    private CertificationEducationDTO education;

    @NonNull
    @Valid
    private List<CertificationKeyQuestionDTO> keyQuestions;

    @Valid
    private List<UpdateCertificationOpenQuestionDTO> openQuestions;
}
