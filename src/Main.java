import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Vadim on 26.01.17.
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();
        DecisionTree decisionTree = new DecisionTree();
        decisionTree.setDimentions(2);
        decisionTree.setNumberOfClasses(2);
        ArrayList<ObjectWithClass> points = new ArrayList<>(1000);
        for (int i = 0; i < 1000; ++i) {
            double x = rnd.nextDouble(), y = rnd.nextDouble();
            int classNmb = y <= x ? 1 : 0;
            FeaturesVector curFeaturesVector = new FeaturesVector(2);
            curFeaturesVector.setFeature(0, x);
            curFeaturesVector.setFeature(1, y);
            points.set(i, new ObjectWithClass(FeaturesVector, classNmb));
        }
        decisionTree.studdy(points);
                
    }

}
