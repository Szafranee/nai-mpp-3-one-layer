import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static List<Vector> createLanguageVectorsFromFiles(String dataDir, List<String> languages) {
        List<Vector> languageVectors = new ArrayList<>();
        for (String language : languages) {
            Vector languageVector = new Vector(new HashMap<>(), language);
            // creates an empty map with all the letters from a to z and 0 as values
            for (char c = 'a'; c <= 'z'; c++) {
                languageVector.lettersMap().put(c, 0.0);
            }

            Path languageDir = Paths.get(dataDir, language);
            try (Stream<Path> paths = Files.walk(languageDir, 1)) {
                paths.filter(Files::isRegularFile)
                        .forEach(path -> {
                            try {
                                List<String> lines = Files.readAllLines(path);
                                for (String line : lines) {
                                    for (char c : line.toLowerCase().toCharArray()) {
                                        if (languageVector.lettersMap().containsKey(c)) {
                                            languageVector.lettersMap().put(c, languageVector.lettersMap().get(c) + 1);
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
            languageVectors.add(languageVector);
        }

        // normalizes each vector
        for (Vector vector : languageVectors) {
            vector.normalize();
        }

        return languageVectors;
    }
}

