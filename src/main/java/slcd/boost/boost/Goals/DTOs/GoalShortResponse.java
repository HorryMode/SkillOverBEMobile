package slcd.boost.boost.Goals.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Goals.Entities.GoalEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalShortResponse {

    public String name;
    public String description;
    public String status;

    public static GoalShortResponse mapFromEntity(GoalEntity goalEntity){
        return new GoalShortResponse(
                goalEntity.getName(),
                goalEntity.getDescription(),
                goalEntity.getStatus().toString()
        );
    }
}
