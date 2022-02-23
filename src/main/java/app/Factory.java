package app;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.getUIFactory;
import static com.almasb.fxgl.dsl.FXGL.getUIFactoryService;
import static com.almasb.fxgl.dsl.FXGL.texture;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.components.EffectComponent;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.OffscreenPauseComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.input.view.KeyView;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.ui.FontType;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import test.components.Enemigos;
import test.components.PlayerComponent;

public class Factory implements EntityFactory {

   /* @Spawns("background")
    public Entity newBackground(SpawnData data) {
        int index = data.get("index");
        int speed = 11 - index;

        return entityBuilder()
                //.view(new ScrollingBackgroundView(texture("background/bg_" + index + ".png", getAppWidth(), getAppHeight()), 0.05 * speed))
                .zIndex(-1)
                .with(new IrremovableComponent())
                .build();
    }*/

    @Spawns("plataforma")
    public Entity newPlatform(SpawnData data) {
        return entityBuilder()
                .type(MEntitytype.plataforma)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .build();
    }



    @Spawns("salida")
    public Entity newDoorBot(SpawnData data) {
        return entityBuilder()
                .type(MEntitytype.salida)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .opacity(0)
                .with(new CollidableComponent(false))
                .build();
    }

    @Spawns("personaje")
    public Entity newPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));

        // this avoids player sticking to walls
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        return entityBuilder()
                .type(MEntitytype.personaje)
                .from(data)
                //.bbox(new HitBox(new Point2D(5,5), BoundingShape.circle(12)))
                .bbox(new HitBox(new Point2D(10,25), BoundingShape.box(10, 17)))
                .viewWithBBox(new Rectangle(30,30, Color.BLUE))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(new PlayerComponent())
                .build();
    }

 


    @Spawns("mensaje1")
    public Entity newMessagePrompt(SpawnData data) {
        var text = getUIFactoryService().newText(data.get("message"), Color.WHITE, FontType.GAME, 20.0);
        text.setStrokeWidth(2);

        return entityBuilder()
                .type(MEntitytype.mensaje1)
                .from(data)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .view(text)
                .with(new CollidableComponent(true))
                .opacity(0)
                .build();
    }


    

    @Spawns("moneda")
    public Entity newCoin(SpawnData data) {
        return entityBuilder()
                .type(MEntitytype.moneda)
                .from(data)
                .view(texture("coin.png").toAnimatedTexture(6, Duration.seconds(0.8)).loop())
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new CollidableComponent(true))
                .build();
    }
    
    @Spawns("bullet")
	public Entity newBullet(SpawnData data) {
		Point2D dir = data.get("dir");
		return entityBuilder().type(MEntitytype.BALA).from(data).viewWithBBox("bullet.png")
				.with(new ProjectileComponent(dir, 200)).with(new OffscreenCleanComponent()).collidable().build();
	}


    /* ENEMIES */
    @Spawns("enemyZombie")
    public Entity newEnemyZombie(SpawnData data) {
        int patrolEndX = data.get("patrolEndX");

        var e = entityBuilder()
                .type(MEntitytype.enemigoNormal)
                .from(data)
                .bbox(new HitBox(new Point2D(10, 20), BoundingShape.box(232 / 4 - 20, 390 / 4 - 20)))
                .with(new LiftComponent().xAxisDistanceDuration(patrolEndX - data.getX(), Duration.seconds(FXGLMath.random(1, 3))))
                .with(new Enemigos(patrolEndX))
                .with(new CollidableComponent(true))
                .build();

        // fix zombie's height
        e.setOnActive(() -> e.translateY(-25));

        return e;
    }

}

