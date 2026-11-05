package patronfactorymethod;

import java.sql.Connection;

/**
 * Producto del patron: contrato comun que cumplen todos los adaptadores
 * que puede fabricar FactoryBaseDatos.
 */
public interface IntAdaptador {

    Connection getConnection();
}
