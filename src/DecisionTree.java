import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Vadim on 26.01.17.
 */
public class DecisionTree {
    private int dimentions;
    private int numberOfClasses;
    private DecisionTreeNode rootNode;
    private int maxDepth = 1000;
    private Random rnd;

    DecisionTree() {
        dimentions = 1;
        numberOfClasses = 1;
        rootNode = new DecisionTreeNode(dimentions, numberOfClasses);
    }

    private int getRandomClassNumber() {
        return Math.abs(rnd.nextInt()) % numberOfClasses;
    }

    public void setDimentions(int _dimentions) {
        dimentions = _dimentions;
        rootNode = new DecisionTreeNode(dimentions, numberOfClasses);
    }

    public void setNumberOfClasses(int _numberOfClasses) {
        numberOfClasses = _numberOfClasses;
        rootNode = new DecisionTreeNode(dimentions, numberOfClasses);
    }

    private DecisionTreeNode goDownToLeaf(FeaturesVector object) {
        DecisionTreeNode currentNode = rootNode;
        while (!currentNode.isLeaf()) {
            currentNode = currentNode.getNextTreeNode(object);
        }
        return currentNode;
    }

    public int getClassByObject(FeaturesVector object) {
        DecisionTreeNode leafNode = goDownToLeaf(object);
        return leafNode.getAnswer();
    }

    public double getProbability(FeaturesVector object, int classNumber) {
        DecisionTreeNode leafNode = goDownToLeaf(object);
        return leafNode.getProbability(classNumber);
    }

    public ArrayList<Double> getProbabilities(FeaturesVector object) {
        DecisionTreeNode leafNode = goDownToLeaf(object);
        return leafNode.getProbabilities();
    }

    private DecisionTreeNode styddyNode(DecisionTreeNode node, ArrayList<Pair<FeaturesVector, Integer>> objects, int depth) {
        boolean isLeaf = (depth > maxDepth);
        ArrayList<Double> probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(.0));
        for (int i = 0; i < objects.size(); ++i) {
            probabilities.set(objects.get(i).getValue(), probabilities.get(objects.get(i).getValue()) + 1.);
        }
        for (int i = 0; i < numberOfClasses; ++i) {
            probabilities.set(i, probabilities.get(i) / (double) numberOfClasses);
            if (probabilities.get(i) > .95) isLeaf = true;
        }
        node.setProbabilities(probabilities);
        if (isLeaf) {
            node.setIsLeaf(true);
        } else {
            int currentDimentionPlot = getRandomClassNumber();
            double treshold = rnd.nextDouble();
            node.setTresholdValue(treshold);
            node.addChildren(new DecisionTreeNode(dimentions, numberOfClasses), new DecisionTreeNode(dimentions, numberOfClasses));
            ArrayList<Pair<FeaturesVector, Integer>> leftObjects = new ArrayList<>(), rightObjects = new ArrayList<>();
            for (int i = 0; i < objects.size(); ++i) {
                if (objects.get(i).getKey().getFeature(currentDimentionPlot) <= treshold) {
                    leftObjects.add(objects.get(i));
                } else  {
                    rightObjects.add(objects.get(i));
                }
            }
            node.setLeftChild(styddyNode(node.getLeftChild(), leftObjects, depth + 1));
            node.setRightChild(styddyNode(node.getRightChild(), rightObjects, depth + 1));
        }
        return node;
    }

    public void studdy(ArrayList<Pair<FeaturesVector, Integer>> objects) {
        rootNode = styddyNode(rootNode, objects, 0);
    }

}
