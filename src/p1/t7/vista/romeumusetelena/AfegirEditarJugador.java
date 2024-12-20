package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.Adreca;
import p1.t6.model.romeumusetelena.Jugador;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class AfegirEditarJugador {

    public static void mostrarFormulari(Jugador jugador, IPersistencia persistencia) {
        JFrame frame = new JFrame("Gestió de Jugadors");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false); 
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
            botonsMenu[i].setEnabled(false); // igual que a equips
            frame.add(botonsMenu[i]);
        }
        
        JLabel titol = new JLabel(jugador == null ? "AFEGIR JUGADOR" : "EDITAR JUGADOR", SwingConstants.CENTER);
        titol.setBounds(0, 60, 900, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        frame.add(titol);

        int xLabelEsquerra = 30, xLabelDreta = 500;
        int yLabel = 130, ampleLabel = 150, altLabel = 30;
        int ampleCamp = 200, altCamp = 30;
        int separacio = 60;
        
        //tots aquests estan a l'esquerra
         JLabel labelNom = new JLabel("Nom:");
        labelNom.setBounds(xLabelEsquerra, yLabel, ampleLabel, altLabel);
        frame.add(labelNom);
        String nomJugador = "";
        if (jugador != null) {
            nomJugador = jugador.getNom();
        }
        JTextField campNom = new JTextField(nomJugador);
        campNom.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel, ampleCamp, altCamp);
        frame.add(campNom);

        JLabel labelCognoms = new JLabel("Cognoms:");
        labelCognoms.setBounds(xLabelEsquerra, yLabel + separacio, ampleLabel, altLabel);
        frame.add(labelCognoms);
        String cognomsJugador = "";
        if (jugador != null) {
            cognomsJugador = jugador.getCognoms();
        }
        JTextField campCognoms = new JTextField(cognomsJugador);
        campCognoms.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + separacio, ampleCamp, altCamp);
        frame.add(campCognoms);
        
        
        JLabel labelSexe = new JLabel("Sexe:");
        labelSexe.setBounds(xLabelEsquerra, yLabel + 2 * separacio, ampleLabel, altLabel);
        frame.add(labelSexe);

        JRadioButton radioDona = new JRadioButton("Dona");
        JRadioButton radioHome = new JRadioButton("Home");

        if (jugador != null) {
            radioDona.setSelected(jugador.getSexe() == 'D');
            radioHome.setSelected(jugador.getSexe() == 'H');
        } else {
            radioDona.setSelected(false);
            radioHome.setSelected(false);
        }

        radioDona.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 2 * separacio, ampleCamp / 2, altCamp);
        radioHome.setBounds(xLabelEsquerra + ampleLabel + 10 + ampleCamp / 2, yLabel + 2 * separacio, ampleCamp / 2, altCamp);
        ButtonGroup grupSexe = new ButtonGroup();
        
        grupSexe.add(radioDona);
        grupSexe.add(radioHome);
        frame.add(radioDona);
        frame.add(radioHome);

        JLabel labelDataNaix = new JLabel("Data Naixament:");
        labelDataNaix.setBounds(xLabelEsquerra, yLabel + 3 * separacio, ampleLabel, altLabel);
        frame.add(labelDataNaix);

        String dataNaixText = "";
        if (jugador != null) {
            dataNaixText = new SimpleDateFormat("dd-MM-yyyy").format(jugador.getDataNaix());
        }
        
        JTextField campDataNaix = new JTextField(dataNaixText);
        campDataNaix.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 3 * separacio, ampleCamp, altCamp);
        frame.add(campDataNaix);


        JLabel labelIdLegal = new JLabel("ID Legal:");
        labelIdLegal.setBounds(xLabelEsquerra, yLabel + 4 * separacio, ampleLabel, altLabel);
        frame.add(labelIdLegal);
        JTextField campIdLegal = new JTextField(jugador != null ? jugador.getIdLegal() : ""); //això ho deixo aixi pq em dona error i va millor que el if
        campIdLegal.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 4 * separacio, ampleCamp, altCamp);
        frame.add(campIdLegal);

        // Segon grup de camps a la dreta
        JLabel labelIBAN = new JLabel("IBAN:");
        labelIBAN.setBounds(xLabelDreta, yLabel, ampleLabel, altLabel);
        frame.add(labelIBAN);
        JTextField campIBAN = new JTextField(jugador != null ? jugador.getIBAN() : "");//igual
        campIBAN.setBounds(xLabelDreta + ampleLabel + 10, yLabel, ampleCamp, altCamp);
        frame.add(campIBAN);

        JLabel labelDireccio = new JLabel("Direcció:");
        labelDireccio.setBounds(xLabelDreta, yLabel + separacio, ampleLabel, altLabel);
        frame.add(labelDireccio);
        JTextField campDireccio = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getDireccio() : "");//igual
        campDireccio.setBounds(xLabelDreta + ampleLabel + 10, yLabel + separacio, ampleCamp, altCamp);
        frame.add(campDireccio);

        JLabel labelPoblacio = new JLabel("Població:");
        labelPoblacio.setBounds(xLabelDreta, yLabel + 2 * separacio, ampleLabel, altLabel);
        frame.add(labelPoblacio);
        JTextField campPoblacio = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getPoblacio() : "");//igual
        campPoblacio.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 2 * separacio, ampleCamp, altCamp);
        frame.add(campPoblacio);

        JLabel labelCodiPostal = new JLabel("Codi Postal:");
        labelCodiPostal.setBounds(xLabelDreta, yLabel + 3 * separacio, ampleLabel, altLabel);
        frame.add(labelCodiPostal);
        JTextField campCodiPostal = new JTextField(jugador != null && jugador.getAdreca() != null ? jugador.getAdreca().getCodiPostal() : "");//igual
        campCodiPostal.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 3 * separacio, ampleCamp, altCamp);
        frame.add(campCodiPostal);

        JLabel labelFoto = new JLabel("Foto:");
        labelFoto.setBounds(xLabelDreta, yLabel + 4 * separacio, ampleLabel, altLabel);
        frame.add(labelFoto);
        
        JPanel panelFoto = new JPanel();
        panelFoto.setLayout(null);
        panelFoto.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 4 * separacio, 150, 150); 
        panelFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelFoto.setBackground(Color.WHITE);
        frame.add(panelFoto);

        JLabel labelPreviewFoto = new JLabel();
        labelPreviewFoto.setBounds(0, 0, 150, 150);
        labelPreviewFoto.setHorizontalAlignment(SwingConstants.CENTER);
        panelFoto.add(labelPreviewFoto);

        JTextField campFoto = new JTextField(jugador != null ? jugador.getFoto() : "");
        campFoto.setVisible(false);

        if (jugador != null && !jugador.getFoto().isEmpty()) {
            try {
                ImageIcon imageIcon = new ImageIcon(jugador.getFoto());
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                labelPreviewFoto.setIcon(new ImageIcon(image));
            } catch (Exception e) {
                labelPreviewFoto.setText("No es pot carregar la imatge");
            }
        } else {
            labelPreviewFoto.setText("Fes clic per seleccionar");
        }
        
        //això també ho he buscat pq sino anava a deixar una string
        panelFoto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Imatges", "jpg", "jpeg", "png", "gif"));
                
                if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        
                        campFoto.setText(selectedFile.getAbsolutePath());
                        
                        ImageIcon imageIcon = new ImageIcon(selectedFile.getPath());
                        Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        labelPreviewFoto.setIcon(new ImageIcon(image));
                        labelPreviewFoto.setText(""); 
                        
                        panelFoto.setBackground(Color.WHITE);
                    } catch (Exception ex) {
                        labelPreviewFoto.setText("Error al carregar la imatge");
                        labelPreviewFoto.setIcon(null);
                    }
                }
            }
        });

        JLabel labelAnyFiRevisio = new JLabel("Any Fi Revisió:");
        labelAnyFiRevisio.setBounds(xLabelEsquerra, yLabel + 5 * separacio, ampleLabel, altLabel);
        frame.add(labelAnyFiRevisio);
        
        int anyActual = Calendar.getInstance().get(Calendar.YEAR);
        JTextField campAnyFiRevisio = new JTextField(jugador != null ? String.valueOf(jugador.getAnyFiRevisioMedica()) : String.valueOf(anyActual));
        campAnyFiRevisio.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 5 * separacio, ampleCamp, altCamp);
        frame.add(campAnyFiRevisio);

        // Botons
        JButton botoGuardar = new JButton("Guardar");
        botoGuardar.setBounds(50, 500, 100, 40);
        botoGuardar.setBackground(new Color(173, 216, 230)); 
        botoGuardar.setFocusPainted(false);
        frame.add(botoGuardar);

        JButton botoCancelar = new JButton("Cancelar");
        botoCancelar.setBounds(200, 500, 100, 40);
        botoCancelar.setBackground(new Color(173, 216, 230));
        botoCancelar.setFocusPainted(false);
        frame.add(botoCancelar);

        frame.setVisible(true);
        
        //tot això son pels camps d'error
        JLabel errorNom = new JLabel("Mínim 2 caràcters i sense números");
        errorNom.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 27, ampleCamp, altLabel);
        errorNom.setForeground(Color.RED);
        errorNom.setVisible(false); 
        frame.add(errorNom);

        JLabel errorCognoms = new JLabel("Mínim 2 caràcters i sense números");
        errorCognoms.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + separacio + altLabel, ampleCamp, altLabel);
        errorCognoms.setForeground(Color.RED);
        errorCognoms.setVisible(false);
        frame.add(errorCognoms);
        
        JLabel errorSexe = new JLabel("S'ha de seleccionar una opció");
        errorSexe.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 2 * separacio + 27, ampleCamp, altLabel);
        errorSexe.setForeground(Color.RED);
        errorSexe.setVisible(false);
        frame.add(errorSexe);
        
        JLabel errorIBAN = new JLabel("Exemple: ES1234567890123456789012");
        errorIBAN.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 30, ampleCamp + 30, altLabel);
        errorIBAN.setForeground(Color.RED);
        errorIBAN.setVisible(false);
        frame.add(errorIBAN);

        JLabel errorCodiPostal = new JLabel("Exemple: 08001");
        errorCodiPostal.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 3 * separacio + altLabel , ampleCamp, altLabel);
        errorCodiPostal.setForeground(Color.RED);
        errorCodiPostal.setVisible(false);
        frame.add(errorCodiPostal);

        JLabel errorID = new JLabel("ID incorrecte. Exemple: 54825311Z");
        errorID.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 4 * separacio + 27, ampleCamp, altLabel);
        errorID.setForeground(Color.RED);
        errorID.setVisible(false); 
        frame.add(errorID);
        
        JLabel errorDireccio = new JLabel("Mínim 8 caràcters i inici sense números");
        errorDireccio.setBounds(xLabelDreta + ampleLabel + 10, yLabel + separacio + 28, ampleCamp +30, altLabel);
        errorDireccio.setForeground(Color.RED);
        errorDireccio.setVisible(false);
        frame.add(errorDireccio);

        JLabel errorPoblacio = new JLabel("Mínim 2 caràcters i inici sense números"); 
        errorPoblacio.setBounds(xLabelDreta + ampleLabel + 10, yLabel + 2 * separacio + altLabel + 2, ampleCamp + 30, altLabel);
        errorPoblacio.setForeground(Color.RED);
        errorPoblacio.setVisible(false);
        frame.add(errorPoblacio);
        
        JLabel errorDataNaix = new JLabel("Exemple: 30-04-2003");
        errorDataNaix.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 3 * separacio + 27, ampleCamp + 30, altLabel);
        errorDataNaix.setForeground(Color.RED);
        errorDataNaix.setVisible(false);
        frame.add(errorDataNaix);
        
        JLabel errorCampBuit = new JLabel("Aquest camp no pot estar buit");
        errorCampBuit.setForeground(Color.RED);
        errorCampBuit.setVisible(false);

        JLabel errorAnyFiRevisio = new JLabel("L'any ha de ser igual o superior a l'actual");
        errorAnyFiRevisio.setBounds(xLabelEsquerra + ampleLabel + 10, yLabel + 5 * separacio + 27, ampleCamp + 30, altLabel);
        errorAnyFiRevisio.setForeground(Color.RED);
        errorAnyFiRevisio.setVisible(false);
        frame.add(errorAnyFiRevisio);

        // Lògica del botó Guardar
        botoGuardar.addActionListener(e -> {
            try {
                boolean hiHaErrors = false;
                boolean hiHaErrorsFormat = false;
                
                // Nom
                if (campNom.getText().trim().isEmpty()) {
                    campNom.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    campNom.setBackground(Color.WHITE);
                    if (campNom.getText().matches(".*\\d.*") || campNom.getText().trim().length() < 2) {
                        errorNom.setVisible(true);
                        hiHaErrorsFormat = true;
                    } else {
                        errorNom.setVisible(false);
                    }
                }
                
                // Cognoms
                if (campCognoms.getText().trim().isEmpty()) {
                    campCognoms.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    campCognoms.setBackground(Color.WHITE);
                    if (campCognoms.getText().matches(".*\\d.*") || campCognoms.getText().trim().length() < 2) {
                        errorCognoms.setVisible(true);
                        hiHaErrorsFormat = true;
                    } else {
                        errorCognoms.setVisible(false);
                    }
                }
                
                // sexe
                if (!radioDona.isSelected() && !radioHome.isSelected()) {
                    errorSexe.setVisible(true);
                    hiHaErrors = true;
                } else {
                    errorSexe.setVisible(false);
                }
                
                // IBAN
                if (campIBAN.getText().trim().isEmpty()) {
                    campIBAN.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    campIBAN.setBackground(Color.WHITE);
                    if (campIBAN.getText().length() != 24) {
                        errorIBAN.setVisible(true);
                        hiHaErrorsFormat = true;
                    } else {
                        errorIBAN.setVisible(false);
                    }
                }
                
                // Direcció
                if (campDireccio.getText().trim().isEmpty()) {
                    campDireccio.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    campDireccio.setBackground(Color.WHITE);
                    if (campDireccio.getText().matches("^[0-9].*") || campDireccio.getText().trim().length() < 8) {
                        errorDireccio.setVisible(true);
                        hiHaErrorsFormat = true;
                    } else {
                        errorDireccio.setVisible(false);
                    }
                }
                
                // Població
                if (campPoblacio.getText().trim().isEmpty()) {
                    campPoblacio.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    campPoblacio.setBackground(Color.WHITE);
                    if (campPoblacio.getText().matches("^[0-9].*") || campPoblacio.getText().trim().length() < 2) {
                        errorPoblacio.setVisible(true);
                        hiHaErrorsFormat = true;
                    } else {
                        errorPoblacio.setVisible(false);
                    }
                }
                
                // Codi Postal
                if (campCodiPostal.getText().trim().isEmpty()) {
                    campCodiPostal.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    campCodiPostal.setBackground(Color.WHITE);
                    if (!campCodiPostal.getText().matches("\\d{5}")) {
                        errorCodiPostal.setVisible(true);
                        hiHaErrorsFormat = true;
                    } else {
                        errorCodiPostal.setVisible(false);
                    }
                }
                
                // ID Legal
                if (campIdLegal.getText().trim().isEmpty()) {
                    campIdLegal.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    campIdLegal.setBackground(Color.WHITE);
                    if (!campIdLegal.getText().matches("\\d{8}[A-Z]")) {
                        errorID.setVisible(true);
                        hiHaErrorsFormat = true;
                    } else {
                        errorID.setVisible(false);
                    }
                }
                
                // Data Naixement
                if (campDataNaix.getText().trim().isEmpty()) {
                    campDataNaix.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    campDataNaix.setBackground(Color.WHITE);
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        sdf.setLenient(false);
                        Date dataNaix = sdf.parse(campDataNaix.getText());
                        Date dataActual = new Date();

                        if (dataNaix.after(dataActual)) {
                            errorDataNaix.setText("La data ha de ser passada");
                            errorDataNaix.setVisible(true);
                            hiHaErrorsFormat = true;
                        } else {
                            if (jugador != null) {
                                if (!sdf.format(jugador.getDataNaix()).equals(campDataNaix.getText())) {
                                    boolean permesCanviarData = persistencia.esPermesCanviarDataNaixement(jugador.getId(), dataNaix);
                                    if (!permesCanviarData) {
                                        errorDataNaix.setText("El canvi de data afectaria la categoria");
                                        errorDataNaix.setVisible(true);
                                        hiHaErrorsFormat = true;
                                    } else {
                                        errorDataNaix.setVisible(false);
                                    }
                                }
                            } else {
                                errorDataNaix.setVisible(false);
                            }
                        }
                    } catch (Exception ex) {
                        errorDataNaix.setText("Format incorrecte. Exemple: 30-04-2003");
                        errorDataNaix.setVisible(true);
                        hiHaErrorsFormat = true;
                    }
                }
                
                // Foto
                if (campFoto.getText().trim().isEmpty()) {
                    panelFoto.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    panelFoto.setBackground(Color.WHITE);
                }
                
                // Any Fi Revisió
                if (campAnyFiRevisio.getText().trim().isEmpty()) {
                    campAnyFiRevisio.setBackground(new Color(255, 200, 200));
                    hiHaErrors = true;
                } else {
                    try {
                        int anyRevisio = Integer.parseInt(campAnyFiRevisio.getText());
                        if (anyRevisio < anyActual) {
                            campAnyFiRevisio.setBackground(new Color(255, 200, 200));
                            errorAnyFiRevisio.setVisible(true);
                            hiHaErrorsFormat = true;
                        } else {
                            campAnyFiRevisio.setBackground(Color.WHITE);
                            errorAnyFiRevisio.setVisible(false);
                        }
                    } catch (NumberFormatException ex) {
                        campAnyFiRevisio.setBackground(new Color(255, 200, 200));
                        errorAnyFiRevisio.setVisible(true);
                        hiHaErrorsFormat = true;
                    }
                }
                
                if (hiHaErrors) {
                    JOptionPane.showMessageDialog(frame, "Tots els camps són obligatoris", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (hiHaErrorsFormat) {
                    JOptionPane.showMessageDialog(frame, "Hi ha camps amb format incorrecte", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                //comprova si es pot canviar el sexe
                if (jugador != null) {
                    char sexeOriginal = jugador.getSexe();
                    char sexeNou = radioDona.isSelected() ? 'D' : 'H';
                    
                    if (sexeOriginal != sexeNou) {
                        boolean permesCanviarSexe = persistencia.esPermesCanviarSexe(jugador.getId());
                        if (!permesCanviarSexe) {
                            JOptionPane.showMessageDialog(frame,
                                "No es pot canviar el sexe perquè el jugador pertany a un equip no mixt","Error",JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                
                String nom = campNom.getText();
                String cognoms = campCognoms.getText();
                char sexe = radioDona.isSelected() ? 'D' : 'H'; //així és més curt
                
                String dataNaixTextCamp = campDataNaix.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                sdf.setLenient(false); 
                Date dataNaix = sdf.parse(dataNaixTextCamp);

                String IBAN = campIBAN.getText();
                String direccio = campDireccio.getText();
                String poblacio = campPoblacio.getText();
                String codiPostal = campCodiPostal.getText();
                Adreca adreca = new Adreca(direccio, codiPostal, poblacio);

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
                    
                } else {
                    Jugador j = new Jugador(nom, cognoms, adreca, foto, anyFiRevisioMedica, IBAN, idLegal, dataNaix, sexe);
                    persistencia.afegirJugador(j);
                }
                
                persistencia.confirmarCanvis();
                JOptionPane.showMessageDialog(frame, "Jugador guardat amb èxit!");
                
                frame.dispose();
                new GestioJugador(persistencia);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        botoCancelar.addActionListener(e -> {
            frame.dispose();
            new GestioJugador(persistencia);
        });

        frame.setVisible(true);

    }
}
