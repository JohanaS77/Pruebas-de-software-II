public class InversaCadenas {

    public static boolean esInversa(String cadena1, String cadena2) {
        if (cadena1.length() != cadena2.length()) {
            return false;
        }
        // Recorremos la primera cadena y la comparamos con la segunda al revés
        for (int i = 0; i < cadena1.length(); i++) {
            if (cadena1.charAt(i) != cadena2.charAt(cadena2.length() - 1 - i)) {
                return false; 
            }
        }

        return true; // Si pasó todas las comparaciones, son inversas
    }

    // Método principal para probar
    public static void main(String[] args) {
        String cadena1 = "hola";
        String cadena2 = "aloh";
        String cadena3 = "java";

        System.out.println("¿'hola' y 'aloh' son inversas? " + esInversa(cadena1, cadena2));
        System.out.println("¿'hola' y 'java' son inversas? " + esInversa(cadena1, cadena3));
    }
}

