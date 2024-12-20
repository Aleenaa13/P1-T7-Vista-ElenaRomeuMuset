package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.Equip;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Membre;
import p1.t6.model.romeumusetelena.Temporada;

public class GestioEquips {
    // Constants de configuració
    private static final int AMPLADA_FINESTRA = 900;
    private static final int ALTURA_FINESTRA = 600;
    private static final int ALTURA_BOTO_MENU = 40;
    private static final int MARGE_LATERAL = 50;
    private static final int POSICIO_I_TITOL = 60;
    private static final int ALTURA_TITOL = 40;
    
    // Constants de colors
    private static final Color COLOR_MENU = new Color(70, 130, 180);
    private static final Color COLOR_FONS = new Color(245, 245, 245);
    private static final Color COLOR_TITOL = new Color(70, 130, 180);
    private static final Color COLOR_BOTO_ACCIO = new Color(135, 206, 250);
    
    private IPersistencia persistencia;
    private DefaultTableModel modelTaulaEquips;
    private JTable taulaEquips;
    private List<Equip> equips ;
    private List<Equip> equipsFiltrats;
    private JComboBox<String> comboTemporada;
    private JComboBox<String> comboCategoria;
    private JFrame finestra;

    public GestioEquips(IPersistencia persistencia) {
        this.persistencia = persistencia;
        inicialitzarFinestra();
        inicialitzarComponents();
    }

    private void inicialitzarFinestra() {
        finestra = new JFrame("Gestió d'Equips");
        finestra.setSize(AMPLADA_FINESTRA, ALTURA_FINESTRA);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(null);
        finestra.setResizable(false);
        finestra.setLocationRelativeTo(null);
        finestra.getContentPane().setBackground(COLOR_FONS);
    }

    private void inicialitzarComponents() {
        afegirTitol();
        configurarMenuSuperior();
        inicialitzarTaula();
        inicialitzarFiltres();
        inicialitzarBotons();
        
        finestra.setVisible(true);
    }

    private void afegirTitol() {
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, POSICIO_I_TITOL, AMPLADA_FINESTRA, ALTURA_TITOL);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(COLOR_TITOL);
        finestra.add(titol);
    }

    private void configurarMenuSuperior() {
        int numBotons = 6;
        int ampladaBoto = AMPLADA_FINESTRA / numBotons;
        int alturaBoto = ALTURA_BOTO_MENU;
        String[] nomsBotons = {"INICI", "EQUIPS", "JUGADORS", "TEMPORADES", "INFORMES", "TANCAR SESSIÓ"};
        JButton[] botonsMenu = new JButton[numBotons];

        for (int i = 0; i < nomsBotons.length; i++) {
            botonsMenu[i] = new JButton(nomsBotons[i]);
            botonsMenu[i].setBounds(i * ampladaBoto, 0, ampladaBoto, alturaBoto);
            botonsMenu[i].setBackground(COLOR_MENU);
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
        
        //botonsMenu[1].setEnabled(false);
        
        botonsMenu[2].addActionListener(e -> {
            finestra.dispose();
            new GestioJugador(persistencia);
        });
        botonsMenu[3].addActionListener(e -> {
            finestra.dispose();
            new GestioTemporades(persistencia);
        });
        botonsMenu[4].addActionListener(e -> {
            finestra.dispose();
            new Informes(persistencia);
        });
        
        botonsMenu[5].addActionListener(e -> {
            TancarSessio.executar(finestra, persistencia);
        });
    }

    private void inicialitzarTaula() {
        modelTaulaEquips = new DefaultTableModel(new String[]{"Nom", "Tipus", "Categoria", "Temporada"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taulaEquips = new JTable(modelTaulaEquips);
        taulaEquips.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taulaEquips);
        scrollPane.setBounds(MARGE_LATERAL, 150, 800, 300);
        finestra.add(scrollPane);

        carregarEquips();
    }

    private void carregarEquips() {
        try {
            equips = persistencia.obtenirTotsEquips();
            equipsFiltrats = new ArrayList<>();
            modelTaulaEquips.setRowCount(0);

             // Filtra només els equips de l'any actual.
            int anyActual = java.time.Year.now().getValue();
            for (Equip equip : equips) {
                if (equip.getAnyTemporada() == anyActual) { 
                    equipsFiltrats.add(equip);
                }
            }

            Collections.sort(equipsFiltrats, (e1, e2) -> Integer.compare(e1.getIdCategoria(), e2.getIdCategoria()));

            
            for (Equip equip : equipsFiltrats) {
                String tipus = switch (equip.getTipus()) {
                    case H -> "Homes";
                    case D -> "Dones";
                    case M -> "Mixt";
                    default -> "Desconegut";
                };

                String categoria = switch (equip.getIdCategoria()) {
                    case 1 -> "Benjamí";
                    case 2 -> "Aleví";
                    case 3 -> "Infantil";
                    case 4 -> "Cadet";
                    case 5 -> "Junior";
                    case 6 -> "Senior";
                    default -> "Desconeguda";
                };

                modelTaulaEquips.addRow(new Object[]{ //el mateix que a jugadors (explicat)
                    equip.getNom(),
                    tipus,
                    categoria,
                    equip.getAnyTemporada()
                });
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en obtenir els equips: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicialitzarFiltres() {
        inicialitzarFiltreCategoria();
        inicialitzarFiltreTemporada();
    }

    private void inicialitzarFiltreCategoria() {
        JLabel etiquetaFiltreCategoria = new JLabel("Filtrar per categoria:");
        etiquetaFiltreCategoria.setBounds(AMPLADA_FINESTRA - MARGE_LATERAL - 150 - 150, 
            470, 
            150, 
            30
        );
        finestra.add(etiquetaFiltreCategoria);

        String[] categories = {"Totes", "Benjamí", "Aleví", "Infantil", "Cadet", "Junior", "Senior"};
        comboCategoria = new JComboBox<>(categories);
        comboCategoria.setBounds(AMPLADA_FINESTRA - MARGE_LATERAL - 150,
            470,
            150,
            30
        );
        finestra.add(comboCategoria);

        comboCategoria.addActionListener(e -> {
            if (comboTemporada != null) {
                String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
                String temporadaSeleccionada = (String) comboTemporada.getSelectedItem();
                aplicarFiltres(categoriaSeleccionada, temporadaSeleccionada);
            }
        });
    }

    private void inicialitzarFiltreTemporada() {
        JLabel etiquetaFiltreTemporada = new JLabel("Filtrar per temporada:");
        etiquetaFiltreTemporada.setBounds(AMPLADA_FINESTRA - MARGE_LATERAL - 150 - 150, 
            510, 
            150, 
            30
        );
        finestra.add(etiquetaFiltreTemporada);

        comboTemporada = new JComboBox<>();
        comboTemporada.setBounds(AMPLADA_FINESTRA - MARGE_LATERAL - 150,
            510,
            150,
            30
        );
        finestra.add(comboTemporada);

        try {
            List<Temporada> temporades = persistencia.obtenirTotesTemporades();
            for (Temporada temporada : temporades) {
                comboTemporada.addItem(String.valueOf(temporada.getAny()));
            }
            comboTemporada.setSelectedItem("2024");
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en carregar temporades: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }

        comboTemporada.addActionListener(e -> {
            if (comboCategoria != null) {
                String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
                String temporadaSeleccionada = (String) comboTemporada.getSelectedItem();
                aplicarFiltres(categoriaSeleccionada, temporadaSeleccionada);
            }
        });
    }

    private void aplicarFiltres(String categoriaSeleccionada, String temporadaSeleccionada) {
        try {
            int anySeleccionat = Integer.parseInt(temporadaSeleccionada);
            equipsFiltrats = new ArrayList<>();
            modelTaulaEquips.setRowCount(0);
            
            boolean equipsTrobats = false;

            // Primer recopilem tots els equips que compleixen els filtres
            for (Equip equip : equips) {
                if (equip.getAnyTemporada() == anySeleccionat) {
                    String categoria = switch (equip.getIdCategoria()) {
                        case 1 -> "Benjamí";
                        case 2 -> "Aleví";
                        case 3 -> "Infantil";
                        case 4 -> "Cadet";
                        case 5 -> "Junior";
                        case 6 -> "Senior";
                        default -> "Desconeguda";
                    };

                    if ("Totes".equals(categoriaSeleccionada) || categoria.equals(categoriaSeleccionada)) {
                        equipsFiltrats.add(equip);
                        equipsTrobats = true;
                    }
                }
            }

            Collections.sort(equipsFiltrats, (e1, e2) -> Integer.compare(e1.getIdCategoria(), e2.getIdCategoria()));

            for (Equip equip : equipsFiltrats) {
                String categoria = switch (equip.getIdCategoria()) {
                    case 1 -> "Benjamí";
                    case 2 -> "Aleví";
                    case 3 -> "Infantil";
                    case 4 -> "Cadet";
                    case 5 -> "Junior";
                    case 6 -> "Senior";
                    default -> "Desconeguda";
                };

                String tipus = switch (equip.getTipus()) {
                    case H -> "Homes";
                    case D -> "Dones";
                    case M -> "Mixt";
                    default -> "Desconegut";
                };

                modelTaulaEquips.addRow(new Object[]{
                    equip.getNom(),
                    tipus,
                    categoria,
                    equip.getAnyTemporada()
                });
            }

            // Això mira si no hi ha cap equip per la categoria seleccionada
            if (!equipsTrobats && !"Totes".equals(categoriaSeleccionada)) {
                modelTaulaEquips.addRow(new Object[]{"Categoria sense equips","-",categoriaSeleccionada,temporadaSeleccionada});
                taulaEquips.setEnabled(false);
            } else {
                taulaEquips.setEnabled(true);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en aplicar els filtres: " + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicialitzarBotons() {
        JButton botoAfegirEquip = crearBotoAfegir();
        JButton botoModificarEquip = crearBotoModificar();
        JButton botoEliminarEquip = crearBotoEliminar();
    }

    private JButton crearBotoAfegir() {
        JButton botoAfegirEquip = new JButton("Afegir");
        botoAfegirEquip.setBounds(MARGE_LATERAL, 490, 100, 30);
        botoAfegirEquip.setBackground(COLOR_BOTO_ACCIO);
        botoAfegirEquip.setFocusPainted(false);
        finestra.add(botoAfegirEquip);

        botoAfegirEquip.addActionListener(e -> {
            AfegirEditarEquip.mostrarFormulari(null, persistencia);
            carregarEquips();
        });

        return botoAfegirEquip;
    }


    private JButton crearBotoModificar() {
        JButton botoModificarEquip = new JButton("Modificar");
        botoModificarEquip.setBounds(MARGE_LATERAL + 100 + 50, 490, 100, 30);
        botoModificarEquip.setBackground(COLOR_BOTO_ACCIO);
        botoModificarEquip.setFocusPainted(false);
        finestra.add(botoModificarEquip);

        botoModificarEquip.addActionListener(e -> {
            int filaSeleccionada = taulaEquips.getSelectedRow();
            if (filaSeleccionada != -1) {
                finestra.dispose(); 
                Equip equipSeleccionat = equipsFiltrats.get(filaSeleccionada);
                AfegirEditarEquip.mostrarFormulari(equipSeleccionat, persistencia);
            } else {
                JOptionPane.showMessageDialog(finestra, "Selecciona un equip per modificar.","Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        return botoModificarEquip;
    }

    private JButton crearBotoEliminar() {
        JButton botoEliminarEquip = new JButton("Eliminar");
        botoEliminarEquip.setBounds(MARGE_LATERAL + 2 * (100 + 50), 490, 100, 30);
        botoEliminarEquip.setBackground(COLOR_BOTO_ACCIO);
        botoEliminarEquip.setFocusPainted(false);
        finestra.add(botoEliminarEquip);

        botoEliminarEquip.addActionListener(e -> {
            try {
                int filaSeleccionada = taulaEquips.getSelectedRow();

                if (filaSeleccionada != -1) {
                    Equip equipSeleccionat = equipsFiltrats.get(filaSeleccionada);
                    
                    //Aquí mira si un equip té membres per poder eliminar l'equip
                    List<Membre> membres = persistencia.obtenirMembresDEquip(equipSeleccionat.getId());
                    
                    //si en té demana confirmació, sinó no
                    if (!membres.isEmpty()) {
                        int resposta = JOptionPane.showConfirmDialog(
                            finestra,
                            "L'equip té " + membres.size() + " membres associats. Si elimines l'equip, s'eliminaran també tots els seus membres. " +
                            "Estàs segur que vols continuar?","Confirmar eliminació",
                            JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                        
                        if (resposta != JOptionPane.YES_OPTION) {
                            return; 
                        }
                    }
                    
                    try {
                        persistencia.eliminarEquip(equipSeleccionat.getId());
                        persistencia.confirmarCanvis();

                        carregarEquips();
                        
                        JOptionPane.showMessageDialog(finestra,"Equip eliminat correctament",
                                "Èxit",JOptionPane.INFORMATION_MESSAGE);
                        
                    } catch (GestorBDEsportsException ex) {
                        try {
                            persistencia.desferCanvis();
                        } catch (GestorBDEsportsException ex1) {
                            JOptionPane.showMessageDialog(finestra,"Error greu en desfer els canvis: " + ex1.getMessage(),
                                "Error",JOptionPane.ERROR_MESSAGE);
                        }
                        JOptionPane.showMessageDialog(finestra,"Error en eliminar l'equip: " + ex.getMessage(),
                            "Error",JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(finestra,"Selecciona un equip per eliminar",
                        "Avís",JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(finestra,"Error inesperat: " + ex.getMessage(),
                    "Error",JOptionPane.ERROR_MESSAGE);
            }
        });

        return botoEliminarEquip;
    }
}

