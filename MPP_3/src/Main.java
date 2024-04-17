import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<String> languagesList = FileHandler.getLanguages("data");
        List<List<Vector>> trainingVectorsList = FileHandler.createTrainingVectors(languagesList, "data");
    }

    public static List<Perceptron> createPerceptrons(List<Vector> trainingVectorsList) {
        List<Perceptron> perceptrons = new ArrayList<>();

        for (Vector vector : trainingVectorsList) {
            perceptrons.add(new Perceptron(vector.getVectorLanguage(), vector.getLettersMap().size(), 0.001));
        }
        System.out.println(perceptrons.size() + " perceptrons created for the following languages: ");
        for (Perceptron perceptron : perceptrons) {
            System.out.println(perceptron.perceptronLanguage);
        }
        System.out.println();
        return perceptrons;
    }

    public static void trainPerceptrons(List<Perceptron> perceptrons, List<Vector> trainingVectorsList) {
        for (Perceptron perceptron : perceptrons) {
            // gets the training vector for the perceptron's language
            Vector trainingVector = trainingVectorsList.stream()
                    .filter(vector -> vector.getVectorLanguage().equals(perceptron.perceptronLanguage))
                    .findFirst()
                    .orElse(null);
            if (trainingVector == null) {
                throw new IllegalArgumentException("No training vector found for the perceptron's language");
            }

            int guess = perceptron.guess(trainingVector);
            System.out.println("Training perceptron for language: " + perceptron.perceptronLanguage);
            System.out.println("Initial guess: " + guess);

            int counter = 0;
            while (guess != 1) {
                System.out.println("Training iteration: " + counter++);
                perceptron.train(trainingVector, guess);
                guess = perceptron.guess(trainingVector);
                System.out.println("New guess: " + guess);
                if (counter > 50000) {
                    System.out.println("Perceptron training failed for language: " + perceptron.perceptronLanguage);
                    break;
                }
            }

            System.out.println("Perceptron trained for language: " + perceptron.perceptronLanguage);
            System.out.println();

        }
    }

    public static void testPerceptrons(List<Perceptron> perceptrons, String text) {
        Vector testVector = new Vector(FileHandler.createLettersMap(text));
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
        System.out.println(guesses.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());
    }

}