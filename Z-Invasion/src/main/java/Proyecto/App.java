package Proyecto;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;

import Personajes.Mundo;

public class App extends GameApplication {

	private Entity player;

	private static final int SPEED = 150;
	private static final int HEIGHT = 16 * 15;
	private static final int WIDTH = 16 * 35;

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(16 * 35);
		settings.setHeight(16 * 15);
	}

	@Override
	protected void initGame() {
		FXGL.getGameWorld().addEntityFactory(new Mundo());
		FXGL.spawn("background", new SpawnData(0, 0).put("width", WIDTH).put("height", HEIGHT));
		FXGL.setLevelFromMap("zombieW1.tmx");
		player = FXGL.getGameWorld().spawn("prota", 50, 50);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
