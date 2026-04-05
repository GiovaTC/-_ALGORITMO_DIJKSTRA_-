import java.util.*;
// dijkstra :. . / .
public class Dijkstra {

    public static Map<Integer, Double> calcular(List<Arista> aristas, int inicio) {

        Map<Integer, List<Arista>> grafo = new HashMap<>();
        Map<Integer, Double> distancias = new HashMap<>();

        // construir grafo .
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
            for ( Arista a : grafo.get(nodo)) {
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
