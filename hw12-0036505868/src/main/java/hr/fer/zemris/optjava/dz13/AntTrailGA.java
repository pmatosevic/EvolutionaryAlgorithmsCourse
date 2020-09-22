package hr.fer.zemris.optjava.dz13;

import hr.fer.zemris.optjava.dz13.algorithm.Ant;
import hr.fer.zemris.optjava.dz13.algorithm.Chromosome;
import hr.fer.zemris.optjava.dz13.algorithm.GPAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AntTrailGA extends JFrame {

    private static final int MAX_ACTIONS = 600;
    private Map map;
    private List<AntState> passedStates;
    int currentIndex = 0;

    SolutionDisplay display;

    public AntTrailGA(Map map, List<AntState> passedStates) {
        this.map = map;
        this.passedStates = passedStates;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(640, 660);
        setTitle("Prikaz rješenja");

        initGUI();
    }


    private void initGUI() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new FlowLayout());
        JButton step = new JButton("Korak");
        step.addActionListener(e -> {
            if (currentIndex >= passedStates.size()) {
                JOptionPane.showMessageDialog(null, "Kraj dostignut.");
                return;
            }

            display.drawState(passedStates.get(currentIndex));
            currentIndex++;
        });
        JButton reset = new JButton("Početak");
        reset.addActionListener(e -> {
            currentIndex = 0;
            display.resetMap(map.duplicate());
            display.drawState(passedStates.get(currentIndex));
            currentIndex++;
        });
        panel.add(step);
        panel.add(reset);

        display = new SolutionDisplay(map.duplicate());

        cp.add(panel, BorderLayout.NORTH);
        cp.add(display, BorderLayout.CENTER);

        display.drawState(null);
        reset.doClick();
    }


    public static void main(String[] args) throws IOException {
        Path mapPath = Paths.get(args[0]);
        int maxIter = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        double minFitness = Double.parseDouble(args[3]);
        Path solutionFile = Paths.get(args[4]);

        Map mainMap = new Map(loadMap(mapPath));

        GPAlgorithm algorithm = new GPAlgorithm(populationSize, MAX_ACTIONS, mainMap);
        Chromosome best = algorithm.run(maxIter, minFitness);
        String solStr = best.getRoot().toString();

        System.out.println("Best fitness: " + best.getFitness());
        System.out.println("Solution: " + solStr);
        Files.writeString(solutionFile, solStr);

        System.out.println("Starting simulation of the best solution.");

        SwingUtilities.invokeLater(() -> {
            AntTrailGA frame = new AntTrailGA(mainMap, simulate(mainMap, best));
            frame.setVisible(true);
        });

    }


    private static List<AntState> simulate(Map map, Chromosome c) {
        Ant ant = new Ant(MAX_ACTIONS, map.duplicate());
        try {
            while (true) {
                c.getRoot().execute(ant);
            }
        } catch (IllegalStateException ignored) {}
        return ant.getStates();
    }

    public static boolean[][] loadMap(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String[] dimensions = reader.readLine().split("x");
            int rows = Integer.parseInt(dimensions[0].trim());
            int cols = Integer.parseInt(dimensions[1].trim());

            boolean[][] map = new boolean[rows][cols];
            for (int i = 0; i < rows; i++) {
                String line = reader.readLine();
                for (int j = 0; j < cols; j++) {
                    map[i][j] = line.charAt(j) == '1';
                }
            }

            return map;
        }
    }

}
