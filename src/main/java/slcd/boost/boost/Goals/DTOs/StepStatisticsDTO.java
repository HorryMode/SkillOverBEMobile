package slcd.boost.boost.Goals.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepStatisticsDTO {

    private long inProgress;
    private long completed;
    private boolean isStepsExists;
}
