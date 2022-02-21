package Proyecto;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;

import Mecánicas.Controles;
import Mecánicas.EntidadTipo;
import Personajes.Mundo;
import javafx.scene.input.KeyCode;

public class App extends GameApplication {

	private Entity player;

	/* private static final int SPEED = 150; */
	private static final int HEIGHT = 16 * 15;
	private static final int WIDTH = 16 * 35;

	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(1066);
		settings.setHeightFromRatio(16 / 9.0);
	}

	public void initInput() {
		FXGL.getInput().addAction(new UserAction("Left") {

			@Override
			protected void onAction() {
				player.getComponent(Controles.class).left();
			}
		}, KeyCode.A);

		FXGL.getInput().addAction(new UserAction("Right") {

			@Override
			protected void onAction() {
				player.getComponent(Controles.class).right();
			}
		}, KeyCode.D);

		FXGL.getInput().addAction(new UserAction("Jump") {

			@Override
			protected void onAction() {
				player.getComponent(Controles.class).jump();
			}
		}, KeyCode.W);

		FXGL.onKeyDown(KeyCode.F, "shoot", () -> player.getComponent(Controles.class).shoot());
	}

	@Override
	protected void initGame() {

		// FUNCIONA ENSEÑA ESCENA
		FXGL.getGameWorld().addEntityFactory(new Mundo());

		FXGL.spawn("background", new SpawnData(0, 0).put("width", WIDTH).put("height", HEIGHT));
		FXGL.setLevelFromMap("Level01.tmx");

		// añade al personaje en una parte del mapa: x y
		player = FXGL.getGameWorld().spawn("prota", 50, 50);

		FXGL.getGameScene().getViewport().setBounds(-1500, 0, 1500, (15 * 70));
		FXGL.getGameScene().getViewport().bindToEntity(player, (5 * 70) / 2, (5 * 70) / 2);

	}

	protected void initPhysics() {

		// Acciones con las monedas
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntidadTipo.PLAYER, EntidadTipo.MONEDA) {

			protected void onCollisionBegin(Entity player, Entity coin) {
				coin.removeFromWorld();// al colicionar con la moneda la quita del mapa
			}
		});

		// Acciones con la puerta
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntidadTipo.PLAYER, EntidadTipo.PUERTA) {

			protected void onCollisionBegin(Entity player, Entity puerta) {
				FXGL.getDisplay().showMessageBox("Nivel completado", () -> {
					System.out.println("Dialog closed");
				});
			}
		});

		FXGL.onCollisionBegin(EntidadTipo.BALA, EntidadTipo.PLATAFORMA, (bullet, plataforma) -> {
			bullet.removeFromWorld();
		});

		FXGL.onCollisionBegin(EntidadTipo.BALA, EntidadTipo.PUERTA, (bullet, puerta) -> {
			bullet.removeFromWorld();
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
