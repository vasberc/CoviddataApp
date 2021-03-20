package views;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.application.Platform;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
    
    private final Container container = this.getContentPane();
    private JPanel panel;
    private final FlowLayout layout = new FlowLayout();
    
    public MainFrame(String title) {
        super(title);        
        initMainFrame();
    }
    //Αρχικοποίηση του Frame
    private void initMainFrame() {
        container.setLayout(layout);
        //Αλλαγή εικονιδίου στο παράθυρο
        setIconImage(new ImageIcon(getClass().getResource("/img/covidIcon.png")).getImage());
        //Listener για τερματισμό της εφαρμογής
        addWindowListener(new WindowAdapter (){
            @Override
            //Οι εργασίες της main, έχουν τελειώσει,
            //άρα αν κάνουμε dispose το παράθυρο θα κλείσει η εφαρμογή
            public void windowClosing(WindowEvent event){
                //Πάρε όλα τα frame
                Frame[] frames = Frame.getFrames();
                //Διαπέρασέ τα
                for(Frame f: frames)
                    //κλείσε τα
                    f.dispose();
                //Κλείσε το jfx app thread
                Platform.exit();
            }
        });        
        setResizable(false);
        //Προεπιλεγμένο View κατά την εκκίνηση   
        showOptionsView();         
        setVisible(true);         
    }
    //Δείχνει το View με τα Options
    public void showOptionsView() {
        container.removeAll();//Αφαιρεί από το ContentPane πιθανό περιεχόμενο
        container.repaint();//Ζωγραφίζει ξανά το ContentPane        
        panel = new OptionsView();//Δημιουργεί το View
        container.add(panel);//Το τοποθετεί στο ContentPane
        container.repaint();//Ζωγραφίζει ξανά το ContentPane
        container.validate();//Κάνει valid τα συστατικά του ContentPane
        pack();//Θέτει το μέγεθος του παραθύρου με βάση τα περιεχόμενά του
    }
    //Ίδιες ενέργειες για το DataManageView
    public void showDataManageView() {
        container.removeAll();
        panel = new DataManageView();
        container.add(panel);        
        container.repaint();
        container.validate();        
        pack();
    }
    //Ίδιες ενέργειες για το DataMapView
    public void showDataMapView() {
        container.removeAll();       
        panel = new DataMapView();
        container.add(panel);        
        container.repaint();
        container.validate(); 
        pack();
    }
    //Ίδιες ενέργειες για το CountryDataView
    public void showCountryDataView() {
        container.removeAll();    
        panel = new CountryDataView();
        container.add(panel);
        container.repaint();
        container.validate();    
        pack();
    }
    //Επιστρέφει το View για διαχείριση
    public JPanel getPanel() {
        return panel;
    }
}
