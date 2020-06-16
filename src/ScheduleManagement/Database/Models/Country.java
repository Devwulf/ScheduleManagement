package ScheduleManagement.Database.Models;

import ScheduleManagement.Database.Annotations.Column;
import ScheduleManagement.Database.Annotations.Key;
import ScheduleManagement.Database.Annotations.NotUpdatable;
import ScheduleManagement.Database.Annotations.Table;

import java.sql.Timestamp;

@Table(name = "country")
public class Country
{
    @Key(isAutoGen = true)
    private int countryId;

    private String country;

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

    public int getCountryId()
    {
        return countryId;
    }

    public void setCountryId(int countryId)
    {
        this.countryId = countryId;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
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

    // Used for combo boxes of this type
    @Override
    public String toString()
    {
        return country;
    }
}
