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
    // Guardar lista de productos en archivo
    public void guardarProductos(List<Producto> productos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            // Ordenar por nombre (o el criterio que quieras)
            productos.stream()
                    .sorted(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER))
                    .forEach(p -> {
                        try {
                            writer.write(p.getNombre() + ";" +
                                    p.getPrecio() + ";" +
                                    p.getTipo() + ";" +
                                    p.getTemporada() + ";" +
                                    p.getDiasParaVencer() + ";" +
                                    p.getCantidadKg());
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }


    // Leer productos desde archivo
    public List<Producto> cargarProductos() throws IOException {
        List<Producto> productos = new ArrayList<>();

        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return new ArrayList<>(); // Simplemente retorna lista vacía
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 6) {
                    String nombre = partes[0].trim();
                    double precio = Double.parseDouble(partes[1].trim());
                    String tipo = partes[2].trim();
                    String temporada = partes[3].trim();
                    int diasParaVencer = Integer.parseInt(partes[4].trim());
                    double cantidadKg = Double.parseDouble(partes[5].trim());

                    Producto producto = new Producto(
                            nombre, precio, tipo, temporada, diasParaVencer, cantidadKg
                    );
                    productos.add(producto);
                }
            }
        }

        return productos;
    }
}
