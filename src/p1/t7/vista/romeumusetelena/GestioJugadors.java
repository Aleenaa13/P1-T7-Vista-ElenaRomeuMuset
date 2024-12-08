package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Jugador;

public class GestioJugadors {
    private IPersistencia persistencia; // Per accedir a la interfície de persistència
    private DefaultTableModel modelTaulaJugadors;

    public GestioJugadors(IPersistencia persistencia) {
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

        JTable taulaJugadors = new JTable(modelTaulaJugadors);
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

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBounds(250, 470, 100, 30);
        btnEditar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnEditar.setFocusPainted(false);
        frame.add(btnEditar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(400, 470, 100, 30);
        btnEliminar.setBackground(new Color(173, 216, 230)); // Blau cel
        btnEliminar.setFocusPainted(false);
        frame.add(btnEliminar);

    }

    private void carregarJugadors() {
        try {
            List<Jugador> jugadors = persistencia.obtenirTotsJugadors(); // Obtenim els jugadors de la BD
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
        JComboBox<String> comboCategoria = new JComboBox<>(categories);
        comboCategoria.setBounds(750, 470, 100, 30);
        frame.add(comboCategoria);

        // Listener pel JComboBox
        comboCategoria.addActionListener(e -> {
            String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
            if ("Totes".equals(categoriaSeleccionada)) {
                carregarJugadors(); // Tornar a carregar tots els jugadors
            } else {
                filtrarPerCategoria(categoriaSeleccionada); // Aplicar filtre
            }
        });
    }

    private void filtrarPerCategoria(String categoria) {
        try {
            List<Jugador> jugadors = persistencia.obtenirTotsJugadors(); // Obtenim tots els jugadors
            modelTaulaJugadors.setRowCount(0); // Netejar taula

            for (Jugador jugador : jugadors) {
                String categoriaJugador = calcularCategoria(calcularEdat(jugador.getDataNaix()));

                if (categoriaJugador.equals(categoria)) {
                    modelTaulaJugadors.addRow(new Object[]{
                        jugador.getIdLegal(),
                        jugador.getNom(),
                        jugador.getCognoms(),
                        calcularEdat(jugador.getDataNaix()),
                        categoriaJugador
                    });
                }
            }
        } catch (GestorBDEsportsException e) {
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

        JTextField txtBuscarNom = new JTextField();
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
            buscarPerNom(nom); // Cridar la funció buscarPerNom
        });
    }
    
    private void buscarPerNom(String nom) {
        try {
            List<Jugador> jugadors = persistencia.buscarNomJugador(nom); // Utilitzar el mètode de la capa de persistència
            modelTaulaJugadors.setRowCount(0); // Netejar la taula abans de mostrar els resultats

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
            JOptionPane.showMessageDialog(null, "Error en buscar jugadors pel nom: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrePerNIF(JFrame frame) {
        // Afegir un camp de text per cercar pel NIF
        JLabel lblBuscarNIF = new JLabel("NIF:");
        lblBuscarNIF.setBounds(270, 110, 50, 30);
        frame.add(lblBuscarNIF);

        JTextField txtBuscarNIF = new JTextField();
        txtBuscarNIF.setBounds(310, 110, 100, 30);
        frame.add(txtBuscarNIF);

        JButton btnBuscarNIF = new JButton("🔍");
        btnBuscarNIF.setBounds(420, 110, 50, 30);
        btnBuscarNIF.setBackground(new Color(173, 216, 230)); // Blau cel
        btnBuscarNIF.setFocusPainted(false);
        frame.add(btnBuscarNIF);

        // Listener per al botó Buscar NIF
        btnBuscarNIF.addActionListener(e -> {
            String nif = txtBuscarNIF.getText().trim();
            buscarPerNIF(nif); // Cridar la funció buscarPerNIF
        });
    }
    
    private void buscarPerNIF(String nif) {
        try {
            List<Jugador> jugadors = persistencia.buscarPerNIFJugador(nif); // Utilitzar el mètode de la capa de persistència
            modelTaulaJugadors.setRowCount(0); // Netejar la taula abans de mostrar els resultats

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
            JOptionPane.showMessageDialog(null, "Error en buscar jugadors pel NIF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void filtrePerDataNaix(JFrame frame) {
        // Afegir un camp de text per cercar per data de naixement
        JLabel lblBuscarDataNaix = new JLabel("DATA DE NAIXAMENT:");
        lblBuscarDataNaix.setBounds(480, 110, 140, 30);
        frame.add(lblBuscarDataNaix);

        JTextField txtBuscarDataNaix = new JTextField();
        txtBuscarDataNaix.setBounds(620, 110, 100, 30);
        frame.add(txtBuscarDataNaix);

        JButton btnBuscarDataNaix = new JButton("🔍");
        btnBuscarDataNaix.setBounds(730, 110, 50, 30);
        btnBuscarDataNaix.setBackground(new Color(173, 216, 230)); // Blau cel
        btnBuscarDataNaix.setFocusPainted(false);
        frame.add(btnBuscarDataNaix);

        // Listener per al botó Buscar Data de Naixement
        btnBuscarDataNaix.addActionListener(e -> {
            String dataNaixStr = txtBuscarDataNaix.getText().trim();
            if (!dataNaixStr.isEmpty()) {
                try {
                    // Validar que la data està en el format dd-MM-yyyy
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    format.setLenient(false); // Desactivar la flexibilitat en el format de data
                    Date date = format.parse(dataNaixStr);

                    // Convertir a java.sql.Date
                    java.sql.Date dataNaix = new java.sql.Date(date.getTime());

                    buscarPerDataNaix(dataNaix); // Cridar la funció buscarPerDataNaix
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Format de data invàlid! El format correcte és dd-MM-yyyy.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void buscarPerDataNaix(java.sql.Date dataNaix) {
        try {
            List<Jugador> jugadors = persistencia.buscarPerDataNaixJugador(dataNaix); // Utilitzar el mètode de la capa de persistència
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
            JOptionPane.showMessageDialog(null, "Error en buscar jugadors per data de naixement: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void filtrePerOrdenarCognom(JFrame frame) {
        // Afegir un label per l'ordenació per cognom
        JLabel lblOrdenarPerCognom = new JLabel("Ordenar per cognom:");
        lblOrdenarPerCognom.setBounds(600, 510, 150, 30);
        frame.add(lblOrdenarPerCognom);

        // Crear un JCheckBox per activar/desactivar l'ordenació
        JCheckBox chkOrdenarCognom = new JCheckBox();
        chkOrdenarCognom.setBounds(750, 515, 20, 20);
        frame.add(chkOrdenarCognom);

        // Listener per al JCheckBox per ordenar els jugadors per cognom
        chkOrdenarCognom.addActionListener(e -> {
            boolean ordenarPerCognom = chkOrdenarCognom.isSelected();
            buscarJugadorsOrdenatsPerCognom(ordenarPerCognom); // Cridar la funció per obtenir els jugadors ordenats
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



}
