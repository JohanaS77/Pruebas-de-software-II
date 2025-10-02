package sistemagestioncombos;

import sistemagestioncombos.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
public class VentanaPrincipal extends JFrame{
    private Inventario inventario;
    private ArchivoProducto archivoProductos;
    private ArchivoCombo archivoCombos;
    private GestorCombos gestor;

    public VentanaPrincipal(Inventario inventario) {
        this.inventario = inventario;
        this.archivoProductos = new ArchivoProducto("productos.txt");
        this.archivoCombos = new ArchivoCombo("combos.txt");
        this.gestor = new GestorCombos(inventario);

        setTitle("Sistema de Gestión de Combos");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configurar el fondo principal con un rosado claro
        getContentPane().setBackground(new Color(255, 228, 225)); // Rosado claro

        // Panel principal con grid layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(255, 228, 225)); // Mismo fondo rosado

        // Crear botones con colores pastel
        Color[] coloresPastel = {
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

        JButton btnInventario = crearBotonPastel("Ver Inventario de Productos", coloresPastel[0]);
        JButton btnCombos = crearBotonPastel("Ver Lista de Combos", coloresPastel[1]);
        JButton btnAgregarProducto = crearBotonPastel("Agregar Producto", coloresPastel[2]);
        JButton btnModificarProducto = crearBotonPastel("Modificar Producto", coloresPastel[3]);
        JButton btnEliminarProducto = crearBotonPastel("Eliminar Producto", coloresPastel[4]);
        JButton btnCrearCombo = crearBotonPastel("Crear Combo", coloresPastel[5]);
        JButton btnModificarCombo = crearBotonPastel("Modificar Combo", coloresPastel[6]);
        JButton btnEliminarCombo = crearBotonPastel("Eliminar Combo", coloresPastel[7]);
        JButton btnValorInventario = crearBotonPastel("Calcular Valor Total", coloresPastel[8]);
        JButton btnProductosRecientes = crearBotonPastel("Ver Productos Recientes", coloresPastel[9]);
        JButton btnComboDelDia = crearBotonPastel("Combo del Día", new Color(255, 215, 0)); // Dorado
        JButton btnSalir = crearBotonPastel("Salir", new Color(255, 160, 160));

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
        boton.setForeground(new Color(70, 70, 70)); // Texto gris oscuro
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBorder(BorderFactory.createRaisedBevelBorder());
        boton.setPreferredSize(new Dimension(200, 40));
        boton.setFocusPainted(false);

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = color;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(originalColor);
            }
        });

        return boton;
    }

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
                double precio = Double.parseDouble(precioField.getText());
                String tipo = tipoField.getText().trim();
                String temporada = temporadaField.getText().trim();
                int dias = Integer.parseInt(diasField.getText());
                double cantidad = Double.parseDouble(cantidadField.getText());

                Producto producto = new Producto(nombre, precio, tipo, temporada, dias, cantidad);
                inventario.agregarProducto(producto);
                archivoProductos.guardarProductos(inventario.listarProductos());
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente!");
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

            switch (opcion) {
                case "Precio" -> producto.setPrecio(Double.parseDouble(nuevoValor));
                case "Tipo" -> producto.setTipo(nuevoValor);
                case "Temporada" -> producto.setTemporada(nuevoValor);
                case "Dias para vencer" -> producto.setDiasParaVencer(Integer.parseInt(nuevoValor));
                case "Cantidad" -> producto.setCantidadKg(Double.parseDouble(nuevoValor));
            }
            archivoProductos.guardarProductos(inventario.listarProductos());
            JOptionPane.showMessageDialog(this, "Producto modificado exitosamente!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
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
                double descuento = Double.parseDouble(descuentoField.getText().trim());
                int unidades = Integer.parseInt(unidadesField.getText().trim());

                // Crear el combo
                gestor.crearCombo(nombre, temporada, descuento, unidades);

                // Agregar productos al combo
                String[] productosIngresados = productosField.getText().split(",");
                for (String prod : productosIngresados) {
                    inventario.buscarProductoPorNombre(prod.trim())
                            .ifPresent(p -> {
                                // Crear copia con 1kg
                                Producto copia = new Producto(
                                        p.getNombre(),
                                        p.getPrecio(),
                                        p.getTipo(),
                                        p.getTemporada(),
                                        p.getDiasParaVencer(),
                                        1.0   // 🔹 siempre 1kg
                                );
                                gestor.agregarProductoACombo(nombre, copia);
                            });

                }

                // Guardar en archivo
                archivoCombos.guardarCombos(inventario.listarCombos());
                JOptionPane.showMessageDialog(this, "Combo creado exitosamente!");

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

            switch (opcion) {
                case "Temporada" -> combo.setTemporada(nuevoValor);
                case "Descuento (%)" -> combo.setDescuento(Double.parseDouble(nuevoValor));
                case "Unidades" -> combo.setUnidades(Integer.parseInt(nuevoValor));
            }
            archivoCombos.guardarCombos(inventario.listarCombos());
            JOptionPane.showMessageDialog(this, "Combo modificado exitosamente!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
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
            // Cargar datos si existen
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
