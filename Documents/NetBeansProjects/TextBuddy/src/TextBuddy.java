import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class run a program which allows user to use operations such as add, display, delete, clear, exit on a text file.
 * User's input will be saved into a .txt file.
 * To run: java textbuddy [file name]
 *    e.g: java textbuddy myfile.txt
 * 
 * @author Ter Yao Xiang A0110751W
 */

public class TextBuddy {

    private static String CURR_DIR = System.getProperty("user.dir") + "\\";
    
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            createFileIfNotExist(args[0]);
            printWelcomeMessage(args[0]);
            run(args[0]);
        } else {
            System.out.println("Missing file name!");
        }
    }
    
    private static void createFileIfNotExist(String fileName) throws IOException {
        File textFile = new File(CURR_DIR + fileName);
        
        if (!textFile.exists()) {
            textFile.createNewFile();
        }
    }
    
    private static void printWelcomeMessage(String fileName) {
        System.out.println("Welcome to TextBuddy. " + fileName + " is ready for use");
    }
    
    private static void run(String fileName) throws IOException {
        Scanner sc = new Scanner(System.in);
        String[] input;
        
        do {
            System.out.print("command: ");
            input = sc.nextLine().split(" ");
            try {
                processCommand(fileName, input);
            } catch (IOException e) {
                System.exit(1);
            }
        } while (true);
    }
    
    private static void processCommand(String fileName, String[] input) throws IOException {
        switch (input[0]) {
            case "add":
                addString(fileName, input);
                break;
                
            case "display":
                displayFile(fileName);
                break;
                
            case "delete":
                deleteLine(fileName, input);
                break;
                
            case "clear":
                clearTextFile(fileName);
                break;
                
            case "exit":
                System.exit(0);
                break;
                
            default:
                System.out.println("Wrong command!");
                break;
        }
    }
    
    //This method append a new line of string to the end of the text file
    private static void addString(String fileName, String[] inputs) throws IOException {
        String fileLocation = CURR_DIR + fileName;
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation, true));
        
        if (inputs.length > 1) {
            String input = concatenateStringArray(inputs);
            writer.write(input);
            writer.newLine();
            
            System.out.println("added to " + fileName + ": \"" + input + "\"");
        }
        
        writer.close();
    }

    //This method displays all lines of text found in the text file, numbered.
    private static void displayFile(String fileName) throws IOException {
        String fileLocation = CURR_DIR + fileName;
        BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
        
        int i = 1;
        String currLine = reader.readLine();
        while (currLine != null) {
            System.out.println(i + ". " + currLine);
            currLine = reader.readLine();
            i++;
        }
        if (i == 1) {
            System.out.println(fileName + " is empty");
        }
        reader.close();
    }

    //This method will delete the specified line in the text file.
    //Deletion is done in a temporary file before having it renamed and replace the original file.
    private static void deleteLine(String fileName, String[] input) throws IOException {
        Integer lineDelete;
        try {
            lineDelete = Integer.parseInt(input[1]);
        } catch (Exception e) {
            System.out.println("No line specified!");
            return;
        }
        String fileLocation = CURR_DIR + fileName;
        File textFile = new File(fileLocation);
        File tempFile = new File(fileLocation + ".tmp");
        
        writeExcludingDeletedLine(lineDelete, fileName);
        
        textFile.delete();
        tempFile.renameTo(textFile);
    }

    //This method resets the text file by deleting the original file and creates a new one
    private static void clearTextFile(String fileName) throws IOException {
        String fileLocation = CURR_DIR + fileName;
        File textFile = new File(fileLocation);
        textFile.delete();
        System.out.println("all content deleted from " + fileName);
        createFileIfNotExist(fileName);
    }

    //This method joins all arrays in inputs[] and returns the concatenated string.
    private static String concatenateStringArray(String[] inputs) {
        String str = inputs[1];
        for (int i = 2; i < inputs.length; i++) {
            str = str + " " + inputs[i];
        }
        return str;
    }

    //This method write all lines except the deleted line to the text file
    private static void writeExcludingDeletedLine(Integer lineDelete, String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + ".tmp"));
        String currLine, strDeleted = null;
        
        for (int i = 1; i > 0; i++) {
            currLine = reader.readLine();
            if (lineDelete != i) {
                if (currLine != null) {
                    writer.write(currLine);
                    writer.newLine();
                } else {
                    break;
                }
            } else {
                strDeleted = currLine;
            }
        }
        printDeletedMessage(strDeleted, fileName);
        
        writer.close();
        reader.close();
    }

    //This method prints the deleted line or prints an error if no line is deleted.
    private static void printDeletedMessage(String strDeleted, String fileName) {
        if (strDeleted != null) {
            System.out.println("deleted from " + fileName + ": \"" + strDeleted + "\"");
        } else {
            System.out.println("There is no such line to delete!");
        }
    }

}