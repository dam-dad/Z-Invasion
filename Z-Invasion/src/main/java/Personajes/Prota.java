package Personajes;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.util.Duration;

public class Prota extends Component {

	private int speed = 0;

	private PhysicsComponent physics;
	private Entity entity;
	private AnimatedTexture textura;
	private AnimationChannel animIdle, animWalk, animJump;

	public Prota() {
		animIdle = new AnimationChannel(FXGL.image("prota.png"), 4, 32, 42, Duration.seconds(1), 1, 1);
		animWalk = new AnimationChannel(FXGL.image("run.png"), 4, 32, 42, Duration.seconds(1), 0, 3);
		animJump = new AnimationChannel(FXGL.image("jump.png"), 4, 32, 42, Duration.seconds(1), 0, 3);

		textura = new AnimatedTexture(animIdle);
	}

	@Override
	public void onAdded() {
		entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
		entity.getViewComponent().addChild(textura);
	}

	@Override
	public void onUpdate(double tpf) {
		entity.translateX(speed * tpf);

		if (speed != 0) {
			if (textura.getAnimationChannel() == animIdle) {
				textura.loopAnimationChannel(animWalk);
				textura.loopAnimationChannel(animJump);
			}

			speed = (int) (speed * 0.9);

			if (FXGLMath.abs(speed) < 1) {
				speed = 0;
				textura.loopAnimationChannel(animIdle);
			}
		}
	}

	public void left() {
		speed = -150;
		physics.setVelocityX(speed);
	}

	public void right() {
		speed = 150;
		physics.setVelocityX(speed);
	}

	public void jump() {
		speed = -400;
		physics.setVelocityY(speed);
	}
}
