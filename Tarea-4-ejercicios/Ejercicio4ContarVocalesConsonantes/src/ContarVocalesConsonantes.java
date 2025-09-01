public class ContarVocalesConsonantes {

    // Método que cuenta vocales y consonantes en una cadena
    public static void contarVocalesConsonantes(String texto) {
        int vocales = 0;
        int consonantes = 0;

        texto = texto.toLowerCase(); // Convertimos a minúsculas para simplificar

        for (char c : texto.toCharArray()) {
            if (Character.isLetter(c)) {
                if ("aeiou".indexOf(c) != -1) {
                    vocales++;
                } else {
                    consonantes++;
                }
            }
        }

        System.out.println("Texto: " + texto);
        System.out.println("Número de vocales: " + vocales);
        System.out.println("Número de consonantes: " + consonantes);
    }

    // Método principal para probar
    public static void main(String[] args) {
        contarVocalesConsonantes("Hola Mundo");
        contarVocalesConsonantes("Johana es genial!");
        contarVocalesConsonantes("Me encanta la asignatura Pruebas de Software");
    }
}

