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
import java.util.Calendar;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;

import javax.imageio.ImageIO;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser; 
import javafx.stage.Stage;

/**
 * Displays an album with all the photos in it. 
 * @author Rizwan Khan(mrk150) && Ahmed Ghoneim(asg179)
 *
 */
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
	static String curPathDetail = "";
	 
	public static String strngFP; // String stores current path to the file
	public static ArrayList<String> pathxName = new ArrayList();  //Arraylist stores just the name of the list
	public static ArrayList<String> pathx = new ArrayList();  //Arraylist stores just the path of the list
	public static ArrayList<String> pathxDate = new ArrayList(); 
	static String albumpath = "";

	
	/**
	 * Button to add photos to album
	 */
	@FXML Button adp;
	/**
	 * List to display all photos in an album
	 */
	@FXML ListView photosList;
	/**
	 * List to display details of the selected photo
	 */
	@FXML ListView photosListDetail;
	@FXML private ImageView imgview; 
	albumViewController a = new albumViewController();
	 
	ObservableList<String> photosListObs = FXCollections.observableArrayList(pathx);

	
	public static ArrayList<String> details = new ArrayList();
	ObservableList<String> photosListObsDetail = FXCollections.observableArrayList("Name: " , "Caption :" , "Tags: ");	
	String[][] captionInp = new String[100][2];//[INDEXES][DATA]
	public static String[][][] tags = new String[100][3][2];//[INDEXES][TYPE][DATA]
	public static int photoIndex = 0;
	//TAGS - 0 IS FOR PERSON 1 IS LOCATION 2 IS OTHER
	String tagComp = "";


	
    //WHEN YOU PRESS THE ADD PHOTO BUTTON YOU GET AN ALERT
    // THIS METHOD WORKS WHEN YOU CLICK THE ADD PHOTO BUTTON
	/**
	 * Displays a dialog in order to add any details to photos added. Pop-up displays
	 * automatically when a photo is added.
	 * @param event Pushing "Add Photo" Button
	 * @throws IOException
	 */
	@FXML public void addCaption(ActionEvent event) throws IOException 
	{
		String c = "";
		//albumViewController a = new albumViewController();
		TextInputDialog dialog = new TextInputDialog("Enter Caption");
		dialog.setTitle("Caption");
		dialog.setHeaderText("Caption");
		dialog.setContentText("Please enter a caption for the photo:");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			c = result.get();   
		}	
	
		int index = 0;
		if(!strngFP.equals("null"))
		index = pathx.indexOf(strngFP);
		
		else			
		{index = pathx.indexOf(curPathDetail);}
		String name = pathxName.get(index);

		captionInp[index][0]=name;
		captionInp[index][1]=c;

		serializingPathsForDetailOptions(a.getPath(),"\\details.ser");
		photosListObsDetail = FXCollections.observableArrayList("Name: " + pathxName.get(index), "Caption :" + captionInp[index][1] , "Tags: " + tags[index][0][1]
				, "Date: " + pathxDate.get(index)
				);	
		photosListDetail.setItems(photosListObsDetail);	
	}
	
	/**
	 * Adds tags to the photo via a dialog
	 * @param event Pushing "Add tags" Button
	 * @throws IOException
	 */
	@FXML public void addTags(ActionEvent event) throws IOException 
	{
		List<String> choices = new ArrayList<>();
		choices.add("Person");
		choices.add("Location");
		choices.add("Other");

		ChoiceDialog<String> dialog = new ChoiceDialog<>("Tag choices", choices);
		dialog.setTitle("Tag selection");
		dialog.setHeaderText("TAG");
		dialog.setContentText("Choose a Tag");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    ////ln("Your choice: " + result.get());
		}
		int choice = 0;
		String option =  result.get();
		if(option.equals("Person"))choice = 0;
		if(option.equals("Location"))choice = 1;
		if(option.equals("Other"))choice = 2;
		
		TextInputDialog dd = new TextInputDialog("Enter a tag value");
		dd.setTitle("Share your tag");
		dd.setHeaderText("TAG");
		dd.setContentText("Please enter your tag value:");

		// Traditional way to get the response value.
		Optional<String> result2 = dd.showAndWait();
		if (result2.isPresent())
		{
		    ////ln("Your name: " + result2.get());
		    }
		    String response = result2.get();
		    
		    //String[][][] tags = new String[100][3][1]
			int index = 0;
		    if(!strngFP.equals("null"))
		    index = pathx.indexOf(strngFP);
			
			else			
			{index = pathx.indexOf(curPathDetail);}
			String name = pathxName.get(index);

			
			String tagComplete = "";
			tagComplete = tags[index][0][1] + " ";
			
			
			if(choice==0)
			{
				tags[index][0][0]= "(Person:" + response + ") ";	
				tagComplete = tagComplete + "" + tags[index][0][0];
				if(!tags[index][0][1].equals("null"))tags[index][0][1] = tags[index][0][0] + " " + tags[index][0][1];
				else {tags[index][0][1] =tags[index][0][0];}
			}

			if(choice==1)
			{
				tags[index][1][0]= "(Location:" + response + ")";	
				tagComplete = tagComplete + "" + tags[index][0][0];
				if(!tags[index][0][1].equals("null"))tags[index][0][1] = tags[index][1][0] + " " + tags[index][0][1];
				else {tags[index][0][1] =tags[index][1][0];}
			}	

			if(choice==2)
			{
				tags[index][2][0]= "(Other:" + response + ")";
				tagComplete = tagComplete + "" + tags[index][0][0];
				if(!tags[index][0][1].equals("null"))tags[index][0][1] = tags[index][2][0] + " " + tags[index][0][1];
				else {tags[index][0][1] =tags[index][2][0];}
			}	
			
			String fileend = "\\Captions.ser";
			//tagComp = tagComplete;
			//serializingPathsForDetailOptions(a.getPath(),fileend);	
			photosListObsDetail = FXCollections.observableArrayList("Name: " + pathxName.get(index), "Caption :" + captionInp[index][1] , "Tags: " + tags[index][0][1]
					, "Date: " + pathxDate.get(index)
					);	
			photosListDetail.setItems(photosListObsDetail);					
				albumViewController a = new albumViewController();
				String albumPathway=a.getPath() + fileend;
				capSerDetailFile = albumPathway;


				albumPathway=albumPathway.replace("/", "\\");
				try
				  {
				   FileOutputStream fileOut = new FileOutputStream(albumPathway);
				   ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
				   outStream.writeObject(tags);
				   outStream.close();
				   fileOut.close();
				  }catch(IOException i)
				  {
				   i.printStackTrace();
				  }	
	}	

	/**
	* Special serialization for the tags of photos
	* @param albumPathway The path of the album storing the photo
	* @param fileend The end of the file
	*/
	public void serializingTags(String albumPathway,String fileend)
	{
		capSerDetailFile = albumPathway;
		//ln("album path is : " + albumPathway);

		albumPathway=albumPathway.replace("/", "\\");
		try
		  {
		   FileOutputStream fileOut = new FileOutputStream(albumPathway);
		   ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
		   outStream.writeObject(tags);
		   outStream.close();
		   fileOut.close();
		  }catch(IOException i)
		  {
		   i.printStackTrace();
		  }		
	}
	
	/**
	 * Edits the info of a photo
	 * @param event Pushing "Edit info" Button
	 * @throws IOException
	 */
	@FXML public void editInfo(ActionEvent event) throws IOException 
	{
		
		LoginController a = new LoginController();
		a.useruser = "";
		Button b = (Button)event.getSource();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/editPhoto.FXML"));
		
		currentScene = (Stage) b.getScene().getWindow();
		AnchorPane root = (AnchorPane)loader.load();
		newScene = new Scene(root,600,400);
		newScene.getStylesheets().add(getClass().getResource("/app/application.css").toExternalForm());
		currentScene.setScene(newScene);
		currentScene.show();	
		
		
	}
	//THIS IS WHEN YOU CLICK ON A LIST ITEM IN LISTVIEW
	//*** Helps select the correct path of the photo of the selected photo in the list
	@FXML
	public void mousephotosListDetail(MouseEvent event) throws IOException
	{
		
	
		//photosListObsDetail = FXCollections.observableArrayList("Name: " , "Caption :" , "Tags: ");	
		//photosListDetail.setItems(photosListObsDetail);

	}

	//*** SERIALIZES THE ADDED DATA TO STORING AND RESTORATION
	String capSerDetailFile = "";
	/**
	 * Serializes the path name of the added photos to the album for stable storage
	 * @param loc The path of the photo added
	 * @param fileends The end of the file containing the album
	 */
	public void serializingPathsForDetailOptions(String loc,String fileends)
	{
		albumViewController a = new albumViewController();
		String albumPathway=loc + fileends;
		capSerDetailFile = albumPathway;

		albumPathway=albumPathway.replace("/", "\\");
		try
		  {
		   FileOutputStream fileOut = new FileOutputStream(albumPathway);
		   ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
		   outStream.writeObject(captionInp);
		   outStream.close();
		   fileOut.close();
		  }catch(IOException i)
		  {
		   i.printStackTrace();
		  }	
	}	
	
	//*** ADDS PHOTOS TO THE ALBUM AND STORES PHOTOS IN FOLDERS AND PATH IN THE DATASTRUCTURE
	/**
	 * Displays a file searcher to choose any image to add it to the album.
	 * Stores its path name in order to serialize it later.
	 * @param event
	 * @throws IOException
	 */
	@FXML public void add(ActionEvent event) throws IOException 
    {  
		albumpath = a.getPath();
		String loc = albumpath+"/path.ser";
		String dateloc = albumpath+"/date.ser";
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
		////ln("path: " + strngFP);
		file = new File(strngFP);
		//if(pathx!=null) {file = new File(strngFP);}
        image = new Image(file.toURI().toString());
        imgview.setImage(image);
		//imgview.setImage(new Image(strngFP)); 
		//pathx.add(filePath);
        int check = 0;

		
        
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

		
		
	//photosList.getItems().add(result); 

	File foldername = new File(StringFP2);
	BufferedImage img = ImageIO.read(foldername);
	String filename = albumpath + "/" + result + "." + photoExtension;
	filename = filename.replace("/", "\\");
	if(pathx.contains(filename))
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Duplicate found");
		alert.setContentText("Image already exists with the same name!");
		alert.showAndWait();
		int id = pathxName.indexOf(result);
		 pathxName.remove(id);
		 //photosList.getItems().add(result); 

	}
	else
	{
		photosList.getItems().add(result); 
		if(photoExtension.equals("jpg"))ImageIO.write(img, "jpg",new File(filename));
		if(photoExtension.equals("jpeg"))ImageIO.write(img, "jpeg",new File(filename));
		if(photoExtension.equals("png"))ImageIO.write(img, "png",new File(filename));
		if(photoExtension.equals("gif"))ImageIO.write(img, "gif",new File(filename));
		
		pathx.add(filename);
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		String ddate = hour + ":" + minute;
		String time =  " [" + day + "/"+ month + "/"+ year + " - " + ddate + "]";
		pathxDate.add(time);

		serializingDate(dateloc);
		serializingPaths(loc,locName);
		
		int index = pathxName.indexOf(result);
		photosListObsDetail = FXCollections.observableArrayList("Name: " + pathxName.get(index), "Caption :" + captionInp[index][1] , "Tags: "
				, "Date: " + time  
				);	
		photosListDetail.setItems(photosListObsDetail);	
				
	}

    }
	
	/**
	 * Serializes the added photos' and albums' dates
	 * @param f The path of the photo
	 */
	public void serializingDate(String f)
	{
		f=f.replace("/", "\\");
		try
		  {
		   FileOutputStream fileOut = new FileOutputStream(f);
		   ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
		   outStream.writeObject(pathxDate);
		   outStream.close();
		   fileOut.close();
		  }catch(IOException i)
		  {
		   i.printStackTrace();
		  }			
	}
	
	//*** SERIALIZES THE ADDED DATA TO STORING AND RESTORATION
	/**
	 * Serializes the added photos and albums
	 * @param loc Photo path
	 * @param locName Album Path
	 */
	public void serializingPaths(String loc,String locName)
	{
		loc=loc.replace("/", "\\");
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
	/**
	 * Deltes the selected photo from the list of photos and from the album completely.
	 * @param event Pushing "Delete Photo" Button
	 */
	@FXML public void delete(ActionEvent event) 
    { 
		albumpath = a.getPath();
		String loc = albumpath+"/path.ser";
		String locName = albumpath+"/pathName.ser";
		
		int index = photosList.getSelectionModel().getSelectedIndex();
		int i = 0;
		int size =  pathx.size();

		if(index==pathx.size()-1)
		{
			photosList.getSelectionModel().select(index-1);
			currentSelectedIndex = index-1;
			strngFP = pathx.get(currentSelectedIndex);			
			file = new File(strngFP);
	        image = new Image(file.toURI().toString());
	        imgview.setImage(image);
	        String imgPath = pathx.get(index);
	        pathx.remove(index);
	        pathxName.remove(index);
	        //pathxDate.remove(index);
	        File f =new File(imgPath);
	        f.delete();
			photosListObs = FXCollections.observableArrayList(pathxName);	
			photosList.setItems(photosListObs); 	
			i = index;
			//pathxDate.remove(index);
		}
	/*	
		if(index==0 && size==1)
		{
			//ln("yesssssssssssss");
			
			photosList.getSelectionModel().select(0);
			currentSelectedIndex = 0;
			strngFP = pathx.get(currentSelectedIndex);			
			file = new File(strngFP);
	        image = new Image(file.toURI().toString());
	        imgview.setImage(null);
			photosListObs = FXCollections.observableArrayList(pathxName);	
			photosList.setItems(photosListObs); 
	        String imgPath = pathx.get(index);
	        
	        pathxName.remove(index);
			photosListObs = FXCollections.observableArrayList(pathxName);	
			photosList.setItems(photosListObs); 
	        //pathxDate.remove(index);
	        File f =new File(imgPath);
	        f.delete();
	        pathx.remove(index);
			i = index;
		}	
		*/
		
		else
		{
	        String imgPath = pathx.get(index);
	        pathx.remove(index);
	        pathxName.remove(index);
	        //pathxDate.remove(index);
	        File f =new File(imgPath);
	        f.delete();			
			photosList.getSelectionModel().select(index);
			autoselectt();
			photosListObs = FXCollections.observableArrayList(pathxName);
			photosList.setItems(photosListObs);	
			photosList.getSelectionModel().select(index);
			i = index;
			//pathxDate.remove(index);
		}  

		serializingPaths(loc,locName);	
		tags[i][0][0] = ""; 
		tags[i][1][0]  = "";
		tags[i][2][0]  = "";
		tags[i][0][1]  = "";
		captionInp[i][0]="";
		captionInp[i][1]="";

		int aaaa = tags.length - ( index - 1 ) ;
		int bbbb = captionInp.length - ( index - 1 ) ;
		   System.arraycopy( tags, index - 1, tags, index, aaaa ) ;
		   System.arraycopy( captionInp, index - 1, captionInp, index, bbbb ) ;

		   //serializingTags(String pathx.get(index),"\\Captions.ser");
    }
	
	 
	//*** COPIES THE SELECTED PHOTO FROM ONE ALBUM TO ANOTHER ALBUM
	/**
	 * Copies the selected photo from one album to another.
	 * @param event Pushing "Copy Image" Button
	 * @throws IOException
	 */
	@FXML public void copyImage(ActionEvent event) throws IOException 
    { 
		albumViewController a = new albumViewController();
		//ArrayList<String> pathxName
		ArrayList<String> choices = new ArrayList<>();
		choices = a.albumnameList;
		String path = "";

		if(!choices.isEmpty())
		{
			ChoiceDialog<String> dialog = new ChoiceDialog<>("Album list", choices);
			dialog.setTitle("Copy Image to another album");
			dialog.setHeaderText("Copy Image");
			dialog.setContentText("Choose the album you wish to copy the image to:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
			    //ln("Options: " + result.get());
			}

			// The Java 8 way to get the response value (with lambda expression).
			String letter2 =result.get();
			path = a.savephotoNamePath + letter2 + "/";
			addCM(path,result.get());
		}

    }	

	//*** HELPED METHOD FOR MOVING AND COPYING PHOTOS TO OTHER ALBUMS
	/**
	 * Helper method to move and copy folders across albums
	 * @param path The path of the photo
	 * @param name The name of the album to go to
	 * @throws IOException
	 */
	public void addCM(String path,String name) throws IOException 
    {  

		path = path.replace("/", "\\");
		//path = path.replace("/", "\\");
		String photoExtension = "";
		String photoPath = strngFP; 
		

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

		
		File foldername = new File(photoPath);
		BufferedImage img = ImageIO.read(foldername);

		
		if(photoExtension.equals("jpg"))ImageIO.write(img, "jpg",new File(filename));
		if(photoExtension.equals("jpeg"))ImageIO.write(img, "jpeg",new File(filename));
		if(photoExtension.equals("png"))ImageIO.write(img, "png",new File(filename));
		if(photoExtension.equals("gif"))ImageIO.write(img, "gif",new File(filename));

    }	
	
	//*** MOVES THE SELECTED PHOTO FROM ONE ALBUM TO ANOTHER ALBUM 
	/**
	 * Moves photos from one album to another
	 * @param event Pushing "Move Image" Button
	 * @throws IOException
	 */
	@FXML public void moveImage(ActionEvent event) throws IOException 
    { 
		albumViewController a = new albumViewController();
		//ArrayList<String> pathxName
		ArrayList<String> choices = new ArrayList<>();
		choices = a.albumnameList;

		String path = "";
int indep=0;
		if(!choices.isEmpty())
		{
			ChoiceDialog<String> dialog = new ChoiceDialog<>("Choices", choices);
			dialog.setTitle("Copy Image to another album");
			dialog.setHeaderText("Copy Image");
			dialog.setContentText("Choose the album you wish to copy the image to:");

			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
			    //ln("Options: " + result.get());
			}

			// The Java 8 way to get the response value (with lambda expression).
			String letter2 =result.get();
			path = a.savephotoNamePath + letter2 + "/";

			addCM(path,result.get());

			int ind = pathx.indexOf(strngFP);
			indep = ind;
	        File f =new File(strngFP);
	        f.delete();
			pathx.remove(ind); 
			//pathxDate.remove(ind);
			String abc = typeExt(strngFP);
			if(pathxName.contains(abc))
			{

				int aa = pathxName.indexOf(abc);
				pathxName.remove(aa);
			}
			//pathxName.remove(ind);
			photosListObs = FXCollections.observableArrayList(pathxName);	
			photosList.setItems(photosListObs);			
		}		
		tags[indep][1][0] = "";
		tags[indep][2][0] = "";
		tags[indep][3][0] = "";
		tags[indep][0][1] = "";
		captionInp[indep][0]="";
		captionInp[indep][1]="";
    }	
	

	//*** Moves to the next selection in the list
	/**
	 * Selects the next photo available in the photo list
	 * @param event Pushing "Next" Button
	 */
	@FXML public void next(ActionEvent event) 
    { 
		if(currentSelectedIndex<pathx.size())
			{
			photosList.getSelectionModel().select(currentSelectedIndex+1);
			//photosList.getSelectionModel().getSelectedIndex();
			autoselectt();
			//photosList.getSelectionModel().select(currentSelectedIndex+1);
			//photosListObs = FXCollections.observableArrayList(pathxName);	
	  		//photosList.setItems(photosListObs); 
			}
    }
	
	
	//*** Moves to the previous selection in the list
	/**
	 * Selects the previous photo available in the photo list
	 * @param event Pushing "Prev" Button
	 */
	@FXML public void prev(ActionEvent event) 
    { 
		if(currentSelectedIndex>0)
			{
			photosList.getSelectionModel().select(currentSelectedIndex-1);
			autoselectt();
			//photosList.getSelectionModel().select(currentSelectedIndex-1);
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
	/**
	 * Helper method to obtain the paths of the photos selected in the 
	 * list of photos
	 */
	public void autoselectt()
	{
		int index = photosList.getSelectionModel().getSelectedIndex();
		curIndex = index;
		currentSelectedIndex = index;
		strngFP = pathx.get(index);

		file = new File(strngFP);
        image = new Image(file.toURI().toString());
        imgview.setImage(image);	
		photosListObs = FXCollections.observableArrayList(pathxName);	
		photosList.setItems(photosListObs);  
		
		photosListObsDetail = FXCollections.observableArrayList("Name: " + pathxName.get(index), "Caption :" + captionInp[index][1] , "Tags: " + tags[index][0][1]
				, "Date: " + pathxDate.get(index)
				);	
		photosListDetail.setItems(photosListObsDetail);			
		
	}
	
	
	//THIS IS WHEN YOU CLICK ON A LIST ITEM IN LISTVIEW
	//*** Helps select the correct path of the photo of the selected photo in the list
	/**
	 * Obtains the path of the selected photo upon the click of a mouse
	 * @param event Using mouse to click on list of photos
	 * @throws IOException
	 */
	@FXML
	public void mousephotosList(MouseEvent event) throws IOException
	{
		int index = photosList.getSelectionModel().getSelectedIndex();
		currentSelectedIndex = index;
		strngFP = pathx.get(index);

		file = new File(strngFP);

        image = new Image(file.toURI().toString());
        imgview.setImage(image);

		
		photosListObs = FXCollections.observableArrayList(pathx);
		if( event.getSource() == photosList )
		{
			index = photosList.getSelectionModel().getSelectedIndex();
			if(index<0)return;
		} 
		
		photosListObsDetail = FXCollections.observableArrayList("Name: " + pathxName.get(index), "Caption: " + captionInp[index][1] ,
				 "Tags: " + tags[index][0][1],"Date: " + pathxDate.get(index)
				);	
		photosListDetail.setItems(photosListObsDetail);		
		
	}

	//*** GOES BACK TO THE ALBUM VIEW PAGE
	/**
	 * Returns to the list of albums for the user
	 * @param event Pushing "Album View" Button
	 * @throws IOException
	 */
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
	/**
	 * Displays an image more clearly
	 * @param event Pushing "Display Image" Button
	 * @throws IOException
	 */
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
	/**
	 * Helper method to store the paths of the photos
	 * @param filePath The path of the photo
	 * @return correct format to store in serializable file
	 */
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
	
	/**
	 * Deserializes a file of stored photos in album, if any exist
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		 /* String dq = a.albumnameListPath.get(0);
		  dq = dq + "\\date.ser";
		  File aaa6 = new  File(dq);
        if(!aaa6.exists())
        {
        pathxDate.add("21/11/18");
        }
		*/
		
		for(int i=0;i<100;i++)
		{
			captionInp[i][1]="";
			tags[i][0][0]="";
			tags[i][1][0]="";
			tags[i][2][0]="";
			tags[i][0][1]="";
		}
		
		
		photosListDetail.setItems(photosListObsDetail);
		
		albumpath = a.getPath();
		String loc = albumpath+"/path.ser";
		String pp = albumpath;
		String locName = albumpath+"/pathName.ser";
		loc = loc.replace("/", "\\");
		File aaa = new File(loc);
		File p1 = new File(pp);

		if(aaa.exists())
		{

			if(p1.list().length<=0)
			{

				File a1 = new File(loc);
				File a2 = new File(locName);
				a1.delete();
				a2.delete();
				pathx.clear();
				pathxName.clear();
				
			}
			
			//captionInp
			
			if(p1.list().length>0)
			{

				try
			       {
					albumViewController a = new albumViewController();

			          FileInputStream fileIn =new FileInputStream(loc);
			          FileInputStream fileIn2 =new FileInputStream(locName);
			          

    
			          ObjectInputStream in = new ObjectInputStream(fileIn);
			          ObjectInputStream in2 = new ObjectInputStream(fileIn2);
			          pathx = (ArrayList<String>) in.readObject();
			          pathxName = (ArrayList<String>) in2.readObject(); 

			          
			          ///////////////////////////
			          File folder = new File(albumpath);
			          File[] listOfFiles = folder.listFiles();
			          for (File file : listOfFiles) {
			              if (file.isFile()) {
			                  if(!pathx.contains(file.getName()))
			                  {
			                	  if(!pathxName.contains(file.getName()))// && (!(file.getName().equals("path.ser")) || !file.getName().equals("pathName.ser")))
			                	  {
			                		  
			                		  if(file.getName().equals("path.ser") || file.getName().equals("pathName.ser") || file.getName().equals("details.ser")
			                				  || file.getName().equals("Captions.ser") || file.getName().equals("date.ser")
			                				  )
				                	  {
				                		  
				                	  }
			                		  
			                		  else
			                		  {
			                			if(!pathx.contains(file.getAbsolutePath()))
			                			{
						                	  String abc = albumpath + "/"+file.getName();
							                  pathx.add(abc);
							                  String name = typeExt(file.getName());
							                  pathxName.add(name);			                				
			                			}
			                		  }
			                	  }
			                	  
			                	  else
			                	  {

				                	  String abc = albumpath + "/"+file.getName();
					                  pathx.add(abc);
					                  pathxName.add(file.getName());
			                	  }
			                  }
			              }
			          }

			          //////////////// For details part  //////////////////////
			          photosListObs = FXCollections.observableArrayList(pathxName);	
			  		  photosList.setItems(photosListObs); 
					  String detailCaptionpath = a.albumnameListPath.get(0);
					  detailCaptionpath = detailCaptionpath + "\\details.ser";
					  File aaa1 = new  File(detailCaptionpath);
			          if(aaa1.exists())
			          {
			        	  FileInputStream captionDetails =new FileInputStream(detailCaptionpath);
				          ObjectInputStream cpt = new ObjectInputStream(captionDetails);
				          //Object ci = captionInp;
				          captionInp = (String[][])cpt.readObject(); //<--DETAILS PART--->
						  cpt.close();
						  captionDetails.close();						          			        	  
			          }			  		
			          in.close();
			          in2.close();
			          fileIn.close();
			          fileIn2.close();
			          
			          //////////////// For Captions part  //////////////////////
			          photosListObs = FXCollections.observableArrayList(pathxName);	
			  		  photosList.setItems(photosListObs); 
					  String CaptionCaptionpath = a.albumnameListPath.get(0);
					  CaptionCaptionpath = CaptionCaptionpath + "\\Captions.ser";
					  File aaa2 = new  File(CaptionCaptionpath);
			          if(aaa2.exists())
			          {
			        	  FileInputStream captionccDetails =new FileInputStream(CaptionCaptionpath);
				          ObjectInputStream captionccDetails2 = new ObjectInputStream(captionccDetails);
				          //Object ci = captionInp;
				          tags = (String[][][])captionccDetails2.readObject(); //<--DETAILS PART--->
				          captionccDetails2.close();
				          captionccDetails.close();						          			        	  
			          }			  		

			          
			          
			          //////////////// For date part  //////////////////////
			          photosListObs = FXCollections.observableArrayList(pathxName);	
			  		  photosList.setItems(photosListObs); 
					  String DatePath = a.albumnameListPath.get(0);
					  DatePath = DatePath + "\\date.ser";
					  File aaa3 = new  File(DatePath);
			          if(aaa3.exists())
			          {
			        	  
			        	  FileInputStream er =new FileInputStream(DatePath);
				          ObjectInputStream er2 = new ObjectInputStream(er);
				          //Object ci = captionInp;
				          pathxDate = (ArrayList<String>) er2.readObject(); //<--DETAILS PART--->
				          er2.close();
						  er.close();						          			        	  
			          }			  		

			  		//serializingDate(dateloc); // String dateloc = albumpath+"/date.ser";
			          
			          
			          
			          
			  		photosList.getSelectionModel().select(0);
					String abc = pathx.get(0);
					curPathDetail = abc;

					//This will be replaced by the pic on the very top
					file = new File(abc);
					//if(pathx!=null) {file = new File(strngFP);}
			        image = new Image(file.toURI().toString());
			        imgview.setImage(image);
			          
					photosListObsDetail = FXCollections.observableArrayList("Name: " + pathxName.get(0), "Caption :" + captionInp[0][1] , "Tags: " + tags[0][0][1]
							, "Date: " + pathxDate.get(0));	
					photosListDetail.setItems(photosListObsDetail);	 		        
			        //int i = pathxName.indexOf(abc);

			        
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

	          File folder = new File(lock);

	          if(folder.list().length>0)
	          { 
		          File[] listOfFiles = folder.listFiles();

		          for (File file : listOfFiles) {

		              if (file.isFile()) {

		                  if(!pathx.contains(file.getName()))
		                  {
		                	  
	                		  if(file.getName().equals("path.ser") || file.getName().equals("pathName.ser") || file.getName().equals("details.ser")
	                				  || file.getName().equals("Captions.ser") || file.getName().equals("date.ser")
	                				  )
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

		          photosListObs = FXCollections.observableArrayList(pathxName);	
			  		photosList.setItems(photosListObs);

			  		photosList.getSelectionModel().select(0);
					String abc = pathx.get(0);

					//This will be replaced by the pic on the very top
					file = new File(abc);
					//if(pathx!=null) {file = new File(strngFP);}
			        image = new Image(file.toURI().toString());
			        imgview.setImage(image); 
	          }

	          //////////////////////////////////////
		}

		photosList.getSelectionModel().select(0);
		//String abc = pathxName.get(0);

	}	
	
	//*** LOGS OUT
	/**
	 * Logs out from current session
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
}
 