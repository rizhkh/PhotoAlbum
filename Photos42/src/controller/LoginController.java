package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import app.LoginInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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

/**
 * Controls the Login view. Obtains user entered credentials and logs in 
 * to appropriate subsystem, if user exists.
 * @author Rizwan Khan(mrk150) && Ahmed Ghoneim(asg179) 
 *
 */
public class LoginController {

	/**
	 * Login button
	 */
	@FXML Button login;
	/**
	 * Field to enter username
	 */
	@FXML TextField userName;
	/**
	 * Field to enter password
	 */
	@FXML PasswordField password;
	
	/*
	 * Strings to hold what the user will enter
	 */
	private String user = null;
	private String pass = null;
	
	private Stage currentScene;
	private Scene newScene;
	FXMLLoader loader;
	
    public static String useruser = "";
	
	/*
	 * Array Lists that will compare the user input to stored 
	 * userNames and passWords
	 */
	List<String> x = new ArrayList<String> ();
	List<String> y = new ArrayList<String> ();
	List<LoginInfo> both = new ArrayList<LoginInfo>();
	
	//File storing userNames and passWords
	File file = new File("login.txt");
	
	/**
	 * The action of pushing a button. It will login to the appropriate
	 * subsystem based on the credentials entered. If admin tries to login
	 * it will go to the admin subsystem. It any other user tries to login
	 * it will go to their subsystem. If a "userArrayList.ser" file of 
	 * serialized users exists in the working folder, it will use that 
	 * to obtain a list of possible users. Else, it will login the first time
	 * using the login.txt file.
	 * @param e
	 * @throws IOException
	 */
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
		
		File temp = new File("userArrayList.ser");
		if (temp.exists()){
			//ln("exists");
			try{
				//ln("in");
		          FileInputStream fileIn =new FileInputStream("userArrayList.ser");
		          ObjectInputStream in = new ObjectInputStream(fileIn);
		          both = (ArrayList<LoginInfo>) in.readObject();
		          //ln(both.size());
		          for(int i = 0; i < both.size(); i++){
		        	  x.add(i, both.get(i).getUser());
		        	  //ln(x.get(i));
		        	  y.add(i, both.get(i).getPass());
		        	  //ln(y.get(i));
		          }  
		          in.close();
		          fileIn.close();
		       }catch(IOException | ClassNotFoundException i){
		          i.printStackTrace();
		       }
		}else{
		
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
		}
		
		//IGNORE, ERROR CHECKING
		for (int i = 0; i < x.size(); i++){
			//ln("X[" + i + "]: " + x.get(i));
		}
		//IGNORE, ERROR CHECKING
		for (int i = 0; i < y.size(); i++){
			//ln("Y[" + i + "]: " + y.get(i));
		}
		
		boolean passCheck = false;//to check if the info matches anything
		
		//both.add(0, new LoginInfo("stock", "stock"));
		//x.add(0, "stock");
		//y.add(0, "stock");
		
		//check if user is admin or regular
		for (int i  = 0; i < x.size(); i++){
			if (user.equals(x.get(i))){
				if (pass.equals(y.get(i))){
					//user and pass match
					if (user.equals("admin")){
						passCheck = true;
						//GO TO ADMIN SUBSYSTEM
						//ln("Good");//TESTING PURPOSES
						
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
						//ln("Bad");//TESTING PURPOSES
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/view/albumView.FXML"));
						
						currentScene = (Stage) b.getScene().getWindow();
						AnchorPane root = (AnchorPane)loader.load();
						newScene = new Scene(root,600,400);
						newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
						currentScene.setScene(newScene);
						
						useruser = user;
						
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
			//ln("Username/Password is incorrect");
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
