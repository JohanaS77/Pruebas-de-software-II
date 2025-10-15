package sistemagestioncombos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComboTest {

    // Constantes para pruebas
    private static final String NOMBRE_VALIDO = "Combo Familiar";
    private static final String TEMPORADA_VALIDA = "Alta";
    private static final double DESCUENTO_VALIDO = 10.0;
    private static final int UNIDADES_VALIDAS = 5;

    private Producto productoA;
    private Producto productoB;

    @BeforeEach
    void setUp() {
        // Productos simulados que se inicializan antes de CADA prueba
        // Lo importante para el Combo son los precios unitarios:
        // Producto A: Precio Unitario = 1000.0/Kg. Cantidad en inventario: 2.0 Kg
        productoA = new Producto("Manzana", 1000.0, "Fruta", "Alta", 10, 2.0);
        // Producto B: Precio Unitario = 5000.0/Kg. Cantidad en inventario: 0.5 Kg
        productoB = new Producto("Zanahoria", 5000.0, "Verdura", "Media", 20, 0.5);
    }

    // --- Pruebas del Constructor y Getters ---

    @Test
    @DisplayName("Constructor: Crear combo con valores iniciales válidos")
    void constructor_DebeCrearComboConValoresValidos() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);

        assertEquals(NOMBRE_VALIDO, combo.getNombre());
        assertEquals(TEMPORADA_VALIDA, combo.getTemporada());
        assertEquals(DESCUENTO_VALIDO, combo.getDescuento());
        assertEquals(UNIDADES_VALIDAS, combo.getUnidades());
        assertEquals(0.0, combo.getPrecioOriginal(), "El precio original debe ser 0 al inicio");
        assertEquals(0.0, combo.getPrecioFinal(), "El precio final debe ser 0 al inicio");
        assertTrue(combo.getProductos().isEmpty(), "La lista de productos debe estar vacía al inicio");
    }

    // --- Pruebas de Excepciones en Constructor y Setters ---

    @Test
    @DisplayName("Constructor: Argumentos inválidos deben lanzar IllegalArgumentException")
    void constructor_ArgumentosInvalidos_DebeLanzarExcepcion() {
        // Nombre nulo o vacío
        assertThrows(IllegalArgumentException.class, () -> new Combo(null, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS));
        assertThrows(IllegalArgumentException.class, () -> new Combo("", TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS));

        // Temporada nula o vacía
        assertThrows(IllegalArgumentException.class, () -> new Combo(NOMBRE_VALIDO, null, DESCUENTO_VALIDO, UNIDADES_VALIDAS));

        // Descuento fuera de rango [0, 100]
        assertThrows(IllegalArgumentException.class, () -> new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, -1.0, UNIDADES_VALIDAS));
        assertThrows(IllegalArgumentException.class, () -> new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, 101.0, UNIDADES_VALIDAS));

        // Unidades negativas
        assertThrows(IllegalArgumentException.class, () -> new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, -1));
    }

    @Test
    @DisplayName("Setters: Descuento inválido y unidades negativas deben lanzar excepción")
    void setters_ArgumentosInvalidos_DebeLanzarExcepcion() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);

        // Descuento
        assertThrows(IllegalArgumentException.class, () -> combo.setDescuento(-5.0));
        assertThrows(IllegalArgumentException.class, () -> combo.setDescuento(100.1));

        // Unidades
        assertThrows(IllegalArgumentException.class, () -> combo.setUnidades(-10));
    }

    // --- Pruebas de Gestión de Productos y Recálculos ---

    @Test
    @DisplayName("agregarProducto: Debe agregar producto y recalcular precios correctamente (basado en precio unitario)")
    void agregarProducto_DebeRecalcularPrecios() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);

        // 1. Agregar Producto A
        combo.agregarProducto(productoA); // Precio/Kg: 1000.0
        assertEquals(1, combo.getProductos().size());

        // Precio Original esperado (solo la suma de precios unitarios): 1000.0
        assertEquals(1000.0, combo.getPrecioOriginal(), 0.001);
        // Precio final = 1000.0 * (1 - 0.10) = 900.0
        assertEquals(900.0, combo.getPrecioFinal(), 0.001);

        // 2. Agregar Producto B
        combo.agregarProducto(productoB); // Precio/Kg: 5000.0. Nuevo Original: 1000.0 + 5000.0 = 6000.0
        assertEquals(2, combo.getProductos().size());

        // Precio Original esperado: 6000.0
        assertEquals(6000.0, combo.getPrecioOriginal(), 0.001);
        // Precio final = 6000.0 * (1 - 0.10) = 5400.0
        assertEquals(5400.0, combo.getPrecioFinal(), 0.001);

        // 3. Prueba de excepción (producto nulo)
        assertThrows(IllegalArgumentException.class, () -> combo.agregarProducto(null));
    }

    @Test
    @DisplayName("agregarProducto: No debe agregar productos duplicados (mismo nombre/tipo/temporada)")
    void agregarProducto_NoDebeAgregarDuplicados() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);

        // Producto con los mismos atributos clave que productoA, pero diferente precio/cantidad
        Producto productoDuplicado = new Producto("manzana", 500.0, "fruta", "alta", 5, 1.0);

        combo.agregarProducto(productoA); // Precio/Kg: 1000.0

        // Intenta agregar el duplicado. El método equals de Producto lo detecta.
        assertThrows(IllegalStateException.class, () -> combo.agregarProducto(productoDuplicado),
                "Debe lanzar excepción al intentar agregar un producto ya existente");

        assertEquals(1, combo.getProductos().size(), "El tamaño de la lista de productos debe ser 1");
        // El precio original debe ser el del primer producto agregado
        assertEquals(1000.0, combo.getPrecioOriginal(), 0.001);
    }

    @Test
    @DisplayName("eliminarProducto: Debe eliminar producto y recalcular precios")
    void eliminarProducto_DebeEliminarYRecalcularPrecios() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        combo.agregarProducto(productoA); // 1000.0
        combo.agregarProducto(productoB); // Total Original: 6000.0

        // 1. Eliminar Producto A (Precio/Kg 1000.0)
        assertTrue(combo.eliminarProducto(productoA));
        assertEquals(1, combo.getProductos().size());
        // Nuevo Original: 5000.0 (solo B queda)
        assertEquals(5000.0, combo.getPrecioOriginal(), 0.001);
        // Nuevo Final: 5000.0 * 0.9 = 4500.0
        assertEquals(4500.0, combo.getPrecioFinal(), 0.001);

        // 2. Intentar eliminar un producto que ya no existe
        assertFalse(combo.eliminarProducto(productoA), "Debe retornar false si el producto no existe");

        // 3. Eliminar Producto B (deja la lista vacía)
        assertTrue(combo.eliminarProducto(productoB));
        assertTrue(combo.getProductos().isEmpty());
        // Original y Final deben ser 0.0
        assertEquals(0.0, combo.getPrecioOriginal(), 0.001);
        assertEquals(0.0, combo.getPrecioFinal(), 0.001);
    }

    @Test
    @DisplayName("setDescuento: Debe cambiar el descuento y recalcular el precio final")
    void setDescuento_DebeRecalcularPrecioFinal() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, 0.0, UNIDADES_VALIDAS);
        combo.agregarProducto(productoA); // Original: 1000.0. Final: 1000.0

        // 1. Aplicar 20% de descuento
        combo.setDescuento(20.0);
        assertEquals(20.0, combo.getDescuento());
        // Precio final = 1000.0 * (1 - 0.20) = 800.0
        assertEquals(800.0, combo.getPrecioFinal(), 0.001);

        // 2. Aplicar 100% de descuento
        combo.setDescuento(100.0);
        // Precio final = 1000.0 * (1 - 1.00) = 0.0
        assertEquals(0.0, combo.getPrecioFinal(), 0.001);
    }

    // --- Pruebas de Métodos de Negocio ---

    @Test
    @DisplayName("calcularValorTotalEnInventario: Debe calcular correctamente el valor total")
    void calcularValorTotalEnInventario_DebeCalcularCorrectamente() {
        // Combo: Precio Original 6000.0, Descuento 10.0%, Precio Final 5400.0, Unidades 5
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, 10.0, 5);
        combo.agregarProducto(productoA);
        combo.agregarProducto(productoB);

        // Valor total en inventario = PrecioFinal * Unidades = 5400.0 * 5 = 27000.0
        double esperado = 5400.0 * 5.0;
        assertEquals(esperado, combo.calcularValorTotalEnInventario(), 0.001,
                "El cálculo del valor total en inventario es incorrecto.");

        // Prueba con 0 unidades
        combo.setUnidades(0);
        assertEquals(0.0, combo.calcularValorTotalEnInventario(), 0.001);
    }

    // --- Pruebas de utilidades (equals y hashCode) ---

    @Test
    @DisplayName("Equals: Combos iguales (mismo nombre y temporada) deben ser iguales")
    void equals_MismosAtributosClave_DebeSerTrue() {
        Combo c1 = new Combo("Fiesta", "Baja", 10.0, 5);
        Combo c2 = new Combo("fiesta", "baja", 5.0, 10); // Diferente descuento y unidades

        assertTrue(c1.equals(c2));
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    @DisplayName("Equals: Combos diferentes deben ser diferentes")
    void equals_DiferentesAtributosClave_DebeSerFalse() {
        Combo c1 = new Combo("Fiesta", "Baja", 10.0, 5);
        Combo c2 = new Combo("Familiar", "Baja", 10.0, 5); // Diferente Nombre
        Combo c3 = new Combo("Fiesta", "Alta", 10.0, 5); // Diferente Temporada

        assertFalse(c1.equals(c2));
        assertFalse(c1.equals(c3));
    }

    @Test
    @DisplayName("setNombre: Debe actualizar nombre válido")
    void setNombre_NombreValido_DebeActualizar() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        combo.setNombre("Combo Ejecutivo");
        assertEquals("Combo Ejecutivo", combo.getNombre());
    }

    @Test
    @DisplayName("setNombre: Debe lanzar excepción con nombre nulo o vacío")
    void setNombre_NombreInvalido_DebeLanzarExcepcion() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> combo.setNombre(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> combo.setNombre("")),
                () -> assertThrows(IllegalArgumentException.class, () -> combo.setNombre("   "))
        );
    }

    @Test
    @DisplayName("setTemporada: Debe actualizar temporada válida")
    void setTemporada_TemporadaValida_DebeActualizar() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        combo.setTemporada("Baja");
        assertEquals("Baja", combo.getTemporada());
    }

    @Test
    @DisplayName("setTemporada: Debe lanzar excepción con temporada nula o vacía")
    void setTemporada_TemporadaInvalida_DebeLanzarExcepcion() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> combo.setTemporada(null)),
                () -> assertThrows(IllegalArgumentException.class, () -> combo.setTemporada("")),
                () -> assertThrows(IllegalArgumentException.class, () -> combo.setTemporada("  "))
        );
    }

    @Test
    @DisplayName("getProductos: Debe retornar copia defensiva (encapsulamiento)")
    void getProductos_DebeRetornarCopiaDefensiva() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        combo.agregarProducto(productoA);

        List<Producto> listaObtenida = combo.getProductos();
        int tamañoOriginal = listaObtenida.size();

        Producto nuevoProducto = new Producto("Naranja", 800.0, "Fruta", "Baja", 3, 1.5);
        listaObtenida.add(nuevoProducto);

        assertEquals(tamañoOriginal, combo.getProductos().size());
    }

    @Test
    @DisplayName("toString: Debe retornar representación String válida")
    void toString_DebeRetornarFormatoValido() {
        Combo combo = new Combo("Combo Test", "Media", 15.0, 3);
        combo.agregarProducto(productoA);

        String resultado = combo.toString();
        assertTrue(resultado.contains("Combo Test"));
        assertTrue(resultado.contains("Media"));
    }

    @Test
    @DisplayName("equals: Debe retornar true al comparar el mismo objeto")
    void equals_MismoObjeto_DebeSerTrue() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        assertTrue(combo.equals(combo));
    }

    @Test
    @DisplayName("equals: Debe retornar false con null")
    void equals_ObjetoNull_DebeSerFalse() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        assertFalse(combo.equals(null));
    }

    @Test
    @DisplayName("equals: Debe retornar false con objeto de otra clase")
    void equals_OtraClase_DebeSerFalse() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        assertFalse(combo.equals("No soy un Combo"));
    }

    @Test
    @DisplayName("setDescuento: Debe permitir descuento de 0%")
    void setDescuento_Cero_DebePermitir() {
        Combo combo = new Combo(NOMBRE_VALIDO, TEMPORADA_VALIDA, DESCUENTO_VALIDO, UNIDADES_VALIDAS);
        combo.agregarProducto(productoA);
        combo.setDescuento(0.0);
        assertEquals(combo.getPrecioOriginal(), combo.getPrecioFinal());
    }
}