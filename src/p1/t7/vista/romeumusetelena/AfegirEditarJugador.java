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

        // Títol centrat
        JLabel titol = new JLabel(jugador == null ? "AFEGIR JUGADOR" : "EDITAR JUGADOR", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        // Labels i camps de text
        int xLabel = 200, yLabel = 130, ampleLabel = 150, altLabel = 30;
        int xCamp = xLabel + ampleLabel + 10, ampleCamp = 200, altCamp = 30;
        int separacio = 40;

        JLabel labelNom = new JLabel("Nom:");
        labelNom.setBounds(xLabel, yLabel, ampleLabel, altLabel);
        frame.add(labelNom);

        JTextField campNom = new JTextField(jugador != null ? jugador.getNom() : "");
        campNom.setBounds(xCamp, yLabel, ampleCamp, altCamp);
        frame.add(campNom);

        JLabel labelCognoms = new JLabel("Cognoms:");
        labelCognoms.setBounds(xLabel, yLabel + separacio, ampleLabel, altLabel);
        frame.add(labelCognoms);

        JTextField campCognoms = new JTextField(jugador != null ? jugador.getCognoms() : "");
        campCognoms.setBounds(xCamp, yLabel + separacio, ampleCamp, altCamp);
        frame.add(campCognoms);

        JLabel labelSexe = new JLabel("Sexe:");
        labelSexe.setBounds(xLabel, yLabel + 2 * separacio, ampleLabel, altLabel);
        frame.add(labelSexe);

        JRadioButton radioDona = new JRadioButton("Dona", jugador != null && jugador.getSexe() == 'D');
        radioDona.setBounds(xCamp, yLabel + 2 * separacio, ampleCamp / 2, altCamp);

        JRadioButton radioHome = new JRadioButton("Home", jugador != null && jugador.getSexe() == 'H');
        radioHome.setBounds(xCamp + ampleCamp / 2, yLabel + 2 * separacio, ampleCamp / 2, altCamp);

        ButtonGroup grupSexe = new ButtonGroup();
        grupSexe.add(radioDona);
        grupSexe.add(radioHome);

        frame.add(radioDona);
        frame.add(radioHome);

        JLabel labelDataNaix = new JLabel("Data Naixament:");
        labelDataNaix.setBounds(xLabel, yLabel + 3 * separacio, ampleLabel, altLabel);
        frame.add(labelDataNaix);

        JTextField campDataNaix = new JTextField(jugador != null ? jugador.getDataNaix().toString() : ""); // S'assumeix format correcte
        campDataNaix.setBounds(xCamp, yLabel + 3 * separacio, ampleCamp, altCamp);
        frame.add(campDataNaix);

        JLabel labelIBAN = new JLabel("IBAN:");
        labelIBAN.setBounds(xLabel, yLabel + 4 * separacio, ampleLabel, altLabel);
        frame.add(labelIBAN);

        JTextField campIBAN = new JTextField(jugador != null ? jugador.getIBAN() : "");
        campIBAN.setBounds(xCamp, yLabel + 4 * separacio, ampleCamp, altCamp);
        frame.add(campIBAN);

        // Afegir camps per a l'adreça
        JLabel labelDireccio = new JLabel("Direcció:");
        labelDireccio.setBounds(xLabel, yLabel + 5 * separacio, ampleLabel, altLabel);
        frame.add(labelDireccio);

        JTextField campDireccio = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getDireccio() : "");
        campDireccio.setBounds(xCamp, yLabel + 5 * separacio, ampleCamp, altCamp);
        frame.add(campDireccio);

        JLabel labelPoblacio = new JLabel("Població:");
        labelPoblacio.setBounds(xLabel, yLabel + 6 * separacio, ampleLabel, altLabel);
        frame.add(labelPoblacio);

        JTextField campPoblacio = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getPoblacio() : "");
        campPoblacio.setBounds(xCamp, yLabel + 6 * separacio, ampleCamp, altCamp);
        frame.add(campPoblacio);

        JLabel labelCodiPostal = new JLabel("Codi Postal:");
        labelCodiPostal.setBounds(xLabel, yLabel + 7 * separacio, ampleLabel, altLabel);
        frame.add(labelCodiPostal);

        JTextField campCodiPostal = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getCodiPostal() : "");
        campCodiPostal.setBounds(xCamp, yLabel + 7 * separacio, ampleCamp, altCamp);
        frame.add(campCodiPostal);

        // Altres camps necessaris per a la creació del jugador
        JLabel labelFoto = new JLabel("Foto:");
        labelFoto.setBounds(xLabel, yLabel + 8 * separacio, ampleLabel, altLabel);
        frame.add(labelFoto);

        JTextField campFoto = new JTextField(jugador != null ? jugador.getFoto() : "");
        campFoto.setBounds(xCamp, yLabel + 8 * separacio, ampleCamp, altCamp);
        frame.add(campFoto);

        JLabel labelAnyFiRevisio = new JLabel("Any Fi Revisió:");
        labelAnyFiRevisio.setBounds(xLabel, yLabel + 9 * separacio, ampleLabel, altLabel);
        frame.add(labelAnyFiRevisio);

        JTextField campAnyFiRevisio = new JTextField(jugador != null ? String.valueOf(jugador.getAnyFiRevisioMedica()) : "2024");
        campAnyFiRevisio.setBounds(xCamp, yLabel + 9 * separacio, ampleCamp, altCamp);
        frame.add(campAnyFiRevisio);

        JLabel labelIdLegal = new JLabel("ID Legal:");
        labelIdLegal.setBounds(xLabel, yLabel + 10 * separacio, ampleLabel, altLabel);
        frame.add(labelIdLegal);

        JTextField campIdLegal = new JTextField(jugador != null ? jugador.getIdLegal() : "");
        campIdLegal.setBounds(xCamp, yLabel + 10 * separacio, ampleCamp, altCamp);
        frame.add(campIdLegal);

        // Botons Acció
        JButton botoGuardar = new JButton("Guardar");
        botoGuardar.setBounds(350, 500, 100, 40);
        frame.add(botoGuardar);

        JButton botoCancelar = new JButton("Cancelar");
        botoCancelar.setBounds(500, 500, 100, 40);
        frame.add(botoCancelar);

        // Variables per emmagatzemar el jugador actualitzat
       // final Jugador[] jugadorActualitzat = {jugador};

        // Variable per saber si el jugador ha estat guardat
        //final boolean[] jugadorGuardat = {false};
        
        
        // Lògica del botó Guardar
        botoGuardar.addActionListener(e -> {
            try {
                // Obtenir dades del formulari
                String nom = campNom.getText();
                String cognoms = campCognoms.getText();
                char sexe = radioDona.isSelected() ? 'D' : 'H';
                
                String dataNaixText = campDataNaix.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.setLenient(false); 
                Date dataNaix = sdf.parse(dataNaixText);

                String IBAN = campIBAN.getText();

                // Obtenir la informació d'adreça
                String direccio = campDireccio.getText();
                String poblacio = campPoblacio.getText();
                String codiPostal = campCodiPostal.getText();
                Adreca adreca = new Adreca(direccio, poblacio, codiPostal);

                // Altres camps necessaris
                String foto = campFoto.getText();
                int anyFiRevisioMedica = Integer.parseInt(campAnyFiRevisio.getText());
                String idLegal = campIdLegal.getText();

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
