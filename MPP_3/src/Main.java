import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static double learningRate = 0.07;
    static int epochs = 1000;

    static List<Perceptron> perceptronLayer;
    static List<Vector> trainingVectorsList;
    static List<String> languagesList;
    static String dataDir = "data";

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        languagesList = FileHandler.getLanguages(dataDir);
        System.out.println("Available languages: ");
        System.out.println("----------------------");
        for (String language : languagesList) {
            System.out.println(language);
        }
        trainingVectorsList = FileHandler.createVectorList(languagesList, dataDir);
        System.out.println("------------------------");
        System.out.println("Training vectors created.");
        System.out.println("------------------------");

        perceptronLayer = createPerceptrons(learningRate);

        trainPerceptrons(perceptronLayer, trainingVectorsList, epochs);

        SwingUtilities.invokeLater(LanguageGuesserGui::new);
    }


    public static List<Perceptron> createPerceptrons(double learningRate) {
        List<Perceptron> perceptrons = new ArrayList<>();

        for (String language : languagesList) {
            int weightsSize = trainingVectorsList.getFirst().getLettersMap().size();
            Perceptron perceptron = new Perceptron(language, weightsSize, learningRate);
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

    public static void trainPerceptrons(List<Perceptron> perceptrons, List<Vector> trainingVectorsList, int epochs) {
        System.out.println("Training perceptrons...");
        for (Perceptron perceptron : perceptrons) {
            for (Vector vector : trainingVectorsList) {
                int target = vector.getVectorLanguage().equals(perceptron.perceptronLanguage) ? 1 : 0;
                int guess = perceptron.guess(vector);
                if (target != guess) {
                    perceptron.train(vector, guess);
                }
            }
            System.out.println("Perceptron for " + perceptron.perceptronLanguage + " trained.");
        }

        System.out.println("------------------------");
        System.out.println("Perceptrons trained.");
        System.out.println(perceptrons);
        System.out.println("------------------------");
    }

    public static String guessLanguage(List<Perceptron> perceptrons, String text) {
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

    public static double checkAccuracy(List<Perceptron> perceptrons, List<Vector> testVectors) {
        int correctGuesses = 0;
        for (Perceptron perceptron : perceptrons) {
            for (Vector vector : testVectors) {
                int target = vector.getVectorLanguage().equals(perceptron.perceptronLanguage) ? 1 : 0;
                int guess = perceptron.guess(vector);
                if (target == guess) {
                    correctGuesses++;
                }
            }
        }
        return (double) correctGuesses / testVectors.size();
    }

}