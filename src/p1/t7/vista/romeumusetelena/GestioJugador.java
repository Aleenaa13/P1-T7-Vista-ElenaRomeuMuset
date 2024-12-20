package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Jugador;

public class GestioJugador {
    private IPersistencia persistencia; 
    private DefaultTableModel modelTaulaJugadors;
    private  JTable taulaJugadors;
    private List<Jugador> jugadors; // Llista que mira des de la base de dades 
    private List<Jugador> jugadorsFiltrats; // Llista per actualitzar les dades
    private JTextField txtCercaNom;
    private JTextField txtCercaNIF;
    private JTextField txtCercaDataNaix;
    private JComboBox<String> comboCategoria;
    private JCheckBox chkOrdenarCognom;
    private JFrame finestra;
    
    // Constants de configuració
    private static final int AMPLADA_FINESTRA = 900;
    private static final int ALTURA_FINESTRA = 600;
    private static final int ALTURA_BOTO_MENU = 40;
    private static final int MARGE_LATERAL = 50;
    private static final int ALTURA_TITOL = 40;

    // Constants de taula
    private static final int POSICIO_I_TAULA = 150;
    private static final int AMPLADA_TAULA = 800;
    private static final int ALTURA_TAULA = 300;

    // Constants de botons
    private static final int POSICIO_I_BOTONS = 490;
    private static final int AMPLADA_BOTO = 100;
    private static final int ALTURA_BOTO = 30;

    public GestioJugador(IPersistencia persistencia) {
        this.persistencia = persistencia;
        inicialitzarFinestra();
        configurarMenuSuperior();
        afegirTitol();
        inicialitzarTaula();
        inicialitzarBotons();
        configurarFiltres();
        finestra.setVisible(true);
    }

    private void inicialitzarFinestra() {
        finestra = new JFrame("Gestió de Jugadors");
        finestra.setSize(AMPLADA_FINESTRA, ALTURA_FINESTRA);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(null);
        finestra.setResizable(false);
        finestra.setLocationRelativeTo(null);
        
        Color blancTrencat = new Color(245, 245, 245);
        finestra.getContentPane().setBackground(blancTrencat);
    }

    private void configurarMenuSuperior() {
        int numBotons = 6;
        int ampladaBoto = AMPLADA_FINESTRA / numBotons;
        String[] nomsBotons = {"INICI", "EQUIPS", "JUGADORS", "TEMPORADES", "INFORMES", "TANCAR SESSIÓ"};
        JButton[] botonsMenu = new JButton[numBotons];

        for (int i = 0; i < nomsBotons.length; i++) {
            botonsMenu[i] = new JButton(nomsBotons[i]);
            botonsMenu[i].setBounds(i * ampladaBoto, 0, ampladaBoto, ALTURA_BOTO_MENU);
            botonsMenu[i].setBackground(new Color(70, 130, 180));
            botonsMenu[i].setForeground(Color.WHITE);
            botonsMenu[i].setFocusPainted(false);
            botonsMenu[i].setBorderPainted(false);
            botonsMenu[i].setOpaque(true);
            finestra.add(botonsMenu[i]);
        }

        botonsMenu[0].addActionListener(e -> {
            finestra.dispose();
            new PantallaPrincipal(persistencia);
        });
        botonsMenu[1].addActionListener(e -> {
            finestra.dispose();
            new GestioEquips(persistencia);
        });
        
        //botonsMenu[2].setEnabled(false);
        
        botonsMenu[3].addActionListener(e -> {
            finestra.dispose();
            new GestioTemporades(persistencia);
        });
        botonsMenu[4].addActionListener(e -> {
            finestra.dispose();
            new Informes(persistencia);
        });
        botonsMenu[5].addActionListener(e -> TancarSessio.executar(finestra, persistencia));
    }

    private void afegirTitol() {
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 60, AMPLADA_FINESTRA, ALTURA_TITOL);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        finestra.add(titol);
    }

    private void configurarFiltres() {
        JLabel lblBuscarNom = new JLabel("BUSCA PER:");
        lblBuscarNom.setBounds(50, 80, 100, 30);
        finestra.add(lblBuscarNom);
        
        filtreCategoria(finestra);
        flitrarPerNomJugador(finestra);
        filtrePerNIF(finestra);
        filtrePerDataNaix(finestra);
        filtrePerOrdenarCognom(finestra);
    }

    private void inicialitzarTaula() {
        modelTaulaJugadors = new DefaultTableModel(new String[]{"ID Legal", "Nom", "Cognoms", "Edat", "Categoria"},0) 
        {
            public boolean esCelaEditable(int fila, int columna) {
                return false;
            }
        };

        taulaJugadors = new JTable(modelTaulaJugadors);
        taulaJugadors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane panellDesplacament = new JScrollPane(taulaJugadors);
        panellDesplacament.setBounds(MARGE_LATERAL, POSICIO_I_TAULA, AMPLADA_TAULA, ALTURA_TAULA);
        finestra.add(panellDesplacament);

        carregarJugadors();
    }

    private void carregarJugadors() {
        try {
            jugadors = persistencia.obtenirTotsJugadors();
            jugadorsFiltrats = new ArrayList<>(jugadors);
            modelTaulaJugadors.setRowCount(0);

            for (Jugador jugador : jugadors) {
                modelTaulaJugadors.addRow(new Object[]{ //No podia posar jugador i he buscat això del object
                    jugador.getIdLegal(),
                    jugador.getNom(),
                    jugador.getCognoms(),
                    calcularEdat(jugador.getDataNaix()),
                    calcularCategoria(calcularEdat(jugador.getDataNaix()))
                });
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null,"Error en obtenir els jugadors: " + e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void filtreCategoria(JFrame frame) {
        // ComboBox per filtrar per categoria
        JLabel lblFiltreCategoria = new JLabel("Filtrar per Categoria:");
        lblFiltreCategoria.setBounds(600, 470, 150, 30);
        frame.add(lblFiltreCategoria);

        String[] categories = {"Totes", "Benjamí", "Aleví", "Infantil", "Cadet", "Juvenil", "Sènior"};
        comboCategoria = new JComboBox<>(categories); // Inicializar la variable de clase
        comboCategoria.setBounds(750, 470, 100, 30);
        frame.add(comboCategoria);

        comboCategoria.addActionListener(e -> aplicarFiltresActuals());
    }

    private int calcularEdat(Date dataNaix) {
        Calendar avui = Calendar.getInstance();
        Calendar dataNaixement = Calendar.getInstance();
        dataNaixement.setTime(dataNaix);

        int edat = avui.get(Calendar.YEAR) - dataNaixement.get(Calendar.YEAR);
        if (avui.get(Calendar.DAY_OF_YEAR) < dataNaixement.get(Calendar.DAY_OF_YEAR)) {
            edat--;
        }
        return edat;
    }

    private String calcularCategoria(int edat) {
        if (edat >= 7 && edat <= 8) return "Benjamí";
        if (edat >= 9 && edat <= 11) return "Aleví";
        if (edat >= 12 && edat <= 13) return "Infantil";
        if (edat >= 14 && edat <= 15) return "Cadet";
        if (edat >= 16 && edat <= 17) return "Juvenil";
        return "Sènior";
    }

    private void flitrarPerNomJugador(JFrame frame) {
        JLabel lblBuscarNom = new JLabel("NOM:");
        lblBuscarNom.setBounds(50, 110, 100, 30);
        frame.add(lblBuscarNom);

        txtCercaNom = new JTextField(); 
        txtCercaNom.setBounds(100, 110, 100, 30);
        frame.add(txtCercaNom);

        JButton btnBuscar = new JButton("🔍");
        btnBuscar.setBounds(210, 110, 50, 30);
        btnBuscar.setBackground(new Color(173, 216, 230)); 
        btnBuscar.setFocusPainted(false);
        frame.add(btnBuscar);

        btnBuscar.addActionListener(e -> aplicarFiltresActuals());
    }
    
    private void filtrePerNIF(JFrame frame) {
        JLabel lblBuscarNIF = new JLabel("NIF:");
        lblBuscarNIF.setBounds(270, 110, 50, 30);
        frame.add(lblBuscarNIF);

        txtCercaNIF = new JTextField(); 
        txtCercaNIF.setBounds(310, 110, 100, 30);
        frame.add(txtCercaNIF);

        JButton btnBuscarNIF = new JButton("🔍");
        btnBuscarNIF.setBounds(420, 110, 50, 30);
        btnBuscarNIF.setBackground(new Color(173, 216, 230)); 
        btnBuscarNIF.setFocusPainted(false);
        frame.add(btnBuscarNIF);

        btnBuscarNIF.addActionListener(e -> aplicarFiltresActuals());
    }
    
    private void filtrePerDataNaix(JFrame frame) {
        JLabel lblBuscarDataNaix = new JLabel("DATA DE NAIXAMENT:");
        lblBuscarDataNaix.setBounds(480, 110, 140, 30);
        frame.add(lblBuscarDataNaix);

        txtCercaDataNaix = new JTextField(); 
        txtCercaDataNaix.setBounds(620, 110, 100, 30);
        frame.add(txtCercaDataNaix);

        JButton btnBuscarDataNaix = new JButton("🔍");
        btnBuscarDataNaix.setBounds(730, 110, 50, 30);
        btnBuscarDataNaix.setBackground(new Color(173, 216, 230));
        btnBuscarDataNaix.setFocusPainted(false);
        frame.add(btnBuscarDataNaix);

        btnBuscarDataNaix.addActionListener(e -> {
            if (!txtCercaDataNaix.getText().trim().equals("")) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    sdf.parse(txtCercaDataNaix.getText().trim());
                    aplicarFiltresActuals();
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(frame,"Format de data invàlid! El format correcte és dd-MM-yyyy.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                aplicarFiltresActuals();
            }
        });
    }
    
    private void filtrePerOrdenarCognom(JFrame frame) {
        JLabel lblOrdenarPerCognom = new JLabel("Ordenar per cognom:");
        lblOrdenarPerCognom.setBounds(600, 510, 150, 30);
        frame.add(lblOrdenarPerCognom);

        chkOrdenarCognom = new JCheckBox(); 
        chkOrdenarCognom.setBounds(750, 515, 20, 20);
        frame.add(chkOrdenarCognom);

        chkOrdenarCognom.addActionListener(e -> aplicarFiltresActuals());
    }
    
       private void aplicarFiltresActuals() {
        String nom = txtCercaNom.getText().trim();
        String nif = txtCercaNIF.getText().trim();
        Date dataNaix = null;
        try {
            if (!txtCercaDataNaix.getText().trim().equals("")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                dataNaix = sdf.parse(txtCercaDataNaix.getText().trim());
            }
        } catch (ParseException ex) {
            // Això ho faig per que no peti el programa
        }
        String categoria = (String) comboCategoria.getSelectedItem();
        boolean ordenarPerCognom = chkOrdenarCognom.isSelected();
        aplicarFiltresCombinats(nom, nif, dataNaix, categoria, ordenarPerCognom);
    }
       
    private void aplicarFiltresCombinats(String nom, String nif, Date dataNaix, String categoria, boolean ordenarPerCognom) {
        try {
            jugadorsFiltrats = new ArrayList<>(jugadors);

            if (nom != null && !nom.equals("")) {
                List<Jugador> jugadorsFiltratsTmp = new ArrayList<>();
                for (Jugador j : jugadorsFiltrats) {
                    if (j.getNom().toLowerCase().contains(nom.toLowerCase())) {
                        jugadorsFiltratsTmp.add(j);
                    }
                }
                jugadorsFiltrats = jugadorsFiltratsTmp;
            }

            if (nif != null && !nif.equals("")) {
                List<Jugador> jugadorsFiltratsTmp = new ArrayList<>();
                for (Jugador j : jugadorsFiltrats) {
                    if (j.getIdLegal().toLowerCase().contains(nif.toLowerCase())) {
                        jugadorsFiltratsTmp.add(j);
                    }
                }
                jugadorsFiltrats = jugadorsFiltratsTmp;
            }

            if (dataNaix != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dataStr = sdf.format(dataNaix);
                List<Jugador> jugadorsFiltratsTmp = new ArrayList<>();
                for (Jugador j : jugadorsFiltrats) {
                    if (sdf.format(j.getDataNaix()).equals(dataStr)) {
                        jugadorsFiltratsTmp.add(j);
                    }
                }
                jugadorsFiltrats = jugadorsFiltratsTmp;
            }

            if (categoria != null && !categoria.equals("Totes")) {
                List<Jugador> jugadorsFiltratsTmp = new ArrayList<>();
                for (Jugador j : jugadorsFiltrats) {
                    if (calcularCategoria(calcularEdat(j.getDataNaix())).equals(categoria)) {
                        jugadorsFiltratsTmp.add(j);
                    }
                }
                jugadorsFiltrats = jugadorsFiltratsTmp;
            }

            if (ordenarPerCognom) {
                Collections.sort(jugadorsFiltrats, (j1, j2) -> j1.getCognoms().compareTo(j2.getCognoms()));
            }

            actualitzarTaula();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Error en aplicar els filtres: " + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualitzarTaula() {
        modelTaulaJugadors.setRowCount(0);
        for (Jugador jugador : jugadorsFiltrats) {
            modelTaulaJugadors.addRow(new Object[]{
                jugador.getIdLegal(),
                jugador.getNom(),
                jugador.getCognoms(),
                calcularEdat(jugador.getDataNaix()),
                calcularCategoria(calcularEdat(jugador.getDataNaix()))
            });
        }
    }

    private void inicialitzarBotons() {
        JButton botoAfegirJugador = crearBotoAfegir();
        JButton botoModificarJugador = crearBotoModificar();
        JButton botoEliminarJugador = crearBotoEliminar();
    }

    private JButton crearBotoAfegir() {
        JButton botoAfegirJugador = new JButton("Afegir");
        botoAfegirJugador.setBounds(MARGE_LATERAL, POSICIO_I_BOTONS, AMPLADA_BOTO, ALTURA_BOTO);
        botoAfegirJugador.setBackground(new Color(173, 216, 230));
        botoAfegirJugador.setFocusPainted(false);
        finestra.add(botoAfegirJugador);
        
        botoAfegirJugador.addActionListener(e -> {
            AfegirEditarJugador.mostrarFormulari(null, persistencia);
            carregarJugadors();
        });
        
        return botoAfegirJugador;
    }

    private JButton crearBotoModificar() {
        JButton botoModificarJugador = new JButton("Modificar");
        botoModificarJugador.setBounds(MARGE_LATERAL + 100 + 50, 490, 100, 30);
        botoModificarJugador.setBackground(new Color(173, 216, 230));
        botoModificarJugador.setFocusPainted(false);
        finestra.add(botoModificarJugador);
        
        botoModificarJugador.addActionListener(e -> {
            int filaSeleccionada = taulaJugadors.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(finestra,"Has de seleccionar un jugador","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            finestra.dispose();
            Jugador jugadorSeleccionat = jugadorsFiltrats.get(filaSeleccionada);
            AfegirEditarJugador.mostrarFormulari(jugadorSeleccionat, persistencia);
        });
        
        return botoModificarJugador;
    }

    private JButton crearBotoEliminar() {
        JButton botoEliminarJugador = new JButton("Eliminar");
        botoEliminarJugador.setBounds(MARGE_LATERAL + 2 * (100 + 50), 490, 100, 30);
        botoEliminarJugador.setBackground(new Color(173, 216, 230));
        botoEliminarJugador.setFocusPainted(false);
        finestra.add(botoEliminarJugador);
        
        botoEliminarJugador.addActionListener(e -> {
            int filaSeleccionada = taulaJugadors.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(finestra,"Has de seleccionar un jugador","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            Jugador jugadorSeleccionat = jugadorsFiltrats.get(filaSeleccionada);
            int confirmacio = JOptionPane.showConfirmDialog(finestra,"Estàs segur que vols eliminar el jugador: "
                    + jugadorSeleccionat.getNom() + "?","Confirmar",JOptionPane.YES_NO_OPTION);

            if (confirmacio == JOptionPane.YES_OPTION) {
                try {
                    persistencia.eliminarJugador(jugadorSeleccionat.getId());
                    persistencia.confirmarCanvis();
                    carregarJugadors();
                } catch (GestorBDEsportsException ex) {
                    JOptionPane.showMessageDialog(finestra,"Error al eliminar el jugador: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        return botoEliminarJugador;
    }

}
