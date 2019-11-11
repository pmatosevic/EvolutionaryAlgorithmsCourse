package hr.fer.zemris.trisat;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TriSatParser {

    private int vars;
    private int clausesNum;
    private List<Clause> clauses = new ArrayList<>();
    private SATFormula satFormula;

    public TriSatParser(BufferedReader reader) throws IOException {
        parseFormula(reader);
    }

    public SATFormula getSATFormula() {
        return satFormula;
    }

    private void parseFormula(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine().trim();
            if (line == null || line.startsWith("%")) break;
            if (line.startsWith("c")) continue;

            if (line.startsWith("p")) {
                String[] parts = line.split("\\s+");
                vars = Integer.parseInt(parts[2].trim());
                clausesNum = Integer.parseInt(parts[3].trim());
            } else {
                String[] parts = line.split("\\s+0\\s+");
                for (String part : parts) {
                    if (part.isBlank()) continue;
                    clauses.add(parseClause(part.trim()));
                }
            }
        }

        satFormula = new SATFormula(vars, clauses.toArray(new Clause[0]));
    }

    private Clause parseClause(String line) {
        String[] parts = line.split("\\s+");
        int[] indexes = new int[parts.length - 1];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = Integer.parseInt(parts[i]);
        }
        return new Clause(indexes);
    }

}
