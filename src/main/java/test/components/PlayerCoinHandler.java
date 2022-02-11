package test.components;

import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.getUIFactoryService;
import static com.almasb.fxgl.dsl.FXGL.inc;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.ui.FontType;

import app.MEntitytype;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class PlayerCoinHandler extends CollisionHandler {

    public PlayerCoinHandler() {
        super(MEntitytype.personaje, MEntitytype.moneda);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity coin) {
        coin.removeComponent(CollidableComponent.class);

        inc("score", +100);

        animationBuilder()
                .duration(Duration.seconds(0.25))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .onFinished(coin::removeFromWorld)
                .scale(coin)
                .from(new Point2D(1, 1))
                .to(new Point2D(0, 0))
                .buildAndPlay();

        var text = getUIFactoryService().newText("+100", Color.RED, FontType.GAME, 26.0);
        text.setStrokeWidth(2.75);

        var textEntity = entityBuilder()
                .at(coin.getPosition())
                .view(text)
                .buildAndAttach();

        animationBuilder()
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .onFinished(textEntity::removeFromWorld)
                .translate(textEntity)
                .from(textEntity.getPosition())
                .to(textEntity.getPosition().subtract(0, 100))
                .buildAndPlay();
    }
}
