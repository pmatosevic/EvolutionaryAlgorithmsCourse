package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz2.function.Function1;
import hr.fer.zemris.optjava.dz2.function.Function2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Jednostavno {

    public static void main(String[] args) {
        String option = args[0];
        int maxIterations = Integer.parseInt(args[1]);

        Matrix startingPoint = new Matrix(2, 1);
        if (args.length >= 4) {
            startingPoint.set(0, 0, Double.parseDouble(args[2]));
            startingPoint.set(1, 0, Double.parseDouble(args[3]));
        } else {
            startingPoint.set(0, 0, -5);
            startingPoint.set(1, 0, -5);
        }

        IHFunction function;
        if (option.startsWith("1")) function = new Function1();
        else if (option.startsWith("2")) function = new Function2();
        else throw new IllegalArgumentException("Illegal function choice.");

        List<Matrix> path = new ArrayList<>();
        Matrix result;
        switch (option.charAt(1)) {
            case 'a':
                result = NumOptAlgorithms.gradientDescent(function, maxIterations, startingPoint, m -> path.add(m.copy()));
                break;
            case 'b':
                result = NumOptAlgorithms.newtonMethod(function, maxIterations, startingPoint, m -> path.add(m.copy()));
                break;
            default:
                throw new IllegalArgumentException("Illegal algorithm choice.");
        }

        System.out.printf("Result: (%f, %f)%n", result.get(0, 0), result.get(1, 0));
        System.out.println("Path:");
        System.out.println("{" + path.stream().map(Jednostavno::pointToStr).collect(Collectors.joining(", ")) + "}");
    }

    private static String pointToStr(Matrix m) {
        return "(" + m.get(0, 0) + ", " + m.get(1, 0) + ")";
    }

}
