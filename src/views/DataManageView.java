/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import javax.swing.JButton;
import boundaryclasses.DbManager;
import coviddataapp.CovidDataType;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import model.Country;
import model.Coviddata;

/**
 *
 * 
 */
public class DataManageView extends javax.swing.JPanel {
    //Έχουν προστεθεί getters για να διαχειρίζεται το GUI ο Presenter
    public JButton getBackToOptionsButton() {
        return backToOptionsButton;
    }

    public JButton getDeleteCountryButton() {
        return deleteCountryButton;
    }

    public JButton getDeleteDataButton() {
        return deleteDataButton;
    }

    public JLabel getInfoText() {
        return infoText;
    }

    public JButton getInsertCountryButton() {
        return insertCountryButton;
    }

    public JButton getInsertDataButton() {
        return insertDataButton;
    }

    public JComboBox<CovidDataType> getTypeComboBoc() {
        return typeComboBoc;
    }

    public JList<String> getCoutryJList() {
        return coutryJList;
    }  
    
    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
    }   

    public List<Country> getCountryList() {
        return countryList;
    }

    public List<Coviddata> getCoviddataList() {
        return coviddataList;
    }

    public JTable getDataTable() {
        return dataTable;
    }

    public JButton getDeleteAll() {
        return deleteAll;
    }    

    public JLabel getDataLabel() {
        return dataLabel;
    }

    public JLabel getLoadingLabel() {
        return loadingLabel;
    }

    public JList<Country> getjServerList() {
        return jServerList;
    }

    public List<Country> getServerList() {
        return serverList;
    }
    
    public DataManageView() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        em = DbManager.getEm();
        countryQuery = java.beans.Beans.isDesignTime() ? null : em.createQuery("SELECT c FROM Country c", Country.class);
        countryList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(countryQuery.getResultList());
        coviddataList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(new ArrayList<>());
        serverList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(new ArrayList<>());
        backToOptionsButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        insertCountryButton = new javax.swing.JButton();
        deleteCountryButton = new javax.swing.JButton();
        insertDataButton = new javax.swing.JButton();
        deleteDataButton = new javax.swing.JButton();
        Title = new javax.swing.JLabel();
        coutriesTitle = new javax.swing.JLabel();
        dataTitle = new javax.swing.JLabel();
        typeComboBoc = new javax.swing.JComboBox<CovidDataType>(new CovidDataType[] { CovidDataType.DEATHS, CovidDataType.RECOVERED, CovidDataType.CONFIRMED});
        dataTypeLable = new javax.swing.JLabel();
        infoText = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        coutryJList = new javax.swing.JList<>();
        deleteAll = new javax.swing.JButton();
        loadingLabel = new javax.swing.JLabel();
        dataLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jServerList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();

        em.clear();

        if(!countryList.isEmpty()) countryList.sort((Country c1, Country c2) -> c1.getName().compareTo(c2.getName()));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setMaximumSize(null);
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(840, 640));

        backToOptionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/menu.png"))); // NOI18N
        backToOptionsButton.setText("<html><p >Κεντρικό</p><p style=\"text-align:center\">Μενού</p></html>");
        backToOptionsButton.setToolTipText("Κάντε κλίκ για να επιστρέψετε στο κεντρικό μενού");
        backToOptionsButton.setPreferredSize(new java.awt.Dimension(114, 56));

        dataTable.setToolTipText("Είδος δεδομένων (1 = Θάνατοι, 2 = Ασθενείς που έχουν ανακάμψει, 3 = Επιβεβαιωμένα κρούσματα)");
        dataTable.getTableHeader().setReorderingAllowed(false);

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, coviddataList, dataTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${country.name}"));
        columnBinding.setColumnName("Όνομα Χώρας");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${datakind}"));
        columnBinding.setColumnName("Είδος");
        columnBinding.setColumnClass(Short.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${trndate}"));
        columnBinding.setColumnName("Έως");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane2.setViewportView(dataTable);
        if (dataTable.getColumnModel().getColumnCount() > 0) {
            dataTable.getColumnModel().getColumn(0).setResizable(false);
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(55);
            dataTable.getColumnModel().getColumn(1).setResizable(false);
            dataTable.getColumnModel().getColumn(1).setPreferredWidth(15);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(30);
        }

        insertCountryButton.setText("Λήψη δεδομένων");
        insertCountryButton.setToolTipText("<html>Κάντε κλικ για να γίνει λήψη των δεδομένων από τον server<br><span style=\"color:red\">Οι διαθέσιμες χώρες θα αποθηκευτούν αυτόματα</span></html>");

        deleteCountryButton.setText("Διαγραφή Χώρων");
        deleteCountryButton.setToolTipText("<html>Κάντε κλικ για να διαγράψετε από την βάση δεδομένων την επιλεγμένη χώρα<br><span style=\"color:red\">Κατά την διαγραφή της χώρας θα διαγραφούν και τα δεδομένα που αναφέρονται σε αυτήν</span></html");

        insertDataButton.setText("Εισαγωγή >>");
        insertDataButton.setToolTipText("<html>Eισάγει δεδομένα στη βάση, για την επιλεγμένη χώρα<br><span style=\"color:red\"> (πρέπει να υπάρχει η χώρα στην βάση δεδομένων)</span></html>");

        deleteDataButton.setText("Διαγραφή δεδομένων");
        deleteDataButton.setToolTipText("Κάντε κλικ για να διαγράψετε τα δεδομένα τις επιλεγμένης χώρας");

        Title.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        Title.setText("Διαχείριση Βάσης Δεδομένων");

        coutriesTitle.setText("Χώρες Αποθηκευμένες στη Βάση Δεδομένων");
        coutriesTitle.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        dataTitle.setText("Δεδομένα που έχουν αποθηκευτεί στη βάση δεδομένων");
        dataTitle.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        typeComboBoc.setMaximumRowCount(3);
        typeComboBoc.setToolTipText("Επιλέξτε είδος δεδομένων");

        dataTypeLable.setLabelFor(insertCountryButton);
        dataTypeLable.setText("Επιλέξτε το είδος δεδομένων για λήψη ");
        dataTypeLable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        infoText.setBackground(new java.awt.Color(255, 255, 255));
        infoText.setForeground(java.awt.Color.blue);
        infoText.setText("Πηγαίνετε το ποντίκι πάνω σε κάποιο κουμπί για να δείτε πληροφορίες");

        org.jdesktop.swingbinding.JListBinding jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, countryList, coutryJList);
        jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        bindingGroup.addBinding(jListBinding);

        jScrollPane3.setViewportView(coutryJList);

        deleteAll.setText("Διαγραφή όλων των δεδομένων");
        deleteAll.setToolTipText("<html><span style=\"color:red\">Κάντε κλικ για να διαγράψετε όλα τα δεδομένα από τη βάση χώρα</span></html");

        loadingLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ajax-loader.gif"))); // NOI18N

        dataLabel.setForeground(java.awt.Color.red);
        dataLabel.setText("Δεν υπάρχουν δεδομένα για εισαγωγή, παρακαλώ πατήστε λήψη δεδομένων");

        jServerList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jListBinding = org.jdesktop.swingbinding.SwingBindings.createJListBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, serverList, jServerList);
        jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        bindingGroup.addBinding(jListBinding);

        jScrollPane1.setViewportView(jServerList);

        jLabel1.setText("Αποτελέσματα αναζήτησης");
        jLabel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(typeComboBoc, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(insertCountryButton))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(23, 23, 23)
                                .addComponent(insertDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dataTypeLable, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(coutriesTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(dataTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(loadingLabel)
                                    .addComponent(deleteDataButton))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(Title)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(backToOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(deleteCountryButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteAll))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 73, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(infoText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backToOptionsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Title))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataTypeLable)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeComboBoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insertCountryButton)
                    .addComponent(dataTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(78, 78, 78)
                                .addComponent(insertDataButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(coutriesTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteDataButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deleteCountryButton)
                            .addComponent(deleteAll))
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loadingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(infoText, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dataLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        coutriesTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        dataTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadingLabel.setVisible(false);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Title;
    private javax.swing.JButton backToOptionsButton;
    private java.util.List<Country> countryList;
    private javax.persistence.Query countryQuery;
    private javax.swing.JLabel coutriesTitle;
    private javax.swing.JList<String> coutryJList;
    private java.util.List<Coviddata> coviddataList;
    private javax.swing.JLabel dataLabel;
    private javax.swing.JTable dataTable;
    private javax.swing.JLabel dataTitle;
    private javax.swing.JLabel dataTypeLable;
    private javax.swing.JButton deleteAll;
    private javax.swing.JButton deleteCountryButton;
    private javax.swing.JButton deleteDataButton;
    private javax.persistence.EntityManager em;
    private javax.swing.JLabel infoText;
    private javax.swing.JButton insertCountryButton;
    private javax.swing.JButton insertDataButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<Country> jServerList;
    private javax.swing.JLabel loadingLabel;
    private java.util.List<Country> serverList;
    private javax.swing.JComboBox<CovidDataType> typeComboBoc;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
