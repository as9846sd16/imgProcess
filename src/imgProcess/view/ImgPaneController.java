package imgProcess.view;

import java.io.File;
import java.io.IOException;
import java.time.Year;
import java.util.Arrays;

import javax.imageio.ImageIO;

import imgProcess.MainApp;
import imgProcess.model.ImageProcess;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.chart.BubbleChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ImgPaneController {
	private MainApp mainApp;
	ObservableList<String> cbItems =  FXCollections.observableArrayList("Origin", "Result");
	ObservableList<String> methodItems =  FXCollections.observableArrayList(
			"Average Blur", "Gaussion Blur", "Median Filter", "Min Filter", "Max Filter", "Peak Filter", "Valley Filter", "Difference", "High-boost");
	public int tImgNum;
	private int methodNum;
	@FXML
	private ChoiceBox cb1;
	@FXML
	private ChoiceBox cb2;
	@FXML
	private ChoiceBox cb3;
	@FXML
	private ChoiceBox methodCb;
	@FXML
	private ImageView imgRight;
	@FXML
	private ImageView imgleft;
	@FXML
	private Button runbt;
	@FXML
	private Button saveBt;
	@FXML
	private Button saveAllBt; 
	@FXML
	private Button deleteBt;
	@FXML
	private TextField nameTf;
	@FXML
	private TextField sizeTf;
	@FXML
	private TextField sigmaTf;
	@FXML
	private TextField peakTf;
	@FXML
	private TextField alphaTf;
	@FXML
	private VBox sigmaBox;
	@FXML
	private VBox sizeBox;
	@FXML
	private VBox peakBox;
	@FXML
	private VBox alphaBox;
	@FXML
	private SplitPane sp;
	@FXML
	private void initialize(){
		setCbs(true);
		setRunItem(true);
		SplitPane.Divider divider = sp.getDividers().get(0);
		divider.positionProperty().addListener((ob, oldD, newD) -> divider.setPosition(0.5) );
		cb1.setValue(cbItems.get(0));
		cb1.setItems(cbItems);
		cb1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				int index = cb1.getSelectionModel().selectedIndexProperty().getValue();
				setImgV(true, mainApp.imgs.get(index));
			}

		});
		cb2.setItems(cbItems);
		cb2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				int index = cb2.getSelectionModel().selectedIndexProperty().getValue();
				setImgV(false, mainApp.imgs.get(index));
			}
		});
		cb3.setItems(cbItems);
		cb3.setValue(cbItems.get(0));
		cb3.setMouseTransparent(true);
		cb3.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				int index = cb3.getSelectionModel().selectedIndexProperty().getValue();
				tImgNum = index;
			}
		});
		methodCb.setItems(methodItems);
		methodCb.setValue(methodItems.get(0));
		methodCb.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				int index = methodCb.getSelectionModel().selectedIndexProperty().getValue();
				methodNum = index;
				if(methodNum == 1){
					sigmaBox.setVisible(true);
				}
				else{
					sigmaBox.setVisible(false);
				}
				if(methodNum <= 2){
					sizeBox.setVisible(true);
				}
				else{
					sizeBox.setVisible(false);
				}
				if(methodNum == 7){
					peakBox.setVisible(true);
				}
				else{
					peakBox.setVisible(false);
				}
				if(methodNum == 8){
					alphaBox.setVisible(true);
				}
				else{
					alphaBox.setVisible(false);
				}
			}
		});
		tImgNum = 0;
		sizeTf.setText("3");
		sigmaTf.setText("0.797");
		sigmaBox.setVisible(false);
		peakTf.setText("45");
		peakBox.setVisible(false);
		alphaTf.setText("1.1");
		alphaBox.setVisible(false);
	}
	@FXML
	private void handleRun(){
		setCbs(false);
		Image img = mainApp.imgs.get(tImgNum);
		int size = Integer.parseInt(sizeTf.getText());
		switch (methodNum) {
		case 0:
			img = ImageProcess.avgBlur(img, size);
			break;
		case 1:
			double sigma = Double.parseDouble(sigmaTf.getText());
			img = ImageProcess.gaussBlur(img, size, sigma);
			break;
		case 2:
			img = ImageProcess.medianBlur(img, size);
			break;
		case 3:
			img = ImageProcess.minMaxFilter(img, true);
			break;
		case 4:
			img = ImageProcess.minMaxFilter(img, false);
			break;
		case 5:
			img = ImageProcess.peakValleyFilter(img, false);
			break;
		case 6:
			img = ImageProcess.peakValleyFilter(img, true);
			break;
		case 7:
			img = ImageProcess.difference(img, Integer.parseInt(peakTf.getText()));
			break;
		case 8:
			img = ImageProcess.highBoost(img, Double.parseDouble(alphaTf.getText()));
			break;
		default:
			break;
		}
		
		mainApp.imgs.remove(1);
		mainApp.imgs.add(1, img);
		setImgV(false,img);
		cb2.setValue(cbItems.get(1));
		cb3.setMouseTransparent(false);
		cb3.setValue(cbItems.get(1));
		tImgNum = 1;
	}
	@FXML
	public void handleAddBt(){
		String str = nameTf.getText();
		mainApp.imgs.addLast(mainApp.imgs.get(tImgNum));
		cbItems.add(str);
	}
	@FXML
	public void handleSaveBt(){
		File file;
		try {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
			fc.setInitialFileName(cbItems.get(tImgNum));
			fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
			file = fc.showSaveDialog(mainApp.primaryStage);
			if(file!=null){
				ImageIO.write(SwingFXUtils.fromFXImage(mainApp.imgs.get(tImgNum), null), "png", file);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	public void handleSaveAllBt(){
		File file;
		try {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
			fc.setInitialFileName("Please choose a directory to save all images.");
			fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
			file = fc.showSaveDialog(mainApp.primaryStage);
			File f;
			if(file!=null && cbItems.size()>=3){
				for(int i = 2; i<cbItems.size(); i++){
					f = new File(file.getParentFile() + "\\"+ cbItems.get(i) + ".jpg");
					ImageIO.write(SwingFXUtils.fromFXImage(mainApp.imgs.get(i), null), "png", f);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML
	public void handleDeleteBt(){
		if(tImgNum!=0 && tImgNum!=1){
			cbItems.remove(tImgNum);
			tImgNum = tImgNum - 1;
		}
	}
	public void setRunItem(boolean isD){
		runbt.setDisable(isD);
		saveBt.setDisable(isD);
		saveAllBt.setDisable(isD);
	}
	public void setMainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
	public void setImgV(boolean isL, Image img){
		ImageView imgv = (isL ? this.imgleft : this.imgRight);
		double iw = img.getWidth(), ih = img.getHeight();
		final double ww = 500, hh = 350;
		imgv.setFitHeight(0);
		imgv.setFitWidth(0);
		if(iw > ww) imgv.setFitWidth(ww);
		if(ih > hh) imgv.setFitHeight(hh);
		imgv.setImage(img);
	}
	public void setCbs(boolean isb){
		cb1.setMouseTransparent(isb);
		cb2.setMouseTransparent(isb);
	}
}