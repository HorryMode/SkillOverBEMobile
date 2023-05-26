package slcd.boost.boost.Certification.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import slcd.boost.boost.Certification.Entities.CertificationEntity;
import slcd.boost.boost.Certification.Enums.ECertificationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class CreateCertificationRequest {

    @NotNull
    private Long ownerId;

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
    private List<CreateCertificationGoalDTO> goals;

    @Valid
    private List<CreateCertificationOpenQuestionDTO> openQuestions;

    //Параметры owner заполнется в сервисе
    public CertificationEntity mapToEntity(){
        return new CertificationEntity(
                UUID.randomUUID(),
                null,
                this.getCertificationDateString(),
                this.getDuration(),
                this.getVenue(),
                null,
                null,
                null,
                null,
                null,
                ECertificationStatus.CREATED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
