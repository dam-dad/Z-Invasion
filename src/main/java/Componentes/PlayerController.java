package Componentes;

import static com.almasb.fxgl.dsl.FXGL.image;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import Entidades.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 *Propiedades del personaje principal
 */

public class PlayerController extends Component {

	private PhysicsComponent physics;

	 int jumps = 2;

	boolean shoot=false;
	
	
	public PlayerController() {
	
    }
	/**
	 *En este método hace que el máximo salto sea 2 y el jugador vaya al suelo
	 */
    public void onAdded() {
        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                jumps = 2;
            }
        });
    }
	
    @Override
    public void onUpdate(double tpf) {
       
    }
	/**
	 *En este método se encarga de que el jugador se mueva
	 */
    private boolean isMoving() {
        return physics.isMovingX();
    }
	/**
	 *En este método se encarga de que el jugador vaya por la izquierda
	 */
	public void left() {
		
		getEntity().setScaleX(-1);
		physics.setVelocityX(-300);
	}
	/**
	 *En este método se encarga de que el jugador vaya por la derecha
	 */
	public void right() {
		
		getEntity().setScaleX(1);
		physics.setVelocityX(300);
	}
	/**
	 *En este método se encarga de que el jugador pueda saltar
	 */
	public void jump() {
		if (jumps == 0) {
            return;
		}
        physics.setVelocityY(-270);

        jumps--;
	}
	/**
	 *En este método se encarga de que el jugador pueda detenerse
	 */
	public void stop() {
        physics.setVelocityX(0);
    }

	/**
	 *En estos método hace que sea posible que el jugador pueda disparar
	 */
	public void shoot() {
		if(shoot==true) {
			Point2D center = entity.getCenter();
			Vec2 dir = Vec2.fromAngle(entity.getRotation() + 0);
			FXGL.spawn("bullet", new SpawnData(center.getX(), center.getY()).put("dir", dir.toPoint2D()));
		}else {
			
		}
	}
	public void beableshoot() {
		shoot=true;
	}
}


