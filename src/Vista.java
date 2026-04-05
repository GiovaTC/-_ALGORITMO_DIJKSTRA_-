import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class Vista extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnCalcular;

    public Vista() {
        setTitle("Dijkstra - Carreteras España");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"Nodo", "Distancia"}, 0);
        tabla = new JTable(modelo);
        btnCalcular = new JButton("Calcular Ruta");

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnCalcular, BorderLayout.SOUTH);
    }

    public void mostrarResultados(Map<Integer, Double> resultados) {
        modelo.setRowCount(0);
        for (Map.Entry<Integer, Double> e : resultados.entrySet()) {
            modelo.addRow(new Object[]{e.getKey(), e.getValue()});
        }
    }

    public JButton getBtnCalcular() {
        return btnCalcular;
    }
}