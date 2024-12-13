package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.Adreca;
import p1.t6.model.romeumusetelena.Jugador;

public class AfegirEditarJugador {

    public static void mostrarFormulari(Jugador jugador, IPersistencia persistencia) {
        // Crear el frame
        JFrame frame = new JFrame("Gestió de Jugadors");
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
        JLabel titol = new JLabel(jugador == null ? "AFEGIR JUGADOR" : "EDITAR JUGADOR", SwingConstants.CENTER);
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
        JTextField campNom = new JTextField(jugador != null ? jugador.getNom() : "");
        campNom.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel, ampleCamp, altCamp);
        frame.add(campNom);

        JLabel labelCognoms = new JLabel("Cognoms:");
        labelCognoms.setBounds(xLabelEsquerra, yLabel + separacio, ampleLabel, altLabel);
        frame.add(labelCognoms);
        JTextField campCognoms = new JTextField(jugador != null ? jugador.getCognoms() : "");
        campCognoms.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + separacio, ampleCamp, altCamp);
        frame.add(campCognoms);

        JLabel labelSexe = new JLabel("Sexe:");
        labelSexe.setBounds(xLabelEsquerra, yLabel + 2 * separacio, ampleLabel, altLabel);
        frame.add(labelSexe);
        JRadioButton radioDona = new JRadioButton("Dona", jugador != null && jugador.getSexe() == 'D');
        radioDona.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 2 * separacio, ampleCamp / 2, altCamp);
        JRadioButton radioHome = new JRadioButton("Home", jugador != null && jugador.getSexe() == 'H');
        radioHome.setBounds(xLabelEsquerra + ampleLabel + 10 + ampleCamp / 2, yLabel + 2 * separacio, ampleCamp / 2, altCamp);
        ButtonGroup grupSexe = new ButtonGroup();
        grupSexe.add(radioDona);
        grupSexe.add(radioHome);
        frame.add(radioDona);
        frame.add(radioHome);

        JLabel labelDataNaix = new JLabel("Data Naixament:");
        labelDataNaix.setBounds(xLabelEsquerra, yLabel + 3 * separacio, ampleLabel, altLabel);
        frame.add(labelDataNaix);

        // Formatar la data en el format dd-MM-yyyy
        String dataNaixText = jugador != null ? new SimpleDateFormat("dd-MM-yyyy").format(jugador.getDataNaix()) : "";

        // Assignar la data al camp de text
        JTextField campDataNaix = new JTextField(dataNaixText);
        campDataNaix.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 3 * separacio, ampleCamp, altCamp);
        frame.add(campDataNaix);


        JLabel labelIdLegal = new JLabel("ID Legal:");
        labelIdLegal.setBounds(xLabelEsquerra, yLabel + 4 * separacio, ampleLabel, altLabel);
        frame.add(labelIdLegal);
        JTextField campIdLegal = new JTextField(jugador != null ? jugador.getIdLegal() : "");
        campIdLegal.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 4 * separacio, ampleCamp, altCamp);
        frame.add(campIdLegal);

        // Segon grup de camps a la dreta
        JLabel labelIBAN = new JLabel("IBAN:");
        labelIBAN.setBounds(xLabelDreta, yLabel, ampleLabel, altLabel);
        frame.add(labelIBAN);
        JTextField campIBAN = new JTextField(jugador != null ? jugador.getIBAN() : "");
        campIBAN.setBounds(xLabelDreta + ampleLabel + 10, yLabel, ampleCamp, altCamp);
        frame.add(campIBAN);

        JLabel labelDireccio = new JLabel("Direcció:");
        labelDireccio.setBounds(xLabelDreta, yLabel + separacio, ampleLabel, altLabel);
        frame.add(labelDireccio);
        JTextField campDireccio = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getDireccio() : "");
        campDireccio.setBounds(xLabelDreta + ampleLabel + 10, yLabel + separacio, ampleCamp, altCamp);
        frame.add(campDireccio);

        JLabel labelPoblacio = new JLabel("Població:");
        labelPoblacio.setBounds(xLabelDreta, yLabel + 2 * separacio, ampleLabel, altLabel);
        frame.add(labelPoblacio);
        JTextField campPoblacio = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getPoblacio() : "");
        campPoblacio.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 2 * separacio, ampleCamp, altCamp);
        frame.add(campPoblacio);

        JLabel labelCodiPostal = new JLabel("Codi Postal:");
        labelCodiPostal.setBounds(xLabelDreta, yLabel + 3 * separacio, ampleLabel, altLabel);
        frame.add(labelCodiPostal);
        JTextField campCodiPostal = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getCodiPostal() : "");
        campCodiPostal.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 3 * separacio, ampleCamp, altCamp);
        frame.add(campCodiPostal);

        JLabel labelFoto = new JLabel("Foto:");
        labelFoto.setBounds(xLabelDreta, yLabel + 4 * separacio, ampleLabel, altLabel);
        frame.add(labelFoto);
        JTextField campFoto = new JTextField(jugador != null ? jugador.getFoto() : "");
        campFoto.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 4 * separacio, ampleCamp, altCamp);
        frame.add(campFoto);

        JLabel labelAnyFiRevisio = new JLabel("Any Fi Revisió:");
        labelAnyFiRevisio.setBounds(xLabelDreta, yLabel + 5 * separacio, ampleLabel, altLabel);
        frame.add(labelAnyFiRevisio);
        JTextField campAnyFiRevisio = new JTextField(jugador != null ? String.valueOf(jugador.getAnyFiRevisioMedica()) : "2024");
        campAnyFiRevisio.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 5 * separacio, ampleCamp, altCamp);
        frame.add(campAnyFiRevisio);

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
        
        JLabel errorNom = new JLabel("Exemple: Joan");
        errorNom.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 22, ampleCamp, altLabel);
        errorNom.setForeground(Color.RED);
        errorNom.setVisible(false); // Inicialment ocult
        frame.add(errorNom);

        JLabel errorCognoms = new JLabel("Exemple: Garcia");
        errorCognoms.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + separacio + altLabel + 12, ampleCamp, altLabel);
        errorCognoms.setForeground(Color.RED);
        errorCognoms.setVisible(false);
        frame.add(errorCognoms);

        JLabel errorIBAN = new JLabel("Exemple: ES1234567890123456789012");
        errorIBAN.setBounds(xLabelDreta + ampleLabel -10 , yLabel + 30, ampleCamp, altLabel);
        errorIBAN.setForeground(Color.RED);
        errorIBAN.setVisible(false);
        frame.add(errorIBAN);

        JLabel errorCodiPostal = new JLabel("Exemple: 08001");
        errorCodiPostal.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 3 * separacio + altLabel , ampleCamp, altLabel);
        errorCodiPostal.setForeground(Color.RED);
        errorCodiPostal.setVisible(false);
        frame.add(errorCodiPostal);

        JLabel errorID = new JLabel("ID incorrecte. Exemple: 54825311Z");
        errorID.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 4 * separacio + 23, ampleCamp, altLabel);
        errorID.setForeground(Color.RED);
        errorID.setVisible(false); // Inicialment ocult
        frame.add(errorID);
        
        // Lògica del botó Guardar
        botoGuardar.addActionListener(e -> {
            try {
                // Obtenir dades del formulari
                String nom = campNom.getText();
                if (nom.matches(".*\\d.*")) { // Comprovem si el nom té números
                    errorNom.setVisible(true);
                } else {
                    errorNom.setVisible(false);
                }
                
                String cognoms = campCognoms.getText();
                if (cognoms.matches(".*\\d.*")) { // Comprovem si els cognoms tenen números
                    errorCognoms.setVisible(true);
                } else {
                    errorCognoms.setVisible(false);
                }
        
                char sexe = radioDona.isSelected() ? 'D' : 'H';
                
                String dataNaixTextCamp = campDataNaix.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.setLenient(false); 
                Date dataNaix = sdf.parse(dataNaixTextCamp);

                String IBAN = campIBAN.getText();
                if (IBAN.length() != 24) {
                    errorIBAN.setVisible(true);
                } else {
                    errorIBAN.setVisible(false);
                }

                // Obtenir la informació d'adreça
                String direccio = campDireccio.getText();
                String poblacio = campPoblacio.getText();
                
                String codiPostal = campCodiPostal.getText();
                if (!codiPostal.matches("\\d{5}")) { // Només 5 dígits
                    errorCodiPostal.setVisible(true);
                } else {
                    errorCodiPostal.setVisible(false);
                }
                
                Adreca adreca = new Adreca(direccio, poblacio, codiPostal);

                // Altres camps necessaris
                String foto = campFoto.getText();
                int anyFiRevisioMedica = Integer.parseInt(campAnyFiRevisio.getText());
                
                String idLegal = campIdLegal.getText();
                if (!idLegal.matches("\\d+")) { // Comprovem que l'ID sigui només numèric
                    errorID.setVisible(true);
                } else {
                    errorID.setVisible(false);
                }

                if(jugador != null){
                    jugador.setAdreca(adreca);
                    jugador.setAnyFiRevisioMedica(anyFiRevisioMedica);
                    jugador.setCognoms(cognoms);
                    jugador.setFoto(foto);
                    jugador.setIBAN(IBAN);
                    jugador.setNom(nom);
                    jugador.setSexe(sexe);
                    jugador.setDataNaix(dataNaix);
                    jugador.setIdLegal(idLegal);
                    
                    persistencia.modificarJugador(jugador);
                    
                }else{
                    // Actualitzar el jugador amb les dades del formulari
                  //  System.out.println("aqui");
                    Jugador j = new Jugador(nom, cognoms, adreca, foto, anyFiRevisioMedica, IBAN, idLegal, dataNaix, sexe);

                    persistencia.afegirJugador( new Jugador(nom, cognoms, adreca, foto, anyFiRevisioMedica, IBAN, idLegal, dataNaix, sexe));
                    
                }
                persistencia.confirmarCanvis();
                

                // Marcar que el jugador ha estat guardat
                //jugadorGuardat[0] = true;
                JOptionPane.showMessageDialog(frame, "Jugador guardat amb èxit!");

                // Tancar la finestra després de desar
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
               // infoError(ex);
            }
        });

        // Lògica del botó Cancel·lar
        botoCancelar.addActionListener(e -> frame.dispose());

        // Mostrar la finestra
        frame.setVisible(true);

        // Esperar fins que la finestra es tanqui (els esdeveniments es gestionen dins d'ActionListener)
//        while (frame.isVisible()) {
//            try {
//                Thread.sleep(100); // Esperem fins que la finestra es tanqui
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // Retornar el jugador actualitzat només si ha estat guardat
//        if (jugadorGuardat[0]) {
//            return jugadorActualitzat[0];
//        } else {
//            return null;
//        }
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
