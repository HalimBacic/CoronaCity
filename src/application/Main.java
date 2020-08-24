package application;
	
import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import etf.pj2.cc.exceptions.CCLogger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;

//TODO Kada startuje aplikacija ponovo, ambulanceCar, Ambulance i WatchmanGUI ne funkcionišu kako trebaju

@SuppressWarnings("serial")
public class Main extends Application implements Serializable {
	

	
	@Override
	public void start(Stage primaryStage) {
	
		try {
	        Parent startScreen = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
	        primaryStage.setTitle("Corona City Simulator");
	        primaryStage.setScene(new Scene(startScreen, 700, 500));
	        primaryStage.setX(100);
	        primaryStage.setY(150);
	        primaryStage.show();
	        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent arg0) {
					File fileWatcher=new File("FileWatcher.txt");
					if(fileWatcher.exists())
						fileWatcher.delete();	
					File loggerFiles=new File("log");
					if(loggerFiles.exists())
						fileWatcher.delete();	
				}
			});
	        
		} catch(Exception e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		File fileWatcher=new File("FileWatcher.txt");
		if(fileWatcher.exists())
			fileWatcher.delete();
	}
}
