package presenters;

import boundaryclasses.DbManager;
import coviddataapp.AppSystem;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import javafx.application.Platform;
import javax.swing.JButton;
import javax.swing.JPanel;
import views.OptionsView;

/**
 *
 * 
 */
public  class OvPresenter implements Presenter{//OptionsView Presenter
    
    private  final OptionsView view;
    private final AppSystem appSystem;
    private JButton[] buttons;

    public OvPresenter(final JPanel view, final AppSystem appSystem) {
		//Κάνει cast το JPanel στην κλάση του View
        this.view = (OptionsView) view;
		//Θέτει το σύστημα για να μπορεί να κάνει ενέργειες όταν πατάει ο χρήστης κουμπιά 
        this.appSystem = appSystem;  
        setUpViewEvents();
        
    }

    @Override
    public final void setUpViewEvents() {    
        buttons = new JButton[]{
            view.getDataButton(),   //0 = showDataView
            view.getCountryButton(),//1 = showCountryDataView
            view.getMapButton(),    //2 = showDataMapView
            view.getExitButton()   // 3 = System.exit(0)
        };
        if(DbManager.getEm() ==null)   
            desableAllButtons();
        else {
            view.getInfoLabel().setVisible(false);
            view.getIconLabel().setVisible(false);
        }
        //Κουμπί DataManage
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				//Καλεί τον mainFrame να αλλάξει View
                appSystem.getMainFrame().showDataManageView();
				//Δημιουργεί τον Presenter για το νέο View
                Presenter p = new DataVPresenter( appSystem.getMainFrame().getPanel(), appSystem);
				//Στέλνει στο σύστημα τον νέο Presenter και τον παλιό για ενέργειες
                appSystem.setPresenter(p, appSystem.getPresenter());
                
            }
        });
        //Ίδιες λειτουργίες για το CountryButton
        buttons[1].addActionListener((ActionEvent e) -> { 
            appSystem.getMainFrame().showCountryDataView();
            Presenter p = new CdvPresenter( appSystem.getMainFrame().getPanel(), appSystem);
            appSystem.setPresenter(p, appSystem.getPresenter());
        });
        //Ίδιες λειτουργίες για το MapButton
        buttons[2].addActionListener((ActionEvent e) -> {
            appSystem.getMainFrame().showDataMapView();
            Presenter p = new DmvPresenter( appSystem.getMainFrame().getPanel(), appSystem);
            appSystem.setPresenter(p, appSystem.getPresenter());
                         
        });
        //Κλείσιμο της εφαρμογής
        buttons[3].addActionListener((ActionEvent e) -> {
            //Πάρε όλα τα frame
            Frame[] frames = Frame.getFrames();
            //Διαπέρασε τα
            for(Frame f: frames)
                //κλείσε τα
                f.dispose();
            //Κλείσε το jfx app thread
            Platform.exit();
        });
        
    }

    @Override
	//Διαχειρίζεται το GUI σε σχέση με τις αλλαγές στο σύστημα
    public void propertyChange(PropertyChangeEvent evt) {
        //Το όνομα που ορίσαμε στο PropertyChangeEvent
        String propName = evt.getPropertyName();
		//Η νέα τιμή που περάσαμε
        Object newVal = evt.getNewValue();
		//Η παλιά τιμή που περάσαμε
        Object oldVal = evt.getOldValue();
        
        if("Db login".equalsIgnoreCase(propName)){            
            view.getInfoLabel().setText(String.valueOf(newVal));
            view.getIconLabel().setVisible(false);
            view.repaint();
            if(!String.valueOf(newVal).equalsIgnoreCase(""))
                enableAllButtons();
        }
    }
    
    private void desableAllButtons() {
        for(JButton button: buttons)
            button.setEnabled(false);
    }

    private void enableAllButtons() {
        for(JButton button: buttons)
            button.setEnabled(true);
    }
}
