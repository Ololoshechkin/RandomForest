import java.util.ArrayList;
import java.util.Dictionary;

/**
 * Created by Vadim on 26.01.17.
 */
public class FeaturesVector {
    private int dimension;
    private ArrayList<Double> featuresList;

    public void setDimension(int n) {
        dimension = n;
        featuresList = new ArrayList<Double>(dimension);
    }

    public void setFeatures(ArrayList<Double> features) {
        featuresList = features;
    }

    public void setFeature(int i, double value) {
        featuresList.set(i, new Double(value));
    }

    FeaturesVector() {
        dimension = 1;
        featuresList = new ArrayList<Double>(dimension);
    }

    FeaturesVector(int n) {
        dimension = n;
        featuresList = new ArrayList<Double>(dimension);
    }

    FeaturesVector(int n, ArrayList<Double> features) {
        dimension = n;
        featuresList = features;
    }

    public double getFeature(int i) {
        return featuresList.get(i);
    }

    public ArrayList<Double> getFeaturesList() {
        return featuresList;
    }


}
