import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * Created by Vadim on 01.02.17.
 */
public class RandomForest {
    private int dimentions;
    private int numberOfClasses;
    private int treesCommitteeSize;
    private ArrayList<DecisionTree> treesCommittee;
    private int m;
    private Random random;
    private double[] probability;

    RandomForest() {
        dimentions = 1;
        treesCommitteeSize = 10;
        treesCommittee = new ArrayList<>();
        m = (int) Math.max(Math.sqrt((double) dimentions), 2.0);
        random = new Random();
        probability = new double[numberOfClasses];
    }

    public void setDimentions(int _dimentions) {
        dimentions = _dimentions;
        m = (int) Math.max(Math.sqrt((double) dimentions), 2.0);
    }

    public void setNumberOfClasses(int _numberOfClasses) {
        numberOfClasses = _numberOfClasses;
        probability = new double[numberOfClasses];
    }

    public void setM(int _m) {
        m = _m;
    }

    public void setTreesCommiteeSize(int size) {
        treesCommitteeSize = size;
    }

    public void studdy(ArrayList<ObjectWithClass> samples) {
        ArrayList<Integer> allDimentions = new ArrayList<>();
        for (int i = 0; i < dimentions; ++i) {
            allDimentions.add(i);
        }
        for (int it = 0; it < treesCommitteeSize; ++it) {
            ArrayList<ObjectWithClass> currentSamplesSelection = new ArrayList<>();
            ArrayList<Integer> currentAllowedDimentions = new ArrayList<>();
            for (int i = 0; i < samples.size(); ++i) {
                currentSamplesSelection.add(new ObjectWithClass(samples.get(Math.abs(random.nextInt()) % samples.size())));
            }
            Collections.shuffle(allDimentions);
            for (int i = 0; i < m; ++i) {
                currentAllowedDimentions.add(allDimentions.get(i));
            }
            DecisionTree currentTree = new DecisionTree();
            currentTree.setDimentions(dimentions);
            currentTree.setAllowedDimentions(currentAllowedDimentions);
            currentTree.setNumberOfClasses(numberOfClasses);
            currentTree.study(currentSamplesSelection);
            treesCommittee.add(currentTree);
        }
    }

    public double getProbability(int classNumber) {
        return 1.0 / (double) numberOfClasses;
    }

    public int getClassByObject(FeaturesVector object) {
        for (int i = 0; i < numberOfClasses; ++i) {
            probability[i] = 0.0;
        }
        for (int i = 0; i < treesCommitteeSize; ++i) {
            ArrayList<Double> currentProbabilities = treesCommittee.get(i).getProbabilities(object);
            for (int j = 0; j < numberOfClasses; ++j) {
                probability[j] += currentProbabilities.get(j);
            }
        }
        int answer = 0;
        for (int i = 1; i < numberOfClasses; ++i) {
            if (probability[i] > probability[answer]) {
                answer = i;
            }
        }
        return answer;
    }

}

/*
D. m = sqrt(D)
m rand from d (k times) :
a) shuffle : O(k*(D+m))
b) set+brain || seg tree : O(k * (D * log + m * log))
c) persistent seg tree : O(D * log + k * m * log) + O(D * log) mem + I'm fucking lazy bitch to write it)
*/
