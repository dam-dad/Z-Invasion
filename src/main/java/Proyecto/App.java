package Proyecto;

import static com.almasb.fxgl.dsl.FXGL.despawnWithDelay;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getDisplay;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.onCollisionOneTimeOnly;

import java.util.Arrays;
import java.util.EnumSet;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;

import Componentes.PlayerController;
import Entidades.EntityType;
import Objetos.ZFactory;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class App  extends GameApplication {

	private Entity player;
	
	SpawnData s1= new SpawnData(67, 112);
	SpawnData s2= new SpawnData(84, 821);
	SpawnData s3= new SpawnData(102, 652);
	SpawnData s4= new SpawnData(88, 715);
	SpawnData s5= new SpawnData(62, 725);
	SpawnData s6= new SpawnData(192, 51);
	SpawnData s7= new SpawnData(84, 47);
	
	private int InicioLevel = 1;
	
	@Override
	protected void initSettings(GameSettings settings) {
		settings.setWidth(1920);
		settings.setHeight(1080);
		settings.setTitle("Z-Invasion");
		settings.setVersion("0.1-SNAPSHOT");
		settings.setMenuEnabled(true);
		settings.setManualResizeEnabled(true);
		settings.setEnabledMenuItems(EnumSet.of(MenuItem.EXTRA));
		settings.getCredits().addAll(Arrays.asList(

					"Z-Invasion",
					"---Miembros del equipo---",
					"Guillermo\nDaniel\nJoel",
					"Este juego tenía como plan ser una juego inspirado en Metal Slug y Megaman"
					
				)
				);
		
	}

	public void initInput() {
		//Movimiento
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
            	getPlayer().getComponent(PlayerController.class).left();
            }

            @Override
            protected void onActionEnd() {
            	getPlayer().getComponent(PlayerController.class).stop();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
            	getPlayer().getComponent(PlayerController.class).right();
            }

            @Override
            protected void onActionEnd() {
            	getPlayer().getComponent(PlayerController.class).stop();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
            	getPlayer().getComponent(PlayerController.class).jump();
            }
        }, KeyCode.W);
		FXGL.onKeyDown(KeyCode.F, "shoot", () -> getPlayer().getComponent(PlayerController.class).shoot());
		
	}
	
	private static Entity getPlayer() {
	        return FXGL.getGameWorld().getSingleton(EntityType.PLAYER);
	    }
	  

	@Override
	protected void initGame() {

		FXGL.getGameWorld().addEntityFactory(new ZFactory());
	      
        
        FXGL.setLevelFromMap("level1.tmx");
     
  	  
  	  player= FXGL.getGameWorld().spawn("jugador", s1);
  	  
  	  /**/Viewport viewport = FXGL.getGameScene().getViewport();
		
		viewport.setBounds(-1500, 0, 3200, FXGL.getAppHeight());
		viewport.bindToEntity(player, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
		viewport.setLazy(true);

	}

	// PARTES DE FÍSICAS
	protected void initPhysics() {

		// Acciones con las monedas
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.MONEDA) {

			protected void onCollisionBegin(Entity player, Entity coin) {
				coin.removeFromWorld();
			}
		});
		
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.mensaje) {
            
			protected void onCollisionBegin(Entity player, Entity m) {
			m.getViewComponent().setOpacity(1);

            despawnWithDelay(m, Duration.seconds(4.5));
			}
		});

		 FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.PUERTA) {
			protected void onCollisionBegin(Entity player, Entity pill) {
				
				player.removeFromWorld();
	               //avanzamos de número
	                InicioLevel++;
	                if(InicioLevel>=8) {
	                	getDisplay().showMessageBox("You finished the game!");
	                }
	                
	            	switch (InicioLevel) {
	            	
	            	case 2:
	            		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
	            		Entity player2 =new Entity();
		                player2= FXGL.getGameWorld().spawn("jugador", s2);
		                FXGL.getGameScene().getViewport().bindToEntity(player2, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	            	break;
	            	case 3:	
	            		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
	            		Entity player3 =new Entity();
	            		player3= FXGL.getGameWorld().spawn("jugador", s3);
		                FXGL.getGameScene().getViewport().bindToEntity(player3, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
		                break;
	            	case 4:	
	            		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
	            		Entity player4 =new Entity();
	            		player4= FXGL.getGameWorld().spawn("jugador", s4);
		                FXGL.getGameScene().getViewport().bindToEntity(player4, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
		                break;
	            	case 5:	
	            		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
	            		Entity player5 =new Entity();
	            		player5= FXGL.getGameWorld().spawn("jugador", s5);
		                FXGL.getGameScene().getViewport().bindToEntity(player5, FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
		                break;
	            	case 6:	
	            		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
	            		Entity player6 =new Entity();
	            		player6= FXGL.getGameWorld().spawn("jugador", s6);
		                FXGL.getGameScene().getViewport().bindToEntity(player6, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
		                break;
		                
	            	case 7:	
	            		
	            		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
	            		Entity player7 =new Entity();
	            		player7= FXGL.getGameWorld().spawn("jugador", s7);
		                FXGL.getGameScene().getViewport().bindToEntity(player7, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
		                
		                break;
		                
		            
	            	}
			}
		});

		FXGL.onCollisionBegin(EntityType.BALA, EntityType.PLATAFORMA, (bullet, plataforma) -> {
			bullet.removeFromWorld();
		});

		FXGL.onCollisionBegin(EntityType.BALA, EntityType.PUERTA, (bullet, puerta) -> {
			bullet.removeFromWorld();
		});

		FXGL.onCollisionBegin(EntityType.BALA, EntityType.ENEMY, (bullet, enemy) -> {
			bullet.removeFromWorld();
			enemy.removeFromWorld();
		});

		FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.muerte, (Player, enemy) -> {
			Player.removeFromWorld();
			switch (InicioLevel) {
			case 1:
				FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player1 =new Entity();
                player1= FXGL.getGameWorld().spawn("jugador", s1);
                FXGL.getGameScene().getViewport().bindToEntity(player1, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
				break;
        	case 2:
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player2 =new Entity();
                player2= FXGL.getGameWorld().spawn("jugador", s2);
                FXGL.getGameScene().getViewport().bindToEntity(player2, FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
        	break;
        	case 3:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player3 =new Entity();
        		player3= FXGL.getGameWorld().spawn("jugador", s3);
                FXGL.getGameScene().getViewport().bindToEntity(player3, FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
                break;
        	case 4:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player4 =new Entity();
        		player4= FXGL.getGameWorld().spawn("jugador", s4);
                FXGL.getGameScene().getViewport().bindToEntity(player4,FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
                break;
        	case 5:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player5 =new Entity();
        		player5= FXGL.getGameWorld().spawn("jugador", s5);
                FXGL.getGameScene().getViewport().bindToEntity(player5, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                break;
        	case 6:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player6 =new Entity();
        		player6= FXGL.getGameWorld().spawn("jugador", s6);
                FXGL.getGameScene().getViewport().bindToEntity(player6, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                break;
        	case 7:	
        		
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player7 =new Entity();
        		player7= FXGL.getGameWorld().spawn("jugador", s7);
        		FXGL.getGameScene().getViewport().bindToEntity(player7, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                
                break;
        	}
		});
		
		
		FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.ENEMY, (Player, enemy) -> {
			Player.removeFromWorld();
			switch (InicioLevel) {
			case 1:
				FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player1 =new Entity();
                player1= FXGL.getGameWorld().spawn("jugador", s1);
                FXGL.getGameScene().getViewport().bindToEntity(player1, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
				break;
        	case 2:
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player2 =new Entity();
                player2= FXGL.getGameWorld().spawn("jugador", s2);
                FXGL.getGameScene().getViewport().bindToEntity(player2, FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
        	break;
        	case 3:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player3 =new Entity();
        		player3= FXGL.getGameWorld().spawn("jugador", s3);
                FXGL.getGameScene().getViewport().bindToEntity(player3, FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
                break;
        	case 4:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player4 =new Entity();
        		player4= FXGL.getGameWorld().spawn("jugador", s4);
                FXGL.getGameScene().getViewport().bindToEntity(player4,FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
                break;
        	case 5:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player5 =new Entity();
        		player5= FXGL.getGameWorld().spawn("jugador", s5);
                FXGL.getGameScene().getViewport().bindToEntity(player5, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                break;
        	case 6:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player6 =new Entity();
        		player6= FXGL.getGameWorld().spawn("jugador", s6);
                FXGL.getGameScene().getViewport().bindToEntity(player6, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                break;
        	case 7:	
        		
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player7 =new Entity();
        		player7= FXGL.getGameWorld().spawn("jugador", s7);
        		FXGL.getGameScene().getViewport().bindToEntity(player7, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                
                break;
        	}
		});
		
		
	}


	public static void main(String[] args) {
		launch(args);
	}

	
	
}
