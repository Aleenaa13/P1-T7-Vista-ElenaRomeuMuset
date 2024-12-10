package p1.t7.vista.romeumusetelena;

import javax.swing.JOptionPane;
import org.esportsapp.persistencia.IPersistencia;

public class IniciarPrograma {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Cal passar el nom de la classe que dona la persistència com a primer argument");
            System.exit(0);
        }
       
        // Crear una instància de la persistència
        IPersistencia persistencia = null; // Exemple de com es podria crear una instància de persistència
        String nomClassePersistencia = args[0];

        // Iniciar la pantalla d'inici de sessió
        try{
            persistencia = (IPersistencia) Class.forName(nomClassePersistencia).newInstance();
            PantallaIniciSessio pantallaInici = new PantallaIniciSessio();
            pantallaInici.mostrarPantallaIniciSessio(persistencia);
            
            
            IPersistencia finalPersistencia = persistencia; // Necesario porque las variables deben ser effectively final
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    //System.out.println("Tancant la connexió amb la base de dades...");
                    finalPersistencia.tancarConnexio(); // Método para cerrar la conexión
                    //System.out.println("Connexió tancada correctament.");
                } catch (Exception e) {
                   // System.err.println("Error en tancar la connexió: " + e.getMessage());
                }
            }));
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            //JOptionPane.showMessageDialog(º"Usuari o contrasenya incorrecte!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
}
