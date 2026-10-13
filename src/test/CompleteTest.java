package test;

import pvz.PlantsVsZombies;
import pvz.logic.Game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class CompleteTest {
  private Path resourcesDir;
  private List<String> testFilesPrefixes;

  public CompleteTest(String testResources) {
    try {
      this.resourcesDir = Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource(testResources)).toURI());
    } catch (URISyntaxException | NullPointerException e) {
      fail("Unable to read test resources from folder " + testResources);
    }
    this.testFilesPrefixes = Arrays.stream(Objects.requireNonNull(this.resourcesDir.toFile().listFiles()))
        .filter( f -> f.getName().endsWith("-input.txt"))
        .map(f -> f.toPath().getFileName().toString().replace("-input.txt", ""))
        .sorted()
        .toList();
  }

  private void checkOutput(Path expectedPath, Path actualPath) throws FileNotFoundException, IOException {
    try (BufferedReader expected = new BufferedReader(new FileReader(expectedPath.toFile()));
         BufferedReader actual = new BufferedReader(new FileReader(actualPath.toFile()))) {

      String expectedLine = expected.readLine();
      String actualLine = actual.readLine();
      int lineNumber = 1;
      while (expectedLine != null && actualLine != null &&
          (expectedLine.equals(actualLine)
              || areLinesEquivalent(expectedLine, actualLine))) { // ORDER not important

        expectedLine = expected.readLine();
        actualLine = actual.readLine();
        lineNumber++;
      }

      if (expectedLine != null || actualLine != null) {
        String lineMessage = "Line: %d%n".formatted(lineNumber);
        String expectedMessage = "Expected: %s%n".formatted(
            expectedLine == null ? "EOF" : expectedLine);
        String actualMessage = "Actual  : %s%n".formatted(
            actualLine == null ? "EOF" : actualLine);
        System.out.println(lineMessage + expectedMessage + actualMessage);

        fail(lineMessage + expectedMessage + actualMessage);
      }
    }
  }

  private static String sortCharacters(String segment) {
    return segment.chars()
        .sorted()
        .collect(StringBuilder::new,
            StringBuilder::appendCodePoint,
            StringBuilder::append)
        .toString();
  }

  private static boolean areSegmentsEquivalent(String segment1, String segment2) {
    // Ordena los caracteres de ambos segmentos y los compara
    return sortCharacters(segment1.trim()).equals(sortCharacters(segment2.trim()));
  }

  public static boolean areLinesEquivalent(String expectedLine, String actualLine) {
    // Dividir ambas líneas por los delimitadores '|'
    String[] expectedParts = expectedLine.split("┃");
    String[] actualParts = actualLine.split("┃");

    // 1. Ambas líneas deben tener exactamente dos delimitadores
    if (expectedParts.length != 3 || actualParts.length != 3) {
      return false;
    }

    // 2. El contenido fuera de los delimitadores debe ser idéntico
    if (!expectedParts[0].equals(actualParts[0]) || !expectedParts[2].equals(actualParts[2])) {
      return false;
    }

    // 3. El contenido dentro de los delimitadores debe dividirse en DIM_X trozos
    String[] expectedSegments = splitIntoSegments(expectedParts[1], Game.NUM_COLS);
    String[] actualSegments = splitIntoSegments(actualParts[1], Game.NUM_COLS);

    if (expectedSegments == null || actualSegments == null) {
      return false; // Si no se pueden dividir correctamente
    }

    // Comparar cada segmento entre ambas líneas
    for (int i = 0; i < Game.NUM_COLS; i++) {
      if (!areSegmentsEquivalent(expectedSegments[i], actualSegments[i])) {
        // System.out.println("Expected segment: " + expectedSegments[i]);
        // System.out.println("Actual segment: " + actualSegments[i]);
        return false;
      }
    }

    // Si todas las condiciones se cumplen, las líneas son equivalentes
    return true;
  }

  private static String[] splitIntoSegments(String content, int segments) {
    if (content.length() % segments != 0) {
      return null; // No se puede dividir en partes iguales
    }

    int segmentLength = content.length() / segments;
    String[] result = new String[segments];

    for (int i = 0; i < segments; i++) {
      result[i] = content.substring(i * segmentLength, (i + 1) * segmentLength);
    }

    return result;
  }

  public void parameterizedTest(Path input, Path expected, Path output, String[] args) {

    if (!output.getParent().toFile().exists()) {
      fail("Output file cannot be created at " + output.toAbsolutePath());
    }
    try (PrintStream out = new PrintStream(output.toFile()); InputStream in = new FileInputStream(input.toFile())) {
      PrintStream oldOut = System.out;
      InputStream oldIn = System.in;

      System.setOut(out);
      System.setIn(in);

      // ejecuta el Main con entrada de algo_input.txt, y salida redirigida a algo_output.txt
      PlantsVsZombies.main(args);

      System.setOut(oldOut);
      System.setIn(oldIn);

      checkOutput(expected, output);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
      fail("File not found: " + e.getMessage());
    } catch (IOException e1) {
      e1.printStackTrace();
      fail("IOException: " + e1.getMessage());
    }
  }

  protected void testN(int n) {
    String[] testDescription = testFilesPrefixes.get(n).split("-");
    int testNumber = Integer.parseInt(testDescription[0]);
    assert n == testNumber;

    String level = testDescription[1].split("_")[0];
    String seed = testDescription[1].split("_")[1];
    System.out.println("Launching test " + testNumber + " (" + level + ", " + seed + ")" );
    parameterizedTest(Paths.get(this.resourcesDir.toString(), this.testFilesPrefixes.get(n) + "-input.txt"),
        Paths.get(this.resourcesDir.toString(), testFilesPrefixes.get(n) + "-expected.txt"),
        Paths.get(this.resourcesDir.toString(), testFilesPrefixes.get(n) + "-output.txt"),
        new String[]{level, seed});
  }


}
