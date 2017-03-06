package imgProcess;

import java.io.IOException;
import java.util.LinkedList;

import imgProcess.view.ImgPaneController;
import imgProcess.view.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	public Stage primaryStage;
	private BorderPane rootLayout;
	private ImgPaneController imgController;
	public LinkedList<Image> imgs;
	@Override
	public void start(Stage primaryStage) {
		imgs = new LinkedList<Image>();
		imgs.add(null);
		imgs.add(null);
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Filtering");
		this.primaryStage.setResizable(false);
		this.primaryStage.getIcons().add(new Image("file:src/imgProcess/img.jpg"));
		initRootLayout();
		showImgPane();
		
	}
	private void initRootLayout(){
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			RootController rtController = loader.getController();
			rtController.setMainApp(this);
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			//primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void showImgPane(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ImgPane.fxml"));
			AnchorPane ImgPane = (AnchorPane) loader.load();
			rootLayout.setCenter(ImgPane);
			imgController = loader.getController();
			imgController.setMainApp(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadImg(Image img){
		imgs.remove(0);
		imgs.add(0, img);
		imgController.setImgV(true, img);
		imgController.setRunItem(false);
	}
	public static void main(String[] args) {
		launch(args);
	}
}
