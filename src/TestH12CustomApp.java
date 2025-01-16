///////////////////////// TOP OF FILE COMMENT BLOCK ////////////////////////////
//
// Title:           AI Rock Paper Scissors with file saving database
// Course:          CS 200 Fall24
//
// Author:          Nana
// Email:           nampong@wisc.edu
// Lecturer's Name: Jim Williams
//
///////////////////////////////// CITATIONS ////////////////////////////////////
//
// No one helped
//
/////////////////////////////// 80 COLUMNS WIDE ////////////////////////////////


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * This is the test bench that contains testing methods for the H12CustomApp class.
 * The createTestDataFile and readTestDataFile are private testing methods intended to 
 * be used within the test cases.
 *
 * All the test cases within the testH12CustomApp method should be changed to test the 
 * methods in your H12CustomApp class.
 *
 * @author Jim Williams
 * @author //Nana Ampong
 */
public class TestH12CustomApp {

    /**
     * This method runs the selected tests.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        testH12CustomApp();
    }

    /**
     * This is a testing method to create a file with the specified name and fileContents
     * to be used by other testing methods. On a FileNotFoundException a stack trace is
     * printed and then returns.
     *
     * @param testDataFilename The filename of the testing file to create.
     * @param fileContents The data to put into the file.
     */
    private static void createTestDataFile(String testDataFilename, String fileContents) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(testDataFilename);
            writer.print(fileContents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }

    /**
     * This is a testing method to read and return the entire contents of the specified file to
     * be used soley by other testing methods.
     * On a FileNotFoundException a stack trace is printed and then "" returned.
     *
     * @param dataFilename The name of the file to read.
     * @return The contents of the file or "" on error.
     */
    private static String readTestDataFile(String dataFilename) {
        File file = new File(dataFilename);
        Scanner input = null;
        String contents = "";
        try {
            input = new Scanner( file);
            while (input.hasNextLine()) {
                contents += input.nextLine() + "\n"; //assuming all lines end with newline
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (input != null) input.close();
        }
        return contents;
    }

    /**
     * Tests that the H12CustomApp read input and write output methods handle
     * the cases described in their method header comments.
     *
     * @return true for passing all testcases, false otherwise
     */
    public static boolean testH12CustomApp() {
        boolean error = false;
        //NOTE: DELETE OR SIGNIFICANTLY ADAPT THESE TEST CASES TO TEST YOUR READ FILE
        //AND WRITE FILE METHODS.
        
        {  // Test reading a valid file with a prediction matrix
            String fileToRead = "validMatrix.txt";
            String fileContents = "1 2 3\n4 5 6\n7 8 9\n";
            createTestDataFile(fileToRead, fileContents);

            // Load the matrix using the loadPredictionMatrix method
            int[][] resultMatrix = H12CustomApp.loadPredictionMatrix(fileToRead);
            int[][] expectedMatrix = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };

            // Compare the loaded matrix to the expected matrix
            boolean matricesEqual = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (resultMatrix[i][j] != expectedMatrix[i][j]) {
                        matricesEqual = false;
                        break;
                    }
                }
            }

            if (!matricesEqual) {
                error = true;
                System.out.println("loadPredictionMatrix 1) failed: matrices do not match");
            } else {
                System.out.println("loadPredictionMatrix 1) success");
            }

            // Cleanup the file after test
            File file = new File(fileToRead);
            file.delete();
        }


        {  // Test loading from a file that doesn't exist
            String fileToRead = "nonExistentFile.txt";

            // Try loading the matrix using the loadPredictionMatrix method
            int[][] resultMatrix = H12CustomApp.loadPredictionMatrix(fileToRead);
            int[][] expectedMatrix = new int[3][3]; // Matrix of all zeros

            // Compare the loaded matrix to the expected matrix
            boolean matricesEqual = true;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (resultMatrix[i][j] != expectedMatrix[i][j]) {
                        matricesEqual = false;
                        break;
                    }
                }
            }

            if (!matricesEqual) {
                error = true;
                System.out.println("loadPredictionMatrix 2) failed: matrices do not match");
            } else {
                System.out.println("loadPredictionMatrix 2) success");
            }
        }


        {  // Test saving a valid matrix to a file
            String fileToWrite = "validMatrixOutput.txt";
            int[][] matrixToWrite = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };

            // Save the matrix to the file
            try {
                H12CustomApp.savePredictionMatrix(matrixToWrite, fileToWrite);
            } catch (IOException e) {
                error = true;
                System.out.println("savePredictionMatrix 1) failed to save matrix");
            }

            // Read the file to check if the contents are correct
            String actualContents = readTestDataFile(fileToWrite);
            String expectedContents = "1 2 3 \n4 5 6 \n7 8 9 \n";

            // Check if the contents match the expected output
            if (!actualContents.equals(expectedContents)) {
                error = true;
                System.out.println("savePredictionMatrix 1) expected: " + expectedContents
                        + " actual: "
                        + actualContents);
            } else {
                System.out.println("savePredictionMatrix 1) success");
            }

            // Cleanup the file after test
            File file = new File(fileToWrite);
            file.delete();
        }


        {  // Test saving to a non-existent directory
            String fileToWrite = "nonexistentDir/invalidFile.txt";
            int[][] matrixToWrite = {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
            };

            // Check if the directory exists, and we expect it not to
            File directory = new File("nonexistentDir");
            if (directory.exists()) {
                error = true;
                System.out.println("savePredictionMatrix 2) The directory should not exist for " +
                        "this " + "test.");
            } else {
                // Attempt to save the matrix to the file (this should fail)
                boolean saveResult = false;
                try {
                    H12CustomApp.savePredictionMatrix(matrixToWrite, fileToWrite);
                } catch (IOException e) {
                    System.out.println("savePredictionMatrix 2) exception caught as expected: "
                            + e.getMessage());
                }

                if (saveResult) {
                    error = true;
                    System.out.println("savePredictionMatrix 2) succeeded unexpectedly.");
                } else {
                    System.out.println("savePredictionMatrix 2) success");
                }
            }
        }


        if (error) {
            System.out.println("\nTestH12CustomApp failed");
            return false;
        } else {
            System.out.println("\nTestH12CustomApp passed");
            System.out.println("There may be output from the methods being tested.");
            return true;
        }
    }
}
