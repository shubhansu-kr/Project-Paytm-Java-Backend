import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class NotesManagementSystem {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String directoryPath = "notes/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
        while (true) {
            try {
                System.out.println("\nEnter 1 to create a new note");
                System.out.println("Enter 2 to read an existing note");
                System.out.println("Enter 3 to delete an existing note");
                System.out.println("Enter 4 to exit");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        createNote(directoryPath, sc);
                        break;
                    case 2:
                        readNote(directoryPath, sc);
                        break;
                    case 3:
                        deleteNote(directoryPath, sc);
                        break;
                    case 4:
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice! Please enter a number.");
                sc.nextLine(); // Clear the input buffer
            }
        }
    }

    private static void createNote(String directoryPath, Scanner sc) throws IOException {
        System.out.println("Enter the name of the note:");
        String fileName = sc.next();
        File file = new File(directoryPath + fileName + ".txt");
        if (file.createNewFile()) {
            System.out.println("Note created successfully!");
        } else {
            System.out.println("Note with the same name already exists!");
        }
        System.out.println("Enter the content of the note:");
        String content = sc.next();
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }

    private static void readNote(String directoryPath, Scanner sc) throws IOException {
        System.out.println("Enter the name of the note:");
        String fileName = sc.next();
        File file = new File(directoryPath + fileName + ".txt");
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } else {
            System.out.println("Note not found!");
        }
    }

    private static void deleteNote(String directoryPath, Scanner sc) {
        System.out.println("Enter the name of the note:");
        String fileName = sc.next();
        File file = new File(directoryPath + fileName + ".txt");
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Note deleted successfully!");
            } else {
                System.out.println("Failed to delete the note!");
            }
        } else {
            System.out.println("Note not found!");
        }
    }
}