package slcd.boost.boost.Certification.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Certification.Entities.CertificationKeyQuestionEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CertificationKeyQuestionDTO {

    @NotNull
    private Long keyQuestionId;

    private String answer;

    public static CertificationKeyQuestionDTO mapFromEntity(CertificationKeyQuestionEntity keyQuestionEntity){
        return new CertificationKeyQuestionDTO(
                keyQuestionEntity.getSCertificationKeyQuestion().getId(),
                keyQuestionEntity.getAnswer()
        );
    }
}
