import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by Vadim on 26.01.17.
 */
public class DecisionTree {
    private int dimentions;
    private int numberOfClasses;
    private DecisionTreeNode rootNode;
    private int maxDepth = 20;
    private double tresholdProbability = 0.99;
    private Random rnd;
    private double infinity = 1e18;
    private ArrayList<Integer> allowedDimentions;

    DecisionTree() {
        dimentions = 1;
        numberOfClasses = 1;
        rootNode = new DecisionTreeNode(dimentions, numberOfClasses);
        allowedDimentions = new ArrayList<>();
        for (int i = 0; i < dimentions; ++i) {
            allowedDimentions.add(new Integer(i));
        }
    }

    private int getRandomClassNumber() {
        return Math.abs(rnd.nextInt()) % numberOfClasses;
    }

    public void setDimentions(int _dimentions) {
        dimentions = _dimentions;
        rootNode = new DecisionTreeNode(dimentions, numberOfClasses);
        allowedDimentions = new ArrayList<>();
        for (int i = 0; i < dimentions; ++i) {
            allowedDimentions.add(new Integer(i));
        }
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

    private double getJiniIndex(ArrayList<ObjectWithClass> objects, int l, int r) {
        if (r == l - 1) return 0.0;
        double J = 0.0;
        double[] P = new double[numberOfClasses];
        for (int i = 0; i < numberOfClasses; ++i) {
            P[i] = 0.0;
        }
        for (int i = l; i <= r; ++i) {
            P[objects.get(i).getClassNumber()] += 1.0;
        }
        for (int i = 0; i < numberOfClasses; ++i) {
            J += square(P[i] / (double) (r - l + 1));
        }
        J = 1.0 - J;
        return J;
    }

    private SeparatingPlane getSeparatingPlane(ArrayList<ObjectWithClass> objects) {
        SeparatingPlane bestSeparatingPlane = new SeparatingPlane();
        double bestInf = -infinity;
        for (int d : allowedDimentions) {
            int finalD = d;
            Collections.sort(objects, (ObjectWithClass obj1, ObjectWithClass obj2) -> {
                    double coord1 = obj1.getFeaturesVector().getFeature(finalD);
                    double coord2 = obj2.getFeaturesVector().getFeature(finalD);
                    return coord1 < coord2 ? -1 : (coord1 == coord2 ? 0 : 1);
                }
            );
            double leftJiniIndex = 0.0;
            double rightJiniIndex = getJiniIndex(objects, 0, objects.size() - 1);
            double fullJiniIndex = rightJiniIndex;
            double[] leftN = new double[numberOfClasses], rightN = new double[numberOfClasses];
            for (int i = 0; i < numberOfClasses; ++i) {
                leftN[i] = 0.0;
                rightN[i] = 0.0;
            }
            for (int i = 0; i < objects.size(); ++i) {
                rightN[objects.get(i).getClassNumber()] += 1.0;
            }
            int i = 0;
            while (i <= objects.size()) {
                double Inf = fullJiniIndex - ((double) i) / ((double) objects.size()) * leftJiniIndex  -
                        ((double) objects.size() - i) / ((double) objects.size()) * rightJiniIndex;
                if (Inf > bestInf) {
                    bestInf = Inf;
                    bestSeparatingPlane = new SeparatingPlane(
                            d,
                            (i == 0 ? -infinity : objects.get(i - 1).getFeaturesVector().getFeature(d))
                    );
                }
                if (i == objects.size()) break;
                double curCoord = objects.get(i).getFeaturesVector().getFeature(d);
                do {
                    ++i;
                    // <recalc indexes>
                    leftJiniIndex = 1.0 - leftJiniIndex;
                    rightJiniIndex = 1.0 - rightJiniIndex;
                    leftJiniIndex *= square((i - 1.0) / (double) i);
                    rightJiniIndex *= square((objects.size() - i + 1.0) / (objects.size() - i + 0.0));
                    int classNmb = objects.get(i - 1).getClassNumber();
                    leftJiniIndex += square((leftN[classNmb] + 1.0) / (i + 0.0)) -
                            square((leftN[classNmb]) / (i + 0.0));
                    rightJiniIndex += square((rightN[classNmb] - 1.0) / (objects.size() - i + 0.0)) -
                            square((rightN[classNmb]) / (objects.size() - i + 0.0));
                    leftJiniIndex = 1.0 - leftJiniIndex;
                    rightJiniIndex = 1.0 - rightJiniIndex;
                    leftN[classNmb] += 1.0;
                    rightN[classNmb] -= 1.0;
                    // </recalc indexes>
                } while (i + 1 < objects.size() &&
                        objects.get(i + 1).getFeaturesVector().getFeature(d) == curCoord);
            }
        }
        return bestSeparatingPlane;
    }

    private DecisionTreeNode stydyNode(DecisionTreeNode node, ArrayList<ObjectWithClass> objects, int depth) {
        boolean isLeaf = (depth > maxDepth || objects.isEmpty());
        ArrayList<Double> probabilities = new ArrayList<>();
        for (int i = 0; i < numberOfClasses; ++i) {
            probabilities.add(new Double(.0));
        }
        for (int i = 0; i < objects.size(); ++i) {
            probabilities.set(
                    objects.get(i).getClassNumber(),
                    probabilities.get(objects.get(i).getClassNumber()) + 1.0
            );
        }
        for (int i = 0; i < numberOfClasses; ++i) {
            probabilities.set(i, probabilities.get(i) / (double) objects.size());
            if (probabilities.get(i) > tresholdProbability)
                isLeaf = true;
        }
        node.setProbabilities(probabilities);
        if (isLeaf) {
            node.setIsLeaf(true);
        } else {
            SeparatingPlane currentSeparatingPlane = getSeparatingPlane(objects);
            node.setSeparatingPlane(currentSeparatingPlane);
            node.addChildren(
                    new DecisionTreeNode(dimentions, numberOfClasses),
                    new DecisionTreeNode(dimentions, numberOfClasses)
            );
            ArrayList<ObjectWithClass> leftObjects = new ArrayList<>();
            ArrayList<ObjectWithClass> rightObjects = new ArrayList<>();
            for (int i = 0; i < objects.size(); ++i) {
                if (node.nextNodeIsLeft(objects.get(i).getFeaturesVector())) {
                    leftObjects.add(new ObjectWithClass(objects.get(i)));
                } else {
                    rightObjects.add(new ObjectWithClass(objects.get(i)));
                }
            }
            node.setLeftChild(stydyNode(node.getLeftChild(), leftObjects, depth + 1));
            node.setRightChild(stydyNode(node.getRightChild(), rightObjects, depth + 1));
        }
        return node;
    }

    public void study(ArrayList<ObjectWithClass> objects) {
        rootNode = stydyNode(rootNode, objects, 0);
    }

    private double square(double x) {
        return x * x;
    }

    private void getAllSeparatingPlanes(DecisionTreeNode node, ArrayList<SeparatingPlane> allSeparatingPlanes) {
        if (node.isLeaf()) return;
        allSeparatingPlanes.add(node.getSeparatingPlane());
        getAllSeparatingPlanes(node.getLeftChild(), allSeparatingPlanes);
        getAllSeparatingPlanes(node.getRightChild(), allSeparatingPlanes);
    }

    public void visualizeTree(ArrayList<ObjectWithClass> objects) throws InterruptedException {
        VisualiserWindow window = new VisualiserWindow(500, 500);
        window.setPoints(objects);
        ArrayList<SeparatingPlane> allSeparatingPlanes = new ArrayList<>();
        getAllSeparatingPlanes(rootNode, allSeparatingPlanes);
        window.setSeparatingPlanes(allSeparatingPlanes);
        window.setDecisionTree(this);
        Thread windowThread = new Thread(window);
        windowThread.start();
    }

    public void setAllowedDimentions(ArrayList<Integer> _allowedDimentions) {
        allowedDimentions = new ArrayList<Integer>(_allowedDimentions);
    }

}
