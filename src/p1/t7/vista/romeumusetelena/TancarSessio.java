package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import org.esportsapp.persistencia.IPersistencia;

public class TancarSessio {
    public static void executar(JFrame frameActual, IPersistencia persistencia) {
        int confirmacio = JOptionPane.showConfirmDialog(frameActual, "Estàs segur que vols tancar sessió?", "Confirmació", JOptionPane.YES_NO_OPTION);
        if (confirmacio == JOptionPane.YES_OPTION) {
            frameActual.dispose();
            mostrarPantallaIniciSessio(persistencia); 
        }
    }

    private static void mostrarPantallaIniciSessio(IPersistencia persistencia) {
        PantallaIniciSessio pantallaInici = new PantallaIniciSessio(); 
        pantallaInici.mostrarPantallaIniciSessio(persistencia);
    }
}
