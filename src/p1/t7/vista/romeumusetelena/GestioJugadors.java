package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        // Carregar dades de la base de dades
        carregarJugadors();

        // Mostrar el frame
        frame.setVisible(true);
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
                    jugador.getCategoria() // Categoria calculada a la classe Jugador
                });
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
}
