package ScheduleManagement.Database;

import ScheduleManagement.Models.User;

// NOTE: This class can be instantiated again and again, separately
// in different classes and they can all be used separately
public class DBContext
{
    public DBSet<User> Users;

    public DBContext()
    {
        Users = new DBSet<>(User.class);
    }
}
