package lermitage.intellij.ilovedevtoys.toolwindow.setup;

import lermitage.intellij.ilovedevtoys.tools.JSONStringTools;
import lermitage.intellij.ilovedevtoys.toolwindow.options.JSONStringToolOptions;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JSONStringToolSetup extends AbstractToolSetup {

    private final JSplitPane jsonStringSplitPane;
    private final JTextArea jsonStringJsonArea;
    private final JTextArea jsonStringStringTextArea;
    private final JButton changeOrientationButton;

    public JSONStringToolSetup(
        JSplitPane jsonStringSplitPane,
        JTextArea jsonStringJsonArea,
        JTextArea jsonStringStringTextArea,
        JButton changeOrientationButton
    ) {
        this.jsonStringSplitPane = jsonStringSplitPane;
        this.jsonStringJsonArea = jsonStringJsonArea;
        this.jsonStringStringTextArea = jsonStringStringTextArea;
        this.changeOrientationButton = changeOrientationButton;
    }

    public void setup() {
        JSONStringToolOptions settings = JSONStringToolOptions.getInstance();
        jsonStringSplitPane.setOrientation(settings.DIVIDER_ORIENTATION);
        jsonStringSplitPane.setDividerLocation(settings.DIVIDER_LOCATION);

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

        jsonStringSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY , evt -> {
            settings.DIVIDER_LOCATION = (int) evt.getNewValue();
        });

        changeOrientationButton.addActionListener(e -> {
            float JsonAreaPercentage = (float) jsonStringSplitPane.getDividerLocation() /
                (jsonStringSplitPane.getMaximumDividerLocation() - jsonStringSplitPane.getMinimumDividerLocation());
            int dividerOrientation = jsonStringSplitPane.getOrientation() ^ 1;
            jsonStringSplitPane.setOrientation(dividerOrientation);
            settings.DIVIDER_ORIENTATION = dividerOrientation;
            int dividerLocation = Math.round(
                (jsonStringSplitPane.getMaximumDividerLocation() - jsonStringSplitPane.getMinimumDividerLocation()) * JsonAreaPercentage
            );
            jsonStringSplitPane.setDividerLocation(dividerLocation);
        });
    }
}
