package sistemagestioncombos;

import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para la clase ArchivoProducto.
 * Se enfoca en asegurar la correcta lectura y escritura de productos
 * en un archivo, manejando I/O real.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ArchivoProductoTest { // Aseguramos public class

    // Nombre del archivo temporal que se usará para las pruebas
    private static final String RUTA_ARCHIVO_TEMPORAL = "test_productos_temp.txt";

    private ArchivoProducto archivoProducto;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {
        // 1. Inicializar la clase a probar con la ruta del archivo temporal
        archivoProducto = new ArchivoProducto(RUTA_ARCHIVO_TEMPORAL);

        // 2. Crear objetos de prueba (los nombres se eligen para verificar orden alfabético)
        producto1 = new Producto("Manzana Roja", 5000.0, "Fruta", "Alta", 7, 2.5);
        producto2 = new Producto("Zanahoria", 2000.0, "Verdura", "Media", 14, 1.0);
    }

    @AfterAll
    static void cleanup() throws IOException {
        // *** IMPORTANTE ***: Eliminar el archivo temporal después de todas las pruebas.
        Files.deleteIfExists(Paths.get(RUTA_ARCHIVO_TEMPORAL));
        System.out.println("Archivo temporal de productos eliminado: " + RUTA_ARCHIVO_TEMPORAL);
    }

    // --------------------------------------------------------------------------------
    // Pruebas de Guardar (Escribir)
    // --------------------------------------------------------------------------------

    @Test
    @Order(1) // Esta prueba debe ejecutarse primero para crear el archivo
    @DisplayName("Guardar productos debe escribir la lista en el archivo y ordenarlos alfabéticamente")
    void guardarProductos_ListaValida_EscribeEnArchivo() throws IOException {
        // Arrange
        List<Producto> listaProductos = List.of(producto2, producto1);

        // Act
        archivoProducto.guardarProductos(listaProductos);

        // Assert
        File archivo = new File(RUTA_ARCHIVO_TEMPORAL);
        assertTrue(archivo.exists(), "El archivo debe existir después de guardar.");

        List<String> lineas = Files.readAllLines(Paths.get(RUTA_ARCHIVO_TEMPORAL));

        assertEquals(2, lineas.size(), "El archivo debe contener 2 líneas.");

        // El ArchivoProducto debe ordenar por nombre: Manzana (M) antes que Zanahoria (Z)
        assertTrue(lineas.get(0).startsWith("Manzana Roja"), "La primera línea debe ser 'Manzana Roja' (orden alfabético).");
        assertTrue(lineas.get(1).startsWith("Zanahoria"), "La segunda línea debe ser 'Zanahoria'.");
    }

    // --------------------------------------------------------------------------------
    // Pruebas de Cargar (Leer)
    // --------------------------------------------------------------------------------

    @Test
    @Order(2)
    @DisplayName("Cargar productos debe retornar la lista de productos guardados con datos correctos")
    void cargarProductos_ArchivoExisteYEsValido_RetornaLista() throws IOException {
        // Act
        // ¡CORRECCIÓN APLICADA! Llamando al método cargarProductos()
        List<Producto> productosLeidos = archivoProducto.cargarProductos();

        // Assert
        assertEquals(2, productosLeidos.size(), "Debe cargar los 2 productos del archivo.");

        // Verificación del primer producto (Manzana)
        Producto pLeido1 = productosLeidos.get(0);
        assertEquals("Manzana Roja", pLeido1.getNombre(), "El nombre debe coincidir.");
        assertEquals("Fruta", pLeido1.getTipo(), "El tipo debe coincidir.");
        assertEquals(5000.0, pLeido1.getPrecio(), 0.001, "El precio debe coincidir.");

        // Verificación del segundo producto (Zanahoria)
        Producto pLeido2 = productosLeidos.get(1);
        assertEquals("Zanahoria", pLeido2.getNombre(), "El nombre debe coincidir.");
        assertEquals("Verdura", pLeido2.getTipo(), "El tipo debe coincidir.");
        assertEquals(2000.0, pLeido2.getPrecio(), 0.001, "El precio debe coincidir.");
    }

    @Test
    @DisplayName("Cargar productos debe retornar lista vacía si el archivo no existe")
    void cargarProductos_ArchivoNoExiste_RetornaListaVacia() throws IOException {
        // Arrange
        ArchivoProducto archivoInexistente = new ArchivoProducto("archivo_temporal_nunca_creado.txt");

        // Act
        // ¡CORRECCIÓN APLICADA! Llamando al método cargarProductos()
        List<Producto> productosLeidos = archivoInexistente.cargarProductos();

        // Assert
        assertTrue(productosLeidos.isEmpty(), "Debe retornar una lista vacía si el archivo no existe.");
    }

    // --------------------------------------------------------------------------------
    // Pruebas de Constructor
    // --------------------------------------------------------------------------------

    @Test
    @DisplayName("El constructor debe lanzar IllegalArgumentException si la ruta es nula o vacía")
    void constructor_RutaInvalida_LanzaExcepcion() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ArchivoProducto(null), "Ruta nula debe lanzar excepción.");
        assertThrows(IllegalArgumentException.class, () -> new ArchivoProducto(""), "Ruta vacía debe lanzar excepción.");
    }
    @Nested
    public class ArchivoProductoTestAdicional {

        private static final String RUTA_TEST = "test_productos_adicional.txt";
        private ArchivoProducto archivoProducto;

        @BeforeEach
        void setUp() {
            archivoProducto = new ArchivoProducto(RUTA_TEST);
        }

        @AfterEach
        void cleanup() throws IOException {
            Files.deleteIfExists(Paths.get(RUTA_TEST));
        }

        // --- PRUEBA 1: Cubrir el ordenamiento cuando el archivo NO existe ---
        @Test
        @DisplayName("cargarProductos: Debe ejecutar ordenamiento incluso cuando archivo no existe")
        void cargarProductos_ArchivoNoExiste_EjecutaOrdenamiento() throws IOException {
            // Arrange: archivo no existe
            String rutaInexistente = "archivo_que_nunca_existira_123.txt";
            ArchivoProducto archivo = new ArchivoProducto(rutaInexistente);

            // Act: Esto ejecutará el bloque de ordenamiento (líneas 37-47)
            List<Producto> resultado = archivo.cargarProductos();

            // Assert
            assertNotNull(resultado);
            assertTrue(resultado.isEmpty());
        }

        // --- PRUEBA 2: Líneas malformadas (partes.length != 6) ---
        @Test
        @DisplayName("cargarProductos: Debe ignorar líneas malformadas (menos de 6 campos)")
        void cargarProductos_LineasMalformadas_IgnoraLineasInvalidas() throws IOException {
            // Arrange: Archivo con línea válida, línea con 3 campos, y línea vacía
            String contenido =
                    "Manzana;1500.0;Fruta;Alta;10;5.0\n" +
                            "Linea;Incompleta;Solo3\n" +  // Solo 3 campos, debe ser ignorada
                            "\n" +                          // Línea vacía
                            "Pera;1000.0;Fruta;Baja;5;2.0\n";

            Files.writeString(Paths.get(RUTA_TEST), contenido);

            // Act
            List<Producto> productos = archivoProducto.cargarProductos();

            // Assert: Solo debe cargar las 2 líneas válidas
            assertEquals(2, productos.size());
            assertEquals("Manzana", productos.get(0).getNombre());
            assertEquals("Pera", productos.get(1).getNombre());
        }

        // --- PRUEBA 3: Línea con más de 6 campos (debe procesar solo los primeros 6) ---
        @Test
        @DisplayName("cargarProductos: Debe procesar líneas con campos extra correctamente")
        void cargarProductos_LineasConCamposExtra_ProcesaSoloNecesarios() throws IOException {
            // Arrange: Línea con 8 campos (los últimos 2 son extra)
            String contenido = "Tomate;500.0;Verdura;Media;7;3.5;CampoExtra;OtroCampo\n";
            Files.writeString(Paths.get(RUTA_TEST), contenido);

            // Act
            List<Producto> productos = archivoProducto.cargarProductos();

            // Assert: Debe procesar correctamente usando solo los primeros 6
            assertEquals(1, productos.size());
            Producto p = productos.get(0);
            assertEquals("Tomate", p.getNombre());
            assertEquals(500.0, p.getPrecio(), 0.001);
            assertEquals("Verdura", p.getTipo());
            assertEquals(3.5, p.getCantidadKg(), 0.001);
        }

        // --- PRUEBA 4: Manejo de NumberFormatException ---
        @Test
        @DisplayName("cargarProductos: Debe lanzar NumberFormatException con datos no numéricos")
        void cargarProductos_DatosNoNumericos_LanzaExcepcion() throws IOException {
            // Arrange: Precio no es número
            String contenido = "Producto;NO_ES_NUMERO;Fruta;Alta;10;5.0\n";
            Files.writeString(Paths.get(RUTA_TEST), contenido);

            // Act & Assert
            assertThrows(NumberFormatException.class, () -> {
                archivoProducto.cargarProductos();
            });
        }

        // --- PRUEBA 5: Datos con espacios en blanco (trim) ---
        @Test
        @DisplayName("cargarProductos: Debe hacer trim correctamente de los datos")
        void cargarProductos_DatosConEspacios_HaceTrimCorrectamente() throws IOException {
            // Arrange: Datos con espacios al inicio y final
            String contenido = "  Naranja  ; 800.0 ; Fruta ; Media ; 12 ; 4.5 \n";
            Files.writeString(Paths.get(RUTA_TEST), contenido);

            // Act
            List<Producto> productos = archivoProducto.cargarProductos();

            // Assert
            assertEquals(1, productos.size());
            Producto p = productos.get(0);
            assertEquals("Naranja", p.getNombre()); // Sin espacios
            assertEquals("Fruta", p.getTipo());     // Sin espacios
        }

        // --- PRUEBA 6: Guardar lista con productos ordenados alfabéticamente ---
        @Test
        @DisplayName("guardarProductos: Debe ordenar por nombre (case-insensitive)")
        void guardarProductos_DebeOrdenarPorNombre() throws IOException {
            // Arrange: Lista desordenada
            List<Producto> productos = new ArrayList<>();
            productos.add(new Producto("Zanahoria", 500.0, "Verdura", "Baja", 10, 2.0));
            productos.add(new Producto("Aguacate", 1500.0, "Fruta", "Alta", 5, 1.0));
            productos.add(new Producto("manzana", 1000.0, "Fruta", "Media", 7, 3.0)); // minúscula

            // Act
            archivoProducto.guardarProductos(productos);

            // Assert: Verificar orden en archivo
            List<String> lineas = Files.readAllLines(Paths.get(RUTA_TEST));
            assertEquals(3, lineas.size());

            // Orden debe ser: Aguacate, manzana, Zanahoria (case-insensitive)
            assertTrue(lineas.get(0).startsWith("Aguacate"));
            assertTrue(lineas.get(1).startsWith("manzana"));
            assertTrue(lineas.get(2).startsWith("Zanahoria"));
        }

        // --- PRUEBA 7: Guardar y cargar ciclo completo ---
        @Test
        @DisplayName("guardarProductos y cargarProductos: Ciclo completo de persistencia")
        void guardarYCargar_CicloCompleto_MantieneDatos() throws IOException {
            // Arrange
            List<Producto> productosOriginales = List.of(
                    new Producto("Banana", 600.0, "Fruta", "Alta", 3, 5.0),
                    new Producto("Lechuga", 400.0, "Verdura", "Media", 5, 1.5)
            );

            // Act: Guardar
            archivoProducto.guardarProductos(productosOriginales);

            // Act: Cargar
            List<Producto> productosCargados = archivoProducto.cargarProductos();

            // Assert: Verificar que los datos se mantienen
            assertEquals(2, productosCargados.size());

            // Verificar primer producto (ordenado alfabéticamente: Banana)
            Producto p1 = productosCargados.get(0);
            assertEquals("Banana", p1.getNombre());
            assertEquals(600.0, p1.getPrecio(), 0.001);
            assertEquals(5.0, p1.getCantidadKg(), 0.001);

            // Verificar segundo producto (Lechuga)
            Producto p2 = productosCargados.get(1);
            assertEquals("Lechuga", p2.getNombre());
            assertEquals(400.0, p2.getPrecio(), 0.001);
        }

        // --- PRUEBA 8: Guardar lista vacía ---
        @Test
        @DisplayName("guardarProductos: Debe crear archivo vacío con lista vacía")
        void guardarProductos_ListaVacia_CreaArchivoVacio() throws IOException {
            // Arrange
            List<Producto> listaVacia = new ArrayList<>();

            // Act
            archivoProducto.guardarProductos(listaVacia);

            // Assert
            File archivo = new File(RUTA_TEST);
            assertTrue(archivo.exists());

            List<String> lineas = Files.readAllLines(Paths.get(RUTA_TEST));
            assertTrue(lineas.isEmpty() || lineas.get(0).isEmpty());
        }

        // --- PRUEBA 9: Constructor con ruta con espacios ---
        @Test
        @DisplayName("Constructor: Debe lanzar excepción con ruta solo de espacios")
        void constructor_RutaSoloEspacios_LanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> {
                new ArchivoProducto("   ");
            });
        }

        // --- PRUEBA 10: Archivo con una sola línea válida ---
        @Test
        @DisplayName("cargarProductos: Debe cargar correctamente archivo con un solo producto")
        void cargarProductos_UnSoloProducto_CargaCorrectamente() throws IOException {
            // Arrange
            String contenido = "Kiwi;1200.0;Fruta;Baja;14;2.0\n";
            Files.writeString(Paths.get(RUTA_TEST), contenido);

            // Act
            List<Producto> productos = archivoProducto.cargarProductos();

            // Assert
            assertEquals(1, productos.size());
            assertEquals("Kiwi", productos.get(0).getNombre());
        }

        @Test
        @DisplayName("cargarProductos: Debe ejecutar todas las ramas del ordenamiento incluyendo tipos no estándar")
        void cargarProductos_TiposNoEstandar_CubreTodasLasRamas() throws IOException {
            // Arrange: Productos de prueba que NO son frutas ni verduras
            String contenido =
                    "Leche;500.0;Lacteo;Media;3;1.5\n" +
                            "Queso;800.0;Lacteo;Alta;10;0.5\n";

            Files.writeString(Paths.get(RUTA_TEST), contenido);

            // Act
            List<Producto> productos = archivoProducto.cargarProductos();

            // Assert: Verificamos que se carguen correctamente
            // Esto ejecuta la línea 53 (return 2) para cubrir esa rama
            assertEquals(2, productos.size());
            assertEquals("Leche", productos.get(0).getNombre());
            assertEquals("Queso", productos.get(1).getNombre());

            // Ambos son tipo "Lacteo", no fruta ni verdura
            assertEquals("Lacteo", productos.get(0).getTipo());
            assertEquals("Lacteo", productos.get(1).getTipo());
        }
    }
}