import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Vadim on 26.01.17.
 */
public class Main {

    private static int getClass(double x, double y) {
        if ((x - 0.5) * (x - 0.5) + (y - 0.5) * (y - 0.5) <= 0.1)
            return 2;
        return (y >= -(x - 0.5) * (x - 0.5) + 0.5 ? 1 : 0);
    }

    private static int getNumberOfClasses() {
        return 3;
    }

    private static int getStyddyingSamplesNumber() {
        return 5000;
    }

    private static int getValidationSamplesNumber() {
        return 7000;
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();
        DecisionTree decisionTree = new DecisionTree();
        decisionTree.setDimentions(2);
        decisionTree.setNumberOfClasses(getNumberOfClasses());
        ArrayList<ObjectWithClass> points = new ArrayList<>();
        for (int i = 0; i < getStyddyingSamplesNumber(); ++i) {
            double x = rnd.nextDouble(), y = rnd.nextDouble();
            int classNmb = getClass(x, y);
            FeaturesVector curFeaturesVector = new FeaturesVector(2);
            curFeaturesVector.setFeature(0, x);
            curFeaturesVector.setFeature(1, y);
            points.add(new ObjectWithClass(curFeaturesVector, classNmb));
        }
        System.out.println("wait, please...");
        decisionTree.studdy(points);
        decisionTree.visualizeTree(points);
        int goodAnswers = 0;
        for (int i = 0; i < getValidationSamplesNumber(); ++i) {
            double x = rnd.nextDouble(), y = rnd.nextDouble();
            int expectedClassNmb = getClass(x, y);
            FeaturesVector curFeaturesVector = new FeaturesVector(2);
            curFeaturesVector.setFeature(0, x);
            curFeaturesVector.setFeature(1, y);
            int curAns = decisionTree.getClassByObject(curFeaturesVector);
            goodAnswers += (curAns == expectedClassNmb ? 1 : 0);
        }
        System.out.println("correct : " + 100.0 * (double) goodAnswers / getValidationSamplesNumber() + "%");
        System.out.println("now start entering your points (x, y) from 0.0 to 1.0:");
        while (true) {
            String xString, yString;
            xString = sc.next();
            yString = sc.next();
            xString = xString.replace('.', ',');
            yString = yString.replace('.', ',');
            double x = Double.parseDouble(xString), y = Double.parseDouble(yString);
            FeaturesVector obj = new FeaturesVector(2);
            obj.setFeature(0, x);
            obj.setFeature(1, y);
            System.out.println("class = " + decisionTree.getClassByObject(obj));
        }
    }

}
