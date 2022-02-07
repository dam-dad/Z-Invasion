package Personajes;

import com.almasb.fxgl.dsl.FXGL;
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

import Mecánicas.EntidadTipo;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Mundo implements EntityFactory {

	@Spawns("prota")
	private Entity newPlayer(SpawnData data) {
		PhysicsComponent physics = new PhysicsComponent();
		physics.setBodyType(BodyType.DYNAMIC);

		return FXGL.entityBuilder().type(EntidadTipo.PLAYER).from(data).with(physics)
				.with(new CollidableComponent(true)).with(new Prota()).build();
	}

	@Spawns("platform")
	public Entity newPlataforma(SpawnData data) {
		return FXGL.entityBuilder().type(EntidadTipo.PLATAFORMA).from(data)
				.bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
				.with(new PhysicsComponent()).build();
	}

	@Spawns("background")
	public Entity spawnBackground(SpawnData data) {
		return FXGL.entityBuilder()
				.view(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.BLACK))
				.with(new IrremovableComponent()).zIndex(-100).build();
	}
}
