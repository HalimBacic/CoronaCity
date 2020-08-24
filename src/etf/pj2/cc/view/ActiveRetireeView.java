package etf.pj2.cc.view;

import java.util.Random;
import etf.pj2.map.Position;
import javafx.scene.paint.Color;

public class ActiveRetireeView 	extends PersonView {

	public ActiveRetireeView(Integer fieldSize, Position position) {
		super(fieldSize, position);
		Random random = new Random();
		Integer littleMove= random.nextInt(10);
		circle.setLayoutX(position.getX() * fieldSize + 10+littleMove);
		circle.setLayoutY(position.getY() * fieldSize + 10+littleMove);
		info.setLayoutX(position.getX() * fieldSize - 115);
		info.setLayoutY(position.getY() * fieldSize - 17);
		circle.setRadius(fieldSize * 15 / 100);
		setColor(Color.YELLOW);
		circle.setFill(color);
	}


}
