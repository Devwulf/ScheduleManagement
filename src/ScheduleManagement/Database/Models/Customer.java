package ScheduleManagement.Database.Models;

import ScheduleManagement.Database.Annotations.*;

import java.sql.Timestamp;

@Table(name = "customer")
public class Customer
{
    @Key(isAutoGen = true)
    private int customerId = 0;

    private String customerName;

    @ForeignKey(fieldName = "address")
    private int addressId;
    @Exclude // Don't want this to be tracked by the dbset too
    private Address address;

    @Column(name = "active")
    private boolean isActive = false;

    @Column(name = "createDate")
    @NotUpdatable
    private Timestamp dateCreated;
    @NotUpdatable
    private String createdBy;

    @Column(name = "lastUpdate")
    @NotUpdatable
    private Timestamp dateModified;
    @Column(name = "lastUpdateBy")
    private String modifiedBy;

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(int customerId)
    {
        this.customerId = customerId;
    }

    public int getAddressId()
    {
        return addressId;
    }

    public void setAddressId(int addressId)
    {
        this.addressId = addressId;
    }

    public Address getAddress()
    {
        return address;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public void setActive(boolean active)
    {
        isActive = active;
    }

    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public Timestamp getDateModified()
    {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified)
    {
        this.dateModified = dateModified;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString()
    {
        return customerName;
    }
}
