package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import org.esportsapp.persistencia.IPersistencia;

public class PantallaPrincipal {
    private final IPersistencia persistencia; // Interfície de persistència

    public PantallaPrincipal(IPersistencia persistencia) {
        this.persistencia = persistencia; // Inicialitzar la interfície de persistència

        // Crear el frame principal amb mida fixa
        JFrame frame = new JFrame("Pantalla Principal - Gestió Futbol");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false); // Finestra no redimensionable

        // Fons blanc trencat
        Color blancTrencat = new Color(245, 245, 245);
        frame.getContentPane().setBackground(blancTrencat);

        // Configuració comuna dels botons del menú superior
        int numBotons = 6;
        int ampladaBoto = 900 / numBotons;
        int alturaBoto = 40;

        String[] nomsBotons = {"Inici", "Gestió d'Equips", "Gestió de Jugadors", "Gestió de Temporades", "Informe d'Equips", "Tancar Sessió"};
        JButton[] botonsMenu = new JButton[numBotons];

        for (int i = 0; i < nomsBotons.length; i++) {
            botonsMenu[i] = new JButton(nomsBotons[i]);
            botonsMenu[i].setBounds(i * ampladaBoto, 0, ampladaBoto, alturaBoto);
            botonsMenu[i].setBackground(new Color(70, 130, 180));
            botonsMenu[i].setForeground(Color.WHITE);
            botonsMenu[i].setFocusPainted(false);
            botonsMenu[i].setBorderPainted(false);
            botonsMenu[i].setOpaque(true);
            frame.add(botonsMenu[i]);
        }

        // Afegir acció al botó "Tancar Sessió"
        botonsMenu[5].addActionListener(e -> TancarSessio.executar(frame, persistencia));

        // Afegir accions als altres botons
        botonsMenu[0].addActionListener(e -> mostrarPantallaInici(frame));
        botonsMenu[1].addActionListener(e -> mostrarGestioEquips(frame));
        botonsMenu[2].addActionListener(e -> mostrarGestioJugadors(frame));
        botonsMenu[3].addActionListener(e -> mostrarGestioTemporades(frame));
        botonsMenu[4].addActionListener(e -> mostrarInformeEquips(frame));

        // Títol centrat
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        // Botons centrals
        JButton btnVisualitzacioEquips = new JButton("Visualització d'Equips");
        btnVisualitzacioEquips.setBounds(300, 150, 300, 50);
        btnVisualitzacioEquips.setBackground(new Color(173, 216, 230));
        btnVisualitzacioEquips.setFocusPainted(false);
        btnVisualitzacioEquips.addActionListener(e -> mostrarGestioEquips(frame));
        frame.add(btnVisualitzacioEquips);

        JButton btnGestioJugadorsCentral = new JButton("Gestió de Jugadors");
        btnGestioJugadorsCentral.setBounds(300, 230, 300, 50);
        btnGestioJugadorsCentral.setBackground(new Color(173, 216, 230));
        btnGestioJugadorsCentral.setFocusPainted(false);
        btnGestioJugadorsCentral.addActionListener(e -> mostrarGestioJugadors(frame));
        frame.add(btnGestioJugadorsCentral);

        // Mostrar el frame
        frame.setLocationRelativeTo(null); // Centrar la finestra
        frame.setVisible(true);
    }

    private void mostrarPantallaInici(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "Pantalla d'Inici");
    }

    private void mostrarGestioEquips(JFrame frame) {
        frame.setVisible(false);
        new GestioEquips(persistencia); // Passa la persistència a la nova pantalla
    }

    private void mostrarGestioJugadors(JFrame frame) {
        frame.setVisible(false);
        new GestioJugadors(persistencia); // Passa la persistència a la nova pantalla
    }

    private void mostrarGestioTemporades(JFrame frame) {
        frame.setVisible(false);
        new GestioTemporades(persistencia); // Passa la persistència a la nova pantalla
    }

    private void mostrarInformeEquips(JFrame frame) {
        frame.setVisible(false);
        // Implementar si hi ha una classe InformeEquips
    }
}
