import java.util.Scanner;
public class Ejercicio2CajaGalletas {
    public static int calcularTotalGalletas(int galletas) {
        if (galletas <= 0) {
            return 0;
        }
        int total = 0;
        while (galletas >= 1) {
            total += galletas;
            galletas = galletas / 2;
        }
        return total;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese la cantidad inicial de galletas: ");
        int galletas = sc.nextInt();
        int total = calcularTotalGalletas(galletas);
        System.out.println("El total de galletas usadas en las cajas es: " + total);
        sc.close();
    }
}

