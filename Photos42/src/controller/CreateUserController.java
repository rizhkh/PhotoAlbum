package controller;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import app.LoginInfo;
import javafx.collections.FXCollections;
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

/**
 * Creates a new user with the entered credentials. Checks if the info 
 * entered already exists or not to avoid conflict.
 * @author Rizwan Khan(mrk150) && Ahmed Ghoneim(asg179)
 *
 */
public class CreateUserController implements Initializable, Serializable{

	//File storing userNames and passWords
	File file = new File("login.txt");
 	FileWriter fileWriter ;  
	BufferedWriter bufferedWriter;
	
	/**
	 * Field for entering username
	 */
	@FXML TextField userName;
	/**
	 * Field for entering password
	 */
	@FXML PasswordField pw; 
	/**
	 * Button to confirm entry
	 */
	@FXML Button confirm;
	/**
	 * Button to cancel entry
	 */
	@FXML Button cancel;

	private Stage currentScene;
	private Scene newScene;
	FXMLLoader loader;
	
	List<String> x = new ArrayList<String> ();
	List<String> y = new ArrayList<String> ();
	List<LoginInfo> userArrayList = new ArrayList<LoginInfo>();
	
	/*
	Create user button pressed - checks if folder exist if not creates it 
	calls other methods checks if username exist if not creates it
	*/
	
	/**
	 * Creates a new user if they do not exist. Will create a specialized
	 * folder for each user to preserve their data in the project working
	 * directory.
	 * @param event Pushing confirm button
	 * @throws IOException
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
		
		//System.out.println("confirm pressed !");
		String currentpath = System.getProperty("user.dir");
		//System.out.println("current path is:" + currentpath);
		currentpath = currentpath + "\\";
		currentpath = currentpath.replace("\\", "/");
		
		String strDirectoy ="user/" + user;
		
		
		currentpath = currentpath + strDirectoy;
		File pathOfDirectory = new File(currentpath);
		boolean pathDirExist = pathOfDirectory.exists();
		
		if(pathDirExist)
		{}//System.out.println("Folder exists");}
		 else {
			//System.out.println("Folder does not exists.Creating...");
			int result = createNewUser(user, password);
			if(result==-1) {
				//System.out.println("User exists.Try again");
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
	
	/**
	 * Cancels the creation of a new user
	 * @param event Pushing Cancel Button
	 * @throws IOException
	 */
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
	
	/**
	 * Adds info entered into list of users if they do not exist.
	 * @param user The username entered
	 * @param password The password entered
	 * @return Integer for error checking
	 * @throws IOException
	 */
	
	public int createNewUser(String user, String password) throws IOException
	{
		
		int res = checkUser();
		if(res==-1)
		{
			//System.out.println("Re-try. User exists");
			return -1;
		} 
		else{
			saveUserList(user, password);
		}
		return 0;
	}
	
	/*
	Check if users exists or not
	*/
	
	/**
	 * Called in createNewUser() to check if the credentials entered 
	 * match any other user or not
	 * @return Integer for error checking
	 * @throws FileNotFoundException
	 */
	public int checkUser() throws FileNotFoundException
	{
		int check = -1;
		String uname = userName.getText();
		String p = pw.getText();
		//uname = uname + ";"; //+ p + ";";//just need to check if username is the same, not password
		//System.out.println("searching : " + uname);
		for (int i = 0; i < x.size(); i++){
				//System.out.println("data: " + x.get(i));
					if (uname.equals(x.get(i))){
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Already Exists");
						alert.setHeaderText("This user already exists, please try again.");
						alert.showAndWait();
						return -1;
					}
		}
				
		check = 1;		
        return check;
	}


	/**
	 * Will load the current list of users
	 * to cross check with entered infomation later on.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loadUsers();
	}
	
	/**
	 * Serializes the list of users, adding the newly
	 * added info to the list of users to serialize.
	 * Overrides file "userArrayList.ser" if it exists
	 * @param user
	 * @param password
	 */
	public void saveUserList(String user, String password){
	
		LoginInfo x = new LoginInfo(user, password);
		userArrayList.add(x);
		//x = new LoginInfo("admin", "admin");
		//userArrayList.add(0, x);
		try{
			FileOutputStream fileOut = new FileOutputStream("userArrayList.ser");
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(userArrayList);
			outStream.close();
			fileOut.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	
	/*try {
		writer = new FileWriter(file, false);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	bufferWriter = new BufferedWriter(writer);
	
	for (int i = 0; i < userList.size(); i++){
		System.out.println("writing data");
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

	/**
	 * Loads current list of usersl, if they exist. If "userArrayList.ser"
	 * file exists, will deserialize that and store as current list of users.
	 */
	public void loadUsers(){
		File temp = new File("userArrayList.ser");
		if (temp.exists()){
			try{
				FileInputStream fileIn =new FileInputStream("userArrayList.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				userArrayList = (ArrayList<LoginInfo>) in.readObject(); 
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
						x.add(data);
					}
				
					if (userPass == 1){
						//store passWord
						tempPass = data;
						y.add(data);
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
				userArrayList.add(user);
			}
			userData.close();
		}
	
		if(userArrayList.size() > 0 && userArrayList.get(0).getUser().equals("admin")){
			userArrayList.remove(0);
		}
	
	}
}
