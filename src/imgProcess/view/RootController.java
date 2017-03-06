package imgProcess.view;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;

import imgProcess.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class RootController {
	private MainApp mainApp;
	private FileChooser fc;
	public RootController(){
		fc = new FileChooser();
		fc.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
		fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
	}
	@FXML
	private void handleLoad(){
		try {
			File file = fc.showOpenDialog(mainApp.primaryStage);
			if (file != null) {
				String url = file.toURI().toURL().toString();
				String r = file.toString();
				Image img = new Image(url);
				mainApp.loadImg(img);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
}
