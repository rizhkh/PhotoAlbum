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

public class imageViewController implements Initializable {


	private Stage currentScene;
	private Scene newScene; 
	FXMLLoader loader;
	Stage stage;	
	@FXML private ImageView imgview; 
	String currentPath = "";
	 Image image;

	
	@FXML public void nextPic(ActionEvent event) 
    { 

			expandedViewController a = new expandedViewController();
			int index = a.pathx.indexOf(currentPath);
			System.out.println("index :" + index);
			String path = "";
			int sizee = a.pathx.size();
			if(index < sizee-1)
			{
				path = a.pathx.get(index+1);
				System.out.println(path);
				setImage(path);
				File file = new File(path);
		       Image image = new Image(file.toURI().toString());
		        imgview.setImage(image);
			}
		
    }	

	@FXML public void prevPic(ActionEvent event) 
    { 
		expandedViewController a = new expandedViewController();
		int index = a.pathx.indexOf(currentPath);
		//currentPath = a.pathx.get(index-1);
		System.out.println(currentPath);
		String path = "";  
		if(index>=0)
		{
			path = a.pathx.get(index-1);
			System.out.println(path);
			setImage(path);
			File file = new File(path);
	       Image image = new Image(file.toURI().toString());
	        imgview.setImage(image);
		}
    }		

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
	
	@FXML public void logOut(ActionEvent event) throws IOException 
    { 
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

	public void setImage(String path)
	{
		
		expandedViewController a = new expandedViewController();
		currentPath = path;
		System.out.println("image path : " + a.strngFP);
		File file = new File(currentPath);
        Image image = new Image(file.toURI().toString());
        imgview.setImage(image);
				
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		expandedViewController a = new expandedViewController();
		currentPath = a.strngFP;
		System.out.println("image path : " + a.strngFP);
		setImage(currentPath);
		/*
		expandedViewController a = new expandedViewController();
		currentPath = a.strngFP;
		System.out.println("image path : " + a.strngFP);
		File file = new File(a.strngFP);
        Image image = new Image(file.toURI().toString());
        imgview.setImage(image);
		*/
	}		
		
	
}
