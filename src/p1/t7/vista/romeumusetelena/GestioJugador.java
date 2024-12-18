package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.Equip;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Jugador;
import p1.t6.model.romeumusetelena.Temporada;
import p1.t6.model.romeumusetelena.TipusEquip;

public class GestioJugador {
    private IPersistencia persistencia; // Per accedir a la interfície de persistència
    private DefaultTableModel modelTaulaJugadors;
    private  JTable taulaJugadors;
    private List<Jugador> jugadors; // Lista de jugadores cargada de la base de datos
    private List<Jugador> jugadorsFiltrats; // Lista sincronizada con la tabla
    private JTextField txtBuscarNom;
    private JTextField txtBuscarNIF;
    private JTextField txtBuscarDataNaix;
    private JComboBox<String> comboCategoria;
    private JCheckBox chkOrdenarCognom;
    

    public GestioJugador(IPersistencia persistencia) {
        this.persistencia = persistencia; // Inicialitzar la interfície de persistència

        // Crear el frame
        JFrame frame = new JFrame("Gestió de Jugadors");
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

        // Afegir acció als botons del menú
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

        // Títol centrat
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        // Configurar la taula
        modelTaulaJugadors = new DefaultTableModel(new String[]{"ID Legal", "Nom", "Cognoms", "Edat", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Cap cel·la és editable
            }
       };
 
        taulaJugadors = new JTable(modelTaulaJugadors);
        taulaJugadors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Només una fila seleccionable
        JScrollPane scrollPane = new JScrollPane(taulaJugadors);
        scrollPane.setBounds(50, 150, 800, 300);
        frame.add(scrollPane);

        // Configurar botons inferiors
        configurarBotonsInferiors(frame);

        // Carregar dades de la base de dades
        carregarJugadors();
        
        //flitres
        filtreCategoria(frame);
        
        JLabel lblBuscarNom = new JLabel("BUSCA PER:");
        lblBuscarNom.setBounds(50, 80, 100, 30);
        frame.add(lblBuscarNom);
        
        flitrarPerNomJugador(frame);
        filtrePerNIF(frame);
        filtrePerDataNaix(frame);
        filtrePerOrdenarCognom(frame);
        
        // Mostrar el frame
        frame.setVisible(true);
    }

    private void configurarBotonsInferiors(JFrame frame) {
        // Botons d'acció
        JButton btnAfegir = new JButton("Afegir");
        btnAfegir.setBounds(100, 470, 100, 30);
        btnAfegir.setBackground(new Color(173, 216, 230)); // Blau cel
        btnAfegir.setFocusPainted(false);
        frame.add(btnAfegir);
        
        btnAfegir.addActionListener(e -> {
            
            AfegirEditarJugador.mostrarFormulari(null, persistencia);
        });
        
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(250, 470, 100, 30);
        btnEditar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnEditar.setFocusPainted(false);
        frame.add(btnEditar);
        
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = taulaJugadors.getSelectedRow();

            // Verificar si s'ha seleccionat alguna fila
            if (filaSeleccionada != -1) {
                Jugador jugadorSeleccionat = jugadorsFiltrats.get(filaSeleccionada);
                AfegirEditarJugador.mostrarFormulari(jugadorSeleccionat, persistencia);
            }
            
        });
        
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(400, 470, 100, 30);
        btnEliminar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnEliminar.setFocusPainted(false);
        frame.add(btnEliminar);
        
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = taulaJugadors.getSelectedRow();

            if (filaSeleccionada != -1) {
                Jugador jugadorSeleccionat = jugadorsFiltrats.get(filaSeleccionada); // Trabaja con la lista filtrada
                int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro que vols eliminar el jugador: "+ jugadorSeleccionat.getNom()+ "?", "Confirmar", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        persistencia.eliminarJugador(jugadorSeleccionat.getId());
                        persistencia.confirmarCanvis();
                        //System.out.println(jugadorSeleccionat);
                        buscarPerNom(""); // Refresca la tabla con todos los jugadores
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al eliminar el jugador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    private void carregarJugadors() {
        try {
            jugadors = persistencia.obtenirTotsJugadors(); // Obtenim els jugadors de la BD
            jugadorsFiltrats = new ArrayList<>(jugadors); // Inicialitzar jugadorsFiltrats amb tots els jugadors
            modelTaulaJugadors.setRowCount(0); // Esborrem les dades antigues

            for (Jugador jugador : jugadors) {
                modelTaulaJugadors.addRow(new Object[]{
                    jugador.getIdLegal(),
                    jugador.getNom(),
                    jugador.getCognoms(),
                    calcularEdat(jugador.getDataNaix()),
                    calcularCategoria(calcularEdat(jugador.getDataNaix())) // Categoria calculada
                });
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en obtenir els jugadors: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        comboCategoria.addActionListener(e -> {
            String nom = txtBuscarNom.getText().trim();
            String nif = txtBuscarNIF.getText().trim();
            Date dataNaix = null;
            try {
                if (!txtBuscarDataNaix.getText().trim().isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    dataNaix = format.parse(txtBuscarDataNaix.getText().trim());
                }
            } catch (Exception ex) {
                // Mantener dataNaix como null si hay error
            }
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean ordenarPerCognom = chkOrdenarCognom.isSelected();
            aplicarFiltrosCombinados(nom, nif, dataNaix, categoria, ordenarPerCognom);
        });
    }

    private void filtrarPerCategoria(String categoria) {
        try {
            jugadorsFiltrats = new ArrayList<>(); // Reiniciar la lista filtrada
            modelTaulaJugadors.setRowCount(0); // Netejar taula

            for (Jugador jugador : jugadors) {
                String categoriaJugador = calcularCategoria(calcularEdat(jugador.getDataNaix()));

                if (categoriaJugador.equals(categoria)) {
                    jugadorsFiltrats.add(jugador); // Actualizar la lista filtrada
                    modelTaulaJugadors.addRow(new Object[]{
                        jugador.getIdLegal(),
                        jugador.getNom(),
                        jugador.getCognoms(),
                        calcularEdat(jugador.getDataNaix()),
                        categoriaJugador
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en obtenir els jugadors: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private int calcularEdat(java.util.Date dataNaix) {
        java.util.Calendar avui = java.util.Calendar.getInstance();
        java.util.Calendar dataNaixement = java.util.Calendar.getInstance();
        dataNaixement.setTime(dataNaix);

        int edat = avui.get(java.util.Calendar.YEAR) - dataNaixement.get(java.util.Calendar.YEAR);
        if (avui.get(java.util.Calendar.DAY_OF_YEAR) < dataNaixement.get(java.util.Calendar.DAY_OF_YEAR)) {
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
        // Afegir un camp de text per cercar pel nom
        JLabel lblBuscarNom = new JLabel("NOM:");
        lblBuscarNom.setBounds(50, 110, 100, 30);
        frame.add(lblBuscarNom);

        txtBuscarNom = new JTextField(); // Inicializar la variable de clase
        txtBuscarNom.setBounds(100, 110, 100, 30);
        frame.add(txtBuscarNom);

        JButton btnBuscar = new JButton("🔍");
        btnBuscar.setBounds(210, 110, 50, 30);
        btnBuscar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnBuscar.setFocusPainted(false);
        frame.add(btnBuscar);

        // Listener per al botó Buscar
        btnBuscar.addActionListener(e -> {
            String nom = txtBuscarNom.getText().trim();
            String nif = txtBuscarNIF.getText().trim();
            Date dataNaix = null;
            try {
                if (!txtBuscarDataNaix.getText().trim().isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    dataNaix = format.parse(txtBuscarDataNaix.getText().trim());
                }
            } catch (Exception ex) {
                // Mantener dataNaix como null si hay error
            }
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean ordenarPerCognom = chkOrdenarCognom.isSelected();
            aplicarFiltrosCombinados(nom, nif, dataNaix, categoria, ordenarPerCognom);
        });
    }
    
    private void buscarPerNom(String nom) {
        try {
            jugadorsFiltrats = persistencia.buscarNomJugador(nom); // Actualiza la lista filtrada
            modelTaulaJugadors.setRowCount(0); // Limpia la tabla

            for (Jugador jugador : jugadorsFiltrats) {
                modelTaulaJugadors.addRow(new Object[]{
                    jugador.getIdLegal(),
                    jugador.getNom(),
                    jugador.getCognoms(),
                    calcularEdat(jugador.getDataNaix()),
                    calcularCategoria(calcularEdat(jugador.getDataNaix()))
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en buscar jugadors pel nom: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void filtrePerNIF(JFrame frame) {
        // Afegir un camp de text per cercar pel NIF
        JLabel lblBuscarNIF = new JLabel("NIF:");
        lblBuscarNIF.setBounds(270, 110, 50, 30);
        frame.add(lblBuscarNIF);

        txtBuscarNIF = new JTextField(); // Inicializar la variable de clase
        txtBuscarNIF.setBounds(310, 110, 100, 30);
        frame.add(txtBuscarNIF);

        JButton btnBuscarNIF = new JButton("🔍");
        btnBuscarNIF.setBounds(420, 110, 50, 30);
        btnBuscarNIF.setBackground(new Color(173, 216, 230)); // Blau cel
        btnBuscarNIF.setFocusPainted(false);
        frame.add(btnBuscarNIF);

        // Listener per al botó Buscar NIF
        btnBuscarNIF.addActionListener(e -> {
            String nom = txtBuscarNom.getText().trim();
            String nif = txtBuscarNIF.getText().trim();
            Date dataNaix = null;
            try {
                if (!txtBuscarDataNaix.getText().trim().isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    dataNaix = format.parse(txtBuscarDataNaix.getText().trim());
                }
            } catch (Exception ex) {
                // Mantener dataNaix como null si hay error
            }
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean ordenarPerCognom = chkOrdenarCognom.isSelected();
            aplicarFiltrosCombinados(nom, nif, dataNaix, categoria, ordenarPerCognom);
        });
    }
    
    private void buscarPerNIF(String nif) {
        try {
            jugadorsFiltrats = persistencia.buscarPerNIFJugador(nif); // Utilitzar el mètode de la capa de persistència
            modelTaulaJugadors.setRowCount(0); // Netejar la taula abans de mostrar els resultats

            for (Jugador jugador : jugadorsFiltrats) {
                modelTaulaJugadors.addRow(new Object[]{
                    jugador.getIdLegal(),
                    jugador.getNom(),
                    jugador.getCognoms(),
                    calcularEdat(jugador.getDataNaix()),
                    calcularCategoria(calcularEdat(jugador.getDataNaix())) // Categoria calculada
                });
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en buscar jugadors pel NIF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void filtrePerDataNaix(JFrame frame) {
        // Afegir un camp de text per cercar per data de naixement
        JLabel lblBuscarDataNaix = new JLabel("DATA DE NAIXAMENT:");
        lblBuscarDataNaix.setBounds(480, 110, 140, 30);
        frame.add(lblBuscarDataNaix);

        txtBuscarDataNaix = new JTextField(); // Inicializar la variable de clase
        txtBuscarDataNaix.setBounds(620, 110, 100, 30);
        frame.add(txtBuscarDataNaix);

        JButton btnBuscarDataNaix = new JButton("🔍");
        btnBuscarDataNaix.setBounds(730, 110, 50, 30);
        btnBuscarDataNaix.setBackground(new Color(173, 216, 230)); // Blau cel
        btnBuscarDataNaix.setFocusPainted(false);
        frame.add(btnBuscarDataNaix);

        // Listener per al botó Buscar Data de Naixement
        btnBuscarDataNaix.addActionListener(e -> {
            String nom = txtBuscarNom.getText().trim();
            String nif = txtBuscarNIF.getText().trim();
            Date dataNaix = null;
            try {
                if (!txtBuscarDataNaix.getText().trim().isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    dataNaix = format.parse(txtBuscarDataNaix.getText().trim());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Format de data invàlid! El format correcte és dd-MM-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean ordenarPerCognom = chkOrdenarCognom.isSelected();
            aplicarFiltrosCombinados(nom, nif, dataNaix, categoria, ordenarPerCognom);
        });
    }

    private void buscarPerDataNaix(java.sql.Date dataNaix) {
        try {
            jugadorsFiltrats = persistencia.buscarPerDataNaixJugador(dataNaix); // Utilitzar el mètode de la capa de persistència
            modelTaulaJugadors.setRowCount(0); // Netejar la taula abans de mostrar els resultats

            for (Jugador jugador : jugadorsFiltrats) {
                modelTaulaJugadors.addRow(new Object[] {
                    jugador.getIdLegal(),
                    jugador.getNom(),
                    jugador.getCognoms(),
                    calcularEdat(jugador.getDataNaix()),
                    calcularCategoria(calcularEdat(jugador.getDataNaix())) // Categoria calculada
                });
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en buscar jugadors per data de naixement: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void filtrePerOrdenarCognom(JFrame frame) {
        // Afegir un label per l'ordenació per cognom
        JLabel lblOrdenarPerCognom = new JLabel("Ordenar per cognom:");
        lblOrdenarPerCognom.setBounds(600, 510, 150, 30);
        frame.add(lblOrdenarPerCognom);

        // Crear un JCheckBox per activar/desactivar l'ordenació
        chkOrdenarCognom = new JCheckBox(); // Inicializar la variable de clase
        chkOrdenarCognom.setBounds(750, 515, 20, 20);
        frame.add(chkOrdenarCognom);

        // Listener per al JCheckBox per ordenar els jugadors per cognom
        chkOrdenarCognom.addActionListener(e -> {
            String nom = txtBuscarNom.getText().trim();
            String nif = txtBuscarNIF.getText().trim();
            Date dataNaix = null;
            try {
                if (!txtBuscarDataNaix.getText().trim().isEmpty()) {
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    dataNaix = format.parse(txtBuscarDataNaix.getText().trim());
                }
            } catch (Exception ex) {
                // Mantener dataNaix como null si hay error
            }
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean ordenarPerCognom = chkOrdenarCognom.isSelected();
            aplicarFiltrosCombinados(nom, nif, dataNaix, categoria, ordenarPerCognom);
        });
    }

    private void buscarJugadorsOrdenatsPerCognom(boolean ordenarPerCognom) {
        try {
            List<Jugador> jugadors = persistencia.buscarJugadorsOrdenatsPerCognom(ordenarPerCognom); // Utilitzar la capa de persistència
            modelTaulaJugadors.setRowCount(0); // Netejar la taula abans de mostrar els resultats

            for (Jugador jugador : jugadors) {
                modelTaulaJugadors.addRow(new Object[] {
                    jugador.getIdLegal(),
                    jugador.getNom(),
                    jugador.getCognoms(),
                    calcularEdat(jugador.getDataNaix()),
                    calcularCategoria(calcularEdat(jugador.getDataNaix())) // Categoria calculada
                });
            }
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(null, "Error en ordenar jugadors per cognom: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void infoError(Throwable aux) {
        do {
            if (aux.getMessage() != null) {
                System.out.println("\t" + aux.getMessage());
            }
            aux = aux.getCause();
        } while (aux != null);
    }

    private void aplicarFiltrosCombinados(String nom, String nif, Date dataNaix, String categoria, boolean ordenarPerCognom) {
        try {
            // Comenzamos con la lista completa de jugadores
            jugadorsFiltrats = new ArrayList<>(jugadors);

            // Aplicar filtro por nombre
            if (nom != null && !nom.isEmpty()) {
                jugadorsFiltrats = jugadorsFiltrats.stream()
                    .filter(j -> j.getNom().toLowerCase().contains(nom.toLowerCase()))
                    .collect(Collectors.toList());
            }

            // Aplicar filtro por NIF
            if (nif != null && !nif.isEmpty()) {
                jugadorsFiltrats = jugadorsFiltrats.stream()
                    .filter(j -> j.getIdLegal().toLowerCase().contains(nif.toLowerCase()))
                    .collect(Collectors.toList());
            }

            // Aplicar filtro por fecha de nacimiento
            if (dataNaix != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dataStr = sdf.format(dataNaix);
                jugadorsFiltrats = jugadorsFiltrats.stream()
                    .filter(j -> sdf.format(j.getDataNaix()).equals(dataStr))
                    .collect(Collectors.toList());
            }

            // Aplicar filtro por categoría
            if (categoria != null && !categoria.equals("Totes")) {
                jugadorsFiltrats = jugadorsFiltrats.stream()
                    .filter(j -> calcularCategoria(calcularEdat(j.getDataNaix())).equals(categoria))
                    .collect(Collectors.toList());
            }

            // Aplicar ordenación por apellido si está activada
            if (ordenarPerCognom) {
                jugadorsFiltrats.sort(Comparator.comparing(Jugador::getCognoms));
            }

            // Actualizar la tabla
            actualizarTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al aplicar los filtros: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTabla() {
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

}
