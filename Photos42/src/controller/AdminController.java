package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminController implements Initializable{

	/**
	 * These are parameters for the buttons in this scene
	 */
	@FXML Button createUser;
	@FXML Button deleteUser;
	@FXML Button logout;
	
	/*
	 * Class to hold loginInfo in the form of (userName, passWord)
	 */
	class loginInfo{
		String userName;
		String passWord;
		
		public loginInfo(String userName, String passWord){
			this.userName = userName;
			this.passWord = passWord;
		}
		
		public String getUser(){
			return this.userName;
		}
		
		public String getPass(){
			return this.passWord;
		}
		
		@Override
		public String toString(){
			return getUser();
		}
	}
	
	//File storing userNames and passWords
	File file = new File("login.txt");
	
	/*
	 * Holds list of users using loginInfo format
	 */
	private ObservableList<loginInfo> userList = FXCollections.observableArrayList();
	
	/*
	 * Sets userList into listView
	 */
	@FXML ListView<loginInfo> userListView;
	
	private Stage currentScene;
	private Scene newScene;
	FXMLLoader loader;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//begin to scan the login file to check for existence and set listView
		Scanner userData = null;
		try {
			userData = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int userPass = 0;
				
		while (userData.hasNextLine()){
			//scan whole line
			Scanner scan = new Scanner(userData.nextLine());
			
			//temp holders for username and password
			String tempUser = null;
			String tempPass = null;
					
			scan.useDelimiter(";");
			while (scan.hasNext()){
				String data = scan.next();
				
				//skipping to not display admin as an editable user
				if (data.equals("admin"))
					continue;
				
				if (userPass == 0){
					//store userName 
					tempUser = data;
				}
				
				if (userPass == 1){
					//store passWord
					tempPass = data;
				}
				
				userPass++;
			}
			userPass = 0;
			scan.close();
			
			//skipping to not display admin as an editable user
			if (tempUser == null || tempPass == null)
				continue;
			
			//store info into node and load into observable list
			loginInfo user = new loginInfo(tempUser, tempPass);
			userList.add(user);
		}
		userData.close();
				
		//set the listView to show the list of users
		userListView.setItems(userList);
	}
	
	//Action of pushing login button
	public void buttonPushed2 (ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		
		if(b == createUser){
			//go to scene view
			System.out.println("herekne");
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/createUser.FXML"));
			
			currentScene = (Stage) b.getScene().getWindow();
			AnchorPane root = (AnchorPane)loader.load();
			newScene = new Scene(root,600,400);
			newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
			currentScene.setScene(newScene);
			currentScene.show();
		}else if(b == deleteUser){
			
		}else if (b == logout){
			
		}
	}
	
}