import java.util.ArrayList;
import java.io.*;
import java.nio.file.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in
public class Main {
    public static void main(String[] args) {
        String items = args[0]; // items text file
        String users = args[1]; // users text file
        String commands = args[2]; // commands text file
        String output = args[3]; // output text file

        ArrayList<LibraryItems> LibraryItemsList = readTextItems(items);
        //the first text is items text so read and make a list with it.
        ArrayList<LibraryUsers> LibraryUsersList = readTextUsers(users);
        //second text is users text so read and make a list with it.

        //pass lists to LibraryManagement Class to use the lists in the methods borrow,return etc.
        LibraryManagement libraryManagement = new LibraryManagement(LibraryItemsList, LibraryUsersList,output);

        //to read and execute the command text
        libraryManagement.readTxtCommands(commands);

        //close the output file
        libraryManagement.closeWriter();

        //to remove the last newline in the output file
        libraryManagement.removeLastNewline(output);


    }

    public static ArrayList<LibraryItems> readTextItems(String items){
        ArrayList<LibraryItems> LibraryItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(items))) {
            String line;
            while((line = br.readLine()) != null){String[] parts = line.split(",");

                if (parts[0].equals("B")){
                    // if the first character is B that means it is a book.
                    Books book =new Books(parts[1],parts[2],parts[3],parts[4],parts[5]);
                    // create a new book object with given properties

                    LibraryItems.add(book);
                    // adds the book to the ItemsList.

                }
                else if (parts[0].equals("D")){
                    // if the first character is D that means it is a DVD.
                    DVDs dvd = new DVDs(parts[1],parts[2],parts[3],parts[4],parts[5],parts[6]);
                    // create a new DVD object with given properties

                    LibraryItems.add(dvd);
                    // add the DVD to the ItemsList.

                }
                else if (parts[0].equals("M")){
                    // if the first character is M that means it is a magazine.
                    Magazines magazine=new Magazines(parts[1],parts[2],parts[3],parts[4],parts[5]);
                    // create a new magazine object with given properties

                    LibraryItems.add(magazine);
                    // add the magazine to the ItemsList.

                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return LibraryItems;

    }

    public static ArrayList<LibraryUsers> readTextUsers(String users) {
        ArrayList<LibraryUsers> LibraryUsers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(users))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts[0].equals("S")) {
                    Students student = new Students(parts[1],parts[2],parts[3],parts[4],parts[5],parts[6]);
                    // create a new Student object with given properties

                    LibraryUsers.add(student);
                    // add the student to UsersList.
                }
                if (parts[0].equals("A")) {
                    AcademicMembers academicMember = new AcademicMembers(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                    // create a new Academic Member object with given properties

                    LibraryUsers.add(academicMember);
                    // add the academic member to UsersList.
                }
                if (parts[0].equals("G")){
                    //if the first character is G that means it is a Guest.

                    Guests guest = new Guests(parts[1],parts[2],parts[3],parts[4]);
                    // create a new Guest object with given properties

                    LibraryUsers.add(guest);
                    // add the guest to UsersList.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return LibraryUsers;
    }
}

