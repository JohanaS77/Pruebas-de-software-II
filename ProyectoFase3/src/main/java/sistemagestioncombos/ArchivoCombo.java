package sistemagestioncombos;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ArchivoCombo {
    private final String rutaArchivo;

    public ArchivoCombo(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new IllegalArgumentException("La ruta del archivo no puede estar vacía");
        }
        this.rutaArchivo = rutaArchivo;
    }

    // Guardar lista de combos en archivo
    public void guardarCombos(List<Combo> combos) throws IOException {
        // Ordenar por nombre antes de escribir
        List<Combo> combosOrdenados = combos.stream()
                .sorted(Comparator.comparing(Combo::getNombre, String.CASE_INSENSITIVE_ORDER))
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Combo c : combosOrdenados) {
                String productosStr = c.getProductos().stream()
                        .map(Producto::getNombre)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");

                writer.write(c.getNombre() + ";" +
                        c.getTemporada() + ";" +
                        c.getPrecioOriginal() + ";" +
                        c.getDescuento() + ";" +
                        c.getPrecioFinal() + ";" +
                        c.getUnidades() + ";" +
                        productosStr);
                writer.newLine();
            }
            writer.flush(); // Asegurar que se escriban todos los datos
        } catch (IOException e) {
            System.err.println("Error al guardar combos en archivo: " + e.getMessage());
            throw e; // Re-lanzar para que el llamador pueda manejarlo
        }
    }


    // Leer combos desde archivo
    public List<Combo> cargarCombos(Inventario inventario) throws IOException {
        List<Combo> combos = new ArrayList<>();
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            return combos; // Si no existe, retornamos lista vacía (ya está vacía, no necesita ordenar)
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0;

            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();

                // Ignorar líneas vacías
                if (linea.isEmpty()) {
                    continue;
                }

                String[] partes = linea.split(";");
                if (partes.length < 6) {
                    System.err.println("Advertencia: Línea " + numeroLinea +
                            " tiene formato inválido y será ignorada.");
                    continue;
                }

                try {
                    String nombre = partes[0].trim();
                    String temporada = partes[1].trim();
                    double precioOriginal = Double.parseDouble(partes[2].trim());
                    double descuento = Double.parseDouble(partes[3].trim());
                    double precioFinal = Double.parseDouble(partes[4].trim());
                    int unidades = Integer.parseInt(partes[5].trim());

                    Combo combo = new Combo(nombre, temporada, descuento, unidades);

                    // Recuperar productos
                    if (partes.length >= 7 && !partes[6].trim().isEmpty()) {
                        String[] nombresProductos = partes[6].split(",");
                        for (String nombreProd : nombresProductos) {
                            String nombreLimpio = nombreProd.trim();
                            if (!nombreLimpio.isEmpty()) {
                                Optional<Producto> productoOpt = inventario.buscarProductoPorNombre(nombreLimpio);
                                if (productoOpt.isPresent()) {
                                    combo.agregarProducto(productoOpt.get());
                                } else {
                                    System.err.println("Advertencia: Producto '" + nombreLimpio +
                                            "' no encontrado para el combo '" + nombre + "'");
                                }
                            }
                        }
                    }

                    combos.add(combo);

                } catch (NumberFormatException e) {
                    System.err.println("Error de formato numérico en línea " + numeroLinea + ": " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.err.println("Error de validación en línea " + numeroLinea + ": " + e.getMessage());
                }
            }
        }

        // Ordenar combos alfabéticamente antes de retornarlos
        combos.sort(Comparator.comparing(Combo::getNombre, String.CASE_INSENSITIVE_ORDER));
        return combos;
    }
}
