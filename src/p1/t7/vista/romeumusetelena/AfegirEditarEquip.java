package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.Adreca;
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
        // Crear el frame
        JFrame frame = new JFrame("Gestió d'Equips");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false); // Finestra no redimensionable
        frame.setLocationRelativeTo(null); // Centrar la finestra

        // Fons blanc trencat
        Color blancTrencat = new Color(245, 245, 245);
        frame.getContentPane().setBackground(blancTrencat);
        
        // Crear el menú amb els botons desactivats
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
            botonsMenu[i].setEnabled(false); // Desactivar els botons perquè no siguin clicables
            frame.add(botonsMenu[i]);
        }
        
        // Títol centrat
        JLabel titol = new JLabel(equip == null ? "AFEGIR EQUIP" : "EDITAR EQUIP", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        // Labels i camps de text
        int xLabelEsquerra = 30, xLabelDreta = 500;  // Separació entre les dues columnes
        int yLabel = 130, ampleLabel = 150, altLabel = 30;
        int ampleCamp = 200, altCamp = 30;
        int separacio = 60;

        // Primer grup de camps a l'esquerra
        JLabel labelNom = new JLabel("Nom:");
        labelNom.setBounds(xLabelEsquerra, yLabel, ampleLabel, altLabel);
        frame.add(labelNom);
        JTextField campNom = new JTextField(equip != null ? equip.getNom() : "");
        campNom.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel, ampleCamp, altCamp);
        frame.add(campNom);

        // Afegir l'etiqueta "Tipus d'Equip"
        JLabel labelTipusEquip = new JLabel("Tipus equip:");
        labelTipusEquip.setBounds(xLabelEsquerra, yLabel + 2 * separacio, ampleLabel, altLabel);
        frame.add(labelTipusEquip);

        // Crear els botons d'opció per al tipus d'equip
        JRadioButton radioH = new JRadioButton("Home", equip != null && equip.getTipus() == TipusEquip.H);
        radioH.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 2 * separacio, ampleCamp / 3, altCamp);
        JRadioButton radioD = new JRadioButton("Dona", equip != null && equip.getTipus() == TipusEquip.D);
        radioD.setBounds(xLabelEsquerra + ampleLabel + 10 + ampleCamp / 3, yLabel + 2 * separacio, ampleCamp / 3, altCamp);
        JRadioButton radioM = new JRadioButton("Mixt", equip != null && equip.getTipus() == TipusEquip.M);
        radioM.setBounds(xLabelEsquerra + ampleLabel + 10 + 2 * ampleCamp / 3, yLabel + 2 * separacio, ampleCamp / 3, altCamp);

        // Agrupar els botons d'opció per garantir que només un es pugui seleccionar a la vegada
        ButtonGroup grupTipusEquip = new ButtonGroup();
        grupTipusEquip.add(radioH);
        grupTipusEquip.add(radioD);
        grupTipusEquip.add(radioM);

        // Afegir els botons al frame
        frame.add(radioH);
        frame.add(radioD);
        frame.add(radioM);

        // Crear el JComboBox per la categoria
        String[] categories = {"Benjamí", "Aleví", "Infantil", "Cadet", "Junior", "Senior"};
        JComboBox<String> comboCategoria = new JComboBox<>(categories);
        comboCategoria.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 4 * separacio, ampleCamp, altCamp);
        frame.add(comboCategoria);

        // Establir la categoria per defecte si no s'ha passat un equip
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
            comboCategoria.setSelectedItem("Benjamí");  // Categoria per defecte
        }
         
        // Afegir l'ActionListener per filtrar per categoria
        comboCategoria.addActionListener(e -> {
            String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
            // Aquí pots implementar la lògica de filtratge per categoria si cal
        });


        // Crear el JComboBox per la temporada
        JComboBox<String> comboTemporada = new JComboBox<>();
        comboTemporada.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 5 * separacio, ampleCamp, altCamp);
        frame.add(comboTemporada);

        try {
            // Obtenir totes les temporades des de la base de dades
            List<Temporada> temporades = persistencia.obtenirTotesTemporades();  // Mètode que recupera totes les temporades de la base de dades

            // Afegir totes les temporades al combo (només el valor que vols mostrar, potser l'any)
            for (Temporada temporada : temporades) {
                comboTemporada.addItem(String.valueOf(temporada.getAny()));  // Suposant que Temporada té un mètode getAny() per obtenir l'any
            }

            // Establir la temporada actual si es passa un equip
            if (equip != null) {
                comboTemporada.setSelectedItem(String.valueOf(equip.getAnyTemporada()));
            }

        } catch (GestorBDEsportsException ex) {
            JOptionPane.showMessageDialog(frame, "Error al obtenir les temporades: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Afegir l'ActionListener per filtrar per temporada
        comboTemporada.addActionListener(e -> {
            String temporadaSeleccionada = (String) comboTemporada.getSelectedItem();
            // Aquí pots implementar la lògica de filtratge per temporada si cal
        });

        
        // Botons Acció
        JButton botoGuardar = new JButton("Guardar");
        botoGuardar.setBounds(50, 500, 100, 40);
        botoGuardar.setBackground(new Color(173, 216, 230)); // Blau cel
        botoGuardar.setFocusPainted(false);
        frame.add(botoGuardar);

        JButton botoCancelar = new JButton("Cancelar");
        botoCancelar.setBounds(200, 500, 100, 40);
        botoCancelar.setBackground(new Color(173, 216, 230)); // Blau cel
        botoCancelar.setFocusPainted(false);
        frame.add(botoCancelar);

        // Mostrar el frame
        frame.setVisible(true);

        
        // Lògica del botó Guardar
        botoGuardar.addActionListener(e -> {
            try {
                // Obtenir dades del formulari
                String nom = campNom.getText();

                // Obtenir el tipus d'equip seleccionat
                TipusEquip tipus = radioH.isSelected() ? TipusEquip.H : (radioD.isSelected() ? TipusEquip.D : TipusEquip.M);

                // Obtenir la categoria seleccionada
                String categoriaSeleccionada = (String) comboCategoria.getSelectedItem();
                int idCategoria = switch (categoriaSeleccionada) {
                    case "Benjamí" -> 1;
                    case "Aleví" -> 2;
                    case "Infantil" -> 3;
                    case "Cadet" -> 4;
                    case "Junior" -> 5;
                    case "Senior" -> 6;
                    default -> 0; // Valor per defecte en cas que no es trobi
                };

                // Obtenir la temporada seleccionada
                int anyTemporada = Integer.parseInt((String) comboTemporada.getSelectedItem());

                // Comprovar si és un equip existent o un de nou
                if (equip != null) {
                    // Actualitzar les dades de l'equip existent
                    equip.setNom(nom);
                    equip.setTipus(tipus);
                    equip.setIdCategoria(idCategoria);
                    equip.setAnyTemporada(anyTemporada);

                    // Modificar l'equip a la base de dades
                    persistencia.modificarEquip(equip);
                } else {
                    // Crear un nou equip
                    Equip nouEquip = new Equip(nom, tipus, anyTemporada, idCategoria);
                    persistencia.afegirEquip(nouEquip);
                }

                // Confirmar els canvis a la base de dades
                persistencia.confirmarCanvis();

                // Mostrar un missatge de confirmació
                JOptionPane.showMessageDialog(frame, "Equip guardat amb èxit!");

                // Tancar la finestra després de desar
                frame.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        // Lògica del botó Cancel·lar
        botoCancelar.addActionListener(e -> frame.dispose());

        // Mostrar la finestra
        frame.setVisible(true);


    }
    
    
    
    private static void infoError(Throwable aux) {
        do {
            if (aux.getMessage() != null) {
                System.out.println("\t" + aux.getMessage());
            }
            aux = aux.getCause();
        } while (aux != null);
    }
}
