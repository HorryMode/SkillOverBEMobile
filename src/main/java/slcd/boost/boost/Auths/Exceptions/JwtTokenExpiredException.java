package slcd.boost.boost.Auths.Exceptions;

public class JwtTokenExpiredException extends RuntimeException{

    public JwtTokenExpiredException(String message){
        super(message);
    }
}
