package proyecto;


import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.view.KeyView;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Menu extends FXGLMenu {

	private ObjectProperty<ChronButton> selectedButton;

	public Menu() {
		super(MenuType.MAIN_MENU);

		ChronButton btnPlayGame = new ChronButton("Nueva Partida", "Crear una nueva partida", () -> fireNewGame());
		ChronButton btnOptions = new ChronButton("Opciones", "Ajustes del juego", () -> {
		});
		ChronButton btnExit = new ChronButton("Salir del juego", "Volver al escritorio", () -> fireExit());

		selectedButton = new SimpleObjectProperty<>(btnPlayGame);

		var textDescription = FXGL.getUIFactory().newText("", Color.LIGHTGRAY, 14.0);
		textDescription.textProperty()
				.bind(Bindings.createStringBinding(() -> selectedButton.get().description, selectedButton));

		var vbox = new VBox(15, btnPlayGame,
				new ChronButton("Cargar partida", "Cargar el último punto de guardado", () -> fireLoad(null)),
				btnOptions, btnExit, new LineSeparator(), textDescription);
		vbox.setAlignment(Pos.CENTER_LEFT);
		vbox.setTranslateX(100);
		vbox.setTranslateY(430);

		var view = new KeyView(KeyCode.ESCAPE, Color.BLUE, 24.0);

		var hbox = new HBox(25, FXGL.getUIFactory().newText("Atras", 14.0), view);
		hbox.setTranslateX(FXGL.getAppWidth() - 150);
		hbox.setTranslateY(580);

		getContentRoot().getChildren().addAll(vbox, hbox);
	}

	@Override
	protected Button createActionButton(String arg0, Runnable arg1) {
		return new Button();
	}

	@Override
	protected Button createActionButton(StringBinding arg0, Runnable arg1) {
		return new Button();
	}

	@Override
	protected Node createBackground(double arg0, double arg1) {
		return FXGL.texture("background.png");
	}

	@Override
	protected Node createProfileView(String arg0) {
		return new Rectangle();
	}

	@Override
	protected Node createTitleView(String arg0) {
		return new Rectangle();
	}

	@Override
	protected Node createVersionView(String arg0) {
		return new Rectangle();
	}

	private static final Color SELECCIONADO_COLOR = Color.WHITE;
	private static final Color NO_SELECCIONADO_COLOR = Color.GRAY;

	private class ChronButton extends StackPane {
		private String name;
		private String description;
		private Runnable action;

		private Text text;

		public ChronButton(String name, String description, Runnable action) {
			this.name = name;
			this.description = description;
			this.action = action;

			text = FXGL.getUIFactory().newText(name, Color.WHITE, 15.0);

			text.fillProperty()
					.bind(Bindings.when(focusedProperty()).then(SELECCIONADO_COLOR).otherwise(NO_SELECCIONADO_COLOR));

			focusedProperty().addListener((o, ov, nv) -> {
				if (nv) {
					selectedButton.setValue(this);
				}
			});

			setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.ENTER) {
					action.run();
				}
			});

			setAlignment(Pos.CENTER_LEFT);

			getChildren().addAll(text);
		}
	}

	private static class LineSeparator extends Parent {
		private Rectangle line = new Rectangle(400, 2);

		public LineSeparator() {
			var gradient = new LinearGradient(0, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE),
					new Stop(0.5, Color.GRAY), new Stop(1.0, Color.TRANSPARENT));

			line.setFill(gradient);

			getChildren().add(line);
		}
	}
}
