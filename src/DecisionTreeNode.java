import java.util.ArrayList;

/**
 * Created by Vadim on 26.01.17.
 */
public class DecisionTreeNode {
    private int dimentions;
    private int numberOfClasses;
    private SeparatingPlane separatingPlane;
    private DecisionTreeNode leftTreeNode, rightTreeNode;
    private boolean isLeaf;
    private ArrayList<Double> probabilities;

    DecisionTreeNode(int _dimentions, int _numberOfClasses) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        separatingPlane = new SeparatingPlane();
        leftTreeNode = null;
        rightTreeNode = null;
        isLeaf = false;
        probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(0.0));
    }

    DecisionTreeNode(int _dimentions, int _numberOfClasses, boolean _isLeaf) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        separatingPlane = new SeparatingPlane();
        leftTreeNode = null;
        rightTreeNode = null;
        isLeaf = _isLeaf;
        probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(0.0));
    }

    DecisionTreeNode(int _dimentions, int _numberOfClasses, boolean _isLeaf, SeparatingPlane _separatingPlane) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        separatingPlane = _separatingPlane;
        leftTreeNode = null;
        rightTreeNode = null;
        isLeaf = _isLeaf;
        probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(0.0));
    }

    DecisionTreeNode(int _dimentions, int _numberOfClasses, boolean _isLeaf, int _answer) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        separatingPlane = new SeparatingPlane();
        leftTreeNode = null;
        rightTreeNode = null;
        isLeaf = _isLeaf;
        probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(0.0));
    }

    DecisionTreeNode(
            int _dimentions,
            int _numberOfClasses,
            SeparatingPlane _separatingPlane,
            boolean _isLeaf,
            DecisionTreeNode _leftTreeNode,
            DecisionTreeNode _rightTreeNode
    ) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        separatingPlane = _separatingPlane;
        leftTreeNode = _leftTreeNode;
        rightTreeNode = _rightTreeNode;
        isLeaf = _isLeaf;
        probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(0.0));
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public DecisionTreeNode getNextTreeNode(FeaturesVector features) {
        if (features.getFeature(separatingPlane.getDimentionNumber()) <= separatingPlane.getTresholdValue())
            return leftTreeNode;
        return rightTreeNode;
    }

    public boolean nextNodeIsLeft(FeaturesVector features) {
        return (features.getFeature(separatingPlane.getDimentionNumber()) <= separatingPlane.getTresholdValue());
    }

    public int getAnswer() {
        int argmax = 0;
        for (int i = 1; i < numberOfClasses; ++i)
            if (probabilities.get(i) > probabilities.get(argmax))
                argmax = i;
        return argmax;
    }

    public double getProbability(int classNumber) {
        return probabilities.get(classNumber);
    }

    public ArrayList<Double> getProbabilities() {
        return probabilities;
    }

    public void setProbability(int classNumber, double probability) {
        probabilities.set(classNumber, probability);
    }

    public void setProbabilities(ArrayList<Double> _probabilities) {
        probabilities = _probabilities;
    }

    public void setIsLeaf(boolean newVal) {
        isLeaf = newVal;
    }

    public void setTresholdValue(double treshold) {
        separatingPlane.setTresholdValue(treshold);
    }

    public void setPlaneDimentionNumber(int dimentionNmb) {
        separatingPlane.setDimentionNumber(dimentionNmb);
    }

    public void setSeparatingPlane(SeparatingPlane plane) {
        separatingPlane = plane;
    }

    public void addChildren(DecisionTreeNode leftChild, DecisionTreeNode rightChild) {
        leftTreeNode = leftChild;
        rightTreeNode = rightChild;
    }

    public void setRightChild(DecisionTreeNode rightChild) {
        rightTreeNode = rightChild;
    }

    public void setLeftChild(DecisionTreeNode leftChild) {
        leftTreeNode = leftChild;
    }

    public DecisionTreeNode getRightChild() {
        return rightTreeNode;
    }

    public DecisionTreeNode getLeftChild() {
        return leftTreeNode;
    }

}
