import java.util.Arrays;

public class Perceptron {
    String perceptronLanguage;
    double[] weights;
    double bias;
    double learningRate;

    public Perceptron(String perceptronLanguage, int weightsSize, double learningRate) {
        this.perceptronLanguage = perceptronLanguage;
        this.weights = new double[weightsSize];
        this.learningRate = learningRate;
        initializeAndNormalizeWeights();
        initializeBias();
    }

    private void initializeAndNormalizeWeights() {
        initializeWeights();
        /*System.out.println("Weights before normalization: ");
        for (double weight : weights) {
            System.out.println(weight);
        }*/
        normalizeWeights();
        /*System.out.println("Weights after normalization: ");
        for (double weight : weights) {
            System.out.println(weight);
        }*/
    }

    private void initializeWeights() {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
            System.out.println("Weight " + i + ": " + weights[i]);
        }
    }

    private void normalizeWeights() {
        double sum = 0;
        for (double weight : weights) {
            sum += weight * weight;
        }
        double magnitude = Math.sqrt(sum);
        for (int i = 0; i < weights.length; i++) {
            weights[i] /= magnitude;
        }
    }

    private void initializeBias() {
        bias = Math.random() * 2 - 1;
    }

    public int guess(Vector vector) {
        double sum = calculateSum(vector);
        return sum > bias ? 1 : 0;
    }

    public double continuousGuess(Vector vector) {
        double sum = calculateSum(vector);
        return 1 / (1 + Math.exp(-sum)); // sigmoid function
    }

    private double calculateSum(Vector vector) {
        double[] inputVectorValues = vector.getLettersMap().values().stream().mapToDouble(Double::doubleValue).toArray();
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += inputVectorValues[i] * weights[i];
        }
        return sum;
    }

    public void train(Vector vector, int guess) {
        int target = guess == 1 ? 0 : 1;
        double error = target - guess;
        adjustWeightsAndBias(vector, error);
    }

    private void adjustWeightsAndBias(Vector vector, double error) {
        double[] inputs = vector.getLettersMap().values().stream().mapToDouble(Double::doubleValue).toArray();
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * error * inputs[i];
        }
        bias += learningRate * error * -1;
    }

    @Override
    public String toString() {
        return "Perceptron{" +
                "perceptronLanguage='" + perceptronLanguage + '\'' +
                ", weights=" + Arrays.toString(weights) +
                ", bias=" + bias +
                '}';
    }
}
