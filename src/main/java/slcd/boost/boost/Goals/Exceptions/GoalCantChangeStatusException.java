package slcd.boost.boost.Goals.Exceptions;

public class GoalCantChangeStatusException extends RuntimeException{
    public GoalCantChangeStatusException(String message){
        super(message);
    }
}
