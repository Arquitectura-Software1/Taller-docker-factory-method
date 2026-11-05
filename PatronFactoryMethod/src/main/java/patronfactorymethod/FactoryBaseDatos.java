package patronfactorymethod;

import implementacion.AdaptadorMysql;
import implementacion.AdaptadorPostgresql;
import java.util.Properties;
import utilidades.UtilidadesAcceso;

/**
 * Fabrica (Factory) de adaptadores de conexion. El codigo cliente pide un
 * IntAdaptador y nunca usa new sobre las clases concretas.
 */
public class FactoryBaseDatos {

    private static final String CONFIGURACION = "ConfiguracionFactory.properties";
    private static final String CLASE_DEFECTO = "defaultDBClass";

    private FactoryBaseDatos() {
    }

    /**
     * Devuelve el adaptador del tipo solicitado explicitamente.
     */
    public static IntAdaptador getDBAdapter(TiposBD tipo) {
        switch (tipo) {
            case MYSQL:
                return new AdaptadorMysql();
            case POSTGRESQL:
                return new AdaptadorPostgresql();
            default:
                throw new IllegalArgumentException("Tipo de base de datos no soportado: " + tipo);
        }
    }

    /**
     * Devuelve el adaptador configurado en ConfiguracionFactory.properties.
     * La clase se instancia por reflexion a partir de su nombre.
     */
    public static IntAdaptador getDefaultDBAdapter() {
        Properties prop = UtilidadesAcceso.loadProperty(CONFIGURACION);
        String nombreClase = prop.getProperty(CLASE_DEFECTO);
        try {
            return (IntAdaptador) Class.forName(nombreClase)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (ReflectiveOperationException | ClassCastException e) {
            throw new IllegalStateException("No se pudo crear el adaptador " + nombreClase, e);
        }
    }
}
