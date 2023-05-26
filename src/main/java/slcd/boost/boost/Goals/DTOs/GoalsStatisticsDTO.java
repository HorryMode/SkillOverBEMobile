package slcd.boost.boost.Goals.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalsStatisticsDTO {

    private String name;
    private int inProgress;
    private int completed;
    private boolean isGoalsExists;
}
