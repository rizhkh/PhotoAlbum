package controller;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser; 
import javafx.stage.Stage;

public class expandedViewController implements Initializable {
 
	Stage stage;
	File file = new File("");
	 Image image;
	
	public static String strngFP;
	ArrayList<String> pathx = new ArrayList(); 
	
	@FXML Button adp;
	@FXML ListView photosList;
	@FXML private ImageView imgview; 
	 
	ObservableList<String> photosListObs = FXCollections.observableArrayList(pathx);
	
    //WHEN YOU PRESS THE ADD PHOTO BUTTON YOU GET AN ALERT
    // THIS METHOD WORKS WHEN YOU CLICK THE ADD PHOTO BUTTON	
	@FXML public void add(ActionEvent event) 
    {  
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
		System.out.println("path: " + strngFP);
		file = new File(strngFP);
		//if(pathx!=null) {file = new File(strngFP);}
        image = new Image(file.toURI().toString());
        imgview.setImage(image);
		//imgview.setImage(new Image(strngFP)); 
		pathx.add(filePath);
		
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
	
		
		//System.out.println(result);
		//RESULT IS THE NAME OF THE FILE
	photosList.getItems().add(result);
	
	//strngFP = pathx.get(index);
	//System.out.println("$$$$ " + strngFP);

	//photosListObs = FXCollections.observableArrayList(pathx);
		
	
    }
	
	
	//THIS IS WHEN YOU CLICK ON A LIST ITEM IN LISTVIEW
	@FXML
	public void mousephotosList(MouseEvent event) throws IOException
	{
		int index = 	photosList.getSelectionModel().getSelectedIndex();
		//System.out.println("$$$$ " + index);
		strngFP = pathx.get(index);
		//System.out.println("$$$$ " + strngFP);
		
		//String abcd = "\\\\";
		//filePath = filePath.replace("\\", abcd);
		//filePath = filePath.replace("[", "");
		//filePath = filePath.replace("]", "");
		//strngFP = filePath;
		System.out.println("path: " + strngFP);
		file = new File(strngFP);
		//if(pathx!=null) {file = new File(strngFP);}
        image = new Image(file.toURI().toString());
        imgview.setImage(image);
		//imgview.setImage(new Image(strngFP)); 
		//pathx.add(filePath);
		
		photosListObs = FXCollections.observableArrayList(pathx);
		if( event.getSource() == photosList )
		{
			index = photosList.getSelectionModel().getSelectedIndex();
			if(index<0)return;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		file = new File("C:\\Users\\Rizwan\\Desktop\\img\\me.png");
		//if(pathx!=null) {file = new File(strngFP);}
        image = new Image(file.toURI().toString());
        imgview.setImage(image);
		
	}	
	
	
}
