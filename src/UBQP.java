import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UBQP {

    public int n;
    public int p;
    public int[][] q;

    public String toString() {
        StringBuilder ret = new StringBuilder("n = " + n + " p = " + p + '\n');
        for (int[] subArray : q) {
            ret.append(Arrays.toString(subArray) + '\n');
        }
        return ret.toString();
    }

    /**
     * Prend en compte P ?
     * @return
     */
    public int[] solutionAleatoire() {
        int[] sol = new int[n];
        for (int i = 0 ; i < n ; i ++) {
            sol[i] = Math.round((float) Math.random());
        }
        if (Arrays.stream(sol).sum() < p) {
            return solutionAleatoire();
        }
        return sol;
    }

    /**
     * Respecte la contrainte p
     * @param sequence Séquence de départ
     * @return le meilleur voisin
     */
    public int[] meilleurVoisin(int[] sequence) {
        int[] meilleurSolution = null;
        int coupMeilleureSolution = Integer.MAX_VALUE;
        int[] copie = null;

        int iCoup;
        for (int i = 0 ; i < sequence.length ; i++) {
            copie = sequence.clone();
            copie[i] = copie[i] == 0 ? 1 : 0;

            if (Arrays.stream(copie).sum() >= p) {
                iCoup = calculerSolution(copie);
                if (iCoup < coupMeilleureSolution) {
                    coupMeilleureSolution = iCoup;
                    meilleurSolution = copie;
                }
            }
        }
        return meilleurSolution;
    }

    /**
     * Correspond a f(X)
     * @param vecteur
     * @return
     */
    public int calculerSolution(int[] vecteur) {
        int sol = 0;

        for (int i = 0 ; i < n ; i++) {
            for (int j = 0 ; j < n ; j++) {
                sol += q[i][j] * vecteur[i]  * vecteur[j];
            }
        }


        return sol;
    }

    public int[] steepestHillClimbingRestart(int maxFois, int maxDepl) {
        int[] s = null;
        int[] sPrime;

        for (int i = 0 ; i < maxFois ; i++) {
            sPrime = steepestHillClimbing(maxDepl);

            if (s == null || (calculerSolution(sPrime) < calculerSolution(s))) {
                s = sPrime;
            }
        }
        return s;
    }

    public int[] steepestHillClimbing(int maxDepl) {
        int[] s = solutionAleatoire();
        int[] sPrime;
        int nbDepl = 0;
        boolean stop = false;

        System.out.println("On part de " + Arrays.toString(s));

        do {
            sPrime = meilleurVoisin(s);
            if (sPrime != null && calculerSolution(sPrime) < calculerSolution(s)) {
                s = sPrime;
            } else {
                stop = true;
            }
            nbDepl ++;
        } while (!stop && nbDepl < maxDepl);
        return s;
    }

    private List<int[]> voisinsNonTabous(List<int[]> tabou, int[] s) {
        List<int[]> results = new LinkedList<>();
        int[] sequence = s.clone();
        int[] copie = null;
        for (int i = 0 ; i < sequence.length ; i++) {
            copie = sequence.clone();
            copie[i] = copie[i] == 0 ? 1 : 0;

            if (Arrays.stream(copie).sum() >= p && !Utils.listContains(tabou, copie)) {
                results.add(copie);
            }
        }

        return results;
    }

    /**
     * Question 6
     * @param s une solution existante
     * @return une solution pas mal
     */
    public int[] methodeTabou(int[] s, int maxDepl, int k) {
        int[] msol = s;
        int[] sPrime = null;
        LinkedList<int[]> tabou = new LinkedList<>();
        int nbDepl = 0;
        boolean stop = false;
        List<int[]> voisinsNonTabous;

        do {
            System.out.println(" > " + nbDepl + "e déplacement : \nMeilleur solution : "
                    + Arrays.toString(msol) + " cout = " + calculerSolution(msol)
                    + "tabous = " + tabou.stream().map(Arrays::toString).collect(Collectors.joining(",")));
            voisinsNonTabous = voisinsNonTabous(tabou, s);
            if (!voisinsNonTabous.isEmpty()) {
                sPrime = meilleursVoisinsParmis(voisinsNonTabous);
            } else {
                stop = true;
            }
            Utils.addIfSizeLowerThanK(tabou, k, s);
            if (sPrime != null && calculerSolution(sPrime) < calculerSolution(msol)) {
                msol = sPrime;
            }
            s = sPrime;
            nbDepl ++;
        } while (nbDepl != maxDepl && !stop);

        return msol;
    }

    private int[] meilleursVoisinsParmis(List<int[]> voisinsNonTabous) {
        int[] meilleurVoisin = null;
        int scoreMeilleurvoisin = Integer.MAX_VALUE;
        int tmpCout;

        for (int[] sequence : voisinsNonTabous) {
            tmpCout = calculerSolution(sequence);
            if (scoreMeilleurvoisin > tmpCout) {
                meilleurVoisin = sequence;
                scoreMeilleurvoisin = tmpCout;
            }
        }

        return meilleurVoisin;
    }

    public static void main(String[] args) {
        // test calculerScore
        UBQP ex = Utils.extractUBQPFromFile("partition6.txt");
        System.out.println(ex);

        System.out.println("steepestHillClimbing");
        int[] s = ex.steepestHillClimbing(5);
        System.out.println(Arrays.toString(s) + " cout = " + ex.calculerSolution(s));

        // System.out.println(ex.calculerSolution(new int[]{1, 1, 0, 1, 0, 0}));

        System.out.println("\nsteepestHillClimbingRestart :");
        int[] sol = ex.steepestHillClimbingRestart(4, 100);
        System.out.println(Arrays.toString(sol) + " cout => " + ex.calculerSolution(sol));

        System.out.println("TABOU");
        int[] tabou = ex.methodeTabou(ex.solutionAleatoire(), 1000, 1_000_000); // todo sol alea
        System.out.println("\nTabou  : " + Arrays.toString(tabou)
                + "cout = " + ex.calculerSolution(tabou));
    }
}
