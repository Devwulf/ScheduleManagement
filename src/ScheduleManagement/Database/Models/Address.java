package ScheduleManagement.Database.Models;

import ScheduleManagement.Database.Annotations.*;

import java.sql.Timestamp;

@Table(name = "address")
public class Address
{
    @Key(isAutoGen = true)
    private int addressId = 0;
    @ForeignKey(fieldName = "city")
    private int cityId;
    @Exclude
    private City city;

    private String address;
    private String address2;
    private String postalCode;

    @Column(name = "phone")
    private String phoneNumber;

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

    public int getAddressId()
    {
        return addressId;
    }

    public void setAddressId(int addressId)
    {
        this.addressId = addressId;
    }

    public int getCityId()
    {
        return cityId;
    }

    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }

    public City getCity()
    {
        return city;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
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
}
