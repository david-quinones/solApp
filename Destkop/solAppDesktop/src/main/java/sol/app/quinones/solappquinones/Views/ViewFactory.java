package sol.app.quinones.solappquinones.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Controllers.Teacher.TeacherController;
import sol.app.quinones.solappquinones.Controllers.admin.AdminController;
import sol.app.quinones.solappquinones.Controllers.user.UserController;

public class ViewFactory {
    //Admin Views
    private AnchorPane dashboardView;

    public ViewFactory(){}

    public AnchorPane getDashboardView(){
        if(dashboardView == null){
            try{
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml")).load();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return  dashboardView;
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Admin.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }

    public void showUserWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/User/User.fxml"));
        UserController userController = new UserController();
        loader.setController(userController);
        createStage(loader);
    }

    public void showTeacherWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Teacher/Teacher.fxml"));
        TeacherController teacherController = new TeacherController();
        loader.setController(teacherController);
        createStage(loader);
    }

    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        }catch(Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("SOLAPP - ESTEL BRESSOL");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }


}
