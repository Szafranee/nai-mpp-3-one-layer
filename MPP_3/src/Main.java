import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> languagesList = FileHandler.getLanguages("data");
        List<Vector> vectorsList = FileHandler.createLanguageVectorsFromFiles("data", languagesList);
    }
}