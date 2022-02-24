package Objetos;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
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

public class ZFactory implements EntityFactory {

	// En el factory creamos los objetos que utilizaremos en el juego

	// añadimos Spawns con el nombre del objeto dado en el Tiled Map Editor
	@Spawns("plataforma")
	public Entity newPlataforma(SpawnData data) {

		return FXGL.entityBuilder()
				.type(EntityType.PLATAFORMA)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
	}

	@Spawns("moneda")
	public Entity newMoneda(SpawnData data) {

		// En este caso creamos el objeto Moneda con forma circular y
		// de color dorado

		return FXGL.entityBuilder().type(EntityType.MONEDA).from(data).with(new CollidableComponent(true))// <--Dice que																						// objeto
				.viewWithBBox(new Circle(data.<Integer>get("width") / 2, Color.GOLD)).build();
	}
	
	@Spawns("muerte")
	public Entity newmuerte(SpawnData data) {

		// En este caso creamos el objeto Moneda con forma circular y
		// de color dorado

		return FXGL.entityBuilder().type(EntityType.muerte).from(data)
				.with(new CollidableComponent(true))// <--Dice que																						// objeto
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.build();
	}
	
	
	
	@Spawns("jugador")
	public Entity newPlayer(SpawnData data) {

		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);

	       physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));
	       physics.setFixtureDef(new FixtureDef().friction(0.0f));

		return/* FXGL.entityBuilder()
				.type(EntityType.PLAYER)
				.viewWithBBox("assets/texture/player.png")
				.from(data)
				.bbox(new HitBox(new Point2D(10,25), BoundingShape.box(10, 17)))
				.with(physics)
				.with(new CollidableComponent(true))
				.with(new PlayerController())
				.build();*/
				
				
				FXGL.entityBuilder()
				.type(EntityType.PLAYER)
                .from(data)
                //.bbox(new HitBox (BoundingShape.box(30,30)) )
                .viewWithBBox(new Rectangle(30,30, Color.BLUE))
                .with(physics)
                .with(new CollidableComponent(true))//<--Dice que es posible colicionar con el objeto, nos servirá para interctuar con objetos
                .with(new PlayerController())
                .build();
				
				
				/*FXGL.entityBuilder().type(EntityType.PLAYER).from(data)
				// .bbox(new HitBox (BoundingShape.box(30,30)) )
				.viewWithBBox("assets/texture/Idle000.png").with(physics).with(new CollidableComponent(true))// <--Dice
				// que es posible colicionar con el objeto, nos servirá para interctuar con objetos
				.with(new PlayerController()).build();*/
		
	}

	
	
	// Crea un fondo de color negro
	@Spawns("Background")
	public Entity spawnBackground(SpawnData data) {
		return FXGL.entityBuilder().view("background.png").with(new IrremovableComponent()).zIndex(-10).scale(0, 0)
				.build();
	}

	// Creamos la puerta con sus propiedades
	@Spawns("puerta")
	public Entity spawnPuerta(SpawnData data) {
		return FXGL.entityBuilder().type(EntityType.PUERTA).from(data)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new CollidableComponent(true))
				.build();

	}

	@Spawns("bullet")
	public Entity newBullet(SpawnData data) {
		Point2D dir = data.get("dir");
		return FXGL.entityBuilder().type(EntityType.BALA).from(data).viewWithBBox("assets/textures/bullet.png")
				.with(new ProjectileComponent(dir, 800)).with(new OffscreenCleanComponent()).collidable().build();
	}

	@Spawns("enemyZombie")
	public Entity newEnemyZombie(SpawnData data) {
		/*int patrolEndX = data.get("patrolEndX");*/

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
	
	 @Spawns("reaparecer")
	    public Entity spawnSpawn(SpawnData data) {
	        return FXGL.entityBuilder()
	                .type(EntityType.reaparecer)
	                .from(data)
	                .at(data.getX(),data.getY())
	                .with(new CollidableComponent(true))//ponemos Collidable ya que haremos algo al entrar en contacto.
	                .build();
	        
	  }
	

//	@Spawns("enemy")
//	public Entity newEnemy(SpawnData data) {
//
//		// Creamos el componente físico de forma dinámica, ya que haremos
//		// cambios en este
//
//		PhysicsComponent physics = new PhysicsComponent();
//		physics.setBodyType(BodyType.DYNAMIC);
//
//		// Creamos el jugador
//		// con el objeto fisicas para que reaccione a las coliciones
//		// le añadimos la clase PlayerControl para que tenga sus métodos
//
//		return FXGL.entityBuilder().type(EntityType.PLAYER).from(data)
//				// .bbox(new HitBox (BoundingShape.box(30,30)) )
//				.viewWithBBox("zombie.png").with(physics).with(new CollidableComponent(true))// <--Dice
//				// que
//				// es
//				// posible
//				// colicionar
//				// con
//				// el
//				// objeto,
//				// nos
//				// servirá
//				// para
//				// interctuar
//				// con
//				// objetos
//				.with(new PlayerControl()).build();
//	}
}