/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Βασίλης
 */
@Entity
@Table(name = "COVIDDATA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Coviddata.findAll", query = "SELECT c FROM Coviddata c")
    , @NamedQuery(name = "Coviddata.findByCoviddata", query = "SELECT c FROM Coviddata c WHERE c.coviddata = :coviddata")
    , @NamedQuery(name = "Coviddata.findByTrndate", query = "SELECT c FROM Coviddata c WHERE c.trndate = :trndate")
    , @NamedQuery(name = "Coviddata.findByDatakind", query = "SELECT c FROM Coviddata c WHERE c.datakind = :datakind")
    , @NamedQuery(name = "Coviddata.findByQty", query = "SELECT c FROM Coviddata c WHERE c.qty = :qty")
    , @NamedQuery(name = "Coviddata.findByProodqty", query = "SELECT c FROM Coviddata c WHERE c.proodqty = :proodqty")
    //Βρίσκει δεδομένα με το όνομα της χώρας, ανάμεσα σε 2 ημερομηνίες
    , @NamedQuery(name = "Coviddata.findBYCountryDataBetweenDates", query = "SELECT c FROM Coviddata c WHERE c.country.name = :coutryname and c.datakind = :datakind and c.trndate >= :fromDate and c.trndate <= :toDate")    
      //Βρίσκει δεδομένα με το όνομα της χώρας
    , @NamedQuery(name = "Coviddata.findByCountry", query = "SELECT c FROM Coviddata c WHERE c.country.name = :coutryname")
      //Βρίσκει τα δεδομένα με βάση τον τύπο
    , @NamedQuery(name = "Coviddata.findByCountryNdata", query = "SELECT c FROM Coviddata c WHERE c.country.name = :coutryname and c.datakind = :datakind")
      //διαγραφή με την χώρα και τον τύπο
    , @NamedQuery(name = "Coviddata.deleteByCountryNdata", query = "DELETE FROM Coviddata c WHERE c.country.name = :coutryname and c.datakind = :datakind")
      //διαγραφή με την χώρα  
    , @NamedQuery(name = "Coviddata.deleteByCountry", query = "DELETE FROM Coviddata c WHERE c.country.name = :coutryname") 
      //διαγραφή όλων
    , @NamedQuery(name = "Coviddata.deleteAll", query = "DELETE FROM Coviddata c")
})
public class Coviddata implements Serializable, Comparable<Coviddata> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COVIDDATA")
    private Integer coviddata;
    @Basic(optional = false)
    @Column(name = "TRNDATE")
    @Temporal(TemporalType.DATE)
    private Date trndate;
    @Basic(optional = false)
    @Column(name = "DATAKIND")
    private short datakind;
    @Basic(optional = false)
    @Column(name = "QTY")
    private int qty;
    @Basic(optional = false)
    @Column(name = "PROODQTY")
    private int proodqty;
    @JoinColumn(name = "COUNTRY", referencedColumnName = "COUNTRY")
    @ManyToOne(optional = false)
    private Country country;

    public Coviddata() {
    }

    public Coviddata(Integer coviddata) {
        this.coviddata = coviddata;
    }
    
    public Coviddata(Country country, Date trndate, int proodqty) {
        this.country = country;
        this.trndate = trndate;
        this.proodqty = proodqty;
    }

    public Coviddata(Integer coviddata, Date trndate, short datakind, int qty, int proodqty) {
        this.coviddata = coviddata;
        this.trndate = trndate;
        this.datakind = datakind;
        this.qty = qty;
        this.proodqty = proodqty;
    }

    public Integer getCoviddata() {
        return coviddata;
    }

    public void setCoviddata(Integer coviddata) {
        this.coviddata = coviddata;
    }

    public Date getTrndate() {
        return trndate;
    }

    public void setTrndate(Date trndate) {
        this.trndate = trndate;
    }

    public short getDatakind() {
        return datakind;
    }

    public void setDatakind(short datakind) {
        this.datakind = datakind;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getProodqty() {
        return proodqty;
    }

    public void setProodqty(int proodqty) {
        this.proodqty = proodqty;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coviddata != null ? coviddata.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Coviddata)) {
            return false;
        }
        Coviddata other = (Coviddata) object;
        //Είναι ίδια αντικείμενα αν αναφέρονται στην ίδια χώρα και στην ίδια ημερομηνία και στο ίδιο είδος
        if ((this.country.equals(other.country)) && (datakind==other.datakind) && (trndate.equals(other.trndate))) {
            return true;
        }
        return false;
    }
    
    @Override
    public int compareTo(Coviddata other) {
        if(country.equals(other.country))//Αν αναφέρονται στην ίδια χώρα
            //σύγκριση με βάση την ημερομηνία
            if(trndate.equals(other.trndate))
                //αν είναι ίδια η ημερομηνία σύγκρινε με βάση το datakind
                return datakind-other.datakind;
            else
                return trndate.compareTo(other.trndate);
        //Αλλιώς σύγκριση με βάση το όνομα χώρας 
        return country.getName().compareTo(other.country.getName());
    }

    @Override
    public String toString() {
        return "model.Coviddata[ coviddata=" + coviddata + " ]";
    }
    
}
