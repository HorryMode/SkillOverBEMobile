package slcd.boost.boost.Goals.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Goals.Entities.GoalNoteEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoalNoteDTO {

    @NotNull
    private String text;

    public static GoalNoteDTO mapFromEntity(GoalNoteEntity noteEntity){
        return new GoalNoteDTO(
                noteEntity.getText()
        );
    }
}
