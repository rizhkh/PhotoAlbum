package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class LoginController {

	/*
	 * JavaFX Fields to instantiate user input and login button
	 */
	@FXML Button login;
	@FXML TextField userName;
	@FXML PasswordField password;
	
	/*
	 * Strings to hold what the user will enter
	 */
	private String user = null;
	private String pass = null;
	public static String useruser = "";
	
	private Stage currentScene;
	private Scene newScene;
	FXMLLoader loader;
	
	
	
	
	/*
	 * Array Lists that will compare the user input to stored 
	 * userNames and passWords
	 */
	List<String> x = new ArrayList<String> ();
	List<String> y = new ArrayList<String> ();
	
	//File storing userNames and passWords
	File file = new File("login.txt");
	
	//Action of pushing login button
	public void buttonPushed (ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		
		if (b == login){
			//get the userName and passWord
			if (userName.getText().isEmpty() || password.getText().isEmpty()){
				Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Required Fields empty");
	        	alert.setHeaderText("You left required fields empty. Please follow input directions.");
	        	alert.showAndWait();
	        	return;
	        }
			user = userName.getText();
			pass = password.getText();
			useruser = user;
		}
		
		//begin to scan the login file to check for existence
		Scanner userData = new Scanner(file);
		int userPass = 0;
		
		while (userData.hasNextLine()){
			//scan whole line
			Scanner scan = new Scanner(userData.nextLine());
			
			scan.useDelimiter(";");
			while (scan.hasNext()){
				String data = scan.next();
				if (userPass == 0){
					//add to userName table
					x.add(data);
				}
				
				if (userPass == 1){
					//add to passWord table
					y.add(data);
				}
				
				userPass++;
			}
			userPass = 0;
			scan.close();
		}
		userData.close();
		
		//IGNORE, ERROR CHECKING
		for (int i = 0; i < x.size(); i++){
			System.out.println("X[" + i + "]: " + x.get(i));
		}
		//IGNORE, ERROR CHECKING
		for (int i = 0; i < y.size(); i++){
			System.out.println("Y[" + i + "]: " + y.get(i));
		}
		
		boolean passCheck = false;//to check if the info matches anything
		
		//check if user is admin or regular
		for (int i  = 0; i < x.size(); i++){
			if (user.equals(x.get(i))){
				if (pass.equals(y.get(i))){
					//user and pass match
					if (user.equals("admin")){
						passCheck = true;
						//GO TO ADMIN SUBSYSTEM
						System.out.println("Good");//TESTING PURPOSES
						
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/admin.FXML"));
						
						currentScene = (Stage) b.getScene().getWindow();
						AnchorPane root = (AnchorPane)loader.load();
						newScene = new Scene(root,600,400);
						newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
						currentScene.setScene(newScene);
						
						AdminController controller = loader.getController();
						
						currentScene.setOnHidden(e12 -> {
							try {
								controller.shutdown();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Platform.exit();
						});
						
						currentScene.show();
						break;
					}else{
						//GO TO USER SUBSYSTEM
						System.out.println("Bad");//TESTING PURPOSES
						
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/albumView.FXML"));
						
						currentScene = (Stage) b.getScene().getWindow();
						AnchorPane root = (AnchorPane)loader.load();
						newScene = new Scene(root,600,400);
						newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
						currentScene.setScene(newScene);
						
						albumViewController controller = loader.getController();
						controller.setUserName(user);
						currentScene.show();
						
						passCheck = true;
						break;
					}
				}else{
				}
			}else{
				
			}
		}
		
		//nothing matched in the array, error message
		if (passCheck == false){
			System.out.println("Username/Password is incorrect");
			Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Incorrect Info");
	    	alert.setHeaderText("The username/password combo is incorrect");
	    	alert.showAndWait();
		}
		
		//clear the arrays to avoid confusion in next read in
		x.clear();
		y.clear();
	}
}
