# -_ALGORITMO_DIJKSTRA_- :.
✅ SOLUCIÓN PROPUESTA (ARQUITECTURA COMPLETA):

<img width="1254" height="1254" alt="image" src="https://github.com/user-attachments/assets/f2679c99-ba70-41fa-84d4-f1a262ab088f" />    

<img width="2547" height="1072" alt="image" src="https://github.com/user-attachments/assets/59f42f49-37b6-4b7d-8244-b98e4ad35a38" />    

```

✔ Java (IntelliJ)
✔ Swing (JTable)
✔ JDBC → Oracle 19c
✔ Grafo con nodos (ciudades) y aristas (distancias)
✔ Dijkstra (camino más corto) + opción heurística para camino largo

🧩 1. MODELO DE DATOS – ORACLE 19c
📌 Tabla de Nodos (Ciudades)
CREATE TABLE NODOS (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NOMBRE VARCHAR2(100)
);

📌 Tabla de Aristas (Carreteras)
CREATE TABLE ARISTAS (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ORIGEN NUMBER,
    DESTINO NUMBER,
    DISTANCIA NUMBER,
    FOREIGN KEY (ORIGEN) REFERENCES NODOS(ID),
    FOREIGN KEY (DESTINO) REFERENCES NODOS(ID)
);

🧠 2. MODELO JAVA
📌 Nodo.java
public class Nodo {
    public int id;
    public String nombre;

    public Nodo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}

📌 Arista.java
public class Arista {
    public int origen;
    public int destino;
    public double distancia;

    public Arista(int origen, int destino, double distancia) {
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
    }
}

🗄️ 3. DAO (JDBC - ORACLE)
📌 Conexion.java
import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String user = "system";
        String pass = "oracle";

        return DriverManager.getConnection(url, user, pass);
    }
}

📌 GrafoDAO.java
import java.sql.*;
import java.util.*;

public class GrafoDAO {

    public List<Arista> obtenerAristas() {
        List<Arista> lista = new ArrayList<>();

        try (Connection conn = Conexion.getConnection()) {
            String sql = "SELECT ORIGEN, DESTINO, DISTANCIA FROM ARISTAS";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Arista(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDouble(3)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}

⚙️ 4. ALGORITMO DIJKSTRA
📌 Dijkstra.java
import java.util.*;

public class Dijkstra {

    public static Map<Integer, Double> calcular(List<Arista> aristas, int inicio) {

        Map<Integer, List<Arista>> grafo = new HashMap<>();
        Map<Integer, Double> distancias = new HashMap<>();

        // Construir grafo
        for (Arista a : aristas) {
            grafo.computeIfAbsent(a.origen, k -> new ArrayList<>()).add(a);
            distancias.put(a.origen, Double.MAX_VALUE);
            distancias.put(a.destino, Double.MAX_VALUE);
        }

        distancias.put(inicio, 0.0);

        PriorityQueue<int[]> cola = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        cola.add(new int[]{inicio, 0});

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int nodo = actual[0];

            if (!grafo.containsKey(nodo)) continue;

            for (Arista a : grafo.get(nodo)) {
                double nuevaDist = distancias.get(nodo) + a.distancia;

                if (nuevaDist < distancias.get(a.destino)) {
                    distancias.put(a.destino, nuevaDist);
                    cola.add(new int[]{a.destino, (int) nuevaDist});
                }
            }
        }

        return distancias;
    }
}

🖥️ 5. INTERFAZ GRÁFICA (SWING)
📌 Vista.java
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

🎮 6. CONTROLADOR
📌 Controlador.java
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

🚀 7. MAIN
public class Vista {
    public static void main(String[] args) {
        Vista vista = new Vista();
        GrafoDAO dao = new GrafoDAO();

        new Controlador(vista, dao);

        vista.setVisible(true);
    }
}

🔴 CAMINO MÁS LARGO (ENFOQUE HEURÍSTICO)

⚠️ No garantiza solución óptima en grafos con ciclos

public class CaminoLargo {

    public static void dfs(int nodo, Map<Integer, List<Arista>> grafo,
                           Set<Integer> visitados, double distanciaActual,
                           double[] maxDistancia) {

        visitados.add(nodo);

        maxDistancia[0] = Math.max(maxDistancia[0], distanciaActual);

        if (grafo.containsKey(nodo)) {
            for (Arista a : grafo.get(nodo)) {
                if (!visitados.contains(a.destino)) {
                    dfs(a.destino, grafo, visitados,
                        distanciaActual + a.distancia, maxDistancia);
                }
            }
        }

        visitados.remove(nodo);
    }
}

🧠 CONCLUSIÓN TÉCNICA:

✔ Dijkstra → correcto para redes de carreteras reales
❌ Camino más largo exacto → problema NP-hard en grafos con ciclos
✔ Alternativa → heurísticas, restricciones o uso de grafos DAG :. . / .  
