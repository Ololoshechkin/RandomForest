import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    double getJiniIndex(ArrayList<ObjectWithClass> objects) {
        double J = 0.0;
        double[] P = new double[numberOfClasses];
        for (int i = 0; i < numberOfClasses; ++i) {
            P[i] = 0.0;
        }
        for (int i = 0; i < objects.size(); ++i) {
            P[objects.get(i).getClassNumber()] += 1.0 / (double)objects.size();
        }
        for (int i = 0; i < numberOfClasses; ++i) {
            J += P[i] * P[i];
        }
        J = 1.0 - J;
        return J;
    }

    private SeparatingPlane getSeparatingPlane(ArrayList<ObjectWithClass> objects) {
        SeparatingPlane bestSeparatingPlane = new SeparatingPlane();
        double bestInf = 1e18;
        for (int d = 0; d < dimentions; ++d) {
            int finalD = d;
            Collections.sort(objects,
                    new Comparator<ObjectWithClass>() {
                        @Override
                        public int compare(ObjectWithClass obj1, ObjectWithClass obj2) {
                            double coord1 = obj1.getFeaturesVector().getFeature(finalD);
                            double coord2 = obj2.getFeaturesVector().getFeature(finalD);
                            return coord1 < coord2 ? -1 : (coord1 == coord2 ? 0 : 1);
                        }
                    }
            );
            ArrayList<ObjectWithClass> leftObjects = new ArrayList<ObjectWithClass>();
            ArrayList<ObjectWithClass> rightObjects = new ArrayList<ObjectWithClass>();
            for (int i = 0; i < objects.size(); ++i) {
                rightObjects.add(objects.get(objects.size() - i - 1));
            }
            int i = 0;
            while (i <= objects.size()) {
                double Inf = ((double) leftObjects.size()) * getJiniIndex(leftObjects) +
                        ((double) rightObjects.size()) * getJiniIndex(rightObjects);
                if (Inf < bestInf) {
                    bestInf = Inf;
                    bestSeparatingPlane = new SeparatingPlane(d, objects.get(i).getFeaturesVector().getFeature(d));
                }
                double curCoord = objects.get(i).getFeaturesVector().getFeature(d);
                while (i + 1 <= objects.size() && objects.get(i + 1).getFeaturesVector().getFeature(d) == curCoord) {
                    leftObjects.add(rightObjects.get(rightObjects.size() - 1));
                    rightObjects.remove(rightObjects.size() - 1);
                    ++i;
                }
                ++i;
            }
        }
        return bestSeparatingPlane;
    }

    private DecisionTreeNode styddyNode(DecisionTreeNode node, ArrayList<ObjectWithClass> objects, int depth) {
        boolean isLeaf = (depth > maxDepth);
        ArrayList<Double> probabilities = new ArrayList<Double>(numberOfClasses);
        for (int i = 0; i < numberOfClasses; ++i)
            probabilities.set(i, new Double(.0));
        for (int i = 0; i < objects.size(); ++i) {
            probabilities.set(objects.get(i).getClassNumber(), probabilities.get(objects.get(i).getClassNumber()) + 1.);
        }
        for (int i = 0; i < numberOfClasses; ++i) {
            probabilities.set(i, probabilities.get(i) / (double) numberOfClasses);
            if (probabilities.get(i) > .95) isLeaf = true;
        }
        node.setProbabilities(probabilities);
        if (isLeaf) {
            node.setIsLeaf(true);
        } else {
            SeparatingPlane currentSeparatingPlane = getSeparatingPlane(objects);
            node.setSeparatingPlane(currentSeparatingPlane);
            node.addChildren(new DecisionTreeNode(dimentions, numberOfClasses), new DecisionTreeNode(dimentions, numberOfClasses));
            ArrayList<ObjectWithClass> leftObjects = new ArrayList<>();
            ArrayList<ObjectWithClass> rightObjects = new ArrayList<>();
            for (int i = 0; i < objects.size(); ++i) {
                if (node.nextNodeIsLeft(objects.get(i).getFeaturesVector())) {
                    leftObjects.add(objects.get(i));
                } else {
                    rightObjects.add(objects.get(i));
                }
            }
            node.setLeftChild(styddyNode(node.getLeftChild(), leftObjects, depth + 1));
            node.setRightChild(styddyNode(node.getRightChild(), rightObjects, depth + 1));
        }
        return node;
    }

    public void studdy(ArrayList<ObjectWithClass> objects) {
        rootNode = styddyNode(rootNode, objects, 0);
    }

}
