package ScheduleManagement.Database.Models;

import ScheduleManagement.Database.Annotations.Key;
import ScheduleManagement.Database.Annotations.Column;
import ScheduleManagement.Database.Annotations.NotUpdatable;
import ScheduleManagement.Database.Annotations.Table;

import java.sql.*;

@Table(name = "user")
public class User
{
    @Key(isAutoGen = true)
    private int userId = 0;

    @Column(name = "userName")
    private String username;

    // This won't have a column annotation because the column name
    // is already the same as the field name
    private String password;

    @Column(name = "active")
    private boolean isActive = false;

    @Column(name = "createDate")
    @NotUpdatable
    private Timestamp dateCreated;
    @NotUpdatable
    private String createdBy;

    @Column(name = "lastUpdate")
    @NotUpdatable // The database automatically sets this anyways
    private Timestamp dateModified;
    @Column(name = "lastUpdateBy")
    private String modifiedBy;

    // Getters/Setters
    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
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
}
