import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Utils {
    public static UBQP extractUBQPFromFile(String filename) {
        UBQP ubqp = new UBQP();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            String[] splitted = line.split(" ");
            ubqp.n = Integer.parseInt(splitted[0]);
            ubqp.p = Integer.parseInt(splitted[1]);

            ubqp.q = new int[ubqp.n][ubqp.n];

            int l = 0, c = 0;
            for (int i = 2 ; i < splitted.length ; i++) {
                ubqp.q[l][c] = Integer.parseInt(splitted[i]);

                if (c == ubqp.n - 1) {
                    l++;
                }
                c = c < ubqp.n-1 ? c+1 : 0;
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ubqp;
    }

    public static int somme(int[] t) {
        return Arrays.stream(t).sum();
    }

    public static void addIfSizeLowerThanK(LinkedList<int[]> tabou, int k, int[] s) {
        if (tabou.size() >= k) {
            tabou.removeFirst();
        }
        tabou.add(s);
    }

    public static void addIfSizeLowerThanK(LinkedList<List<Ville>> tabou, int k, List<Ville> s) {
        if (tabou.size() >= k) {
            tabou.removeFirst();
        }
        tabou.add(s);
    }

    public static boolean listContains(List<int[]> tabou, int[] copie) {
        for (int[] c : tabou) {
            if (c == copie)
                return true;

            if (Arrays.equals(c, copie)) {
                return true;
            }
        }

        return false;
    }

    public static void permut(List<Ville> result, int i, int j) {
        Ville t = result.get(i);
        result.set(i, result.get(j));
        result.set(j, t);
    }
}
