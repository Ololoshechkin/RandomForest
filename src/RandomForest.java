import java.util.ArrayList;
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

    RandomForest() {
        dimentions = 1;
        treesCommitteeSize = 10;
        treesCommittee = new ArrayList<>();
        m = (int) Math.max(Math.sqrt((double) dimentions), 2.0);
        random = new Random();
    }

    public void setDimentions(int _dimentions) {
        dimentions = _dimentions;
        m = (int) Math.max(Math.sqrt((double) dimentions), 2.0);
    }

    public void setNumberOfClasses(int _numberOfClasses) {
        numberOfClasses = _numberOfClasses;
    }

    public void setM(int _m) {
        m = _m;
    }

    public void setTreesCommiteeSize(int size) {
        treesCommitteeSize = size;
    }

    void studdy(ArrayList<ObjectWithClass> samples) {
        for (int it = 0; it < treesCommitteeSize; ++it) {
            ArrayList<ObjectWithClass> currentSamplesSelection = new ArrayList<>();
            ArrayList<Integer> currentAllowedDimentions = new ArrayList<>();
            HashSet<Integer> allowedDimentionsSet;
            for (int i = 0; i < samples.size(); ++i) {
                currentSamplesSelection.add(new ObjectWithClass(samples.get(random.nextInt() % samples.size())));
            }
            for (int i = 0; i < m; ++i) {
                currentAllowedDimentions.add(i);
            }
            DecisionTree currentTree = new DecisionTree();
            currentTree.setDimentions(dimentions);
            currentTree.setAllowedDimentions(currentAllowedDimentions);
            currentTree.setNumberOfClasses(numberOfClasses);
            currentTree.study(currentSamplesSelection);
            treesCommittee.add(currentTree);
        }
    }
}
