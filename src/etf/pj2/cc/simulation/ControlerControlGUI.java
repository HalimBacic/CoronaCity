package etf.pj2.cc.simulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import etf.pj2.cc.controllers.AmbulanceControl;
import etf.pj2.cc.controllers.PersonControl;
import etf.pj2.cc.entity.Ambulance;
import etf.pj2.cc.entity.Person;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.map.City;
import etf.pj2.map.Position;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;


@SuppressWarnings("serial")
public class ControlerControlGUI implements Serializable {	
	
	private Person infectedPerson;
	private AmbulanceControl ambulanceControl;
    private Vector<AmbulanceControl> allAmbulanceControls;
    private Vector<PersonControl> allPersonControls;
    private City city;
	private Stage stage;
	private Group groupCity;   
	private long startTime=0;
	private static File filewatcher=new File("FileWatcher.txt"); 
	private Integer paused=0;
	private CoronaWatchService watcherService;
	
	@FXML
	private TextField tfInformation;
	@FXML
	private TextField tfRecovered;
	@FXML
	private Button btnEnableMoving;
	@FXML
	private Button btnStopSimulation;
	@FXML
	private Button btnStartAgain;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnSendAmbulance;
	@FXML
	private Button btnViewAmbulance;
	@FXML
	private Button btnViewStatistics;
	@FXML
	private TextField tfAI=new TextField();
	@FXML
	private Text tfIndicator = new Text();

	
	
	public ControlerControlGUI() {
		
	}

	@FXML
	private void initialize() {
		
	}
	
	public void setElements(Vector<AmbulanceControl> ambulances,Vector<PersonControl> persons,City city)
	{
		this.allAmbulanceControls=ambulances;
		this.allPersonControls=persons;
		this.city=city;
		watcherService=new CoronaWatchService(this);
	}
	
	
	public void setInfectedInfo(Person person,AmbulanceControl ambulance)
	{
		infectedPerson=person;
		ambulanceControl=ambulance;
	}
	
	public void setStage(Stage stage)
	{
		this.stage=stage;
	}
	
	public void setGroupCity(Group groupCity)
	{
		this.groupCity=groupCity;
	}
	
	public void settfAI(String printerString)
	{
		tfAI.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ff2424"), new CornerRadii(0), new Insets(0))));
		tfAI.setText(printerString);
	}
	
	public void sendAmbulanceCar()
	{
		tfAI.setBackground(new Background(new BackgroundFill(Paint.valueOf("#e6dec8"), new CornerRadii(1), new Insets(1))));
		tfAI.setText("Alarm Info");
		ambulanceControl.sendCarFromAmbulance(infectedPerson);
	}
	
	public void setInfected()
	{
		Integer numberOfInfected=Integer.parseInt(tfInformation.getText());
		numberOfInfected++;
		tfInformation.setText(numberOfInfected.toString());
		   try {
				editFileWatcher();
			} catch (IOException e) {
				CCLogger.cclogger(getClass().getName(), Level.INFO, e);
			}
	}
	
	public void setInfected(String editedText)
	{
		tfInformation.setText(editedText);
		System.out.println("");
	}
	
	public void enableMoving() throws IOException, InterruptedException
	{				
		tfIndicator.setText("Working");
		tfIndicator.setFill(Paint.valueOf("#00d90b"));
		Vector<Thread> allThreads = new Vector<Thread>();
		allThreads.addAll(allPersonControls);
		allThreads.addAll(allAmbulanceControls);			
			try {
				FileOutputStream fileOutputStream = new FileOutputStream("start.ser");
				ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
				
				objectOutputStream.writeObject(city);
				objectOutputStream.close();
			} catch (IOException e) {
				CCLogger.cclogger(getClass().getName(), Level.INFO, e);
			}

			
			startTime = System.currentTimeMillis();


			for (Thread thread : allThreads) {
				thread.start();
			}
			watcherService.start();
	}
	
   public void stopSimulation() throws IOException
   {
	   if(paused==0) {
	    paused=1;
		FileOutputStream fileOutputStream=new FileOutputStream("pause.ser");
		ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
		
		objectOutputStream.writeObject(city);
		objectOutputStream.close();
		
		   for(PersonControl person : allPersonControls)
		   {
			   person.setPause(1);
		   }
		   
		   for (AmbulanceControl ambulance : allAmbulanceControls) {
				 ambulance.setPause(1);
			}
		   
		   watcherService.setPause(1);
		
		btnStopSimulation.setText("CONTINUE");   
		tfIndicator.setText("Paused");
		tfIndicator.setFill(Paint.valueOf("#f5aa07"));
	   }
	   else
	   {
		   paused=0;
		   btnStopSimulation.setText("STOP SIMULATION");
		   tfIndicator.setText("Working");
		   tfIndicator.setFill(Paint.valueOf("#00d90b"));
		   
			Vector<Thread> allThreads = new Vector<Thread>();
			allThreads.addAll(allPersonControls);
			allThreads.addAll(allAmbulanceControls);	
		   
			try {
				FileInputStream fileInputStream=new FileInputStream("pause.ser");
				ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
				city = (City) objectInputStream.readObject();
				objectInputStream.close();
			} catch (ClassNotFoundException | IOException e) {
				CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
			}
			
			
			   for(PersonControl person : allPersonControls)
			   {
				   person.setPause(0);
			   }
			   
			   for (AmbulanceControl ambulance : allAmbulanceControls) {
					 ambulance.setPause(0);
				}
			   watcherService.setPause(0);
	   }
   }
   
   public void exitSimulation()
   {
	   for(PersonControl person : allPersonControls)
	   {
		   person.setStartorStop(0);
	   }
	   
	   for (AmbulanceControl ambulance : allAmbulanceControls) {
			 ambulance.setStartorStop(0);
		}
	   
	   watcherService.setStartorStop(0);
	   
	   long endTime=System.currentTimeMillis();
	   
	   
	   long elapsedTime=endTime-startTime;
	   
	   SimpleDateFormat dateFormater = new SimpleDateFormat("mm:ss");
	   Date elapsedTimeDate=new Date(elapsedTime);
	   
	   SimpleDateFormat nameFormat= new SimpleDateFormat("HH_mm_ss___dd_MM_YYYY");
	   
	   String nameTime=nameFormat.format(endTime);
	   String executionTime=dateFormater.format(elapsedTimeDate);
	   
	   Integer ambulanceNumber=city.getAmbulanceVector().size();
	   
       File information=new File("information.txt");
       BufferedWriter bufferedWriter;
       try {
		 bufferedWriter=new BufferedWriter(new FileWriter(information, true));
	     bufferedWriter.write("\nAmbulance number: "+ambulanceNumber);
	     bufferedWriter.write("\nInfected: "+tfInformation.getText());
	     bufferedWriter.write("\nRecovered: "+tfRecovered.getText());
	     bufferedWriter.write("\nWorking time: "+executionTime);
	     bufferedWriter.close();
	} catch (IOException e) {
		CCLogger.cclogger(getClass().getName(), Level.INFO, e);
	}
       
       information.renameTo(new File("„SIM-JavaKov-20-"+nameTime+".txt"));
	   stage.close();
	   
		File fileWatcher=new File("FileWatcher.txt");
		if(fileWatcher.exists())
			fileWatcher.delete();
   }
   
   public void setRecovered()
   {
	   Integer recovered=Integer.parseInt(tfRecovered.getText());
	   recovered++;
	   tfRecovered.setText(recovered.toString());
	   try {
		editFileWatcher();
	} catch (IOException e) {
		CCLogger.cclogger(getClass().getName(), Level.INFO, e);
	}
   }
   
   public void setRecovered(String editedText)
   {
	   tfRecovered.setText(editedText);
	   System.out.println("");
   }
   
   @SuppressWarnings("unchecked")
public void viewAmbulance()
   {

	   ObservableList<Ambulance> observableList=FXCollections.observableArrayList();
	   observableList.addAll(city.getAmbulanceVector());
			   
	   Stage stageAmbulanceView = new Stage();
	   
       TableView<Ambulance> tableView=new TableView<Ambulance>();
	   tableView.setItems(observableList);
       tableView.setEditable(false);
       tableView.setMinWidth(310);
       tableView.setMaxHeight(150);
	   
       TableColumn<Ambulance, Position> ambulanceName=new TableColumn<Ambulance, Position>("Hospital Position");
       TableColumn<Ambulance, Integer> ambulanceCapacity=new TableColumn<Ambulance, Integer>("Hospital Capacity");
       TableColumn<Ambulance, Integer> ambulanceStat=new TableColumn<Ambulance, Integer>("Current Stat");
       
       ambulanceName.setCellValueFactory(new PropertyValueFactory<Ambulance, Position>("position"));
       ambulanceCapacity.setCellValueFactory(new PropertyValueFactory<Ambulance, Integer>("capacity"));
       ambulanceStat.setCellValueFactory(new PropertyValueFactory<Ambulance, Integer>("currentStat"));
       
       
       tableView.getColumns().addAll(ambulanceName,ambulanceCapacity,ambulanceStat);
       tableView.setEditable(false);
       tableView.setMinWidth(310);
       tableView.setMaxHeight(50*city.getAmbulanceVector().size());
       
	   Button btnAddAmbulance=new Button("Add Ambulance");
	   btnAddAmbulance.setOnAction(new EventHandler<ActionEvent>() {
		
		@Override
		public void handle(ActionEvent arg0) {
			try {
				newAmbulanceFunction();
			} catch (IOException e) {
				CCLogger.cclogger(ControlerControlGUI.class.getName(), Level.WARNING, e);
			}
			
		}
	});
	   
	   VBox paneBox=new VBox();
	   paneBox.getChildren().addAll(tableView,btnAddAmbulance);
	   paneBox.setPadding(new Insets(30, 30, 30, 30));
	   paneBox.setSpacing(30);
	   paneBox.setAlignment(Pos.CENTER);
	   
	   Scene scene=new Scene(paneBox);
	   stageAmbulanceView.setHeight(400);
	   stageAmbulanceView.setWidth(370);
	   stageAmbulanceView.setScene(scene);
	   stageAmbulanceView.show();   
   }
   
   public void newAmbulanceFunction() throws IOException
   {
	   FXMLLoader loader=new FXMLLoader(getClass().getResource("NewAmbulance.fxml"));
	   Parent root=loader.load();
	   
	   Stage addAmbulanceStage = new Stage();
	   
	   addAmbulanceStage.setScene(new Scene(root, 400, 280));
	   NewAmbulanceControl control=loader.getController();
	   control.setElements(city, groupCity,addAmbulanceStage,allAmbulanceControls,allPersonControls);
	   addAmbulanceStage.show();
   }
   
   public void viewStatistic() throws IOException
   {
	   FXMLLoader loader=new FXMLLoader(getClass().getResource("Statistic.fxml"));
	   loader.load();

	   StatisticController statisticController=loader.getController();
	   statisticController.setStatistic(city);	   
   }
   
   public void editFileWatcher() throws IOException
   {
	   BufferedWriter fileWatchBufferedWriter=new BufferedWriter(new FileWriter(filewatcher));
	   String writeLine=new String(tfInformation.getText()+" "+tfRecovered.getText());
	   fileWatchBufferedWriter.write(writeLine);
	   fileWatchBufferedWriter.close();
   }
   
   public synchronized void readAndChange() throws IOException
   {
		BufferedReader reader=new BufferedReader(new FileReader(filewatcher));
		String firstLine=reader.readLine();
		String[] firstLineWords=firstLine.split(" ");
		if(firstLineWords[0]==null)
			firstLineWords[0]="?";
		if(firstLineWords[1]==null)
			firstLineWords[1]="?";
		
		setInfected(firstLineWords[0]);
		setRecovered(firstLineWords[1]);
		reader.close();
   }
   
   public void startAgain()
   {
	   
		File fileWatcher=new File("FileWatcher.txt");
		if(fileWatcher.exists())
			fileWatcher.delete();
		
	   System.out.println("I am here");
	   tfIndicator.setText("Working");
	   tfIndicator.setFill(Paint.valueOf("#00d90b"));
	   
	   for(PersonControl person : allPersonControls)
	   {
		   person.setStartorStop(0);
	   }
	   
	   for (AmbulanceControl ambulance : allAmbulanceControls) {
			 ambulance.setStartorStop(0);
		}
	   
	   watcherService.setStartorStop(0);
	   
	  
		Vector<Thread> allThreads = new Vector<Thread>();
		allThreads.addAll(allPersonControls);
		allThreads.addAll(allAmbulanceControls);
	   
		paused=0;
		
		try {
			FileInputStream fileInputStream=new FileInputStream("start.ser");
			ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
			city = (City) objectInputStream.readObject();
			objectInputStream.close();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
		
	   //TODO Vratiti niti na početak
		
		if(!fileWatcher.exists())
			try {
				fileWatcher.createNewFile();
			} catch (IOException e) {
				CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
			}
		
		System.out.println("Start again");
   }	
}
