package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioTemporades {
    public GestioTemporades() {
        // Crear el frame
        JFrame frame = new JFrame("Gestió de Temporades");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false); // Finestra no redimensionable
        frame.setLocationRelativeTo(null); // Centrar la finestra

        // Fons blanc trencat
        Color blancTrencat = new Color(245, 245, 245);
        frame.getContentPane().setBackground(blancTrencat);

        // Menú superior (fix)
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

        // Afegir acció als botons del menú
        botonsMenu[5].addActionListener(e -> TancarSessio.executar(frame));
        botonsMenu[0].addActionListener(e -> {
            frame.dispose();
            new PantallaPrincipal();
        });
        botonsMenu[1].addActionListener(e -> {
            frame.dispose();
            new GestioEquips();
        });
        botonsMenu[2].addActionListener(e -> {
            frame.dispose();
            new GestioJugadors();
        });
        botonsMenu[3].addActionListener(e -> {
            frame.dispose();
            new GestioTemporades();
        });
        botonsMenu[4].addActionListener(e -> {
            frame.dispose();
            //new InformeEquips();
        });

        // Títol centrat
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        // Taula per mostrar les temporades
        String[] columnes = {"Any"};
        String[][] dades = {
            {"2024"},
            {"2023"},
            {"2022"}
        };
        JTable taulaTemporades = new JTable(dades, columnes);
        JScrollPane scrollPane = new JScrollPane(taulaTemporades);
        scrollPane.setBounds(300, 150, 300, 300); // Ajustem la taula amb amplada de 300 i centrada
        frame.add(scrollPane);

        // Cerca per any
        JLabel lblBuscaAny = new JLabel("Busca per any:");
        lblBuscaAny.setBounds(350, 495, 150, 30);
        frame.add(lblBuscaAny);

        JTextField txtBuscaAny = new JTextField();
        txtBuscaAny.setBounds(450, 495, 200, 30);
        frame.add(txtBuscaAny);

        // Afegir, eliminar
        JButton btnAfegir = new JButton("Afegir");
        btnAfegir.setBounds(40, 495, 100, 30);
        btnAfegir.setBackground(new Color(173, 216, 230)); // Blau cel
        btnAfegir.setFocusPainted(false);
        frame.add(btnAfegir);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(170, 495, 100, 30);
        btnEliminar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnEliminar.setFocusPainted(false);
        frame.add(btnEliminar);

        // Botó cerca (lupa)
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBounds(680, 495, 100, 30); // A la dreta dels altres botons
        btnBuscar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnBuscar.setFocusPainted(false);
        frame.add(btnBuscar);

        // Funcionalitat de cerca per any
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String any = txtBuscaAny.getText();
                // Filtrar les dades per any (equals)
                for (int i = 0; i < taulaTemporades.getRowCount(); i++) {
                    String value = taulaTemporades.getValueAt(i, 0).toString();
                    if (value.equals(any)) {
                        taulaTemporades.setRowSelectionAllowed(true);
                        taulaTemporades.setRowSelectionInterval(i, i);
                    } else {
                        taulaTemporades.setRowSelectionAllowed(false);
                    }
                }
            }
        });

        // Mostrar el frame
        frame.setVisible(true);
    }
}
