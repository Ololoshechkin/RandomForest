/**
 * Created by Vadim on 31.01.17.
 */
public class ObjectWithClass {

    private FeaturesVector featuresVector;
    private int classNumber;

    ObjectWithClass() {
        featuresVector = new FeaturesVector();
        classNumber = 0;
    }

    ObjectWithClass(FeaturesVector _featuresVector, int _classNumber) {
        featuresVector = _featuresVector;
        classNumber = _classNumber;
    }

    public void setFeaturesVector(FeaturesVector _featuresVector) {
        featuresVector = _featuresVector;
    }

    public void setClassNumber(int _classNumber) {
        classNumber = _classNumber;
    }

    public FeaturesVector getFeaturesVector() {
        return featuresVector;
    }

    public int getClassNumber() {
        return classNumber;
    }

}
