/* 
Name: Kathryn von Schlegell
Date: 08/14/2020
Course: CPSC 50100
Semester: Summer 2020
Assignment: Machine Learning
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class NearestNeighbor {

	static double[][] trainingAttribute;
	static double[][] testingAttribute;
	static String[] trainingClass;
	static String[] testingClass;

	public static void main(String[] args) {

		outputHeader();

		// Variable names for the training and test files
		String trainingData = "";
		String testingData = "";

		Scanner input = new Scanner(System.in);
		System.out.print("\nEnter the name of the training file: ");
		trainingData = input.nextLine();
		// Opening and reading training file into an array list
		ArrayList<String> trainingItems = ReadFile(trainingData);

		// Passing in values to variables
		trainingAttribute = new double[trainingItems.size()][4];
		trainingClass = new String[trainingItems.size()];

		ParseData(trainingItems, trainingAttribute, trainingClass);

		System.out.print("Enter the name of the testing file: ");
		testingData = input.nextLine();
		// Close scanner
		input.close();
		// Opening and reading testing file into an array list
		ArrayList<String> testingItems = ReadFile(testingData);

		// Passing in values to variables
		testingAttribute = new double[trainingItems.size()][4];
		testingClass = new String[trainingItems.size()];

		ParseData(testingItems, testingAttribute, testingClass);

		String[] predictedFlower = PredictFlowers();

		outputResults(predictedFlower);
	}

	private static String outputHeader() {

		String outputHeader = "Programming Fundamentals" + "\nNAME: Kathryn von Schlegell"
				+ "\nPROGRAMMING ASSIGNMENT 3";
		System.out.println(outputHeader);
		return outputHeader;

	}

	private static ArrayList<String> ReadFile(String fileName) {

		ArrayList<String> dataRecord = new ArrayList<String>();
		try {
			Scanner fileScan = new Scanner(new File(fileName));
			while (fileScan.hasNext()) {
				String line = fileScan.nextLine();
				dataRecord.add(line);
			}
			fileScan.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		return dataRecord;
	}

	private static void ParseData(ArrayList<String> dataRecord, double[][] data, String[] classLabel) {

		for (int i = 0; i < dataRecord.size(); i++) {
			String[] items = dataRecord.get(i).split(",");
			data[i][0] = Double.parseDouble(items[0]);
			data[i][1] = Double.parseDouble(items[1]);
			data[i][2] = Double.parseDouble(items[2]);
			data[i][3] = Double.parseDouble(items[3]);
			classLabel[i] = items[4];
		}
	}

	private static double ComputeNearestNeighbor(int trainingDataIndex, int testingDataIndex) {
		double distance = 0.0;

		for (int i = 0; i < 4; i++) {
			distance += Math.pow(trainingAttribute[trainingDataIndex][i] - testingAttribute[testingDataIndex][i], 2);
		}
		distance = Math.sqrt(distance);
		return distance;
	}

	// Using the distance formula to find the best flower.
	// The shortest distance will determine class label
	private static String DetermineBestFlower(int testingDataIndex) {

		double minDistance = Double.MAX_VALUE;
		String bestFlower = "";

		for (int trainingDataIndex = 0; trainingDataIndex < trainingClass.length; trainingDataIndex++) {
			double distance = ComputeNearestNeighbor(trainingDataIndex, testingDataIndex);
			if (distance < minDistance) {
				minDistance = distance;
				bestFlower = trainingClass[trainingDataIndex];
			}
		}
		return bestFlower;
	}

	// Iterating through the testing class and going to determineBestFlower method
	// to find the predicted flower
	private static String[] PredictFlowers() {

		String[] predictedFlower = new String[testingClass.length];
		for (int i = 0; i < testingClass.length; i++) {
			predictedFlower[i] = DetermineBestFlower(i);
		}
		return predictedFlower;
	}

	private static void outputResults(String[] predictedFlower) {

		int correctFlowers = 0;
		System.out.println("\nEX#: \tTRUE LABEL, \tPREDICTED LABEL");

		for (int i = 0; i < predictedFlower.length; i++) {
			// Formatted the columns for a cleaner look
			String outputString = String.format("%d: \t%s\t%s", (i + 1), testingClass[i], predictedFlower[i]);
			System.out.println(outputString);
			if (testingClass[i].equals(predictedFlower[i])) {
				correctFlowers++;
			}
		}
		double accuracy = (double) correctFlowers / predictedFlower.length;
		System.out.println("ACCURACY: " + accuracy);
	}
}
