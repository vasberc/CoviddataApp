package boundaryclasses;

import coviddataapp.AppSystem;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeSet;
import model.Country;
import model.Coviddata;

/**
 *
 * 
 */
public class HtmlWriter {
    //Static μέθοδος για την εγγραφή του html αρχείου
    public static void writer(List<Country> countries, int zoom, int width, int height) {
        String[] locations = new String[countries.size()];
        int deaths = 0,
            confirmed = 0,
            recovered = 0;
        boolean hasDeaths = false,
                hasConfirmed = false,
                hasRecovered = false;
        TreeSet<Coviddata> coviddata = new TreeSet<>();
        coviddata.addAll(countries.get(0).getCoviddataList());
        //Την βασική χώρα την γράφουμε ξεχωριστά
        for(Coviddata c: coviddata) {
            //Εάν το Coviddata είναι deaths
            if(c.getDatakind() == 1) {
                    //πρόσθεσε την ποσότητα
                    deaths += c.getQty();
                    hasDeaths = true;
            }
            //Εάν το Coviddata είναι recovered
            else if (c.getDatakind() == 2) {
                //πρόσθεσε την ποσότητα
                recovered += c.getQty();
                hasRecovered = true;
            }
            else {//Αλλιώς είναι confirmed 
                //πρόσθεσε την ποσότητα
                confirmed += c.getQty();
                hasConfirmed = true;
            }
        }        
        //Γράψε στην πρώτη θέση του πίνακα και στο πρώτο πεδίο το όνομα της χώρας
        locations[0] ="        [\""+countries.get(0).getName();
        //Εάν υπάρχουν δεδομένα για τις επιλεγμένες ημερομηνίες
        if(!coviddata.isEmpty()) {
            //https://stackoverflow.com/a/48429952/15090628
            //Με τον κώδικα που που είναι στα σχόλια, όταν είχαμε εισαγωγή νέας χώρα και πηγαίναμε να τυπώσουμε χάρτη πετούσε exception κατά την κλήση toInstand()
            locations[0] += ", <br>Δεδομένα από: "+LocalDate.from(Instant.ofEpochMilli(coviddata.first().getTrndate().getTime())
                                                            .atZone(ZoneId.systemDefault()))
                                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+
                                                                    /*coviddata.first().getTrndate()
                                                                    .toInstant()
                                                                    .atZone(ZoneId.systemDefault())
                                                                    .toLocalDate()*/
                                                                    
                            " έως: "+LocalDate.from(Instant.ofEpochMilli(coviddata.last().getTrndate().getTime())
                                              .atZone(ZoneId.systemDefault()))
                                              .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                                       /*.toInstant()
                                                     .atZone(ZoneId.systemDefault())
                                                     .toLocalDate()*/
            locations[0] +=", <br>Θάνατοι=";
            //Εάν έχει θανάτους
            if(hasDeaths)
                //Γράψε τους στο πρώτο πεδίο
                locations[0] += deaths;
            else locations[0] += "Δεν υπάρχουν δεδομένα";
            locations[0] +=", <br>Κρούσματα=";
            //Εαν έχει επιβεβαιωμένα κρούσματα
            if(hasConfirmed)
                //Γράψε τα στο πρώτο πεδίο
                locations[0] += confirmed;
            else locations[0] += "Δεν υπάρχουν δεδομένα";
            locations[0] +=", <br>Ανέκαμψαν=";
            //Εάν έχει επανακάμψαντες
            if(hasRecovered)
                //Γράψε τους στο πρώτο πεδίο
                locations[0] += recovered;
            else locations[0] += "Δεν υπάρχουν δεδομένα";
        }
        else locations[0] += "<br>Δεν υπάρχουν δεδομένα για τις επιλεγμένες ημερομηνίες."
                           + "<br>Παρακαλώ επιλέξτε μια προγενέστερη ημερομηνία 'Από'"
                           + "<br>ή μεταβείτε στην διαχείριση δεδομένων και ενημερώστε"
                           + "<br>τα δεδομένα της χώρας.";
            //Στο δεύτερο και τρίτο πεδίο γράψε lat και long
            locations[0] +="\", "+countries.get(0).getLat()+", "+countries.get(0).getLong1()+"]";
        
        
        //Διαπέρασε τις υπόλοιπες χώρες και κάνε τα ίδια
        for(int i=1; i<countries.size(); i++) {
            coviddata.clear();
            coviddata.addAll(countries.get(i).getCoviddataList());
            deaths = 0;
            confirmed = 0;
            recovered = 0;
            hasDeaths = false;
            hasConfirmed = false;
            hasRecovered = false;
            for(Coviddata c: countries.get(i).getCoviddataList()) {
                //Εάν το Coviddata είναι deaths
                if(c.getDatakind() == 1) {
                        //πρόσθεσε την ποσότητα
                        deaths += c.getQty();
                        hasDeaths = true;
                }
                //Εάν το Coviddata είναι recovered
                else if (c.getDatakind() == 2) {
                    //πρόσθεσε την ποσότητα
                    recovered += c.getQty();
                    hasRecovered = true;
                }
                else {//Αλλιώς είναι confirmed 
                    //πρόσθεσε την ποσότητα
                    confirmed += c.getQty();
                    hasConfirmed = true;
                }
            }        
            //Γράψε στην πρώτη θέση του πίνακα και στο πρώτο πεδίο το όνομα της χώρας
            locations[i] =",\n        [\""+countries.get(i).getName();
            //Εάν υπάρχουν δεδομένα για τις επιλεγμένες ημερομηνίες
            if(!coviddata.isEmpty()) {
                locations[i] += ", <br>Δεδομένα από: "+LocalDate.from(Instant.ofEpochMilli(coviddata.first().getTrndate().getTime())
                                                                .atZone(ZoneId.systemDefault()))
                                                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+
                                " έως: "+LocalDate.from(Instant.ofEpochMilli(coviddata.last().getTrndate().getTime())
                                                  .atZone(ZoneId.systemDefault()))
                                                  .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                locations[i] +=", <br>Θάνατοι=";
                //Εάν έχει θανάτους
                if(hasDeaths)
                    //Γράψε τους στο πρώτο πεδίο
                    locations[i] += deaths;
                else locations[i] += "Δεν υπάρχουν δεδομένα";
                locations[i] +=", <br>Κρούσματα=";
                //Εαν έχει επιβεβαιωμένα κρούσματα
                if(hasConfirmed)
                    //Γράψε τα στο πρώτο πεδίο
                    locations[i] += confirmed;
                else locations[i] += "Δεν υπάρχουν δεδομένα";
                locations[i] +=", <br>Ανέκαμψαν=";
                //Εάν έχει επανακάμψαντες
                if(hasRecovered)
                    //Γράψε τους στο πρώτο πεδίο
                    locations[i] += recovered;
                else locations[i] += "Δεν υπάρχουν δεδομένα";
            }
        else locations[i] += "<br>Δεν υπάρχουν δεδομένα για τις επιλεγμένες ημερομηνίες."
                           + "<br>Παρακαλώ επιλέξτε μια προγενέστερη ημερομηνία 'Από'"
                           + "<br>ή μεταβείτε στην διαχείριση δεδομένων και ενημερώστε"
                           + "<br>τα δεδομένα της χώρας.";
            //Στο δεύτερο και τρίτο πεδίο γράψε lat και long
            locations[i] +="\", "+countries.get(i).getLat()+", "+countries.get(i).getLong1()+"]";
        }
        Boolean ioError = false;
        //https://stackoverflow.com/a/1001568/15090628
        //https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream("html\\mappage.html"),
                    "UTF-8"
                )
            );
        ) {
            //Γράψε στο αρχείο
            bw.write(
                    "<!DOCTYPE html>\n" +
                    "<html> \n" +
                    "<head> \n" +
                    "  <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /> \n" +
                    "  <title>Google Maps Multiple Markers</title> \n" +
                    "  <script src=\"http://maps.google.com/maps/api/js?sensor=false\" \n" +
                    "          type=\"text/javascript\"></script>\n" +
                    "</head> \n" +
                    "<body style=\"margin: 0; padding: 0;\">\n" +
                    "  <div id=\"map\" style=\"width: "+width+"px; height: "+height+"px;\"></div>\n" +
                    "\n" +
                    "  <script type=\"text/javascript\">\n" +
                    "    var locations = [\n");
            bw.flush();
                    //Βάλε τον πίνακα με τις χώρες
            for(int i=0; i<locations.length; i++) {
                bw.write(locations[i]);   
            }
            bw.flush();
            bw.write(
                    "\n    ];\n" +
                    "\n" +
                    "    var map = new google.maps.Map(document.getElementById('map'), {\n" +
                    "      zoom: "+zoom+",\n" +
                    "      center: new google.maps.LatLng("+countries.get(0).getLat()+", "+countries.get(0).getLong1()+"),\n" +
                    "      mapTypeId: google.maps.MapTypeId.ROADMAP\n" +
                    "    });\n" +
                    "\n" +
                    "    var infowindow = new google.maps.InfoWindow();\n" +
                    "\n" +
                    "    var marker, i;\n" +
                    "\n" +
                    "    for (i = 0; i < locations.length; i++) {  \n" +
                    "      marker = new google.maps.Marker({\n" +
                    "        position: new google.maps.LatLng(locations[i][1], locations[i][2]),\n" +
                    "        map: map\n" +
                    "      });\n" +
                    "\n" +
                    "      google.maps.event.addListener(marker, 'click', (function(marker, i) {\n" +
                    "        return function() {\n" +
                    "          infowindow.setContent(locations[i][0]);\n" +
                    "          infowindow.open(map, marker);\n" +
                    "        }\n" +
                    "      })(marker, i));\n" +
                    "    }\n" +
                    "  </script>\n" +
                    "</body>\n" +
                    "</html>"
            );
           
        }
        catch (IOException ex) {
            ioError = true;
            System.out.println("ioError");
        }
        catch (Exception ex) {
            AppSystem.terminate("Συνέβη κάποιο μη αναστρέψιμο σφάλμα κατά την εγγραφή του χάρτη,<br>η εφαρμογή θα τερματιστεί");
            System.out.println("Error");
        }
        finally {            
            //Συμβάν firePropertyChange, ενημερώνει τον presenter ότι η διαδικασία τελείωσε
            AppSystem.getPropChangeFirer().firePropertyChange("Html writer, oldVal->true, newVal->hasIOError", true, (boolean)ioError);
        }
    }
}
