import java.util.ArrayList;
import java.util.Dictionary;

/**
 * Created by Vadim on 26.01.17.
 */
public class FituresVector {
    private int dimension;
    private ArrayList<Double> fituresList;

    public void setDimension(int n) {
        dimension = n;
        fituresList = new ArrayList<Double>(dimension);
    }

    public void setFitures(ArrayList<Double> fitures) {
        fituresList = fitures;
    }

    public void setFiture(int i, double value) {
        fituresList.set(i, new Double(value));
    }

    FituresVector() {
        dimension = 1;
        fituresList = new ArrayList<Double>(dimension);
    }

    FituresVector(int n) {
        dimension = n;
        fituresList = new ArrayList<Double>(dimension);
    }

    FituresVector(int n, ArrayList<Double> fitures) {
        dimension = n;
        fituresList = fitures;
    }

    public double getFiture(int i) {
        return fituresList.get(i);
    }

    public ArrayList<Double> getFituresList() {
        return fituresList;
    }


}
