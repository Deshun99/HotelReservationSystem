/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.enumeration.EmployeeAccessRightsEnum;

/**
 *
 * @author wong-
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    
    @Column(length = 32, nullable = false)
    private String employeeName;
    
    @Column(length = 32, unique = true, nullable = false)
    private String username;
    
    @Column(length = 32, nullable = false)
    private String password;
    
    @Column(nullable = false)
    private EmployeeAccessRightsEnum accessRights;

    public Long getEmployeeId() {
        return employeeId;
    }
    
    public Employee(){
    }

    public Employee(String employeeName, String username, String password, EmployeeAccessRightsEnum accessRights) {
        this.employeeName = employeeName;
        this.username = username;
        this.password = password;
        this.accessRights = accessRights;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Employee's Name: " + employeeName +
                "\nEmployee's Role: " + accessRights + "\n";
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the accessRights
     */
    public EmployeeAccessRightsEnum getAccessRights() {
        return accessRights;
    }

    /**
     * @param accessRights the accessRights to set
     */
    public void setAccessRights(EmployeeAccessRightsEnum accessRights) {
        this.accessRights = accessRights;
    }
    
}
