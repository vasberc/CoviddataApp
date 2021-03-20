/*
 * https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html#programmatic Look and feel
 */
package coviddataapp;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * 
 * 
 */
public class CovidDataApp {
    
    public static void main(String[] args) {
        try {//Θέτει lookAndFell στην εφαρμογή
            LookAndFeelInfo[] lookAndFeells = UIManager.getInstalledLookAndFeels();
            UIManager.setLookAndFeel(lookAndFeells[1].getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(CovidDataApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Αλλαγή text των κουμπιών στα optionpane
        UIManager.put("OptionPane.yesButtonText", "Ναι");
        UIManager.put("OptionPane.noButtonText", "Όχι");                
        new AppSystem();//Εκκινεί το σύστημα
    }
}
