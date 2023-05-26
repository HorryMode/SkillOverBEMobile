package slcd.boost.boost.Certification.Exceptions;

import slcd.boost.boost.General.Exceptions.ResourceNotFoundException;

public class NotCompletedCertificationNotFoundException extends ResourceNotFoundException {
    public NotCompletedCertificationNotFoundException(String message) {
        super(message);
    }
}
