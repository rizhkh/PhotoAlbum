package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Displays an image. Can toggle between the next image and the previous
 * image in the album. 
 * @author Rizwan Khan(mrk150) && Ahmed Ghoneim(asg179)
 *
 */
public class imageViewController implements Initializable {


	private Stage currentScene;
	private Scene newScene; 
	FXMLLoader loader;
	Stage stage;	
	@FXML private ImageView imgview; 
	String currentPath = "";
	 Image image;

	/**
	 * Goes to the next picture in the album
	 * @param event Pushing "Next" Button
	 */
	@FXML public void nextPic(ActionEvent event) 
    { 

			expandedViewController a = new expandedViewController();
			int index = a.pathx.indexOf(currentPath);
			//System.out.println("index :" + index);
			String path = "";
			int sizee = a.pathx.size();
			if(index < sizee-1)
			{
				path = a.pathx.get(index+1);
				//System.out.println(path);
				setImage(path);
				File file = new File(path);
		       Image image = new Image(file.toURI().toString());
		        imgview.setImage(image);
			}
		
    }	

	/**
	 * Goes to previous picture in album
	 * @param event Pushing "Previous" Button
	 */
	@FXML public void prevPic(ActionEvent event) 
    { 
		expandedViewController a = new expandedViewController();
		int index = a.pathx.indexOf(currentPath);
		//currentPath = a.pathx.get(index-1);
		//System.out.println(currentPath);
		String path = "";  
		if(index>=0)
		{
			path = a.pathx.get(index-1);
			//System.out.println(path);
			setImage(path);
			File file = new File(path);
	       Image image = new Image(file.toURI().toString());
	        imgview.setImage(image);
		}
    }		

	/**
	 * Goes back to the view of all photos in an album
	 * @param event Pushing "Go Back" Button
	 * @throws IOException
	 */
	@FXML public void goBack(ActionEvent event) throws IOException 
    { 
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/expandedView.FXML"));
		
		currentScene = (Stage) b.getScene().getWindow();
		AnchorPane root = (AnchorPane)loader.load();
		newScene = new Scene(root,600,400);
		newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
		currentScene.setScene(newScene);
		currentScene.show();	
    }
	
	/**
	 * Logs out of current session
	 * @param event Pushing "Logout" Button
	 * @throws IOException
	 */
	@FXML public void logOut(ActionEvent event) throws IOException 
    { 
		LoginController a = new LoginController();
		a.useruser = "";
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/login.FXML"));
		
		currentScene = (Stage) b.getScene().getWindow();
		AnchorPane root = (AnchorPane)loader.load();
		newScene = new Scene(root,600,400);
		newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
		currentScene.setScene(newScene);
		currentScene.show();	
    }

	/**
	 * Sets the image onto the display 
	 * @param path The path of the image
	 */
	public void setImage(String path)
	{
		
		expandedViewController a = new expandedViewController();
		currentPath = path;
		//System.out.println("image path : " + a.strngFP);
		File file = new File(currentPath);
        Image image = new Image(file.toURI().toString());
        imgview.setImage(image);
				
	}
	
	/**
	 * Obtains the path of the image and calls setImage() to display it.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		expandedViewController a = new expandedViewController();
		currentPath = a.strngFP;
		//System.out.println("image path : " + a.strngFP);
		setImage(currentPath);
	}		
		
	
}
 