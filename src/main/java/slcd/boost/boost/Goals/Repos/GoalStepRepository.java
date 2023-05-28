package slcd.boost.boost.Goals.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slcd.boost.boost.Goals.Entities.GoalEntity;
import slcd.boost.boost.Goals.Entities.GoalStepEntity;

import java.util.List;

@Repository
public interface GoalStepRepository extends JpaRepository<GoalStepEntity, Long> {
    List<GoalStepEntity> findByGoal(GoalEntity goal);
}