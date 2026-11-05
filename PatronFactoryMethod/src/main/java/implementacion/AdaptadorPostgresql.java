package implementacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import patronfactorymethod.IntAdaptador;
import utilidades.UtilidadesAcceso;

/**
 * Producto concreto: conexion a PostgreSQL (contenedor pg-fis).
 */
public class AdaptadorPostgresql implements IntAdaptador {

    private static final String ARCHIVO = "DatosPostgresql.properties";

    // Registra el driver JDBC antes de que se use getConnection
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se encontro el driver de PostgreSQL: " + e);
        }
    }

    @Override
    public Connection getConnection() {
        Properties prop = UtilidadesAcceso.loadProperty(ARCHIVO);
        try {
            Connection con = DriverManager.getConnection(
                    createConnectionString(prop),
                    prop.getProperty("user"),
                    prop.getProperty("password"));
            System.out.println("[AdaptadorPostgresql] Conectado a " + con.getMetaData().getDatabaseProductName()
                    + " " + con.getMetaData().getDatabaseProductVersion());
            return con;
        } catch (SQLException e) {
            throw new IllegalStateException("Error conectando a PostgreSQL: " + e.getMessage(), e);
        }
    }

    public String createConnectionString(Properties prop) {
        return "jdbc:postgresql://" + prop.getProperty("host") + ":" + prop.getProperty("port")
                + "/" + prop.getProperty("dbname");
    }
}
