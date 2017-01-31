import javafx.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Vadim on 26.01.17.
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Pair<Double, Double>> points = new ArrayList<>(10);
        for (int i = 0; i < 10; ++i) {
            double x = sc.nextDouble(), y = sc.nextDouble();
            Pair<Double, Double> pnt = new Pair<>(x, y);
            points.set(i, pnt);
        }
        DecisionTree = new DecisionTree();
    }
}
