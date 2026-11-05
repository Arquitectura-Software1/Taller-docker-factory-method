package entidades;

/**
 * Entidad que representa un registro de la tabla articulos.
 */
public class Articulos {

    private Long idArticulo;
    private String nombreArticulo;
    private Double valor;

    public Articulos() {
    }

    public Articulos(Long idArticulo, String nombreArticulo, Double valor) {
        this.idArticulo = idArticulo;
        this.nombreArticulo = nombreArticulo;
        this.valor = valor;
    }

    public Long getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Long idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return String.format("Articulo{id=%d, nombre='%s', valor=%.2f}",
                idArticulo, nombreArticulo, valor);
    }
}
