import java.util.Objects;

public class Ville {
    int identifiant;
    int x;
    int y;

    public Ville(int identifiant, int x, int y) {
        this.identifiant = identifiant;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "V" + identifiant +
                "(" + x +
                ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ville ville = (Ville) o;
        return identifiant == ville.identifiant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiant);
    }

    public double calculerDistance(Ville o) {
        return Math.sqrt(
          Math.pow(o.x - x, 2) + Math.pow(o.y - y, 2)
        );
    }
}
