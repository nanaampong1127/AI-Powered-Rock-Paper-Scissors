///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           AI Rock Paper Scissors with file saving database
// Course:          CS 200 Fall
//
// Author:          Nana Ampong
// Email:           nampong@wisc.edu
// Lecturer's Name: Jim Williams
//
///////////////////////////////// CITATIONS ////////////////////////////////////
//
// No one helped
//
///////////////////////////////// REFLECTION ///////////////////////////////////
//
// 1. How did you decide what parameters the read and write methods should accept?
// I chose the parameters to make the methods flexible and reusable. For the read
// method, I included a String filename so it can load data from any file without
// hardcoding the name. For the write method, I included an int[][] matrix to specify
// the data to save and a String filename to define the output file. This
// keeps the methods clear and changeable.
// 2. What challenges did you encounter while reading or writing the file contents?
// While reading, I had to handle cases where the file didn't exist or was formatted
// incorrectly. To fix this, I added default values and error messages to make sure
// the program didn't crash. For writing, it was a challenge to format the matrix
// properly, with the right spacing and line breaks, so it could be read back
// correctly. I also had some issues with file paths, as files weren't always saved
// or read from the right location, which required careful testing.
// 3. How did you resolve the challenges?
// To handle missing or badly formatted files, I made sure the program returned a
// default matrix and displayed clear error messages when an issue occurred. For
// formatting the matrix during writing, I added loops to ensure values were written
// with proper spacing and line breaks. To fix file path issues, I tested the program
// in different directories and used absolute paths when needed to ensure the files
// were saved and loaded correctly.
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Random;

/**
 * This program is a Rock, Paper, Scissors game where you play against an AI.
 * The AI learns your moves using a prediction matrix that updates as you play.
 * You can save the AI's learning to a file or load it from a file to continue where you left off.
 *
 * @author Nana Ampong
 */
public class H12CustomApp {


    /**
     * This program is a Rock, Paper, Scissors game where you play against an AI.
     * The AI learns from your moves and tries to predict your next move.
     * You can choose to save or load the AI's learning data from a file.
     * Example: You play the game, and the AI gets better at predicting your moves over time.
     * You can also save the AI's knowledge and load it when you play again.
     *
     * @param args Command-line arguments (not used in this program).
     */
    public static void main(String[] args) {
        try {
            Scanner scnr = new Scanner(System.in);
            Random rand = new Random();

            int[][] predictionMatrix = new int[3][3];
            String filename = "predictionData.txt";

            final int ROCK = 0;
            final int PAPER = 1;
            final int SCISSORS = 2;

            int lastMove = -1;

            int playerMove = 0;

            int gameendCount = 0;

            int playerPoints = 0;
            int aiPoints = 0;
            boolean usingFile = true;


            //interacts with the user to obtain multiple values.
            //then call methods that you write to do operations
            //on those values.  Use anything through chapter 10.
            //The separate TestH10CustomApp.java file contains
            //test cases called from testH10CustomApp() method.
            System.out.println("Welcome to this game of Rock, Paper, Scissors!");
            System.out.println("In this version you will play against an AI " +
                    "that will learn your patters as you play");
            System.out.println("\nYou need 30 points to win. Good Luck!");
            System.out.println("To end the game input -1");

            System.out.println("\nDo you want to load the AI's learning data? (y/n)");
            String response = scnr.nextLine();
            if (response.equalsIgnoreCase("y")) {
                predictionMatrix = initializePredictionMatrix(filename, scnr);
            } else {
                System.out.println("You've decided to play without using previous data");
                usingFile = false;
                predictionMatrix = new int[3][3];
                if (predictionMatrix[0].length == 1) {
                    gameendCount = 3;
                }
            }
            while (true) {
                if (gameendCount >= 3) {
                    return;
                }
                System.out.print("Please make a choice [R/P/S]: ");
                if (scnr.hasNextLine() && scnr.hasNext()) {
                    playerMove = letterToNum(scnr.next());
                } else {
                    System.out.println("No input provided.");
                    return;
                }

                if (playerMove == -2) {
                    System.out.println("Invalid Input, Try again\n");
                    gameendCount += 1;
                    continue;
                } else if (playerMove == -1) {
                    System.out.println("Your wins: " + playerPoints + " | " + "AI wins: " +
                            aiPoints);
                    System.out.println("Thanks for playing.");
                    return;
                }
                int aiMove = getAiMove(predictionMatrix, lastMove, rand);

                System.out.println("AI choose: " + moveTranslator(aiMove));
                gameendCount = 0;

                if (playerMove == aiMove) {
                    System.out.println("It's a Tie!");
                } else {
                    if (userWin(moveTranslator(playerMove), moveTranslator(aiMove))) {
                        System.out.println("You Win!");
                        playerPoints++;
                        System.out.println("Your wins: " + playerPoints + " | " + "AI wins: "
                                + aiPoints);
                    } else {
                        System.out.println("You Lose!");
                        aiPoints++;
                        System.out.println("Your wins: " + playerPoints + " | " + "AI wins: "
                                + aiPoints);
                    }
                }
                updateMatrix(predictionMatrix, playerMove, lastMove);
                lastMove = playerMove;

                if (playerPoints == 30) {
                    System.out.println("\nYou win the game nice job!");
                    if (usingFile) {
                        System.out.println("Do you want to save the AI's learning data? (y/n)");
                        response = scnr.next();
                        if (response.equalsIgnoreCase("y")) {
                            savePredictionMatrix(predictionMatrix, "predictionData.txt");
                            System.out.println("predictionData Saved");
                        }
                    }
                    scnr.close();
                    return;
                } else if (aiPoints == 30) {
                    System.out.println("\nYou lose, AI wins!");
                    if (usingFile) {
                        System.out.println("Do you want to save the AI's learning data? (y/n)");
                        response = scnr.next();
                        if (response.equalsIgnoreCase("y")) {
                            savePredictionMatrix(predictionMatrix, "predictionData.txt");
                            System.out.println("predictionData Saved");
                        }
                    }
                    scnr.close();
                    return;
                }

            }
        } catch (Exception e) {
            // Handle the exception by printing an error message
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();  // Optionally print the stack trace for debugging
        }


    }


    /**
     * Determines the AI's move based on the prediction matrix and the last move.
     * If there is no previous move, it selects a random move.
     *
     * @param predictionTable 2D array tracking counts between moves.
     * @param lastmove The last move made by the player.
     * @param rand Random for selecting moves when no prediction is available.
     * @return The AI's predicted move.
     */
    public static int getAiMove(int[][] predictionTable, int lastmove, Random rand){
        if(lastmove == -1){
            return rand.nextInt(3);
        }
        int predictedMove = movePredictor(predictionTable, lastmove, rand);
        switch(predictedMove){
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 0;
            default:
                return rand.nextInt(3);
        }
    }

    /**
     * Predicts the player's next move based on the data in transition matrix in the
     * prediction table.
     * Returns the move that has occurred most frequently after the last move.
     *
     * @param predictionTable 2D array tracking transition between moves.
     * @param lastMove The last move made by the player.
     * @param rand Random for selecting moves if there is no data.
     * @return The predicted next move of the player.
     */
    public static int movePredictor(int[][] predictionTable, int lastMove, Random rand){
        int [] transitionList = predictionTable[lastMove];
        int total = 0;
        int maxIndex = 0;

        for(int count:transitionList){
            total += count;
        }

        if(total == 0){
            return rand.nextInt(3);
        }

        for(int i = 0; i < transitionList.length; i++){
            if(transitionList[i] > transitionList[maxIndex]){
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    /**
     * Determines if the user's move beats the AI's move.
     *
     * @param user The user's move
     * @param comp The AI's move
     * @return true if the user wins, false otherwise.
     * @throws Exception If an invalid move is passed.
     */
    public static boolean userWin(String user, String comp){
        switch(user.toLowerCase()){
            case "rock":
                return comp.equalsIgnoreCase("scissors");
            case "paper":
                return comp.equalsIgnoreCase("rock");
            case "scissors":
                return comp.equalsIgnoreCase("paper");
        }
        return false;
    }

    /**
     * Converts a number move to its string version ("ROCK", "PAPER", or "SCISSORS").
     *
     * @param move The numeric version of a move (0 = ROCK, 1 = PAPER, 2 = SCISSORS).
     * @return The string of the move or "-2" for invalid input.
     */
    public static String moveTranslator(int move){
        switch (move){
            case 0:
                return "ROCK";
            case 1:
                return "PAPER";
            case 2:
                return "SCISSORS";
            default:
                return "-2";
        }
    }
    /**
     * Converts a letter input from the user to its corresponding numeric move.
     *
     * @param letterChoice The user's input ("R", "P", "S", or "-1" to end the game).
     * @return The numeric version of the move (0 = ROCK, 1 = PAPER, 2 = SCISSORS,
     *         -1 for ending the game, -2 for invalid input).
     */
    public static int letterToNum(String letterChoice){
        switch (letterChoice.toLowerCase()){
            case "r":
                return 0;
            case "p":
                return 1;
            case "s":
                return 2;
            case "-1":
                return -1;
            default:
                return -2;
        }
    }

    /**
     * Updates the prediction matrix by adding to the count of the given transition
     * from lastMove to playerMove.
     *
     * @param predictionMatrix 2D array tracking transitions between moves.
     * @param playerMove The player's most recent move.
     * @param lastMove The player's previous move.
     */
    public static void updateMatrix(int[][] predictionMatrix, int playerMove, int lastMove){
        if(lastMove != -1){
            predictionMatrix[lastMove][playerMove]++;
        }
    }

    /**
     * This method loads a prediction matrix from a file. It reads the file and fills a 3x3 matrix
     * with the integer values found in the file. If the file is not found, it prints an error
     * message.
     *
     * @param filename The name of the file to load the matrix from.
     * @return A 2D array (3x3 matrix) containing the loaded values, or a matrix with zeros if the
     * file is not found.
     */
    public static int[][] loadPredictionMatrix(String filename) {
        int[][] matrix = new int[3][3];
        File file = new File(filename);
        try (Scanner fileScanner = new Scanner(file)) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (fileScanner.hasNextInt()) {
                        matrix[i][j] = fileScanner.nextInt();
                    } else{
                        throw new FileNotFoundException();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
        return matrix;
    }


    /**
     * This method saves the given prediction matrix to a file. It writes
     * each row of the matrix to the file,
     * with each value separated by a space and each row on a new line.
     *
     * @param matrix The 2D array (prediction matrix) to save.
     * @param filename The name of the file where the matrix will be saved.
     * @throws IOException If there is an error writing to the file.
     */
    public static void savePredictionMatrix(int[][] matrix, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            for (int[] row : matrix) {
                for (int value : row) {
                    writer.print(value + " ");
                }
                writer.println();
            }
        }
    }

    /**
     * This method sets up the prediction matrix. It checks if the given file exists.
     * If the file exists, it loads the matrix from the file. If the file doesn't exist,
     * the user is asked if they want to create a new matrix. If they agree, a new matrix
     * with all zeros is created.
     *
     * @param filename The name of the file to load the matrix from.
     * @param scnr The Scanner used to get user input if the file is not found.
     * @return A 2D array for the prediction matrix (either loaded from the file or newly created).
     */
    public static int[][] initializePredictionMatrix(String filename, Scanner scnr) {
        File file = new File(filename);

        // Check if file exists
            if (file.exists()) {
                return loadPredictionMatrix(filename);
            } else {
                System.out.println(filename + " does not exist.");
                System.out.println("Would you like to create a new prediction matrix? (y/n)");

                String response = scnr.nextLine();
                if (response.equalsIgnoreCase("y")) {
                    System.out.println("Creating a new prediction matrix...");
                    return new int[3][3]; // Initialize with zeros
                } else {
                    System.out.println("Please create the file and try again.");
                }
            }

        return new int [1][1];
    }
}
