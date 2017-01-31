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
        ArrayList<ObjectWithClass> points = new ArrayList<>();
        for (int i = 0; i < 1000; ++i) {
            double x = rnd.nextDouble(), y = rnd.nextDouble();
            int classNmb = (x * x + y * y <= 0.5 ? 1 : 0);
            FeaturesVector curFeaturesVector = new FeaturesVector(2);
            curFeaturesVector.setFeature(0, x);
            curFeaturesVector.setFeature(1, y);
            points.add(new ObjectWithClass(curFeaturesVector, classNmb));
        }
        System.out.println("wait, please...");
        decisionTree.studdy(points);
        int goodAnswers = 0;
        for (int i = 0; i < 5000; ++i) {
            double x = rnd.nextDouble(), y = rnd.nextDouble();
            int expectedClassNmb = (x <= y ? 1 : 0);
            FeaturesVector curFeaturesVector = new FeaturesVector(2);
            curFeaturesVector.setFeature(0, x);
            curFeaturesVector.setFeature(1, y);
            int curAns = decisionTree.getClassByObject(curFeaturesVector);
            goodAnswers += (curAns == expectedClassNmb ? 1 : 0);
        }
        System.out.println("correct : " + 100.0 * (double) goodAnswers / 5000.0 + "%");
        System.out.println("now start :");
        while (true) {
            double x = sc.nextDouble(), y = sc.nextDouble();
            FeaturesVector obj = new FeaturesVector(2);
            obj.setFeature(0, x);
            obj.setFeature(1, y);
            System.out.println("class = " + decisionTree.getClassByObject(obj));
        }
    }

}
