package lermitage.intellij.ilovedevtoys.toolwindow.setup;

import lermitage.intellij.ilovedevtoys.tools.JSONStringTools;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JSONStringToolSetup extends AbstractToolSetup {

    private final JTextArea jsonStringJsonArea;
    private final JTextArea jsonStringStringTextArea;
    private final JButton changeOrientationButton;

    public JSONStringToolSetup(JTextArea jsonStringJsonArea,
                               JTextArea jsonStringStringTextArea,
                               JButton changeOrientationButton) {
        this.jsonStringJsonArea = jsonStringJsonArea;
        this.jsonStringStringTextArea = jsonStringStringTextArea;
        this.changeOrientationButton = changeOrientationButton;
    }

    public void setup() {
        jsonStringJsonArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                jsonStringStringTextArea.setText(JSONStringTools.jsonToString(jsonStringJsonArea.getText()));
                jsonStringStringTextArea.setCaretPosition(0);
                updateWithBestNumberOfRows(jsonStringJsonArea, jsonStringStringTextArea);
            }
        });

        jsonStringStringTextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                jsonStringJsonArea.setText(JSONStringTools.prettyPrintJson(jsonStringStringTextArea.getText()));
                jsonStringJsonArea.setCaretPosition(0);
                updateWithBestNumberOfRows(jsonStringJsonArea, jsonStringStringTextArea);
            }
        });

        changeOrientationButton.addActionListener(e -> jsonStringStringTextArea.setText("Button clicked!"));
    }
}
