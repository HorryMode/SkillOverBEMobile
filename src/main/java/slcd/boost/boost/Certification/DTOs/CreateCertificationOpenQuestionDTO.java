package slcd.boost.boost.Certification.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCertificationOpenQuestionDTO {

    @NotNull
    private String question;

    private String responsible;

    private String term;

    private String result;
}
