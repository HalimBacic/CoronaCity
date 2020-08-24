package etf.pj2.cc.view;

import java.util.Random;
import etf.pj2.map.Position;
import javafx.scene.paint.Color;

public class ActiveKidView extends PersonView {

	public ActiveKidView(Integer fieldSize, Position position) {
		super(fieldSize, position);
		Random random = new Random();
		Integer littleMove= random.nextInt(10);
		circle.setLayoutX(position.getX() * fieldSize + 32+littleMove);
		circle.setLayoutY(position.getY() * fieldSize + 32+littleMove);
		circle.setRadius(fieldSize * 12 / 100);
		setColor(Color.LIGHTPINK);
		circle.setFill(color);
	}

}
