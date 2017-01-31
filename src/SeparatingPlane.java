/**
 * Created by Vadim on 31.01.17.
 */
public class SeparatingPlane {

    private int dimentionNumber;
    private double tresholdValue;

    SeparatingPlane() {
        dimentionNumber = 0;
        tresholdValue = .0;
    }

    SeparatingPlane(int _dimentionNumber, double _tresholdValue) {
        dimentionNumber = _dimentionNumber;
        tresholdValue = _tresholdValue;
    }

    public void setDimentionNumber(int nmb) {
        dimentionNumber = nmb;
    }

    public void setTresholdValue(double val) {
        tresholdValue = val;
    }

    public int getDimentionNumber() {
        return dimentionNumber;
    }

    public double getTresholdValue() {
        return tresholdValue;
    }

}
