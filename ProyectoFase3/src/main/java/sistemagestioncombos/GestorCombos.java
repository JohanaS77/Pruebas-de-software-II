package sistemagestioncombos;

import java.util.Optional;
public class GestorCombos {
    private Inventario inventario;

    // Constructor
    public GestorCombos(Inventario inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("El inventario no puede ser nulo");
        }
        this.inventario = inventario;
    }

    // Crear un combo nuevo y agregarlo al inventario
    public void crearCombo(String nombre, String temporada, double descuento, int unidades) {
        Combo combo = new Combo(nombre, temporada, descuento, unidades);
        inventario.agregarCombo(combo);
    }

    // Agregar un producto existente a un combo
    public void agregarProductoACombo(String nombreCombo, Producto producto) {
        Optional<Combo> comboOpt = inventario.buscarComboPorNombre(nombreCombo);

        if (comboOpt.isEmpty()) {
            throw new IllegalArgumentException("No existe un combo con el nombre: " + nombreCombo);
        }

        comboOpt.get().agregarProducto(producto);
    }


    // Eliminar un producto de un combo
    public boolean eliminarProductoDeCombo(String nombreCombo, String nombreProducto) {
        Optional<Combo> comboOpt = inventario.buscarComboPorNombre(nombreCombo);
        if (comboOpt.isEmpty()) {
            return false;
        }
        Combo combo = comboOpt.get();

        // Buscar el producto dentro del combo (insensible a mayúsculas/minúsculas)
        Optional<Producto> productoEnCombo = combo.getProductos()
                .stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreProducto))
                .findFirst();

        if (productoEnCombo.isEmpty()) {
            return false;
        }

        // eliminarProducto(...) ya llama a recalcularPrecios() internamente
        return combo.eliminarProducto(productoEnCombo.get());
    }



    // Calcular el valor de un combo por nombre
    public double calcularValorCombo(String nombreCombo) {
        Optional<Combo> comboOpt = inventario.buscarComboPorNombre(nombreCombo);
        if (comboOpt.isEmpty()) {
            throw new IllegalArgumentException("No existe un combo con el nombre: " + nombreCombo);
        }
        return comboOpt.get().getPrecioFinal();
    }

    // Obtener total de combos disponibles en inventario
    public int totalCombosDisponibles() {
        return inventario.totalCombos();
    }
}


