package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import app.LoginInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Will display the admin subsystem and allow for
 * the creation and the deletion of users. It will also
 * list all current users.
 * @author Rizwan Khan(mrk150) && Ahmed Ghoneim(asg179)
 *
 */
public class AdminController implements Initializable{

	/**
	 * Allows admin to add a new user
	 */
	@FXML Button createUser;
	/**
	 * Allows admin to delete a user
	 */
	@FXML Button deleteUser;
	/**
	 * Logs out from admin subsystem
	 */
	@FXML Button logout;
	
	//File storing userNames and passWords
	File file = new File("login.txt");
	FileWriter writer = null;
	BufferedWriter bufferWriter;
	
	/*
	 * Holds list of users using loginInfo format
	 */
	private ObservableList<LoginInfo> userList = FXCollections.observableArrayList();
	ArrayList<LoginInfo> userArrayList = new ArrayList<LoginInfo>();
	
	/**
	 * Sets the users into the listView
	 */
	@FXML ListView<LoginInfo> userListView;
	
	private Stage currentScene;
	private Scene newScene;
	FXMLLoader loader;

	/**
	 * Load initial set of users, if any exist
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		//begin to scan the login file to check for existence and set listView]
		//ln("now im running");
		loadUsers();
		saveUserList();
	}
	
	/**
	 * The action of pushing a button. Will create a user,
	 * delete a user, or logout.
	 * @param e Pushing a button
	 * @throws IOException
	 */
	public void buttonPushed2 (ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		
		if(b == createUser){
			//go to scene view
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/createUser.FXML"));
			
			currentScene = (Stage) b.getScene().getWindow();
			AnchorPane root = (AnchorPane)loader.load();
			newScene = new Scene(root,600,400);
			newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
			currentScene.setScene(newScene);
			currentScene.show();
			
			//added to make sure the observable list includes the newly added user, if any
			this.userArrayList.removeAll(userArrayList);
			this.userList.removeAll(userList);
			//ln("running");
			loadUsers();
			
		}else if(b == deleteUser){
			
			userList.remove(userListView.getSelectionModel().getSelectedItem());
			userListView.setItems(userList);
			
		}else if (b == logout){
			
			saveUserList();
			//writer.close();
			//bufferWriter.close();
			
			//go back to main scene view
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/login.FXML"));
			
			currentScene = (Stage) b.getScene().getWindow();
			AnchorPane root = (AnchorPane)loader.load();
			newScene = new Scene(root,600,400);
			newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
			currentScene.setScene(newScene);
			currentScene.show();
		}
	}
	
	/**
	 * Serializes the list of users into "userArrayList.ser" file
	 * in the same working folder
	 */
	public void saveUserList(){
		
		if (userArrayList.size() > 0 && userArrayList.get(0).getUser().equals("admin") == false){
			LoginInfo x = new LoginInfo("admin", "admin");
			userArrayList.add(0, x);
		}
		
		//ln("writing data in admin");
		for (int i = 0; i < userArrayList.size(); i++){
			//ln("num2: userArrayList[" + i + "]: " + userArrayList.get(i));
		}
		for (int i = 0; i < userList.size(); i++){
			//ln("num2: userList[" + i + "]: " + userList.get(i));
		}
		try{
			FileOutputStream fileOut = new FileOutputStream("userArrayList.ser");
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(userArrayList);
			outStream.close();
			fileOut.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		//ln("end of save");
		
		/*try {
			writer = new FileWriter(file, false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bufferWriter = new BufferedWriter(writer);
		
		for (int i = 0; i < userList.size(); i++){
			//ln("writing data");
			if (i == 0){
				try {
					bufferWriter.write("admin;admin;");
					bufferWriter.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			String user = userList.get(i).getUser();
			String pass = userList.get(i).getPass();
			
			String toWrite1 = user + ";" + pass + ";";
			try {
				bufferWriter.write(toWrite1);
				bufferWriter.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			bufferWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	
	public void shutdown(){
		//ln("Admin stop");
		//saveUserList();
	}
	
	/**
	 * Loads users into a list. If a serializable file already exits
	 * in the same of "userArrayList.ser", it will deserialize that. Else
	 * it will use the logout.txt upon the first run.
	 */
	public void loadUsers(){
		File temp = new File("userArrayList.ser");
		if (temp.exists()){
			try{
		          FileInputStream fileIn =new FileInputStream("userArrayList.ser");
		          ObjectInputStream in = new ObjectInputStream(fileIn);
		          userArrayList = (ArrayList<LoginInfo>) in.readObject();
		          userList = FXCollections.observableArrayList(userArrayList);	
		          userListView.setItems(userList);   
		          in.close();
		          fileIn.close();
		       }catch(IOException | ClassNotFoundException i){
		          i.printStackTrace();
		       }
		}else{
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
				LoginInfo user = new LoginInfo(tempUser, tempPass);
				userList.add(user);
				userArrayList.add(user);
			}
			userData.close();
					
			
		}
		if (userArrayList.size() > 0 && userArrayList.get(0).getUser().equals("admin") && userArrayList.size() > 0 && userList.get(0).getUser().equals("admin")){
			userArrayList.remove(0);
			userList.remove(0);
		}
		
		//set the listView to show the list of users
		userListView.setItems(userList);
		//for (int i = 0; i < userArrayList.size(); i++){
			////ln("userArrayList[" + i + "]: " + userArrayList.get(i));
			//if(userArrayList.get(i).getUser().equals("admin")){
				////ln("true");
				//userArrayList.remove(i);
			//}
		//}
	}
	
}
