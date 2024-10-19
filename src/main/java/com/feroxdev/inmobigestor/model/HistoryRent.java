package com.feroxdev.inmobigestor.model;
import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author hulke
 */
@Entity
@Table(name = "historyrent")
@NamedQueries({
        @NamedQuery(name = "Historyrent.findAll", query = "SELECT h FROM Historyrent h"),
        @NamedQuery(name = "Historyrent.findByIdHistoryRent", query = "SELECT h FROM Historyrent h WHERE h.idHistoryRent = :idHistoryRent"),
        @NamedQuery(name = "Historyrent.findByStartDate", query = "SELECT h FROM Historyrent h WHERE h.startDate = :startDate"),
        @NamedQuery(name = "Historyrent.findByEndDate", query = "SELECT h FROM Historyrent h WHERE h.endDate = :endDate"),
        @NamedQuery(name = "Historyrent.findByExitDate", query = "SELECT h FROM Historyrent h WHERE h.exitDate = :exitDate")})
public class HistoryRent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idHistoryRent")
    private Integer idHistoryRent;
    @Basic(optional = false)
    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "exitDate")
    @Temporal(TemporalType.DATE)
    private Date exitDate;
    @JoinColumn(name = "idEstate", referencedColumnName = "idEstate")
    @ManyToOne(optional = false)
    private Estates idEstate;
    @JoinColumn(name = "idClient", referencedColumnName = "idClient")
    @ManyToOne
    private Clients idClient;
    @JoinColumn(name = "idClientRented", referencedColumnName = "idClient")
    @ManyToOne
    private Clients idClientRented;

    public HistoryRent() {
    }

    public HistoryRent(Integer idHistoryRent) {
        this.idHistoryRent = idHistoryRent;
    }

    public HistoryRent(Integer idHistoryRent, Date startDate) {
        this.idHistoryRent = idHistoryRent;
        this.startDate = startDate;
    }

    public Integer getIdHistoryRent() {
        return idHistoryRent;
    }

    public void setIdHistoryRent(Integer idHistoryRent) {
        this.idHistoryRent = idHistoryRent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getExitDate() {
        return exitDate;
    }

    public void setExitDate(Date exitDate) {
        this.exitDate = exitDate;
    }

    public Estates getIdEstate() {
        return idEstate;
    }

    public void setIdEstate(Estates idEstate) {
        this.idEstate = idEstate;
    }

    public Clients getIdClient() {
        return idClient;
    }

    public void setIdClient(Clients idClient) {
        this.idClient = idClient;
    }

    public Clients getIdClientRented() {
        return idClientRented;
    }

    public void setIdClientRented(Clients idClientRented) {
        this.idClientRented = idClientRented;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistoryRent != null ? idHistoryRent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistoryRent)) {
            return false;
        }
        HistoryRent other = (HistoryRent) object;
        if ((this.idHistoryRent == null && other.idHistoryRent != null) || (this.idHistoryRent != null && !this.idHistoryRent.equals(other.idHistoryRent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pa.cifpaviles.dam.mavenproject1.Historyrent[ idHistoryRent=" + idHistoryRent + " ]";
    }

}
