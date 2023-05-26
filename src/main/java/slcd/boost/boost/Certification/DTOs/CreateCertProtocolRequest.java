package slcd.boost.boost.Certification.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCertProtocolRequest {

    private String name;
    private Long ownerId;

}
