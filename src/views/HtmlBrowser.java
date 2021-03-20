
package views;

import java.awt.Dimension;
import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * //https://stackoverflow.com/a/26028556/15090628 source
 */
public class HtmlBrowser extends JFXPanel {
    private WebView webView;
    private WebEngine webEngine;
    private String link;
    private Dimension dimension;
    
    public HtmlBrowser() {
        //Τρέχει σε Jfx thread
        Platform.runLater(() -> {
            initialiseJavaFXScene();
        });
    }

    private void initialiseJavaFXScene() {
        //Ορισμός μεγέθους
        dimension = new Dimension(500, 400);
        setPreferredSize(dimension);
        //Το λινκ που θα δείχνει με την αρχικοποίηση
        link = new File("html\\mappage.html").toURI().toString();
        //Νέο webView για την προβολή ιστοσελίδων
        webView = new WebView();
        webEngine = webView.getEngine();
        //Φορτώνει το λίνκ
        webEngine.load(link);
        //Δημιουργεί ένα scene για να το τοποθετήσει στο βασικό scene του Jfx panel
        Scene scene = new Scene(webView);           
        setScene(scene);       
    }
    //Φορτώνει το λινκ που του στέλνουμε
    public void loadLink(String link, Dimension dimension) {
        //this.link = link;
        if(!this.dimension.equals(dimension)) {
            this.dimension = dimension;
            this.setSize(dimension);  
        }
        webEngine.load(link);
    }       
}