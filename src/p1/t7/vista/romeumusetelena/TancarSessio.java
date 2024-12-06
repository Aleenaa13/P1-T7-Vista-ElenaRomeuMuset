package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import org.esportsapp.persistencia.IPersistencia;

public class TancarSessio {
    public static void executar(JFrame frameActual, IPersistencia persistencia) {
        // Confirmació per tancar sessió
        int confirmacio = JOptionPane.showConfirmDialog(frameActual, "Estàs segur que vols tancar sessió?", "Confirmació", JOptionPane.YES_NO_OPTION);
        if (confirmacio == JOptionPane.YES_OPTION) {
            frameActual.dispose(); // Tancar la finestra actual
            mostrarPantallaIniciSessio(persistencia); // Mostrar la pantalla d'inici de sessió
        }
    }

    private static void mostrarPantallaIniciSessio(IPersistencia persistencia) {
        // Crear la finestra d'inici de sessió amb l'estètica de PantallaIniciSessio
        PantallaIniciSessio pantallaInici = new PantallaIniciSessio(); // Passar la persistència
        pantallaInici.mostrarPantallaIniciSessio(persistencia);
    }
}
