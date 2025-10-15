package sistemagestioncombos;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Inventario inventario = new Inventario();
        GestorCombos gestor = new GestorCombos(inventario);
        ArchivoProducto archivoProductos = new ArchivoProducto("productos.txt");
        ArchivoCombo archivoCombos = new ArchivoCombo("combos.txt");

        // Cargar datos previos desde archivos
        try {
            List<Producto> productos = archivoProductos.cargarProductos();
            for (Producto p : productos) {
                inventario.agregarProducto(p);
            }
            List<Combo> combos = archivoCombos.cargarCombos(inventario);
            for (Combo c : combos) {
                inventario.agregarCombo(c);
            }
            System.out.println(" Datos cargados desde archivo.");
        } catch (IOException e) {
            System.out.println(" No se pudieron cargar los datos: " + e.getMessage());
        }

        int opcion;
        do {
            System.out.println("\n=== Sistema de Gestión de Combos ===");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Modificar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Crear combo");
            System.out.println("6. Agregar producto a combo");
            System.out.println("7. Listar combos");
            System.out.println("8. Modificar combo");
            System.out.println("9. Eliminar combo");
            System.out.println("10. Calcular valor total inventario");
            System.out.println("11. Ver productos recientes");
            System.out.println("12. Rotar combo promocional");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            try {
                switch (opcion) {
                    case 1 -> {
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.print("Precio (kg): ");
                        double precio = sc.nextDouble();
                        sc.nextLine();
                        System.out.print("Tipo: ");
                        String tipo = sc.nextLine();
                        System.out.print("Temporada: ");
                        String temporada = sc.nextLine();
                        System.out.print("Días para vencer: ");
                        int diasParaVencer = sc.nextInt();
                        System.out.print("Cantidad (Kg): ");
                        double cantidadKg = sc.nextDouble();
                        sc.nextLine();

                        Producto producto = new Producto(nombre, precio, tipo, temporada, diasParaVencer, cantidadKg);
                        inventario.agregarProducto(producto);
                        archivoProductos.guardarProductos(inventario.listarProductos());
                        System.out.println(" Producto agregado y guardado.");
                    }
                    case 2 -> {
                        System.out.println("=== Lista de Productos ===");
                        inventario.listarProductos().forEach(System.out::println);
                    }
                    case 3 -> {
                        System.out.print("Nombre del producto a modificar: ");
                        String nombreBuscar = sc.nextLine();
                        Optional<Producto> productoOpt = inventario.buscarProductoPorNombre(nombreBuscar);

                        if (productoOpt.isEmpty()) {
                            System.out.println(" No existe un producto con ese nombre.");
                            break;
                        }

                        Producto producto = productoOpt.get();
                        System.out.println("Producto actual: " + producto);

                        System.out.println("\n¿Qué desea modificar?");
                        System.out.println("1. Precio");
                        System.out.println("2. Tipo");
                        System.out.println("3. Temporada");
                        System.out.println("4. Días para vencer");
                        System.out.println("5. Cantidad (Kg)");
                        System.out.print("Seleccione opción: ");
                        int opcionMod = sc.nextInt();
                        sc.nextLine();

                        switch (opcionMod) {
                            case 1 -> {
                                System.out.print("Nuevo precio: ");
                                double nuevoPrecio = sc.nextDouble();
                                producto.setPrecio(nuevoPrecio);
                            }
                            case 2 -> {
                                System.out.print("Nuevo tipo: ");
                                String nuevoTipo = sc.nextLine();
                                producto.setTipo(nuevoTipo);
                            }
                            case 3 -> {
                                System.out.print("Nueva temporada: ");
                                String nuevaTemporada = sc.nextLine();
                                producto.setTemporada(nuevaTemporada);
                            }
                            case 4 -> {
                                System.out.print("Nuevos días para vencer: ");
                                int nuevosDias = sc.nextInt();
                                producto.setDiasParaVencer(nuevosDias);
                            }
                            case 5 -> {
                                System.out.print("Nueva cantidad (Kg): ");
                                double nuevaCantidad = sc.nextDouble();
                                producto.setCantidadKg(nuevaCantidad);
                            }
                            default -> System.out.println(" Opción no válida.");
                        }

                        archivoProductos.guardarProductos(inventario.listarProductos());
                        System.out.println(" Producto modificado y guardado.");
                    }
                    case 4 -> {
                        System.out.print("Nombre del producto a eliminar: ");
                        String nombreEliminar = sc.nextLine();

                        if (inventario.eliminarProducto(nombreEliminar)) {
                            archivoProductos.guardarProductos(inventario.listarProductos());
                            System.out.println(" Producto eliminado exitosamente.");
                        } else {
                            System.out.println(" No se encontró el producto especificado.");
                        }
                    }
                    case 5 -> {
                        System.out.print("Nombre combo: ");
                        String nombre = sc.nextLine();
                        System.out.print("Temporada: ");
                        String temporada = sc.nextLine();
                        System.out.print("Descuento (%): ");
                        double descuento = sc.nextDouble();
                        System.out.print("Unidades: ");
                        int unidades = sc.nextInt();
                        sc.nextLine();

                        gestor.crearCombo(nombre, temporada, descuento, unidades);

                        //  Pedir productos que forman el combo
                        System.out.println("Ingrese los nombres de los productos que forman el combo (separados por coma): ");
                        String productosInput = sc.nextLine();
                        String[] productosIngresados = productosInput.split(",");

                        for (String prod : productosIngresados) {
                            inventario.buscarProductoPorNombre(prod.trim())
                                    .ifPresent(p -> {
                                        // siempre agregar 1kg
                                        Producto copia = new Producto(
                                                p.getNombre(),
                                                p.getPrecio(),
                                                p.getTipo(),
                                                p.getTemporada(),
                                                p.getDiasParaVencer(),
                                                1.0
                                        );
                                        gestor.agregarProductoACombo(nombre, copia);
                                    });
                        }

                        archivoCombos.guardarCombos(inventario.listarCombos());
                        System.out.println(" Combo creado y guardado con productos.");
                    }

                    case 6 -> {
                        System.out.print("Nombre del combo: ");
                        String nombreCombo = sc.nextLine();
                        System.out.print("Nombre del producto: ");
                        String nombreProducto = sc.nextLine();

                        // Buscar el producto en el inventario antes de agregarlo al combo
                        Optional<Producto> productoOpt = inventario.buscarProductoPorNombre(nombreProducto);

                        if (productoOpt.isPresent()) {
                            gestor.agregarProductoACombo(nombreCombo, productoOpt.get());
                            archivoCombos.guardarCombos(inventario.listarCombos());
                            System.out.println("Producto agregado correctamente al combo y guardado.");
                        } else {
                            System.out.println("No existe un producto con el nombre: " + nombreProducto);
                        }
                    }

                    case 7 -> {
                        System.out.println("=== Lista de Combos ===");
                        inventario.listarCombos().forEach(System.out::println);
                    }
                    case 8 -> {
                        System.out.print("Nombre del combo a modificar: ");
                        String nombreComboBuscar = sc.nextLine();
                        Optional<Combo> comboOpt = inventario.buscarComboPorNombre(nombreComboBuscar);

                        if (comboOpt.isEmpty()) {
                            System.out.println(" No existe un combo con ese nombre.");
                            break;
                        }

                        Combo combo = comboOpt.get();
                        System.out.println("Combo actual: " + combo);

                        System.out.println("\n¿Qué desea modificar?");
                        System.out.println("1. Temporada");
                        System.out.println("2. Descuento");
                        System.out.println("3. Unidades");
                        System.out.println("4. Eliminar producto del combo");
                        System.out.print("Seleccione opción: ");
                        int opcionModCombo = sc.nextInt();
                        sc.nextLine();

                        switch (opcionModCombo) {
                            case 1 -> {
                                System.out.print("Nueva temporada: ");
                                String nuevaTemporada = sc.nextLine();
                                combo.setTemporada(nuevaTemporada);
                            }
                            case 2 -> {
                                System.out.print("Nuevo descuento (%): ");
                                double nuevoDescuento = sc.nextDouble();
                                combo.setDescuento(nuevoDescuento);
                            }
                            case 3 -> {
                                System.out.print("Nuevas unidades: ");
                                int nuevasUnidades = sc.nextInt();
                                combo.setUnidades(nuevasUnidades);
                            }
                            case 4 -> {
                                System.out.println("Productos en el combo:");
                                combo.getProductos().forEach(p -> System.out.println("- " + p.getNombre()));
                                System.out.print("Nombre del producto a eliminar: ");
                                String nombreProductoEliminar = sc.nextLine();

                                if (gestor.eliminarProductoDeCombo(nombreComboBuscar, nombreProductoEliminar)) {
                                    System.out.println(" Producto eliminado del combo.");
                                } else {
                                    System.out.println(" No se pudo eliminar el producto.");
                                }
                            }
                            default -> System.out.println(" Opción no válida.");
                        }

                        archivoCombos.guardarCombos(inventario.listarCombos());
                        System.out.println(" Combo modificado y guardado.");
                    }
                    case 9 -> {
                        System.out.print("Nombre del combo a eliminar: ");
                        String nombreComboEliminar = sc.nextLine();

                        if (inventario.eliminarCombo(nombreComboEliminar)) {
                            archivoCombos.guardarCombos(inventario.listarCombos());
                            System.out.println(" Combo eliminado exitosamente.");
                        } else {
                            System.out.println(" No se encontró el combo especificado.");
                        }
                    }
                    case 10 -> {
                        System.out.println(" Valor total inventario: " +
                                inventario.calcularValorTotalInventario());
                    }
                    case 11 -> {
                        System.out.println("=== Productos Recientes ===");
                        List<Producto> recientes = inventario.obtenerProductosRecientes(5);
                        if (recientes.isEmpty()) {
                            System.out.println("No hay productos recientes.");
                        } else {
                            recientes.forEach(System.out::println);
                        }
                    }
                    case 12 -> {
                        Combo comboRotado = inventario.obtenerSiguienteComboRotacion();
                        if (comboRotado == null) {
                            System.out.println("No hay combos disponibles para rotación.");
                        } else {
                            System.out.println("=== Combo Promocional del Día ===");
                            System.out.println(comboRotado);
                            System.out.println("¡Precio especial: $" +
                                    String.format("%.2f", comboRotado.getPrecioFinal()) + "!");
                        }
                    }
                    case 0 -> {
                        System.out.println(" Saliendo del sistema...");
                        archivoProductos.guardarProductos(inventario.listarProductos());
                        archivoCombos.guardarCombos(inventario.listarCombos());
                    }
                    default -> System.out.println(" Opción no válida.");
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
            }
        } while (opcion != 0);

        sc.close();
    }
}
