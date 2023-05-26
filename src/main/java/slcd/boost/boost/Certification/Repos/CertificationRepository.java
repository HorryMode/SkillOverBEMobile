package slcd.boost.boost.Certification.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import slcd.boost.boost.Certification.Entities.CertificationEntity;
import slcd.boost.boost.Certification.Enums.ECertificationStatus;
import slcd.boost.boost.Users.Entities.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CertificationRepository extends JpaRepository<CertificationEntity, Long> {
    Optional<CertificationEntity> findByOwnerAndStatusNot(UserEntity owner, ECertificationStatus status);
    List<CertificationEntity> findByOwner(UserEntity owner);
    boolean existsByOwnerAndStatusNot(UserEntity owner, ECertificationStatus status);
    Optional<CertificationEntity> findByUuidAndStatusNot(UUID uuid, ECertificationStatus status);
    Optional<CertificationEntity> findByUuid(UUID uuid);
    Optional<CertificationEntity> findByStatusNot(ECertificationStatus status);
}