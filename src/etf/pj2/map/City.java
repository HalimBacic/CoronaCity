package etf.pj2.map;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;
import etf.pj2.cc.entity.*;
import etf.pj2.cc.exceptions.BadCCNumber;
import interfaces.Types;
import javafx.scene.Group;

/**
 * 
 * @author halim 
 */
@SuppressWarnings("serial")
public class City implements Types,Serializable {
	private Integer dimension;
	private Integer fieldSize;
	Field[][] matrixFields;
	private Vector<House> allHouses = new Vector<>();
	private Vector<Ambulance> allAmbulances=new Vector<>();
	Vector<Person> persons=new Vector<>();   
    Integer ambulanceNumCars;
	
	public Vector<Ambulance> getAmbulanceVector()
	{
		return getAllAmbulances();
	}
	
	public Vector<Person> getPersonVector()
	{
		return persons;
	}
	
	public City(Integer x, Integer hss, Integer cp,Integer ambulance) throws BadCCNumber {

		setFieldSize(830/x);
		ambulanceNumCars=ambulance;
		if (hss > x * x)
			throw new BadCCNumber();
		this.dimension = x;
		matrixFields = new Field[dimension][dimension];
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++)
				matrixFields[i][j] = new Field(Types.FREE, new Position(i, j));
		fillCityMap(hss, cp);
		matrixFields[0][0].setType(Types.AMBULANCE);
		matrixFields[0][dimension - 1].setType(Types.AMBULANCE);
		matrixFields[dimension - 1][0].setType(Types.AMBULANCE);
		matrixFields[dimension - 1][dimension - 1].setType(Types.AMBULANCE);
	}

	private void fillCityMap(Integer hss, Integer cp) {
		while (hss > 0 || cp > 0) {
			Random random = new Random();
			int type = random.nextInt(50);
			int column = random.nextInt(dimension - 2) + 1;
			int row = random.nextInt(dimension - 2) + 1;
			{
				if (matrixFields[column][row].getType().equals(Types.FREE)) {
					if (type % 2 == 0 && hss > 0) {
						matrixFields[column][row].setType(HOUSE);
						hss--;
						getAllHouses().add(new House(column, row));
					} else if (type % 2 == 1 && cp > 0) {
						matrixFields[column][row].setType(CONTROL_POINT);
						for(int i=column-1;i<2+column;i++)
							for(int j=row-1;j<2+row;j++)
								matrixFields[i][j].setControlPoint(true);
						cp--;
					}
				}
			}
		}
	}

	public Ambulance setAmbulance(Integer population,Position pos,Integer cars,Group groupcity)
	{		
		Ambulance ambulance=new Ambulance(population, pos, getDimension(), cars, getFieldSize());
		getAllAmbulances().add(ambulance);
        return ambulance;
	}

	public Integer getDimension() {
		return dimension;
	}

	public void setDimension(Integer x) {
		this.dimension = x;
	}

	public void setHouseCarantine(Integer houseID) {
		for (House house : getAllHouses()) {
			if (house.getID().equals(houseID)) {
				getAllHouses().elementAt(getAllHouses().indexOf(house)).setStatus(false);
			}
		}
	}

	public synchronized Field getField(Integer i, Integer j) {
		if(i<0) i=0;
		if(j<0) j=0;
		if(i>getDimension()-1) i=getDimension()-1;
		if(j>getDimension()-1) i=getDimension()-1;
		return this.matrixFields[i][j];
	}

	public Field getField(Position p) {
		return getField(p.getX(), p.getY());
	}

	public Integer getFieldSize() {
		return fieldSize;
	}

	public void setFieldSize(Integer fieldSize) {
		this.fieldSize = fieldSize;
	}

	public Field[][] returnFieldMatrix() {
		return matrixFields;
	}

	public Vector<House> getAllHouses() {
		return allHouses;
	}

	public void setAllHouses(Vector<House> allHouses) {
		this.allHouses = allHouses;
	}

	public Vector<Ambulance> getAllAmbulances() {
		return allAmbulances;
	}

	public void setAllAmbulances(Vector<Ambulance> allAmbulances) {
		this.allAmbulances = allAmbulances;
	}
}
