package app;

import static com.almasb.fxgl.dsl.FXGL.addUINode;
import static com.almasb.fxgl.dsl.FXGL.despawnWithDelay;
import static com.almasb.fxgl.dsl.FXGL.despawnWithScale;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getDisplay;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getInput;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.getUIFactory;
import static com.almasb.fxgl.dsl.FXGL.getbp;
import static com.almasb.fxgl.dsl.FXGL.geti;
import static com.almasb.fxgl.dsl.FXGL.getip;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.onCollisionOneTimeOnly;
import static com.almasb.fxgl.dsl.FXGL.runOnce;
import static com.almasb.fxgl.dsl.FXGL.set;
import static com.almasb.fxgl.dsl.FXGL.setLevelFromMap;
import static com.almasb.fxgl.dsl.FXGL.spawn;
import static com.almasb.fxgl.dsl.FXGL.spawnWithScale;
import static com.almasb.fxgl.dsl.FXGL.texture;

import java.io.File;
import java.util.Map;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;

import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import test.components.PlayerCoinHandler;
import test.components.PlayerComponent;
import ui.LevelEndScene;


public class App  extends GameApplication{

	//Creamos al jugador
    private Entity player;
  //Nivel donde comenzará
    private static final int STARTING_LEVEL = 0;
    //muestra mensajes
    private static final boolean SHOW_BG = false;
    
    //seleccionamos que es su primera vez que juega
    private boolean firstTime = true;
    
    //Creamos la variable que saltará cuando termine el nivel
    private LevelEndScene levelEndScene;
	
	@Override
	protected void initSettings(GameSettings settings) {

		settings.setWidth(1280);
        settings.setHeight(720);
        settings.setSceneFactory(new SceneFactory() {
			@Override
			public FXGLMenu newMainMenu() {
				return new Menu();
			}
		});
		
	}
	
	 @Override
	    protected void initInput() {
	    	//recogemos input
	    	
	    	//Movimiento
	        getInput().addAction(new UserAction("Left") {
	            @Override
	            protected void onAction() {
	                player.getComponent(PlayerComponent.class).left();
	            }

	            @Override
	            protected void onActionEnd() {
	                player.getComponent(PlayerComponent.class).stop();
	            }
	        }, KeyCode.A);

	        getInput().addAction(new UserAction("Right") {
	            @Override
	            protected void onAction() {
	                player.getComponent(PlayerComponent.class).right();
	            }

	            @Override
	            protected void onActionEnd() {
	                player.getComponent(PlayerComponent.class).stop();
	            }
	        }, KeyCode.D);

	        getInput().addAction(new UserAction("Jump") {
	            @Override
	            protected void onActionBegin() {
	                player.getComponent(PlayerComponent.class).jump();
	            }
	        }, KeyCode.W);
	
	
	 }
	
	 @Override
	    protected void initGameVars(Map<String, Object> vars) {
	        
	    	//Valores inciales a empezar el juego
	    	
	    	//Seleccionnamos el nivel 0 debido a que es su primera vez
	    	vars.put("level", STARTING_LEVEL);
	        
	    	//Creamos el contador empezando en 0.0
	    	vars.put("levelTime", 0.0);
	    	
	        //Creamos la ventana de puntuación
	    	vars.put("score", 0);
	        
	    }
	 
	
	
	 //Devuelve al jugador al nivel donde estaba
    public void onPlayerDied() {
        setLevel(geti("level"));
    }

    //cambia de nivel 
    private void setLevel(int levelNum) {
        int cont =0;
    	//situamos al jugador
    	if (player != null) {
    		
    		//Siyuamos al personaje en la posicion deseada
    		player.getComponent(PhysicsComponent.class).overwritePosition(new Point2D(50, 50));
            player.setZ(Integer.MAX_VALUE);
        }
    	
    	//reiniciamos el cronometro
        set("levelTime", 0.0);

        
        //Obtenemos el mapa
       
        /**/
        String levelFile = "/assets/levels.tmx/Level0.tmx";

        Level level;


            try {
                level = new TMXLevelLoader().load(TMXLevelLoader.class.getResource(levelFile).toURI(), getGameWorld());
               
                getGameWorld().setLevel(level);

                System.out.println("Success");

            } catch (Exception e) {
                level = setLevelFromMap("level" + levelNum  + ".tmx");
            }
        
            level = setLevelFromMap("level" + levelNum  + ".tmx");
        

        var shortestTime = level.getProperties().getDouble("star1time");

        var levelTimeData = new ui.LevelEndScene.LevelTimeData(shortestTime * 2.4, shortestTime*1.3, shortestTime);

        set("levelTimeData", levelTimeData);
    }
    
    private void nextLevel() {
    	
    	//comprobamos si se llega al maximo
        if (geti("level") == 6) {
            getDisplay().showMessageBox("You finished the game!");
            return;
        }else {
        	//Cambiamos de nivel, avanzando
        setLevel(geti("level"));
        }

    }
    
    
    @Override
    protected void initGame() {
    	
    	//Comprueba si es la primera vez que juega
        if (firstTime) {
            levelEndScene = new LevelEndScene();
            firstTime = false;
        }

        //Obtenemos el factory y tdods sus elementos
        getGameWorld().addEntityFactory(new Factory());

        //asignamos null a jugador
        player = null;
        
        nextLevel();

        // debemos spawnear al jugador despues de next level, de lo contrario, el jugador es eliminado
        //antes de que la marca de actualización agregue al jugador al mundo del juego 
        player = getGameWorld().spawn("player", 50, 50);
        
        set("player", player);

       
        //Este elemento es la camara 
        Viewport viewport = getGameScene().getViewport();
        //Ponemos sus dimensiones
        viewport.setBounds(-1500, 0, 250 * 70, getAppHeight());
        //hacemos que siga al personaje
        viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);

        viewport.setLazy(true);
    }
    
    @Override
    protected void initPhysics() {
    	//añade gravedad al juego
        getPhysicsWorld().setGravity(0, 760);
        
        //Añadimos las coliciones del jugador con moneda
        getPhysicsWorld().addCollisionHandler(new PlayerCoinHandler());
        
        //Crea coliciones para la puerta de salida
        onCollisionOneTimeOnly(MEntitytype.personaje, MEntitytype.salida, (player, door) -> {
            levelEndScene.onLevelFinish();

            // lo anterior se ejecuta en su propia escena, por lo que el desvanecimiento
            //esperará hasta el usuario sale de esa escena
            getGameScene().getViewport().fade(() -> {
                nextLevel();
            });
        });

        //Crea Mensajes que luego desaparecen, Esto es util en el turorial
        onCollisionOneTimeOnly(MEntitytype.personaje, MEntitytype.mensaje1, (player, prompt) -> {
            prompt.getViewComponent().setOpacity(1);

            despawnWithDelay(prompt, Duration.seconds(4.5));
        });


        //Coliciones con los enemigos
        onCollisionBegin(MEntitytype.personaje, MEntitytype.enemigoNormal, (player, enemy) -> {
            
        	player.getComponent(PlayerComponent.class).onHit(enemy);

        });

        
    }
    
    
    
 protected void initUI() {
    	

        //Textura de contador monedas
        var coin = texture("texture/coin.png", 48 * 0.75, 51 * 0.75);

        //Contador de puntos
        var scoreText = getUIFactory().newText("", Color.GOLD, 38.0);
        
        scoreText.setStrokeWidth(2.5);
        
        scoreText.setStroke(Color.color(0.0, 0.0, 0.0, 0.56));
        
        scoreText.textProperty().bind(getip("score").asString());

        addUINode(coin, 130, 15);
        addUINode(scoreText, 170, 48);

        // TODO: add convenience methods to map game world coord to UI coord
        //var line = new Line();
        //line.startXProperty().bind(player.xProperty());
    }

    @Override
    protected void onUpdate(double tpf) {
        inc("levelTime", tpf);

        if (player.getY() > getAppHeight()) {
            onPlayerDied();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
	
}
