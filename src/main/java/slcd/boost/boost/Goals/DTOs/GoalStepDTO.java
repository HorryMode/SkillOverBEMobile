package slcd.boost.boost.Goals.DTOs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import slcd.boost.boost.Goals.Entities.GoalEntity;
import slcd.boost.boost.Goals.Entities.GoalStepEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalStepDTO {

    @NotNull
    private String text;

    @NotNull
    private boolean checked;

    public GoalStepEntity mapToEntity(GoalEntity goalEntity){
        //Создание новою сущности
        GoalStepEntity goalStepEntity = new GoalStepEntity();

        //Создание модели маппинга из DTO в сущность
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        TypeMap<GoalStepDTO, GoalStepEntity> typeMap = modelMapper.createTypeMap(GoalStepDTO.class, GoalStepEntity.class);
        typeMap.addMappings(mapper -> {
            mapper.skip(GoalStepEntity::setId);
            mapper.skip(GoalStepEntity::setGoal);
        });

        //Обновление сущности
        modelMapper.map(this, goalStepEntity);
        goalStepEntity.setGoal(goalEntity);

        return goalStepEntity;
    }

    public static GoalStepDTO mapFromEntity(GoalStepEntity goalStepEntity){
        //Создание DTO
        GoalStepDTO goalStepDTO = new GoalStepDTO();


        //Создание модели маппинга из DTO в сущность
        ModelMapper modelMapper = new ModelMapper();

        //Обновление сущности
        modelMapper.map(goalStepEntity, goalStepDTO);

        return goalStepDTO;
    }
}
