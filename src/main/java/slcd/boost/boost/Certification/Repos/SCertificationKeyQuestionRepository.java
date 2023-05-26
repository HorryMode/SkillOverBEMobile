package slcd.boost.boost.Certification.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slcd.boost.boost.Certification.Entities.SCertificationKeyQuestionEntity;

import java.util.Optional;

@Repository
public interface SCertificationKeyQuestionRepository extends JpaRepository<SCertificationKeyQuestionEntity, Long> {
    @Override
    Optional<SCertificationKeyQuestionEntity> findById(Long aLong);
}