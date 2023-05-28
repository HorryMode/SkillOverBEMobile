package slcd.boost.boost.General.Exceptions;

public class UserNotHaveAuthoritiesException extends RuntimeException{

    public UserNotHaveAuthoritiesException(String message){
        super(message);
    }
}
