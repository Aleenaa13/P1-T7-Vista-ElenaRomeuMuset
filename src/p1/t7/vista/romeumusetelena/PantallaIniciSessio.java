package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Usuari;

public class PantallaIniciSessio {
    private IPersistencia persistencia;
    
    public void mostrarPantallaIniciSessio(IPersistencia persistencia) {
        this.persistencia = persistencia;
        inicialitzarFinestra();
    }
    
    private void inicialitzarFinestra() {
        JFrame finestra = crearFinestra();
        
        JTextField campUsuari = afegirCampUsuari(finestra);
        JPasswordField campContrasenya = afegirCampContrasenya(finestra);
        afegirEtiquetes(finestra);
        afegirBotons(finestra, campUsuari, campContrasenya);
        
        finestra.setVisible(true);
    }
    
    private JFrame crearFinestra() {
        JFrame finestra = new JFrame("Inici de Sessió - Gestió Futbol");
        finestra.setSize(900, 600);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLocationRelativeTo(null);
        finestra.setResizable(false);
        finestra.setLayout(null);
        finestra.getContentPane().setBackground(new Color(245, 245, 245));
        return finestra;
    }
    
    private void afegirEtiquetes(JFrame finestra) {
        JLabel etiquetaUsuari = new JLabel("Usuari:");
        etiquetaUsuari.setFont(new Font("Arial", Font.PLAIN, 14));
        etiquetaUsuari.setBounds(300, 200, 100, 30);
        finestra.add(etiquetaUsuari);
        
        JLabel etiquetaContrasenya = new JLabel("Contrasenya:");
        etiquetaContrasenya.setFont(new Font("Arial", Font.PLAIN, 14));
        etiquetaContrasenya.setBounds(300, 250, 100, 30);
        finestra.add(etiquetaContrasenya);
    }
    
    private JTextField afegirCampUsuari(JFrame finestra) {
        JTextField campUsuari = new JTextField();
        campUsuari.setBounds(400, 200, 200, 30);
        finestra.add(campUsuari);
        return campUsuari;
    }
    
    private JPasswordField afegirCampContrasenya(JFrame finestra) {
        JPasswordField campContrasenya = new JPasswordField();
        campContrasenya.setBounds(400, 250, 200, 30);
        finestra.add(campContrasenya);
        return campContrasenya;
    }
    
    private void afegirBotons(JFrame finestra, JTextField campUsuari, JPasswordField campContrasenya) {
        JButton botoIniciarSessio = crearBotoIniciarSessio();
        botoIniciarSessio.setBounds(375, 300, 150, 30);
        finestra.add(botoIniciarSessio);
        afegirAccioBotoIniciarSessio(botoIniciarSessio, finestra, campUsuari, campContrasenya);
        
        JButton botoOblidatContrasenya = crearBotoContrasenyaOblidada();
        botoOblidatContrasenya.setBounds(350, 350, 200, 30);
        finestra.add(botoOblidatContrasenya);
        afegirAccioBotoContrseanyaOblidada(botoOblidatContrasenya);
    }
    
    private JButton crearBotoIniciarSessio() {
        JButton boto = new JButton("Iniciar Sessió");
        boto.setBackground(new Color(0, 102, 204));
        boto.setForeground(Color.WHITE);
        return boto;
    }
    
    private void afegirAccioBotoIniciarSessio(JButton boto, JFrame finestra, JTextField campUsuari, JPasswordField campContrasenya) {
        boto.addActionListener(e -> {
            String usuari = campUsuari.getText();
            String contrasenya = new String(campContrasenya.getPassword());

            try {
                if (persistencia.validarUsuari(usuari, Usuari.encriptarContrasenya(contrasenya))) {
                    finestra.dispose();
                    new PantallaPrincipal(persistencia);
                } else {
                    JOptionPane.showMessageDialog(finestra, "Usuari o contrasenya incorrecte!");
                }
            } catch (GestorBDEsportsException ex) {
                JOptionPane.showMessageDialog(finestra, 
                        "Error en validar l'usuari: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private JButton crearBotoContrasenyaOblidada() {
        JButton boto = new JButton("Has oblidat la contrasenya?");
        boto.setBackground(new Color(0, 102, 204));
        boto.setForeground(Color.WHITE);
        return boto;
    }
    
    private void afegirAccioBotoContrseanyaOblidada(JButton boto) {
        boto.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Contacte amb l'administrador del club.");
        });
    }
}
