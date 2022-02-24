package JasperReport;

public class Miembro {
   private String nombre;
   private String apellidos;
   private String descripcion;

    public Miembro() {
    }

    public Miembro( String descripcion) {
        super();
//        this.nombre = nombre;
//        this.apellidos = apellidos;
        this.descripcion = descripcion;
    }

/*    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }*/

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
