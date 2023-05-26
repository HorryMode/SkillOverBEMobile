package slcd.boost.boost.Auths.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slcd.boost.boost.Auths.Entities.RefreshSessionEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshSessionRepository extends JpaRepository<RefreshSessionEntity, Long> {
    List<RefreshSessionEntity> findByFingerprintAndRefreshToken(String fingerprint, UUID refreshToken);
    List<RefreshSessionEntity> findByFingerprint(String fingerprint);
}