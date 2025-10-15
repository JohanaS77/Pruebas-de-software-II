package sistemagestioncombos;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentanaPrincipal extends JFrame {

    // CONSTANTES DE CONFIGURACIÓN
    private static final String ARCHIVO_PRODUCTOS = "productos.txt";
    private static final String ARCHIVO_COMBOS = "combos.txt";
    private static final String TITULO_VENTANA = "Sistema de Gestión de Combos";
    private static final int VENTANA_ANCHO = 600;
    private static final int VENTANA_ALTO = 500;
    private static final int GRID_FILAS = 7;
    private static final int GRID_COLUMNAS = 2;
    private static final int GRID_GAP_H = 15;
    private static final int GRID_GAP_V = 15;
    private static final int BORDE_PADDING = 30;

    // CONSTANTES DE COLORES
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(255, 228, 225); // Rosado claro
    private static final Color COLOR_TEXTO_BOTON = new Color(70, 70, 70); // Gris oscuro
    private static final Color[] COLORES_BOTONES_PASTEL = {
            new Color(255, 182, 193), // Rosa claro
            new Color(173, 216, 230), // Azul claro
            new Color(144, 238, 144), // Verde claro
            new Color(255, 218, 185), // Durazno
            new Color(221, 160, 221), // Ciruela claro
            new Color(255, 255, 224), // Amarillo claro
            new Color(176, 224, 230), // Azul polvo
            new Color(255, 192, 203), // Rosa
            new Color(240, 230, 140), // Caqui claro
            new Color(230, 230, 250)  // Lavanda
    };
    private static final Color COLOR_BOTON_COMBO_DIA = new Color(255, 215, 0); // Dorado
    private static final Color COLOR_BOTON_SALIR = new Color(255, 160, 160); // Rojo claro

    private Inventario inventario;
    private ArchivoProducto archivoProductos;
    private ArchivoCombo archivoCombos;
    private GestorCombos gestor;

    public VentanaPrincipal(Inventario inventario) {
        this.inventario = inventario;
        this.archivoProductos = new ArchivoProducto(ARCHIVO_PRODUCTOS);
        this.archivoCombos = new ArchivoCombo(ARCHIVO_COMBOS);
        this.gestor = new GestorCombos(inventario);

        setTitle(TITULO_VENTANA);
        setSize(VENTANA_ANCHO, VENTANA_ALTO);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(COLOR_FONDO_PRINCIPAL);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(GRID_FILAS, GRID_COLUMNAS, GRID_GAP_H, GRID_GAP_V));
        panel.setBorder(BorderFactory.createEmptyBorder(BORDE_PADDING, BORDE_PADDING,
                BORDE_PADDING, BORDE_PADDING));
        panel.setBackground(COLOR_FONDO_PRINCIPAL);

        // Crear botones con colores pastel
        JButton btnInventario = crearBotonPastel("Ver Inventario de Productos", COLORES_BOTONES_PASTEL[0]);
        JButton btnCombos = crearBotonPastel("Ver Lista de Combos", COLORES_BOTONES_PASTEL[1]);
        JButton btnAgregarProducto = crearBotonPastel("Agregar Producto", COLORES_BOTONES_PASTEL[2]);
        JButton btnModificarProducto = crearBotonPastel("Modificar Producto", COLORES_BOTONES_PASTEL[3]);
        JButton btnEliminarProducto = crearBotonPastel("Eliminar Producto", COLORES_BOTONES_PASTEL[4]);
        JButton btnCrearCombo = crearBotonPastel("Crear Combo", COLORES_BOTONES_PASTEL[5]);
        JButton btnModificarCombo = crearBotonPastel("Modificar Combo", COLORES_BOTONES_PASTEL[6]);
        JButton btnEliminarCombo = crearBotonPastel("Eliminar Combo", COLORES_BOTONES_PASTEL[7]);
        JButton btnValorInventario = crearBotonPastel("Calcular Valor Total", COLORES_BOTONES_PASTEL[8]);
        JButton btnProductosRecientes = crearBotonPastel("Ver Productos Recientes", COLORES_BOTONES_PASTEL[9]);
        JButton btnComboDelDia = crearBotonPastel("Combo del Día", COLOR_BOTON_COMBO_DIA);
        JButton btnSalir = crearBotonPastel("Salir", COLOR_BOTON_SALIR);

        // Configurar eventos
        btnInventario.addActionListener(e -> new VentanaInventario(inventario).setVisible(true));
        btnCombos.addActionListener(e -> new VentanaCombos(inventario).setVisible(true));
        btnAgregarProducto.addActionListener(e -> mostrarDialogoAgregarProducto());
        btnModificarProducto.addActionListener(e -> mostrarDialogoModificarProducto());
        btnEliminarProducto.addActionListener(e -> mostrarDialogoEliminarProducto());
        btnCrearCombo.addActionListener(e -> mostrarDialogoCrearCombo());
        btnModificarCombo.addActionListener(e -> mostrarDialogoModificarCombo());
        btnEliminarCombo.addActionListener(e -> mostrarDialogoEliminarCombo());
        btnValorInventario.addActionListener(e -> {
            double valor = inventario.calcularValorTotalInventario();
            JOptionPane.showMessageDialog(this,
                    String.format("Valor total del inventario: $%.2f", valor),
                    "Valor Total", JOptionPane.INFORMATION_MESSAGE);
        });
        btnProductosRecientes.addActionListener(e -> mostrarProductosRecientes());
        btnComboDelDia.addActionListener(e -> mostrarComboDelDia());
        btnSalir.addActionListener(e -> {
            try {
                archivoProductos.guardarProductos(inventario.listarProductos());
                archivoCombos.guardarCombos(inventario.listarCombos());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        });

        // Agregar botones al panel
        panel.add(btnInventario);
        panel.add(btnCombos);
        panel.add(btnAgregarProducto);
        panel.add(btnModificarProducto);
        panel.add(btnEliminarProducto);
        panel.add(btnCrearCombo);
        panel.add(btnModificarCombo);
        panel.add(btnEliminarCombo);
        panel.add(btnValorInventario);
        panel.add(btnProductosRecientes);
        panel.add(btnComboDelDia);
        panel.add(btnSalir);

        add(panel, BorderLayout.CENTER);
    }

    private JButton crearBotonPastel(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(COLOR_TEXTO_BOTON);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        boton.setPreferredSize(new Dimension(200, 40));
        boton.setFocusPainted(false);

        final Color colorOriginal = color;
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorOriginal.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorOriginal);
            }
        });
        return boton;
    }

    // Aquí van TODOS tus métodos privados (mostrarDialogo...)
    // Los copio del documento que me compartiste:

    private void mostrarDialogoAgregarProducto() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBackground(Color.WHITE);

        JTextField nombreField = new JTextField();
        JTextField precioField = new JTextField();
        JTextField tipoField = new JTextField();
        JTextField temporadaField = new JTextField();
        JTextField diasField = new JTextField();
        JTextField cantidadField = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Precio:"));
        panel.add(precioField);
        panel.add(new JLabel("Tipo:"));
        panel.add(tipoField);
        panel.add(new JLabel("Temporada:"));
        panel.add(temporadaField);
        panel.add(new JLabel("Dias para vencer:"));
        panel.add(diasField);
        panel.add(new JLabel("Cantidad:"));
        panel.add(cantidadField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = nombreField.getText().trim();
                String precioTexto = precioField.getText().trim();
                String tipo = tipoField.getText().trim();
                String temporada = temporadaField.getText().trim();
                String diasTexto = diasField.getText().trim();
                String cantidadTexto = cantidadField.getText().trim();

                if (nombre.isEmpty() || precioTexto.isEmpty() || tipo.isEmpty() ||
                        temporada.isEmpty() || diasTexto.isEmpty() || cantidadTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Todos los campos son obligatorios. Por favor complete la información.",
                            "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double precio = Double.parseDouble(precioTexto);
                int dias = Integer.parseInt(diasTexto);
                double cantidad = Double.parseDouble(cantidadTexto);

                Producto producto = new Producto(nombre, precio, tipo, temporada, dias, cantidad);
                inventario.agregarProducto(producto);
                archivoProductos.guardarProductos(inventario.listarProductos());
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: Precio, Días y Cantidad deben ser valores numéricos válidos.",
                        "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoModificarProducto() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del producto a modificar:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        Optional<Producto> productoOpt = inventario.buscarProductoPorNombre(nombre);
        if (productoOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró el producto.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto producto = productoOpt.get();
        String[] opciones = {"Precio", "Tipo", "Temporada", "Dias para vencer", "Cantidad"};
        String opcion = (String) JOptionPane.showInputDialog(this,
                "Producto actual: " + producto + "\n\n¿Qué desea modificar?",
                "Modificar Producto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (opcion == null) return;

        try {
            String nuevoValor = JOptionPane.showInputDialog(this, "Nuevo " + opcion.toLowerCase() + ":");
            if (nuevoValor == null) return;

            nuevoValor = nuevoValor.trim();

            if (nuevoValor.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El valor no puede estar vacío.",
                        "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            switch (opcion) {
                case "Precio" -> {
                    double precio = Double.parseDouble(nuevoValor);
                    producto.setPrecio(precio);
                }
                case "Tipo" -> producto.setTipo(nuevoValor);
                case "Temporada" -> producto.setTemporada(nuevoValor);
                case "Dias para vencer" -> {
                    int dias = Integer.parseInt(nuevoValor);
                    producto.setDiasParaVencer(dias);
                }
                case "Cantidad" -> {
                    double cantidad = Double.parseDouble(nuevoValor);
                    producto.setCantidadKg(cantidad);
                }
            }

            archivoProductos.guardarProductos(inventario.listarProductos());
            JOptionPane.showMessageDialog(this, "Producto modificado exitosamente!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: El valor ingresado no es un número válido.\n" +
                            "Por favor ingrese un valor numérico correcto.",
                    "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error de validación: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoEliminarProducto() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del producto a eliminar:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el producto '" + nombre + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                if (inventario.eliminarProducto(nombre)) {
                    archivoProductos.guardarProductos(inventario.listarProductos());
                    JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el producto.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoCrearCombo() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBackground(Color.WHITE);

        JTextField nombreField = new JTextField();
        JTextField temporadaField = new JTextField();
        JTextField descuentoField = new JTextField();
        JTextField unidadesField = new JTextField();
        JTextField productosField = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Temporada:"));
        panel.add(temporadaField);
        panel.add(new JLabel("Descuento (%):"));
        panel.add(descuentoField);
        panel.add(new JLabel("Unidades:"));
        panel.add(unidadesField);
        panel.add(new JLabel("Productos (separados por coma):"));
        panel.add(productosField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Crear Combo", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nombre = nombreField.getText().trim();
                String temporada = temporadaField.getText().trim();
                String descuentoTexto = descuentoField.getText().trim();
                String unidadesTexto = unidadesField.getText().trim();
                String productosTexto = productosField.getText().trim();

                if (nombre.isEmpty() || temporada.isEmpty() || descuentoTexto.isEmpty() ||
                        unidadesTexto.isEmpty() || productosTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Todos los campos son obligatorios. Por favor complete la información.",
                            "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                double descuento = Double.parseDouble(descuentoTexto);
                int unidades = Integer.parseInt(unidadesTexto);

                gestor.crearCombo(nombre, temporada, descuento, unidades);

                String[] productosIngresados = productosTexto.split(",");
                List<String> productosNoEncontrados = new ArrayList<>();

                for (String prod : productosIngresados) {
                    String nombreProd = prod.trim();

                    if (nombreProd.isEmpty()) {
                        continue;
                    }

                    Optional<Producto> productoOpt = inventario.buscarProductoPorNombre(nombreProd);

                    if (productoOpt.isPresent()) {
                        Producto p = productoOpt.get();
                        Producto copia = new Producto(
                                p.getNombre(),
                                p.getPrecio(),
                                p.getTipo(),
                                p.getTemporada(),
                                p.getDiasParaVencer(),
                                1.0
                        );
                        gestor.agregarProductoACombo(nombre, copia);
                    } else {
                        productosNoEncontrados.add(nombreProd);
                    }
                }

                archivoCombos.guardarCombos(inventario.listarCombos());

                if (productosNoEncontrados.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Combo creado exitosamente!");
                } else {
                    String mensaje = "Combo creado, pero no se encontraron los siguientes productos:\n" +
                            String.join(", ", productosNoEncontrados);
                    JOptionPane.showMessageDialog(this, mensaje,
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error: Descuento y Unidades deben ser valores numéricos válidos.",
                        "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarDialogoModificarCombo() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del combo a modificar:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        Optional<Combo> comboOpt = inventario.buscarComboPorNombre(nombre);
        if (comboOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró el combo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Combo combo = comboOpt.get();
        String[] opciones = {"Temporada", "Descuento (%)", "Unidades"};
        String opcion = (String) JOptionPane.showInputDialog(this,
                "Combo actual: " + combo + "\n\n¿Qué desea modificar?",
                "Modificar Combo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (opcion == null) return;

        try {
            String nuevoValor = JOptionPane.showInputDialog(this, "Nuevo/a " + opcion.toLowerCase() + ":");
            if (nuevoValor == null) return;

            nuevoValor = nuevoValor.trim();

            if (nuevoValor.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El valor no puede estar vacío.",
                        "Campo Vacío", JOptionPane.WARNING_MESSAGE);
                return;
            }

            switch (opcion) {
                case "Temporada" -> combo.setTemporada(nuevoValor);
                case "Descuento (%)" -> {
                    double descuento = Double.parseDouble(nuevoValor);
                    combo.setDescuento(descuento);
                }
                case "Unidades" -> {
                    int unidades = Integer.parseInt(nuevoValor);
                    combo.setUnidades(unidades);
                }
            }

            archivoCombos.guardarCombos(inventario.listarCombos());
            JOptionPane.showMessageDialog(this, "Combo modificado exitosamente!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: El valor ingresado no es un número válido.\n" +
                            "Por favor ingrese un valor numérico correcto.",
                    "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Error de validación: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoEliminarCombo() {
        String nombre = JOptionPane.showInputDialog(this, "Nombre del combo a eliminar:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el combo '" + nombre + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                if (inventario.eliminarCombo(nombre)) {
                    archivoCombos.guardarCombos(inventario.listarCombos());
                    JOptionPane.showMessageDialog(this, "Combo eliminado exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el combo.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarProductosRecientes() {
        List<Producto> recientes = inventario.obtenerProductosRecientes(5);

        if (recientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos recientes.",
                    "Productos Recientes",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder mensaje = new StringBuilder("Últimos productos agregados:\n\n");
        for (int i = 0; i < recientes.size(); i++) {
            Producto p = recientes.get(i);
            mensaje.append((i + 1)).append(". ").append(p.getNombre())
                    .append(" - $").append(String.format("%.2f", p.getPrecio()))
                    .append(" (").append(p.getTipo()).append(")\n");
        }

        JOptionPane.showMessageDialog(this,
                mensaje.toString(),
                "Productos Recientes",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarComboDelDia() {
        Combo comboDelDia = inventario.obtenerSiguienteComboRotacion();

        if (comboDelDia == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay combos disponibles para promoción.",
                    "Combo del Día",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("🌟 COMBO PROMOCIONAL DEL DÍA 🌟\n\n");
        mensaje.append("Nombre: ").append(comboDelDia.getNombre()).append("\n");
        mensaje.append("Temporada: ").append(comboDelDia.getTemporada()).append("\n");
        mensaje.append("Precio original: $").append(String.format("%.2f", comboDelDia.getPrecioOriginal())).append("\n");
        mensaje.append("Descuento: ").append(comboDelDia.getDescuento()).append("%\n");
        mensaje.append("PRECIO FINAL: $").append(String.format("%.2f", comboDelDia.getPrecioFinal())).append("\n");
        mensaje.append("Unidades disponibles: ").append(comboDelDia.getUnidades()).append("\n\n");
        mensaje.append("Productos incluidos:\n");

        comboDelDia.getProductos().forEach(p ->
                mensaje.append("• ").append(p.getNombre()).append("\n"));

        JOptionPane.showMessageDialog(this,
                mensaje.toString(),
                "Combo del Día",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Inventario inventario = new Inventario();
            try {
                ArchivoProducto archivoProductos = new ArchivoProducto("productos.txt");
                ArchivoCombo archivoCombos = new ArchivoCombo("combos.txt");

                List<Producto> productos = archivoProductos.cargarProductos();
                for (Producto p : productos) {
                    inventario.agregarProducto(p);
                }

                List<Combo> combos = archivoCombos.cargarCombos(inventario);
                for (Combo c : combos) {
                    inventario.agregarCombo(c);
                }
            } catch (IOException e) {
                System.out.println("No se pudieron cargar los datos: " + e.getMessage());
            }

            new VentanaPrincipal(inventario).setVisible(true);
        });
    }
}