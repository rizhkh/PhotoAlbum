package controller;


import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;

import javax.imageio.ImageIO;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser; 
import javafx.stage.Stage;

public class expandedViewController implements Initializable,Serializable {
 
	private Stage currentScene;
	private Scene newScene; 
	FXMLLoader loader;
	Stage stage;
	File file = new File("");
	 Image image;
	 static int currentSelectedIndex; // to store index of selected index
	 
	 String currentLoggedInUserdcurrentLoggedInUser = "user/"; // Used in building/mapping paths
	 String curDir = System.getProperty("user.dir"); // Used to get parent directory
	 String pathMake = curDir + "/" + currentLoggedInUserdcurrentLoggedInUser + "Album/"; // Used in building/mapping paths
	
	public static String strngFP; // String stores current path to the file
	public static ArrayList<String> pathxName = new ArrayList();  //Arraylist stores just the name of the list
	public static ArrayList<String> pathx = new ArrayList();  //Arraylist stores just the path of the list
	static String albumpath = "";
	
	@FXML Button adp;
	@FXML ListView photosList;
	@FXML private ImageView imgview; 
	albumViewController a = new albumViewController();
	 
	ObservableList<String> photosListObs = FXCollections.observableArrayList(pathx);
	
    //WHEN YOU PRESS THE ADD PHOTO BUTTON YOU GET AN ALERT
    // THIS METHOD WORKS WHEN YOU CLICK THE ADD PHOTO BUTTON
	
	//*** ADDS PHOTOS TO THE ALBUM AND STORES PHOTOS IN FOLDERS AND PATH IN THE DATASTRUCTURE
	@FXML public void add(ActionEvent event) throws IOException 
    {  
		albumpath = a.getPath();
		String loc = albumpath+"/path.ser";
		String locName = albumpath+"/pathName.ser";
		 
		//This Part serializes a arraylist that stores the img paths 
	
		
        stage = (Stage)adp.getScene().getWindow(); 
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a photo");
		List<File> l = fileChooser.showOpenMultipleDialog(stage);
		String filePath = l.toString();
		String abcd = "\\\\";
		filePath = filePath.replace("\\", abcd);
		filePath = filePath.replace("[", "");
		filePath = filePath.replace("]", "");
		strngFP = filePath;
		String StringFP2 = strngFP;
		//System.out.println("path: " + strngFP);
		file = new File(strngFP);
		//if(pathx!=null) {file = new File(strngFP);}
        image = new Image(file.toURI().toString());
        imgview.setImage(image);
		//imgview.setImage(new Image(strngFP)); 
		//pathx.add(filePath);
		
		int spot = 0;
		char abc = '\\';

		for(int i=0; i <filePath.length() ;i++) 
		{
			if(filePath.charAt(i)=='.')
			{
				spot = i ; 
				i=1000;
			}
		}
		int spot2=0;
		for(int i=filePath.length()-1; i>=0 ;i--) 
		{
			if(filePath.charAt(i)=='\\')
			{
				spot2 = i;
				i=-10;
			}
		}
		String result = "";
		for(int i=spot2+1; i<spot; i++)
		{
			result = result + filePath.charAt(i);
		}
		pathxName.add(result);
		//RESULT IS THE NAME OF THE FILE
		int type = 0;
		for(int i=0; i <filePath.length() ;i++) 
		{
			if(filePath.charAt(i)=='.')
			{
				type = i ; 
				i=1000;
			}
		}
		String photoExtension = "";
		for(int i=type+1; i<filePath.length(); i++)
		{
			photoExtension = photoExtension + filePath.charAt(i);
		}		

		
		
	photosList.getItems().add(result); 
	System.out.println("\n STRINGFP $$$$$$$$$$$$$$$$$$$$$$$ : " + StringFP2);
	File foldername = new File(StringFP2);
	BufferedImage img = ImageIO.read(foldername);
	String filename = albumpath + "/" + result + "." + photoExtension;
	filename = filename.replace("/", "\\");

	if(photoExtension.equals("jpg"))ImageIO.write(img, "jpg",new File(filename));
	if(photoExtension.equals("jpeg"))ImageIO.write(img, "jpeg",new File(filename));
	if(photoExtension.equals("png"))ImageIO.write(img, "png",new File(filename));
	if(photoExtension.equals("gif"))ImageIO.write(img, "gif",new File(filename));
	
	pathx.add(filename);

	serializingPaths(loc,locName);	
	
    }
	
	//*** SERIALIZES THE ADDED DATA TO STORING AND RESTORATION
	public void serializingPaths(String loc,String locName)
	{
		loc=loc.replace("/", "\\");
		System.out.println("zxccccccccccc " + loc);
		try
		  {
		   FileOutputStream fileOut = new FileOutputStream(loc);
		   FileOutputStream fileOut2 = new FileOutputStream(locName);
		   ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
		   ObjectOutputStream outStream2 = new ObjectOutputStream(fileOut2);
		   outStream.writeObject(pathx);
		   outStream2.writeObject(pathxName);
		   outStream.close();
		   outStream2.close(); 
		   fileOut.close();
		   fileOut2.close();
		  }catch(IOException i)
		  {
		   i.printStackTrace();
		  }		
	}	
	
	
	//*** DELETES THE SELECTED PHOTO FROM THE LIST AND FOLDER
	@FXML public void delete(ActionEvent event) 
    { 
		albumpath = a.getPath();
		String loc = albumpath+"/path.ser";
		String locName = albumpath+"/pathName.ser";
		
		int index = photosList.getSelectionModel().getSelectedIndex();
		
		System.out.println("&&&&&&&&&&" + pathx.size() + " index " + index);
		
		if(index==pathx.size()-1)
		{
			System.out.println("(index==pathx.size() && index!=0)");
			photosList.getSelectionModel().select(index-1);
			currentSelectedIndex = index-1;
			strngFP = pathx.get(currentSelectedIndex);			
			file = new File(strngFP);
	        image = new Image(file.toURI().toString());
	        imgview.setImage(image);
	        String imgPath = pathx.get(index);
	        pathx.remove(index);
	        pathxName.remove(index);
	        File f =new File(imgPath);
	        f.delete();
			photosListObs = FXCollections.observableArrayList(pathxName);	
			photosList.setItems(photosListObs); 			
		}
		
		else
		{
	        String imgPath = pathx.get(index);
	        pathx.remove(index);
	        pathxName.remove(index);
	        File f =new File(imgPath);
	        f.delete();			
			photosList.getSelectionModel().select(index);
			autoselectt();
			photosListObs = FXCollections.observableArrayList(pathxName);
			photosList.setItems(photosListObs);	
			photosList.getSelectionModel().select(index);
		}  
		
		serializingPaths(loc,locName);	
    }
	
	 
	//*** COPIES THE SELECTED PHOTO FROM ONE ALBUM TO ANOTHER ALBUM
	@FXML public void copyImage(ActionEvent event) throws IOException 
    { 
		System.out.println("in copyimage");
		albumViewController a = new albumViewController();
		//ArrayList<String> pathxName
		ArrayList<String> choices = new ArrayList<>();
		choices = a.albumnameList;
		System.out.println(choices);
		String path = "";

		if(!choices.isEmpty())
		{
			ChoiceDialog<String> dialog = new ChoiceDialog<>("b", choices);
			dialog.setTitle("Copy Image to another album");
			dialog.setHeaderText("Copy Image");
			dialog.setContentText("Choose the album you wish to copy the image to:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
			    System.out.println("Options: " + result.get());
			}

			// The Java 8 way to get the response value (with lambda expression).
			String letter2 =result.get();
			path = a.savephotoNamePath + letter2 + "/";
			System.out.println("Moving this image to folder :" + path);
			addCM(path,result.get());
		}

    }	

	//*** HELPED METHOD FOR MOVING AND COPYING PHOTOS TO OTHER ALBUMS
	public void addCM(String path,String name) throws IOException 
    {  

		path = path.replace("/", "\\");
		System.out.println("Inside addCM() $$ path : " + path + " ");
		//path = path.replace("/", "\\");
		String photoExtension = "";
		String photoPath = strngFP; 
		System.out.println("photoPath : " + photoPath);
		

		int type = 0;
		for(int i=0; i <photoPath.length() ;i++) 
		{
			if(photoPath.charAt(i)=='.')
			{
				type = i ; 
				i=1000;
			}
		}

		for(int i=type+1; i<photoPath.length(); i++)
		{
			photoExtension = photoExtension + photoPath.charAt(i);
		}			

		String filename = path + "\\" + name + "." + photoExtension;
		filename = filename.replace("/", "\\");


		System.out.println("photoPath (copyImage) : " + photoPath);
		System.out.println("filename (copyImage) : " + filename);		
		
		File foldername = new File(photoPath);
		BufferedImage img = ImageIO.read(foldername);

		System.out.println("photoPath (copyImage) : " + photoPath);
		System.out.println("filename (copyImage) : " + filename);	
		
		if(photoExtension.equals("jpg"))ImageIO.write(img, "jpg",new File(filename));
		if(photoExtension.equals("jpeg"))ImageIO.write(img, "jpeg",new File(filename));
		if(photoExtension.equals("png"))ImageIO.write(img, "png",new File(filename));
		if(photoExtension.equals("gif"))ImageIO.write(img, "gif",new File(filename));

    }	
	
	//*** MOVES THE SELECTED PHOTO FROM ONE ALBUM TO ANOTHER ALBUM 
	@FXML public void moveImage(ActionEvent event) throws IOException 
    { 
		albumViewController a = new albumViewController();
		//ArrayList<String> pathxName
		ArrayList<String> choices = new ArrayList<>();
		choices = a.albumnameList;
		System.out.println(choices);
		String path = "";

		if(!choices.isEmpty())
		{
			ChoiceDialog<String> dialog = new ChoiceDialog<>("b", choices);
			dialog.setTitle("Copy Image to another album");
			dialog.setHeaderText("Copy Image");
			dialog.setContentText("Choose the album you wish to copy the image to:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
			    System.out.println("Options: " + result.get());
			}

			// The Java 8 way to get the response value (with lambda expression).
			String letter2 =result.get();
			path = a.savephotoNamePath + letter2 + "/";
			System.out.println("Moving this image to folder :" + path);
			addCM(path,result.get());
			System.out.println("DONE ");
			int ind = pathx.indexOf(strngFP);
	        File f =new File(strngFP);
	        f.delete();
			pathx.remove(ind); 
			String abc = typeExt(strngFP);
			if(pathxName.contains(abc))
			{
				System.out.println("YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
				int aa = pathxName.indexOf(abc);
				pathxName.remove(aa);
			}
			//pathxName.remove(ind);
			photosListObs = FXCollections.observableArrayList(pathxName);	
			photosList.setItems(photosListObs);			
		}		

    }	
	

	//*** Moves to the next selection in the list
	@FXML public void next(ActionEvent event) 
    { 
		if(currentSelectedIndex<pathx.size())
			{
			photosList.getSelectionModel().select(currentSelectedIndex+1);
			//photosList.getSelectionModel().getSelectedIndex();
			autoselectt();
			photosList.getSelectionModel().select(currentSelectedIndex+1);
			//photosListObs = FXCollections.observableArrayList(pathxName);	
	  		//photosList.setItems(photosListObs); 
			}
    }
	
	
	//*** Moves to the previous selection in the list
	@FXML public void prev(ActionEvent event) 
    { 
		if(currentSelectedIndex>0)
			{photosList.getSelectionModel().select(currentSelectedIndex-1);
			autoselectt();
			photosList.getSelectionModel().select(currentSelectedIndex-1);
			//photosListObs = FXCollections.observableArrayList(pathxName);
	  		//photosList.setItems(photosListObs); 
			}
    }
	
	@FXML public void search(ActionEvent event) 
    { 
		
    }
	
	//This method is just for autoselection of items in listview and preview
	//When you hit next or prev it selects and shows it
	static int curIndex = 0;
	
	//***helper method to obtain paths for the photos selection in the list
	public void autoselectt()
	{
		int index = photosList.getSelectionModel().getSelectedIndex();
		curIndex = index;
		photosList.getSelectionModel().select(index);/////////////////////
		currentSelectedIndex = index;
		strngFP = pathx.get(index);
		System.out.println("path: " + strngFP);
		file = new File(strngFP);
        image = new Image(file.toURI().toString());
        imgview.setImage(image);	
		photosListObs = FXCollections.observableArrayList(pathxName);	
		photosList.setItems(photosListObs);   
	}
	
	
	//THIS IS WHEN YOU CLICK ON A LIST ITEM IN LISTVIEW
	//*** Helps select the correct path of the photo of the selected photo in the list
	@FXML
	public void mousephotosList(MouseEvent event) throws IOException
	{
		int index = photosList.getSelectionModel().getSelectedIndex();
		currentSelectedIndex = index;
		strngFP = pathx.get(index);

		System.out.println("path: " + strngFP);
		file = new File(strngFP);

        image = new Image(file.toURI().toString());
        imgview.setImage(image);

		
		photosListObs = FXCollections.observableArrayList(pathx);
		if( event.getSource() == photosList )
		{
			index = photosList.getSelectionModel().getSelectedIndex();
			if(index<0)return;
		} 
	}

	//*** GOES BACK TO THE ALBUM VIEW PAGE
	@FXML public void albumView(ActionEvent event) throws IOException 
    { 
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/albumView.FXML"));
		
		currentScene = (Stage) b.getScene().getWindow();
		AnchorPane root = (AnchorPane)loader.load();
		newScene = new Scene(root,600,400);
		newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
		currentScene.setScene(newScene);
		currentScene.show();	
    }	
	
	//*** Moves to DISPLAY IMAGE SCENE TO DISPLAY IMAGE
	@FXML public void displayImage(ActionEvent event) throws IOException 
    { 
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/imageView.FXML"));
		currentScene = (Stage) b.getScene().getWindow();
		AnchorPane root = (AnchorPane)loader.load();
		newScene = new Scene(root,600,400);
		newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
		currentScene.setScene(newScene);
		currentScene.show();		
    }	
	
	//*** HELPER METHOD TO STORE PATHS OF THE PHOTOS
	public String typeExt(String filePath)
	{
		int spot = 0;
		char abc = '\\';

		for(int i=0; i <filePath.length() ;i++) 
		{
			if(filePath.charAt(i)=='.')
			{
				spot = i ; 
				i=1000;
			}
		}
		int spot2=0;
		for(int i=filePath.length()-1; i>=0 ;i--) 
		{
			if(filePath.charAt(i)=='\\')
			{
				spot2 = i;
				i=-10;
			}
		}
		String result = "";
		for(int i=spot2+1; i<spot; i++)
		{
			result = result + filePath.charAt(i);
		}
		return result;
	}	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		albumpath = a.getPath();
		String loc = albumpath+"/path.ser";
		String pp = albumpath;
		String locName = albumpath+"/pathName.ser";
		loc = loc.replace("/", "\\");
		File aaa = new File(loc);
		File p1 = new File(pp);
		System.out.println("initialize of expandedView :" + loc);
		if(aaa.exists())
		{
			System.out.println("exists &&&" + loc);
			if(p1.list().length<=0)
			{
				System.out.println("exists bt no file" + loc);
				File a1 = new File(loc);
				File a2 = new File(locName);
				a1.delete();
				a2.delete();
				pathx.clear();
				pathxName.clear();
				
			}
			
			
			if(p1.list().length>0)
			{
				System.out.println("\nexists" + loc);
				try
			       {
			          FileInputStream fileIn =new FileInputStream(loc);
			          FileInputStream fileIn2 =new FileInputStream(locName);
			          ObjectInputStream in = new ObjectInputStream(fileIn);
			          ObjectInputStream in2 = new ObjectInputStream(fileIn2);
			          pathx = (ArrayList<String>) in.readObject();
			          pathxName = (ArrayList<String>) in2.readObject(); 	       
			          System.out.println(pathx);
			          System.out.println(pathxName);
			          
			          ///////////////////////////
			          System.out.println( "arraylist : " + pathxName);
			          File folder = new File(albumpath);
			          File[] listOfFiles = folder.listFiles();
			          for (File file : listOfFiles) {
			              if (file.isFile()) {
			                  System.out.println( "currently reading file : " +file.getName());
			                  if(!pathx.contains(file.getName()))
			                  {
			                	  if(!pathxName.contains(file.getName()))// && (!(file.getName().equals("path.ser")) || !file.getName().equals("pathName.ser")))
			                	  {
			                		  
			                		  if(file.getName().equals("path.ser") || file.getName().equals("pathName.ser"))
				                	  {
				                		  
				                	  }
			                		  
			                		  else
			                		  {
			                			if(!pathx.contains(file.getAbsolutePath()))
			                			{
					                		  System.out.println( "yes this filename does not exists: " + file.getName());
					                		  System.out.println( "file that is about to be added to arraylist: " +file.getName());
						                	  String abc = albumpath + "/"+file.getName();
							                  pathx.add(abc);
							                  String name = typeExt(file.getName());
							                  pathxName.add(name);			                				
			                			}
			                		  }
			                	  }
			                	  
			                	  else
			                	  {
			                		  System.out.println( "file that is about to be added to arraylist: " +file.getName());
				                	  String abc = albumpath + "/"+file.getName();
					                  pathx.add(abc);
					                  pathxName.add(file.getName());
			                	  }
			                  }
			              }
			          }
			          System.out.println( "arraylist : " + pathxName);
			          //////////////////////////////////////
			          photosListObs = FXCollections.observableArrayList(pathxName);	
			  		photosList.setItems(photosListObs);   
			          in.close();
			          in2.close();
			          fileIn.close();
			          fileIn2.close();
			          
			  		photosList.getSelectionModel().select(0);
					String abc = pathx.get(0);
					System.out.println("starting : " + abc);
					//This will be replaced by the pic on the very top
					file = new File(abc);
					//if(pathx!=null) {file = new File(strngFP);}
			        image = new Image(file.toURI().toString());
			        imgview.setImage(image);
			          
			       }catch(IOException i)
			       {
			          i.printStackTrace();
			          return;
			       } catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}				
		}
		else
		{
			 ///////////////////////+////
			String lock = albumpath + "/";
			System.out.println("ELSEE INITLAL");
	          File folder = new File(lock);
	          System.out.println(lock);
	          System.out.println("hello ");
	          if(folder.list().length>0)
	          { 
		          File[] listOfFiles = folder.listFiles();
		          System.out.println("hello ");
		          for (File file : listOfFiles) {
		        	  System.out.println("hello ");
		              if (file.isFile()) {
		                  System.out.println(file.getName());
		                  if(!pathx.contains(file.getName()))
		                  {
		                	  
		                	  if(file.getName().equals("path.ser") || file.getName().equals("pathName.ser"))
		                	  {}
		                	  
		                	  else
		                	  {
			                	  String abc = albumpath + "/"+file.getName();
				                  pathx.add(abc);
				                  pathxName.add(file.getName());
		                	  }
		                  }
		              }
		          }		
		          System.out.println("hello 3");
		          photosListObs = FXCollections.observableArrayList(pathxName);	
			  		photosList.setItems(photosListObs);
			  		 System.out.println("hello 5");
			  		photosList.getSelectionModel().select(0);
					String abc = pathx.get(0);
					System.out.println("starting : " + abc);
					//This will be replaced by the pic on the very top
					file = new File(abc);
					//if(pathx!=null) {file = new File(strngFP);}
			        image = new Image(file.toURI().toString());
			        imgview.setImage(image); 
	          }
	          System.out.println("ELSEE INITLAL");
	          //////////////////////////////////////
		}

		photosList.getSelectionModel().select(0);
		//String abc = pathxName.get(0);

	}	
	
	//*** LOGS OUT
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
