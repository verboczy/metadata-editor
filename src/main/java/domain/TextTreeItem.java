package domain;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TextTreeItem extends HBox {

    private final Label text;

    public TextTreeItem(Label text) {
        super();

        this.text = text;
        this.getChildren().add(text);
    }

    public String getText() {
        return text.getText();
    }
}