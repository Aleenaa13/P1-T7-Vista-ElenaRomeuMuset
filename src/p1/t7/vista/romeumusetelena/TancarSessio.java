package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;

public class TancarSessio {
    public static void executar(JFrame frameActual) {
        // Confirmació per tancar sessió
        int confirmacio = JOptionPane.showConfirmDialog(frameActual, "Estàs segur que vols tancar sessió?", "Confirmació", JOptionPane.YES_NO_OPTION);
        if (confirmacio == JOptionPane.YES_OPTION) {
            frameActual.dispose(); // Tancar la finestra actual
            mostrarPantallaIniciSessio(); // Mostrar la pantalla d'inici de sessió
        }
    }

    private static void mostrarPantallaIniciSessio() {
        // Crear la finestra d'inici de sessió amb l'estètica de PantallaIniciSessio
        JFrame frameIniciSessio = new JFrame("Inici de Sessió - Gestió Futbol");
        frameIniciSessio.setSize(900, 600);
        frameIniciSessio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameIniciSessio.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frameIniciSessio.setLocationRelativeTo(null); // Centrar la finestra
        frameIniciSessio.setResizable(false); // Impedir que la finestra canviï de mida

        // Fons blanc trencat
        frameIniciSessio.getContentPane().setBackground(new java.awt.Color(245, 245, 245));

        // Etiquetes
        JLabel lblUsuari = new JLabel("Usuari:");
        lblUsuari.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Separació entre components
        frameIniciSessio.add(lblUsuari, gbc);

        JLabel lblContrasenya = new JLabel("Contrasenya:");
        lblContrasenya.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        frameIniciSessio.add(lblContrasenya, gbc);

        // Camps de text
        JTextField txtUsuari = new JTextField();
        txtUsuari.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        frameIniciSessio.add(txtUsuari, gbc);

        JPasswordField txtContrasenya = new JPasswordField();
        txtContrasenya.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        frameIniciSessio.add(txtContrasenya, gbc);

        // Botons
        JButton btnIniciarSessio = new JButton("Iniciar Sessió");
        btnIniciarSessio.setBackground(new java.awt.Color(0, 102, 204)); // Blau elèctric
        btnIniciarSessio.setForeground(java.awt.Color.WHITE);
        btnIniciarSessio.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // El botó ocupa dues columnes
        frameIniciSessio.add(btnIniciarSessio, gbc);

        JButton btnOblidatContrasenya = new JButton("Has oblidat la contrasenya?");
        btnOblidatContrasenya.setBackground(new java.awt.Color(0, 102, 204)); // Blau elèctric
        btnOblidatContrasenya.setForeground(java.awt.Color.WHITE);
        btnOblidatContrasenya.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // El botó ocupa dues columnes
        frameIniciSessio.add(btnOblidatContrasenya, gbc);

        // Acció del botó "Iniciar Sessió"
        btnIniciarSessio.addActionListener(e -> {
            String usuari = txtUsuari.getText();
            String contrasenya = new String(txtContrasenya.getPassword());

            if ("elena".equals(usuari) && "1234".equals(contrasenya)) {
                frameIniciSessio.dispose(); // Tanca la pantalla actual
                new PantallaPrincipal(); // Obre la pantalla principal
            } else {
                JOptionPane.showMessageDialog(frameIniciSessio, "Usuari o contrasenya incorrecte!");
            }
        });

        // Acció del botó "Has oblidat la contrasenya?"
        btnOblidatContrasenya.addActionListener(e -> {
            JOptionPane.showMessageDialog(frameIniciSessio, "Contacte amb l'administrador del club.");
        });

        // Mostrar el frame
        frameIniciSessio.setVisible(true);
    }
}
