package pe.edu.upeu;

public class ServicioAImpl implements ServicioA {

    @Override
    public int sumar(int num1, int num2) {
        return num1 + num2;
    }

    @Override
    public int restar(int num1, int num2) {
        return num1 - num2;
    }

    @Override
    public double dividir(int num1, int num2) {
        if (num2 == 0) {
            throw  new ArithmeticException("No se puede dividir por cero");
        }
        return num1 / num2;
    }
}
