package slcd.boost.boost.Goals.Exceptions;

import slcd.boost.boost.General.Exceptions.ResourceNotFoundException;

public class GoalNotFoundException extends ResourceNotFoundException {
    public GoalNotFoundException(String message) {
        super(message);
    }
}
