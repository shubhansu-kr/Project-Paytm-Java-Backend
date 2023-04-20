import java.util.ArrayList;
import java.util.Scanner;

public class NotesManager {
    private ArrayList<String> notes;
    private Scanner scanner;

    public NotesManager() {
        notes = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Create a new note");
            System.out.println("2. Edit an existing note");
            System.out.println("3. Read a note");
            System.out.println("4. Delete a note");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createNote();
                    break;
                case 2:
                    editNote();
                    break;
                case 3:
                    readNote();
                    break;
                case 4:
                    deleteNote();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void createNote() {
        System.out.print("Enter note text: ");
        scanner.nextLine(); // consume the newline character
        String note = scanner.nextLine();
        notes.add(note);
        System.out.println("Note created.");
    }

    private void editNote() {
        System.out.print("Enter note number: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // consume the newline character
        if (index >= 0 && index < notes.size()) {
            System.out.print("Enter new note text: ");
            String note = scanner.nextLine();
            notes.set(index, note);
            System.out.println("Note edited.");
        } else {
            System.out.println("Invalid note number.");
        }
    }

    private void readNote() {
        System.out.print("Enter note number: ");
        int index = scanner.nextInt() - 1;
        if (index >= 0 && index < notes.size()) {
            System.out.println(notes.get(index));
        } else {
            System.out.println("Invalid note number.");
        }
    }

    private void deleteNote() {
        System.out.print("Enter note number: ");
        int index = scanner.nextInt() - 1;
        if (index >= 0 && index < notes.size()) {
            notes.remove(index);
            System.out.println("Note deleted.");
        } else {
            System.out.println("Invalid note number.");
        }
    }

    public static void main(String[] args) {
        NotesManager notesManager = new NotesManager();
        notesManager.run();
    }
}