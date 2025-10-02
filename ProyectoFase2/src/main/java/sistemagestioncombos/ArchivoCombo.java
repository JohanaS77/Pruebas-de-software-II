package sistemagestioncombos;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            combos.stream()
                    .sorted(Comparator.comparing(Combo::getNombre, String.CASE_INSENSITIVE_ORDER))
                    .forEach(c -> {
                        try {
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
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }


    // Leer combos desde archivo
    public List<Combo> cargarCombos(Inventario inventario) throws IOException {
        List<Combo> combos = new ArrayList<>();

        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            combos.sort(Comparator.comparing(Combo::getNombre, String.CASE_INSENSITIVE_ORDER));
            return combos; // si no existe, retornamos lista vacía
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 6) {
                    String nombre = partes[0].trim();
                    String temporada = partes[1].trim();
                    double precioOriginal = Double.parseDouble(partes[2].trim());
                    double descuento = Double.parseDouble(partes[3].trim());
                    double precioFinal = Double.parseDouble(partes[4].trim());
                    int unidades = Integer.parseInt(partes[5].trim());

                    Combo combo = new Combo(nombre, temporada, descuento, unidades);

                    // Recuperar productos
                    if (partes.length >= 7) {
                        String[] nombresProductos = partes[6].split(",");
                        for (String nombreProd : nombresProductos) {
                            inventario.buscarProductoPorNombre(nombreProd.trim())
                                    .ifPresent(combo::agregarProducto);
                        }
                    }

                    if (combo.getPrecioOriginal() != precioOriginal || combo.getPrecioFinal() != precioFinal) {
                        // recalculará automáticamente con los productos agregados
                    }

                    combos.add(combo);
                }
            }
        }

        // 🔹 Ordenar combos alfabéticamente antes de retornarlos
        combos.sort(Comparator.comparing(Combo::getNombre, String.CASE_INSENSITIVE_ORDER));
        return combos;
    }
}
