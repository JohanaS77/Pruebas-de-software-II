package sistemagestioncombos;

import sistemagestioncombos.Combo;
import sistemagestioncombos.Inventario;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class VentanaCombos extends JFrame{
    private Inventario inventario;
    private JTable tablaCombos;
    private DefaultTableModel modeloTabla;

    public VentanaCombos(Inventario inventario) {
        this.inventario = inventario;
        setTitle("Lista de Combos");
        setSize(1200, 500);
        setLocationRelativeTo(null); // centrar ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Configuración de la tabla (sin emojis)
        String[] columnas = {"Nombre", "Temporada", "Productos incluidos", "Precio Original ($)", "Descuento (%)", "Precio Final ($)", "Unidades"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaCombos = new JTable(modeloTabla);

        // Personalizar el header de la tabla - Color fuerte pero no tanto
        JTableHeader header = tablaCombos.getTableHeader();
        header.setBackground(new Color(205, 92, 92)); // Rojo indio - fuerte pero no demasiado
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
                    c.setBackground(new Color(255, 99, 71)); // Tomate cuando seleccionado
                    c.setForeground(Color.WHITE);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(255, 235, 235)); // Rosa muy suave - tono 1
                    } else {
                        c.setBackground(new Color(255, 228, 225)); // Rosa claro suave - tono 2
                    }
                    c.setForeground(new Color(139, 0, 0)); // Rojo oscuro para texto
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };

        // Aplicar el renderer a todas las columnas
        for (int i = 0; i < tablaCombos.getColumnCount(); i++) {
            tablaCombos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Configurar altura de las filas
        tablaCombos.setRowHeight(30);
        tablaCombos.setFont(new Font("Arial", Font.PLAIN, 12));

        // Scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tablaCombos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout());
        panelInferior.setBackground(new Color(255, 192, 203)); // Rosa claro

        //JLabel infoLabel = new JLabel("Lista de Combos Disponibles");
        //infoLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
        //infoLabel.setForeground(new Color(139, 0, 0));
        //panelInferior.add(infoLabel);

        // Botón para refrescar datos
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(205, 92, 92));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefrescar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnRefrescar.setPreferredSize(new Dimension(120, 35));
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.addActionListener(e -> cargarCombosEnTabla());
        panelInferior.add(btnRefrescar);

        add(panelInferior, BorderLayout.SOUTH);

        // Configurar el fondo de la ventana
        getContentPane().setBackground(new Color(255, 240, 245)); // Rosa muy claro (Lavender Blush)

        // Cargar datos al inicio
        cargarCombosEnTabla();
    }

    private void cargarCombosEnTabla() {
        modeloTabla.setRowCount(0); // limpiar tabla

        inventario.listarCombos().stream()
                // Ordenar combos por nombre (alfabéticamente)
                .sorted((c1, c2) -> c1.getNombre().compareToIgnoreCase(c2.getNombre()))
                .forEach(c -> {
                    String productos = c.getProductos().stream()
                            .map(p -> p.getNombre())
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("Sin productos");
                    Object[] fila = {
                            c.getNombre(),
                            c.getTemporada(),
                            productos,
                            String.format("%.2f", c.getPrecioOriginal()),
                            c.getDescuento() + "%",
                            String.format("%.2f", c.getPrecioFinal()),
                            c.getUnidades()
                    };
                    modeloTabla.addRow(fila);
                });
    }


    // Método main de prueba sin combos precargados
    public static void main(String[] args) {
        Inventario inventario = new Inventario();
        SwingUtilities.invokeLater(() -> {
            new VentanaCombos(inventario).setVisible(true);
        });
    }
}
