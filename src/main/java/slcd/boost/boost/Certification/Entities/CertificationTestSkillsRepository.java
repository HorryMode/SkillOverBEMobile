package slcd.boost.boost.Certification.Entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationTestSkillsRepository extends JpaRepository<CertificationTestSkillsEntity, Long> {
}