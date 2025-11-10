package src.presenteFacil.aeds3;

public class ParIDScore {
    private int id;
    private double score;

    public ParIDScore(int id, double score) {
        this.id = id;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public double getScore() {
        return score;
    }
    
    public String toString() {
        return "(" + id + "; " + String.format("%.3f", score) + ")";
    }
}

