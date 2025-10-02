package sistemagestioncombos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
// No se necesita importar Collections si usamos List.copyOf() de Java 9+

public class Inventario {
    private List<Producto> productos;
    private List<Combo> combos;
    private Stack<Producto> productosRecientes;
    private Queue<Combo> colaRotacionCombos;

    // Constructor
    public Inventario() {
        this.productos = new ArrayList<>();
        this.combos = new ArrayList<>();
        this.productosRecientes = new Stack<>();
        this.colaRotacionCombos = new LinkedList<>();
    }

    // -------------------------
    // Gestión de Productos
    // -------------------------
    public void agregarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (buscarProductoPorNombre(producto.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un producto con el mismo nombre");
        }
        productos.add(producto);
        productosRecientes.push(producto);
    }

    public boolean eliminarProducto(String nombre) {
        return productos.removeIf(p -> p.getNombre().equalsIgnoreCase(nombre));
    }

    public Optional<Producto> buscarProductoPorNombre(String nombre) {
        return productos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    public List<Producto> listarProductos() {
        // CAMBIO APLICADO (Inmutabilidad y Encapsulamiento)
        return List.copyOf(productos);
    }

    public int totalProductos() {
        return productos.size();
    }

    // Método para obtener los últimos productos agregados (máximo 5)
    public List<Producto> obtenerProductosRecientes(int cantidad) {
        List<Producto> recientes = new ArrayList<>();
        Stack<Producto> tempStack = new Stack<>();

        // Sacar elementos del stack original (máximo la cantidad solicitada)
        int elementos = Math.min(cantidad, productosRecientes.size());

        for (int i = 0; i < elementos; i++) {
            if (!productosRecientes.isEmpty()) {
                Producto producto = productosRecientes.pop();
                recientes.add(producto);
                tempStack.push(producto);
            }
        }
        // Restaurar el stack original
        while (!tempStack.isEmpty()) {
            productosRecientes.push(tempStack.pop());
        }
        // Se devuelve el resultado como lista inmutable (consistencia)
        return List.copyOf(recientes);
    }

    // Método para obtener combo siguiente en rotación por temporada
    public Combo obtenerSiguienteComboRotacion() {
        if (colaRotacionCombos.isEmpty()) {
            return null;
        }

        // Sacar el primer combo de la cola y volverlo a poner al final
        Combo comboActual = colaRotacionCombos.poll();
        colaRotacionCombos.offer(comboActual);

        return comboActual;
    }

    // Método para obtener combos por temporada específica
    public List<Combo> obtenerCombosPorTemporada(String temporada) {
        // Se devuelve el resultado del filtro como lista inmutable (consistencia)
        return List.copyOf(combos.stream()
                .filter(c -> c.getTemporada().equalsIgnoreCase(temporada))
                .collect(ArrayList::new, (list, combo) -> list.add(combo), ArrayList::addAll));
    }

    // -------------------------
    // Gestión de Combos
    // -------------------------
    public void agregarCombo(Combo combo) {
        if (combo == null) {
            throw new IllegalArgumentException("El combo no puede ser nulo");
        }
        if (buscarComboPorNombre(combo.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un combo con el mismo nombre");
        }
        combos.add(combo);
        colaRotacionCombos.offer(combo);
    }

    public boolean eliminarCombo(String nombre) {
        boolean eliminadoDeCombos = combos.removeIf(c -> c.getNombre().equalsIgnoreCase(nombre));

        // CORRECCIÓN DE BUG DE INGENIERÍA: Si se elimina de la lista principal,
        // debe eliminarse de la cola de rotación para evitar errores.
        if (eliminadoDeCombos) {
            // Se reconstruye la cola sin el combo eliminado.
            colaRotacionCombos = new LinkedList<>(colaRotacionCombos.stream()
                    .filter(c -> !c.getNombre().equalsIgnoreCase(nombre))
                    .toList());
        }
        return eliminadoDeCombos;
    }

    public Optional<Combo> buscarComboPorNombre(String nombre) {
        return combos.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

    public List<Combo> listarCombos() {
        // CAMBIO APLICADO (Inmutabilidad y Encapsulamiento)
        return List.copyOf(combos);
    }

    public int totalCombos() {
        return combos.size();
    }

    // -------------------------
    // Métricas del Inventario
    // -------------------------
    public double calcularValorTotalInventario() {
        double total = 0;

        // Valor de todos los productos
        for (Producto p : productos) {
            total += p.calcularValorTotal();
        }

        // Valor de todos los combos
        for (Combo c : combos) {
            total += c.calcularValorTotalEnInventario();
        }

        return total;
    }
}