/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import boundaryclasses.DbManager;
import coviddataapp.AppSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import model.Country;
import model.Coviddata;

/**
 *
 * 
 */
public class CountryDataView extends javax.swing.JPanel {

    /**
     * Creates new form CountryDataView
     */
    public CountryDataView() {
        initComponents();
    }
    //Έχουν προστεθεί getters για να διαχειρίζεται το GUI ο Presenter
    public JButton getBackToOptionsButton() {
        return backToOptionsButton;
    }

    public List<Coviddata> getConfirmedList() {
        return confirmedList;
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public List<Coviddata> getDeathList() {
        return deathList;
    }

    public JComboBox<Country> getCountryComboBox() {
        return countryComboBox;
    }

    public List<Coviddata> getRecoveredList() {
        return recoveredList;
    }

    public JLabel getConfirmedTabLabel() {
        return confirmedTabLabel;
    }

    public JButton getDatesSearchButton() {
        return datesSearchButton;
    }

    public JLabel getDeathsTabLabel() {
        return deathsTabLabel;
    }

    public JTextField getFromDateTextField() {
        return fromDateTextField;
    }

    public JLabel getRecoveredTabLabel() {
        return recoveredTabLabel;
    }

    public JTextField getToDateTextField() {
        return toDateTextField;
    }

    public JButton getFromDateButton() {
        return fromDateButton;
    }

    public JButton getToDateBuuton() {
        return toDateBuuton;
    }

    public JLabel getDateErrorLabel() {
        return dateErrorLabel;
    }

    public JCheckBox getCheckConfirmed() {
        return checkConfirmed;
    }

    public JCheckBox getCheckDeaths() {
        return checkDeaths;
    }

    public JCheckBox getCheckRecovered() {
        return checkRecovered;
    }

    public JButton getDeleteDataButton() {
        return deleteDataButton;
    }

    public JButton getDiagramButton() {
        return diagramButton;
    }

    public JButton getMapButton() {
        return mapButton;
    }

    public JRadioButton getRadioDay() {
        return radioDay;
    }

    public JRadioButton getRadioProod() {
        return radioProod;
    }

    public JTabbedPane getjTabbedPane1() {
        return jTabbedPane1;
    }

    public JLabel getMapLoadingLabel() {
        return mapLoadingLabel;
    }

    public JDialog getMap() {
        return map;
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        em = DbManager.getEm();
        countryQuery = java.beans.Beans.isDesignTime() ? null : em.createNamedQuery("Country.findByData", Country.class);
        countryList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : countryQuery.getResultList();
        deathList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(new ArrayList<Coviddata>());
        confirmedList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(new ArrayList<Coviddata>());
        recoveredList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(new ArrayList<Coviddata>());
        qntGroup = new javax.swing.ButtonGroup();
        map = new javax.swing.JDialog();
        backToOptionsButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        deathsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        deathsTabLabel = new javax.swing.JLabel();
        recoveredPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        recoveredTabLabel = new javax.swing.JLabel();
        confirmedPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        confirmedTabLabel = new javax.swing.JLabel();
        countryComboBox = new javax.swing.JComboBox<Country>(countryList.toArray(new Country[0]));
        selectCountryLabel = new javax.swing.JLabel();
        Title = new javax.swing.JLabel();
        jLabelFrom = new javax.swing.JLabel();
        fromDateTextField = new javax.swing.JTextField();
        jLabelTo = new javax.swing.JLabel();
        toDateTextField = new javax.swing.JTextField();
        datesSearchButton = new javax.swing.JButton();
        fromDateButton = new javax.swing.JButton();
        toDateBuuton = new javax.swing.JButton();
        dateErrorLabel = new javax.swing.JLabel();
        checkDeaths = new javax.swing.JCheckBox();
        checkRecovered = new javax.swing.JCheckBox();
        checkConfirmed = new javax.swing.JCheckBox();
        radioDay = new javax.swing.JRadioButton();
        radioProod = new javax.swing.JRadioButton();
        diagramButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        mapButton = new javax.swing.JButton();
        deleteDataButton = new javax.swing.JButton();
        mapLoadingLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();

        em.clear();

        for(Country c: countryList) {
            c.setCoviddataList(em.createNamedQuery("Coviddata.findByCountry", Coviddata.class).setParameter("coutryname", c.getName()).getResultList());
        }
        Collections.sort(countryList);

        qntGroup.add(radioDay);
        qntGroup.add(radioProod);
        qntGroup.setSelected(radioDay.getModel(), true);

        map.setModal(true);
        map.setResizable(false);
        map.setIconImage(new ImageIcon(getClass().getResource("/img/map.png")).getImage());
        map.setTitle("Προβολή δεδομένων σε χάρτη");

        map.add(AppSystem.getBrowser());
        map.pack();

        javax.swing.GroupLayout mapLayout = new javax.swing.GroupLayout(map.getContentPane());
        map.getContentPane().setLayout(mapLayout);
        mapLayout.setHorizontalGroup(
            mapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        mapLayout.setVerticalGroup(
            mapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );

        setPreferredSize(new java.awt.Dimension(840, 640));

        backToOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu.png"))); // NOI18N
        backToOptionsButton.setText("<html><p >Κεντρικό</p><p style=\"text-align:center\">Μενού</p></html>");
        backToOptionsButton.setPreferredSize(new java.awt.Dimension(114, 56));

        jTable1.getTableHeader().setReorderingAllowed(false);

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, deathList, jTable1);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${trndate}"));
        columnBinding.setColumnName("Ημερομηνία");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${proodqty}"));
        columnBinding.setColumnName("Σωρευτικό πλήθος");
        columnBinding.setColumnClass(Integer.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${qty}"));
        columnBinding.setColumnName("Ημερήσιο πλήθος");
        columnBinding.setColumnClass(Integer.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        deathsTabLabel.setText("<html><span style=\"color:red\">Δεν έχετε εισάγει δεδομένα θανάτων για αυτήν τη χώρα</span></html>");

        javax.swing.GroupLayout deathsPanelLayout = new javax.swing.GroupLayout(deathsPanel);
        deathsPanel.setLayout(deathsPanelLayout);
        deathsPanelLayout.setHorizontalGroup(
            deathsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deathsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(deathsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deathsTabLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        deathsPanelLayout.setVerticalGroup(
            deathsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deathsPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deathsTabLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        deathsTabLabel.setVisible(false);

        jTabbedPane1.addTab("Θάνατοι", deathsPanel);

        jTable3.getTableHeader().setReorderingAllowed(false);

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, recoveredList, jTable3);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${trndate}"));
        columnBinding.setColumnName("Ημερομηνία");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${proodqty}"));
        columnBinding.setColumnName("Σωρευτικό πλήθος");
        columnBinding.setColumnClass(Integer.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${qty}"));
        columnBinding.setColumnName("Ημερήσιο πλήθος");
        columnBinding.setColumnClass(Integer.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane3.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setResizable(false);
            jTable3.getColumnModel().getColumn(1).setResizable(false);
            jTable3.getColumnModel().getColumn(2).setResizable(false);
        }

        recoveredTabLabel.setText("<html><span style=\"color:red\">Δεν έχετε εισάγει δεδομένα ασθενών που έχουν ανακάμψει για αυτήν τη χώρα</span></html>");

        javax.swing.GroupLayout recoveredPanelLayout = new javax.swing.GroupLayout(recoveredPanel);
        recoveredPanel.setLayout(recoveredPanelLayout);
        recoveredPanelLayout.setHorizontalGroup(
            recoveredPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recoveredPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(recoveredPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recoveredTabLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        recoveredPanelLayout.setVerticalGroup(
            recoveredPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recoveredPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recoveredTabLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        recoveredTabLabel.setVisible(false);

        jTabbedPane1.addTab("Ασθενείς που έχουν ανακάμψει", recoveredPanel);

        jTable2.getTableHeader().setReorderingAllowed(false);

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, confirmedList, jTable2);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${trndate}"));
        columnBinding.setColumnName("Ημερομηνία");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${proodqty}"));
        columnBinding.setColumnName("Σωρευτικό πλήθος");
        columnBinding.setColumnClass(Integer.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${qty}"));
        columnBinding.setColumnName("Ημερήσιο πλήθος");
        columnBinding.setColumnClass(Integer.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
        }

        confirmedTabLabel.setText("<html><span style=\"color:red\">Δεν έχετε εισάγει δεδομένα επιβεβαιωμένων κρουσμάτων για αυτήν τη χώρα</span></html>");

        javax.swing.GroupLayout confirmedPanelLayout = new javax.swing.GroupLayout(confirmedPanel);
        confirmedPanel.setLayout(confirmedPanelLayout);
        confirmedPanelLayout.setHorizontalGroup(
            confirmedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirmedPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(confirmedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(confirmedTabLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        confirmedPanelLayout.setVerticalGroup(
            confirmedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(confirmedPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(confirmedTabLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        confirmedTabLabel.setVisible(false);

        jTabbedPane1.addTab("Επιβεβαιωμένα κρούσματα", confirmedPanel);

        countryComboBox.setSelectedIndex(-1);
        countryComboBox.setToolTipText("Κάντε κλικ για να επιλέξετε Χώρα");

        selectCountryLabel.setText("Επιλέξτε Χώρα");

        Title.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        Title.setText("Προβολή Δεδομένων Covid");

        jLabelFrom.setText("Από:");

        fromDateTextField.setEditable(false);

        jLabelTo.setText("Έως:");

        toDateTextField.setEditable(false);

        datesSearchButton.setText("Αναζήτηση");
        datesSearchButton.setToolTipText("<html>Εμφανίζει τα δεδομένα για το επιλεγμένο εύρος ημερομηνιών<br><span style=\"color:red\">πρέπει η ημερομηνία μέχρι να μην είναι πριν την ημερομηνία έως</span></html>\n");
        datesSearchButton.setEnabled(false);

        fromDateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calendar.png"))); // NOI18N
        fromDateButton.setEnabled(false);

        toDateBuuton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calendar.png"))); // NOI18N
        toDateBuuton.setEnabled(false);

        dateErrorLabel.setText("<html><span style=\"color:red\">Η ημ/νία έως πρέπει να είναι μετά την ημ/νία από</span></html>");

        checkDeaths.setText("Θάνατοι");
        checkDeaths.setEnabled(false);

        checkRecovered.setText("Ανέκαμψαν");
        checkRecovered.setEnabled(false);

        checkConfirmed.setText("Επιβεβ. κρούσματα");
        checkConfirmed.setEnabled(false);

        radioDay.setText("Δεδομένα ημέρας");

        radioProod.setSelected(true);
        radioProod.setText("Σωρευτικά δεδομένα");

        diagramButton.setText("Προβολή σε διάγραμμα");
        diagramButton.setEnabled(false);

        jLabel1.setText("Επιλογές διαγράμματος");

        mapButton.setText("Προβολή σε χάρτη");
        mapButton.setEnabled(false);

        deleteDataButton.setText("Διαγραφή δεδομένων");
        deleteDataButton.setEnabled(false);

        mapLoadingLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ajax-loader1.gif"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelFrom)
                                        .addGap(18, 18, 18)
                                        .addComponent(fromDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabelTo)
                                        .addGap(18, 18, 18)
                                        .addComponent(toDateTextField)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(toDateBuuton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fromDateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(selectCountryLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(140, 140, 140)
                                .addComponent(datesSearchButton))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(42, 42, 42))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(47, 47, 47))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(checkDeaths, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(checkRecovered, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(checkConfirmed)
                                    .addComponent(radioDay)
                                    .addComponent(radioProod))
                                .addGap(109, 109, 109))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(diagramButton)
                                .addGap(105, 105, 105))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(mapButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mapLoadingLabel)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(Title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backToOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Title)
                    .addComponent(backToOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selectCountryLabel))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fromDateButton)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelFrom)
                                .addComponent(fromDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(toDateBuuton)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelTo)
                                .addComponent(toDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(datesSearchButton)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(16, 16, 16)
                        .addComponent(checkDeaths)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkRecovered)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkConfirmed)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioDay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioProod)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(diagramButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mapButton)
                            .addComponent(mapLoadingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteDataButton)
                        .addGap(38, 38, 38))))
        );

        dateErrorLabel.setVisible(false);
        if(deathList.isEmpty()) checkDeaths.setEnabled(false);
        if(recoveredList.isEmpty()) checkRecovered.setEnabled(false);
        if(confirmedList.isEmpty()) checkConfirmed.setEnabled(false);
        mapLoadingLabel.setVisible(false);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Title;
    private javax.swing.JButton backToOptionsButton;
    private javax.swing.JCheckBox checkConfirmed;
    private javax.swing.JCheckBox checkDeaths;
    private javax.swing.JCheckBox checkRecovered;
    private java.util.List<Coviddata> confirmedList;
    private javax.swing.JPanel confirmedPanel;
    private javax.swing.JLabel confirmedTabLabel;
    private javax.swing.JComboBox<Country> countryComboBox;
    private java.util.List<Country> countryList;
    private javax.persistence.Query countryQuery;
    private javax.swing.JLabel dateErrorLabel;
    private javax.swing.JButton datesSearchButton;
    private java.util.List<Coviddata> deathList;
    private javax.swing.JPanel deathsPanel;
    private javax.swing.JLabel deathsTabLabel;
    private javax.swing.JButton deleteDataButton;
    private javax.swing.JButton diagramButton;
    private javax.persistence.EntityManager em;
    private javax.swing.JButton fromDateButton;
    private javax.swing.JTextField fromDateTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JDialog map;
    private javax.swing.JButton mapButton;
    private javax.swing.JLabel mapLoadingLabel;
    private javax.swing.ButtonGroup qntGroup;
    private javax.swing.JRadioButton radioDay;
    private javax.swing.JRadioButton radioProod;
    private java.util.List<Coviddata> recoveredList;
    private javax.swing.JPanel recoveredPanel;
    private javax.swing.JLabel recoveredTabLabel;
    private javax.swing.JLabel selectCountryLabel;
    private javax.swing.JButton toDateBuuton;
    private javax.swing.JTextField toDateTextField;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
