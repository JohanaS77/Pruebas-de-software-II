public class OrdenAscendente {

    // Método que comprueba si el arreglo está en orden ascendente
    public static boolean estaOrdenadoAscendente(int[] arreglo) {
        for (int i = 0; i < arreglo.length - 1; i++) {
            if (arreglo[i] > arreglo[i + 1]) {
                return false;
            }
        }
        return true; 
    }

    // Método principal para probar
    public static void main(String[] args) {
        int[] arreglo1 = {1, 2, 3, 4, 5};
        int[] arreglo2 = {3, 2, 5, 6};
        int[] arreglo3 = {10, 10, 15, 20}; // con repetidos

        System.out.println("Arreglo1 está ordenado? " + estaOrdenadoAscendente(arreglo1));
        System.out.println("Arreglo2 está ordenado? " + estaOrdenadoAscendente(arreglo2));
        System.out.println("Arreglo3 está ordenado? " + estaOrdenadoAscendente(arreglo3));
    }
}

