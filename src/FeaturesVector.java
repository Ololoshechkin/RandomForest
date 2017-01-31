import java.util.ArrayList;
import java.util.Dictionary;

/**
 * Created by Vadim on 26.01.17.
 */
public class FeaturesVector {
    private int dimensions;
    private ArrayList<Double> featuresList;

    public void setDimension(int n) {
        dimensions = n;
        featuresList = new ArrayList<>();
        for (int i = 0; i < dimensions; ++i) {
            featuresList.add(new Double(0.0));
        }
    }

    public void setFeatures(ArrayList<Double> features) {
        featuresList = features;
    }

    public void setFeature(int i, double value) {
        featuresList.set(i, new Double(value));
    }

    FeaturesVector() {
        dimensions = 1;
        featuresList = new ArrayList<>();
        featuresList.add(new Double(0.0));
    }

    FeaturesVector(int n) {
        dimensions = n;
        featuresList = new ArrayList<>();
        for (int i = 0; i < dimensions; ++i) {
            featuresList.add(new Double(0.0));
        }
    }

    FeaturesVector(int n, ArrayList<Double> features) {
        dimensions = n;
        featuresList = features;
    }

    FeaturesVector(FeaturesVector prototype) {
        dimensions = prototype.dimensions;
        featuresList = new ArrayList<>(prototype.getFeaturesList());
    }

    public double getFeature(int i) {
        return featuresList.get(i);
    }

    public ArrayList<Double> getFeaturesList() {
        return featuresList;
    }


}
