package pe.edu.upeu;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@DisplayName("Calc Basic")
public class ServicioATest {
    private ServicioA servicioA;
    @BeforeAll
    static void setup(){
        System.out.println("Iniciando ServicioATest");
    }
    @BeforeEach
    void setUp(){
       servicioA=new ServicioAImpl();
    }
    @AfterAll
    static void finalizar(){
        System.out.println("Finalizando ServicioATest");
    }
    @Nested
    class OperacionesBasicas{

        @Test
        @DisplayName("Suma dos valores")
         void sumaDosValores(){
             int result=servicioA.sumar(2,3);
             Assertions.assertThat(result).isEqualTo(5);
        }

        @Test
        @DisplayName("Restar dos valores")
        void restarDosValores(){
            int result=servicioA.restar(6,3);
            Assertions.assertThat(result).isEqualTo(3);
        }

    }
    @Nested
    class Parametrizadas{

        @ParameterizedTest(name = "sumar({0}, {1}) = {2}")
        @CsvSource({"1,3, 4","3,4,7", "-5,-2, -7"})
        @DisplayName("Sumas parametrizadas")
        void sumaParametrizada(int num1, int num2, int num3){
            Assertions.assertThat(servicioA.sumar(num1,num2)).isEqualTo(num3);
        }

        @ParameterizedTest(name = "{0}/{1}={2}")
        @MethodSource("casosDeDivision")
        @DisplayName("Dividir Parame..")
        void dividirParametrizada(int num1, int num2, double num3){
            Assertions.assertThat(servicioA.dividir(num1,num2)).isEqualTo(num3);
        }

        static Stream<Arguments> casosDeDivision(){
            return Stream.of(
                    Arguments.of(10,2,5),
                    Arguments.of(6,2,3),
                    Arguments.of(-10,2,-5)

            );
        }

        @Test
        @DisplayName("Division con denominador cero")
        void divisionConCero(){
            Assertions.assertThatThrownBy(() -> servicioA.dividir(6,0))
                    .isInstanceOf(ArithmeticException.class)
            .hasMessageContaining("dividir por cero");

        }



    }



}
