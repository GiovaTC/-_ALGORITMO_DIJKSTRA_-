import java.awt.event.*;
import java.util.*;

public class Controlador {

    private Vista vista;
    private GrafoDAO dao;

    public Controlador(Vista vista, GrafoDAO dao) {
        this.vista = vista;
        this.dao = dao;

        vista.getBtnCalcular().addActionListener(e -> ejecutar());
    }

    private void ejecutar() {
        List<Arista> aristas = dao.obtenerAristas();

        Map<Integer, Double> resultado = Dijkstra.calcular(aristas, 1);
        vista.mostrarResultados(resultado);
    }
}   
