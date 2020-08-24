package etf.pj2.cc.simulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import etf.pj2.cc.controllers.ActiveKidControl;
import etf.pj2.cc.controllers.ActivePersonControl;
import etf.pj2.cc.controllers.ActiveRetireeControl;
import etf.pj2.cc.controllers.AmbulanceControl;
import etf.pj2.cc.controllers.PersonControl;
import etf.pj2.cc.entity.Ambulance;
import etf.pj2.cc.entity.Person;
import etf.pj2.cc.entity.Watchman;
import etf.pj2.cc.exceptions.BadCCAlgorithm;
import etf.pj2.cc.exceptions.BadCCNumber;
import etf.pj2.cc.exceptions.BadChildsNumber;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.cc.view.ActiveKidView;
import etf.pj2.cc.view.ActivePersonView;
import etf.pj2.cc.view.ActiveRetireeView;
import etf.pj2.cc.view.AmbulanceView;
import etf.pj2.map.City;
import etf.pj2.map.House;
import etf.pj2.map.Position;
import interfaces.Locations;
import interfaces.Types;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

@SuppressWarnings("serial")
public class StartSimulation implements Types,Locations,Serializable {

	Vector<Thread> threadVector=new Vector<>();
	Group groupCity=new Group();
	private City city;
	private Watchman watchman=new Watchman();
	private Vector<PersonControl> personControls=new Vector<PersonControl>();
	private Vector<AmbulanceControl> ambulanceControls=new Vector<AmbulanceControl>();
	Integer size;
	Integer screen;
    Stage stage = new Stage();
	
	public StartSimulation(Integer screen,Integer size,Integer houses,Integer persons,Integer retirees,Integer childs,Integer controls,Integer ambulances)
			throws IOException, BadCCNumber, BadChildsNumber
	{
		this.screen=screen;
		this.size=size;
		if(houses+controls>size*size)
			throw new BadCCNumber();
		if((persons==0 && retirees==0) && childs>0)
			throw new BadChildsNumber();
		if(houses < 0 | persons < 0 | retirees< 0 | childs < 0 | ambulances < 1)
			throw new BadCCNumber("Unešeni brojevi ne smiju biti negativni!");
		
		city=new City(size, houses, controls,ambulances);
		Integer population=childs+retirees+persons;	
		
		makeCityAlgorithm(population, persons, retirees,childs,ambulances);
		
		File informationFile=new File("information.txt");
		BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(informationFile));
		String informationString=new String("Size: "+size.toString()+"\nHouses: "+houses.toString()+
				    "\nKids: "+childs.toString()+"\nPersons: "+persons.toString()+"\nRetirees: "+retirees.toString()+
				    "\nControls: "+controls.toString());
		bufferedWriter.write(informationString);
		bufferedWriter.close();	
	}
	
	private void makeCityAlgorithm(Integer population,Integer persons,Integer retirees,Integer childs,Integer ambulances) {
		setPersons(persons);
		setRetirees(retirees);
		try {
			setKids(childs);
		} catch (BadCCAlgorithm e) {
			CCLogger.cclogger(StartSimulation.class.getName(), Level.SEVERE, e);
		}	
		setAmbulance(population,groupCity,ambulances);
	}
	
	
	public void setPersons(Integer number) {
		while (number > 0) {
			Random random = new Random();
			Integer genderType = random.nextInt(10);
			Integer nameNum = random.nextInt(49);
			Integer houseNum = random.nextInt(city.getAllHouses().size());

			String name = null;

			if (genderType % 2 == 0) {
				try {
					name = loadName(Locations.MALES, nameNum);
					if (city.getAllHouses().elementAt(houseNum).isEmpty()) {
						setPersonToEmptyHouse(random, houseNum, name, MALE);
					} else {
						if (city.getAllHouses().elementAt(houseNum).getOwnerSurname().getGender().equals(FEMALE)) {
							setPersonWithOpositeGender(random, houseNum, name, MALE);
						} else {
							setPersonWithRommie(random, houseNum, name, MALE);
						}
					}
				} catch (IOException e) {
					CCLogger.cclogger(City.class.getName(), Level.WARNING, e);
				}
			}
			// FEMALE
			else {
				try {
					name = loadName(Locations.FEMALES, nameNum);
					if (city.getAllHouses().elementAt(houseNum).isEmpty()) {

						setPersonToEmptyHouse(random, houseNum, name, FEMALE);

					} else {
						if (city.getAllHouses().elementAt(houseNum).getOwnerSurname().getGender().equals(MALE)) {
							setPersonWithOpositeGender(random, houseNum, name, FEMALE);
						} else {
							setPersonWithRommie(random, houseNum, name, FEMALE);
						}
					}
				} catch (IOException e) {
					CCLogger.cclogger(City.class.getName(), Level.WARNING, e);
				}
			}
			number--;
		}
	}

	public void setRetirees(Integer numofRetiree) {
		while (numofRetiree > 0) {
			Random random = new Random();
			Integer genderType = random.nextInt(10);
			Integer nameNum = random.nextInt(49);
			Integer houseNum = random.nextInt(city.getAllHouses().size());

			String name = null;
			// MALE
			try {
				if (genderType % 2 == 0) {
					name = loadName(Locations.MALES, nameNum);
					addRetireeToHouse(random, houseNum, name, MALE);
				} else {

					name = loadName(Locations.FEMALES, nameNum);
					addRetireeToHouse(random, houseNum, name, FEMALE);
				}
			} catch (IOException e) {
				CCLogger.cclogger(City.class.getName(), Level.WARNING, e);
			}
			numofRetiree--;
		}
	}
	
	public void addRetireeToHouse(Random random, Integer houseNum, String name, String gender) throws IOException {
		String surname = loadName(Locations.SURNAMES, random.nextInt(49));
		Person person = new Person(random.nextInt(8999) + 1000, name, surname, random.nextInt(47) + 64,gender);
		person.setCity(city);
		person.setHouseID(city.getAllHouses().elementAt(houseNum));
		if (city.getAllHouses().elementAt(houseNum).isEmpty()) {
			city.getAllHouses().elementAt(houseNum).setOwnerSurname(person);
			city.getAllHouses().elementAt(houseNum).setCanHaveChild(true);
			person.setHouseID(city.getAllHouses().elementAt(houseNum));
		} else {
			person.setHouseID(city.getAllHouses().elementAt(houseNum));
		}
		ActiveRetireeView retireeView= new ActiveRetireeView(city.getFieldSize(), person.getPosition());
		ActiveRetireeControl retireeControl=new ActiveRetireeControl(retireeView, person);
		personControls.add(retireeControl);  
	}

	public boolean checkHousesForChilds()
	{
		for (House house : city.getAllHouses()) {
			if(house.isCanHaveChild())
				return true;
		}
		return false;
	}
	public void setKids(Integer numOfkids) throws BadCCAlgorithm {
		while (numOfkids > 0) {
			if(numOfkids>0 && !checkHousesForChilds())
				throw new BadCCAlgorithm("Pokrenite simulaciju ponovo!");
			Random random = new Random();
			Integer genderType = random.nextInt(10);
			Integer nameNum = random.nextInt(49);
			Integer houseNum = random.nextInt(city.getAllHouses().size());

			String name = null;
			try {
				if (genderType % 2 == 0) {
					name = loadName(Locations.MALES, nameNum);
				} else {
					name = loadName(Locations.FEMALES, nameNum);
				}
			} catch (IOException e) {
				CCLogger.cclogger(City.class.getName(), Level.WARNING, e);
			}
			if (city.getAllHouses().elementAt(houseNum).isCanHaveChild()) {
				String surname = city.getAllHouses().elementAt(houseNum).getOwnerSurname().getSurname();
				Person person = new Person(random.nextInt(8999) + 1000, name, surname, random.nextInt(15) + 3, KID);
				person.setCity(city);
				person.setHouseID(city.getAllHouses().elementAt(houseNum));
				ActiveKidView activeKidView= new ActiveKidView(city.getFieldSize(), person.getPosition());
				ActiveKidControl activeKidControl=new ActiveKidControl(activeKidView, person);
				personControls.add(activeKidControl);
				numOfkids--;
			}
			
		}
	}

	public void setPersonToEmptyHouse(Random random, Integer houseNum, String name, String gender) throws IOException {
		String surname = loadName(Locations.SURNAMES, random.nextInt(49));
		Person person = new Person(random.nextInt(8999) + 1000, name, surname, random.nextInt(47) + 18, gender);
		person.setCity(city);
		person.setHouseID(city.getAllHouses().elementAt(houseNum));
		city.getAllHouses().elementAt(houseNum).setOwnerSurname(person);
		city.getAllHouses().elementAt(houseNum).setEmpty(false);
		
		ActivePersonView activePersonView=new ActivePersonView(city.getFieldSize(), person.getPosition());
		ActivePersonControl activePersonControl=new ActivePersonControl(activePersonView, person);
		personControls.add(activePersonControl); 
	}

	public void setPersonWithOpositeGender(Random random, Integer houseNum, String name, String gender) {
		String surname = city.getAllHouses().elementAt(houseNum).getOwnerSurname().getSurname();
		Person person = new Person(random.nextInt(8999) + 1000, name, surname, random.nextInt(47) + 18,gender);
		person.setCity(city);
		person.setHouseID(city.getAllHouses().elementAt(houseNum));

		ActivePersonView activePersonView=new ActivePersonView(city.getFieldSize(), person.getPosition());
		ActivePersonControl activePersonControl=new ActivePersonControl(activePersonView, person);
		personControls.add(activePersonControl);  
		city.getAllHouses().elementAt(houseNum).setCanHaveChild(true);
		city.getAllHouses().elementAt(houseNum).setCoOwner(person);
	}

	public void setPersonWithRommie(Random random, Integer houseNum, String name, String gender) throws IOException {
		String surname = loadName(Locations.SURNAMES, random.nextInt(49));
		Person person = new Person(random.nextInt(8999) + 1000, name, surname, random.nextInt(47) + 18,Types.MALE);
		person.setCity(city);
		person.setHouseID(city.getAllHouses().elementAt(houseNum));
        
		ActivePersonView activePersonView=new ActivePersonView(city.getFieldSize(), person.getPosition());
		ActivePersonControl activePersonControl=new ActivePersonControl(activePersonView, person);
		personControls.add(activePersonControl);  
	}

	public String loadName(String path, Integer nameNum) throws IOException {
		BufferedReader nameFile = new BufferedReader(new FileReader(new File(path)));
		for (int i = 0; i < nameNum; i++)
			nameFile.readLine();
		String name = nameFile.readLine();
		nameFile.close();
		return name;
	}
		
	public void setAmbulance(Integer population,Group groupcity,Integer ambulanceNumCars)
	{		
		Integer[] params= {0,0,0,0};
		
		Integer p=ambulanceNumCars/4;
		
		for(int i=0;i<4;i++)
			params[i]+=p;
	
		
		Integer controlPopulation=population%4;
		switch(controlPopulation)
		{
		case 0:
		  break;
		case 1:
			params[0]++; break;
		case 2:
			params[0]++;params[1]++; break;
		case 3:
			params[0]++; params[1]++; params[2]++; break;	
		}
			
		Integer index=0;
		Integer step=city.getDimension()-1;
		for(int i=0;i<2;i++)
		  for(int j=0;j<2;j++)
		  {
			  Ambulance ambulance=new Ambulance(population, new Position(i*step, j*step), city.getDimension(), params[index], city.getFieldSize());
			  AmbulanceView ambulanceView=new AmbulanceView(groupcity);
			  AmbulanceControl ambulanceControl=new AmbulanceControl(ambulance, ambulanceView,personControls);
			  ambulanceControls.add(ambulanceControl);
			  city.getAllAmbulances().add(ambulance);
			  index++;
		  }
		System.out.println("");
	}

	public static void setImage(String path,Integer x,Integer y,Integer size,Group group) throws FileNotFoundException
	{
		File file=new File(path);
		if(!file.exists()) {
			
		   throw new FileNotFoundException();	
		}
		else {
		FileInputStream imageStream=new FileInputStream(file);
	    Image image=new Image(imageStream,size,size,true,true);
	    ImageView imageView=new ImageView(image);
	    imageView.setX(x);
	    imageView.setY(y);
	    group.getChildren().add(imageView);
		}
    }
	
	
	public void renderCity()
	{
		
		 //830 je vertikalna duzina mog ekrana
		//TODO dodati rezoluciju za izbor
		Integer fieldsize=((screen-70)/size);

		for(int i=0;i<this.size;i++)
			for(int j=0;j<this.size;j++)
			{

				Rectangle fieldRectangle=new Rectangle(i*fieldsize, j*fieldsize, fieldsize, fieldsize);
				if(city.getField(i, j).getType().equals(Types.AMBULANCE))
				{
				    String path=Locations.AMBULANCE;
				    try {
				    setImage(path, i*fieldsize, j*fieldsize, fieldsize, groupCity);
				    }
				    catch(FileNotFoundException ex) {
					CCLogger.cclogger(StartSimulation.class.getName(), Level.WARNING, ex);
				    }
				}
				else if(city.getField(i, j).getType().equals(Types.HOUSE))
				{
					Random random=new Random();
					Integer numInteger = random.nextInt(10);
					if(numInteger%2==0)
					{
				    String path=Locations.HOUSE1;
				    try {
						setImage(path, i*fieldsize, j*fieldsize, fieldsize, groupCity);
					} catch (FileNotFoundException e) {
						CCLogger.cclogger(StartSimulation.class.getName(), Level.WARNING, e);
					}
					}
					else
					{
						String path=Locations.HOUSE2;
					    try {
							setImage(path, i*fieldsize, j*fieldsize, fieldsize, groupCity);
						} catch (FileNotFoundException e) {
							CCLogger.cclogger(StartSimulation.class.getName(), Level.WARNING, e);
						}
					}
				}
				else if(city.getField(i, j).getType().equals(Types.CONTROL_POINT))
				{
					String path=Locations.CONTROL;
				    try {
						setImage(path, i*fieldsize, j*fieldsize, fieldsize, groupCity);
					} catch (FileNotFoundException e) {
						CCLogger.cclogger(StartSimulation.class.getName(), Level.WARNING, e);
					}
				    
				}
				else if(city.getField(i, j).getType().equals(Types.FREE))
				{
					//TODO Ovdje dodavati npr drvo ili nešto slično
					fieldRectangle.setFill(Color.GRAY);
					fieldRectangle.setStroke(Color.DARKGRAY);
					fieldRectangle.setStrokeWidth(1);
					groupCity.getChildren().add(fieldRectangle);
				}
			}	
	}
	
	
	public void setGfxContent()
	{		
		for (PersonControl personControl : personControls) {
			groupCity.getChildren().add(personControl.getPersonView().addToGroup());
		}
	}
	
	
	
	
	public void start() throws IOException, InterruptedException 
	{

		stage.setTitle("Corona City Simulator");

        renderCity();

        setGfxContent();
		
		FXMLLoader loader=new FXMLLoader(getClass().getResource("ControlGUI.fxml"));
		Parent watchBtns=loader.load();
		ControlerControlGUI controller=loader.getController();
		watchman.setControler(controller, city, personControls, ambulanceControls, stage, groupCity);
		
		for (AmbulanceControl ambulanceControl : ambulanceControls) {
			ambulanceControl.getAmbulanceView().setController(controller);
		}
				
		watchBtns.setLayoutX(840);
		watchBtns.setLayoutY(100);
		groupCity.getChildren().add(watchBtns);
		
		groupCity.setLayoutX(10);
		groupCity.setLayoutY(10);

		
		Scene sceneCity=new Scene(groupCity);
		sceneCity.getStylesheets().add(getClass().getResource("Graphics.css").toString());
		stage.setWidth(1400);
		stage.setHeight(870);
		stage.setScene(sceneCity);
		watchman.start();
		stage.show(); 		
	}
}
