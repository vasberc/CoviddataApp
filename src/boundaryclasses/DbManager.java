package boundaryclasses;

import coviddataapp.AppSystem;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;
import model.Country;
import model.Coviddata;

/**
 *Χρήσιμη εντολή SQL για το το τέλος των δοκιμών ALTER TABLE COVIDDATA ALTER COLUMN COVIDDATA RESTART WITH 1
 * 
 */
public class DbManager {
    private static EntityManager em;//Για να τον χρησιμοποιούμε σε κάθε query στην εφαρμογή 
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("CovidDataAppPU");
   
    public DbManager() {
        String oldVal = "",
               newVal = "";
        try {
            em = emf.createEntityManager(); //αρχικοποιώ τη μεταβλητή em
            newVal = "Η σύνδεση με την βάση δεδομένων ολοκληρώθηκε";
            AppSystem.getPropChangeFirer().firePropertyChange("Db login", oldVal, newVal);
        }
        catch (PersistenceException e ) {
            //Μήνυμα αν δεν έχει γίνει σύνδεση στην βάση δεδομένων
            if(e.getCause().getCause() instanceof SQLNonTransientConnectionException){
                AppSystem.terminate("<html>Η βάση δεδομένων δεν είναι συνδεμένη, η εφαρμογή θα τερματιστεί.</html>");//Τερματίζει την εφαρμογή 
            }
        }
        catch (Exception e) {
            //Μήνυμα για οποιαδήποτε άλλη εξέραιση κατά την αρχικοποίηση του DbManager
            e.printStackTrace();
            AppSystem.terminate("<html>Συνέβη ένα αναπάντεχο σφάλμα η εφαρμογής θα τερματιστεί</html>");
            
        }
    }
    
    public TreeMap<String, List<Coviddata>> insertCountries(List<Country> serverList){
        //Θα μπουν όλα τα  Coviddata και θα επιστραφούν για ενέργειες του χρήστη
        TreeMap<String, List<Coviddata>> data = new TreeMap<>();
        Boolean inserted = false;
        String labelString;
        //Θα μπουν όλες οι χώρες που έχει η βάση δεδομένων για σύγκριση
        List<Country> dbList = em.createNamedQuery("Country.findAll", Country.class)
                                 .getResultList();
        em.getTransaction().begin(); //ξεκινάω μια καινούργια συναλλαγή        
        for(Country country: serverList){            
            //Εισαγωγή δεδομένων coviddata στο TreeMap
            data.put(country.getName(), country.getCoviddataList());
            //Θέτω null το coviddataList για εισαγωγής της χώρας στην βάση δεδομένων
            country.setCoviddataList(null);
            //Θα εισαχθεί αν δεν υπάρχει ήδη στην βάση δεδομένων
            if(!dbList.contains(country)) {
                em.persist(country); 
                inserted = true;
            }         
        }
        em.getTransaction().commit();//κάνω το commit όταν έχω τελειώσει με τις επαναλήψεις
        //Δημιουργώ PropertyChange event για ενέργειες του Presenter
        if(inserted)
            labelString = "Οι χώρες αποθηκεύτηκαν στη βάση δεδομένων επιτυχώς";
        else labelString = "Δεν υπάρχουν νέες χώρες για αποθήκευση";
        AppSystem.getPropChangeFirer().firePropertyChange("Countries insert", "", labelString);
        return data;
    }
    
    public void insertData(List<Coviddata> data ){      
        //Χρησιμοποιώ TreeSet για να βελτιώσω τους χρόνους σύγκρισης 
        TreeSet<Coviddata> dbList = new TreeSet<>();
        Boolean inserted = false;
        String labelString;
        dbList.addAll(em.createNamedQuery("Coviddata.findAll", Coviddata.class).getResultList());
        em.getTransaction().begin();       
        for(Coviddata d : data){               
            //Θα εισαχθεί στην βάση δεδομένων μόνο αν δεν υπάρχει ήδη
            if(!dbList.contains(d))  {
                em.persist(d); 
                inserted = true;
            }
            
        }
        em.getTransaction().commit(); //Κάνω commit 1 φορά για όλες τις εγγραφές
        //Δημιουργία PropertyChange event
        if(inserted)
            labelString = "Τα δεδομένα αποθηκεύτηκαν στη βάση επιτυχώς";
        else labelString = "Δεν υπάρχουν καινούρια δεδομένα για αποθήκευση";
        AppSystem.getPropChangeFirer().firePropertyChange("Covid data insert", "", labelString);    
        
    }
    //Διαγράφει όλα τα δεδομένα
    public void deleteAll() {
        em.getTransaction().begin();
        //Διαγράφω από την βάση τα δεδομένα
        em.createNamedQuery("Coviddata.deleteAll").executeUpdate();
        //Διαγράφω από την βάση την χώρα
        em.createNamedQuery("Country.deleteAll").executeUpdate();
        em.getTransaction().commit();
    }
    //Διαγράφει όλα τα δεδομένα Covviddata για την χώρα που δίνεται σαν όρισμα
    public void deleteCoviddataByCountry (Country c) {
        em.getTransaction().begin();
        //Named query με παράμετρο
        em.createNamedQuery("Coviddata.deleteByCountry").setParameter("coutryname", c.getName()).executeUpdate();
        em.getTransaction().commit();
    }
    //Διαγράφει από την βάση δεδομένων την χώρα που περνάει σαν όρισμα
    public void deleteCountry(Country c) {
        em.getTransaction().begin();
        em.createNamedQuery("Country.deleteByName").setParameter("name", c.getName()).executeUpdate();
        em.getTransaction().commit();
    }
    //Διαγράφει όλα τα ομοειδή δεδομένα με βάση την χώρα και τον τύπο που έχει το Coviddata που περνάει σαν όρισμα
    public void deleteCoviddataByCountryNtype (Coviddata c) {
        em.getTransaction().begin();
        em.createNamedQuery("Coviddata.deleteByCountryNdata")
          .setParameter("coutryname", c.getCountry().getName())
          .setParameter("datakind", c.getDatakind()).executeUpdate();
        em.getTransaction().commit();
    }
    //Βρίσκει για μια χώρα όλα τα δεδομένα με βάση το είδος
    public TreeSet<Coviddata> findByCountryNtype (Country c, int datakind) {
        TreeSet<Coviddata> dataList = new TreeSet<>();
        dataList.addAll( em.createNamedQuery("Coviddata.findByCountryNdata", Coviddata.class)
                           .setParameter("coutryname", c.getName())
                           .setParameter("datakind", datakind).getResultList()
        );
        return dataList;
    }
    //Βρίσκει τα δεδομένα μιας χώρας ανάμεσα σε 2 ημερομηνίες
    public TreeSet<Coviddata> findByCoyntryDataBetweenDates(Country c, int datakind, Date fromDate, Date toDate) {
        TreeSet<Coviddata> dataList = new TreeSet<>();
        dataList.addAll(em.createNamedQuery("Coviddata.findBYCountryDataBetweenDates", Coviddata.class)
                          .setParameter("coutryname", c.getName()).setParameter("datakind", datakind)
                          .setParameter("fromDate", fromDate, TemporalType.DATE).setParameter("toDate", toDate, TemporalType.DATE).getResultList()
        );
        return dataList;
        
    }
    //Βρίσκει για κάθε χώρα που έχει Coviddata στην βάση δεδομένων
    //την τελευταία εγγραφή Covviddata με βάση την ημερομηνία
    public List<Coviddata> findLastData(){
        List<Country> list = em.createNamedQuery("Country.findByData", Country.class).getResultList();
        List<Coviddata> lastData = new ArrayList<>();
        for(Country c: list) {
            TreeSet<Coviddata> set = findByCountryNtype(c,1);
            if(!set.isEmpty())
                lastData.add(set.last());
            set = findByCountryNtype(c,2);
            if(!set.isEmpty())
                lastData.add(set.last());
            set = findByCountryNtype(c,3);
            if(!set.isEmpty())
                lastData.add(set.last());            
        }
        return lastData;
    }
    //static μέθοδος για επιστροφή του Entity Manager
    public static EntityManager getEm() {
        return em;
    }
}
