package ScheduleManagement.Managers;

import ScheduleManagement.Database.DBContext;
import ScheduleManagement.Database.NameValuePair;
import ScheduleManagement.Database.Exceptions.IllegalQueryResultSizeException;
import ScheduleManagement.Database.Models.User;
import ScheduleManagement.Utils.TimestampHelper;

import java.sql.Timestamp;
import java.util.List;

public class LoginManager
{
    // TODO: Possibly implement a dependency injection system, similar to ASP.NET Core
    private DBContext context;
    private User currentUser = null;

    public boolean signup(String username, String password)
    {
        try
        {
            List<User> result = context.Users.readEntity(
                    new NameValuePair("userName", username)
            );

            if (result.size() > 1)
                throw new IllegalQueryResultSizeException("The query result size for signup cannot be more than 1. " +
                        "This most likely means there are duplicate users in the database.");

            // This means there's already a user with this username in the database
            if (result.size() == 1)
                return false;

            Timestamp now = TimestampHelper.nowUTC();
            User user = new User();
            user.setUserId(0);
            user.setUsername(username);
            user.setPassword(password);
            user.setActive(true);
            user.setDateCreated(now);
            user.setCreatedBy(username);
            user.setDateModified(now);
            user.setModifiedBy(username);

            context.Users.createEntity(user);

            return true;
        }
        catch (IllegalQueryResultSizeException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String username, String password)
    {
        try
        {
            List<User> result = context.Users.readEntity(
                    new NameValuePair("userName", username),
                    new NameValuePair("password", password),
                    new NameValuePair("active", true)
            );

            if (result.size() > 1)
                throw new IllegalQueryResultSizeException("The query result size for login cannot be more than 1. " +
                        "This most likely means there are duplicate users in the database.");

            // No user with the given username and password found
            // or user is not active anymore
            if (result.size() < 1)
                return false;

            // active actually means if the account is still active and not deleted
            currentUser = result.get(0);

            Log.logToFile("[" + TimestampHelper.now() + "]: User '" + username + "' has logged in!");
            return true;
        }
        catch (IllegalQueryResultSizeException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void logout()
    {
        if (currentUser != null)
            Log.logToFile("[" + TimestampHelper.now() + "]: User '" + currentUser.getUsername() + "' has logged out!");
        currentUser = null;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    /* Singleton implementation below */

    private static LoginManager instance = null;

    private LoginManager()
    {
        context = DBContext.getInstance();
    }

    public static LoginManager getInstance()
    {
        if (instance == null)
            instance = new LoginManager();

        return instance;
    }
}
