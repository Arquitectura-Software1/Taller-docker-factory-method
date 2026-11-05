package patronfactorymethod;

import dao.DAOarticulos;
import entidades.Articulos;
import java.util.List;

/**
 * Codigo cliente. Usa DAOarticulos sin saber a que motor se conecta.
 */
public class PatronFactoryMethod {

    public static void main(String[] args) {

        // 1. Adaptador por defecto (definido en ConfiguracionFactory.properties)
        System.out.println("=== 1. Adaptador por defecto (ConfiguracionFactory.properties) ===");
        DAOarticulos daoDefecto = new DAOarticulos();
        daoDefecto.saveProduct(new Articulos(null, "Teclado mecanico", 185000.0));
        imprimir(daoDefecto.findAllProducts());

        // 2. Mismo codigo cliente contra cada motor, pidiendo el tipo explicitamente
        for (TiposBD tipo : TiposBD.values()) {
            System.out.println();
            System.out.println("=== 2. Adaptador explicito: " + tipo + " ===");
            DAOarticulos dao = new DAOarticulos(tipo);
            dao.saveProduct(new Articulos(null, "Mouse inalambrico " + tipo, 79900.0));
            imprimir(dao.findAllProducts());
        }
    }

    private static void imprimir(List<Articulos> articulos) {
        System.out.println("Articulos encontrados: " + articulos.size());
        for (Articulos a : articulos) {
            System.out.println("  " + a);
        }
    }
}
