package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.ann.FFANN;
import hr.fer.zemris.optjava.dz7.ann.SigmoidTransferFunction;
import hr.fer.zemris.optjava.dz7.ann.TransferFunction;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FFANNTest {

    @Test
    public void test1() throws IOException {
        Path file = Paths.get("07-iris-formatirano.data");
        Dataset dataset = Dataset.load(file);

        FFANN ann = new FFANN(new int[] {4, 5, 3, 3},
                new TransferFunction[] {new SigmoidTransferFunction(), new SigmoidTransferFunction(), new SigmoidTransferFunction()},
                dataset);
        assertEquals(55, ann.getWeightsCount());
        double[] weights = new double[55];
        Arrays.fill(weights, 0.1);

        assertEquals(0.8365366587431725, ann.calculateError(weights), 1e-8);
    }

    @Test
    public void test2() throws IOException {
        Path file = Paths.get("07-iris-formatirano.data");
        Dataset dataset = Dataset.load(file);

        FFANN ann = new FFANN(new int[] {4, 3, 3},
                new TransferFunction[] {new SigmoidTransferFunction(), new SigmoidTransferFunction()},
                dataset);
        assertEquals(27, ann.getWeightsCount());
        double[] weights = new double[27];
        Arrays.fill(weights, -0.2);

        assertEquals(0.7019685477806382, ann.calculateError(weights), 1e-8);
    }



}
