package utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Carga archivos .properties desde la carpeta propiedades del classpath.
 */
public class UtilidadesAcceso {

    private UtilidadesAcceso() {
    }

    public static Properties loadProperty(String nombreArchivo) {
        String ruta = "/propiedades/" + nombreArchivo;
        try (InputStream in = UtilidadesAcceso.class.getResourceAsStream(ruta)) {
            if (in == null) {
                throw new IllegalStateException("No se encontro el archivo " + ruta);
            }
            Properties prop = new Properties();
            prop.load(in);
            return prop;
        } catch (IOException e) {
            throw new IllegalStateException("Error leyendo " + ruta, e);
        }
    }
}
