package sistemagestioncombos;

import sistemagestioncombos.Inventario;
import sistemagestioncombos.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
public class VentanaInventario extends JFrame{
    private Inventario inventario;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public VentanaInventario(Inventario inventario) {
        this.inventario = inventario;
        setTitle("Inventario de Productos");
        setSize(1200, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Configuración de la tabla con encabezados mejorados (sin emojis)
        String[] columnas = {"Nombre", "Precio ($)", "Tipo", "Temporada", "Dias para vencer", "Cantidad (Kg)", "Valor total"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaProductos = new JTable(modeloTabla);

        // Personalizar el header de la tabla - Color fuerte pero no tanto
        JTableHeader header = tablaProductos.getTableHeader();
        header.setBackground(new Color(70, 130, 180)); // Azul acero - fuerte pero no demasiado
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        // Personalizar las celdas de la tabla con tonos intercalados
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(new Color(100, 149, 237)); // Azul claro cuando seleccionado
                    c.setForeground(Color.WHITE);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(230, 245, 255)); // Azul muy suave - tono 1
                    } else {
                        c.setBackground(new Color(220, 237, 255)); // Azul suave - tono 2
                    }
                    c.setForeground(new Color(25, 25, 112)); // Azul marino para texto
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        // Aplicar el renderer a todas las columnas
        for (int i = 0; i < tablaProductos.getColumnCount(); i++) {
            tablaProductos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Configurar altura de las filas
        tablaProductos.setRowHeight(30);
        tablaProductos.setFont(new Font("Arial", Font.PLAIN, 12));

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con información adicional
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.setBackground(new Color(176, 196, 222)); // Azul gris claro

        //JLabel infoLabel = new JLabel("Inventario de Productos");
        //infoLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
        //infoLabel.setForeground(new Color(25, 25, 112));
        //panelInferior.add(infoLabel);

        // Botón para refrescar datos
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(70, 130, 180));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefrescar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnRefrescar.setPreferredSize(new Dimension(120, 35));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.addActionListener(e -> cargarProductosEnTabla());
        panelInferior.add(btnRefrescar);

        add(panelInferior, BorderLayout.SOUTH);

        // Configurar el fondo de la ventana
        getContentPane().setBackground(new Color(240, 248, 255)); // Azul muy claro (Alice Blue)

        // Cargar datos al inicio
        cargarProductosEnTabla();
    }

    private void cargarProductosEnTabla() {
        modeloTabla.setRowCount(0); // limpiar tabla

        inventario.listarProductos().stream()
                // Ordenar primero por tipo (frutas antes que verduras), luego por nombre
                .sorted((p1, p2) -> {
                    int tipoComp = p1.getTipo().compareToIgnoreCase(p2.getTipo());
                    if (tipoComp != 0) {
                        if (p1.getTipo().equalsIgnoreCase("fruta") && p2.getTipo().equalsIgnoreCase("verdura")) {
                            return -1; // fruta primero
                        }
                        if (p1.getTipo().equalsIgnoreCase("verdura") && p2.getTipo().equalsIgnoreCase("fruta")) {
                            return 1; // verdura después
                        }
                        return tipoComp; // si son otros tipos, comparar normal
                    }
                    return p1.getNombre().compareToIgnoreCase(p2.getNombre()); // ordenar por nombre
                })
                .forEach(p -> {
                    Object[] fila = {
                            p.getNombre(),
                            String.format("%.2f", p.getPrecio()),
                            p.getTipo(),
                            p.getTemporada(),
                            p.getDiasParaVencer(),
                            String.format("%.2f", p.getCantidadKg()),
                            String.format("%.2f", p.calcularValorTotal())
                    };
                    modeloTabla.addRow(fila);
                });
    }


    // Método main sin productos de ejemplo
    public static void main(String[] args) {
        Inventario inventario = new Inventario();
        SwingUtilities.invokeLater(() -> {
            new VentanaInventario(inventario).setVisible(true);
        });
    }
}
