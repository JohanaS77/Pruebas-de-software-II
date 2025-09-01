public class ValidarContrasena {

    // Método que valida si una contraseña es segura
    public static boolean esContrasenaSegura(String contrasena) {
        if (contrasena.length() < 8) {
            return false; 
        }

        boolean tieneMayuscula = false;
        boolean tieneMinuscula = false;
        boolean tieneNumero = false;
        boolean tieneEspecial = false;

        String especiales = "!@#$%^&*";

        for (char c : contrasena.toCharArray()) {
            if (Character.isUpperCase(c)) {
                tieneMayuscula = true;
            } else if (Character.isLowerCase(c)) {
                tieneMinuscula = true;
            } else if (Character.isDigit(c)) {
                tieneNumero = true;
            } else if (especiales.indexOf(c) != -1) {
                tieneEspecial = true;
            }
        }

        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
    }

    // Método principal para probar
    public static void main(String[] args) {
        String pass1 = "Abc123!@";
        String pass2 = "abcd1234";
        String pass3 = "ABCD1234!";
        String pass4 = "Segura#2024";

        System.out.println("¿'Abc123!@' es segura? " + esContrasenaSegura(pass1)); // true
        System.out.println("¿'abcd1234' es segura? " + esContrasenaSegura(pass2)); // false
        System.out.println("¿'ABCD1234!' es segura? " + esContrasenaSegura(pass3)); // false
        System.out.println("¿'Segura#2024' es segura? " + esContrasenaSegura(pass4)); // true
    }
}
