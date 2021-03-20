package coviddataapp;

import boundaryclasses.DbManager;
import java.awt.Frame;
import javafx.application.Platform;
import javax.swing.JOptionPane;
import javax.swing.event.SwingPropertyChangeSupport;
import presenters.OvPresenter;
import presenters.Presenter;
import views.HtmlBrowser;
import views.MainFrame;

public class AppSystem {
    
    private final MainFrame mainFrame;
    private static HtmlBrowser browser;
    private static SwingPropertyChangeSupport propChangeFirer;
    private Presenter presenter;
    private DbManager dbManager;
    
    public AppSystem() {
        //Αρχικοποίηση του mainFrame
        mainFrame = new MainFrame("Covid Stats Application");
        //Αρχικοποίηση του jfx browser
        //Αν κλείσει ο browser να μην τερματιστεί το thread του jfx app
        Platform.setImplicitExit(false);
        browser = new HtmlBrowser();
        //Θέτει το σύστημα σαν propertyChangeFirer
        propChangeFirer = new SwingPropertyChangeSupport(this);
        //Με την κατασκευή του συστήματος θέτει στον προεπιλεγμένο Presenter        
        presenter = new OvPresenter( mainFrame.getPanel(), this);
        // Αρχικοποίηση του συστήματος
        initAppSystem();
    }
    
    private void initAppSystem() {
        //Ορίζω Listener για PropertyChange events
        setPropertyChangeListener(presenter);
        //αρχικοποιώ το DbManager
        dbManager = new DbManager();
        
    }
    //Θέτει τον Presenter που θα ακούει τον propertyChangeFirer
    public void setPropertyChangeListener(Presenter presenter) {       
        propChangeFirer.addPropertyChangeListener(presenter);        
    }
    //static μέθοδος για να μπορώ να πυροδοτώ συμβάντα
    public static SwingPropertyChangeSupport getPropChangeFirer() {
        return propChangeFirer;
    }
    
    public MainFrame getMainFrame() {
        return mainFrame;
    }
    
    public static HtmlBrowser getBrowser() {
        return browser;
    }
    //Θέτει τον Presenter του τρέχοντος View
    public void setPresenter(Presenter presenter, Presenter toRemove) {
        if (toRemove != null)
            //Αφαιρεί τον προηγούμενο Presenter από τη λίστα με τους PropertyChangeListeners
            propChangeFirer.removePropertyChangeListener(toRemove);
        this.presenter = presenter;
        setPropertyChangeListener(presenter);
         
    }
    
    public Presenter getPresenter() {
        return presenter;
    }
    
    public DbManager getDbManager() {
        return dbManager;
    }
    //Τερματίζει την εφαρμογή, οι εργασίες της main, έχουν τελειώσει,
    //άρα αν κάνουμε dispose το παράθυρο θα κλείσει η εφαρμογή
    public static void terminate(String message){
        //Πάρε όλα τα frame
        Frame[] frames = Frame.getFrames();        
        //Εάν ο χρήστης έχει κλείσει την εφαρμογή με το χ
        //και το σύστημα προσπαθεί να κάνει αρχικοποίηση του entity manager,
        //χωρίς συνδεμένη την βάση δεδομένων, να μην εμφανιστεί το μήνυμα
        if(frames[0].isVisible()) {
            JOptionPane.showMessageDialog(frames[0], message, "Απροσδόκητο σφάλμα", JOptionPane.ERROR_MESSAGE);
            //Διαπέρασε τα
            for(Frame f: frames)
                //κλείσε τα
                f.dispose();        
            //Κλείσε το jfx app
            Platform.exit();
        }
    }
}
