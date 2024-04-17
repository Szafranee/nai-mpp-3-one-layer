import java.util.Map;

public class Vector {

    private final Map<Character, Double> lettersMap;
    private final String vectorLanguage;

    public Vector(Map<Character, Double> lettersMap) {
        this(lettersMap, null);
    }

    public Vector(Map<Character, Double> lettersMap, String vectorLanguage) {
        this.lettersMap = lettersMap;
        this.vectorLanguage = vectorLanguage;
        normalize();
    }

    public void normalize() {
        int sum = lettersMap.values().stream().mapToInt(Double::intValue).sum();
        lettersMap.replaceAll((k, v) -> v / sum);
    }

    public Map<Character, Double> getLettersMap() {
        return lettersMap;
    }

    public String getVectorLanguage() {
        return vectorLanguage;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "lettersMap=" + lettersMap +
                ", vectorLanguage='" + vectorLanguage + '\'' +
                '}';
    }
}