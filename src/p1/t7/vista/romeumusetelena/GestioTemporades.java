package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Temporada;
import org.esportsapp.persistencia.IPersistencia;

public class GestioTemporades {
    private IPersistencia persistencia; // Per accedir a la interfície de persistència
    private DefaultTableModel modelTaulaTemporades;
    private JTextField txtAny; // Definida com a camp de classe

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
        scrollPane.setBounds(50, 150, 300, 300);
        frame.add(scrollPane);

        // Botons
        JButton btnAfegir = new JButton("Afegir");
        btnAfegir.setBounds(600, 300, 100, 30);
        btnAfegir.setBackground(new Color(70, 130, 180));
        frame.add(btnAfegir);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(50, 470, 100, 30);
        btnEliminar.setBackground(new Color(173, 216, 230));
        frame.add(btnEliminar);

        JLabel lblAny = new JLabel("Any:");
        lblAny.setBounds(540, 240, 40, 30);
        frame.add(lblAny);

        txtAny = new JTextField(); // Inicialitzar el camp com a atribut
        txtAny.setBounds(600, 240, 100, 30);
        frame.add(txtAny);

        // Carregar dades de la base de dades
        carregarTemporades();

        JPanel recuadre = new JPanel();
        recuadre.setBounds(500, 200, 300, 200);
        recuadre.setBackground(new Color(173, 216, 230));
        recuadre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Bordes del recuadre en negre
        frame.add(recuadre);

        // Mostrar el frame
        frame.setVisible(true);

        // Assignació dels botons
        btnAfegir.addActionListener(e -> botoAfegirTemporada(frame));
        btnEliminar.addActionListener(e -> botoEliminarTemporada(frame));
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

    private void botoAfegirTemporada(JFrame frame) {
        String anyText = txtAny.getText();
        if (anyText != null && !anyText.isEmpty()) {
            try {
                int any = Integer.parseInt(anyText);
                Temporada novaTemporada = new Temporada(any);

                boolean afegida = persistencia.afegirTemporada(novaTemporada);
                
                if (afegida) {
                    JOptionPane.showMessageDialog(frame, "Temporada afegida correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                    persistencia.confirmarCanvis();
                    carregarTemporades();
                } else {
                    JOptionPane.showMessageDialog(frame, "La temporada ja existeix.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                txtAny.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "El camp de l'any no pot estar buit.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Funció per eliminar una temporada
    private void botoEliminarTemporada(JFrame frame) {
        int filaSeleccionada = modelTaulaTemporades.getRowCount() > 0 ? modelTaulaTemporades.getRowCount() - 1 : -1;
        filaSeleccionada = filaSeleccionada >= 0 ? filaSeleccionada : 0;

        if (filaSeleccionada != -1) {
            // Obtenir l'any seleccionat a la taula
            int any = (int) modelTaulaTemporades.getValueAt(filaSeleccionada, 0);

            // Confirmació de l'eliminació
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Estàs segur de voler eliminar la temporada " + any + "?",
                    "Confirmar Eliminació",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Intentar eliminar la temporada
                    boolean eliminada = persistencia.eliminarTemporada(any);
                    
                    if (eliminada) {
                        JOptionPane.showMessageDialog(frame, "Temporada eliminada correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                        persistencia.confirmarCanvis();
                        carregarTemporades(); // Actualitzar la llista
                    } else {
                        JOptionPane.showMessageDialog(frame, "No es pot eliminar aquesta temporada.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (GestorBDEsportsException ex) {
                    JOptionPane.showMessageDialog(frame, "Error en eliminar la temporada: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "S'ha produït un error inesperat: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Selecciona una temporada per eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

}
