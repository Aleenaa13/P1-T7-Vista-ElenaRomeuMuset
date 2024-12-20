package p1.t7.vista.romeumusetelena;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import org.esportsapp.persistencia.IPersistencia;
import p1.t6.model.romeumusetelena.*;

public class GestioMembres {
    private static IPersistencia persistencia;
    private static List<Membre> membresActuals;
    private static List<Jugador> totsJugadors;
    
    public static void mostrarFormulari(Equip equip, IPersistencia pers, DefaultTableModel modelTaulaEquip) {
        persistencia = pers;
        JFrame frame = new JFrame("Gestió de Membres - " + equip.getNom());
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        
        JLabel titolDisponibles = new JLabel("MEMBRES DISPONIBLES", SwingConstants.CENTER);
        titolDisponibles.setBounds(50, 20, 400, 30);
        titolDisponibles.setFont(new Font("SansSerif", Font.BOLD, 14));
        titolDisponibles.setForeground(new Color(70, 130, 180));
        frame.add(titolDisponibles);
        
        JLabel titolActuals = new JLabel("MEMBRES DE L'EQUIP", SwingConstants.CENTER);
        titolActuals.setBounds(550, 20, 400, 30);
        titolActuals.setFont(new Font("SansSerif", Font.BOLD, 14));
        titolActuals.setForeground(new Color(70, 130, 180));
        frame.add(titolActuals);
        
        //taules
        DefaultTableModel modelDisponibles = new DefaultTableModel();
        DefaultTableModel modelActuals = new DefaultTableModel();
        
        modelDisponibles.addColumn("ID");
        modelDisponibles.addColumn("Nom");
        modelDisponibles.addColumn("Cognoms");
        modelDisponibles.addColumn("Categoria");
        modelDisponibles.addColumn("Titular");
        
        modelActuals.addColumn("Nom");
        modelActuals.addColumn("Cognoms");
        modelActuals.addColumn("Tipus");
        
        //això és perquè em funcioni elm combobox (ho he buscat)
        JTable taulaDisponibles = new JTable(modelDisponibles) {
            @Override
            public Class<?> getColumnClass(int columna) {
                if (columna == 4) { 
                    return Boolean.class;
                }
                return String.class;
            }
        };
        
        JTable taulaActuals = new JTable(modelActuals);
        
        //aixo és pq només puguis editar el check
        for (int i = 0; i < taulaDisponibles.getColumnCount(); i++) {
            if (i != 4) {  
                taulaDisponibles.getColumnModel().getColumn(i).setCellEditor(null);
            }
        }
        
        taulaActuals.setDefaultEditor(Object.class, null); //he de posar object pq sino no em surten els jugadors
        
        JScrollPane scrollDisponibles = new JScrollPane(taulaDisponibles);
        scrollDisponibles.setBounds(50, 60, 400, 400);
        frame.add(scrollDisponibles);
        
        JScrollPane scrollActuals = new JScrollPane(taulaActuals);
        scrollActuals.setBounds(550, 60, 400, 400);
        frame.add(scrollActuals);
        
        JButton botoAfegirJugador = new JButton("→"); 
        botoAfegirJugador.setBounds(460, 200, 80, 40); 
        botoAfegirJugador.setBackground(new Color(173, 216, 230));
        botoAfegirJugador.setFocusPainted(false);
        botoAfegirJugador.setFont(new Font("SansSerif", Font.BOLD, 20)); 
        frame.add(botoAfegirJugador);
        
        JButton botoEliminarJugador = new JButton("←");  
        botoEliminarJugador.setBounds(460, 260, 80, 40); 
        botoEliminarJugador.setBackground(new Color(173, 216, 230));
        botoEliminarJugador.setFocusPainted(false);
        botoEliminarJugador.setFont(new Font("SansSerif", Font.BOLD, 20));  
        frame.add(botoEliminarJugador);
        
        JButton botoGuardar = new JButton("Guardar");
        botoGuardar.setBounds(50, 480, 100, 40); 
        botoGuardar.setBackground(new Color(173, 216, 230));
        botoGuardar.setFocusPainted(false);
        frame.add(botoGuardar);
        
        JButton botoCancelar = new JButton("Cancelar");
        botoCancelar.setBounds(160, 480, 100, 40); 
        botoCancelar.setBackground(new Color(173, 216, 230));
        botoCancelar.setFocusPainted(false);
        frame.add(botoCancelar);
        
        //aquí carrega les dades
        try {
            membresActuals = persistencia.obtenirMembresDEquip(equip.getId());
            
            for (Membre membre : membresActuals) {
                modelActuals.addRow(new Object[]{ //també ha de ser Object pq membre dona error
                    membre.getJugador().getNom(),
                    membre.getJugador().getCognoms(),
                    membre.getTipus() == TipusMembre.TITULAR ? "Titular" : "Convidat"
                });
            }
            
            totsJugadors = persistencia.obtenirTotsJugadors();
            int categoriaEquip = equip.getIdCategoria();
            
            for (Jugador jugador : totsJugadors) {
                boolean estaEnEquip = false;
                for (Membre membre : membresActuals) {
                    if (membre.getJugador().getId() == jugador.getId()) {
                        estaEnEquip = true;
                        break;
                    }
                }
                
                int categoriaJugador = calcularCategoriaJugador(jugador.getDataNaix());
                boolean categoriaValida = categoriaJugador == categoriaEquip || 
                                        categoriaJugador == (categoriaEquip - 1);
                
                boolean sexeValid = false;
                //comprovacions del sexe 
                if (equip.getTipus() == TipusEquip.M) {  
                    sexeValid = true;  
                } else if (equip.getTipus() == TipusEquip.H) { 
                    sexeValid = jugador.getSexe() == 'H';  
                } else if (equip.getTipus() == TipusEquip.D) {  
                    sexeValid = jugador.getSexe() == 'D';  
                }
                
                if (!estaEnEquip && categoriaValida && sexeValid) {
                    modelDisponibles.addRow(new Object[]{//igual
                        jugador.getId(),
                        jugador.getNom(),
                        jugador.getCognoms(),
                        obtenirTextCategoria(categoriaJugador),
                        false
                    });
                }
            }
            
        } catch (GestorBDEsportsException ex) {
            JOptionPane.showMessageDialog(frame,"Error al carregar les dades: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
        }
        
        taulaDisponibles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taulaActuals.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        botoAfegirJugador.addActionListener(e -> {
            int filaSeleccionada = taulaDisponibles.getSelectedRow();
            if (filaSeleccionada != -1) {
                try {
                    Integer idJugador = (Integer) modelDisponibles.getValueAt(filaSeleccionada, 0);
                    String nom = (String) modelDisponibles.getValueAt(filaSeleccionada, 1);
                    String cognoms = (String) modelDisponibles.getValueAt(filaSeleccionada, 2);
                    boolean esTitular = (Boolean) modelDisponibles.getValueAt(filaSeleccionada, 4);
                    
                    Jugador jugador = persistencia.obtenirJugador(idJugador);
                    Membre membre;
                    if (esTitular) {
                        membre = new Membre(equip.getId(), jugador, TipusMembre.TITULAR);
                    } else {
                        membre = new Membre(equip.getId(), jugador, TipusMembre.CONVIDAT);
                    }
                    
                    persistencia.afegirMembre(membre);
                    
                    membresActuals.add(membre);
                    
                    modelActuals.addRow(new Object[]{nom,cognoms,esTitular ? "Titular" : "Convidat"}); //aqui deixo el ? pq es mes comode que el if
                    modelDisponibles.removeRow(filaSeleccionada);
                    
                    taulaDisponibles.clearSelection();
                    
                } catch (GestorBDEsportsException ex) {
                    JOptionPane.showMessageDialog(frame,"Error al afegir membre: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        botoEliminarJugador.addActionListener(e -> {
            int filaSeleccionada = taulaActuals.getSelectedRow();
            if (filaSeleccionada != -1) {
                try {
                    String nom = (String) modelActuals.getValueAt(filaSeleccionada, 0);
                    String cognoms = (String) modelActuals.getValueAt(filaSeleccionada, 1);
                    
                    Membre membreEliminar = null;
                    for (Membre membre : membresActuals) {
                        if (membre.getJugador().getNom().equals(nom) && membre.getJugador().getCognoms().equals(cognoms)) {
                            membreEliminar = membre;
                            break;
                        }
                    }
                    
                    if (membreEliminar != null) {
                        persistencia.eliminarMembre(membreEliminar);
                        
                        modelActuals.removeRow(filaSeleccionada);
                        
                        Jugador jugador = membreEliminar.getJugador();
                        modelDisponibles.addRow(new Object[]{//igual
                            jugador.getId(),
                            jugador.getNom(),
                            jugador.getCognoms(),
                            obtenirTextCategoria(calcularCategoriaJugador(jugador.getDataNaix())),
                            false
                        });
                    }
                } catch (GestorBDEsportsException ex) {
                    JOptionPane.showMessageDialog(frame,"Error al eliminar membre: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        botoGuardar.addActionListener(e -> {
            try {
                persistencia.confirmarCanvis();
                
                modelTaulaEquip.setRowCount(0); 
                
                List<Membre> membres = persistencia.obtenirMembresDEquip(equip.getId());
                for (Membre membre : membres) {
                    modelTaulaEquip.addRow(new Object[]{
                        membre.getJugador().getNom(),
                        membre.getJugador().getCognoms(),
                        membre.getTipus() == TipusMembre.TITULAR ? "Titular" : "Convidat"}); //igual
                }
                
                JOptionPane.showMessageDialog(frame, "Canvis guardats correctament","Èxit",JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                AfegirEditarEquip.mostrarFormulari(equip, persistencia);
                
            } catch (GestorBDEsportsException ex) {
                JOptionPane.showMessageDialog(frame,"Error al guardar els canvis: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        
        botoCancelar.addActionListener(e -> {
            try {
                persistencia.desferCanvis();
                frame.dispose();
                AfegirEditarEquip.mostrarFormulari(equip, persistencia);
            } catch (GestorBDEsportsException ex) {
                JOptionPane.showMessageDialog(frame,"Error al desfer els canvis: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        
        //aixo explicat a AfegirEditarEquip pero aqui fa rollback()
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    persistencia.desferCanvis();
                    frame.dispose();
                    AfegirEditarEquip.mostrarFormulari(equip, persistencia);
                } catch (GestorBDEsportsException ex) {
                    JOptionPane.showMessageDialog(frame,"Error al desfer els canvis: " + ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        frame.setVisible(true);
    }
    
    private static String obtenirTextCategoria(int idCategoria) throws GestorBDEsportsException {
        try {
            Categoria categoria = persistencia.obtenirCategoria(idCategoria);
            return categoria.getNom();
        } catch (GestorBDEsportsException ex) {
            // Si hi ha error al obtenir la categoria, retorna aquest valor per defecet
            return "Desconeguda";
        }
    }
    
    private static int calcularCategoriaJugador(Date dataNaix) throws GestorBDEsportsException {
        
        List<Categoria> categories = persistencia.obtenirTotesCategories();
        
        Calendar calActual = Calendar.getInstance();
        Calendar calNaix = Calendar.getInstance();
        calNaix.setTime(dataNaix);
        
        int edat = calActual.get(Calendar.YEAR) - calNaix.get(Calendar.YEAR);
        if (calNaix.get(Calendar.MONTH) > calActual.get(Calendar.MONTH) || 
            (calNaix.get(Calendar.MONTH) == calActual.get(Calendar.MONTH) && 
             calNaix.get(Calendar.DAY_OF_MONTH) > calActual.get(Calendar.DAY_OF_MONTH))) {
            edat--;
        }
        
        for (Categoria categoria : categories) {
            if (edat >= categoria.getEdatMin() && edat <= categoria.getEdatMax()) {
                return categoria.getId();
            }
        }
        
        // Si no troba categoria, retornar la més alta (Sènior)
        return 6;
    }
   
}
