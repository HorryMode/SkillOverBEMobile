package slcd.boost.boost.Certification.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCertificationGoalDTO {

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private String gradeThree;

    @NonNull
    private String gradeFour;

    @NonNull
    private String gradeFive;
}
