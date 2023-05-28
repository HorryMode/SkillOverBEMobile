package slcd.boost.boost.Goals.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slcd.boost.boost.Goals.Entities.GoalNoteEntity;

@Repository
public interface GoalNoteRepository extends JpaRepository<GoalNoteEntity, Long> {
}