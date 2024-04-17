import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static double learningRate = 0.1;
    static int epochs = 1;

    static List<Perceptron> perceptrons;
    static List<List<Vector>> trainingVectorsList;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        List<String> languagesList = FileHandler.getLanguages("Languages");
        System.out.println("Available languages: ");
        System.out.println("----------------------");
        for (String language : languagesList) {
            System.out.println(language);
        }
        trainingVectorsList = FileHandler.createTrainingVectors(languagesList, "Languages");
        System.out.println("------------------------");
        System.out.println("Training vectors created.");
        System.out.println("------------------------");

        perceptrons = createPerceptrons(trainingVectorsList, learningRate);

        trainPerceptrons(perceptrons, trainingVectorsList, epochs);

        SwingUtilities.invokeLater(LanguageGuesserGui::new);
    }


    public static List<Perceptron> createPerceptrons(List<List<Vector>> trainingVectorsList, double learningRate) {
        List<Perceptron> perceptrons = new ArrayList<>();

        for (List<Vector> trainingVectors : trainingVectorsList) {
            String perceptronLanguage = trainingVectors.getFirst().getVectorLanguage();
            int weightsSize = trainingVectors.getFirst().getLettersMap().size();
            Perceptron perceptron = new Perceptron(perceptronLanguage, weightsSize, learningRate);
            perceptrons.add(perceptron);
        }
        System.out.println("Perceptrons created.");
        for (Perceptron perceptron : perceptrons) {
            System.out.println(perceptron.perceptronLanguage);
        }
        System.out.println("------------------------");
        System.out.println("Creating perceptrons...");
        System.out.println(perceptrons);
        System.out.println("------------------------");

        return perceptrons;

    }

    public static void trainPerceptrons(List<Perceptron> perceptrons, List<List<Vector>> trainingVectorsList, int epochs) {
        System.out.println("Training perceptrons...");
        for (Perceptron perceptron : perceptrons) {
            // gets the training vector list for the perceptron's language
            List<Vector> trainingVectors = trainingVectorsList.stream()
                    .filter(vectors -> vectors.getFirst().getVectorLanguage().equals(perceptron.perceptronLanguage))
                    .findFirst().get();

            for (int i = 0; i < epochs; i++) {
                for (Vector vector : trainingVectors) {
                    int target = vector.getVectorLanguage().equals(perceptron.perceptronLanguage) ? 1 : 0;
                    int guess = perceptron.guess(vector);
                    if (guess != target) {
                        perceptron.train(vector, guess);
                    }
                }
            }

            System.out.println("Perceptron for " + perceptron.perceptronLanguage + " trained.");

        }

        System.out.println("------------------------");
        System.out.println("Perceptrons trained.");
        System.out.println(perceptrons);
        System.out.println("------------------------");

    }

    public static String testPerceptrons(List<Perceptron> perceptrons, String text) {
        Vector testVector = new Vector(FileHandler.createLettersMap(text));
//        System.out.println(testVector);
        System.out.println(testVector);
        Map<String, Double> guesses = new HashMap<>();
        for (Perceptron perceptron : perceptrons) {
            double guess = perceptron.continuousGuess(testVector);
            guesses.put(perceptron.perceptronLanguage, guess);
        }

        System.out.println("Guesses for the text: ");
        for (Map.Entry<String, Double> entry : guesses.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("The text is most likely in the following language: ");
        String result = guesses.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        System.out.println(result);

        System.out.println("Smallest guess value: ");
        System.out.println(guesses.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey());

        return result;
    }

}