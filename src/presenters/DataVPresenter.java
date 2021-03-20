package presenters;

import boundaryclasses.DbManager;
import boundaryclasses.ServerConnection;
import coviddataapp.AppSystem;
import coviddataapp.CovidDataType;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import model.Country;
import model.Coviddata;
import views.DataManageView;

/**
 *
 * 
 */
public class DataVPresenter implements Presenter {//DataManageView Presenter
    
    private  final DataManageView view;
    private final AppSystem appSystem;    
    private TreeMap<String, List<Coviddata>> tempData = new TreeMap<>();
    private JButton[] buttons; 
    private final DbManager dBm;
    private boolean insertingData = false;

    public DataVPresenter(JPanel view, AppSystem appSystem) {
        this.dBm = appSystem.getDbManager();
		//Κάνει cast το JPanel στην κλάση του View
        this.view = (DataManageView) view;
		//Θέτει το σύστημα για να μπορεί να κάνει ενέργειες όταν πατάει ο χρήστης κουμπιά 
        this.appSystem = appSystem;
        setUpViewEvents();
    }

    @Override
    public final void setUpViewEvents() {//Αρχικοποίηση Presenter και των Event
        //Με την αρχικοποίηση του View βάζω στην λίστα του πίνακα Coviddata
        //μια συνοπτική παρουσίαση, για κάθε χώρα, τι είδος δεδομένα έχουμε αποθηκεύσει
        //στην βάση δεδομένων και μέχρι ποια ημερομηνία
        List<Coviddata> coviddataList = view.getCoviddataList();
        //Καλεί την μέθοδο που βρίσκει την τελευταία ημερομηνία για κάθε χώρα και είδος
        coviddataList.addAll(dBm.findLastData());
        //Εάν η λίστα δεν είναι άδεια
        if(!coviddataList.isEmpty()) 
            //Ταξινόμησε την
            coviddataList.sort((Coviddata c1, Coviddata c2) -> {
                //Πρώτα ανά χώρα και μετά ανά dataKind
                if(c1.getCountry().equals(c2.getCountry())) 
                    return c1.getDatakind()-c2.getDatakind(); 
                else 
                    return c1.getCountry().compareTo(c2.getCountry());
        });
        
        buttons = new JButton[]{
            view.getBackToOptionsButton(),//0 = BackToOptionsButton
            view.getInsertCountryButton(),//1 = InsertCountryButton
            view.getInsertDataButton(),//   2 = InsertDataButton
            view.getDeleteCountryButton(),//3 = getDeleteCountryButton
            view.getDeleteDataButton(),//   4 = getDeleteDataButton
            view.getDeleteAll()//           5 = getDeleteAll()
        };
        //Με την εκκίνηση του GUI φτιάχνω το visibility των deleteButtons
        deleteButtonsVisibility();
       
        //Κουμπί deleteAll
        //Ανώνυμος ActionListener
        buttons[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {  
                JLabel label = view.getInfoText();
                //Ρωτάμε τον χρήστη αν θέλει να προχωρήσει
                int x = JOptionPane.showConfirmDialog (view,
                        "Είστε σίγουροι ότι θέλετε να διαγράψετε όλα τα δεδομένα;",
                        "Προσοχή",
                        JOptionPane.YES_NO_OPTION);
                if(x==0) {                        
                    desableAllButtons();//Λόγω νήματος απενεργοποιούμε όλα τα κουμπιά
                    //Χρησιμοποιεί ένα νήμα διαθέσιμο από τα 10 που έχει το SwingWorker Threadpool
                    new SwingWorker<Void, Void> (){//ορίζω το νήμα                        
                        List<Country> obsCountryList =  view.getCountryList();
                        List<Coviddata> obsCovidatalist = view.getCoviddataList();
                        List<Country> serverList = view.getServerList();
                        @Override
                        //λειτουργίες που εκτελούνται στο SwingWorker thread
                        protected Void doInBackground() {
                            //Διαγραφή δεδομένων
                            dBm.deleteAll();
                            return null;
                        }
                        @Override
                        //Λειτουργίες που εκτελούνται στο event dispatch thread μόλις ολοκληρωθεί το SwingWorker thread
                        public void done() {
                            //Ενημέρωση των obsevable list του GUI 
                            //Ενημέρωση του πίνακα με τις χώρες     
                            obsCountryList.clear();
                            obsCovidatalist.clear();
                            serverList.clear();                            
                            label.setText("η διαγραφή δεδομένων, ολοκληρώθηκε με επιτυχία");//εμφανίζει μήνυμα στο view
                            //επαναφέρω τα κουμπιά                            
                            enableAllButtons();
                            deleteButtonsVisibility();
                            //Μήνυμα προς τον χρήστη
                            view.getDataLabel().setText("<html><span style=\"color:red\">Δεν υπάρχουν δεδομένα για εισαγωγή, παρακαλώ πατήστε λήψη δεδομένων</span><html>");
                            //Απόκρυψη του loading label
                            view.getLoadingLabel().setVisible(false);
                        }
                    }.execute();
                    //Εκτελείται παράλληλα με το SwingWorker thread
                    label.setText("Διαγραφή δεδομένων, παρακαλώ περιμένετε...");//εμφανίζει μήνυμα στο view
                    //εμφάνιση του loading label
                    view.getLoadingLabel().setVisible(true);
                }
            }          
        });
        
        //κουμπί deleteCountry         
        buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel label = view.getInfoText();
                int[] selected = view.getCoutryJList().getSelectedIndices();//παίρνω τους δίκτες που έχουν επιλεγεί
                //Εκτελώ ενέργειες μόνο αν υπάρχει επιλογή
                if( selected.length!=0 ) {
                    //Ρωτάω τον χρήστη αν επιθυμεί να προχωρήσει
                    int x = JOptionPane.showConfirmDialog (view,
                            "<html>Είστε σίγουροι ότι θέλετε να διαγράψετε τις επιλεγμένες χώρες;<br>"
                          + "<span style=\"color:red\">Σημείωση: Κατά την διαγραφή μιας χώρας, θα διαγραφούν και Covid data αν υπάρχουν.</span></html>",
                            "Προσοχή",
                            JOptionPane.YES_NO_OPTION);
                    if(x==0) {
                        desableAllButtons();//Λόγω νήματος απενεργοποιούμε όλα τα κουμπιά                        
                        new SwingWorker<Void, ArrayList<Object>> () {//ορίζω το νήμα  
                            List<Country> obsCountryList =  view.getCountryList();
                            List<Coviddata> obsCoviddataList = view.getCoviddataList();
                            List<Country>  countriesToRemove = new ArrayList<>();
                            List<Coviddata>  coviddataToRemove = new ArrayList<>();
                            @Override
                            protected Void doInBackground() throws InterruptedException {//λειτουργίες στο background ώστε τα γραφικά να μην κολλάνε.
                                //Τοποθετώ σε άλλη λίστα για μην έχω πρόβλημα με τους δείκτες κατά την αφαίρεση αντικειμένων από τα observable lists
                                countriesToRemove.addAll(obsCountryList);
                                coviddataToRemove.addAll(obsCoviddataList);
                                ArrayList<Object> toPublish = new ArrayList<>();                                
                                for(int i: selected){
                                    //Αφαιρώ πρώτα τα δεδομένα coviddata που ενδεχομένως υπάρχουν, λόγω foreign key
                                    dBm.deleteCoviddataByCountry(countriesToRemove.get(i));
                                    //Βρίσκω το data που υπάρχει στο JTable
                                    for(Coviddata d: coviddataToRemove)
                                        if(d.getCountry().equals(countriesToRemove.get(i))) {
                                            //Το προσθέτω στη λίστα που θα γίνει publish
                                            toPublish.add(d);
                                        }
                                    //Αφαιρώ την χώρα από το database
                                    dBm.deleteCountry(countriesToRemove.get(i));
                                    //Την προσθέτω στην λίστα που θα γίνει publish
                                    toPublish.add(countriesToRemove.get(i));
                                    publish(toPublish);
                                    //δίνω λίγο χρόνο για να προλάβει να εκτελεστεί το publish πριν την επόμενη κλήση
                                    Thread.sleep(10);
                                } 
                                return null;
                            } 
                            @Override
                            //Εκτελείται με την κλήση της publsh στο event dispatch thread
                            //Εδώ σε συγχρονισμό με το worker Thread επηρεάζω αντικείμενα του GUI
                            protected void process(List<ArrayList<Object>> chunks) {
                                ArrayList<Object> mostRecentValue = new ArrayList<>();
                                //Σε κάθε κλήση παίρνω το τελευταίο στοιχείο που στάλθηκε
                                mostRecentValue.addAll(chunks.get(chunks.size()-1));
                                //Αφαίρεση από τις λίστες και κατ επέκταση από τα JTables
                                for(Object o: mostRecentValue) {
                                if(o instanceof Coviddata)
                                    obsCoviddataList.remove(o);
                                    else obsCountryList.remove(o);
                                }
                            } 
                            
                            @Override
                            public void done() {//ενέργειες όταν τελειώσει το νήμα
                                label.setText("Η διαγραφή δεδομένων ολοκληρώθηκε με επιτυχία");//εμφανίζει μήνυμα στο view 
                                //Μετά τις ενέργειες του νήματος επαναφέρω τα κουμπιά
                                enableAllButtons();
                                deleteButtonsVisibility();
                                view.getLoadingLabel().setVisible(false);
                            }
                        }.execute();
                        label.setText("Διαγραφή δεδομένων, παρακαλώ περιμένετε...");//εμφανίζει μήνυμα στο view
                        view.getLoadingLabel().setVisible(true);//θα εκτελεστεί μαζί με το νήμα                        
                    }
                }
            }
        });
        
        //Κουμπί deleteData        
        buttons[4].addActionListener(new ActionListener() {//Εσωτερική ανώνυμη κλάση ActionListener
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel label = view.getInfoText();
				//παίρνω τον δείκτη που έχει επιλεγεί
                int[] index = view.getDataTable().getSelectedRows();
                if(index.length>0){
                    int x = JOptionPane.showConfirmDialog (view, "Είστε σίγουροι ότι θέλετε να διαγράψετε τα επιλεγμένα δεδομένα;","Προσοχή",JOptionPane.YES_NO_OPTION);
                    if(x == 0) {
                        desableAllButtons();//Λόγω νήματος απενεργοποιούμε όλα τα κουμπιά                   
                        new SwingWorker<Void, Coviddata> (){//ορίζω το νήμα
                            List<Coviddata> obsCoviddatalist = view.getCoviddataList();//Παίρνω τη λίστα του GUI
                            ArrayList<Coviddata> toRemove = new ArrayList<>();
                            @Override
							//λειτουργίες στο background ώστε τα γραφικά να μην κολλάνε
                            protected Void doInBackground() throws InterruptedException {                                   
                                //Δημιουργώ πρώτα μια λίστα toRemove, για να μην έχω πρόβλημα με τους δείκτες κατά την αφαίρεση αντικειμένων
                                for(int i=0; i<index.length; i++)
                                    toRemove.add(obsCoviddatalist.get(index[i]));
                                for(Coviddata c : toRemove) {
                                    //Εκτελώ query που διαγράφει τα data με βάση την επιλεγμένη εγγραφή
                                    dBm.deleteCoviddataByCountryNtype(c);
                                    publish(c);
                                    Thread.sleep(10);
                                }              
                                return null;
                            }
                            @Override
                            protected void process(List<Coviddata> chunks) {
                                Coviddata mostRecentValue = chunks.get(chunks.size()-1);
                                obsCoviddatalist.remove(mostRecentValue);
                            }
                            @Override
                            public void done() {//ενέργειες όταν τελειώσει το νήμα
                                label.setText("Η διαγραφή δεδομένων ολοκληρώθηκε με επιτυχία");//εμφανίζει μήνυμα στο view 
                                //Μετά τις ενέργειες του νήματος επαναφέρω τα κουμπιά
                                enableAllButtons();
                                deleteButtonsVisibility();
                                view.getLoadingLabel().setVisible(false);
                            }
                        }.execute();
                        label.setText("Διαγραφή δεδομένων, παρακαλώ περιμένετε...");//εμφανίζει μήνυμα στο view
                        view.getLoadingLabel().setVisible(true);//θα εκτελεστεί μαζί με το νήμα
                    }
                }                   
            }          
        }); 
        
        //κουμπί εισαγωγή χωρών
        buttons[1].addActionListener(new ActionListener() {//Εσωτερική ανώνυμη κλάση ActionListener
            @Override
            public void actionPerformed(ActionEvent e) { 
                JLabel label = view.getInfoText();
                label.setText("Επικοινωνία με τον server, παρακαλώ περιμένετε...");//εμφανίζει μήνυμα στο view 
                List<Country> serverList = view.getServerList();                
                CovidDataType datatype;//Αντικείμενο enum CovidDataType
                datatype = (CovidDataType) view.getTypeComboBoc().getSelectedItem();//παίρνει το επιλεγμένο type                
                desableAllButtons();
                new SwingWorker<Void, Void> (){                    
                    List<Country> countries;
                    List<Country> obsCountryList =  view.getCountryList();                    
                    @Override
                    protected Void doInBackground() { 
                        //Καλώ την stastic μέθοδο και ανακτώ όλα τα δεδομένα που υπάρχουν στον server
                        countries =  ServerConnection.reciveData(datatype);//κατεβάζει τη λίστα με όλες τις χώρες και τα δεδομένα
                        //Στέλνω τα δεδομένα για εγγραφή των χωρών στην βάση δεδομένων
                        if(countries != null)
                            setTempData(dBm.insertCountries(countries));//καλεί το DbManager να εισάγει τα δεδομένα στη βάση 
                        return null;                            
                    }
                    @Override
                    public void done() {
                        if(countries !=null){//Αν έχει γίνει κανονικά η σύνδεση με τον server
                            //Στην περίπτωση που ο server δεν έχει χώρες για το type που έγινε αίτημα τις αφαιρώ από την λίστα
                            serverList.retainAll(countries);
                            for(Country country: countries) {//ενημερώνω την λίστα για να ενημερώσει τον πίνακα
                                if(!serverList.contains(country))
                                    serverList.add(country);
                                if(!obsCountryList.contains(country))
                                    obsCountryList.add(country);
                            }                            
                            Collections.sort(obsCountryList);
                        }
                        //Μετά τις ενέργειες του νήματος επαναφέρω τα κουμπιά
                        enableAllButtons();                        
                        deleteButtonsVisibility();
                        view.getLoadingLabel().setVisible(false);
                    }
                }.execute();
                //Εκτελούνται παράλληλα με το νήμα του Swigworker
                serverList.clear();//Αδειάζει τον πίνακα του GUI
                label = view.getDataLabel();
                label.setText("<html>Πατώντας εισαγωγή θα εισάγετε δεδομένα είδος: "+ datatype.getLabelName()+
                              "<br><span style=\"color:red\">Για να εισάγετε άλλο είδος επιλέξτε από τη λίστα "
                                      + "το είδος που επιθυμείτε και πατήστε λήψη δεδομένων ξανά</span></html>");
                label.setForeground(Color.BLUE);
                view.getLoadingLabel().setVisible(true);
            }          
        });
        
        //κουμπί εισαγωγή δεδομένων       
        //αν δεν έχουμε κάνει λήψη είναι απενεργοποιημένο
        buttons[2].setEnabled(!tempData.isEmpty());
        buttons[2].addActionListener(new ActionListener() {//Εσωτερική ανώνυμη κλάση ActionListener
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Country> serverList = view.getServerList();
                String value = serverList.get(view.getjServerList().getSelectedIndex()).getName();
                if(value!=null) {
                    desableAllButtons();
                    insertingData = true;
                    JLabel label = view.getInfoText();
                    label.setText("Εισαγωγή δεδομένων στη βάση");//εμφάνιση μηνύματος
                    new SwingWorker<Void, Coviddata> (){                    
                        List<Coviddata> obsCoviddataList = view.getCoviddataList();
                        //Θέτει στο Selected τη λίστα με τα δεδομένα της επιλεγμένης χώρας
                        List<Coviddata> selected = tempData.get(value);
                        @Override
                        protected Void doInBackground() { 
                            if(value != null) {
                                //καλώ τον DbManager να εισάγει τα δεδομένα
                                dBm.insertData(selected);
                            } 
                            //Θέλω ο πίνακας για είναι εύχρηστος να δείχνει μόνο 1 εγγραφή 
                            //δείχνοντας στον χρήστη μόνο όνομα χώρας και τύπο δεδομένων
                            //Οπότε στο observable list θα εισάγω μόνο 1 εγγραφή
                            //Έχω ορίσει κατάλληλα την equals στην κλάση Coviddata
                            if(!obsCoviddataList.contains(selected.get(selected.size()-1)))
                                publish(selected.get(selected.size()-1)); 
                            return null;
                        }
                        @Override
                        protected void process(List<Coviddata> chunks) {
                            //Κάνω add κάθε φορά το τελευταίο αντικείμενο
                            Coviddata mostRecentValue = chunks.get(chunks.size()-1);
                            //Ελέγχει αν υπάρχει για την χώρα και το είδος αναφορά στη λίστα
                            for(Coviddata c: obsCoviddataList)
                                if(c.getCountry().equals(mostRecentValue.getCountry()) && c.getDatakind() == mostRecentValue.getDatakind()) {
                                    //Την αφαιρεί για να δείχνει η λίστα την νέα ημερομηνία έως
                                    obsCoviddataList.remove(c);
                                    break;
                                }
                            
                            obsCoviddataList.add(mostRecentValue);
                            //Ταξινομώ την λίστα πρώτα με βάση το όνομα χώρας και μετά με βάση τον τύπο δεδομένων
                            obsCoviddataList.sort((Coviddata c1, Coviddata c2) -> {
                                if(c1.getCountry().equals(c2.getCountry()))
                                    return c1.getDatakind()-c2.getDatakind();
                                else 
                                    return c1.getCountry().compareTo(c2.getCountry());
                            });
                        }
                        @Override
                        public void done() {
                            //Μετά τις ενέργειες του νήματος επαναφέρω τα κουμπιά
                            insertingData = false;
                            enableAllButtons();
                            deleteButtonsVisibility();
                            view.getLoadingLabel().setVisible(false);
                        }
                    }.execute();
                    view.getLoadingLabel().setVisible(true);//θα εκτελεστεί μαζί με το νήμα
                }
            }         
        });
        //κουμπί backToOptions
        buttons[0].addActionListener(new ActionListener() {//Εσωτερική ανώνυμη κλάση ActionListener
            @Override
            public void actionPerformed(ActionEvent e) {
				//Καλεί τον mainFrame να αλλάξει View
                appSystem.getMainFrame().showOptionsView();
				//Δημιουργεί τον Presenter για το νέο View    
                Presenter p = new OvPresenter( appSystem.getMainFrame().getPanel(), appSystem); 
				//Στέλνει στο σύστημα τον νέο Presenter και τον παλιό για ενέργειες				
                appSystem.setPresenter(p, appSystem.getPresenter());
            }          
        });
        //Αν διαγραφεί μια χώρα δεν μπορούμε να κάνουμε εισαγωγή δεδομένων
        view.getjServerList().addListSelectionListener((ListSelectionEvent e) -> {
            //Κάθε φορά που ο χρήστης επιλέγει μια χώρα ελέγχω αν θα λειτουργεί το insertButton
            insertDataButtonState();
        });
    }    
    private void insertDataButtonState() {
        List<Country> obsCountryList = view.getCountryList();
        List<Country> serverList = view.getServerList();
        Country selected = null;
        //Αν υπάρχει επιλογή από τον χρήστη την βάζω στο selected
        if(view.getjServerList().getSelectedIndex()!=-1)
            selected = serverList.get(view.getjServerList().getSelectedIndex());
        //Εάν δεν έχω κατεβάσει δεδομένα το insert δεν λειτουργεί
        if(serverList.isEmpty())
            buttons[2].setEnabled(false);
        //Εάν η χώρα δεν υπάρχει στην βάση δεδομένων το insert δεν λειτουργεί
        else if(!obsCountryList.contains(selected))
            buttons[2].setEnabled(false);
        //Εάν πραγματοποιείται εισαγωγή δεδομένων το insert δεν λειτουργεί
        else if(insertingData)
            buttons[2].setEnabled(false);
        //Σε όλες τις άλλες περιπτώσεις λειτουργεί
        else buttons[2].setEnabled(true);
    }
    //εμφανίζω τα deleteButtons με βάση την λίστα του GUI
    private void deleteButtonsVisibility() {
        buttons[3].setVisible(!view.getCountryList().isEmpty());
        buttons[4].setVisible(!view.getCoviddataList().isEmpty());
        //Το delete all data μπορεί να εμφανίζεται και χωρίς να έχω coviddata στο database
        buttons[5].setVisible(!view.getCountryList().isEmpty());
    }
    //Κάνει όλα τα κουμπιά μη διαθέσιμα
    private void desableAllButtons() {
        for(JButton button: buttons)
            button.setEnabled(false);
    }
    //Κάνει όλα τα κουμπιά διαθέσιμα
    private void enableAllButtons() {
        for(JButton button: buttons)
            button.setEnabled(true);
        //Το insertData ελέγχεται κατά περίπτωση
        insertDataButtonState();
    }
    //Διαχειρίζεται το GUI σε σχέση με τις αλλαγές στο σύστημα 
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        Object newVal = evt.getNewValue();
        Object oldVal = evt.getOldValue();
        
            //Μήνυμα για το αποτέλεσμα του αιτήματος από τον server και την αποθήκευση στο Database
            if("server conection".equalsIgnoreCase(propName) || "Countries insert".equalsIgnoreCase(propName) || "Covid data insert".equalsIgnoreCase(propName)){
                JLabel label = view.getInfoText();
                label.setText(String.valueOf(newVal));
            }
    }   
    
    public void setTempData(TreeMap<String, List<Coviddata>> tempData) {
        this.tempData = tempData;
    }
}
