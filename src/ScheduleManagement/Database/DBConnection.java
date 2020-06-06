package ScheduleManagement.Database;

import java.sql.Connection;

public class DBConnection
{
    private String name;
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public DBConnection(String name, String url, String username, String password)
    {
        this.name = name;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public Connection getConnection()
    {
        return connection;
    }

    public void setConnection(Connection connection)
    {
        if (this.connection == null)
            this.connection = connection;
    }
}
