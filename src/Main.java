import javafx.util.Pair;

import javax.xml.bind.ValidationEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Vadim on 26.01.17.
 */
public class Main {

    public static int NumberOfClasses = 4;

    private static int getClass(double x, double y) {
        if (0.75 - x <= y && y <= 0.92 - x) return 3;
        if ((x - 0.5) * (x - 0.5) + (y - 0.5) * (y - 0.5) <= 0.1)
            return 2;
        return (y >= -(x - 0.5) * (x - 0.5) + 0.5 ? 1 : 0);
    }

    public static void main(String[] args) throws InterruptedException {
        int StyddyingSamplesNumber = 4000;
        int ValidationSamplesNumber = 7000;

        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        System.out.println("If you want default params, then enter '-', else enter another word: ");
        if (sc.next().compareTo("-") != 0) {
            System.out.println("Enter studdying samples number :");
            StyddyingSamplesNumber = sc.nextInt();
            if (StyddyingSamplesNumber < 10) StyddyingSamplesNumber = 10;
            System.out.println("Enter validation samples number :");
            ValidationSamplesNumber = sc.nextInt();
            if (ValidationSamplesNumber < 10) ValidationSamplesNumber = 10;
        }
        System.out.println("wait, please...");

        DecisionTree decisionTree = new DecisionTree();
        decisionTree.setDimentions(2);
        decisionTree.setNumberOfClasses(NumberOfClasses);
        ArrayList<ObjectWithClass> points = new ArrayList<>();
        for (int i = 0; i < StyddyingSamplesNumber; ++i) {
            double x = rnd.nextDouble(), y = rnd.nextDouble();
            int classNmb = getClass(x, y);
            FeaturesVector curFeaturesVector = new FeaturesVector(2);
            curFeaturesVector.setFeature(0, x);
            curFeaturesVector.setFeature(1, y);
            points.add(new ObjectWithClass(curFeaturesVector, classNmb));
        }
        decisionTree.study(points);
        decisionTree.visualizeTree(points);
        RandomForest randomForest = new RandomForest();
        randomForest.setDimentions(2);
        randomForest.setNumberOfClasses(NumberOfClasses);
        randomForest.setTreesCommiteeSize(10);
        randomForest.studdy(points);
        int goodAnswers = 0;
        int goodForestAnswers = 0;
        for (int i = 0; i < ValidationSamplesNumber; ++i) {
            double x = rnd.nextDouble(), y = rnd.nextDouble();
            int expectedClassNmb = getClass(x, y);
            FeaturesVector curFeaturesVector = new FeaturesVector(2);
            curFeaturesVector.setFeature(0, x);
            curFeaturesVector.setFeature(1, y);
            int curAns = decisionTree.getClassByObject(curFeaturesVector);
            int curForestAns = randomForest.getClassByObject(curFeaturesVector);
            goodAnswers += (curAns == expectedClassNmb ? 1 : 0);
            goodForestAnswers += (curForestAns == expectedClassNmb ? 1 : 0);
        }
        System.out.println("one tree  :  correct : " + 100.0 * (double) goodAnswers / ValidationSamplesNumber + "%");
        System.out.println("forest  :  correct : " + 100.0 * (double) goodForestAnswers / ValidationSamplesNumber + "%");
        System.out.println("now start entering your points (x, y) from 0.0 to 1.0:");
        while (true) {
            String xString, yString;
            xString = sc.next();
            yString = sc.next();
            xString = xString.replace(',', '.');
            yString = yString.replace(',', '.');
            double x = Double.parseDouble(xString), y = Double.parseDouble(yString);
            FeaturesVector obj = new FeaturesVector(2);
            obj.setFeature(0, x);
            obj.setFeature(1, y);
            System.out.println("class = " + decisionTree.getClassByObject(obj));
        }
    }

}
