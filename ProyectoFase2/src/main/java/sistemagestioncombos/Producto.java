package sistemagestioncombos;

import java.util.Objects;
public class Producto {
    private String nombre;
    private double precio;
    private String tipo;
    private String temporada;
    private int diasParaVencer;
    private double cantidadKg;

    // Constructor
    public Producto(String nombre, double precio, String tipo,
                    String temporada, int diasParaVencer, double cantidadKg) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo no puede estar vacío");
        }
        if (temporada == null || temporada.isBlank()) {
            throw new IllegalArgumentException("La temporada no puede estar vacía");
        }
        if (diasParaVencer < 0) {
            throw new IllegalArgumentException("Los días para vencer no pueden ser negativos");
        }
        if (cantidadKg <= 0) {
            throw new IllegalArgumentException("La cantidad en Kg debe ser mayor a cero");
        }

        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
        this.temporada = temporada;
        this.diasParaVencer = diasParaVencer;
        this.cantidadKg = cantidadKg;
    }

    // Getters y Setters con validaciones
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo no puede estar vacío");
        }
        this.tipo = tipo;
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

    public int getDiasParaVencer() {
        return diasParaVencer;
    }

    public void setDiasParaVencer(int diasParaVencer) {
        if (diasParaVencer < 0) {
            throw new IllegalArgumentException("Los días para vencer no pueden ser negativos");
        }
        this.diasParaVencer = diasParaVencer;
    }

    public double getCantidadKg() {
        return cantidadKg;
    }

    public void setCantidadKg(double cantidadKg) {
        if (cantidadKg <= 0) {
            throw new IllegalArgumentException("La cantidad en Kg debe ser mayor a cero");
        }
        this.cantidadKg = cantidadKg;
    }

    // Calcular valor total del producto
    public double calcularValorTotal() {
        return this.precio * this.cantidadKg;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", tipo='" + tipo + '\'' +
                ", temporada='" + temporada + '\'' +
                ", diasParaVencer=" + diasParaVencer +
                ", cantidadKg=" + cantidadKg +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return nombre.equalsIgnoreCase(producto.nombre) &&
                tipo.equalsIgnoreCase(producto.tipo) &&
                temporada.equalsIgnoreCase(producto.temporada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), tipo.toLowerCase(), temporada.toLowerCase());
    }
}
