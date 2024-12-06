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
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            //JOptionPane.showMessageDialog(º"Usuari o contrasenya incorrecte!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
}
