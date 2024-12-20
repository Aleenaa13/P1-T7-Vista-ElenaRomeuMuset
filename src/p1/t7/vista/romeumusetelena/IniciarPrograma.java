package p1.t7.vista.romeumusetelena;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.esportsapp.persistencia.IPersistencia;

public class IniciarPrograma {
    public static void main(String[] args) {
        iniciarTema();
        validarArguments(args);
        iniciarAplicacio(args[0]);
    }
    
    private static void iniciarTema() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.out.println("Error al carregar el tema");
        }
    }
    
    private static void validarArguments(String[] args) {
        if (args.length == 0) {
            System.out.println("Cal passar el nom de la classe que dona la persistència com a primer argument");
            System.exit(0);
        }
    }
    
    private static void iniciarAplicacio(String nomClassePersistencia) {
        try {
            IPersistencia persistencia = crearInstanciaPersistencia(nomClassePersistencia);
            iniciarPantallaIniciSessio(persistencia);
            configurarTancamentAplicacio(persistencia);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    // Crea una instància dinàmica de la classe de persistència utilitzant reflection
    private static IPersistencia crearInstanciaPersistencia(String nomClassePersistencia) throws Exception {
        return (IPersistencia) Class.forName(nomClassePersistencia).newInstance();
    }
    
    private static void iniciarPantallaIniciSessio(IPersistencia persistencia) {
        PantallaIniciSessio pantallaInici = new PantallaIniciSessio();
        pantallaInici.mostrarPantallaIniciSessio(persistencia);
    }
    
    // Configura un hook per tancar la connexió amb la base de dades quan es tanca l'aplicació
    private static void configurarTancamentAplicacio(IPersistencia persistencia) {
        IPersistencia persistenciaFinal = persistencia;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                persistenciaFinal.tancarConnexio();
            } catch (Exception e) {
                System.err.println("Error en tancar la connexió: " + e.getMessage());
            }
        }));
    }
}
