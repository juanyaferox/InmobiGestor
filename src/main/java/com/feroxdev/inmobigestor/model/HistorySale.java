package com.feroxdev.inmobigestor.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "historysale")
@NamedQueries({
        @NamedQuery(name = "Historysale.findAll", query = "SELECT h FROM Historysale h"),
        @NamedQuery(name = "Historysale.findByIdHistorySale", query = "SELECT h FROM Historysale h WHERE h.idHistorySale = :idHistorySale"),
        @NamedQuery(name = "Historysale.findBySalePrice", query = "SELECT h FROM Historysale h WHERE h.salePrice = :salePrice"),
        @NamedQuery(name = "Historysale.findBySaleDate", query = "SELECT h FROM Historysale h WHERE h.saleDate = :saleDate")})
public class HistorySale implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idHistorySale")
    private Integer idHistorySale;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "salePrice")
    private BigDecimal salePrice;
    @Basic(optional = false)
    @Column(name = "saleDate")
    @Temporal(TemporalType.DATE)
    private Date saleDate;
    @JoinColumn(name = "idEstate", referencedColumnName = "idEstate")
    @ManyToOne(optional = false)
    private Estates idEstate;
    @JoinColumn(name = "idClientPrevious", referencedColumnName = "idClient")
    @ManyToOne
    private Clients idClientPrevious;
    @JoinColumn(name = "idClientActual", referencedColumnName = "idClient")
    @ManyToOne
    private Clients idClientActual;

    public HistorySale() {
    }

    public HistorySale(Integer idHistorySale) {
        this.idHistorySale = idHistorySale;
    }

    public HistorySale(Integer idHistorySale, Date saleDate) {
        this.idHistorySale = idHistorySale;
        this.saleDate = saleDate;
    }

    public Integer getIdHistorySale() {
        return idHistorySale;
    }

    public void setIdHistorySale(Integer idHistorySale) {
        this.idHistorySale = idHistorySale;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Estates getIdEstate() {
        return idEstate;
    }

    public void setIdEstate(Estates idEstate) {
        this.idEstate = idEstate;
    }

    public Clients getIdClientPrevious() {
        return idClientPrevious;
    }

    public void setIdClientPrevious(Clients idClientPrevious) {
        this.idClientPrevious = idClientPrevious;
    }

    public Clients getIdClientActual() {
        return idClientActual;
    }

    public void setIdClientActual(Clients idClientActual) {
        this.idClientActual = idClientActual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHistorySale != null ? idHistorySale.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HistorySale)) {
            return false;
        }
        HistorySale other = (HistorySale) object;
        if ((this.idHistorySale == null && other.idHistorySale != null) || (this.idHistorySale != null && !this.idHistorySale.equals(other.idHistorySale))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pa.cifpaviles.dam.mavenproject1.Historysale[ idHistorySale=" + idHistorySale + " ]";
    }

}
