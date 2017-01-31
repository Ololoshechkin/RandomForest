import java.util.*;

/**
 * Created by Vadim on 26.01.17.
 */
public class DecisionTree {
    private int dimentions;
    private int numberOfClasses;
    private DecisionTreeNode rootNode;
    private int maxDepth = 20;
    private Random rnd;
    private double infinity = 1e18;

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

    private double getJiniIndex(ArrayList<ObjectWithClass> objects, int l, int r) {
        double J = 0.0;
        double[] P = new double[numberOfClasses];
        for (int i = 0; i < numberOfClasses; ++i) {
            P[i] = 0.0;
        }
        for (int i = l; i <= r; ++i) {
            P[objects.get(i).getClassNumber()] += 1.0 / (double) (r - l + 1);
        }
        for (int i = 0; i < numberOfClasses; ++i) {
            J += square(P[i]);
        }
        J = 1.0 - J;
        //System.out.println("JiniIndex = " + J + " , obj.sz = " + (r - l + 1));
        return J;
    }

    private SeparatingPlane getSeparatingPlane(ArrayList<ObjectWithClass> objects) {
        SeparatingPlane bestSeparatingPlane = new SeparatingPlane();
        double bestInf = infinity;
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
            /*ArrayList<ObjectWithClass> leftObjects = new ArrayList<ObjectWithClass>();
            ArrayList<ObjectWithClass> rightObjects = new ArrayList<ObjectWithClass>();
            for (int i = 0; i < objects.size(); ++i) {
                rightObjects.add(objects.get(objects.size() - i - 1));
            }*/
            /*double leftJiniIndex = 0.0;
            double rightJiniIndex = 1.0;
            double[] leftN = new double[numberOfClasses], rightN = new double[numberOfClasses];
            for (int i = 0; i < numberOfClasses; ++i) {
                leftN[i] = 0.0;
                rightN[i] = 0.0;
            }
            for (int i = 0; i < objects.size(); ++i) {
                rightN[objects.get(i).getClassNumber()] += 1.0;
            }
            for (int i = 0; i < numberOfClasses; ++i) {
                rightJiniIndex -= square(rightN[i] / (double) objects.size());
            }*/
            int i = 0;
            while (i <= objects.size()) {
                double Inf = ((double) i) * getJiniIndex(objects, 0, i - 1) +
                        ((double) objects.size() - i) * getJiniIndex(objects, i, objects.size() - 1);
                if (Inf < bestInf) {
                    bestInf = Inf;
                    bestSeparatingPlane = new SeparatingPlane(
                            d,
                            (i == 0 ? -infinity : objects.get(i - 1).getFeaturesVector().getFeature(d))
                    );
                }
                if (i == objects.size()) break;
                double curCoord = objects.get(i).getFeaturesVector().getFeature(d);
                while (i + 1 < objects.size() && objects.get(i + 1).getFeaturesVector().getFeature(d) == curCoord) {
                    ++i;
                    /*leftJiniIndex *= square(((double) (i - 1)) / ((double) i));
                    rightJiniIndex *= square(((double) (objects.size() - i - 1)) / ((double) (objects.size() - i)));
                    int classNmb = objects.get(i).getClassNumber();
                    leftJiniIndex += square((leftN[classNmb] + 1.0) / (double) i);
                    rightJiniIndex += square((rightN[classNmb] - 1.0) / (double) (objects.size() - i));
                    leftN[classNmb] += 1.0;
                    rightN[classNmb] -= 1.0;*/
                }
                ++i;
            }
        }
        //System.out.println("plane : index = " + bestSeparatingPlane.getDimentionNumber() +" , treshold = " + bestSeparatingPlane.getTresholdValue());
        return bestSeparatingPlane;
    }

    private DecisionTreeNode styddyNode(DecisionTreeNode node, ArrayList<ObjectWithClass> objects, int depth) {
        boolean isLeaf = (depth > maxDepth);
        ArrayList<Double> probabilities = new ArrayList<>();
        for (int i = 0; i < numberOfClasses; ++i) {
            probabilities.add(new Double(.0));
        }
        for (int i = 0; i < objects.size(); ++i) {
            probabilities.set(
                    objects.get(i).getClassNumber(),
                    probabilities.get(objects.get(i).getClassNumber()) + 1.0 / (double) objects.size()
            );
        }
        for (int i = 0; i < numberOfClasses; ++i) {
            probabilities.set(i, probabilities.get(i) / (double) numberOfClasses);
            if (probabilities.get(i) > .95) {
                isLeaf = true;
                //System.out.println("> 0.95 in depth : " + depth + " , and equals : " + probabilities.get(i));
            }
        }
        node.setProbabilities(probabilities);
        if (isLeaf) {
            node.setIsLeaf(true);
            //System.out.println("leaf in depth = " + depth);
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

    private double square(double x) {
        return x * x;
    }

}
