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
