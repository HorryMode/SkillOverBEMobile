package slcd.boost.boost.Certification.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCertificationOpenQuestionDTO {

    private Long openQuestionId;

    @NotNull
    private String question;

    private String responsible;

    private String term;

    private String result;
}
