package slcd.boost.boost.Certification.Exceptions;

import slcd.boost.boost.General.Exceptions.ResourceNotFoundException;

public class CertificationNotFoundException extends ResourceNotFoundException {
    public CertificationNotFoundException(String message) {
        super(message);
    }
}
