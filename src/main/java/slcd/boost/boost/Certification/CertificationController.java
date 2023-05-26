package slcd.boost.boost.Certification;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import slcd.boost.boost.Certification.DTOs.CertificationResponse;
import slcd.boost.boost.Certification.DTOs.CertificationShortResponse;
import slcd.boost.boost.Certification.DTOs.CreateCertificationRequest;
import slcd.boost.boost.Certification.DTOs.UpdateCertificationRequest;
import slcd.boost.boost.General.DTOs.UUIDResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/certifications")
public class CertificationController {

    @Autowired
    private CertificationService certificationService;


    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public CertificationResponse findCertificationByUuid(@PathVariable(name = "uuid") String uuid) throws AccessDeniedException {
        return certificationService.findCertificationByUuidForController(uuid);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CertificationShortResponse> findCertificationsByOwnerId(@RequestParam(name = "ownerId") Long ownerId){
        return certificationService.findCertificationsByOwner(ownerId);
    }
}
