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
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
        @NamedQuery(name = "Users.findByIdUser", query = "SELECT u FROM Users u WHERE u.idUser = :idUser"),
        @NamedQuery(name = "Users.findByUser", query = "SELECT u FROM Users u WHERE u.user = :user"),
        @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
        @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
        @NamedQuery(name = "Users.findByName", query = "SELECT u FROM Users u WHERE u.name = :name"),
        @NamedQuery(name = "Users.findByLastname1", query = "SELECT u FROM Users u WHERE u.lastname1 = :lastname1"),
        @NamedQuery(name = "Users.findByLastname2", query = "SELECT u FROM Users u WHERE u.lastname2 = :lastname2"),
        @NamedQuery(name = "Users.findByDni", query = "SELECT u FROM Users u WHERE u.dni = :dni")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idUser")
    private Integer idUser;
    @Basic(optional = false)
    @Column(name = "user")
    private String user;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "lastname1")
    private String lastname1;
    @Basic(optional = false)
    @Column(name = "lastname2")
    private String lastname2;
    @Basic(optional = false)
    @Column(name = "dni")
    private String dni;
    @OneToMany(mappedBy = "idUser")
    private List<Clients> clientsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser")
    private List<Branchs> branchsList;
    @JoinColumn(name = "idBranch", referencedColumnName = "idBranch")
    @ManyToOne(optional = false)
    private Branchs idBranch;

    public Users() {
    }

    public Users(Integer idUser) {
        this.idUser = idUser;
    }

    public Users(Integer idUser, String user, String password, String name, String lastname1, String lastname2, String dni) {
        this.idUser = idUser;
        this.user = user;
        this.password = password;
        this.name = name;
        this.lastname1 = lastname1;
        this.lastname2 = lastname2;
        this.dni = dni;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<Clients> getClientsList() {
        return clientsList;
    }

    public void setClientsList(List<Clients> clientsList) {
        this.clientsList = clientsList;
    }

    public List<Branchs> getBranchsList() {
        return branchsList;
    }

    public void setBranchsList(List<Branchs> branchsList) {
        this.branchsList = branchsList;
    }

    public Branchs getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(Branchs idBranch) {
        this.idBranch = idBranch;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pa.cifpaviles.dam.mavenproject1.Users[ idUser=" + idUser + " ]";
    }

}
