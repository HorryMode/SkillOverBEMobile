package slcd.boost.boost.Certification;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import slcd.boost.boost.Certification.Exceptions.CertificationAlreadyExistException;
import slcd.boost.boost.Certification.Exceptions.CertificationKeyQuestionNotFoundException;
import slcd.boost.boost.Certification.Exceptions.CertificationNotFoundException;
import slcd.boost.boost.Certification.Exceptions.NotCompletedCertificationNotFoundException;
import slcd.boost.boost.General.DTOs.ExceptionResponse;

@ControllerAdvice(assignableTypes = CertificationController.class)
@Order(1)
public class CertificationAdvice {

    @ExceptionHandler(CertificationAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleCertificationAlreadyExist(CertificationAlreadyExistException e){
        ExceptionResponse response = new ExceptionResponse(409, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CertificationKeyQuestionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleKeyQuestionNotFound(CertificationKeyQuestionNotFoundException e){
        ExceptionResponse response = new ExceptionResponse(404, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CertificationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCertificationNotFound(CertificationNotFoundException e){
        ExceptionResponse response = new ExceptionResponse(404, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotCompletedCertificationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotCompletedCertificationNotFound(NotCompletedCertificationNotFoundException e){
        ExceptionResponse response = new ExceptionResponse(409, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
