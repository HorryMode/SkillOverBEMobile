package slcd.boost.boost.Goals.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateGoalRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String gradeThree;

    @NotNull
    private String gradeFour;

    @NotNull
    private String gradeFive;
}
