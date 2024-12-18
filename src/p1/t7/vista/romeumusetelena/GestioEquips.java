package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.Equip;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Temporada;
import p1.t6.model.romeumusetelena.TipusEquip;

public class GestioEquips {
    private IPersistencia persistencia;
    private DefaultTableModel modelTaulaEquips;
    private JTable taulaEquips;
    private List<Equip> equips ;
    private List<Equip> equipsFiltrats;
    private JComboBox<String> comboTemporada;
    private JComboBox<String> comboCategoria;

    public GestioEquips(IPersistencia persistencia) {
        this.persistencia = persistencia;

        // Crear el frame
        JFrame frame = new JFrame("Gestió d'Equips");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Fons blanc trencat
        Color blancTrencat = new Color(245, 245, 245);
        frame.getContentPane().setBackground(blancTrencat);

        // Menú superior
        configurarMenuSuperior(frame);

        // Títol centrat
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        // Configurar la taula
        configurarTaula(frame);

        // Configurar filtres
        configurarFiltreCategoria(frame);
        configurarFiltreTemporada(frame);

        // Configurar botons inferiors
        configurarBotonsInferiors(frame);

        // Mostrar el frame
        frame.setVisible(true);
    }

    private void configurarMenuSuperior(JFrame frame) {
        int numBotons = 6;
        int ampladaBoto = 900 / numBotons;
        int alturaBoto = 40;
        String[] nomsBotons = {"INICI", "EQUIPS", "JUGADORS", "TEMPORADES", "INFORMES", "TANCAR SESSIÓ"};
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

        botonsMenu[5].addActionListener(e -> {
            TancarSessio.executar(frame, persistencia);
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
            new GestioJugador(persistencia);
        });
        botonsMenu[3].addActionListener(e -> {
            frame.dispose();
            new GestioTemporades(persistencia);
        });
        botonsMenu[4].addActionListener(e -> {
            frame.dispose();
            // new InformeEquips();
        });
    }

    private void configurarTaula(JFrame frame) {
        modelTaulaEquips = new DefaultTableModel(new String[]{"Nom", "Tipus", "Categoria", "Temporada"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        taulaEquips = new JTable(modelTaulaEquips);
        taulaEquips.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taulaEquips);
        scrollPane.setBounds(50, 150, 800, 300);
        frame.add(scrollPane);

        carregarEquips();
    }

    private void carregarEquips() {
        try {
            equips = persistencia.obtenirTotsEquips();
            equipsFiltrats = new ArrayList<>();
            modelTaulaEquips.setRowCount(0);

            // Filtrar inicialmente solo los equipos de 2024
            for (Equip equip : equips) {
                if (equip.getAnyTemporada() == 2024) {  // Mostrar solo equipos de 2024 inicialmente
                    equipsFiltrats.add(equip);
                }
            }

            // Ordenar los equipos por categoría
            equipsFiltrats.sort(Comparator.comparingInt(Equip::getIdCategoria));

            // Mostrar los equipos ordenados
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

                modelTaulaEquips.addRow(new Object[]{
                    equip.getNom(),
                    tipus,
                    categoria,
                    equip.getAnyTemporada()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en obtenir els equips: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarFiltreCategoria(JFrame frame) {
        JLabel etiquetaFiltreCategoria = new JLabel("Filtrar per categoria:");
        etiquetaFiltreCategoria.setBounds(550, 470, 150, 30);
        frame.add(etiquetaFiltreCategoria);

        String[] categories = {"Totes", "Benjamí", "Aleví", "Infantil", "Cadet", "Junior", "Senior"};
        comboCategoria = new JComboBox<>(categories);
        comboCategoria.setBounds(700, 470, 150, 30);
        frame.add(comboCategoria);

        comboCategoria.addActionListener(e -> {
            if (comboTemporada != null) {
                String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
                String temporadaSeleccionada = (String) comboTemporada.getSelectedItem();
                aplicarFiltres(categoriaSeleccionada, temporadaSeleccionada);
            }
        });
    }

    private void configurarFiltreTemporada(JFrame frame) {
        JLabel etiquetaFiltreTemporada = new JLabel("Filtrar per temporada:");
        etiquetaFiltreTemporada.setBounds(550, 510, 150, 30);
        frame.add(etiquetaFiltreTemporada);

        comboTemporada = new JComboBox<>();
        comboTemporada.setBounds(700, 510, 150, 30);
        frame.add(comboTemporada);

        try {
            List<Temporada> temporades = persistencia.obtenirTotesTemporades();
            for (Temporada temporada : temporades) {
                comboTemporada.addItem(String.valueOf(temporada.getAny()));
            }
            comboTemporada.setSelectedItem("2024");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en carregar temporades: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
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

            // Ordenem els equips per categoria
            equipsFiltrats.sort(Comparator.comparingInt(Equip::getIdCategoria));

            // Mostrem els equips ordenats
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

            // Si no s'han trobat equips per la categoria seleccionada
            if (!equipsTrobats && !"Totes".equals(categoriaSeleccionada)) {
                modelTaulaEquips.addRow(new Object[]{
                    "Categoria sense equips",
                    "-",
                    categoriaSeleccionada,
                    temporadaSeleccionada
                });
                taulaEquips.setEnabled(false);
            } else {
                taulaEquips.setEnabled(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en aplicar els filtres: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarBotonsInferiors(JFrame frame) {
        JButton botoAfegirEquip = crearBotoAfegir(frame);
        JButton botoModificarEquip = crearBotoModificar(frame);
        JButton botoEliminarEquip = crearBotoEliminar(frame);
    }

    private JButton crearBotoAfegir(JFrame frame) {
        JButton botoAfegirEquip = new JButton("Afegir");
        botoAfegirEquip.setBounds(50, 490, 100, 30);
        botoAfegirEquip.setBackground(new Color(135, 206, 250));  // Blau cel
        botoAfegirEquip.setFocusPainted(false);
        frame.add(botoAfegirEquip);

        botoAfegirEquip.addActionListener(e -> {
            // Cridar a la classe que gestiona el formulari per afegir o editar un equip
            AfegirEditarEquip.mostrarFormulari(null, persistencia);
        });

        return botoAfegirEquip;
    }


    private JButton crearBotoModificar(JFrame frame) {
        JButton botoModificarEquip = new JButton("Modificar");
        botoModificarEquip.setBounds(200, 490, 100, 30);
        botoModificarEquip.setBackground(new Color(135, 206, 250));  // Blau cel
        botoModificarEquip.setFocusPainted(false);
        frame.add(botoModificarEquip);

        botoModificarEquip.addActionListener(e -> {
            // Obtenir la fila seleccionada de la taula
            int filaSeleccionada = taulaEquips.getSelectedRow();

            // Verificar si s'ha seleccionat alguna fila
            if (filaSeleccionada != -1) {
                // Obtenir les dades de l'equip seleccionat de la llista filtrada
                Equip equipSeleccionat = equipsFiltrats.get(filaSeleccionada);
                // Cridar a la funció per modificar l'equip
                AfegirEditarEquip.mostrarFormulari(equipSeleccionat, persistencia);
            } else {
                JOptionPane.showMessageDialog(frame, "Selecciona un equip per modificar.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        return botoModificarEquip;
    }

    private JButton crearBotoEliminar(JFrame frame) {
        JButton botoEliminarEquip = new JButton("Eliminar");
        botoEliminarEquip.setBounds(350, 490, 100, 30);
        botoEliminarEquip.setBackground(new Color(135, 206, 250));  // Blau cel
        botoEliminarEquip.setFocusPainted(false);
        frame.add(botoEliminarEquip);

        botoEliminarEquip.addActionListener(e -> {
            // Obtenir la fila seleccionada de la taula
            int filaSeleccionada = taulaEquips.getSelectedRow();

            // Verificar si s'ha seleccionat alguna fila
            if (filaSeleccionada != -1) {
                // Obtenir l'equip seleccionat de la llista filtrada
                Equip equipSeleccionat = equipsFiltrats.get(filaSeleccionada);
                
                // Confirmació per eliminar l'equip
                int confirmacio = JOptionPane.showConfirmDialog(frame, 
                    "Vols eliminar l'equip: " + equipSeleccionat.getNom() + "?", 
                    "Confirmar", 
                    JOptionPane.YES_NO_OPTION);

                if (confirmacio == JOptionPane.YES_OPTION) {
                    try {
                        // Eliminar l'equip de la base de dades
                        persistencia.eliminarEquip(equipSeleccionat.getId());
                        persistencia.confirmarCanvis();
                        // Refrescar la taula d'equips
                        carregarEquips();
                        
                        // Reapliquem els filtres després de carregar
                        String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
                        String temporadaSeleccionada = (String) comboTemporada.getSelectedItem();
                        aplicarFiltres(categoriaSeleccionada, temporadaSeleccionada);
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, 
                            "Error al eliminar l'equip: " + ex.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Selecciona un equip per eliminar.", 
                    "Error", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        return botoEliminarEquip;
    }


}

