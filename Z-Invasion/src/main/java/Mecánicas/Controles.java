package Mecánicas;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

import javafx.geometry.Point2D;

public class Controles extends Component {

	private PhysicsComponent physics;

	public void onUpdate(Entity entity, double tpf) {
	}

	public void left() {
		physics.setVelocityX(-150);
	}

	public void right() {
		physics.setVelocityX(150);
	}

	public void jump() {
		physics.setVelocityY(-300);
	}

	public void shoot() {
		Point2D center = entity.getCenter();
		Vec2 dir = Vec2.fromAngle(entity.getRotation() + 0);
		FXGL.spawn("bullet", new SpawnData(center.getX(), center.getY()).put("dir", dir.toPoint2D()));
	}
}
