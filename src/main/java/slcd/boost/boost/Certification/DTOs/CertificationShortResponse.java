package slcd.boost.boost.Certification.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slcd.boost.boost.Certification.Entities.CertificationEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificationShortResponse {
    private String uuid;
    private String status;
    private LocalDateTime created;
    private LocalDateTime updated;

    public static CertificationShortResponse mapFromEntity(CertificationEntity certificationEntity){
        return new CertificationShortResponse(
                certificationEntity.getUuid().toString(),
                certificationEntity.getStatus().toString(),
                certificationEntity.getCreated(),
                certificationEntity.getUpdated()
        );
    }
}
