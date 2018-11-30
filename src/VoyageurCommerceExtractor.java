import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class VoyageurCommerceExtractor {

    private String fileName;

    public VoyageurCommerceExtractor(String fileName) {
        this.fileName = fileName;
    }

    public List<Ville> extract() {
        String ligne;
        String[] parts;
        List<Ville> results = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // osef first line

            while ( (ligne = br.readLine()) != null) {
                parts = ligne.split("\t");
                results.add(new Ville(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

}
