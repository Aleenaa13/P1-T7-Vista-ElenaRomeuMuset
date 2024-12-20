package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import org.esportsapp.persistencia.IPersistencia;

public class PantallaPrincipal {
    private final IPersistencia persistencia;
    
    // Constants de configuració
    private static final int AMPLADA_FINESTRA = 900;
    private static final int ALTURA_FINESTRA = 600;
    private static final int ALTURA_BOTO_MENU = 40;
    private static final Color COLOR_MENU = new Color(70, 130, 180);
    private static final Color COLOR_BOTONS_CENTRALS = new Color(173, 216, 230);
    private static final Color COLOR_FONS = new Color(245, 245, 245);
    private static final Color COLOR_TEXT = new Color(50, 50, 50);
    private static final Color COLOR_TEXT_MENU = Color.WHITE;

    public PantallaPrincipal(IPersistencia persistencia) {
        this.persistencia = persistencia;
        JFrame finestra = inicialitzarFinestra();
        configurarMenuSuperior(finestra);
        configurarContingutPrincipal(finestra);
        finestra.setLocationRelativeTo(null);
        finestra.setVisible(true);
    }

    private JFrame inicialitzarFinestra() {
        JFrame finestra = new JFrame("Pantalla Principal - Gestió Futbol");
        finestra.setSize(AMPLADA_FINESTRA, ALTURA_FINESTRA);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(null);
        finestra.setResizable(false);
        finestra.getContentPane().setBackground(COLOR_FONS);
        return finestra;
    }

    private void configurarMenuSuperior(JFrame finestra) {
        String[] nomsBotons = {"INICI", "EQUIPS", "JUGADORS", "TEMPORADES", "INFORMES", "TANCAR SESSIÓ"};
        int ampladaBotoMenu = AMPLADA_FINESTRA / nomsBotons.length;

        for (int i = 0; i < nomsBotons.length; i++) {
            JButton boto = new JButton(nomsBotons[i]);
            boto.setBounds(i * ampladaBotoMenu, 0, ampladaBotoMenu, ALTURA_BOTO_MENU);
            boto.setBackground(COLOR_MENU);
            boto.setForeground(COLOR_TEXT_MENU);
            boto.setFocusPainted(false);
            boto.setBorderPainted(false);
            boto.setOpaque(true);

            switch (i) {
                /*case 0: ja hi estas aquí*/
                case 1 -> boto.addActionListener(e -> {
                    finestra.setVisible(false);
                    new GestioEquips(persistencia);
                });
                case 2 -> boto.addActionListener(e -> {
                    finestra.setVisible(false);
                    new GestioJugador(persistencia);
                });
                case 3 -> boto.addActionListener(e -> {
                    finestra.setVisible(false);
                    new GestioTemporades(persistencia);
                });
                case 4 -> boto.addActionListener(e -> {
                    finestra.setVisible(false);
                    new Informes(persistencia);
                });
                case 5 -> boto.addActionListener(e -> TancarSessio.executar(finestra, persistencia));
            }
            finestra.add(boto);
        }
    }

    private void configurarContingutPrincipal(JFrame finestra) {
        // Títol
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 100, AMPLADA_FINESTRA, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 32));
        titol.setForeground(COLOR_MENU);
        finestra.add(titol);

        // Botó Gestió d'Equips
        JButton botoEquips = new JButton("VISUALITZACIÓ D'EQUIPS");
        botoEquips.setBounds(300, 200, 300, 60);
        botoEquips.setBackground(COLOR_BOTONS_CENTRALS);
        botoEquips.setForeground(COLOR_TEXT);
        botoEquips.setFont(new Font("SansSerif", Font.BOLD, 14));
        botoEquips.setFocusPainted(false);
        botoEquips.addActionListener(e -> {
            finestra.setVisible(false);
            new GestioEquips(persistencia);
        });
        finestra.add(botoEquips);

        // Botó Gestió de Jugadors
        JButton botoJugadors = new JButton("VISUALITZACIÓ DE JUGADORS");
        botoJugadors.setBounds(300, 280, 300, 60);
        botoJugadors.setBackground(COLOR_BOTONS_CENTRALS);
        botoJugadors.setForeground(COLOR_TEXT);
        botoJugadors.setFont(new Font("SansSerif", Font.BOLD, 14));
        botoJugadors.setFocusPainted(false);
        botoJugadors.addActionListener(e -> {
            finestra.setVisible(false);
            new GestioJugador(persistencia);
        });
        finestra.add(botoJugadors);
    }
}
