package pe.edu.upeu;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordValidatorTest {

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

    @ParameterizedTest
    @NullAndEmptySource
    void contrasenhaNulo(String passw){
        Assertions.assertThat(validator.isValid(passw)).isFalse();
        Assertions.assertThat(validator.nivelSeguridad(passw)).isZero();
    }

    @ParameterizedTest
    @ValueSource(strings = {"acd", "123456", "PASSWORD", "Password", "12Passswd"})
    void contrasenhaConNivelIncompleto(String passw){
        Assertions.assertThat(
                validator.isValid(passw)).isFalse();
    }


}
