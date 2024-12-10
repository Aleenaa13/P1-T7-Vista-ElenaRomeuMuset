package p1.t7.vista.romeumusetelena;

import p1.t6.model.romeumusetelena.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.esportsapp.persistencia.IPersistencia;

public class AfegirEditarEquip extends JPanel {

    private JTextField txtNom;
    private JComboBox<Temporada> comboTemporada;
    private JComboBox<Categoria> comboCategoria;
    private JRadioButton radioH;
    private JRadioButton radioD;
    private JRadioButton radioM;
    private JButton btnDesa;
    private JButton btnCancellar;

    private IPersistencia persistencia;
    private boolean esAfegir;
    private Equip equip;

    public AfegirEditarEquip(IPersistencia persistencia, boolean esAfegir, Equip equip) {
        this.persistencia = persistencia;
        this.esAfegir = esAfegir;
        this.equip = equip;
        inicialitzarComponents();
    }

    private void inicialitzarComponents() {
        setLayout(null);  // Per poder posar els components a posicions fixades

        // Botons de la part superior
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
            add(botonsMenu[i]);  // Afegir al panell
        }

        // Panell per als camps de l'equip
        JPanel panellCamps = new JPanel();
        panellCamps.setLayout(null);
        panellCamps.setBounds(0, 100, 450, 500);  // Ajustem l'altura per ocupar de y=100 a y=600
        add(panellCamps);

        // Panell per al títol amb FlowLayout
        JPanel panellTitol = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panellTitol.setBounds(0, 60, 900, 40);  // Canviem per una mida més flexible
        add(panellTitol);

        // Títol
        JLabel titol = new JLabel("GESTOR CLUB FUTBOL");
        titol.setFont(new Font("SansSerif", Font.BOLD, 24));
        titol.setForeground(new Color(70, 130, 180));
        panellTitol.add(titol);  // Afegim el títol al panell


        // Nom de l'equip
        JLabel lblNom = new JLabel("Nom de l'Equip:");
        lblNom.setBounds(10, 50, 120, 30);
        panellCamps.add(lblNom);

        txtNom = new JTextField();
        txtNom.setBounds(130, 50, 300, 30);
        panellCamps.add(txtNom);

        // Tipus d'equip (radiobuttons)
        JLabel lblTipus = new JLabel("Tipus d'Equip:");
        lblTipus.setBounds(10, 90, 120, 30);
        panellCamps.add(lblTipus);

        JPanel panellTipus = new JPanel(new FlowLayout());
        ButtonGroup grupTipus = new ButtonGroup();
        radioH = new JRadioButton("H", true);
        radioD = new JRadioButton("D");
        radioM = new JRadioButton("M");
        grupTipus.add(radioH);
        grupTipus.add(radioD);
        grupTipus.add(radioM);
        panellTipus.add(radioH);
        panellTipus.add(radioD);
        panellTipus.add(radioM);
        panellTipus.setBounds(130, 90, 300, 30);  // Posició de l'alignament dels radiobuttons
        panellCamps.add(panellTipus);

        // Temporada
        JLabel lblTemporada = new JLabel("Temporada:");
        lblTemporada.setBounds(10, 130, 120, 30);
        panellCamps.add(lblTemporada);

        comboTemporada = new JComboBox<>();
        comboTemporada.setBounds(130, 130, 300, 30);
        carregarTemporades();
        panellCamps.add(comboTemporada);

        // Categoria
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(10, 170, 120, 30);
        panellCamps.add(lblCategoria);

        comboCategoria = new JComboBox<>();
        comboCategoria.setBounds(130, 170, 300, 30);
        carregarCategories();
        panellCamps.add(comboCategoria);

        // Botons Desa i Cancel·lar
        btnDesa = new JButton("Desa");
        btnDesa.setBounds(50, 420, 100, 30);
        btnDesa.setBackground(new Color(135, 206, 250));  // Estil del botó
        btnDesa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestionarDesa();
            }
        });
        panellCamps.add(btnDesa);

        btnCancellar = new JButton("Cancel·lar");
        btnCancellar.setBounds(200, 420, 100, 30);
        btnCancellar.setBackground(new Color(135, 206, 250));  // Estil del botó
        btnCancellar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.getWindowAncestor(AfegirEditarEquip.this).dispose();
            }
        });
        panellCamps.add(btnCancellar);

        // Carregar dades si estem editant un equip existent
        if (!esAfegir && equip != null) {
            carregarEquip();
        }

        // Actualitzar la UI
        revalidate();
        repaint();
    }

    private void carregarTemporades() {
        try {
            List<Temporada> temporades = persistencia.obtenirTotesTemporades();
            for (Temporada temporada : temporades) {
                comboTemporada.addItem(temporada);
            }
            comboTemporada.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Temporada) {
                        Temporada temporada = (Temporada) value;
                        label.setText(String.valueOf(temporada.getAny()));  // Mostrem només l'any
                    }
                    return label;
                }
            });
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(this, "Error carregant temporades: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCategories() {
        try {
            List<Categoria> categories = persistencia.obtenirTotesCategories();
            for (Categoria categoria : categories) {
                comboCategoria.addItem(categoria);
            }
            comboCategoria.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Categoria) {
                        Categoria categoria = (Categoria) value;
                        label.setText(categoria.getNom());  // Mostrem només el nom
                    }
                    return label;
                }
            });
        } catch (GestorBDEsportsException e) {
            JOptionPane.showMessageDialog(this, "Error carregant categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarEquip() {
        txtNom.setText(equip.getNom());
        if (equip.getTipus() == TipusEquip.D) {
            radioD.setSelected(true);
        } else if (equip.getTipus() == TipusEquip.M) {
            radioM.setSelected(true);
        } else {
            radioH.setSelected(true);
        }
        comboTemporada.setSelectedItem(new Temporada(equip.getAnyTemporada()));
        // Aquí es seleccionaria la categoria corresponent a l'ID
    }

    private void gestionarDesa() {
        String nom = txtNom.getText();
        TipusEquip tipus = radioH.isSelected() ? TipusEquip.H : (radioD.isSelected() ? TipusEquip.D : TipusEquip.M);
        Temporada temporadaSeleccionada = (Temporada) comboTemporada.getSelectedItem();
        Categoria categoriaSeleccionada = (Categoria) comboCategoria.getSelectedItem();

        if (nom.isEmpty() || temporadaSeleccionada == null || categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Tots els camps són obligatoris.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear o actualitzar l'equip segons el cas
        if (esAfegir) {
            // Afegir un nou equip
            Equip nouEquip = new Equip(nom, tipus, temporadaSeleccionada.getAny(), categoriaSeleccionada.getId());
            try {
                persistencia.afegirEquip(nouEquip);
                //persistencia.confirmarCanvis();
                JOptionPane.showMessageDialog(this, "Equip afegit correctament!", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(this).dispose();
            } catch (GestorBDEsportsException e) {
                JOptionPane.showMessageDialog(this, "Error afegint l'equip: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Editar l'equip existent
            equip.setNom(nom);
            equip.setTipus(tipus);
            equip.setAnyTemporada(temporadaSeleccionada.getAny());
            equip.setIdCategoria(categoriaSeleccionada.getId());

            try {
                persistencia.modificarEquip(equip);  // Assumint que `modificarEquip` actualitza l'equip a la base de dades
                JOptionPane.showMessageDialog(this, "Equip actualitzat correctament!", "Èxit", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(this).dispose();
            } catch (GestorBDEsportsException e) {
                JOptionPane.showMessageDialog(this, "Error actualitzant l'equip: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
