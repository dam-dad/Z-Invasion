package Proyecto;

import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.despawnWithDelay;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getDisplay;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.getUIFactory;
import static com.almasb.fxgl.dsl.FXGL.getUIFactoryService;
import static com.almasb.fxgl.dsl.FXGL.getip;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.onCollisionOneTimeOnly;
import static com.almasb.fxgl.dsl.FXGL.texture;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.ui.FontType;

import Componentes.PlayerController;
import Entidades.EntityType;
import Objetos.ZFactory;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class App  extends GameApplication {

	/**
	 * Z- Invasion
	 *Dessarrollo
	 *	Joel Jesús Rosales
	 *	Guillermo Rodríguez Pérez
	 *	Daniel Plasencia Orán
	 */
	
	
	/**
	 * Instancia jugador
	 */
	private Entity player;
	
	/**
	 * Contador de muertes que lleva
	 */
	int countdead=0;
	
	/**
	 * Estos son los puntos donde se debe respaunear el prota
	 */
	SpawnData s1= new SpawnData(67, 112);
	SpawnData s2= new SpawnData(84, 821);
	SpawnData s3= new SpawnData(102, 652);
	SpawnData s4= new SpawnData(88, 715);
	SpawnData s5= new SpawnData(62, 725);
	SpawnData s6= new SpawnData(192, 51);
	SpawnData s7= new SpawnData(84, 47);
	SpawnData s9= new SpawnData(117, 428);
	SpawnData s10= new SpawnData(86, 1020);
	SpawnData s11= new SpawnData(104, 455);
	SpawnData s12= new SpawnData(104, 455);
	
	/**
	 * Estos son los puntos donde se debe respaunear el prota
	 */
	
	private int InicioLevel = 9;
	
	
	/**
	 * Valores ajustes iniciales como tamaño de ventana nombre y menu. 
	 */
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
					"Dessarrollo",
					 "*	Joel Jesús Rosales",
					 "*	Guillermo Rodríguez Pérez",
					 "*	Daniel Plasencia Orán",
					"Este juego tenía como plan ser una juego inspirado en Metal Slug y Megaman"
					
				)
				);
		
	}

	/**
	 * Recoge los valores de las teclas 
	 */
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
        
        FXGL.onKeyDown(KeyCode.G, "beableshoot", () -> getPlayer().getComponent(PlayerController.class).beableshoot());
        
        
        /**
    	 * Iniciaría la música pero no va, desconocemos el porqué 
    	 */
        FXGL.onKeyDown(KeyCode.L, () -> {
        	FXGL.play("Action_B.wav");
        });
       
	}
	
	private static Entity getPlayer() {
	        return FXGL.getGameWorld().getSingleton(EntityType.PLAYER);
	    }
	  
	
	/**
	 *Pone la puntuación
	 */
	 @Override
	    protected void initGameVars(Map<String, Object> vars) {
	       
	        //Creamos la ventana de puntuación
	    	vars.put("score", 0);
	    }
	
	 /**
		 *Valores iniciales del juego
		 */
	@Override
	protected void initGame() {
		
		FXGL.getGameWorld().addEntityFactory(new ZFactory());
	      
		FXGL.spawn("Background", new SpawnData(-100, 0).put("width", 4888).put("height", 1920));
        FXGL.setLevelFromMap("Tutorial1.tmx");
     
  	  player= FXGL.getGameWorld().spawn("jugador", s9);
  	  
  	  
  	  com.almasb.fxgl.app.scene.Viewport viewport = FXGL.getGameScene().getViewport();
		
		viewport.setBounds(-1500, 0, 4888, FXGL.getAppHeight());
		viewport.bindToEntity(player, FXGL.getAppWidth()/2 , FXGL.getAppHeight() / 2);
		viewport.setLazy(true);

	}

	
	/**
	 *Pantalla de game over
	 */
	private void gameOver(boolean reachedEndOfGame) {
        StringBuilder builder = new StringBuilder();
        builder.append("Game Over!\n\n");
       
            builder.append("Has llegado al final del juego!, (si lo sé es un final muy abierto pero que quereis, ya sacaremos expansión, o no)\n\n");
        
        builder.append("Monedas obtenidas: ")
                .append(FXGL.geti("score"))
                .append("\nVidas gastadas: ")
                .append(countdead);
        		
        FXGL.getDialogService().showMessageBox(builder.toString(), () -> FXGL.getGameController().gotoMainMenu());
    }
	
	
	/**
	 *Fisicas del mundo y coliciones
	 */
	// PARTES DE FÍSICAS
	protected void initPhysics() {

		// Acciones con las monedas
		
		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.mensaje) {
            
			protected void onCollisionBegin(Entity player, Entity m) {
			m.getViewComponent().setOpacity(1);

            despawnWithDelay(m, Duration.seconds(4.5));
			}
		});

		FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.MONEDA) {
            
			protected void onCollisionBegin(Entity player, Entity m) {
				m.removeFromWorld();

		        inc("score", +100);

		        animationBuilder()
                .duration(Duration.seconds(0.25))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .onFinished(m::removeFromWorld)
                .scale(m)
                .from(new Point2D(1, 1))
                .to(new Point2D(0, 0))
                .buildAndPlay();

        var text = getUIFactoryService().newText("+100", Color.RED, FontType.GAME, 26.0);
        text.setStrokeWidth(2.75);

        var textEntity = entityBuilder()
                .at(m.getPosition())
                .view(text)
                .buildAndAttach();

        animationBuilder()
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .onFinished(textEntity::removeFromWorld)
                .translate(textEntity)
                .from(textEntity.getPosition())
                .to(textEntity.getPosition().subtract(0, 100))
                .buildAndPlay();
		        
			}
		});
		
		
		 FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.PUERTA) {
			protected void onCollisionBegin(Entity player, Entity pill) {
				 
				player.removeFromWorld();
	               //avanzamos de número
	                InicioLevel++;
	                if(InicioLevel>=13) {
	                	 
	                	gameOver(true);
	                }
	                
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
		                gameOver(true);
		                break;
		                
	            		case 9:	
		            		
		            		FXGL.setLevelFromMap("Tutorial1.tmx");
		            		Entity player9 =new Entity();
		            		player9= FXGL.getGameWorld().spawn("jugador", s9);
			                FXGL.getGameScene().getViewport().bindToEntity(player9, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
			                
			                break;
			                
	            		case 10:	
		            		
		            		FXGL.setLevelFromMap("Tutorial2.tmx");
		            		Entity player10 =new Entity();
		            		player10= FXGL.getGameWorld().spawn("jugador", s10);
			                FXGL.getGameScene().getViewport().bindToEntity(player10, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
			                
			                break;
			                
	            		case 11:	
		            		
		            		FXGL.setLevelFromMap("Tutorial3.tmx");
		            		Entity player11 =new Entity();
		            		player11= FXGL.getGameWorld().spawn("jugador", s11);
			                FXGL.getGameScene().getViewport().bindToEntity(player11, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
			                
			                break;
			                
	            		case 12:	
		            		
		            		FXGL.setLevelFromMap("Tutorial4.tmx");
		            		Entity player12 =new Entity();
		            		player12= FXGL.getGameWorld().spawn("jugador", s12);
			                FXGL.getGameScene().getViewport().bindToEntity(player12, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
			                InicioLevel=0;
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
			countdead++;
			switch (InicioLevel) {
			case 1:
				FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player1 =new Entity();
                player1= FXGL.getGameWorld().spawn("jugador", s1);
                FXGL.getGameScene().getViewport().bindToEntity(player1, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
				break;
			case 2:
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player2 =new Entity();
                player2= FXGL.getGameWorld().spawn("jugador", s2);
                FXGL.getGameScene().getViewport().bindToEntity(player2, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
        	break;
        	case 3:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player3 =new Entity();
        		player3= FXGL.getGameWorld().spawn("jugador", s3);
                FXGL.getGameScene().getViewport().bindToEntity(player3, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
                break;
        	case 4:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player4 =new Entity();
        		player4= FXGL.getGameWorld().spawn("jugador", s4);
                FXGL.getGameScene().getViewport().bindToEntity(player4, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
                break;
        	case 5:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player5 =new Entity();
        		player5= FXGL.getGameWorld().spawn("jugador", s5);
                FXGL.getGameScene().getViewport().bindToEntity(player5, FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
                FXGL.set("score", 0);
                break;
        	case 6:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player6 =new Entity();
        		player6= FXGL.getGameWorld().spawn("jugador", s6);
                FXGL.getGameScene().getViewport().bindToEntity(player6, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
                break;
                
        	case 7:	
        		
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player7 =new Entity();
        		player7= FXGL.getGameWorld().spawn("jugador", s7);
                FXGL.getGameScene().getViewport().bindToEntity(player7, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
                break;
                
        		case 9:	
            		FXGL.setLevelFromMap("Tutorial1.tmx");
            		Entity player9 =new Entity();
            		player9= FXGL.getGameWorld().spawn("jugador", s9);
	                FXGL.getGameScene().getViewport().bindToEntity(player9, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	                FXGL.set("score", 0);
	                
	                break;
	                
        		case 10:	
            		
            		FXGL.setLevelFromMap("Tutorial2.tmx");
            		Entity player10 =new Entity();
            		player10= FXGL.getGameWorld().spawn("jugador", s10);
	                FXGL.getGameScene().getViewport().bindToEntity(player10, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	                FXGL.set("score", 0);
	                
	                break;
	                
        		case 11:	
            		
            		FXGL.setLevelFromMap("Tutorial3.tmx");
            		Entity player11 =new Entity();
            		player11= FXGL.getGameWorld().spawn("jugador", s11);
	                FXGL.getGameScene().getViewport().bindToEntity(player11, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	                FXGL.set("score", 0);
	                
	                break;
	                
        		case 12:	
            		
            		FXGL.setLevelFromMap("Tutorial4.tmx");
            		Entity player12 =new Entity();
            		player12= FXGL.getGameWorld().spawn("jugador", s12);
	                FXGL.getGameScene().getViewport().bindToEntity(player12, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	                FXGL.set("score", 0);
	                
	                break;
        	}
		});
		
		
		FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.ENEMY, (Player, enemy) -> {
			Player.removeFromWorld();
			countdead++;
			switch (InicioLevel) {
			case 1:
				FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player1 =new Entity();
                player1= FXGL.getGameWorld().spawn("jugador", s1);
                FXGL.getGameScene().getViewport().bindToEntity(player1, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
				break;
			case 2:
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player2 =new Entity();
                player2= FXGL.getGameWorld().spawn("jugador", s2);
                FXGL.getGameScene().getViewport().bindToEntity(player2, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
        	break;
        	case 3:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player3 =new Entity();
        		player3= FXGL.getGameWorld().spawn("jugador", s3);
                FXGL.getGameScene().getViewport().bindToEntity(player3, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
                break;
        	case 4:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player4 =new Entity();
        		player4= FXGL.getGameWorld().spawn("jugador", s4);
                FXGL.getGameScene().getViewport().bindToEntity(player4, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
                break;
        	case 5:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player5 =new Entity();
        		player5= FXGL.getGameWorld().spawn("jugador", s5);
                FXGL.getGameScene().getViewport().bindToEntity(player5, FXGL.getAppWidth() / 2, FXGL.getAppHeight() /2 );
                FXGL.set("score", 0);
                break;
        	case 6:	
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player6 =new Entity();
        		player6= FXGL.getGameWorld().spawn("jugador", s6);
                FXGL.getGameScene().getViewport().bindToEntity(player6, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
                break;
                
        	case 7:	
        		
        		FXGL.setLevelFromMap("level"+InicioLevel+".tmx");
        		Entity player7 =new Entity();
        		player7= FXGL.getGameWorld().spawn("jugador", s7);
                FXGL.getGameScene().getViewport().bindToEntity(player7, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
                FXGL.set("score", 0);
                
                break;
                
            
        		case 9:	
            		
            		FXGL.setLevelFromMap("Tutorial1.tmx");
            		Entity player9 =new Entity();
            		player9= FXGL.getGameWorld().spawn("jugador", s9);
	                FXGL.getGameScene().getViewport().bindToEntity(player9, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	                FXGL.set("score", 0);
	                break;
	                
        		case 10:	
            		
            		FXGL.setLevelFromMap("Tutorial2.tmx");
            		Entity player10 =new Entity();
            		player10= FXGL.getGameWorld().spawn("jugador", s10);
	                FXGL.getGameScene().getViewport().bindToEntity(player10, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	                FXGL.set("score", 0);
	                break;
	                
        		case 11:	
            		
            		FXGL.setLevelFromMap("Tutorial3.tmx");
            		Entity player11 =new Entity();
            		player11= FXGL.getGameWorld().spawn("jugador", s11);
	                FXGL.getGameScene().getViewport().bindToEntity(player11, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	                FXGL.set("score", 0);
	                break;
	                
        		case 12:	
            		
            		FXGL.setLevelFromMap("Tutorial4.tmx");
            		Entity player12 =new Entity();
            		player12= FXGL.getGameWorld().spawn("jugador", s12);
	                FXGL.getGameScene().getViewport().bindToEntity(player12, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
	               
	                break;
        	}
		});
	}
	
	/**
	 *Interfaz gráfica para la puntuación
	 */
protected void initUI() {
	
	
	var coin = texture("coin2.png", 48 * 0.75, 51 * 0.75);

    //Contador de puntos
    var scoreText = getUIFactory().newText("", Color.GOLD, 38.0);
    
    scoreText.setStrokeWidth(2.5);
    
    scoreText.setStroke(Color.color(0.0, 0.0, 0.0, 0.56));
    
    scoreText.textProperty().bind(getip("score").asString());

    //Añadimos estos elementos
    
    addUINode(coin, 130, 15);
    addUINode(scoreText, 170, 48);
    
    }

	public static void main(String[] args) {
		launch(args);
	}

	
	
}
