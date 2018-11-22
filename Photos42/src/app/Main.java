package app;
	
import controller.AdminController;
import controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

/**
 * Main driver class to start up the project
 * @author Rizwan Khan(mrk150) && Ahmed Ghoneim(asg179)
 *
 */
public class Main extends Application {
	
	/**
	 * Starts the application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.FXML"));
			
			AnchorPane root = (AnchorPane)loader.load();
			LoginController controller = loader.getController();
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Photo Album");
			primaryStage.getIcons().add((new Image("file:photo.png")));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}