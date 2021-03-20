package boundaryclasses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import coviddataapp.AppSystem;
import java.io.IOException;
import coviddataapp.CovidDataType;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.Country;
import model.Coviddata;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * 
 * 
 */
public class ServerConnection{    
    //static μέθοδος για συλλογή δεδομένων 
    public static List<Country> reciveData(CovidDataType datatype) {
        //Λίστα που θα μπουν όλα τα δεδομένα
        List<Country> countries = new ArrayList<>();
        //Το URL που θα πάρουμε όλα τα δεδομένα, αλλάζει με βάση το CovidDataType που περνάμε σαν όρισμα
        String urlToCall = "https://covid2019-api.herokuapp.com/timeseries/"+datatype.getString();
        OkHttpClient client=new OkHttpClient();        
        Request request = new Request.Builder().url(urlToCall).build();        
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseString=response.body().string();
                
                JsonParser parser = new JsonParser();
                GsonBuilder gsonBuilder = new GsonBuilder();
                //Βάζω τον custom adapter που έχω φτιάξει
                gsonBuilder.registerTypeAdapter(Country.class, new CountryTypeAdapter());
                Gson gson = gsonBuilder.create();
                //Μετατροπή του responseString se jsonTree
                JsonElement jsonTree = parser.parse(responseString);
                //Κάνω το jsonTree JsonObject για να χρησιμοποιήσω την get()
                JsonObject jsonObject = jsonTree.getAsJsonObject();
                //Με την get() παίρνω τις χώρες σαν JsonElement που είναι εμφωλευμένες στο datatype
                JsonElement jcountries = jsonObject.get(datatype.getString()); 
                //Διατρέχω τα εμφωλευμένα Element και τα διαβάζω σύμφωνα με τον adapter που όρισα
                for(JsonElement jCountry : jcountries.getAsJsonArray()){
                    //Παίρνω το στοιχείο σαν Country
                    Country country = gson.fromJson(jCountry ,Country.class);
                    //Εάν υπάρχει ήδη η χώρα με το ίδιο όνομα μέσα στη λίστα
                    if(countries.contains(country)){                        
                        Country updatedCountry = countries.get(countries.indexOf(country));
                        countries.remove(updatedCountry);//Την αφαιρώ
                        //Ψάχνω τις ίδιες ημερομηνίες για να προσθέσω τις ποσότητες
                        for(Coviddata d: updatedCountry.getCoviddataList())
                            for(Coviddata dNew: country.getCoviddataList()){
                                if(d.getTrndate().equals(dNew.getTrndate())){
                                    d.setProodqty(d.getProodqty()+dNew.getProodqty());
                                    //κάνω break από την τελευταία for για να ενημερώσω την επόμενη ημερομηνία
                                    break;
                                }               
                            }
                        //Τα θέτω null για να τα φτιάξω μετά από το αρχείο
                        if(updatedCountry.getLat() != null) {
                            updatedCountry.setLat(null);
                            updatedCountry.setLong1(null);
                        }   
                        //Με το τέλος των 2 for βάζω στην λίστα την updatedCountry
                        countries.add(updatedCountry);
                    }
                    //Αν δεν υπάρχει η χώρα την βάζω όπως είναι
                    else countries.add(country);
                }
                //Διαπερνάω πάλι τις χώρες για να φτιάξω την ημερήσια ποσότητα και να θέσω id
                for(Country country: countries){
                    //Θέτω id με βάση το όνομα
                    country.setCountry(country.getName().hashCode());
                    int prevQnt = 0;//Σωρευτική ποσότητα προηγούμενης μέρας αρχικοποιείται σε 0 αφού δεν υπάρχει προηγούμενη μέρα
                    //Διαπερνώ το coviddataList της χώρας
                    for(Coviddata d: country.getCoviddataList()){
                        //Θέτω το datatype σύμφωνα με το όρισμα που περάσαμε
                        d.setDatakind((short)datatype.getValue());
                        //Θέτω το dayQnt με βάση το prevQnt
                        d.setQty(d.getProodqty()-prevQnt);
                        //Κάνω το prevQnt να ισούται με το Proodqty της τωρινής μέρας
                        prevQnt = d.getProodqty();
                    }
                    //Εάν η χώρα έχει null στα lat και long, θα τα διαβάσουμε από το αρχείο
                    if(country.getLat() == null && country.getLong1() == null) {
                        File countriesLatLong = new File("countriesLatLong.txt");
                        Scanner scanner = new Scanner(countriesLatLong);
                        //Διαπερνάει όλες τις γραμμές
                        while (scanner.hasNextLine()) {
                            //Κάνουμε τα ονόματα σε lower case για να μην υπάρχει τυχόν διαφορά
                            String line = scanner.nextLine().toLowerCase();
                            String name = country.getName().toLowerCase();
                            //αρχικοποίηση των μεταβλητών
                            double lat = 0;
                            double long1 = 0;
                            //Εάν η γραμμή έχει το όνομα της χώρας
                            if(line.contains(name)) {
                                //Διαβάζει το lat
                                lat = Double.parseDouble(line.substring(line.indexOf("lat")+3, line.indexOf("long")));
                                //Διαβάζει το long1
                                long1 = Double.parseDouble(line.substring(line.indexOf("long")+4, line.indexOf("eol")));
                                //Τα θέτει στην χώρα
                                country.setLat(lat);
                                country.setLong1(long1);
                            }
                        }
                        scanner.close();
                    }
                }
            }
            //firePropertyChange event
            AppSystem.getPropChangeFirer().firePropertyChange("server conection", "", "Η σύνδεση με τον σέρβερ ολοκληρώθηκε, εισαγωγή χωρών στη βάση δεδομένων");
        }
        catch (IOException e){
            //Σε περίπτωση IOException, δίνω μήνυμα να γίνει ξανά η προσπάθεια
            AppSystem.getPropChangeFirer().firePropertyChange("server conection", "", "<html><span style=\"color:red\">Η σύνδεση με τον σέρβερ απέτυχε, παρακαλώ δοκιμάστε ξανά</span></html>");
            countries = null;//Επιστρέφει null για να μην εκτελεστεί η μέθοδος του DbManager
        }
        catch (Exception e){
            //Σε άλλο Exception τερματίζω το πρόγραμμα
            AppSystem.terminate("<html>Συνέβη ένα αναπάντεχο σφάλμα η εφαρμογή θα τερματιστεί</html>"); 
        }
        return countries;
    } 
}
//Κλάση type adapter για την ανάγνωση json elements
class CountryTypeAdapter extends TypeAdapter<Country> {
    @Override//Για την ανάγνωση
    public Country read(JsonReader in) throws IOException {
        final Country country = new Country();
        in.beginObject();//Για κάθε στοιχείο στο Json String που είναι μέσα σε {}, θα εκτελεστεί η παρακάτω ανάγνωση
        List<Coviddata> datas = new ArrayList<>();
        while (in.hasNext()) {//Θα εκτελεστεί όσο υπάρχουν ακόμη πεδία
            String fieldName = in.nextName();//η nextName() επιστρέφει το όνομα του πεδίου
            try{//Όταν το πεδία είναι το Province/State
                if ("Province/State".equals(fieldName)) {
                    //Αγνοώ το state το βάζω
                    in.skipValue();                    
                }//Για το πεδίο Country/Region
                else if ("Country/Region".equals(fieldName)) {
                    //θέτει σαν όνομα χώρας το όνομα
                    country.setName(in.nextString());  
                }//Για το πεδίο Lat, απλά το περνάει στο πεδίο της χώρας  
                else if ("Lat".equals(fieldName)) {
                    country.setLat(in.nextDouble());
                }//Για το πεδίο Long, απλά το περνάει στο πεδίο της χώρας  
                else if ("Long".equals(fieldName)) {
                    country.setLong1(in.nextDouble());
                } 
                //Κάθε άλλο πεδίο στο Json String αναφέρεται σε μια ημερομηνία
                else {
                    // Παίρνει από το όνομα του πεδίου την ημερομηνία                       
                    int firstindex = fieldName.indexOf("/");//Βρες τον δείκτη για το πρώτο /
                    int lastindex = fieldName.lastIndexOf("/");//Βρες τον δείκτη για το δεύτερο /
                    int month = Integer.parseInt(fieldName.substring(0, firstindex));//Ο μήνας είναι από την αρχή μέχρι το 1ο /
                    int day = Integer.parseInt(fieldName.substring(firstindex+1, lastindex));//Η ημέρα ανάμεσα στα 2 /
                    int year = Integer.parseInt(fieldName.substring(lastindex+1))+2000;//Το έτος από το 2 / μέχρι το τέλος
                    //Μεταβλητή Date που είναι συμβατή με την βάση δεδομένων
                    Date date = java.sql.Date.valueOf(LocalDate.of(year, month, day));
                    Coviddata data = new Coviddata(country, date, in.nextInt());//Δημιουργώ ένα αντικείμενο αντικείμενο Coviddata
                    datas.add(data);//Το προσθέτω στη λίστα που θα μπει στο αντικείμενο χώρας.
                }
            }
            //Αν το αρχείο έχει κάποια μη συμβατή τιμή αγνόησέ την
            catch (IllegalArgumentException e){
                in.skipValue();
            }
            //Σε κάθε άλλο σφάλμα το πετάει στην μέθοδο reciveData() της ServerCnnection class 
            catch (Exception e){
                throw e;
            }
        }
        in.endObject();//Συνάντησε }
        country.setCoviddataList(datas);//Δίνουμε στην χώρα τη λίστα με τα coviddata
        
        return country;//επιστρέφει τη χώρα
    }
    //Για την εγγραφή σε Json String, δεν χρειάζεται στα πλαίσια της εργασίας
    @Override
    public void write(JsonWriter writer, Country t) throws IOException {
        
    }

}