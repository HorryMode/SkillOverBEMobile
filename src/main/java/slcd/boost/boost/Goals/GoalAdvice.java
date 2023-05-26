package slcd.boost.boost.Goals;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import slcd.boost.boost.General.DTOs.ExceptionResponse;
import slcd.boost.boost.Goals.Exceptions.GoalCantChangeStatusException;
import slcd.boost.boost.Goals.Exceptions.GoalNotFoundException;

@ControllerAdvice(assignableTypes = GoalController.class)
@Order(1)
public class GoalAdvice {

    @ExceptionHandler(GoalNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleGoalNotFound(GoalNotFoundException e){
        ExceptionResponse response = new ExceptionResponse(404, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GoalCantChangeStatusException.class)
    public ResponseEntity<ExceptionResponse> handleGoalCantChangeStatus(GoalCantChangeStatusException e){
        ExceptionResponse response = new ExceptionResponse(409, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
