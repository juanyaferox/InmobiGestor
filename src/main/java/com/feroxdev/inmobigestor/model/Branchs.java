package com.feroxdev.inmobigestor.model;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

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
@Table(name = "branchs")
@NamedQueries({
        @NamedQuery(name = "Branchs.findAll", query = "SELECT b FROM Branchs b"),
        @NamedQuery(name = "Branchs.findByIdBranch", query = "SELECT b FROM Branchs b WHERE b.idBranch = :idBranch"),
        @NamedQuery(name = "Branchs.findByIdTown", query = "SELECT b FROM Branchs b WHERE b.idTown = :idTown")})
public class Branchs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idBranch")
    private Integer idBranch;
    @Basic(optional = false)
    @Column(name = "idTown")
    private int idTown;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBranch")
    private List<Clients> clientsList;
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    @ManyToOne(optional = false)
    private Users idUser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBranch")
    private List<Estates> estatesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBranch")
    private List<Users> usersList;

    public Branchs() {
    }

    public Branchs(Integer idBranch) {
        this.idBranch = idBranch;
    }

    public Branchs(Integer idBranch, int idTown) {
        this.idBranch = idBranch;
        this.idTown = idTown;
    }

    public Integer getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch) {
        this.idBranch = idBranch;
    }

    public int getIdTown() {
        return idTown;
    }

    public void setIdTown(int idTown) {
        this.idTown = idTown;
    }

    public List<Clients> getClientsList() {
        return clientsList;
    }

    public void setClientsList(List<Clients> clientsList) {
        this.clientsList = clientsList;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    public List<Estates> getEstatesList() {
        return estatesList;
    }

    public void setEstatesList(List<Estates> estatesList) {
        this.estatesList = estatesList;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBranch != null ? idBranch.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Branchs)) {
            return false;
        }
        Branchs other = (Branchs) object;
        if ((this.idBranch == null && other.idBranch != null) || (this.idBranch != null && !this.idBranch.equals(other.idBranch))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pa.cifpaviles.dam.mavenproject1.Branchs[ idBranch=" + idBranch + " ]";
    }

}
