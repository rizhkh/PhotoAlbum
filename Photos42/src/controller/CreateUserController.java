package controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateUserController implements Initializable{

	//File storing userNames and passWords
	File file = new File("login.txt");
 	FileWriter fileWriter ;  
	BufferedWriter bufferedWriter;
	
	@FXML TextField userName; 
	@FXML PasswordField pw; 
	@FXML Button confirm;
	@FXML Button cancel;

	private Stage currentScene;
	private Scene newScene;
	FXMLLoader loader;
	
	List<String> x = new ArrayList<String> ();
	List<String> y = new ArrayList<String> ();
	
	/*
	Create user button pressed - checks if folder exist if not creates it 
	calls other methods checks if username exist if not creates it
	*/
	 
	@FXML public void createUser(ActionEvent event) throws IOException 
	{
		//get the userName and passWord
		if (userName.getText().isEmpty() || pw.getText().isEmpty()){
			Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Required Fields empty");
        	alert.setHeaderText("You left required fields empty. Please follow input directions.");
        	alert.showAndWait();
        	return;
        }
		
		String user = userName.getText();
		String password = pw.getText();
		
		/*//begin to scan the login file to check for existence
		Scanner userData = new Scanner(file);
		int userPass = 0;
				
		while (userData.hasNextLine()){
			//scan whole line
			Scanner scan = new Scanner(userData.nextLine());
			
			scan.useDelimiter(";");
			while (scan.hasNext()){
				String data = scan.next();
				System.out.println("data: " + data);
				if (userPass == 0){
					if (user.equals(data)){
					Alert alert = new Alert(AlertType.ERROR);
			        alert.setTitle("Required Fields empty");
		        	alert.setHeaderText("This user already exists, please try again.");
		        	alert.showAndWait();
		        	return;
					}else{
						x.add(data);
					}
				}
				
				if (userPass == 1)
					y.add(data);
				
				userPass++;
			}
			userPass = 0;
			scan.close();		
		}
		userData.close();
		//if we are here, then this is new user, add to list, possibly create folder
		
		saveUserList();*/
		
		System.out.println("confirm pressed !");
		String currentpath = System.getProperty("user.dir");
		System.out.println("current path is:" + currentpath);
		currentpath = currentpath + "\\";
		currentpath = currentpath.replace("\\", "/");
		
		String strDirectoy ="user/" + user;
		
		
		currentpath = currentpath + strDirectoy;
		File pathOfDirectory = new File(currentpath);
		boolean pathDirExist = pathOfDirectory.exists();
		
		if(pathDirExist)
			System.out.println("Folder exists");
		 else {
			System.out.println("Folder does not exists.Creating...");
			int result = createNewUser(user, password);
			if(result==-1) {
				System.out.println("User exists.Try again");
			}
			else{
				Files.createDirectories(Paths.get(currentpath));
			
				//go back to AdminView
				Button b = (Button)event.getSource();
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/view/admin.FXML"));
				
				currentScene = (Stage) b.getScene().getWindow();
				AnchorPane root = (AnchorPane)loader.load();
				newScene = new Scene(root,600,400);
				newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
				currentScene.setScene(newScene);
				currentScene.show();
			
			}
		}
	}
	
	@FXML public void cancelUser(ActionEvent event) throws IOException{
		//go back to AdminView
		
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/admin.FXML"));
		
		currentScene = (Stage) b.getScene().getWindow();
		AnchorPane root = (AnchorPane)loader.load();
		newScene = new Scene(root,600,400);
		newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
		currentScene.setScene(newScene);
		currentScene.show();
	}

	/*
	Create new users
	*/
	
	public int createNewUser(String user, String password) throws IOException
	{
		
		int res = checkUser();
		if(res==-1)
		{
			System.out.println("Re-try. User exists");
			return -1;
		} 
		
		else
		{
			saveUserList(user, password);
		}
		return 0;
	}
	
	/*
	Check if users exists or not
	*/
	
	public int checkUser() throws FileNotFoundException
	{
		//file = new File("login.txt"); 
		Scanner txtscan = new Scanner(new File("login.txt"));
		int check = 0;
		int userPass = 0;
		String uname = userName.getText();
		String p = pw.getText();
		//uname = uname + ";"; //+ p + ";";//just need to check if username is the same, not password
		System.out.println("searching : " + uname);
		
		while(txtscan.hasNextLine())
		{   
		    Scanner scan = new Scanner(txtscan.nextLine());
			
			scan.useDelimiter(";");
			while (scan.hasNext()){
				String data = scan.next();
				System.out.println("data: " + data);
				if (userPass == 0){
					if (uname.equals(data)){
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Already Exists");
						alert.setHeaderText("This user already exists, please try again.");
						alert.showAndWait();
						return -1;
					}else{
						x.add(data);
					}
				}
				
				if (userPass == 1)
					y.add(data);
				
				userPass++;
			}
			userPass = 0;
			scan.close();
		}
		check = 1;
		txtscan.close();		
        return check;
	}



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	  
public void saveUserList(String userN, String password){
		
		try {
			fileWriter = new FileWriter(file, false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bufferedWriter = new BufferedWriter(fileWriter);
		
		for (int i = 0; i < x.size(); i++){
			System.out.println("x[" + i + "] " + x.get(i));
			System.out.println("writing data");
			if (i == 0){
				try {
					bufferedWriter.write("admin;admin;");
					bufferedWriter.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			String user = x.get(i);
			String pass = y.get(i);
			
			String toWrite1 = user + ";" + pass + ";";
			try {
				bufferedWriter.write(toWrite1);
				bufferedWriter.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String toWrite1 = userN + ";" + password + ";";
		
		try {
			bufferedWriter.write(toWrite1);
			bufferedWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			bufferedWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
