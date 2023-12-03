package lermitage.intellij.ilovedevtoys.toolwindow.setup;

import com.intellij.ui.JBColor;
import com.intellij.ui.SearchTextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import lermitage.intellij.ilovedevtoys.tools.JSONStringTools;
import lermitage.intellij.ilovedevtoys.toolwindow.settings.JSONStringToolSettings;

public class JSONStringToolSetup extends AbstractToolSetup {

    private final JSplitPane jsonStringSplitPane;
    private final JTextArea jsonStringJsonArea;
    private final JTextArea jsonStringStringTextArea;
    private final JButton changeOrientationButton;
    private final SearchTextField jsonSearchField;
    private final JButton findNext;
    private final JButton findPrev;
    private final HighlightPainter defaultPainter = new DefaultHighlighter.DefaultHighlightPainter(JBColor.CYAN);
    private final HighlightPainter currentPainter = new DefaultHighlighter.DefaultHighlightPainter(JBColor.YELLOW);

    private static class HighlightRecord {
        int start;
        int end;
        Object highlight;

        private HighlightRecord(int start, int end, Object highlight) {
            this.start = start;
            this.end = end;
            this.highlight = highlight;
        }
    }

    private List<HighlightRecord> highlightRecords = new ArrayList<>();
    private int currentHighlightIndex = -1;

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
                findAndHighlightText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                findAndHighlightText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                findAndHighlightText();
            }
        });

        findNext.addActionListener(e -> findStringAction(true));
        findPrev.addActionListener(e -> findStringAction(false));
    }

    private void findStringAction(boolean forward) {
        if (highlightRecords.isEmpty())
            return;

        if (currentHighlightIndex != -1)
            repaintHighlight(currentHighlightIndex, defaultPainter);

        if (forward) {
            currentHighlightIndex++;
            if (currentHighlightIndex >= highlightRecords.size())
                currentHighlightIndex = 0;
        } else {
            currentHighlightIndex--;
            if (currentHighlightIndex == -1)
                currentHighlightIndex = highlightRecords.size() - 1;
        }

        HighlightRecord newHighlight = repaintHighlight(currentHighlightIndex, currentPainter);
        jsonStringJsonArea.select(newHighlight.start, newHighlight.end);
    }

    private HighlightRecord repaintHighlight(int index, HighlightPainter painter) {
        HighlightRecord prevHighlightRecord = highlightRecords.get(index);
        try {
            jsonStringJsonArea.getHighlighter().removeHighlight(prevHighlightRecord.highlight);
            prevHighlightRecord.highlight = jsonStringJsonArea.getHighlighter().addHighlight(
                prevHighlightRecord.start,
                prevHighlightRecord.end,
                painter
            );
        } catch (BadLocationException ignored) {
        }
        return prevHighlightRecord;
    }

    private void findAndHighlightText() {
        jsonStringJsonArea.getHighlighter().removeAllHighlights();
        highlightRecords = new ArrayList<>();
        currentHighlightIndex = -1;
        String textToSearch = jsonSearchField.getText();
        String text = jsonStringJsonArea.getText();
        if (textToSearch.isEmpty() || text.isEmpty())
            return;
        Matcher matcher = Pattern.compile(textToSearch).matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            Object highlight;
            try {
                highlight = jsonStringJsonArea
                    .getHighlighter()
                    .addHighlight(start, end, defaultPainter);
            } catch (BadLocationException ignored) {
                return;
            }
            highlightRecords.add(new HighlightRecord(start, end, highlight));
        }
    }
}
