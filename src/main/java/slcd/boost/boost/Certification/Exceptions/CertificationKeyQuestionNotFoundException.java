package slcd.boost.boost.Certification.Exceptions;

import slcd.boost.boost.General.Exceptions.ResourceNotFoundException;

public class CertificationKeyQuestionNotFoundException extends ResourceNotFoundException {

    public CertificationKeyQuestionNotFoundException(String message) {
        super(message);
    }
}
