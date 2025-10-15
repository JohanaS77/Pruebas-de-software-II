package sistemagestioncombos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas Unitarias para la clase GestorCombos.
 * Se utiliza Mockito para simular el comportamiento de la dependencia 'Inventario',
 * asegurando que solo se pruebe la lógica del GestorCombos.
 */
@ExtendWith(MockitoExtension.class)
class GestorCombosTest {

    @Mock
    private Inventario inventario;

    private GestorCombos gestor;
    private Combo comboDePrueba;
    private Producto productoDePrueba;

    @BeforeEach
    void setUp() {
        comboDePrueba = new Combo("Combo Veraniego", "Alta", 10.0, 5);
        productoDePrueba = new Producto("Naranja", 3900.0, "Fruta", "Alta", 15, 25.0);
        gestor = new GestorCombos(inventario);
    }

    // --------------------------------------------------------------------------------
    // Pruebas de Constructor
    // --------------------------------------------------------------------------------

    @Test
    @DisplayName("Constructor debe inicializar correctamente con Inventario no nulo")
    void constructor_InventarioNoNulo_InicializaCorrectamente() {
        Inventario mockInventario = mock(Inventario.class);
        GestorCombos gestorConMock = new GestorCombos(mockInventario);
        assertNotNull(gestorConMock, "El gestor debe ser inicializado");
    }

    @Test
    @DisplayName("Constructor debe lanzar IllegalArgumentException si Inventario es nulo")
    void constructor_InventarioNulo_LanzaExcepcion() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> new GestorCombos(null),
                "Debería lanzar IllegalArgumentException"
        );
        assertEquals("El inventario no puede ser nulo", thrown.getMessage());
    }

    // --------------------------------------------------------------------------------
    // Pruebas de crearCombo
    // --------------------------------------------------------------------------------

    @Test
    @DisplayName("crearCombo debe agregar un nuevo Combo al inventario")
    void crearCombo_ComboValido_AgregaAlInventario() {
        gestor.crearCombo("Combo Fresco", "Media", 5.0, 10);
        verify(inventario, times(1)).agregarCombo(any(Combo.class));
    }

    // --------------------------------------------------------------------------------
    // Pruebas de agregarProductoACombo
    // --------------------------------------------------------------------------------

    @Test
    @DisplayName("agregarProductoACombo debe agregar el producto a un combo existente")
    void agregarProductoACombo_ComboExiste_AgregaProducto() {
        when(inventario.buscarComboPorNombre("Combo Veraniego"))
                .thenReturn(Optional.of(comboDePrueba));
        int productosIniciales = comboDePrueba.getProductos().size();

        gestor.agregarProductoACombo("Combo Veraniego", productoDePrueba);

        assertEquals(productosIniciales + 1, comboDePrueba.getProductos().size(),
                "El producto debe ser agregado al combo");
        assertTrue(comboDePrueba.getProductos().contains(productoDePrueba),
                "El producto correcto debe estar en la lista");
    }

    @Test
    @DisplayName("agregarProductoACombo debe lanzar excepción si el combo no existe")
    void agregarProductoACombo_ComboNoExiste_LanzaExcepcion() {
        when(inventario.buscarComboPorNombre("Combo Inexistente"))
                .thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> gestor.agregarProductoACombo("Combo Inexistente", productoDePrueba),
                "Debería lanzar IllegalArgumentException"
        );
        assertEquals("No existe un combo con el nombre: Combo Inexistente", thrown.getMessage());
    }

    // --------------------------------------------------------------------------------
    // Pruebas de eliminarProductoDeCombo
    // --------------------------------------------------------------------------------

    @Test
    @DisplayName("eliminarProductoDeCombo debe retornar true si el producto es eliminado exitosamente")
    void eliminarProductoDeCombo_ProductoExiste_RetornaTrueYElimina() {
        comboDePrueba.agregarProducto(productoDePrueba);
        when(inventario.buscarComboPorNombre("Combo Veraniego"))
                .thenReturn(Optional.of(comboDePrueba));

        boolean resultado = gestor.eliminarProductoDeCombo("Combo Veraniego", "Naranja");

        assertTrue(resultado, "Debería retornar true al eliminar el producto");
        assertFalse(comboDePrueba.getProductos().contains(productoDePrueba),
                "El producto debe ser eliminado del combo");
    }

    @Test
    @DisplayName("eliminarProductoDeCombo debe retornar false si el combo no existe")
    void eliminarProductoDeCombo_ComboNoExiste_RetornaFalse() {
        when(inventario.buscarComboPorNombre("Combo Inexistente"))
                .thenReturn(Optional.empty());

        boolean resultado = gestor.eliminarProductoDeCombo("Combo Inexistente", "Naranja");

        assertFalse(resultado, "Debería retornar false si el combo no existe");
    }

    @Test
    @DisplayName("eliminarProductoDeCombo debe retornar false si el producto no está en el combo")
    void eliminarProductoDeCombo_ProductoNoExisteEnCombo_RetornaFalse() {
        when(inventario.buscarComboPorNombre("Combo Veraniego"))
                .thenReturn(Optional.of(comboDePrueba));

        boolean resultado = gestor.eliminarProductoDeCombo("Combo Veraniego", "Manzana");

        assertFalse(resultado, "Debería retornar false si el producto no está en el combo");
    }

    // --------------------------------------------------------------------------------
    // Pruebas de calcularValorCombo
    // --------------------------------------------------------------------------------

    @Test
    @DisplayName("calcularValorCombo debe retornar el precio final del combo")
    void calcularValorCombo_ComboExiste_RetornaPrecioFinal() {
        Producto p1 = new Producto("Manzana", 1000.0, "Fruta", "Alta", 10, 5.0);
        comboDePrueba.agregarProducto(p1);
        double precioEsperado = 900.0;

        when(inventario.buscarComboPorNombre("Combo Veraniego"))
                .thenReturn(Optional.of(comboDePrueba));

        double precioActual = gestor.calcularValorCombo("Combo Veraniego");

        assertEquals(precioEsperado, precioActual, 0.001,
                "El precio final debe ser calculado correctamente");
    }

    @Test
    @DisplayName("calcularValorCombo debe lanzar excepción si el combo no existe")
    void calcularValorCombo_ComboNoExiste_LanzaExcepcion() {
        when(inventario.buscarComboPorNombre("Combo Inexistente"))
                .thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> gestor.calcularValorCombo("Combo Inexistente"),
                "Debería lanzar IllegalArgumentException"
        );
        assertEquals("No existe un combo con el nombre: Combo Inexistente", thrown.getMessage());
    }

    // --------------------------------------------------------------------------------
    // Pruebas de totalCombosDisponibles
    // --------------------------------------------------------------------------------

    @Test
    @DisplayName("totalCombosDisponibles debe retornar el tamaño de la lista de combos del inventario")
    void totalCombosDisponibles_CombosExisten_RetornaConteo() {
        when(inventario.totalCombos()).thenReturn(2);

        int total = gestor.totalCombosDisponibles();

        assertEquals(2, total, "Debe retornar el número correcto de combos");
        verify(inventario, times(1)).totalCombos();
    }

    @Test
    @DisplayName("totalCombosDisponibles debe retornar 0 si no hay combos")
    void totalCombosDisponibles_NoHayCombos_RetornaCero() {
        when(inventario.totalCombos()).thenReturn(0);

        int total = gestor.totalCombosDisponibles();

        assertEquals(0, total, "Debe retornar 0 si no hay combos");
    }
}