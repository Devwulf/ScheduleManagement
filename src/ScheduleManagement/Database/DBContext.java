package ScheduleManagement.Database;

import ScheduleManagement.Database.Models.*;

// NOTE: This class can be instantiated again and again, separately
// in different classes and they can all be used separately
public class DBContext
{
    public DBSet<User> Users;
    public DBSet<Appointment> Appointments;
    public DBSet<Customer> Customers;
    public DBSet<Address> Addresses;
    public DBSet<City> Cities;
    public DBSet<Country> Countries;

    private static DBContext instance = null;

    private DBContext()
    {
        Users = new DBSet<>(User.class);
        Appointments = new DBSet<>(Appointment.class);
        Customers = new DBSet<>(Customer.class);
        Addresses = new DBSet<>(Address.class);
        Cities = new DBSet<>(City.class);
        Countries = new DBSet<>(Country.class);
    }

    public static DBContext getInstance()
    {
        if (instance == null)
            instance = new DBContext();

        return instance;
    }
}
