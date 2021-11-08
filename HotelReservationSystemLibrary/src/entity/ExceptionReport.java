/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author wong-
 */
@Entity
public class ExceptionReport implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exceptionReportId;
    
    @Temporal(TemporalType.DATE)
    private Date exceptionReportDate;
    
    @Column(length = 140)
    private String errorReport; 
    
    @OneToOne(optional = false)
    private ReservationRecord reservation;
    
    public ExceptionReport(Date exceptionDate, String errorReport, ReservationRecord reservation) {
        this.exceptionReportDate = exceptionReportDate;
        this.errorReport = errorReport;
        this.reservation = reservation;
    }

    public ExceptionReport() {
    }
    
    
    
    


    /**
     * @return the reservation
     */
    public ReservationRecord getReservation() {
        return reservation;
    }

    /**
     * @param reservation the reservation to set
     */
    public void setReservation(ReservationRecord reservation) {
        this.reservation = reservation;
    }

    /**
     * @return the exceptionReportDate
     */
    public Date getExceptionReportDate() {
        return exceptionReportDate;
    }

    /**
     * @param exceptionReportDate the exceptionReportDate to set
     */
    public void setExceptionReportDate(Date exceptionReportDate) {
        this.exceptionReportDate = exceptionReportDate;
    }

    /**
     * @return the errorReport
     */
    public String getErrorReport() {
        return errorReport;
    }

    /**
     * @param errorReport the errorReport to set
     */
    public void setErrorReport(String errorReport) {
        this.errorReport = errorReport;
    }

    

    public Long getExceptionReportId() {
        return exceptionReportId;
    }

    public void setExceptionReportId(Long exceptionReportId) {
        this.exceptionReportId = exceptionReportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (exceptionReportId != null ? exceptionReportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the exceptionReportId fields are not set
        if (!(object instanceof ExceptionReport)) {
            return false;
        }
        ExceptionReport other = (ExceptionReport) object;
        if ((this.exceptionReportId == null && other.exceptionReportId != null) || (this.exceptionReportId != null && !this.exceptionReportId.equals(other.exceptionReportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ExceptionReport[ id=" + exceptionReportId + " ]";
    }
    
}
