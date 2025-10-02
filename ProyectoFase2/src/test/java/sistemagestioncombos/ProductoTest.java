package sistemagestioncombos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    // Constantes para pruebas
    private static final String NOMBRE_VALIDO = "Manzana";
    private static final double PRECIO_VALIDO = 1000.0;
    private static final String TIPO_VALIDO = "Fruta";
    private static final String TEMPORADA_VALIDA = "Alta";
    private static final int DIAS_VALIDOS = 5;
    private static final double CANTIDAD_VALIDA = 2.5;

    // --- Pruebas del Constructor y Getters (Estado Inicial) ---

    @Test
    @DisplayName("Constructor: Crear producto con valores válidos")
    void constructor_DebeCrearProductoConValoresValidos() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);

        assertEquals(NOMBRE_VALIDO, producto.getNombre());
        assertEquals(PRECIO_VALIDO, producto.getPrecio());
        assertEquals(TIPO_VALIDO, producto.getTipo());
        assertEquals(TEMPORADA_VALIDA, producto.getTemporada());
        assertEquals(DIAS_VALIDOS, producto.getDiasParaVencer());
        assertEquals(CANTIDAD_VALIDA, producto.getCantidadKg());
    }

    // --- Pruebas de Excepciones en el Constructor ---

    @Test
    @DisplayName("Constructor: Nombre nulo o vacío debe lanzar excepción")
    void constructor_NombreNuloOVacio_DebeLanzarExcepcion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Producto(null, PRECIO_VALIDO, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Producto(" ", PRECIO_VALIDO, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA))
        );
    }

    @Test
    @DisplayName("Constructor: Precio negativo debe lanzar excepción")
    void constructor_PrecioNegativo_DebeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Producto(NOMBRE_VALIDO, -1.0, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA));
    }

    @Test
    @DisplayName("Constructor: Tipo nulo o vacío debe lanzar excepción")
    void constructor_TipoNuloOVacio_DebeLanzarExcepcion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, null, TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, "", TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA))
        );
    }

    @Test
    @DisplayName("Constructor: Temporada nula o vacía debe lanzar excepción")
    void constructor_TemporadaNulaOVacia_DebeLanzarExcepcion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO, null, DIAS_VALIDOS, CANTIDAD_VALIDA)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO, " ", DIAS_VALIDOS, CANTIDAD_VALIDA))
        );
    }

    @Test
    @DisplayName("Constructor: Días para vencer negativos debe lanzar excepción")
    void constructor_DiasNegativos_DebeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO, TEMPORADA_VALIDA, -1, CANTIDAD_VALIDA));
    }

    @Test
    @DisplayName("Constructor: Cantidad en Kg menor o igual a cero debe lanzar excepción")
    void constructor_CantidadKgCeroONegativa_DebeLanzarExcepcion() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, 0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, -1.0))
        );
    }

    // --- Pruebas de Setters ---

    @Test
    @DisplayName("Setters: Modificar precio y verificar actualización")
    void setPrecio_DebeActualizarPrecio() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        double nuevoPrecio = 1500.0;
        producto.setPrecio(nuevoPrecio);
        assertEquals(nuevoPrecio, producto.getPrecio());

        // Prueba de excepción en setter
        assertThrows(IllegalArgumentException.class, () -> producto.setPrecio(-5.0));
    }

    @Test
    @DisplayName("Setters: Modificar días para vencer y verificar actualización")
    void setDiasParaVencer_DebeActualizarDias() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        int nuevosDias = 10;
        producto.setDiasParaVencer(nuevosDias);
        assertEquals(nuevosDias, producto.getDiasParaVencer());

        // Prueba de excepción en setter
        assertThrows(IllegalArgumentException.class, () -> producto.setDiasParaVencer(-1));
    }

    @Test
    @DisplayName("Setters: Modificar cantidad en Kg y verificar actualización")
    void setCantidadKg_DebeActualizarCantidad() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        double nuevaCantidad = 5.0;
        producto.setCantidadKg(nuevaCantidad);
        assertEquals(nuevaCantidad, producto.getCantidadKg());

        // Prueba de excepción en setter
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setCantidadKg(0.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setCantidadKg(-2.0))
        );
    }

    // --- Pruebas de Métodos de Negocio ---

    @Test
    @DisplayName("calcularValorTotal: Debe calcular el valor total correctamente")
    void calcularValorTotal_DebeRetornarCalculoCorrecto() {
        double precio = 5000.0;
        double cantidad = 3.0;
        Producto producto = new Producto(NOMBRE_VALIDO, precio, TIPO_VALIDO, TEMPORADA_VALIDA, DIAS_VALIDOS, cantidad);

        double esperado = precio * cantidad; // 15000.0
        assertEquals(esperado, producto.calcularValorTotal(), "El valor total no es el esperado.");
    }

    // --- Pruebas de utilidades (equals y hashCode) ---

    @Test
    @DisplayName("Equals: Productos iguales (mismo nombre, tipo, temporada) deben ser iguales")
    void equals_MismosAtributosClave_DebeSerTrue() {
        Producto p1 = new Producto("Tomate", 10.0, "Verdura", "Media", 1, 1.0);
        Producto p2 = new Producto("tomate", 20.0, "VERDURA", "media", 5, 2.0); // Diferente precio, días y cantidad

        // Solo se considera nombre, tipo y temporada para la igualdad
        assertTrue(p1.equals(p2));
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("Equals: Productos diferentes deben ser diferentes")
    void equals_DiferentesAtributosClave_DebeSerFalse() {
        Producto p1 = new Producto("Tomate", 10.0, "Verdura", "Media", 1, 1.0);
        Producto p2 = new Producto("Cebolla", 10.0, "Verdura", "Media", 1, 1.0); // Diferente Nombre
        Producto p3 = new Producto("Tomate", 10.0, "Fruta", "Media", 1, 1.0); // Diferente Tipo
        Producto p4 = new Producto("Tomate", 10.0, "Verdura", "Baja", 1, 1.0); // Diferente Temporada

        assertFalse(p1.equals(p2));
        assertFalse(p1.equals(p3));
        assertFalse(p1.equals(p4));
    }

    @Test
    @DisplayName("setNombre: Debe actualizar nombre válido")
    void setNombre_NombreValido_DebeActualizar() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        producto.setNombre("Pera");
        assertEquals("Pera", producto.getNombre());
    }

    @Test
    @DisplayName("setNombre: Debe lanzar excepción con nombre nulo o vacío")
    void setNombre_NombreInvalido_DebeLanzarExcepcion() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setNombre(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setNombre("")),
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setNombre("   "))
        );
    }

    @Test
    @DisplayName("setTipo: Debe actualizar tipo válido")
    void setTipo_TipoValido_DebeActualizar() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        producto.setTipo("Verdura");
        assertEquals("Verdura", producto.getTipo());
    }

    @Test
    @DisplayName("setTipo: Debe lanzar excepción con tipo nulo o vacío")
    void setTipo_TipoInvalido_DebeLanzarExcepcion() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setTipo(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setTipo("")),
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setTipo("  "))
        );
    }

    @Test
    @DisplayName("setTemporada: Debe actualizar temporada válida")
    void setTemporada_TemporadaValida_DebeActualizar() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        producto.setTemporada("Baja");
        assertEquals("Baja", producto.getTemporada());
    }

    @Test
    @DisplayName("setTemporada: Debe lanzar excepción con temporada nula o vacía")
    void setTemporada_TemporadaInvalida_DebeLanzarExcepcion() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setTemporada(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setTemporada("")),
                () -> assertThrows(IllegalArgumentException.class, () -> producto.setTemporada("   "))
        );
    }

    @Test
    @DisplayName("toString: Debe retornar representación String válida")
    void toString_DebeRetornarFormatoValido() {
        Producto producto = new Producto("Tomate", 500.0, "Verdura", "Media", 7, 3.0);
        String resultado = producto.toString();

        assertTrue(resultado.contains("Tomate"));
        assertTrue(resultado.contains("500.0"));
        assertTrue(resultado.contains("Verdura"));
    }

    @Test
    @DisplayName("equals: Debe retornar true al comparar el mismo objeto")
    void equals_MismoObjeto_DebeSerTrue() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        assertTrue(producto.equals(producto));
    }

    @Test
    @DisplayName("equals: Debe retornar false con null")
    void equals_ObjetoNull_DebeSerFalse() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        assertFalse(producto.equals(null));
    }

    @Test
    @DisplayName("equals: Debe retornar false con objeto de otra clase")
    void equals_OtraClase_DebeSerFalse() {
        Producto producto = new Producto(NOMBRE_VALIDO, PRECIO_VALIDO, TIPO_VALIDO,
                TEMPORADA_VALIDA, DIAS_VALIDOS, CANTIDAD_VALIDA);
        assertFalse(producto.equals("No soy un Producto"));
    }
}
