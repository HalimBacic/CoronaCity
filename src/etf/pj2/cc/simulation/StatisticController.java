package etf.pj2.cc.simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVWriter;
import etf.pj2.cc.entity.Ambulance;
import etf.pj2.map.City;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StatisticController {
	@FXML
	private PieChart chartAge;
	
	@FXML
	private BarChart<String, Number> chartGender;
	
	@FXML
	private Button btnOK;
	
	@FXML
	private Pane statPane;
	
	
	@FXML
	private TextField tfInfected;
	
	@FXML
	private TextField tfRecovered;
	
	@FXML
	private Button btnSaveStat;
	
	Stage statStage=new Stage();
	
	private Integer infectedPersons=0; 
	private Integer infectedRetirre=0;
	private Integer infectedKids=0;
	private Integer infectedMales=0;
	private Integer infectedFemales=0;
	private Integer infected=0;
	private Integer recovered=0;
	
	public StatisticController()
	{}
	
	
	@SuppressWarnings("unchecked")
	public void setStatistic(City city)
	{		
		
		btnSaveStat.setText("Save Statistic");
		for (Ambulance ambulance : city.getAmbulanceVector()) {
			infectedPersons+=ambulance.getInfectedPersons();
			infectedRetirre+=ambulance.getInfectedRetiree();
			infectedKids+=ambulance.getInfectedKids();
			infectedMales+=ambulance.getInfectedMales();
			infectedFemales+=ambulance.getInfectedFemales();
			infected+=ambulance.getNumberOfInfected();
			recovered+=ambulance.getNumberOfRecovered();
		}
		
		PieChart.Data dataPersons= new Data("Persons",infectedPersons);
		PieChart.Data dataRetiree= new Data("Retiree", infectedRetirre);
		PieChart.Data dataKids= new Data("Kids", infectedKids);
		
		ObservableList<PieChart.Data> listaDatas=FXCollections.observableArrayList(dataPersons,dataKids,dataRetiree);
		chartAge.setData(listaDatas);
		chartAge.setPrefSize(390, 230);
		chartAge.setLegendSide(Side.LEFT);
		
		CategoryAxis xAxis=new CategoryAxis();
		NumberAxis yAxis=new NumberAxis();
		xAxis.setLabel("Gender");
		yAxis.setLabel("NumOfInfected");
		
		XYChart.Series<String, Number> maleData=new XYChart.Series<String, Number>();
		maleData.getData().add(new XYChart.Data<String, Number>("Males", infectedMales));
		
		XYChart.Series<String, Number> femaleData=new XYChart.Series<String, Number>();
		femaleData.getData().add(new XYChart.Data<String, Number>("Feales", infectedFemales));
		
		chartGender.getData().addAll(maleData,femaleData);		
		chartGender.setPrefSize(390, 230);
		chartGender.setLegendSide(Side.LEFT);
		
		tfInfected.setText(infected.toString());
		tfRecovered.setText(recovered.toString());
		
		
		statStage.setScene(new Scene(statPane));
		statStage.show();
	}
	
	
	public void downloadStatistic() throws IOException, InterruptedException
	{
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("HH_mm");

		String dateNameString = format.format(date).toString();

		File file = new File("report_" + dateNameString + ".csv");
		file.createNewFile();

		CSVWriter csvfile = new CSVWriter(new FileWriter(file));

		String[] headerString = { "CORONA SIMULATION: ", format.format(date) };
		csvfile.writeNext(headerString);

		List<String[]> dataToWriteList = new ArrayList<String[]>();
		dataToWriteList.add(new String[] { "INFECTED: ", infected.toString() });
		dataToWriteList.add(new String[] { "RECOVERED: ", recovered.toString() });
		dataToWriteList.add(new String[] { "MALES: ", infectedMales.toString() });
		dataToWriteList.add(new String[] { "FEMALES: ", infectedFemales.toString() });
		dataToWriteList.add(new String[] { "KIDS: ", infectedKids.toString() });
		dataToWriteList.add(new String[] { "PERSONS: ", infectedPersons.toString() });
		dataToWriteList.add(new String[] { "RETIREES: ", infectedRetirre.toString() });

		csvfile.writeAll(dataToWriteList);

		csvfile.close();
		btnSaveStat.setText("Saved in project folder");
		
	}
	
	
	public void exit()
	{
		statStage.close();
	}
	
}
