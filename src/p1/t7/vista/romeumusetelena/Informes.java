package p1.t7.vista.romeumusetelena;

import org.esportsapp.persistencia.IPersistencia;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import p1.t6.model.romeumusetelena.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Informes {
    // Constants de configuració
    private static final int AMPLADA_FINESTRA = 900;
    private static final int ALTURA_FINESTRA = 600;
    private static final int MARGE_LATERAL = 50;
    private static final int ALTURA_BOTO_MENU = 40;
    private static final String FITXER_PROPIETATS = "db.properties";
    
    private JFrame finestra;
    private IPersistencia persistencia;
    private JButton[] botonsMenu;
    
    // Propietats Jasper
    private String urlJRS;
    private String usuariJRS;
    private String contrasenyaJRS;

    public Informes(IPersistencia persistencia) {
        this.persistencia = persistencia;
        carregarPropietatsJasper();
        inicialitzarComponents();
    }

    private void inicialitzarComponents() {
        configurarFinestra();
        configurarMenu();
        afegirTitol();
        afegirAreaPrevisualitzacio();
        afegirPanellBotons();
        finestra.setVisible(true);
    }

    private void configurarFinestra() {
        finestra = new JFrame("Informes");
        finestra.setSize(AMPLADA_FINESTRA, ALTURA_FINESTRA);
        finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        finestra.setLayout(null);
        finestra.setResizable(false);
        finestra.setLocationRelativeTo(null);
        
        Color blancTrencat = new Color(245, 245, 245);
        finestra.getContentPane().setBackground(blancTrencat);
    }

    private void afegirTitol() {
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL", SwingConstants.CENTER);
        titol.setBounds(0, 60, AMPLADA_FINESTRA, 40);
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        finestra.add(titol);
    }
    
    private void configurarMenu() {
        int numBotons = 6;
        int ampladaBoto = AMPLADA_FINESTRA / numBotons;
        String[] nomsBotons = {"INICI", "EQUIPS", "JUGADORS", "TEMPORADES", "INFORMES", "TANCAR SESSIÓ"};
        botonsMenu = new JButton[numBotons];

        for (int i = 0; i < nomsBotons.length; i++) {
            botonsMenu[i] = new JButton(nomsBotons[i]);
            botonsMenu[i].setBounds(i * ampladaBoto, 0, ampladaBoto, ALTURA_BOTO_MENU);
            botonsMenu[i].setBackground(new Color(70, 130, 180));
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
        
        botonsMenu[1].addActionListener(e -> {
            finestra.dispose();
            new GestioEquips(persistencia);
        });
        
        botonsMenu[2].addActionListener(e -> {
            finestra.dispose();
            new GestioJugador(persistencia);
        });
        
        botonsMenu[3].addActionListener(e -> {
            finestra.dispose();
            new GestioTemporades(persistencia);
        });
        
        //botonsMenu[4].setEnabled(false);
        
        botonsMenu[5].addActionListener(e -> {
            TancarSessio.executar(finestra, persistencia);
        });
        
        
    }

    private void afegirAreaPrevisualitzacio() {
        JTextArea areaPrevisualitzacio = new JTextArea();
        areaPrevisualitzacio.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaPrevisualitzacio);
        scrollPane.setBounds(MARGE_LATERAL, 120, 800, 340);
        finestra.add(scrollPane);
        carregarDades(areaPrevisualitzacio);
    }

    private void afegirPanellBotons() {
        JPanel panellBotons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panellBotons.setBounds(MARGE_LATERAL, 480, 800, 60);
        panellBotons.setBackground(new Color(245, 245, 245));

        afegirBotonsInforme(panellBotons);
        finestra.add(panellBotons);
    }

    private void afegirBotonsInforme(JPanel panell) {
        JButton btnJasper = crearBotoInforme("Generar Informe JRS", e -> mostrarDialegJasper());
        JButton btnXML = crearBotoInforme("Exportar XML", e -> exportarXML());
        JButton btnCSV = crearBotoInforme("Exportar CSV", e -> exportarCSV());

        for (JButton boto : new JButton[]{btnJasper, btnXML, btnCSV}) {
            panell.add(boto);
        }
    }

    private JButton crearBotoInforme(String text, ActionListener accio) {
        JButton boto = new JButton(text);
        boto.setBackground(new Color(173, 216, 230));
        boto.setFocusPainted(false);
        boto.setPreferredSize(new Dimension(200, 40));
        boto.addActionListener(accio);
        return boto;
    }

    private void carregarPropietatsJasper() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(FITXER_PROPIETATS)) {
            props.load(fis);
            urlJRS = props.getProperty("urlJRS");
            usuariJRS = props.getProperty("userJRS");
            contrasenyaJRS = props.getProperty("pwdJRS");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(finestra, "Error al carregar propietats de Jasper: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialegJasper() {
        JDialog dialeg = crearDialegJasper();
        configurarDialegJasper(dialeg);
        dialeg.setVisible(true);
    }

    private JDialog crearDialegJasper() {
        JDialog dialeg = new JDialog(finestra, "Generar Informe PDF", true);
        dialeg.setLayout(new GridBagLayout());
        dialeg.setSize(450, 250);
        dialeg.setLocationRelativeTo(finestra);
        return dialeg;
    }

     private void configurarDialegJasper(JDialog dialeg) {
        dialeg.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel panellPrincipal = new JPanel();
        panellPrincipal.setLayout(new BoxLayout(panellPrincipal, BoxLayout.Y_AXIS));
        panellPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Hi ha algunes coses que les he buscat perque se'm quedes tot més bonic el (SwingConstants, createVerticalStrut)
        JLabel titol = new JLabel("Selecciona els filtres per l'informe:", SwingConstants.CENTER);
        titol.setFont(new Font("SansSerif", Font.BOLD, 14));
        titol.setAlignmentX(Component.CENTER_ALIGNMENT);
        panellPrincipal.add(titol);
        panellPrincipal.add(Box.createVerticalStrut(15));

        Dimension comboSize = new Dimension(200, 25);
        
        //combo Temporada
        JPanel panellTemporada = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTemporada = new JLabel("Temporada:");
        lblTemporada.setPreferredSize(new Dimension(80, 25));
        JComboBox<Temporada> cmbTemporada = new JComboBox<>();
        cmbTemporada.setPreferredSize(comboSize);
        cmbTemporada.addItem(null); // perquè sorti "totes" per defecte
        try {
            for (Temporada temp : persistencia.obtenirTotesTemporades()) {
                cmbTemporada.addItem(temp);
            }
        } catch (GestorBDEsportsException ex) {
            JOptionPane.showMessageDialog(finestra, "Error al carregar temporades: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        panellTemporada.add(lblTemporada);
        panellTemporada.add(cmbTemporada);
        panellPrincipal.add(panellTemporada);
        panellPrincipal.add(Box.createVerticalStrut(10));

        // Combo Categoria
        JPanel panellCategoria = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setPreferredSize(new Dimension(80, 25));
        JComboBox<Categoria> cmbCategoria = new JComboBox<>();
        cmbCategoria.setPreferredSize(comboSize);
        cmbCategoria.addItem(null); 
        try {
            for (Categoria cat : persistencia.obtenirTotesCategories()) {
                cmbCategoria.addItem(cat);
            }
        } catch (GestorBDEsportsException ex) {
            JOptionPane.showMessageDialog(finestra, "Error al carregar categories: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        panellCategoria.add(lblCategoria);
        panellCategoria.add(cmbCategoria);
        panellPrincipal.add(panellCategoria);
        panellPrincipal.add(Box.createVerticalStrut(10));

        // Combo Equip
        JPanel panellEquip = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblEquip = new JLabel("Equip:");
        lblEquip.setPreferredSize(new Dimension(80, 25));
        JComboBox<Equip> cmbEquip = new JComboBox<>();
        cmbEquip.setPreferredSize(comboSize);
        cmbEquip.addItem(null); 
        try {
            for (Equip equip : persistencia.obtenirTotsEquips()) {
                cmbEquip.addItem(equip);
            }
        } catch (GestorBDEsportsException ex) {
            JOptionPane.showMessageDialog(finestra, "Error al carregar equips: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        panellEquip.add(lblEquip);
        panellEquip.add(cmbEquip);
        panellPrincipal.add(panellEquip);
        panellPrincipal.add(Box.createVerticalStrut(20));

        JPanel panellBotons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton btnGenerar = new JButton("Generar PDF");
        JButton btnCancelar = new JButton("Cancel·lar");

        btnGenerar.setBackground(new Color(173, 216, 230));
        btnCancelar.setBackground(new Color(173, 216, 230));
        btnGenerar.setFocusPainted(false);
        btnCancelar.setFocusPainted(false);

        btnGenerar.addActionListener(e -> {
            generarInformeJasper(cmbTemporada, cmbCategoria, cmbEquip);
            dialeg.dispose();
        });

        btnCancelar.addActionListener(e -> dialeg.dispose());

        panellBotons.add(btnGenerar);
        panellBotons.add(btnCancelar);
        panellPrincipal.add(panellBotons);

        dialeg.add(panellPrincipal);
        dialeg.pack();
        dialeg.setSize(new Dimension(350, dialeg.getHeight()));
    }
 
    private void carregarDades(JTextArea area) {
        try {
            StringBuilder sb = new StringBuilder(); //És per montar el string de les dades
            List<Jugador> jugadors = persistencia.obtenirTotsJugadors();
            
            //Això vaig demanar al chat que ho fes bonic pq manualment era molta estona
            for (Jugador jugador : jugadors) {
                sb.append("Jugador: ").append(jugador.getNom())
                  .append(" ").append(jugador.getCognoms()).append("\n");
                sb.append("\tData Naixement: ").append(jugador.getDataNaix()).append("\n");
                sb.append("\tDirecció: ").append(jugador.getAdreca().getDireccio())
                  .append(", ").append(jugador.getAdreca().getCodiPostal())
                  .append(" ").append(jugador.getAdreca().getPoblacio()).append("\n");
                sb.append("\tRevisió Mèdica: ").append(jugador.getAnyFiRevisioMedica()).append("\n");
                
                List<Equip> equips = obtenirEquipsDeJugador(jugador.getId());
                if (!equips.isEmpty()) {
                    sb.append("\tEquips:\n");
                    for (Equip equip : equips) {
                        sb.append("\t\t- ").append(equip.getNom()).append("\n");
                    }
                }
                sb.append("\n");
            }

            area.setText(sb.toString());
        } catch (GestorBDEsportsException ex) {
            JOptionPane.showMessageDialog(null,"Error al carregar les dades: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Equip> obtenirEquipsDeJugador(int idJugador) throws GestorBDEsportsException {
        List<Equip> equips = new ArrayList<>();
        List<Equip> totsEquips = persistencia.obtenirTotsEquips();
        
        for (Equip equip : totsEquips) {
            List<Membre> membres = persistencia.obtenirMembresDEquip(equip.getId());
            for (Membre membre : membres) {
                if (membre.getJugador().getId() == idJugador) {
                    equips.add(equip);
                    break;
                }
            }
        }
        return equips;
    }

    private void exportarXML() {
        try {
            Element root = new Element("jugadors");
            Document doc = new Document(root);

            List<Jugador> jugadors = persistencia.obtenirTotsJugadors();
            for (Jugador jugador : jugadors) {
                Element jugadorElement = new Element("jugador");
                jugadorElement.setAttribute("id", String.valueOf(jugador.getId()));
                
                jugadorElement.addContent(new Element("nom").setText(jugador.getNom()));
                jugadorElement.addContent(new Element("cognoms").setText(jugador.getCognoms()));
                Element dataNaixElement = new Element("dataNaix");
                dataNaixElement.setText(jugador.getDataNaix().toString());
                jugadorElement.addContent(dataNaixElement);
                
                Element adrecaElement = new Element("adreca");
                adrecaElement.addContent(new Element("direccio").setText(jugador.getAdreca().getDireccio()));
                adrecaElement.addContent(new Element("codiPostal").setText(jugador.getAdreca().getCodiPostal()));
                adrecaElement.addContent(new Element("poblacio").setText(jugador.getAdreca().getPoblacio()));
                jugadorElement.addContent(adrecaElement);
                
                jugadorElement.addContent(new Element("revisioMedica").setText(String.valueOf(jugador.getAnyFiRevisioMedica())));
                jugadorElement.addContent(new Element("iban").setText(jugador.getIBAN()));
                jugadorElement.addContent(new Element("idLegal").setText(jugador.getIdLegal()));
                
                Element equipsElement = new Element("equips");
                List<Equip> equips = obtenirEquipsDeJugador(jugador.getId());
                for (Equip equip : equips) {
                    Element equipElement = new Element("equip");
                    equipElement.setAttribute("id", String.valueOf(equip.getId()));
                    equipElement.addContent(new Element("nom").setText(equip.getNom()));
                    equipElement.addContent(new Element("tipus").setText(equip.getTipus().name()));
                    equipsElement.addContent(equipElement);
                }
                jugadorElement.addContent(equipsElement);
                
                root.addContent(jugadorElement);
            }
            
            //Això per que es descarregui directament a descarregues també ho he buscat
            String path = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "jugadors.xml";
            File file = new File(path);
            if (file.exists()) {
                int resposta = JOptionPane.showConfirmDialog(finestra,"El fitxer ja existeix a Descàrregues. Vols sobreescriure'l?",
                    "Confirmar sobreescriptura",JOptionPane.YES_NO_OPTION);
                if (resposta != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(path));

            JOptionPane.showMessageDialog(finestra,"Arxiu XML exportat correctament a la carpeta Descàrregues","Èxit", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(finestra,"Error al exportar a XML: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     private void exportarCSV() {
        try {
            String path = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "jugadors.csv";
            File file = new File(path);
            if (file.exists()) {
                int resposta = JOptionPane.showConfirmDialog(finestra,"El fitxer ja existeix a Descàrregues. Vols sobreescriure'l?", 
                    "Confirmar sobreescriptura",JOptionPane.YES_NO_OPTION);
                if (resposta != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            FileWriter writer = new FileWriter(path);
            writer.write("ID,Nom,Cognoms,Data Naixement,Direcció,Codi Postal,Població,Any Fi Revisió Mèdica,IBAN,ID Legal,Equips\n");

            List<Jugador> jugadors = persistencia.obtenirTotsJugadors();
            for (Jugador jugador : jugadors) {
                List<Equip> equips = obtenirEquipsDeJugador(jugador.getId());
                StringBuilder equipsStr = new StringBuilder();
                for (Equip equip : equips) {
                    if (equipsStr.length() > 0) {
                        equipsStr.append("; "); //per mirar si te mes d'un equip
                    }
                    equipsStr.append(equip.getNom());
                }
                String equipsString = equipsStr.toString();
                
                //això també vaig buscar com es feia
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%d,%s,%s,%s\n",
                    jugador.getId(),
                    escaparCSV(jugador.getNom()),
                    escaparCSV(jugador.getCognoms()),
                    jugador.getDataNaix().toString(),
                    escaparCSV(jugador.getAdreca().getDireccio()),
                    escaparCSV(jugador.getAdreca().getCodiPostal()),
                    escaparCSV(jugador.getAdreca().getPoblacio()),
                    jugador.getAnyFiRevisioMedica(),
                    escaparCSV(jugador.getIBAN()),
                    escaparCSV(jugador.getIdLegal()),
                    escaparCSV(equipsString)
                ));
            }
            writer.close();

            JOptionPane.showMessageDialog(finestra,"Arxiu CSV exportat correctament a la carpeta Descàrregues","Èxit", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(finestra,"Error al exportar a CSV: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //això salta nulls, coses especials...
    private String escaparCSV(String text) {
        if (text == null) return "";
        return "\"" + text.replace("\"", "\"\"") + "\"";
    }

    private void generarInformeJasper(JComboBox<Temporada> cmbTemporada,
                                    JComboBox<Categoria> cmbCategoria,
                                    JComboBox<Equip> cmbEquip) {
        try {
            final int BUFFER_SIZE = 4096;
            
            // Construir URL base
            StringBuilder urlBuilder = new StringBuilder(urlJRS);
            urlBuilder.append("FitxaEquip.pdf");
            
            // Afegir paràmetres utilitzant el mètode correcte
            boolean teParams = false;
            if (cmbTemporada.getSelectedItem() != null) {
                if (!teParams) {
                    urlBuilder.append("?");
                    teParams = true;
                } else {
                    urlBuilder.append("&");
                }
                urlBuilder.append("Temporada=").append(((Temporada)cmbTemporada.getSelectedItem()).getAny());
            }
            
            if (cmbCategoria.getSelectedItem() != null) {
                if (!teParams) {
                    urlBuilder.append("?");
                    teParams = true;
                } else {
                    urlBuilder.append("&");
                }
                urlBuilder.append("Categoria=").append(((Categoria)cmbCategoria.getSelectedItem()).getId());
            }
            
            if (cmbEquip.getSelectedItem() != null) {
                if (!teParams) {
                    urlBuilder.append("?");
                } else {
                    urlBuilder.append("&");
                }
                urlBuilder.append("Equip=").append(((Equip)cmbEquip.getSelectedItem()).getId());
            }
            
            URL obj = new URL(urlBuilder.toString());
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            String auth = Base64.getEncoder().encodeToString((usuariJRS + ":" + contrasenyaJRS).getBytes());
            con.setRequestProperty("Authorization", "Basic " + auth);
            con.setRequestProperty("Accept", "application/pdf");
            
            int codiResposta = con.getResponseCode();
            if (codiResposta == HttpURLConnection.HTTP_OK) {
                
                String nomEquip;
                if (cmbEquip.getSelectedItem() != null) {
                    nomEquip = ((Equip)cmbEquip.getSelectedItem()).getNom();
                } else {
                    nomEquip = "Tots";
                }

                String nomCategoria;
                if (cmbCategoria.getSelectedItem() != null) {
                    nomCategoria = ((Categoria)cmbCategoria.getSelectedItem()).getNom();
                } else {
                    nomCategoria = "Totes";
                }

                String nomArxiu = String.format("Informe_Club_%s_%s_%s.pdf",nomEquip,nomCategoria, 
                    new SimpleDateFormat("ddMMyyyy").format(new Date()));
                
                String carpetaUsuari = System.getProperty("user.home");
                File carpetaDescarregues = new File(carpetaUsuari, "Downloads");
                File arxiuSortida = new File(carpetaDescarregues, nomArxiu);
                
                try {
                    InputStream is = con.getInputStream();
                    FileOutputStream fos = new FileOutputStream(arxiuSortida);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesLlegits;
                    while ((bytesLlegits = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesLlegits);
                    }
                    is.close();
                    fos.close();
                } catch(IOException ex){
                    System.out.println("Error" + ex.getMessage());
                }
                
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(arxiuSortida);
                }
                
                // un missatge de que tot ha anat bé
                String temporadaText;
                if (cmbTemporada.getSelectedItem() != null) {
                    temporadaText = ((Temporada)cmbTemporada.getSelectedItem()).getAny()+"";
                } else {
                    temporadaText = "Totes";
                }

                String categoriaText;
                if (cmbCategoria.getSelectedItem() != null) {
                    categoriaText = ((Categoria)cmbCategoria.getSelectedItem()).getNom();
                } else {
                    categoriaText = "Totes";
                }

                String equipText;
                if (cmbEquip.getSelectedItem() != null) {
                    equipText = ((Equip)cmbEquip.getSelectedItem()).getNom();
                } else {
                    equipText = "Tots";
                }

                String missatge = String.format("✔ Informe generat correctament!\n\n" +
                    "Filtres aplicats:\n" + 
                    "Temporada: %s\n" +
                    "Categoria: %s\n" +
                    "Equip: %s\n\n" +
                    "L'informe s'ha desat a:\n" +
                    "%s\n\n" +
                    "S'obrirà automàticament amb el visor de PDF",temporadaText,categoriaText,equipText,arxiuSortida.getAbsolutePath());
                
                JOptionPane.showMessageDialog(finestra,missatge,"Informe Generat",JOptionPane.INFORMATION_MESSAGE);   
            } else {
                StringBuilder missatgeError = new StringBuilder();
                missatgeError.append("Error al generar l'informe\n\n");
                missatgeError.append("Codi d'error: ").append(codiResposta).append("\n\n");
                missatgeError.append("Si el problema persisteix, contacta amb l'Isidre o el Bernat.");
                
                JOptionPane.showMessageDialog(finestra,missatgeError,"Error",JOptionPane.ERROR_MESSAGE);
            }
            con.disconnect();
            
        } catch (Exception ex) {
            StringBuilder missatgeError = new StringBuilder();
            missatgeError.append("Error inesperat\n\n");
            missatgeError.append(ex.getMessage()).append("\n\n");
            missatgeError.append("Si el problema persisteix, contacta amb l'Isidre o el Bernat.");
            
            
            JOptionPane.showMessageDialog(finestra,missatgeError,"Error",JOptionPane.ERROR_MESSAGE);
            
            //ex.printStackTrace(); aixo era pq em donava error ho deixo
        }
    }
}
