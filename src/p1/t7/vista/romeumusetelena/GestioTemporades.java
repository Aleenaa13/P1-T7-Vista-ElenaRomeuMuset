package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Temporada;

public class GestioTemporades {
    private IPersistencia persistencia; // Per accedir a la interfície de persistència
    private DefaultTableModel modelTaulaTemporades;

    public GestioTemporades(IPersistencia persistencia) {
        this.persistencia = persistencia;

        // Crear el frame
        JFrame frame = new JFrame("Gestió de Temporades");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Fons blanc trencat
        Color blancTrencat = new Color(245, 245, 245);
        frame.getContentPane().setBackground(blancTrencat);

        // Menú superior
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

        // Vincular accions als botons del menú
        botonsMenu[5].addActionListener(e -> {
            frame.dispose();
            new TancarSessio();
        });
                
        botonsMenu[0].addActionListener(e -> {
            frame.dispose();
            new PantallaPrincipal(persistencia);
        });
        botonsMenu[1].addActionListener(e -> {
            frame.dispose();
            new GestioEquips(persistencia);
        });
        botonsMenu[2].addActionListener(e -> {
            frame.dispose();
            new GestioJugadors(persistencia);
        });

        // Títol centrat
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        // Configurar la taula
        modelTaulaTemporades = new DefaultTableModel(new String[]{"Any"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Cap cel·la és editable
            }
        };

        JTable taulaTemporades = new JTable(modelTaulaTemporades);
        taulaTemporades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taulaTemporades);
        scrollPane.setBounds(300, 150, 300, 300);
        frame.add(scrollPane);

        // Botons
        JButton btnAfegir = new JButton("Afegir");
        btnAfegir.setBounds(100, 470, 100, 30);
        btnAfegir.setBackground(new Color(173, 216, 230));
        frame.add(btnAfegir);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(250, 470, 100, 30);
        btnEliminar.setBackground(new Color(173, 216, 230));
        frame.add(btnEliminar);

        JLabel lblBuscaAny = new JLabel("Busca per any:");
        lblBuscaAny.setBounds(400, 470, 100, 30);
        frame.add(lblBuscaAny);

        JTextField txtBuscaAny = new JTextField();
        txtBuscaAny.setBounds(500, 470, 100, 30);
        frame.add(txtBuscaAny);

        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBounds(620, 470, 100, 30);
        btnBuscar.setBackground(new Color(173, 216, 230));
        frame.add(btnBuscar);

        // Carregar dades de la base de dades
        carregarTemporades();

        // Funció de cerca
        btnBuscar.addActionListener(e -> {
            String anyText = txtBuscaAny.getText();
            try {
                int any = Integer.parseInt(anyText);
                carregarTemporades(any);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Introdueix un any vàlid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Mostrar el frame
        frame.setVisible(true);
    }

    private void carregarTemporades() {
        try {
            List<Temporada> temporades = persistencia.obtenirTotesTemporades();
            modelTaulaTemporades.setRowCount(0);
            for (Temporada temporada : temporades) {
                modelTaulaTemporades.addRow(new Object[]{temporada.getAny()});
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en obtenir les temporades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarTemporades(int any) {
        try {
            List<Temporada> temporades = persistencia.obtenirTotesTemporades();
            modelTaulaTemporades.setRowCount(0);
            for (Temporada temporada : temporades) {
                if (temporada.getAny() == any) {
                    modelTaulaTemporades.addRow(new Object[]{temporada.getAny()});
                }
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en obtenir les temporades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
