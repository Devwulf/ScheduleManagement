package ScheduleManagement.Managers;

import ScheduleManagement.Controllers.BaseController;
import ScheduleManagement.Controllers.LoginController;
import ScheduleManagement.Controllers.PopupController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

// Shorthand for FXMLLoader.load and everything that comes with it
public class ViewManager
{
    public enum ViewNames
    {
        Login,
        Calendar,
        Customers,
        Appointments,
        Reports,
        Settings,
        Popup
    }

    private HashMap<ViewNames, URL> views;
    private String defaultTitle;
    private Stage mainStage;

    private static final String VIEWS_PATH = "/ScheduleManagement/Views/";

    // Prevents changing of the primary stage once assigned
    public void initialize(Stage stage, URL mainViewPath, String title, double width, double height)
    {
        if (stage != null)
        {
            defaultTitle = title;
            mainStage = stage;

            try
            {
                FXMLLoader loader = new FXMLLoader(mainViewPath);
                Parent root = loader.load();

                stage.setTitle(title);

                Scene scene = new Scene(root, width, height);
                stage.setScene(scene);
                stage.show();

                BaseController mainController = loader.getController();
                mainController.stage = stage;
                mainController.lateInitialize();
                mainController.initializeAnimations();
                mainController.initializeLanguage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void addView(ViewNames name, String viewName)
    {
        URL path = ViewManager.class.getResource(VIEWS_PATH + viewName + ".fxml");
        views.put(name, path);
    }

    public void removeView(ViewNames name)
    {
        views.remove(name);
    }

    public <TController extends BaseController> TController loadView(ViewNames name)
    {
        return loadView(mainStage, name, "", 0, 0);
    }

    public <TController extends BaseController> TController loadView(ViewNames name, String viewTitle)
    {
        return loadView(mainStage, name, viewTitle, 0, 0);
    }

    public <TController extends BaseController> TController loadView(ViewNames name, String viewTitle, double width, double height)
    {
        return loadView(mainStage, name, viewTitle, width, height);
    }

    public <TController extends BaseController> TController loadSeparateView(ViewNames name)
    {
        return loadView(new Stage(), name, "", 0, 0);
    }

    public <TController extends BaseController> TController loadSeparateView(ViewNames name, String viewTitle)
    {
        return loadView(new Stage(), name, viewTitle, 0, 0);
    }

    public <TController extends BaseController> TController loadSeparateView(ViewNames name, String viewTitle, double width, double height)
    {
        return loadView(new Stage(), name, viewTitle, width, height);
    }

    private <TController extends BaseController> TController loadView(Stage stageToUse, ViewNames name, String viewTitle, double width, double height)
    {
        try
        {
            URL path = views.get(name);
            FXMLLoader loader = new FXMLLoader(path);
            Parent root = loader.load();

            Scene scene;
            if (width <= 0 ||
                    height <= 0)
                scene = new Scene(root);
            else
                scene = new Scene(root, width, height);

            if (!viewTitle.isEmpty())
                stageToUse.setTitle(viewTitle);
            else
                stageToUse.setTitle(defaultTitle);

            stageToUse.setScene(scene);
            stageToUse.show();

            // Pass in the stage to the controller so the
            // controller can close its own window
            TController controller = loader.getController();
            controller.stage = stageToUse;
            controller.lateInitialize();
            controller.initializeAnimations();
            controller.initializeLanguage();

            return controller;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void showWarningPopup(String message)
    {
        PopupController controller = loadSeparateView(ViewNames.Popup, "Warning");
        controller.showWarningPopup(message);
    }

    public void showErrorPopup(String message)
    {
        PopupController controller = loadSeparateView(ViewNames.Popup, "Error");
        controller.showErrorPopup(message);
    }

    public void showSuccessPopup(String message)
    {
        PopupController controller = loadSeparateView(ViewNames.Popup, "Success");
        controller.showSuccessPopup(message);
    }

    public void showConfirmPopup(String message, Runnable onConfirm) {
        PopupController controller = loadSeparateView(ViewNames.Popup, "Confirm");
        controller.showConfirmPopup(message, onConfirm);
    }

    /*
    public void reloadMainView()
    {
        mainController.reloadView();
    }
    /**/

    /* Singleton implementation below */

    private static ViewManager instance = null;

    // Prevents instantiation of class
    private ViewManager()
    {
        views = new HashMap<>();
    }

    public static ViewManager getInstance()
    {
        if (instance == null)
            instance = new ViewManager();

        return instance;
    }
}