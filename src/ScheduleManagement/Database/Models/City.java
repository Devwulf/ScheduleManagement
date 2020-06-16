package ScheduleManagement.Database.Models;

import ScheduleManagement.Database.Annotations.*;

import java.sql.Timestamp;

@Table(name = "city")
public class City
{
    @Key(isAutoGen = true)
    private int cityId = 0;

    private String city;

    @ForeignKey(fieldName = "country")
    private int countryId;
    @Exclude
    private Country country;

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

    public int getCityId()
    {
        return cityId;
    }

    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }

    public int getCountryId()
    {
        return countryId;
    }

    public void setCountryId(int countryId)
    {
        this.countryId = countryId;
    }

    public Country getCountry()
    {
        return country;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
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
        return city;
    }
}
