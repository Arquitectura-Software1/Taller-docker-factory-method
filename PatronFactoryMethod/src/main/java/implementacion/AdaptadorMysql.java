package implementacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import patronfactorymethod.IntAdaptador;
import utilidades.UtilidadesAcceso;

/**
 * Producto concreto: conexion a MySQL (contenedor mysql-fis).
 */
public class AdaptadorMysql implements IntAdaptador {

    private static final String ARCHIVO = "DatosMysql.properties";

    // Registra el driver JDBC antes de que se use getConnection
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se encontro el driver de MySQL: " + e);
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
            System.out.println("[AdaptadorMysql] Conectado a " + con.getMetaData().getDatabaseProductName()
                    + " " + con.getMetaData().getDatabaseProductVersion());
            return con;
        } catch (SQLException e) {
            throw new IllegalStateException("Error conectando a MySQL: " + e.getMessage(), e);
        }
    }

    public String createConnectionString(Properties prop) {
        return "jdbc:mysql://" + prop.getProperty("host") + ":" + prop.getProperty("port")
                + "/" + prop.getProperty("dbname")
                + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    }
}
