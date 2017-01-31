import java.util.ArrayList;

/**
 * Created by Vadim on 26.01.17.
 */
public class DecisionTreeNode {
    private int dimentions;
    private int numberOfClasses;
    private int plotDimensionNumber;
    private double tresholdValue;
    private DecisionTreeNode leftTreeNode, rightTreeNode;
    private boolean isLeaf;
    private ArrayList<Double> probabilities;

    DecisionTreeNode(int _dimentions, int _numberOfClasses) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        plotDimensionNumber = 0;
        tresholdValue = .0;
        leftTreeNode = null;
        rightTreeNode = null;
        isLeaf = false;
        probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(0.0));
    }

    DecisionTreeNode(int _dimentions, int _numberOfClasses, int _plotDimension, boolean _isLeaf) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        plotDimensionNumber = _plotDimension;
        tresholdValue = .0;
        leftTreeNode = null;
        rightTreeNode = null;
        isLeaf = _isLeaf;
        probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(0.0));
    }

    DecisionTreeNode(int _dimentions, int _numberOfClasses ,int _plotDimension, boolean _isLeaf, double _tresholdValue) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        plotDimensionNumber = _plotDimension;
        tresholdValue = _tresholdValue;
        leftTreeNode = null;
        rightTreeNode = null;
        isLeaf = _isLeaf;
        probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(0.0));
    }

    DecisionTreeNode(int _dimentions, int _numberOfClasses, int _plotDimension, boolean _isLeaf, int _answer) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        plotDimensionNumber = _plotDimension;
        tresholdValue = .0;
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
            int _plotDimension,
            double _tresholdValue,
            boolean _isLeaf,
            DecisionTreeNode _leftTreeNode,
            DecisionTreeNode _rightTreeNode
    ) {
        dimentions = _dimentions;
        numberOfClasses = _numberOfClasses;
        plotDimensionNumber = _plotDimension;
        tresholdValue = _tresholdValue;
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

    public DecisionTreeNode getNextTreeNode(FeaturesVector fitures) {
        if (fitures.getFeature(plotDimensionNumber) <= tresholdValue)
            return leftTreeNode;
        return rightTreeNode;
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
        tresholdValue = treshold;
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
