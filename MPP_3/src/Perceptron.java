public class Perceptron {
    String perceptronLanguage;
    double[] weights;
    double bias;

    public Perceptron(String perceptronLanguage, int weightsSize) {
        this.perceptronLanguage = perceptronLanguage;
        this.weights = new double[weightsSize];
        initializeAndNormalizeWeights();
        initializeBias();
    }

    private void initializeAndNormalizeWeights() {
        initializeWeights();
        System.out.println("Weights before normalization: ");
        for (double weight : weights) {
            System.out.println(weight);
        }
        normalizeWeights();
        System.out.println("Weights after normalization: ");
        for (double weight : weights) {
            System.out.println(weight);
        }
    }

    private void initializeWeights() {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
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
}
