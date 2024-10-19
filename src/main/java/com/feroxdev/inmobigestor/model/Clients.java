package com.feroxdev.inmobigestor.model;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author hulke
 */
@Entity
@Table(name = "clients")
@NamedQueries({
        @NamedQuery(name = "Clients.findAll", query = "SELECT c FROM Clients c"),
        @NamedQuery(name = "Clients.findByIdClient", query = "SELECT c FROM Clients c WHERE c.idClient = :idClient"),
        @NamedQuery(name = "Clients.findByIdEstateRented", query = "SELECT c FROM Clients c WHERE c.idEstateRented = :idEstateRented"),
        @NamedQuery(name = "Clients.findByName", query = "SELECT c FROM Clients c WHERE c.name = :name"),
        @NamedQuery(name = "Clients.findByLastname1", query = "SELECT c FROM Clients c WHERE c.lastname1 = :lastname1"),
        @NamedQuery(name = "Clients.findByLastname2", query = "SELECT c FROM Clients c WHERE c.lastname2 = :lastname2"),
        @NamedQuery(name = "Clients.findByEmail", query = "SELECT c FROM Clients c WHERE c.email = :email"),
        @NamedQuery(name = "Clients.findByDni", query = "SELECT c FROM Clients c WHERE c.dni = :dni"),
        @NamedQuery(name = "Clients.findByPhone", query = "SELECT c FROM Clients c WHERE c.phone = :phone"),
        @NamedQuery(name = "Clients.findByAddress", query = "SELECT c FROM Clients c WHERE c.address = :address")})
public class Clients implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idClient")
    private Integer idClient;
    @Column(name = "idEstateRented")
    private Integer idEstateRented;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "lastname1")
    private String lastname1;
    @Column(name = "lastname2")
    private String lastname2;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "dni")
    private String dni;
    @Column(name = "phone")
    private String phone;
    @Column(name = "address")
    private String address;
    @OneToMany(mappedBy = "idClientPrevious")
    private List<HistorySale> historysaleList;
    @OneToMany(mappedBy = "idClientActual")
    private List<HistorySale> historysaleList1;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne
    private Users idUser;
    @JoinColumn(name = "idBranch", referencedColumnName = "idBranch")
    @ManyToOne(optional = false)
    private Branchs idBranch;
    @OneToMany(mappedBy = "idClient")
    private List<Estates> estatesList;
    @OneToMany(mappedBy = "idClient")
    private List<HistoryRent> historyrentList;
    @OneToMany(mappedBy = "idClientRented")
    private List<HistoryRent> historyrentList1;

    public Clients() {
    }

    public Clients(Integer idClient) {
        this.idClient = idClient;
    }

    public Clients(Integer idClient, String name, String lastname1, String dni) {
        this.idClient = idClient;
        this.name = name;
        this.lastname1 = lastname1;
        this.dni = dni;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }

    public Integer getIdEstateRented() {
        return idEstateRented;
    }

    public void setIdEstateRented(Integer idEstateRented) {
        this.idEstateRented = idEstateRented;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname1() {
        return lastname1;
    }

    public void setLastname1(String lastname1) {
        this.lastname1 = lastname1;
    }

    public String getLastname2() {
        return lastname2;
    }

    public void setLastname2(String lastname2) {
        this.lastname2 = lastname2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<HistorySale> getHistorysaleList() {
        return historysaleList;
    }

    public void setHistorysaleList(List<HistorySale> historysaleList) {
        this.historysaleList = historysaleList;
    }

    public List<HistorySale> getHistorysaleList1() {
        return historysaleList1;
    }

    public void setHistorysaleList1(List<HistorySale> historysaleList1) {
        this.historysaleList1 = historysaleList1;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    public Branchs getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(Branchs idBranch) {
        this.idBranch = idBranch;
    }

    public List<Estates> getEstatesList() {
        return estatesList;
    }

    public void setEstatesList(List<Estates> estatesList) {
        this.estatesList = estatesList;
    }

    public List<HistoryRent> getHistoryrentList() {
        return historyrentList;
    }

    public void setHistoryrentList(List<HistoryRent> historyrentList) {
        this.historyrentList = historyrentList;
    }

    public List<HistoryRent> getHistoryrentList1() {
        return historyrentList1;
    }

    public void setHistoryrentList1(List<HistoryRent> historyrentList1) {
        this.historyrentList1 = historyrentList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idClient != null ? idClient.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clients)) {
            return false;
        }
        Clients other = (Clients) object;
        if ((this.idClient == null && other.idClient != null) || (this.idClient != null && !this.idClient.equals(other.idClient))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pa.cifpaviles.dam.mavenproject1.Clients[ idClient=" + idClient + " ]";
    }

}