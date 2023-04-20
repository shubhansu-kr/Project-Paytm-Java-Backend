import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Book> books = new ArrayList<>();
        int choice = 0;

        while (choice != 6) {
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Search Book");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter book details:");
                    System.out.print("Title: ");
                    String title = scanner.next();
                    System.out.print("Author: ");
                    String author = scanner.next();
                    System.out.print("ISBN: ");
                    String isbn = scanner.next();
                    books.add(new Book(title, author, isbn));
                    System.out.println("Book added successfully!");
                    break;

                case 2:
                    System.out.println("List of books:");
                    for (Book book : books) {
                        System.out.println(book);
                    }
                    break;

                case 3:
                    System.out.print("Enter book title to search: ");
                    String searchTitle = scanner.next();
                    for (Book book : books) {
                        if (book.getTitle().equals(searchTitle)) {
                            System.out.println(book);
                            break;
                        }
                    }
                    break;

                case 4:
                    System.out.print("Enter book title to update: ");
                    String updateTitle = scanner.next();
                    for (Book book : books) {
                        if (book.getTitle().equals(updateTitle)) {
                            System.out.print("New title: ");
                            String newTitle = scanner.next();
                            System.out.print("New author: ");
                            String newAuthor = scanner.next();
                            System.out.print("New ISBN: ");
                            String newIsbn = scanner.next();
                            book.setTitle(newTitle);
                            book.setAuthor(newAuthor);
                            book.setIsbn(newIsbn);
                            System.out.println("Book updated successfully!");
                            break;
                        }
                    }
                    break;

                case 5:
                    System.out.print("Enter book title to delete: ");
                    String deleteTitle = scanner.next();
                    for (Book book : books) {
                        if (book.getTitle().equals(deleteTitle)) {
                            books.remove(book);
                            System.out.println("Book deleted successfully!");
                            break;
                        }
                    }
                    break;

                case 6:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice! Please enter a valid choice.");
                    break;
            }
        }

        scanner.close();
    }
}

class Book {
    private String title;
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Title: ";
    }

}