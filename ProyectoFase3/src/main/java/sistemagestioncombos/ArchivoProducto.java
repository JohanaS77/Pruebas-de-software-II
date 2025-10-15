package sistemagestioncombos;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class ArchivoProducto {
    private final String rutaArchivo;

    public ArchivoProducto(String rutaArchivo) {
        if (rutaArchivo == null || rutaArchivo.isBlank()) {
            throw new IllegalArgumentException("La ruta del archivo no puede estar vacía");
        }
        this.rutaArchivo = rutaArchivo;
    }

    // Guardar lista de productos en archivo
    public void guardarProductos(List<Producto> productos) throws IOException {
        // Ordenar por nombre antes de escribir
        List<Producto> productosOrdenados = productos.stream()
                .sorted(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER))
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Producto p : productosOrdenados) {
                writer.write(p.getNombre() + ";" +
                        p.getPrecio() + ";" +
                        p.getTipo() + ";" +
                        p.getTemporada() + ";" +
                        p.getDiasParaVencer() + ";" +
                        p.getCantidadKg());
                writer.newLine();
            }
            writer.flush(); // Asegurar que se escriban todos los datos
        } catch (IOException e) {
            System.err.println("Error al guardar productos en archivo: " + e.getMessage());
            throw e; // Re-lanzar para que el llamador pueda manejarlo
        }
    }
    public List<Producto> cargarProductos() throws IOException {
        List<Producto> productos = new ArrayList<>();
        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            return productos; // Retorna lista vacía
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
                            " tiene formato inválido y será ignorada. Formato esperado: nombre;precio;tipo;temporada;dias;cantidad");
                    continue;
                }

                try {
                    String nombre = partes[0].trim();
                    double precio = Double.parseDouble(partes[1].trim());
                    String tipo = partes[2].trim();
                    String temporada = partes[3].trim();
                    int diasParaVencer = Integer.parseInt(partes[4].trim());
                    double cantidadKg = Double.parseDouble(partes[5].trim());

                    // Validar que los campos de texto no estén vacíos
                    if (nombre.isEmpty() || tipo.isEmpty() || temporada.isEmpty()) {
                        System.err.println("Advertencia: Línea " + numeroLinea +
                                " contiene campos de texto vacíos y será ignorada.");
                        continue;
                    }

                    Producto producto = new Producto(
                            nombre, precio, tipo, temporada, diasParaVencer, cantidadKg
                    );
                    productos.add(producto);

                } catch (NumberFormatException e) {
                    System.err.println("Error de formato numérico en línea " + numeroLinea +
                            ": " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.err.println("Error de validación en línea " + numeroLinea +
                            ": " + e.getMessage());
                }
            }
        }

        return productos;
    }
}
