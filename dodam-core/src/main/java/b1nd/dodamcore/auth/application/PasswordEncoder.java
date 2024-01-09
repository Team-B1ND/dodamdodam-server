package b1nd.dodamcore.auth.application;

public interface PasswordEncoder {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encryptedPassword);

}