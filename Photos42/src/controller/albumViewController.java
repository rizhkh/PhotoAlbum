package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL; 
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import controller.AdminController.loginInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class albumViewController implements Initializable,Serializable {

	private Stage currentScene;
	private Scene newScene;
	FXMLLoader loader;
	Stage stage;

	
	static String currentSelectedFolderPath = "";  //to retreive path
	static int currentSelectedIndex;	//to retreive index of the path
	static String currentPathForDeletion; //to retreive path for selected list to be deleted
	
	private static String userName = null; //to get current username
	
	public static String strngFP; //Currently selected Path to the file/album
	static ArrayList<String> albumnameList = new ArrayList<String>(); 		//Arraylist stores just the name of the list
	static ArrayList<String> albumnameListPath = new ArrayList<String>(); //Arraylist Stores path - both share same index if one is deleted make sure the other is too
	
	@FXML Button adp;
	@FXML ListView albumList; 
	@FXML private ImageView imgview; 
	
	String serialPathForInitPath = ""; //Path(string) to pass for serialization
	String serialPathForInitName = "";//Path(string) to pass for serialization
	static String currentAlbum = ""; //Path(string) for the currently selected album

	String serFolderLoc = System.getProperty("user.dir"); // gets parent folder to map a path
	String serFolderLoc2 = System.getProperty("user.dir");// gets parent folder to map a path
	public static String savephotoName = ""; // String to save album name for re-usability
	public static String savephotoNamePath = "";// String to save album path for re-usability
	
	
	ObservableList<String> albumsListObs = FXCollections.observableArrayList(albumnameList);

	//*** CREATES NEW ALBUM AND DISPLAYS IN LIST
	
	@FXML public void createAlbum(ActionEvent event) throws IOException 
	{
		System.out.println(getUserName());
		int check=0;
		String res = "";
		System.out.println(" <ALBUM VIEW> ");
		String currentUserName = "Album";	// <---------------------------		
		
		
		String currentpath = System.getProperty("user.dir");
		System.out.println("current path is:" + currentpath);
		currentpath = currentpath + "\\";
		currentpath = currentpath.replace("\\", "/");
		
		
		String strDirectoy = "CreatedUsers/"+ getUserName() + "/Album";///////// <---**************->
		serFolderLoc = currentpath + strDirectoy + "/";

		System.out.println("%%%%%%%%%%%%%%%%%%%%% " + serFolderLoc);
		
		//POP UP THAT ASKS FOR ALBUM NAME
		TextInputDialog dialog = new TextInputDialog("Enter Album name");
		dialog.setTitle("Album name");
		dialog.setHeaderText("Album name");
		dialog.setContentText("Please enter album name:");
		String albumNamePath = "";
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    albumNamePath = result.get();   
		}		

		//strDirectoy =strDirectoy + "/" + albumNamePath;
		//currentpath = currentpath + strDirectoy;
		String sss = serFolderLoc;
		serFolderLoc = serFolderLoc + albumNamePath;
		currentAlbum = serFolderLoc;
		
		serFolderLoc = serFolderLoc.replace("/", "\\");
		System.out.println("current path is $$ " + serFolderLoc);
		
		//serFolderLoc	
		File pathOfDirectory = new File(serFolderLoc);
		boolean pathDirExist = pathOfDirectory.exists();

		System.out.println("serFolderLoc path is $$ " + serFolderLoc);
		System.out.println("currentpath path is $$ " + currentpath);	
		
		if(pathDirExist)
		{ 
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Duplicate found");
			alert.setContentText("Album already exists with the same name!");
			alert.showAndWait();	
		}else {
			
			Files.createDirectories(Paths.get(serFolderLoc));
			albumnameListPath.add(serFolderLoc); //<-- Path gets added here
			currentPathForDeletion = serFolderLoc;
			res = nameMaker(currentAlbum);
			albumnameList.add(res); //<---- adds name in arraylist
			albumList.getItems().add(res); //<-- adds name in list view
			albumsListObs = FXCollections.observableArrayList(albumnameList);
			
			String AlbumListser = sss + "/" + "AlbumList.ser";
			String AlbumListNameser = sss + "/" + "AlbumListName.ser";
			serialPathForInitName = AlbumListNameser;
			serialPathForInitPath = AlbumListser;
			currentpath = currentpath + "/";
			serializingPaths(AlbumListser,AlbumListNameser);
			//albumnameList.add(res);
		}
	}
	
	//*** TO SERIALIZE THE PATHS OF THE CREATED ALBUMS
	public void serializingPaths(String loc,String locName)
	{
		try{
		   FileOutputStream fileOut = new FileOutputStream(loc);
		   FileOutputStream fileOut2 = new FileOutputStream(locName);
		   ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
		   ObjectOutputStream outStream2 = new ObjectOutputStream(fileOut2);
		   outStream.writeObject(albumnameListPath);
		   outStream2.writeObject(albumnameList);
		   outStream.close();
		   outStream2.close();
		   fileOut.close();
		   fileOut2.close();
		  }catch(IOException i){
		   i.printStackTrace();
		  }		
	}	
	
	//*** HELPER METHOD THAT RETURNS THE PATH OF THE ALBUM
	//This method returns the path of the album/folder 
	public String getAlbumPath(String z){
		String pathh = "";
		int index = albumnameList.indexOf(z);
		pathh = albumnameListPath.get(index);
		return pathh;
	}

	//*** HELPER METHOD FOR ALBUM CREATION - RETURNS NAMES
	public String nameMaker(String filename){
		int spot = 0;
		for(int i=0; i <filename.length() ;i++) {
			if(i==filename.length()-1){
				spot = i ; 
				i=1000;
			}
		}
		int spot2=0;
		
		for(int i=filename.length()-1; i>=0 ;i--) {
			if(filename.charAt(i)=='/'){
				spot2 = i;
				i=-10;
			}
		}
		
		String result = "";
		for(int i=spot2+1; i<=spot; i++){
			result = result + filename.charAt(i);
		} 
		//albumList.getItems().add(result);
		
		return result;
	}

	//unserializes data to update from other folders

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LoginController a = new LoginController();
		System.out.println("Current User: " + a.useruser);
		String loc =serFolderLoc2 + "/CreatedUsers" + "/"+ a.useruser + "" + "/Album" + "/AlbumList.ser";//serFolderLoc + "\\path.ser";
		String locName = serFolderLoc2 + "/CreatedUsers" + "/"+ a.useruser + "" + "/Album" + "/AlbumListName.ser" ;//serFolderLoc+"\\pathName.ser";
		
		savephotoNamePath = serFolderLoc2 + "/CreatedUsers" + "/"+ a.useruser + "" + "/Album/";
		
		loc = loc.replace("/", "\\");
		locName = locName.replace("/", "\\");
		
		System.out.println(loc);
		File aaa = new File(loc);
		if(aaa.exists()){
			System.out.println("exists ^^^^^^^^^^^^^^^^^^^^^^^" + loc);
			try{
		          FileInputStream fileIn =new FileInputStream(loc);
		          FileInputStream fileIn2 =new FileInputStream(locName);
		          ObjectInputStream in = new ObjectInputStream(fileIn);
		          ObjectInputStream in2 = new ObjectInputStream(fileIn2);
		          albumnameListPath = (ArrayList<String>) in.readObject();
		          albumnameList = (ArrayList<String>) in2.readObject(); 
		          System.out.println(albumnameListPath);
		          System.out.println(albumnameList);
		          albumsListObs = FXCollections.observableArrayList(albumnameList);	
		          albumList.setItems(albumsListObs);   
		          in.close();
		          in2.close();
		          fileIn.close();
		          fileIn2.close();
		       }catch(IOException i){
		          i.printStackTrace();
		          return;
		       }catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	

		//System.out.println(albumnameList);
		if(!albumnameList.isEmpty()){
			albumsListObs = FXCollections.observableArrayList(albumnameList);
			albumList.getSelectionModel().select(0);
			//albumList.getSelectionModel().select(0);
			int index = albumList.getSelectionModel().getSelectedIndex();
			currentSelectedIndex = index;
			strngFP = albumnameList.get(index);
			currentSelectedFolderPath = albumnameListPath.get(index);	
			}				  
	} 
 
	//*** DELETES SELECTED FOLDER AND THINGS INSIDE IT
	@FXML public void delete(ActionEvent event) throws IOException { 
		int index = albumList.getSelectionModel().getSelectedIndex();
		System.out.println("Index of album " + index);
		int pathfordel = index;
		String curFile = albumnameListPath.get(pathfordel);
		String curFileName = albumnameList.get(pathfordel);
		System.out.println("File: " + curFile );
		File cp = new File(curFile);//=  file.getParentFile();
		System.out.println(cp);
		  
		String rem = curFile + ";" + curFileName + ";";
		if(cp.isDirectory() && cp.list().length == 0 ) {
		   System.out.println("Directory is empty");
		   cp.delete();
		   System.out.println("Directory deleted!");
		   albumnameList.remove(pathfordel);
		   albumnameListPath.remove(pathfordel);
		   albumList.getItems().remove(pathfordel);
		   albumsListObs = FXCollections.observableArrayList(albumnameList); 
		} else {
			 System.out.println("Directory is not empty");
			 File[] listFiles = cp.listFiles();
				for(File file : listFiles){
					System.out.println("Deleting "+file.getName());
					file.delete();}
					cp.delete();
					albumnameList.remove(pathfordel);
					albumnameListPath.remove(pathfordel);
				    albumList.getItems().remove(pathfordel);
					albumsListObs = FXCollections.observableArrayList(albumnameList);
		}  
		serializingPaths("AlbumList.ser","AlbumListName.ser");
    }	

	//*** RENAMES ALBUM
	@FXML public void renameAlbum(ActionEvent event) { 
		//POP UP THAT ASKS FOR ALBUM NAME
		TextInputDialog dialog = new TextInputDialog("Enter a new Album name");
		dialog.setTitle("Album name");
		dialog.setHeaderText("Album name");
		dialog.setContentText("Please enter a new album name:");
		String albumNamePath = "";
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    albumNamePath = result.get();   
		}
		String newN1 = currentSelectedFolderPath + "/" + albumNamePath;
		System.out.println("  Sadd " + currentSelectedFolderPath);
		File dir = new File(currentSelectedFolderPath);
        File newName = new File(newN1);
        if ( dir.isDirectory() ) {
        	System.out.println("yes");
                dir.renameTo(newName); 
        }
        
        int index = albumList.getSelectionModel().getSelectedIndex();
        albumnameList.add(albumNamePath);
        albumnameList.remove(albumNamePath);
        albumnameListPath.add(newN1);
        albumnameListPath.remove(currentSelectedFolderPath);
        autoselectt();
		albumsListObs = FXCollections.observableArrayList(albumnameList);
		serializingPaths("AlbumList.ser","AlbumListName.ser");	
    }

	//*** SELECTS NEXT ALBUM
	@FXML public void next(ActionEvent event) { 
		if(currentSelectedIndex<albumnameList.size()){
			albumList.getSelectionModel().select(currentSelectedIndex+1);
			autoselectt();
			albumsListObs = FXCollections.observableArrayList(albumnameList);
			}
    }
	
	//*** SELECTS PREVIOUS ALBUM
	@FXML public void prev(ActionEvent event) { 
		if(currentSelectedIndex>0){
			albumList.getSelectionModel().select(currentSelectedIndex-1);
			autoselectt();
			albumsListObs = FXCollections.observableArrayList(albumnameList);
			}
    }	

	//*** HELPER METHOD FOR SELECTION
	public void autoselectt(){
		int index = albumList.getSelectionModel().getSelectedIndex();
		currentSelectedFolderPath = albumnameListPath.get(index); 		
		albumList.getSelectionModel().select(index);/////////////////////
		currentSelectedIndex = index;
        albumsListObs = FXCollections.observableArrayList(albumnameList);	
        albumList.setItems(albumsListObs);   
	} 

	//*** HELPER METHOD TO GET THE CURRENTLY SELECTED PATH OF ALBUM
	public static String getPath(){
		return currentSelectedFolderPath;
	}
	
	
	//***RETRIEVE INFO OF THE SELECTED ALBUM FROM THE LSIT
	//THIS IS WHEN YOU CLICK ON A LIST ITEM IN LISTVIEW
	@FXML
	public void mouseAlbumList(MouseEvent event) throws IOException{
		int index = albumList.getSelectionModel().getSelectedIndex();
		currentSelectedIndex = index;
		strngFP = albumnameList.get(index);
		currentSelectedFolderPath = albumnameListPath.get(index); 
		System.out.println("path: " + strngFP);

        albumsListObs = FXCollections.observableArrayList(albumnameList);
		if( event.getSource() == albumList ){
			index = albumList.getSelectionModel().getSelectedIndex();
			if(index<0)return;
		}
	}
	
	//*** THIS SETS THE STAGE TO A NEW SCENE
	@FXML public void exploreAlbum(ActionEvent event) throws IOException { 
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
	
	//returns who is the current user for this particular albumView
	public String getUserName(){
		return this.userName;
	}
	
	//receives the current user from the LoginController class
	public void setUserName(String user){
		System.out.println("In here");
		this.userName = user;
	}
	
	//***LOGS OUT
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
	
} 
