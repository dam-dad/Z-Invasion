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

    private boolean isMoving() {
        return physics.isMovingX();
    }
	
	public void left() {
		
		getEntity().setScaleX(-1);
		physics.setVelocityX(-300);
	}

	public void right() {
		
		getEntity().setScaleX(1);
		physics.setVelocityX(300);
	}

	public void jump() {
		if (jumps == 0) {
            return;
		}
        physics.setVelocityY(-270);

        jumps--;
	}
	
	public void stop() {
        physics.setVelocityX(0);
    }

	
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

