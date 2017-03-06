# imgProcess

針對彩色影像進行不同遮罩的平滑化，並對於各方法之間，以及各方法的使用次數所造成的模糊程度與去雜訊程度進行比較。

架構:

imgProcess(Project)
 	|--src(File)
 		|--imgProcess(Package)
	|--MainApp.java				//程式進入點及視窗底層
	|--model(Package)
		|--ImageProcess.java			//影像處理函式
	|--view(Package)
		|--ImgPane.fxml			//上層介面板(含圖像及按鈕)
		|--RootLayout.fxml			//底層介面(含工具欄)
		|--ImgPaneController.java		//對應fxml的事件處理
				|--RootController.java		//對應fxml的事件處理
