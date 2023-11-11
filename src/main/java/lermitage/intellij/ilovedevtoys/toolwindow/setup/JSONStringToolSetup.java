package lermitage.intellij.ilovedevtoys.toolwindow.setup;

import com.intellij.ui.JBColor;
import com.intellij.ui.SearchTextField;
import lermitage.intellij.ilovedevtoys.tools.JSONStringTools;
import lermitage.intellij.ilovedevtoys.toolwindow.settings.JSONStringToolSettings;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class JSONStringToolSetup extends AbstractToolSetup {

    private final JSplitPane jsonStringSplitPane;
    private final JTextArea jsonStringJsonArea;
    private final JTextArea jsonStringStringTextArea;
    private final JButton changeOrientationButton;
    private final SearchTextField jsonSearchField;
    private final JButton findNext;
    private final JButton findPrev;
    private final Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(JBColor.CYAN);

    public JSONStringToolSetup(
        JSplitPane jsonStringSplitPane,
        JTextArea jsonStringJsonArea,
        JTextArea jsonStringStringTextArea,
        JButton changeOrientationButton,
        SearchTextField jsonSearchField,
        JButton findNext,
        JButton findPrev) {
        this.jsonStringSplitPane = jsonStringSplitPane;
        this.jsonStringJsonArea = jsonStringJsonArea;
        this.jsonStringStringTextArea = jsonStringStringTextArea;
        this.changeOrientationButton = changeOrientationButton;
        this.jsonSearchField = jsonSearchField;
        this.findNext = findNext;
        this.findPrev = findPrev;
    }

    public void setup() {
        JSONStringToolSettings settings = JSONStringToolSettings.getInstance();
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

        jsonStringSplitPane.addPropertyChangeListener(
            JSplitPane.DIVIDER_LOCATION_PROPERTY,
            evt -> settings.DIVIDER_LOCATION = (int) evt.getNewValue()
        );

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

        jsonSearchField.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                highlightSearchedText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                highlightSearchedText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                highlightSearchedText();
            }
        });

        findNext.addActionListener(e -> System.out.println("find next"));
        findPrev.addActionListener(e -> System.out.println("find prev"));
    }

    private void highlightSearchedText() {
        jsonStringJsonArea.getHighlighter().removeAllHighlights();
        String textToSearch = jsonSearchField.getText();
        String text = jsonStringJsonArea.getText();
        if (textToSearch.isEmpty() || text.isEmpty()) return;
        Matcher matcher = Pattern.compile(textToSearch).matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            try {
                jsonStringJsonArea
                    .getHighlighter()
                    .addHighlight(start, end, painter);
            } catch (BadLocationException ignored) {
                continue;
            }
            jsonStringJsonArea.select(start, end);
        }
    }
}
