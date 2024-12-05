package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;

public class PantallaIniciSessio {
    public void mostrarPantallaIniciSessio() {
        // Crear el frame principal
        JFrame frame = new JFrame("Inici de Sessió - Gestió Futbol");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centrar el frame a la pantalla
        frame.setResizable(false); // Impedir que la finestra canviï de mida
        frame.setLayout(new GridBagLayout()); // Utilitzem GridBagLayout per centrar els elements
        GridBagConstraints gbc = new GridBagConstraints(); // Per gestionar el disseny de la finestra

        // Fons de la finestra
        frame.getContentPane().setBackground(new java.awt.Color(245, 245, 245)); // Fons blanc trencat

        // Etiquetes
        JLabel lblUsuari = new JLabel("Usuari:");
        lblUsuari.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Separació entre components
        frame.add(lblUsuari, gbc);

        JLabel lblContrasenya = new JLabel("Contrasenya:");
        lblContrasenya.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(lblContrasenya, gbc);

        // Camps de text
        JTextField txtUsuari = new JTextField();
        txtUsuari.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.add(txtUsuari, gbc);

        JPasswordField txtContrasenya = new JPasswordField();
        txtContrasenya.setPreferredSize(new Dimension(200, 25));
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(txtContrasenya, gbc);

        // Botons
        JButton btnIniciarSessio = new JButton("Iniciar Sessió");
        btnIniciarSessio.setBackground(new java.awt.Color(0, 102, 204)); // Blau elèctric
        btnIniciarSessio.setForeground(java.awt.Color.WHITE);
        btnIniciarSessio.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // El botó ocupa dues columnes
        frame.add(btnIniciarSessio, gbc);

        JButton btnOblidatContrasenya = new JButton("Has oblidat la contrasenya?");
        btnOblidatContrasenya.setBackground(new java.awt.Color(0, 102, 204)); // Blau elèctric
        btnOblidatContrasenya.setForeground(java.awt.Color.WHITE);
        btnOblidatContrasenya.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // El botó ocupa dues columnes
        frame.add(btnOblidatContrasenya, gbc);

        // Acció del botó "Iniciar Sessió"
        btnIniciarSessio.addActionListener(e -> {
            String usuari = txtUsuari.getText();
            String contrasenya = new String(txtContrasenya.getPassword());

            if ("elena".equals(usuari) && "1234".equals(contrasenya)) {
                frame.dispose(); // Tanca la pantalla actual
                new PantallaPrincipal(); // Obre la pantalla principal
            } else {
                JOptionPane.showMessageDialog(frame, "Usuari o contrasenya incorrecte!");
            }
        });

        // Acció del botó "Has oblidat la contrasenya?"
        btnOblidatContrasenya.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Contacte amb l'administrador del club.");
        });

        // Mostrar el frame
        frame.setVisible(true);
    }
}
