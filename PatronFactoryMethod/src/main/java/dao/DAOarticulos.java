package dao;

import entidades.Articulos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import patronfactorymethod.FactoryBaseDatos;
import patronfactorymethod.IntAdaptador;
import patronfactorymethod.TiposBD;

/**
 * Acceso a datos de la tabla articulos. Trabaja igual con cualquier motor,
 * porque solo conoce la interfaz IntAdaptador.
 */
public class DAOarticulos {

    private final IntAdaptador adaptador;

    /** Usa el adaptador por defecto definido en ConfiguracionFactory.properties. */
    public DAOarticulos() {
        this.adaptador = FactoryBaseDatos.getDefaultDBAdapter();
    }

    /** Usa el adaptador del tipo indicado. */
    public DAOarticulos(TiposBD tipo) {
        this.adaptador = FactoryBaseDatos.getDBAdapter(tipo);
    }

    public List<Articulos> findAllProducts() {
        String sql = "SELECT idarticulo, nombrearticulo, valor FROM articulos ORDER BY idarticulo";
        List<Articulos> lista = new ArrayList<>();
        try (Connection con = adaptador.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Articulos(
                        rs.getLong("idarticulo"),
                        rs.getString("nombrearticulo"),
                        rs.getDouble("valor")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error consultando articulos: " + e.getMessage(), e);
        }
        return lista;
    }

    public boolean saveProduct(Articulos articulo) {
        String sql = "INSERT INTO articulos (nombrearticulo, valor) VALUES (?, ?)";
        try (Connection con = adaptador.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, articulo.getNombreArticulo());
            ps.setDouble(2, articulo.getValor());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new IllegalStateException("Error guardando articulo: " + e.getMessage(), e);
        }
    }
}
