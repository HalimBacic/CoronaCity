package etf.pj2.cc.view;

import java.util.Random;
import etf.pj2.map.Position;
import javafx.scene.paint.Color;

public class ActivePersonView extends PersonView {

	public ActivePersonView(Integer fieldSize, Position position) {
		super(fieldSize, position);
		Random random = new Random();
		Integer littleMove= random.nextInt(10);
		circle.setLayoutX(position.getX() * fieldSize +22+littleMove);
		circle.setLayoutY(position.getY() * fieldSize + 22+littleMove);
		circle.setRadius(fieldSize * 17 / 100);
		setColor(Color.DARKSEAGREEN);
		circle.setFill(color);
	}
	
}
