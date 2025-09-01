import java.util.Scanner;
import java.math.BigInteger;
public class Ejercicio1TorreVasos {
    public static BigInteger calcularTotalVasos(BigInteger niveles) {
        if (niveles.compareTo(BigInteger.ZERO) <= 0) {
            return BigInteger.ZERO;
        }
        // Fórmula: n * (n + 1) / 2
        return niveles.multiply(niveles.add(BigInteger.ONE)).divide(BigInteger.valueOf(2));
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el número de niveles de la torre: ");
        BigInteger niveles = scanner.nextBigInteger();
        BigInteger total = calcularTotalVasos(niveles);
        System.out.println("Para " + niveles + " niveles se necesitan " + total + " vasos.");
    }
}

