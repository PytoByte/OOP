import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with finding substrings methods.
 */
public class SubstringFinder {

    /**
     * Find substrings in the text file.
     *
     * @param filepath path to file
     * @param substring substring that we're searching
     * @return index of each substring
     * @throws IOException if file not found or can't read ect
     */
    public static List<Long> find(String filepath, String substring) throws IOException {
        if (substring.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> positions = new ArrayList<>();
        int bufferSize = 8192;
        int overlap = substring.length() - 1;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filepath), StandardCharsets.UTF_8)) {
            char[] buffer = new char[bufferSize];
            int charsRead;
            long position = 0;
            String leftover = "";

            while ((charsRead = reader.read(buffer)) != -1) {
                String chunk = new String(buffer, 0, charsRead);
                String text = leftover + chunk;

                int offset = 0;
                int found;
                while ((found = text.indexOf(substring, offset)) != -1) {
                    long globalPos = position + (found - leftover.length());
                    positions.add(globalPos);
                    offset = found + 1;
                }

                leftover = text.length() >= overlap ?
                        text.substring(text.length() - overlap) : text;

                position += chunk.length();
            }
        }

        return positions;
    }
}