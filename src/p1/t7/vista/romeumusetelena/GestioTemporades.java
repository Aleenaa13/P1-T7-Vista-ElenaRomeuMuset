package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Temporada;
import org.esportsapp.persistencia.IPersistencia;

public class GestioTemporades {
    // Constants de configuració
    private static final int AMPLADA_FINESTRA = 900;
    private static final int ALTURA_FINESTRA = 600;
    private static final int ALTURA_BOTO_MENU = 40;
    private static final int MARGE_LATERAL = 100;
    private static final int ALTURA_TITOL = 40;
    
    // Components de la interfície
    private IPersistencia persistencia;
    private DefaultTableModel modelTaulaTemporades;
    private JTextField txtAny;
    private List<Temporada> temporades;
    private JTable taulaTemporades;
    private JFrame finestra;

    public GestioTemporades(IPersistencia persistencia) {
        this.persistencia = persistencia;
        inicialitzarFinestra();
        configurarMenuSuperior();
        afegirTitol();
        configurarPanellPrincipal();
        carregarTemporades();
        finestra.setVisible(true);
    }

    private void inicialitzarFinestra() {
        finestra = new JFrame("Gestió de Temporades");
        finestra.setSize(AMPLADA_FINESTRA, ALTURA_FINESTRA);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(null);
        finestra.setResizable(false);
        finestra.setLocationRelativeTo(null);
        
        Color blancTrencat = new Color(245, 245, 245);
        finestra.getContentPane().setBackground(blancTrencat);
    }
    
    private void afegirTitol() {
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 60, AMPLADA_FINESTRA, ALTURA_TITOL);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        finestra.add(titol);
    }

    private void configurarMenuSuperior() {
        int numBotons = 6;
        int ampladaBoto = AMPLADA_FINESTRA / numBotons;
        String[] nomsBotons = {"INICI", "EQUIPS", "JUGADORS", "TEMPORADES", "INFORMES", "TANCAR SESSIÓ"};
        JButton[] botonsMenu = new JButton[numBotons];

        for (int i = 0; i < nomsBotons.length; i++) {
            JButton boto = new JButton(nomsBotons[i]);
            boto.setBounds(i * ampladaBoto, 0, ampladaBoto, ALTURA_BOTO_MENU);
            boto.setBackground(new Color(70, 130, 180));
            boto.setForeground(Color.WHITE);
            boto.setFocusPainted(false);
            boto.setBorderPainted(false);
            boto.setOpaque(true);
            finestra.add(boto);
            botonsMenu[i] = boto;
        }

        botonsMenu[0].addActionListener(e -> {
            finestra.dispose();
            new PantallaPrincipal(persistencia);
        });
        botonsMenu[1].addActionListener(e -> {
            finestra.dispose();
            new GestioEquips(persistencia);
        });
        botonsMenu[2].addActionListener(e -> {
            finestra.dispose();
            new GestioJugador(persistencia);
        });
        
        //botonsMenu[3].setEnabled(false);
        
        botonsMenu[4].addActionListener(e -> {
            finestra.dispose();
            new Informes(persistencia);
        });
        botonsMenu[5].addActionListener(e -> TancarSessio.executar(finestra, persistencia));
    }

    private void configurarPanellPrincipal() {
        JPanel panellPrincipal = crearPanellPrincipal();
        inicialitzarTaula(panellPrincipal);
        configurarFormulari(panellPrincipal);
        finestra.add(panellPrincipal);
    }

    private JPanel crearPanellPrincipal() {
        JPanel panell = new JPanel(null);
        panell.setBounds(MARGE_LATERAL, 120, 700, 350);
        panell.setBackground(Color.WHITE);
        panell.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return panell;
    }

    private void inicialitzarTaula(JPanel panellPrincipal) {
        modelTaulaTemporades = new DefaultTableModel(new String[]{"TEMPORADES"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taulaTemporades = new JTable(modelTaulaTemporades);
        taulaTemporades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(taulaTemporades);
        scrollPane.setBounds(20, 20, 250, 250);
        panellPrincipal.add(scrollPane);

        JButton btnEliminar = crearBotoEliminar();
        panellPrincipal.add(btnEliminar);
    }

    private void configurarFormulari(JPanel panellPrincipal) {
        JPanel panellFormulari = new JPanel(null);
        panellFormulari.setBounds(290, 20, 390, 310);
        panellFormulari.setBackground(Color.WHITE);

        afegirTitolFormulari(panellFormulari);
        afegirCampAny(panellFormulari);
        afegirBotoAfegir(panellFormulari);

        panellPrincipal.add(panellFormulari);
    }

    private void afegirTitolFormulari(JPanel panell) {
        JLabel lblTitolAfegir = new JLabel("AFEGIR TEMPORADA", SwingConstants.CENTER);
        lblTitolAfegir.setBounds(0, 20, 390, 30);
        lblTitolAfegir.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitolAfegir.setForeground(new Color(70, 130, 180));
        panell.add(lblTitolAfegir);
    }

    private void afegirCampAny(JPanel panell) {
        txtAny = new JTextField();
        txtAny.setBounds(95, 80, 200, 30);
        panell.add(txtAny);
    }

    private void afegirBotoAfegir(JPanel panell) {
        JButton btnAfegir = new JButton("Afegir");
        btnAfegir.setBounds(145, 130, 100, 30);
        btnAfegir.setBackground(new Color(135, 206, 250));
        btnAfegir.setFocusPainted(false);
        btnAfegir.addActionListener(e -> botoAfegirTemporada());
        panell.add(btnAfegir);
    }

    private JButton crearBotoEliminar() {
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(20, 280, 100, 30);
        btnEliminar.setBackground(new Color(135, 206, 250));
        btnEliminar.setFocusPainted(false);
        btnEliminar.addActionListener(e -> botoEliminarTemporada());
        return btnEliminar;
    }

    private void carregarTemporades() {
        try {
            temporades = persistencia.obtenirTotesTemporades();
            modelTaulaTemporades.setRowCount(0);
            for (Temporada temporada : temporades) {
                modelTaulaTemporades.addRow(new Integer[]{temporada.getAny()});
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en obtenir les temporades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void botoAfegirTemporada() {
        String anyText = txtAny.getText();
        if (anyText != null && !anyText.equals("")) {
            try {
                int any = Integer.parseInt(anyText);
                Temporada novaTemporada = new Temporada(any);

                boolean afegida = persistencia.afegirTemporada(novaTemporada);
                
                if (afegida) {
                    JOptionPane.showMessageDialog(finestra, "Temporada afegida correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                    persistencia.confirmarCanvis();
                    carregarTemporades();
                } else {
                    JOptionPane.showMessageDialog(finestra, "La temporada ja existeix.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (HeadlessException | NumberFormatException | GestorBDEsportsException ex) {
                JOptionPane.showMessageDialog(finestra, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                txtAny.setText(""); //tornar a posar el text buit
            }
        } else {
            JOptionPane.showMessageDialog(finestra, "El camp de l'any no pot estar buit.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void botoEliminarTemporada() {
        int filaSeleccionada = taulaTemporades.getSelectedRow();

        if (filaSeleccionada != -1) {
            int any = (int) modelTaulaTemporades.getValueAt(filaSeleccionada, 0);

            int confirmar = JOptionPane.showConfirmDialog(finestra,"Estàs segur de voler eliminar la temporada " + any + "?",
                    "Confirmar Eliminació",JOptionPane.YES_NO_OPTION);

            if (confirmar == JOptionPane.YES_OPTION) {
                try {
                    boolean eliminada = persistencia.eliminarTemporada(any);
                    
                    if (eliminada) {
                        JOptionPane.showMessageDialog(finestra, "Temporada eliminada correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                        persistencia.confirmarCanvis();
                        carregarTemporades();
                    } else {
                        JOptionPane.showMessageDialog(finestra, "No es pot eliminar aquesta temporada.", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (GestorBDEsportsException ex) {
                    JOptionPane.showMessageDialog(finestra, "Error en eliminar la temporada: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (HeadlessException ex) {
                    JOptionPane.showMessageDialog(finestra, "S'ha produït un error inesperat: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(finestra, "Selecciona una temporada per eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

}
