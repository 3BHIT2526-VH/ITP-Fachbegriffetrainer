package util;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class HangmanVisualizer {
    private final JTextArea displayArea;
    private final char[][] grid = new char[10][20]; // Größeres Gitter
    private final List<int[]> fullPath = new ArrayList<>();
    private int lastDrawnIndex = 0;

    public HangmanVisualizer(JTextArea displayArea) {
        this.displayArea = displayArea;
        this.displayArea.setFont(new Font("Monospaced", Font.BOLD, 24));
        reset();
        definePaths();
    }

    public void reset() {
        for (char[] row : grid) Arrays.fill(row, ' ');
        lastDrawnIndex = 0;
        render();
    }

    private void definePaths() {
        fullPath.clear();
        // Boden (Zeile 8)
        for (int x = 4; x <= 14; x++) fullPath.add(new int[]{8, x, '='});
        // Balken hoch (Zeile 7 bis 1)
        for (int y = 7; y >= 1; y--) fullPath.add(new int[]{y, 5, '|'});
        // Diagonale Stütze
        fullPath.add(new int[]{2, 6, '/'});
        // Balken oben quer
        for (int x = 6; x <= 12; x++) fullPath.add(new int[]{1, x, '-'});
        // Seil
        fullPath.add(new int[]{2, 12, '|'});
    }

    public void updateVisual(int errors, int maxErrors) {
        if (errors <= 0) {
            reset();
            return;
        }

        int bodySteps = 4;
        int galgenStepsAvailable = maxErrors - bodySteps;

        List<int[]> targetElements = new ArrayList<>();

        // 1. Galgen-Teil berechnen
        int currentGalgenSteps = Math.min(errors, galgenStepsAvailable);
        double ratio = (double) fullPath.size() / galgenStepsAvailable;
        int charsToShow = (int) (currentGalgenSteps * ratio);
        for (int i = 0; i < charsToShow; i++) targetElements.add(fullPath.get(i));

        // 2. Körper-Teil berechnen
        if (errors > galgenStepsAvailable) {
            int bodyPart = errors - galgenStepsAvailable;
            if (bodyPart >= 1) targetElements.add(new int[]{3, 12, 'O'}); // Kopf
            if (bodyPart >= 2) targetElements.add(new int[]{4, 12, '|'}); // Torso
            if (bodyPart >= 3) { // Arme
                targetElements.add(new int[]{4, 11, '/'});
                targetElements.add(new int[]{4, 13, '\\'});
            }
            if (bodyPart >= 4) { // Beine
                targetElements.add(new int[]{5, 11, '/'});
                targetElements.add(new int[]{5, 13, '\\'});
            }
        }

        animateNewElements(targetElements);
    }

    private void animateNewElements(List<int[]> targetElements) {
        if (lastDrawnIndex >= targetElements.size()) return;

        javax.swing.Timer timer = new javax.swing.Timer(50, null);
        timer.addActionListener(e -> {
            if (lastDrawnIndex < targetElements.size()) {
                int[] el = targetElements.get(lastDrawnIndex);
                grid[el[0]][el[1]] = (char) el[2];
                render();
                lastDrawnIndex++;
            } else {
                timer.stop();
            }
        });
        timer.start();
    }

    private void render() {
        StringBuilder sb = new StringBuilder();
        for (char[] row : grid) {
            sb.append("        ");
            sb.append(new String(row)).append("\n");
        }
        displayArea.setText(sb.toString());
    }
}