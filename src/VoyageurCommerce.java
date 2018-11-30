import java.util.*;
import java.util.stream.Collectors;

public class VoyageurCommerce {

    private List<Ville> villes;

    public VoyageurCommerce(List<Ville> villes) {
        this.villes = villes;
    }

    public List<Ville> solutionAleatoire() {
        List<Ville> results = new LinkedList<>();
        Random r = new Random();

        Ville iVille;
        do {
            iVille = villes.get(r.nextInt(villes.size()));
            if (!results.contains(iVille)) {
                results.add(iVille);
            }
        } while (results.size() != villes.size());


        return results;
    }

    public List<Ville> meilleurVoisin(List<Ville> solution) {
        List<Ville> current = new LinkedList<>();
        List<Ville> bestSolution = new LinkedList<>();
        double bestScore = Double.MAX_VALUE;
        double currentScore = 0.0;

        for (int i = 0 ; i < solution.size() ; i++) {
            for (int j = i+1 ; j < solution.size() ; j++) {
                current = new LinkedList<>(solution);
                Utils.permut(current, i, j);

                /* DEBUG
                System.out.println("Permut " + i + " " + j + " Solution en test : " + current);
                System.out.println("\t score = " + currentScore);
                */

                currentScore = calculerSolution(current);

                if (currentScore < bestScore) {
                    bestScore = currentScore;
                    bestSolution = new LinkedList<>(current);
                }
            }
        }


        return bestSolution;
    }

    /**
     * Rajoute tout seul le fait qu'on passe par 0 et finisse par 0
     * @param solution
     * @return
     */
    public double calculerSolution(final List<Ville> solution) {
        if (solution.isEmpty()) {
            return Double.MAX_VALUE;
        }
        Ville depart = new Ville(0, 0, 0);
        List<Ville> s = new LinkedList<>(solution);
        s.add(0, depart);
        s.add(depart);

        double d = 0.0;
        for (int i = 0 ; i < s.size()-1 ; i++) {
            d += s.get(i).calculerDistance(s.get(i+1));
        }

        return d;
    }

    /**
     * STEEPEST HILL CLIMBING
     * @param maxDepl
     * @return
     */
    public List<Ville> steepestHillClimbing(int maxDepl) {
        List<Ville> s = solutionAleatoire();
        List<Ville> sPrime;
        int nbDepl = 0;
        boolean stop = false;

        // System.out.println("SHC (simple) On part de " + s + " cout = " + calculerSolution(s));

        do {
            sPrime = meilleurVoisin(s);
            // System.out.println("SHC (simple) Voisin au déplacement " + nbDepl + " " + sPrime + " cout = " + calculerSolution(sPrime));
            if (calculerSolution(sPrime) < calculerSolution(s)) {
                s = sPrime;
            } else {
                stop = true;
            }
            nbDepl ++;
        } while (!stop && nbDepl < maxDepl);
        return s;
    }

    public List<Ville> steepestHillClimbingRestart(int maxFois, int maxDepl) {
        List<Ville> s = new LinkedList<>();
        List<Ville> sPrime;

        // System.out.println("SHCR == ");
        for (int i = 0 ; i < maxFois ; i++) {
            sPrime = steepestHillClimbing(maxDepl);

            if (calculerSolution(sPrime) < calculerSolution(s)) {
                s = sPrime;
            }
        }
        return s;
    }

    private List<Ville> meilleursVoisinsParmis(List<List<Ville>> voisinsNonTabous) {
        List<Ville> meilleurVoisin = null;
        double scoreMeilleurvoisin = Double.MAX_VALUE;
        double tmpCout;

        for (List<Ville> sequence : voisinsNonTabous) {
            tmpCout = calculerSolution(sequence);
            if (tmpCout < scoreMeilleurvoisin) {
                meilleurVoisin = sequence;
                scoreMeilleurvoisin = tmpCout;
            }
        }

        return meilleurVoisin;
    }

    private List<List<Ville>> voisinsNonTabous(List<List<Ville>> tabou, List<Ville> s) {
        List<List<Ville>> solutions = new LinkedList<>();
        List<Ville> current = new LinkedList<>();

        for (int i = 0 ; i < s.size() ; i++) {
            for (int j = i+1 ; j < s.size() ; j++) {
                current = new LinkedList<>(s);
                Utils.permut(current, i, j);

                if (!tabou.contains(current)) {
                    solutions.add(current);
                }
            }
        }
        return solutions;
    }

    public List<Ville> tabou(List<Ville> s, int maxDepl, int k) {
        List<Ville> msol = s;
        List<Ville> sPrime = null;
        LinkedList<List<Ville>> tabou = new LinkedList<>();
        int nbDepl = 0;
        boolean stop = false;
        List<List<Ville>> voisinsNonTabous;

        do {
            /*
            System.out.println(" > " + nbDepl + "e déplacement : \nMeilleur solution : "
                    + msol + " cout = " + calculerSolution(msol)
                    + "tabous = " + tabou);
                    */
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

    public static void main(String... args) {
        List<Ville> villes = new VoyageurCommerceExtractor("tsp101.txt").extract();
        VoyageurCommerce vc = new VoyageurCommerce(villes);

        List<Ville> sAlea = vc.solutionAleatoire();
        System.out.println("Alea : " +sAlea + " cout = " + vc.calculerSolution(sAlea));
        List<Ville> voisin = vc.meilleurVoisin(sAlea);
        System.out.println("Voisin : " + voisin + " cout = " + vc.calculerSolution(voisin));

        List<Ville> shc = vc.steepestHillClimbing(10);
        System.out.println("-> SHC (simple) " + shc + " cout = " + vc.calculerSolution(shc));

        List<Ville> shcr = vc.steepestHillClimbingRestart(3, 10);
        System.out.println("-> SHC (restart) " + shcr + " cout = " + vc.calculerSolution(shcr));
//
          List<Ville> tabou = vc.tabou(vc.solutionAleatoire(), 100, 100);
          System.out.println("tabou " + tabou + " cout = " + vc.calculerSolution(tabou));
    }
}
