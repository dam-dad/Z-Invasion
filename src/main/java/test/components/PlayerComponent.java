package test.components;

import static com.almasb.fxgl.dsl.FXGL.image;
import static com.almasb.fxgl.dsl.FXGL.runOnce;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import app.App;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;


public class PlayerComponent extends Component {

    private PhysicsComponent physics;
    

    private AnimatedTexture texture;

    private AnimationChannel animIdle, animWalk;

    private int jumps = 2;

    private boolean isBeingDamaged = false;

    /**/public PlayerComponent() {


        Image image = image("player.png");

        animIdle = new AnimationChannel(image, 4, 32, 42, Duration.seconds(1), 1, 1);
        animWalk = new AnimationChannel(image, 4, 32, 42, Duration.seconds(0.66), 0, 3);

        texture = new AnimatedTexture(animIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if (isOnGround) {
                //play("land.wav");
                jumps = 2;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if (isMoving()) {
            if (texture.getAnimationChannel() != animWalk) {
                texture.loopAnimationChannel(animWalk);
            }
        } else {
            if (texture.getAnimationChannel() != animIdle) {
                texture.loopAnimationChannel(animIdle);
            }
        }
    }

    private boolean isMoving() {
        return physics.isMovingX();
    }

    public void left() {
        

        getEntity().setScaleX(-1);
        physics.setVelocityX(-170);
    }

    public void right() {
       

        getEntity().setScaleX(1);
        physics.setVelocityX(170);
    }

    public void stop() {
        
        physics.setVelocityX(0);
    }

    public void jump() {
      

        if (jumps == 0)
            return;

        //play("jump.wav");
        physics.setVelocityY(-400);

        jumps--;
    }

    public void onHit(Entity attacker) {
        
            FXGL.<App>getAppCast().onPlayerDied();
      
    }

    
}