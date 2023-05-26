package slcd.boost.boost.Goals.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slcd.boost.boost.Goals.Entities.GoalEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
    Optional<GoalEntity> findByUuid(UUID uuid);
}