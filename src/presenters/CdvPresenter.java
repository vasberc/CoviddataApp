package presenters;

import boundaryclasses.DbManager;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import model.Country;
import model.Coviddata;
import views.CountryDataView;
import views.DataChartModal;
import views.DatePicker;

/**
 *
 * 
 */
public class CdvPresenter implements Presenter{//CountryDataView Presenter
    
    private  final CountryDataView view;
    private final AppSystem appSystem;
    private JComponent[] buttons;
    private final DbManager dBm;
    private final TreeSet<Date> dateLimitData = new TreeSet<>();
    private boolean writerError;
    
    public CdvPresenter(final JPanel view, final AppSystem appSystem) {        
        this.dBm = appSystem.getDbManager();
        this.view = (CountryDataView) view;//Κάνει cast το JPanel στην κλάση του View
        this.appSystem = appSystem; //Θέτει το σύστημα για να μπορεί να κάνει ενέργειες όταν πατάει ο χρήστης κουμπιά         
        setUpViewEvents();
        
    }
    
    @Override
    public final void setUpViewEvents() {//Ορίζει τα Event του View
        buttons = new JComponent[]{
            view.getBackToOptionsButton(),//0 = BackToOptionsButton
            view.getCountryComboBox(),//    1 = CountryComboBox
            view.getFromDateButton(),//     2 = FromDateButton
            view.getToDateBuuton(),//       3 = ToDateBuuton
            view.getDatesSearchButton(),//  4 = DatesSearchButton
            view.getCheckDeaths(),//        5 = CheckDeaths
            view.getCheckRecovered(),//     6 = CheckRecovered
            view.getCheckConfirmed(),//     7 = CheckConfirmed
            view.getRadioDay(),//           8 = RadioDay
            view.getRadioProod(),//         9 = RadioProod
            view.getDiagramButton(),//     10 = DiagramButton
            view.getMapButton(),//         11 = MapButton
            view.getDeleteDataButton()//   12 = DeleteDataButton()
        };
        
        //Επιλογέας χώρας
        ((JComboBox) buttons[1]).addActionListener((ActionEvent e) -> {
            //Εάν ο χρήστης έχει επιλέξει κάποια χώρα
            if(((JComboBox)buttons[1]).getSelectedIndex() != -1) {
                //Καθάρισε τις οριακές ημερομηνίες
                dateLimitData.clear();
                List<Coviddata> deathList = view.getDeathList();
                List<Coviddata> confirmedList = view.getConfirmedList();
                List<Coviddata> recoveredList = view.getRecoveredList();
                List<Integer> tabsWithData = new ArrayList<>();
                //Καθάρισε τις observable λίστες
                deathList.clear();
                confirmedList.clear();
                recoveredList.clear();
                //Πάρε το countryName που επέλεξε ο χρήστης
                Country country = (Country) view.getCountryComboBox().getSelectedItem();                
                //Βάλε όλες τις εγγραφές από το query στο deathList
                deathList.addAll(dBm.findByCountryNtype(country,1));
                //Αν δεν είναι άδεια η λίστα
                if(!deathList.isEmpty()) {
                    //Βάλε στις οριακές ημερομηνίες την πρώτη και την τελευταία ημερομηνία
                    dateLimitData.add(deathList.get(0).getTrndate());
                    dateLimitData.add(deathList.get(deathList.size()-1).getTrndate());
                    //Κρύψε το label
                    view.getDeathsTabLabel().setVisible(false);
                    //Βάλε τον δείκτη του tab για θανάτους στη λίστα
                    tabsWithData.add(0);
                }  
                else//Αλλιώς εμφάνισε το label 
                    view.getDeathsTabLabel().setVisible(true);
                //Κάνε τα ίδια για το recoveredList
                recoveredList.addAll(dBm.findByCountryNtype(country,2));
                if(!recoveredList.isEmpty()) {
                    dateLimitData.add(recoveredList.get(0).getTrndate());
                    dateLimitData.add(recoveredList.get(recoveredList.size()-1).getTrndate());
                    view.getRecoveredTabLabel().setVisible(false);
                    tabsWithData.add(1);
                }   
                else 
                    view.getRecoveredTabLabel().setVisible(true);
                //Κάνε τα ίδια για το confirmedList
                confirmedList.addAll(dBm.findByCountryNtype(country,3));
                if(!confirmedList.isEmpty()) {
                    dateLimitData.add(confirmedList.get(0).getTrndate());
                    dateLimitData.add(confirmedList.get(confirmedList.size()-1).getTrndate());
                    view.getConfirmedTabLabel().setVisible(false);
                    tabsWithData.add(2);
                } 
                else
                    view.getConfirmedTabLabel().setVisible(true);
                //Εάν έχουμε οριακές ημερομηνίες, άρα και δεδομένα
                if(!dateLimitData.isEmpty()) {
                    JTextField fromDate = view.getFromDateTextField();
                    JTextField toDate = view.getToDateTextField();
                    //Πάρε την πρώτη ημερομηνία
                    LocalDate localdate = Instant.ofEpochMilli(dateLimitData.first().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                    //Θέσε το κείμενο στο fromDate σύμφωνα με την 1η ημερομηνία
                    fromDate.setText(localdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    //Πάρε την τελευταία ημερομηνία
                    localdate = Instant.ofEpochMilli(dateLimitData.last().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                    //Θέσε το κείμενο στο toDate σύμφωνα με την τελευταία ημερομηνία
                    toDate.setText(localdate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    //Ενεργοποίησε το κουμπί fromDate
                    buttons[2].setEnabled(true);
                    //Ενεργοποίησε το κουμπί toDate
                    buttons[3].setEnabled(true);
                    //Έλεγξε την ενεργοποίηση των κουμπιών
                    allButtonsEnability();
                    //Ενεργοποίησε το κουμπί του χάρτη
                    buttons[11].setEnabled(true);
                    //Ενεργοποίησε το κουμπί για διαγραφή δεδομένων
                    buttons[12].setEnabled(true);
                    //Θέσε επιλεγμένο tab το πρώτο που έχει δεδομένα
                    view.getjTabbedPane1().setSelectedIndex(tabsWithData.get(0));
                }
            }
        });
        //Κουμπί fromDate
        ((JButton) buttons[2]).addActionListener((ActionEvent e) -> {
            //Οριακές ημερομηνίες ημερολογίου, σύμφωνα με τις οριακές ημερομηνίες των δεδομένων
            LocalDate fromDate = Instant.ofEpochMilli(dateLimitData.first().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate toDate = Instant.ofEpochMilli(dateLimitData.last().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            //Τρέχουσα επιλεγμένη ημερομηνία
            LocalDate currentDate = toLocalDate(view.getFromDateTextField().getText());            
            //Κάλεσε το DatePicker
            String fromDateString = new DatePicker(view.getFromDateTextField(), fromDate, toDate, currentDate).setPickedDate();
            //Εάν ο χρήστης δεν έκλεισε το παράθυρο
            if(!fromDateString.equals(""))
                //Θέσε το κείμενο με την ημερομηνία που επέλεξε ο χρήστης
                view.getFromDateTextField().setText(fromDateString);            
            //Έλεγξε την ενεργοποίηση του searchButton
            searchButtonEnability();
        });
        //Κουμπί toDate
        ((JButton) buttons[3]).addActionListener((ActionEvent e) -> {
            //Οριακή ημερομηνία σύμφωνα με την επιλογή του fromDate
            String fromDateString = view.getFromDateTextField().getText();
            LocalDate fromDate = toLocalDate(fromDateString);
            //Οριακή ημερομηνία σύμφωνα με τα δεδομένα
            LocalDate toDate = Instant.ofEpochMilli(dateLimitData.last().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            //Τρέχουσα επιλεγμένη ημερομηνία
            LocalDate currentDate = toLocalDate(view.getToDateTextField().getText()); 
            //Κάλεσε το DatePicker            
            String toDateString = new DatePicker(view.getFromDateTextField(), fromDate, toDate, currentDate).setPickedDate();
            //Εάν ο χρήστης δεν έκλεισε το παράθυρο
            if(!toDateString.equals(""))
                //Θέσε το κείμενο με την ημερομηνία που επέλεξε ο χρήστης
                view.getToDateTextField().setText(toDateString);
            //Έλεγξε την ενεργοποίηση του searchButton
            searchButtonEnability();
        });
        //Κουμπί Search
        ((JButton) buttons[4]).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Αρχικοποίηση μεταβλητών
                Date fromDate = java.sql.Date.valueOf(toLocalDate(view.getFromDateTextField().getText()));
                Date toDate = java.sql.Date.valueOf(toLocalDate(view.getToDateTextField().getText()));                
                List<Coviddata> deathList = view.getDeathList();
                List<Coviddata> comfirmed = view.getConfirmedList();
                List<Coviddata> recovered = view.getRecoveredList();
                Country country = (Country) view.getCountryComboBox().getSelectedItem();
                //Καθάρισε τη deathList
                deathList.clear();
                //Βρες όλα τα δεδομένα ανάμεσα στις επιλεγμένες ημερομηνίες
                deathList.addAll(dBm.findByCoyntryDataBetweenDates(country, 1, fromDate, toDate));
                //Καθάρισε τη recovered
                recovered.clear();
                //Βρες όλα τα δεδομένα ανάμεσα στις επιλεγμένες ημερομηνίες
                recovered.addAll(dBm.findByCoyntryDataBetweenDates(country, 2, fromDate, toDate));
                //Καθάρισε τη comfirmed
                comfirmed.clear();
                //Βρες όλα τα δεδομένα ανάμεσα στις επιλεγμένες ημερομηνίες
                comfirmed.addAll(dBm.findByCoyntryDataBetweenDates(country, 3, fromDate, toDate));
            }
        });
        //Κουμπί προβολή σε διάγραμμα
        ((JButton) buttons[10]).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Coviddata> dataToDiagram = new ArrayList<>();
                List<Coviddata> deathList = view.getDeathList();
                List<Coviddata> comfirmed = view.getConfirmedList();
                List<Coviddata> recovered = view.getRecoveredList();
                //Τύπος δεδομένων για προβολή ημερήσια δεδομένα
                int qntType = 0;
                int linesNum = 0;
                //Αν είναι επιλεγμένο το CheckDeaths
                if(view.getCheckDeaths().isSelected()) {
                    //Βάλε την λίστα θανάτων στη λίστα που θα γίνει διάγραμμα
                    dataToDiagram.addAll(deathList);
                    //Αύξησε τον μετρητή γραμμών
                    linesNum++;
                }
                //Αν είναι επιλεγμένο το CheckRecovered
                if(view.getCheckRecovered().isSelected()) {
                    //Βάλε την λίστα με τους ασθενείς που έχουν ανακάμψει στη λίστα που θα γίνει διάγραμμα
                    dataToDiagram.addAll(recovered);
                    //Αύξησε τον μετρητή γραμμών
                    linesNum++;
                }
                //Αν είναι επιλεγμένο το getCheckConfirmed
                if(view.getCheckConfirmed().isSelected()) {
                    //Βάλε την λίστα με τα επιβεβαιωμένα κρούσματα στη λίστα που θα γίνει διάγραμμα
                    dataToDiagram.addAll(comfirmed);
                    //Αύξησε τον μετρητή γραμμών
                    linesNum++;
                }
                //Εάν είναι επιλεγμένο το radio για τα σωρευτικά δεδομένα
                if(view.getRadioProod().isSelected())
                    //κάνε τον τύπο 1
                    qntType = 1;
                //Αν δεν είναι άδεια η λίστα
                if(!dataToDiagram.isEmpty())
                    //Εμφάνισε το διάγραμμα
                    new DataChartModal(view, dataToDiagram, linesNum, qntType);
            }
        });
        //κουμπί προβολή σε χάρτη
        ((JButton) buttons[11]).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Νήμα που εκτελεί εργασίες εκτός από αλλαγές στα γραφικά
                new SwingWorker<Void, Void> (){
                    //Έλεγχος αν τελείωσαν οι εργασίες του Platform thread
                    boolean finished = false;
                    JDialog map = view.getMap();
                    @Override
                    //Η μέθοδος εκτελείται στο working thread
                    protected Void doInBackground() throws InterruptedException {
                        //Κατάσταση εγγραφής αρχείου
                        writerError = true;
                        List<Coviddata> selectedData = new ArrayList<>();
                        selectedData.addAll(view.getConfirmedList());
                        selectedData.addAll(view.getDeathList());
                        selectedData.addAll(view.getRecoveredList());
                        List<Country> countries = new ArrayList<>();
                        //Από την επιλεγμένη χώρα, πάρε τα δεδομένα από την βάση δεδομένων
                        countries.add((Country)view.getCountryComboBox().getSelectedItem());
                        countries.get(0).setCoviddataList(selectedData);
                        //Κάλεσε την writer να γράψει το Hmtl για την χώρα και ζουμ 5
                        HtmlWriter.writer(countries, 4, 500, 400);
                        //Δίνω χρόνο να κλείσει ο buffer για να ανοίξει το σωστό αρχείο
                        Thread.sleep(2000);
                        //Δημιουργία του λινκ
                        String link = null ;                            
                        switch (((Country)view.getCountryComboBox().getSelectedItem()).getName()) {
                            //Αν έχει επιλεγεί το MS_Zaandam(Κρουαζιερόπλοιο)
                            case "MS_Zaandam": 
                                //Λινκ που λέει το ιστορικό της κρουαζιέρας
                                link = "https://en.wikipedia.org/wiki/COVID-19_pandemic_on_cruise_ships#Zaandam";
                                break;
                                //Αν έχει επιλεγεί το Diamond_Princess(Κρουαζιερόπλοιο)
                            case "Diamond_Princess":
                                //Λινκ που λέει το ιστορικό της κρουαζιέρας
                                link = "https://en.wikipedia.org/wiki/COVID-19_pandemic_on_Diamond_Princess";
                                break;
                            default:
                                //Το λινκ που προβάλει τον χάρτη
                                link = new File("html\\mappage.html").toURI().toString();
                                break;
                        }                            
                        final String linkToLoad = link;
                        //Στο thread του Jfx application
                        Platform.runLater(() -> {
                            //Κάλεσε την loadLink για να φορτώσει το λινκ που στέλνω
                            AppSystem.getBrowser().loadLink(linkToLoad, new Dimension(500, 400));
                            finished = true;
                        });
                        //Αυτοσχέδιος μηχανισμός συγχρονισμού 2 thread
                        //Μπλοκάρω το working thread μέχρι να τελειώσει το platform thread
                        float i = 0;
                        //Όσο το platform thread δεν έχει τελειώσει
                        while(!finished) {
                            //Αν πέρασαν 5 δευτερόλεπτα
                            if(i>=5.0f) {
                                //Δώσε σφάλμα εγγραφής
                                writerError = true;
                                //Βγες από τη while
                                break;
                            } 
                            //Αύξησε κατά 1
                            i=i+0.5f;
                            //Περίμενε 1 δευτερόλεπτο
                            Thread.sleep(500);
                            System.out.println("είναι στη while");
                        }
                        
                        return null;
                    }

                    @Override
                    //Όταν τελειώσει το working thread
                    protected void done() {
                        //Αν η διαδικασία τελειίωσε κανονικά
                        if(!writerError) {
                            
                            //Κρύψε το loading label
                            view.getMapLoadingLabel().setVisible(false);
                            //Εμφάνισε τα κουμπιά
                            allButtonsEnability(); 
                            //Το JDialog που θα εμφανιστεί, να είναι μέσα στο view
                            map.setLocation(view.getLocationOnScreen());
                            //Φτιάξε το μέγεθος σύμφωνα με το περιεχόμενο
                            map.pack();
                            //Εμφάνισε το JDialog
                            map.setVisible(true);
                        }
                        else {
                            JOptionPane.showMessageDialog(view, "<html>Παρουσιάστηκε σφάλμα κατά την δημιουργία του χάρτη.<br>"
                                                                    + "Παρακαλώ πατήστε πάλι προβολή σε χάρτη!!!</html>", "Σφάλμα Χάρτη", JOptionPane.ERROR_MESSAGE);
                            //Κρύψε το loading label
                            view.getMapLoadingLabel().setVisible(false);
                            //Εμφάνισε τα κουμπιά
                            allButtonsEnability(); 
                        }
                    }
                }.execute();
                //Θέσε ορατό το loading label όταν ξεκινήσει το νήμα
                view.getMapLoadingLabel().setVisible(true);
                //Απενεργοποίησε όλα τα κουμπιά
                desableAllButtons();
            }
        });
        //κουμπί deleteData
        ((JButton) buttons[12]).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ερώτηση στον χρήστη για επιβεβαίωση
                int x = JOptionPane.showConfirmDialog(view,
                        "<html>Είστε σίγουροι ότι θέλετε να διαγράψετε<br>όλα τα δεδομένα για την επιλεγμένη χώρα;</html>",
                        "Προσοχή",
                        JOptionPane.YES_NO_OPTION);
                if(x==0) {//Εάν ναι                      
                    List<Coviddata> deathList = view.getDeathList();
                    List<Coviddata> confirmedList = view.getConfirmedList();
                    List<Coviddata> recoveredList = view.getRecoveredList();
                    List<Country> comboList = view.getCountryList();
                    //Πάρε τη χώρα που επέλεξε ο χρήστης
                    Country country = (Country) view.getCountryComboBox().getSelectedItem();
                    //Διέγραψε όλα τα δεδομένα Coviddata για την χώρα αυτή
                    dBm.deleteCoviddataByCountry (country);
                    //Καθάρισε τις observable λίστες
                    deathList.clear();
                    confirmedList.clear();
                    recoveredList.clear();
                    dateLimitData.clear();
                    //Αφαίρεσε την χώρα από την λίστα
                    comboList.remove(country);
                    //Θέσε πάλι το το combobox χωρίς την χώρα αυτή
                    view.getCountryComboBox().setModel(new DefaultComboBoxModel<Country>(comboList.toArray(new Country[0])));
                    view.getCountryComboBox().setSelectedIndex(-1);
                    allButtonsEnability();
                }
            }
        });
        //κουμπί backToOptions
        ((JButton) buttons[0]).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Εσωτερική ανώνυμη κλάση ActionListener με lamda
				//Καλεί τον mainFrame να αλλάξει View
                appSystem.getMainFrame().showOptionsView();
				//Δημιουργεί τον Presenter για το νέο View
                Presenter p = new OvPresenter( appSystem.getMainFrame().getPanel(), appSystem);
				//Στέλνει στο σύστημα τον νέο Presenter κια τον Παλίο για ενέργειες
                appSystem.setPresenter(p, appSystem.getPresenter());
            }
        });
    }
    
    private void searchButtonEnability() {
        //Αρχικοποίηση μεταβλητών
        JTextField fromField = view.getFromDateTextField();
        JTextField toField = view.getToDateTextField();        
        //Εάν κάποιο από τα 2 field είναι κενό
        if(fromField.equals("") || toField.equals(""))
            //Απενεργοποίησε το κουμπί
            buttons[4].setEnabled(false);
        //Εάν δεν υπάρχει επιλεγμένη χώρα
        else if(view.getCountryComboBox().getSelectedIndex() == -1)
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
			//Ενεργοποίησε το κουμπί
            buttons[4].setEnabled(true);
			//Απενεργοποίησε το label
            view.getDateErrorLabel().setVisible(false);
        }
    }
    //Ρυθμίζει το enability των checkButtons και του diagramButton, σύμφωνα με τις λίστες
    private void checkButtonsNdiagramEnability() {
        //Για θανάτους
        buttons[5].setEnabled(!view.getDeathList().isEmpty());
        ((JCheckBox)buttons[5]).setSelected(buttons[5].isEnabled());
        //Για ασθενείς που έχουν ανακάμψει
        buttons[6].setEnabled(!view.getRecoveredList().isEmpty());
        ((JCheckBox)buttons[6]).setSelected(buttons[6].isEnabled());
        //Για επιβεβαιωμένα κρούσματα
        buttons[7].setEnabled(!view.getConfirmedList().isEmpty());
        ((JCheckBox)buttons[7]).setSelected(buttons[7].isEnabled());
        Boolean enable = false;
        //Αν έστω 1 από τα checkButton είναι enabled κάνε και το diagram
        for(int i=5; i<=7; i++)
            if(buttons[i].isEnabled()) {
                enable = true;
                break;
            }
        buttons[10].setEnabled(enable);                
    }
    
    private void desableAllButtons() {
        for(JComponent j: buttons) {
            j.setEnabled(false);
        }
    }
    //Ελέγχει το enability όλων των κουμπιών
    private void allButtonsEnability() {
        buttons[0].setEnabled(true);
        buttons[1].setEnabled(true);
        boolean enable = view.getCountryComboBox().getSelectedIndex() != -1;  
        for(int i=2; i<buttons.length; i++)
        buttons[i].setEnabled(enable);        
        searchButtonEnability();
        checkButtonsNdiagramEnability();
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
        Object oldVal = evt.getOldValue();
        
        //Στέλνει το αποτέλεσμα του γραψίματος του αρχείου
        if("Html writer, oldVal->true, newVal->hasIOError".equalsIgnoreCase(propName)){            
                writerError = (boolean)newVal;
        }
    }
    
}
