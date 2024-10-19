package com.feroxdev.inmobigestor.model;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
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
@Table(name = "estates")
@NamedQueries({
        @NamedQuery(name = "Estates.findAll", query = "SELECT e FROM Estates e"),
        @NamedQuery(name = "Estates.findByIdEstate", query = "SELECT e FROM Estates e WHERE e.idEstate = :idEstate"),
        @NamedQuery(name = "Estates.findByReference", query = "SELECT e FROM Estates e WHERE e.reference = :reference"),
        @NamedQuery(name = "Estates.findByFullAddress", query = "SELECT e FROM Estates e WHERE e.fullAddress = :fullAddress"),
        @NamedQuery(name = "Estates.findByImagePath", query = "SELECT e FROM Estates e WHERE e.imagePath = :imagePath"),
        @NamedQuery(name = "Estates.findByState", query = "SELECT e FROM Estates e WHERE e.state = :state")})
public class Estates implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idEstate")
    private Integer idEstate;
    @Basic(optional = false)
    @Column(name = "reference")
    private int reference;
    @Basic(optional = false)
    @Column(name = "fullAddress")
    private String fullAddress;
    @Column(name = "imagePath")
    private String imagePath;
    @Column(name = "state")
    private Integer state;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstate")
    private List<HistorySale> historysaleList;
    @JoinColumn(name = "idBranch", referencedColumnName = "idBranch")
    @ManyToOne(optional = false)
    private Branchs idBranch;
    @JoinColumn(name = "idClient", referencedColumnName = "idClient")
    @ManyToOne
    private Clients idClient;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstate")
    private List<HistoryRent> historyrentList;

    public Estates() {
    }

    public Estates(Integer idEstate) {
        this.idEstate = idEstate;
    }

    public Estates(Integer idEstate, int reference, String fullAddress) {
        this.idEstate = idEstate;
        this.reference = reference;
        this.fullAddress = fullAddress;
    }

    public Integer getIdEstate() {
        return idEstate;
    }

    public void setIdEstate(Integer idEstate) {
        this.idEstate = idEstate;
    }

    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<HistorySale> getHistorysaleList() {
        return historysaleList;
    }

    public void setHistorysaleList(List<HistorySale> historysaleList) {
        this.historysaleList = historysaleList;
    }

    public Branchs getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(Branchs idBranch) {
        this.idBranch = idBranch;
    }

    public Clients getIdClient() {
        return idClient;
    }

    public void setIdClient(Clients idClient) {
        this.idClient = idClient;
    }

    public List<HistoryRent> getHistoryrentList() {
        return historyrentList;
    }

    public void setHistoryrentList(List<HistoryRent> historyrentList) {
        this.historyrentList = historyrentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstate != null ? idEstate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estates)) {
            return false;
        }
        Estates other = (Estates) object;
        if ((this.idEstate == null && other.idEstate != null) || (this.idEstate != null && !this.idEstate.equals(other.idEstate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pa.cifpaviles.dam.mavenproject1.Estates[ idEstate=" + idEstate + " ]";
    }

}
