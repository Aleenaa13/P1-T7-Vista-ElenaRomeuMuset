package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GestioEquips {
    public GestioEquips() {
        // Crear el frame
        JFrame frame = new JFrame("Gestió d'Equips");
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

        // Taula per mostrar els equips
        String[] columnes = {"Categoria", "Nom de l'Equip", "Nombre de Jugadors", "Temporada"};
        String[][] dades = {
            {"Benjamí", "Femení A", "15", "2024"},
            {"Benjamí", "Masculí A", "13", "2024"},
            {"Benjamí", "Mixta A", "11", "2024"}
        };
        JTable taulaEquips = new JTable(dades, columnes);
        JScrollPane scrollPane = new JScrollPane(taulaEquips);
        scrollPane.setBounds(50, 150, 800, 300);
        frame.add(scrollPane);

        // Busca per nom
        JLabel lblBuscaNom = new JLabel("Busca per nom:");
        lblBuscaNom.setBounds(50, 470, 150, 30);
        frame.add(lblBuscaNom);

        JTextField txtBuscaNom = new JTextField();
        txtBuscaNom.setBounds(150, 470, 200, 30);
        frame.add(txtBuscaNom);

        // Botó cerca (lupa)
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBounds(360, 470, 100, 30);
        btnBuscar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnBuscar.setFocusPainted(false);
        frame.add(btnBuscar);

        // Filtres ComboBox
        JLabel lblCategoria = new JLabel("Filtra per categoria:");
        lblCategoria.setBounds(550, 470, 150, 30);
        frame.add(lblCategoria);

        JComboBox<String> cmbCategoria = new JComboBox<>(new String[]{"Totes", "Benjamí", "Aleví", "Infantil"});
        cmbCategoria.setBounds(700, 470, 150, 30);
        frame.add(cmbCategoria);

        JLabel lblTemporada = new JLabel("Filtra per temporada:");
        lblTemporada.setBounds(550, 510, 150, 30);
        frame.add(lblTemporada);

        JComboBox<String> cmbTemporada = new JComboBox<>(new String[]{"2024", "2023", "2022", "2021"});
        cmbTemporada.setBounds(700, 510, 150, 30);
        frame.add(cmbTemporada);

        // Afegir, editar, eliminar
        JButton btnAfegir = new JButton("Afegir");
        btnAfegir.setBounds(100, 520, 100, 30);
        btnAfegir.setBackground(new Color(173, 216, 230)); // Blau cel
        btnAfegir.setFocusPainted(false);
        frame.add(btnAfegir);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(250, 520, 100, 30);
        btnEditar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnEditar.setFocusPainted(false);
        frame.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(400, 520, 100, 30);
        btnEliminar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnEliminar.setFocusPainted(false);
        frame.add(btnEliminar);

        // Funcionalitat de cerca per nom
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = txtBuscaNom.getText().toLowerCase();
                // Filtrar les dades per nom (contains)
                for (int i = 0; i < taulaEquips.getRowCount(); i++) {
                    boolean matches = false;
                    for (int j = 0; j < taulaEquips.getColumnCount(); j++) {
                        String value = taulaEquips.getValueAt(i, j).toString().toLowerCase();
                        if (value.contains(nom)) {
                            matches = true;
                            break;
                        }
                    }
                    // Mostrar només les files que coincideixen
                    if (matches) {
                        taulaEquips.setRowSelectionAllowed(true);
                        taulaEquips.setRowSelectionInterval(i, i);
                    } else {
                        taulaEquips.setRowSelectionAllowed(false);
                    }
                }
            }
        });

        // Mostrar el frame
        frame.setVisible(true);
    }
}
