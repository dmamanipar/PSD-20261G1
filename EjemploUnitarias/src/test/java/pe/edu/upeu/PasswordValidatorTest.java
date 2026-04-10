package pe.edu.upeu;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PasswordValidatorTest {

    private PasswordValidator validator;

    @BeforeEach
    void setup(){
        validator=new PasswordValidatorImp();
    }

    @Test
    void contrasenhaCompletaValida(){
        Assertions.assertThat(
                validator.isValid("Segura1!")).isTrue();
    }
}
