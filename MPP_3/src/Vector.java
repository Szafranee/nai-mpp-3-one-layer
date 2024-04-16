import java.util.Map;

public record Vector(Map<Character, Double> lettersMap, String vectorLanguage) {
    public Vector {
        if (vectorLanguage == null) {
            throw new IllegalArgumentException("Language cannot be null");
        }
    }

    public void normalize() {
        int sum = lettersMap.values().stream().mapToInt(Double::intValue).sum();
        lettersMap.replaceAll((k, v) -> v / sum);
    }
}
