package sistemagestioncombos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class Combo {
    private String nombre;
    private String temporada;
    private List<Producto> productos;
    private double precioOriginal;
    private double descuento; // en porcentaje
    private double precioFinal;
    private int unidades;

    // Constructor
    public Combo(String nombre, String temporada, double descuento, int unidades) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del combo no puede estar vacío");
        }
        if (temporada == null || temporada.isBlank()) {
            throw new IllegalArgumentException("La temporada no puede estar vacía");
        }
        if (descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        }
        if (unidades < 0) {
            throw new IllegalArgumentException("Las unidades no pueden ser negativas");
        }

        this.nombre = nombre;
        this.temporada = temporada;
        this.descuento = descuento;
        this.unidades = unidades;
        this.productos = new ArrayList<>();
        this.precioOriginal = 0;
        this.precioFinal = 0;
    }

    // Agregar producto al combo
    public void agregarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        // Esto asegura que no se añadan dos productos "iguales" (por nombre, tipo y temporada)
        // al mismo combo, respetando la lógica de equals de Producto.
        if (productos.contains(producto)) {
            throw new IllegalStateException("El producto con nombre " + producto.getNombre() +
                    " ya existe en el combo.");
        }
        productos.add(producto);
        recalcularPrecios();
    }

    // Eliminar producto del combo
    public boolean eliminarProducto(Producto producto) {
        boolean eliminado = productos.remove(producto);
        if (eliminado) {
            recalcularPrecios();
        }
        return eliminado;
    }

    // Recalcular precios al modificar productos o descuento
    private void recalcularPrecios() {
        precioOriginal = 0;
        for (Producto p : productos) {
            precioOriginal += p.getPrecio();
        }
        precioFinal = precioOriginal * (1 - descuento / 100);
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        if (temporada == null || temporada.isBlank()) {
            throw new IllegalArgumentException("La temporada no puede estar vacía");
        }
        this.temporada = temporada;
    }

    public List<Producto> getProductos() {
        return new ArrayList<>(productos); // devolvemos copia para proteger encapsulamiento
    }

    public double getPrecioOriginal() {
        return precioOriginal;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        if (descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        }
        this.descuento = descuento;
        recalcularPrecios();
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        if (unidades < 0) {
            throw new IllegalArgumentException("Las unidades no pueden ser negativas");
        }
        this.unidades = unidades;
    }

    // Métodos de negocio
    public double calcularValorTotalEnInventario() {
        return precioFinal * unidades;
    }

    @Override
    public String toString() {
        return "Combo{" +
                "nombre='" + nombre + '\'' +
                ", temporada='" + temporada + '\'' +
                ", productos=" + productos.size() +
                ", precioOriginal=" + precioOriginal +
                ", descuento=" + descuento + "%" +
                ", precioFinal=" + precioFinal +
                ", unidades=" + unidades +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Combo)) return false;
        Combo combo = (Combo) o;
        return nombre.equalsIgnoreCase(combo.nombre) &&
                temporada.equalsIgnoreCase(combo.temporada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), temporada.toLowerCase());
    }
}
