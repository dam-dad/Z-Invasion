package Objetos;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.texture;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.ui.FontType;

import Componentes.PlayerController;
import Componentes.enemyZombie;
import Entidades.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *Damos propiedades a los objetos con las entidades y los Spawns
 */

public class ZFactory implements EntityFactory {
	/**
	 *Creamos la plataforma con sus propiedades
	 */
	@Spawns("plataforma")
	public Entity newPlataforma(SpawnData data) {

		return FXGL.entityBuilder()
				.type(EntityType.PLATAFORMA)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
	}

	/**
	 * Creamos la moneda con sus propiedades
	 */
	@Spawns("moneda")
	public Entity newMoneda(SpawnData data) {

	     return entityBuilder()
	                .type(EntityType.MONEDA)
	                .from(data)
	                .view(texture("coin.png").toAnimatedTexture(6, Duration.seconds(0.8)).loop())
	                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
	                .with(new CollidableComponent(true))
	                .build();
	}
	/**
	 * Creamos la opcion de que el jugador muera con us propiedades
	 */
	@Spawns("muerte")
	public Entity newmuerte(SpawnData data) {

		
		return FXGL.entityBuilder().type(EntityType.muerte).from(data)
				.with(new CollidableComponent(true))
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.build();
	}

	/**
	 *Cremos el jugador con sus propiedades
	 */
	@Spawns("jugador")
	public Entity newPlayer(SpawnData data) {

		PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));

        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return 
				FXGL.entityBuilder()
				.type(EntityType.PLAYER)
                .from(data)
                //.bbox(new HitBox (BoundingShape.box(30,30)) )
                .viewWithBBox(new Rectangle(30,30, Color.BLUE))
                .with(physics)
                .with(new CollidableComponent(true))//<--Dice que es posible colicionar con el objeto, nos servir� para interctuar con objetos
                .with(new PlayerController())
                .build();
				
		
	}

	/**
	 * Crea un fondo de color negro
	 */
	@Spawns("Background")
	public Entity spawnBackground(SpawnData data) {
		 return FXGL.entityBuilder()
	                .view(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.GRAY))
	                .with(new IrremovableComponent())
	                .zIndex(-100)
	                .build();
	}

	/**
	 * Creamos la puerta con sus propiedades
	 */
	@Spawns("puerta")
	public Entity spawnPuerta(SpawnData data) {
		return FXGL.entityBuilder().type(EntityType.PUERTA).from(data)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new CollidableComponent(true))
				.build();

	}
	/**
	 *tTodo eso inicializa y crea el bojeto de la bala
	 */
	@Spawns("bullet")
	public Entity newBullet(SpawnData data) {
		Point2D dir = data.get("dir");
		return FXGL.entityBuilder().type(EntityType.BALA).from(data)
				.viewWithBBox(new Rectangle(30,30, Color.YELLOW))
				.with(new ProjectileComponent(dir, 800))
				.with(new OffscreenCleanComponent())
				.collidable()
				.build();
	}
	/**
	 *tTodo eso inicializa y crea el objeto del enemigo
	 */
	@Spawns("enemyZombie")
	public Entity newEnemyZombie(SpawnData data) {

		var e = entityBuilder()
				.type(EntityType.ENEMY)
				.from(data)
				.bbox(new HitBox(new Point2D(10, 10), BoundingShape.box(232 / 4 - 20, 390 / 4 - 20)))
				.with(new LiftComponent().xAxisDistanceDuration( (int)data.get("patrolEndx") - data.getX(),
						Duration.seconds(FXGLMath.random(1, 3))))
				.with(new enemyZombie((int)data.get("patrolEndx")))
				.with(new CollidableComponent(true)).build();

	

		return e;
	}
	/**
	 *Creamos los mensajes con sus propiedades
	 */
	@Spawns("messagePrompt")
    public Entity newMessagePrompt(SpawnData data) {
		var text = FXGL.getUIFactory().newText(data.get("message"), Color.BLACK, 20.0);
        text.setStrokeWidth(2);

        return entityBuilder()
                .type(EntityType.mensaje)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .view(text)
                .with(new CollidableComponent(true))
                .opacity(0)
                .build();
    }
	/**
	 * Dando la propiedad para que el jugador pueda reaparecer
	 */
	 @Spawns("reaparecer")
	    public Entity spawnSpawn(SpawnData data) {
	        return FXGL.entityBuilder()
	                .type(EntityType.reaparecer)
	                .from(data)
	                .at(data.getX(),data.getY())
	                .with(new CollidableComponent(true))//ponemos Collidable ya que haremos algo al entrar en contacto.
	                .build();
	        
	  }
	

}
