package test.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;


public class Enemigos extends Component {

    private AnimatedTexture texture;
    private AnimationChannel animWalk;
    private int patrolEndx;
    private boolean goingRight = true;

    private LocalTimer timer;
    private Duration duration;
    private Double distancia;
    private double speed;

    public Enemigos(int patrolEndx){
        this.patrolEndx=patrolEndx;

        duration = Duration.seconds(2);

        int h = 390 / 4;
        int w = 1392 / 4;

        animWalk = new AnimationChannel(FXGL.image("enemies/zombie/zombie1.png", w, h), 6, 232 / 4, h, Duration.seconds(0.75),0, 5);

        texture = new AnimatedTexture(animWalk);
        texture.loop();
    }

    @Override
    public void onAdded(){
        distancia = patrolEndx - entity.getX();
        timer = FXGL.newLocalTimer();
        timer.capture();
        speed = distancia / duration.toSeconds();

        entity.getTransformComponent().setScaleOrigin(new Point2D(232 / 4 / 2, 390 / 4 / 2));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        if (timer.elapsed(duration)) {
            goingRight = !goingRight;
            timer.capture();
        }
        entity.translateY(goingRight ? speed * tpf : -speed * tpf);
        entity.setScaleX(goingRight ? 1 : -1);
    }
}
