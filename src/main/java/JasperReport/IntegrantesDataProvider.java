package JasperReport;

import java.util.ArrayList;
import java.util.List;

public class IntegrantesDataProvider {

    public static List<Miembro> getMiembro(){
        List<Miembro> miembros = new ArrayList<>();
        for (int i=1; i<=100; i++){
            Miembro mi = new Miembro();
//            mi.setNombre("Nombres-" +i);
//            mi.setApellidos("Apellidos-" +i);
            mi.setDescripcion("Descripción-" +i);
            miembros.add(mi);
        }
        return miembros;
    }
    
    public static List<Miembro> getIntegrantes(){
        List<Miembro> miembros = new ArrayList<>();
//        miembros.add(new Miembro("Guillermo","Rodríguez Pérez"));
//        miembros.add(new Miembro("Daniel","Alejandro Plasencia Orán"));
//        miembros.add(new Miembro("Joel","Jesús Rosales García"));
        miembros.add(new Miembro("Es un juego de plataformas con un jugador y con zombies lo que el jugador tiene que hacer  es ir saltando, avanzando y matando a cualquier enemigo que se te interponga en su camino o ir esquivandolo y seguir el camino correcto que le llevará hacia el siguiente nivel. \n"
        		+ "\nEl videojuego ha sido desarrollado en JavaFX con la librería FXGL, es una librería que se centra principalmente en el desarrollo de juegos básicos para JavaFX."));
		return miembros;
	}

}
