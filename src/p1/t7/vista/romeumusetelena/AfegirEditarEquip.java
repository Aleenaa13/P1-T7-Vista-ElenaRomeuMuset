package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.Equip;
import p1.t6.model.romeumusetelena.GestorBDEsportsException;
import p1.t6.model.romeumusetelena.Temporada;
import p1.t6.model.romeumusetelena.TipusEquip;
import java.util.List;  
import javax.swing.table.DefaultTableModel;
import p1.t6.model.romeumusetelena.Membre;
import p1.t6.model.romeumusetelena.TipusMembre;

public class AfegirEditarEquip {
    
    public static void mostrarFormulari(Equip equip, IPersistencia persistencia) {
        JFrame frame = new JFrame("Gestió d'Equips");
        frame.setSize(900, 600);
        frame.setLayout(null);
        frame.setResizable(false); 
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color blancTrencat = new Color(245, 245, 245);
        frame.getContentPane().setBackground(blancTrencat);

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
            botonsMenu[i].setEnabled(false); // els desactivo per lògica
            frame.add(botonsMenu[i]);
        }
        
        //això és perquè si posa un titol o un altre 
        JLabel titol = new JLabel(equip == null ? "AFEGIR EQUIP" : "EDITAR EQUIP", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        int xLabelEsquerra = 50; 
        int yLabel = 180;  
        int ampleLabel = 150;
        int altLabel = 30;
        int ampleCamp = 200;
        int altCamp = 30;
        int separacio = 50;

        JLabel labelNom = new JLabel("Nom:");
        labelNom.setBounds(xLabelEsquerra, yLabel, ampleLabel, altLabel);
        frame.add(labelNom);
        String nomEquip = "";
        if (equip != null) {
            nomEquip = equip.getNom();
        }
        JTextField campNom = new JTextField(nomEquip);
        campNom.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel, ampleCamp, altCamp);
        frame.add(campNom);

        JLabel labelTipusEquip = new JLabel("Tipus equip:");
        labelTipusEquip.setBounds(xLabelEsquerra, yLabel + separacio, ampleLabel, altLabel);
        frame.add(labelTipusEquip);

        JRadioButton radioH = new JRadioButton("Masculí", equip != null && equip.getTipus() == TipusEquip.H);
        radioH.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + separacio, ampleCamp/3, altCamp);
        JRadioButton radioD = new JRadioButton("Femení", equip != null && equip.getTipus() == TipusEquip.D);
        radioD.setBounds(xLabelEsquerra + ampleLabel + 10 + ampleCamp/3, yLabel + separacio, ampleCamp/3, altCamp);
        JRadioButton radioM = new JRadioButton("Mixt", equip != null && equip.getTipus() == TipusEquip.M);
        radioM.setBounds(xLabelEsquerra + ampleLabel + 10 + 2*ampleCamp/3, yLabel + separacio, ampleCamp/3, altCamp);

        ButtonGroup grupTipusEquip = new ButtonGroup();
        grupTipusEquip.add(radioH); 
        grupTipusEquip.add(radioD); 
        grupTipusEquip.add(radioM);

        frame.add(radioH);
        frame.add(radioD);
        frame.add(radioM);

        String[] categories = {"Benjamí", "Aleví", "Infantil", "Cadet", "Junior", "Senior"};
        JComboBox<String> comboCategoria = new JComboBox<>(categories);
        JComboBox<String> comboTemporada = new JComboBox<>();

        JLabel labelCategoria = new JLabel("Categoria:");
        labelCategoria.setBounds(xLabelEsquerra, yLabel + 2*separacio, ampleLabel, altLabel);
        frame.add(labelCategoria);

        comboCategoria.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 2*separacio, ampleCamp, altCamp);
        frame.add(comboCategoria);  

        JLabel labelTemporada = new JLabel("Temporada:");
        labelTemporada.setBounds(xLabelEsquerra, yLabel + 3*separacio, ampleLabel, altLabel);
        frame.add(labelTemporada);

        comboTemporada.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 3*separacio, ampleCamp, altCamp);
        frame.add(comboTemporada);

        if (equip != null) {
            String categoria = switch (equip.getIdCategoria()) {
                case 1 -> "Benjamí";
                case 2 -> "Aleví";
                case 3 -> "Infantil";
                case 4 -> "Cadet";
                case 5 -> "Junior";
                case 6 -> "Senior";
                default -> "Desconeguda";
            };
            comboCategoria.setSelectedItem(categoria);
        } else {
            comboCategoria.setSelectedItem("Benjamí");  // assigno per defecte 
        }
        
        comboCategoria.addActionListener(e -> {
            String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
        });

        try {
            List<Temporada> temporades = persistencia.obtenirTotesTemporades();

            for (Temporada temporada : temporades) {
                comboTemporada.addItem(String.valueOf(temporada.getAny()));
            }

            if (equip != null) {
                comboTemporada.setSelectedItem(String.valueOf(equip.getAnyTemporada()));
            }

        } catch (GestorBDEsportsException ex) {
            JOptionPane.showMessageDialog(frame, "Error al obtenir les temporades: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        comboTemporada.addActionListener(e -> {
            String temporadaSeleccionada = (String) comboTemporada.getSelectedItem();
        });

        JButton botoGuardar = new JButton("Guardar");
        botoGuardar.setBounds(50, 450, 100, 40); 
        botoGuardar.setBackground(new Color(173, 216, 230)); 
        botoGuardar.setFocusPainted(false);
        frame.add(botoGuardar);

        JButton botoCancelar = new JButton("Cancelar");
        botoCancelar.setBounds(200, 450, 100, 40);
        botoCancelar.setBackground(new Color(173, 216, 230)); 
        botoCancelar.setFocusPainted(false);
        frame.add(botoCancelar);

        JPanel panelTaula = new JPanel();
        panelTaula.setBounds(500, 180, 300, 150); 
        panelTaula.setLayout(new BorderLayout());
        
        DefaultTableModel modelTaula = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        modelTaula.addColumn("Nom");
        modelTaula.addColumn("Cognoms");
        modelTaula.addColumn("Tipus");
        
        JTable taulaMembres = new JTable(modelTaula);
        JScrollPane scrollPane = new JScrollPane(taulaMembres);
        panelTaula.add(scrollPane);
        
        JLabel titolTaula = new JLabel("MEMBRES DE L'EQUIP", SwingConstants.CENTER);
        titolTaula.setBounds(500, 150, 300, 30);  
        titolTaula.setFont(new Font("SansSerif", Font.BOLD, 14));
        titolTaula.setForeground(new Color(70, 130, 180));
        frame.add(titolTaula);
        
        JButton botoGestioMembres = new JButton("Gestionar Membres");
        botoGestioMembres.setBounds(575, 340, 150, 35);  
        botoGestioMembres.setBackground(new Color(173, 216, 230));
        botoGestioMembres.setFocusPainted(false);
        frame.add(botoGestioMembres);

        botoGestioMembres.addActionListener(e -> {
            if (equip != null) {
                frame.setVisible(false); 
                GestioMembres.mostrarFormulari(equip, persistencia, modelTaula);
            } else {
                JOptionPane.showMessageDialog(frame,"Cal guardar l'equip primer per gestionar els membres","Avís",JOptionPane.WARNING_MESSAGE);
            }
        });
        
        //això és pq em tanqui la finestra
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                frame.dispose();
                new GestioEquips(persistencia);
            }
        });

        frame.add(panelTaula);
        
        if (equip != null) {
            try {
                List<Membre> membres = persistencia.obtenirMembresDEquip(equip.getId());
                for (Membre membre : membres) {
                    String tipus;
                    if (membre.getTipus() == TipusMembre.TITULAR) {
                        tipus = "Titular";
                    } else {
                        tipus = "Convidat"; 
                    }
                    modelTaula.addRow(new Object[]{
                        membre.getJugador().getNom(),
                        membre.getJugador().getCognoms(),
                        tipus
                    });
                }
            } catch (GestorBDEsportsException ex) {
                JOptionPane.showMessageDialog(frame,"Error al carregar els membres: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }

        frame.setVisible(true);

        botoGuardar.addActionListener(e -> {
            try {
                String nom = campNom.getText();

                TipusEquip tipus;
                if (radioH.isSelected()) {
                    tipus = TipusEquip.H;
                } else if (radioD.isSelected()) {
                    tipus = TipusEquip.D;
                } else {
                    tipus = TipusEquip.M;
                }
                
                String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
                int idCategoria = switch (categoriaSeleccionada) {
                    case "Benjamí" -> 1;
                    case "Aleví" -> 2;
                    case "Infantil" -> 3;
                    case "Cadet" -> 4;
                    case "Junior" -> 5;
                    case "Senior" -> 6;
                    default -> 0; 
                };

                int anyTemporada = Integer.parseInt((String) comboTemporada.getSelectedItem());

                // Aqui mira si un equip existeiz o es nou per psoar les dades
                if (equip != null) {
                    equip.setNom(nom);
                    equip.setTipus(tipus);
                    equip.setIdCategoria(idCategoria);
                    equip.setAnyTemporada(anyTemporada);

                    persistencia.modificarEquip(equip);
                } else {
                    Equip nouEquip = new Equip(nom, tipus, anyTemporada, idCategoria);
                    persistencia.afegirEquip(nouEquip);
                }

                persistencia.confirmarCanvis();

                JOptionPane.showMessageDialog(frame, "Equip guardat amb èxit!");

                frame.dispose();
                new GestioEquips(persistencia);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        botoCancelar.addActionListener(e -> {
            frame.dispose();
            new GestioEquips(persistencia);
        });

        frame.setVisible(true);

        if (equip != null) {
            comboTemporada.setEnabled(false);
            comboCategoria.setEnabled(false);
            radioH.setEnabled(false);
            radioD.setEnabled(false);
            radioM.setEnabled(false);
            
            String nomOriginal = equip.getNom();
            
            botoGuardar.setEnabled(false);
            
            //això ho he buscat, és per habilitar o deshabilitar el botó si han canviat el nom o no
            campNom.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    botoGuardar.setEnabled(!campNom.getText().equals(nomOriginal));
                }
            });
        }

    }
    
}
