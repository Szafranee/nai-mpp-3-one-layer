import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    static double learningRate = 0.01;
    static int epochs = 10;

    static List<Perceptron> perceptronLayer;
    static List<Vector> trainingVectorsList;
    static List<Vector> testVectorsList;
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
        System.out.println("----------------------");
        trainingVectorsList = FileHandler.createVectorList(languagesList, dataDir);
        testVectorsList = FileHandler.createVectorList(languagesList, "test");
        System.out.println("------------------------");
        System.out.println("Training vectors created.");
        System.out.println("------------------------");

        perceptronLayer = createPerceptrons(learningRate);

        trainPerceptrons(perceptronLayer, trainingVectorsList);
        testPerceptrons(perceptronLayer, testVectorsList);

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
        /*for (Perceptron perceptron : perceptrons) {
            System.out.println(perceptron.perceptronLanguage);
        }*/
        /*System.out.println("------------------------");
        System.out.println("Creating perceptrons...");
        System.out.println(perceptrons);
        System.out.println("------------------------");*/

        return perceptrons;

    }

    public static void trainPerceptrons(List<Perceptron> perceptrons, List<Vector> trainingVectorsList) {
        System.out.println("Training perceptrons...");
        for (Perceptron perceptron : perceptrons) {
            trainPerceptronOnVectors(trainingVectorsList, perceptron);
            System.out.println("Perceptron for " + perceptron.perceptronLanguage + " trained.");
        }

        System.out.println("------------------------");
        System.out.println("Perceptrons trained.");
        System.out.println("------------------------");
    }

    public static void testPerceptrons(List<Perceptron> perceptrons, List<Vector> testVectors) {
        System.out.println("Testing perceptrons...");
        int iterations = 0;
        while (checkAccuracy(perceptrons, testVectors) < 100) {
            System.out.println("Accuracy: " + checkAccuracy(perceptrons, testVectors) + "%");
            for (Perceptron perceptron : perceptrons) {
                perceptron.initializeAndNormalizeWeights();
            }

            for (int i = 0; i < epochs && checkAccuracy(perceptrons, testVectors) < 100; i++) {
                System.out.println("Accuracy: " + checkAccuracy(perceptrons, testVectors) + "%");
                for (Perceptron perceptron : perceptrons) {
                    trainPerceptronOnVectors(trainingVectorsList, perceptron);
                }
            }
            iterations++;
        }
        System.out.println("Perceptrons tested.");
        System.out.println("Final accuracy: " + checkAccuracy(perceptrons, testVectors) + "%");
        System.out.println("Iterations: " + iterations);
    }

    private static void trainPerceptronOnVectors(List<Vector> trainingVectorsList, Perceptron perceptron) {
        for (Vector vector : trainingVectorsList) {
            int target = vector.getVectorLanguage().equals(perceptron.perceptronLanguage) ? 1 : 0;
            int guess = perceptron.guess(vector);
            if (target != guess) {
                perceptron.train(vector, guess);
            }
        }
    }

    public static String guessLanguage(List<Perceptron> perceptrons, String text) {
        Vector testVector = new Vector(FileHandler.createLettersMap(text));
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

        return result;
    }

    public static String guessLanguageForTest(List<Perceptron> perceptrons, Vector testVector) {
        Map<String, Double> guesses = new HashMap<>();
        for (Perceptron perceptron : perceptrons) {
            double guess = perceptron.continuousGuess(testVector);
            guesses.put(perceptron.perceptronLanguage, guess);
        }

        return guesses.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    public static double checkAccuracy(List<Perceptron> perceptrons, List<Vector> testVectors) {
        int correctGuesses = 0;
        for (Vector vector : testVectors) {
            String guess = guessLanguageForTest(perceptrons, vector);
            if (guess.equals(vector.getVectorLanguage())) {
                correctGuesses++;
            }
        }
        return (double) correctGuesses / testVectors.size() * 100;
    }

}