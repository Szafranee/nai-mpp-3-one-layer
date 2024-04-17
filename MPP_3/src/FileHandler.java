import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileHandler {

    public static List<String> getLanguages(String dataDir) {
        Path dataDirPath = Paths.get(dataDir);
        List<String> languages = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(dataDirPath, 1)) {
            paths.filter(Files::isDirectory)
                    .skip(1)
                    .forEach(path -> languages.add(path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return languages;
    }

    public static List<List<Vector>> createTrainingVectors(List<String> languages, String dataDir) {
        List<List<Vector>> trainingVectors = new ArrayList<>();
        for (String language : languages) {
            Path languagePath = Paths.get(dataDir, language);
            List<Vector> languageVectors = createLanguageVectorsFromFiles(languagePath);
            trainingVectors.add(languageVectors);
        }
        return trainingVectors;
    }

    private static List<Vector> createLanguageVectorsFromFiles(Path languagePath) {
        List<Vector> languageVectors = new ArrayList<>();
        // create vectors from each file in the Language directory
        try (Stream<Path> paths = Files.walk(languagePath, 1)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String text = Files.readString(path);
                            Map<Character, Double> lettersMap = createLettersMap(text);
                            languageVectors.add(new Vector(lettersMap, path.getFileName().toString()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return languageVectors;
    }

    public static Map<Character, Double> createLettersMap(String text) {
        text = text.toLowerCase();
        Map<Character, Double> lettersMap = new HashMap<>();
        for (char c = 'a'; c <= 'z'; c++) {
            lettersMap.put(c, 0.0);
        }
        for (char c : text.toCharArray()) {
            if (lettersMap.containsKey(c)) {
                lettersMap.put(c, lettersMap.get(c) + 1);
            }
        }
        return lettersMap;
    }
}

