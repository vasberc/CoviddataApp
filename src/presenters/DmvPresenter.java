package presenters;

import boundaryclasses.HtmlWriter;
import coviddataapp.AppSystem;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import javafx.application.Platform;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import model.Country;
import model.Coviddata;
import views.DataMapView;
import views.DatePicker;

/**
 *
 * 
 */
public class DmvPresenter implements Presenter{//DataMapView Presenter
    
    

    private  final DataMapView view;
    private final AppSystem appSystem;    
    private JComponent[] buttons;
    private final TreeSet<Date> dateLimitData = new TreeSet<>();
    private final List<Country> selectedCountries = new ArrayList<>();
    private boolean writerError;
    
    public DmvPresenter(final JPanel view, final AppSystem appSystem) {
		//Κάνει cast το JPanel στην κλάση του View
        this.view = (DataMapView) view;
		//Θέτει το σύστημα για να μπορεί να κάνει ενέργειες όταν πατάει ο χρήστης κουμπιά 
        this.appSystem = appSystem; 
        setUpViewEvents();
        
    }

    @Override
    public final void setUpViewEvents() {//Ορίζει τα Event του View        
        buttons = new JComponent[]{
            view.getBackToOptionsButton(),//0 = BackToOptionsButton
            view.getCountriesComboBox(),//  1 = CountriesComboBox
            view.getFromButton(),//         2 = FromButton
            view.getToButton(),//           3 = ToButton
            view.getShowMapButton()//       4 = ShowMapButton            
        };
        //Αρχικοποίηση των οριακών ημερομηνιών
        for(Country c: view.getMainCountryList()) {
            TreeSet<Coviddata> countryData = new TreeSet<>();
            countryData.addAll(c.getCoviddataList());
            dateLimitData.add(countryData.first().getTrndate());
            dateLimitData.add(countryData.last().getTrndate());
        }
        //Αρχικοποίηση των textField με τις οριακές ημερομηνίες
        if(!dateLimitData.isEmpty()) {
                JTextField fromDate = view.getFromTextField();
                JTextField toDate = view.getToTextField();
                //Πάρε την πρώτη ημερομηνία
                LocalDate localdate = Instant.ofEpochMilli(dateLimitData.first().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                //Θέσε το κείμενο στο fromDate σύμφωνα με την 1η ημερομηνία
                fromDate.setText(localdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                //Πάρε την τελευταία ημερομηνία
                localdate = Instant.ofEpochMilli(dateLimitData.last().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                //Θέσε το κείμενο στο toDate σύμφωνα με την τελευταία ημερομηνία
                toDate.setText(localdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        //Επιλογέας χώρας   
        ((JComboBox)buttons[1]).addActionListener((ActionEvent e) -> {
            List<Country> allCountries = view.getMainCountryList();
            List<Country> restCountries = view.getRestCountriesList(); 
            //Η χώρα που επέλεξε ο χρήστης
            Country selected = (Country)((JComboBox)buttons[1]).getSelectedItem();
            JPanel mapPanel = view.getMapPanel();  
            //Για να αφαιρέσω την επιλεγμένη χώρα από την λίστα με τις υπόλοιπες ΄χωρες
            restCountries.clear();
            restCountries.addAll(allCountries);            
            restCountries.remove(selected);
            //Για να ανανεώσει το view
            view.getCountriesJList().repaint();
            selectedCountries.clear();
            //Στις επιλεγμένες χώρες στην θέση 0 μπαίνει η κύρια χώρα
            selectedCountries.add(0, selected);
            //όποτε αλλάζει κύρια ΄χώρα ο χρήστης καθαρίζω τις επιλογές στις υπόλοιπες χώρες
            view.getCountriesJList().clearSelection();
            //Μετά την επιλογή χώρας ενεργοποιούνται τα κουμπιά
            for(int i=2; i<=4; i++) {
                buttons[i].setEnabled(true);
            }
        });
        //κουμπί from
        ((JButton) buttons[2]).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Οριακές ημερομηνίες ημερολογίου, σύμφωνα με τις οριακές ημερομηνίες των δεδομένων
                LocalDate fromDate = Instant.ofEpochMilli(dateLimitData.first().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate toDate = Instant.ofEpochMilli(dateLimitData.last().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                //Τρέχουσα επιλεγμένη ημερομηνία
                LocalDate currentDate = toLocalDate(view.getFromTextField().getText());            
                //Κάλεσε το DatePicker
                String fromDateString = new DatePicker(view.getFromTextField(), fromDate, toDate, currentDate).setPickedDate();
                //Εάν ο χρήστης δεν έκλεισε το παράθυρο
                if(!fromDateString.equals(""))
                    //Θέσε το κείμενο με την ημερομηνία που επέλεξε ο χρήστης
                    view.getFromTextField().setText(fromDateString);            
                //Έλεγξε την ενεργοποίηση του searchButton
                showMapButtonEnability();
            }
        });
        //Κουμπί to
        ((JButton) buttons[3]).addActionListener((ActionEvent e) -> {
            //Οριακή ημερομηνία σύμφωνα με την επιλογή του fromDate
            String fromDateString = view.getFromTextField().getText();
            LocalDate fromDate = toLocalDate(fromDateString);
            //Οριακή ημερομηνία σύμφωνα με τα δεδομένα
            LocalDate toDate = Instant.ofEpochMilli(dateLimitData.last().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            //Τρέχουσα επιλεγμένη ημερομηνία
            LocalDate currentDate = toLocalDate(view.getToTextField().getText()); 
            //Κάλεσε το DatePicker            
            String toDateString = new DatePicker(view.getFromTextField(), fromDate, toDate, currentDate).setPickedDate();
            //Εάν ο χρήστης δεν έκλεισε το παράθυρο
            if(!toDateString.equals(""))
                //Θέσε το κείμενο με την ημερομηνία που επέλεξε ο χρήστης
                view.getToTextField().setText(toDateString);
            //Έλεγξε την ενεργοποίηση του searchButton
            showMapButtonEnability();
        });
        //κουμπί προβολή χάρτη
        ((JButton) buttons[4]).addActionListener(new ActionListener() {
            JPanel mapPanel = view.getMapPanel(); 
            @Override
            public void actionPerformed(ActionEvent e) {
                //Νήμα που εκτελεί εργασίες εκτός από αλλαγές στα γραφικά
                new SwingWorker<Void, Void> (){
                    boolean finished = false;     
                    @Override
                    //Η μέθοδος εκτελείται στο working thread
                    protected Void doInBackground() throws InterruptedException {
                        Date from = java.sql.Date.valueOf(toLocalDate(view.getFromTextField().getText()));
                        Date to = java.sql.Date.valueOf(toLocalDate(view.getToTextField().getText())); 
                        //Διαπερνάει όλες τις επιλεγμένες χώρες από τις υπόλοιπες χώρες
                        for(int i: view.getCountriesJList().getSelectedIndices())
                            //τις βάζει στη λίστα μαζί με την βασική χώρα
                            selectedCountries.add((Country) view.getRestCountriesList().get(i));
                        //Δημιουργία λίστα που θα στείλουμε στο htmlwriter
                        List<Country> toHtmlCountries = new ArrayList<>();
                        //Εάν ο χρήστης έχει αλλάξει τις επιθυμητές ημερομηνίες
                        if(!from.equals(dateLimitData.first()) || !to.equals(dateLimitData.last())) {
                            //Διαπερνάμε όλες τις επιλεγμένες χώρες
                            for(Country country: selectedCountries) {
                                //Δημιουργία νέας χώρας
                                Country c = new Country();
                                //με ίδιο id
                                c.setCountry(country.getCountry());
                                //ίδιο όνομα
                                c.setName(country.getName());
                                //ίδιες συντεταγμένες
                                c.setLat(country.getLat());
                                c.setLong1(country.getLong1());
                                //Τροποποίηση της λίστας με τα coviddata
                                List<Coviddata> newList = new ArrayList<>(); 
                                //Διαπερνάμε τα coviddata της χώρας 
                                for(Coviddata data: country.getCoviddataList()) {
                                    //Προσθέτουμε μόνο αυτά που είναι ανάμεσα στις ημερομηνίες που θέλουμε
                                    if(!data.getTrndate().before(from) && !data.getTrndate().after(to))
                                        newList.add(data);
                                }
                                //θέτουμε στην νέα χώρα την λίστα
                                c.setCoviddataList(newList);
                                //προσθέτουμε την χώρα στη λίστα που θα πάει για το htmlwriter
                                toHtmlCountries.add(c);
                            }
                        }
                        //Αν ο χρήστης έχει επιλέξει τις αρχικές ημρ/νιες
                        else
                            //η λίστα για το html δείχνει στην λίστα των επιλεγμένων χωρών
                            toHtmlCountries = selectedCountries;
                        //Κατάσταση εγγραφής αρχείου
                        writerError = true; 
                        System.out.println("Πάει για writer");
                        //Κάλεσε την writer να γράψει το Hmtl για την χώρα και ζουμ 5
                        HtmlWriter.writer(toHtmlCountries, 2, 834, 441);
                        System.out.println("τελείωσε writer writerError="+writerError);
                        //Δίνω χρόνο να κλείσει ο buffer για να ανοίξει το σωστό αρχείο
                        Thread.sleep(2000);
                        //Αν η διαδικασία τελείωσε κανονικά                           
                        String link = null;                            
                        link = new File("html\\mappage.html").toURI().toString();                            
                        final String linkToLoad = link;
                        //Στο thread του Jfx application
                        Platform.runLater(() -> { 
                            System.out.println("Πάει για loadlink");                            
                            //Κάλεσε την loadLink για να φορτώσει το λινκ που στέλνω
                            AppSystem.getBrowser().loadLink(linkToLoad, new Dimension(834, 441));
                            //Αν έχει τελειώσει το thread με την φόρτωση του λινκ θέτει το finished true
                            finished = true;   
                            System.out.println("τελείωσε loadlink, finished="+finished);
                        });
                        //Αυτοσχέδιος μηχανισμός συγχρονισμού 2 thread
                        //Μπλοκάρω το working thread μέχρι να τελειώσει το platform thread
                        int i = 0;
                        //Όσο το platform thread δεν έχει τελειώσει
                        while(!finished) {
                            //Αν πέρασαν 5 δευτερόλεπτα
                            if(i>=5) {
                                //Δώσε σφάλμα εγγραφής
                                writerError = true;
                                //Βγες από τη while
                                break;
                            } 
                            //Αύξησε κατά 1
                            i++;
                            //Περίμενε 1 δευτερόλεπτο
                            Thread.sleep(1000);
                             System.out.println("είναι στη while");
                        }
                      
                        return null;
                    }

                    @Override
                    //Όταν τελειώσει το working thread
                    protected void done() {
                        System.out.println("Μπήκε done");
                        if(!writerError) {
                            //κρύβω το LoadingLabel
                            view.getLoadingLabel().setVisible(false);
                            //Το αφαιρώ από το πάνελ μαζί με το text
                            mapPanel.remove(view.getInfoTextLabel());
                            mapPanel.remove(view.getLoadingLabel());
                            mapPanel.repaint();
                            //Βάζω στο πάνελ το JFX Πάνελ με τον browser
                            mapPanel.add(AppSystem.getBrowser());
                            //Καθαρίζω την λίστα με τις επιλεγμένες χώρες για την επόμενη επιλογή του χρήστη
                            selectedCountries.clear();
                            //Καθαρίζω τον επιλογέα βασικής χώρας
                            ((JComboBox)buttons[1]).setSelectedIndex(-1);
                            //Καθαρίζω το jlist με τις επιλογές υπόλοιπων χωρών
                            view.getCountriesJList().clearSelection();
                            //Εμφάνισε τα κουμπιά
                            enableAllButtons();
                        }
                        else {
                            JOptionPane.showMessageDialog(view, "<html>Παρουσιάστηκε σφάλμα κατά την δημιουργία του χάρτη.<br>"
                                                                    + "Παρακαλώ  επιλέξτε πάλι προβολή σε χάρτη!!!</html>", "Σφάλμα Χάρτη", JOptionPane.ERROR_MESSAGE);
                            //Κρύψε το loading label
                            view.getLoadingLabel().setVisible(false);
                            //Εμφάνισε τα κουμπιά
                            enableAllButtons(); 
                        }
                    }
                }.execute();
                //Εκτελείται με το πάτημα του κουμπιού και κρύβει τον browser μέχρι να γραφτεί το αρχείο 
                mapPanel.remove(AppSystem.getBrowser());
                mapPanel.repaint();
                mapPanel.add(view.getLoadingLabel()); 
                //Θέσε ορατό το loading label όταν ξεκινήσει το νήμα
                view.getLoadingLabel().setVisible(true);
                //Απενεργοποίησε όλα τα κουμπιά
                desableAllButtons();
            }
        });
        //Κουμπί πίσω στο μενού
		//Εσωτερική ανώνυμη κλάση ActionListener με lamda
        ((JButton)buttons[0]).addActionListener((ActionEvent e) -> {
			//Καλεί τον mainFrame να αλλάξει View
            appSystem.getMainFrame().showOptionsView();
			//Δημιουργεί τον Presenter για το νέο View
            Presenter p = new OvPresenter( appSystem.getMainFrame().getPanel(), appSystem);
			//Στέλνει στο σύστημα τον νέο Presenter και τον Παλιό για ενέργειες
            appSystem.setPresenter(p, appSystem.getPresenter());
        });
        
    }
    //Απενεργοποιεί όλα τα κουμπιά
    private void desableAllButtons() {
        for(JComponent j: buttons)
            j.setEnabled(false);
    }
    //Ενεργοποιεί όλα τα κουμπιά
    private void enableAllButtons() {
        for(JComponent j: buttons)
            j.setEnabled(true);
        showMapButtonEnability();
    }
    //Ενεργοποιεί το showΜapButton ανάλογα την περίσταση
    private void showMapButtonEnability() {
        //Αρχικοποίηση μεταβλητών
        JTextField fromField = view.getFromTextField();
        JTextField toField = view.getToTextField();        
        //Εάν κάποιο από τα 2 field είναι κενό
        if(fromField.equals("") || toField.equals(""))
            //Απενεργοποίησε το κουμπί
            buttons[4].setEnabled(false);
        //Εαν δεν υπάρχει επιλεγμένη χώρα
        else if(view.getCountriesComboBox().getSelectedIndex() == -1)
            //Απενεργοποίησε το κουμπί
            buttons[4].setEnabled(false);
        //Εάν οι ημερομηνία από είναι μετά την ημερομηνία μέχρι
        else if(!toLocalDate(fromField.getText()).isBefore(toLocalDate(toField.getText()))){
            //Απενεργοποίησε το κουμπί
            buttons[4].setEnabled(false);
            //Ενεργοποίησε το label
            view.getDateErrorLabel().setVisible(true);
        }
        else {//σε κάθε άλλη περίπτωση
            buttons[4].setEnabled(true);//Ενεργοποίησε το κουμπί
            view.getDateErrorLabel().setVisible(false);//Απενεργοποίησε το label
        }
    }
    //Μετατροπή ημερομηνίας String σε LocalDate
    private LocalDate toLocalDate (String date) {        
        return LocalDate.of(Integer.parseInt(date.substring(date.lastIndexOf("/")+1)),//Έτος
                            Integer.parseInt(date.substring(date.indexOf("/")+1, date.lastIndexOf("/"))),//Μήνας
                            Integer.parseInt(date.substring(0, date.indexOf("/"))));//Μέρα
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {//Διαχειρίζεται το GUI σε σχέση με τις αλλαγές στο σύστημα
        String propName = evt.getPropertyName();
        Object newVal = evt.getNewValue();
        Object oldValue = evt.getOldValue();
        
        //Στέλνει το αποτέλεσμα του γραψίματος του αρχείου
        if("Html writer, oldVal->true, newVal->hasIOError".equalsIgnoreCase(propName)){            
                writerError = (boolean)newVal;
            
            
        }
    }
}

