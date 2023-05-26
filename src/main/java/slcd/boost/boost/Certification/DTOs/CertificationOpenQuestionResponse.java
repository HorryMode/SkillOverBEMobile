package slcd.boost.boost.Certification.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Certification.Entities.CertificationOpenQuestionEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificationOpenQuestionResponse {

    private Long id;
    private String question;
    private String responsible;
    private String term;
    private String result;

    public static CertificationOpenQuestionResponse mapFromEntity(CertificationOpenQuestionEntity openQuestionEntity){
        return new CertificationOpenQuestionResponse(
                openQuestionEntity.getId(),
                openQuestionEntity.getQuestion(),
                openQuestionEntity.getResponsible(),
                openQuestionEntity.getTerm(),
                openQuestionEntity.getResult()
        );
    }
}
