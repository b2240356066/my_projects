import java.io.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LibraryManagement {
    private ArrayList<LibraryItems> libraryItemsList; // ItemsList
    private ArrayList<LibraryUsers> libraryUsersList; // UsersList
    private BufferedWriter writer;

    public LibraryManagement(ArrayList<LibraryItems> libraryItemsList, ArrayList<LibraryUsers> libraryUsersList,String outputFile) {
        this.libraryItemsList = libraryItemsList;
        this.libraryUsersList = libraryUsersList;
        try {
            this.writer = new BufferedWriter(new FileWriter(outputFile)); // initialize writer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // close the file when all the lines are printed out.
    public void closeWriter() {
        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write the output to output.txt file.
    public void writeTxtOutput(String message) {
        try {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // to remove the last newline character
    public void removeLastNewline(String filePath) {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "rw")) {
            long length = file.length();
            // empty file
            if (length == 0) return;

            //the last character
            file.seek(length - 1);
            byte lastByte = file.readByte();

            // if the last line is \n remove it.
            if (lastByte == '\n') {
                file.setLength(length - 1);
                if (length > 1) {
                    file.seek(length - 2);
                    if (file.readByte() == '\r') {
                        file.setLength(length - 2);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    // read the commands text file and parce it.
    public void readTxtCommands(String commands) {
        {
            try (BufferedReader br = new BufferedReader(new FileReader(
                    commands))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts[0].equals("borrow")) {
                        //if parts[0] equals borrow sequential methods will be executed.
                        if (!checkPenalty(parts[1], parts[2])){
                            if (!checkAvailability(parts[1], parts[2])){
                                if (!checkType(parts[1], parts[2])){
                                    if (!checkLimit(parts[1], parts[2])){
                                        // if all of these conditions are met, then user can borrow the item.
                                        borrowItem(parts[1], parts[2], parts[3]);
                                    }
                                }
                            }

                        }
                    }
                    if (parts[0].equals("return")) {
                        // if parts[0] equals return the item will be returned.
                        returnItem(parts[1], parts[2]);
                    }
                    if (parts[0].equals("pay")) {
                        // if parts[0] equals pay, user's penalty will be set to 0.
                        payPenalty(parts[1]);
                    }
                    if (parts[0].equals("displayUsers")) {
                        // 2 newline space
                        writeTxtOutput("");
                        writeTxtOutput("");
                        // write all the user's information.
                        displayUsers();
                    }
                    if (parts[0].equals("displayItems")) {
                        // 1 newline space
                        writeTxtOutput("");
                        // write all the item's information.
                        displayItems();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public String getNameOfUser(String userID) {
        // get the name of user whose id is equal to given id.
        String userName;
        for (LibraryUsers user : libraryUsersList) {
            if (user.userID.equals(userID)) {
                userName = user.name;
                return userName;
            }
        }
        return "Unknown";
    }

    public boolean checkPenalty(String userID, String itemID) {
        String itemName = null;
        //get the name of item whose id is equal to given id.
        for (LibraryItems item : libraryItemsList) {
            if (item.itemID.equals(itemID)) {
                itemName = item.name;
                break;
            }
        }
        for (LibraryUsers user : libraryUsersList) {
            // when the id given matches the user's id, check if user's penalty is equal or more than 6 $.
            if (user.userID.equals(userID)) {
                if (user.getPenalty() >= 6) {
                    // if penalty is equal or more than 6 $, print out the given message.
                    writeTxtOutput(getNameOfUser(userID) + " cannot borrow " + itemName + ", you must first pay the penalty amount! 6$");
                    return true;
                }
                break;
            }
        }
        return false;
       }

    public boolean checkLimit(String userID, String itemID) {
        String itemName = null;
        //get the name of item whose id is equal to given id.
        for (LibraryItems item : libraryItemsList) {
            if (item.itemID.equals(itemID)) {
                itemName = item.name;
                break;
            }
        }
        for (LibraryUsers user : libraryUsersList) {
            // when the id given matches the user's id, check if user's limit is reached.
            if (user.userID.equals(userID)) {
                if (user.itemLimit>=user.MaxItemLimit){
                    writeTxtOutput(getNameOfUser(userID) + " cannot borrow " + itemName + ", since the borrow limit has been reached!");
                        return true;
                    }
                break;
            }

        }
        return false;
    }

    public boolean checkAvailability(String userID, String itemID) {
        String itemName;
        //get the name of item whose id is equal to given id.
        for (LibraryItems item : libraryItemsList) {
            if (item.itemID.equals(itemID)) {
                itemName = item.name;
                if (!item.isAvailable()) {
                    //if the item is not available, print out the given message.
                    writeTxtOutput(getNameOfUser(userID) + " cannot borrow " + itemName + ", it is not available!");
                    return true;
                }
                break;
            }

        }
        return false;
    }

    public boolean checkType(String userID, String itemID) {
        for (LibraryItems item : libraryItemsList) {
            if (item.itemID.equals(itemID)) {
                for (LibraryUsers user : libraryUsersList) {
                    if (user.userID.equals(userID)) {
                        // when both itemID and userID is matched, check the types of the items and the not-allowed types the user can't have.
                        if (item.getType().equals("reference") && user.getClass().equals(Students.class)) {
                            writeTxtOutput((getNameOfUser(userID) + " cannot borrow reference item!"));
                            return true;
                        }
                        if (item.getType().equals("rare") && user.getClass().equals(Guests.class)){
                            writeTxtOutput((getNameOfUser(userID) + " cannot borrow rare item!"));
                            return true;
                        }
                        if (item.getType().equals("limited") && user.getClass().equals(Guests.class)){
                            writeTxtOutput((getNameOfUser(userID) + " cannot borrow limited item!"));
                            return true;
                        }
                    }
                }
                break;
                }

            }
        return false;
    }


    public void borrowItem(String userID,String itemID,String borrowDate) {
        String itemName;
        String userName;
        int overDueLimit;
        for (LibraryUsers user : libraryUsersList) {
            if (user.userID.equals(userID)) {
                // increase the borrowed items count by 1.
                user.increaseItemLimit();
                userName = user.name;

        for (LibraryItems item : libraryItemsList) {
            if (item.itemID.equals(itemID)) {
                itemName = item.name;
                //change the availability to false
                item.changeAvailability();
                // save the date the item is borrowed.
                item.BorrowedDate = borrowDate;
                // save the user that borrows the item.
                item.BorrowedBy = userName;
                //print out the given message.
                writeTxtOutput((getNameOfUser(userID) + " successfully borrowed! " + itemName));
                break;
            }
        }
            //get the computer's date. (the date that code is working at.)
            LocalDate today = LocalDate.now();
            //format the date as dd/MM/yyyy.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate borrowedDate = LocalDate.parse(borrowDate, formatter);
            // get how many days there are between the borrowed date and the computer's date.
            long daysBetweenDates = ChronoUnit.DAYS.between(borrowedDate, today) + 1;

            //if user extended the overDueDays this means the item will be automatically returned and the penalty will be applied to user.
            overDueLimit = user.OverDueDays;
            if (daysBetweenDates > overDueLimit) {
                //increase penalty
                user.setPenalty();
                //return the item.
                AutoReturnBorrowedItem(userID,itemID);}
            break;}}
        }

        public void returnItem (String userID, String itemID){
        String itemName;
        for (LibraryItems item : libraryItemsList) {
            if (item.itemID.equals(itemID)) {
                itemName = item.name;
                //change availability to true.
                item.changeAvailability();
                // erase the borrowed date
                item.BorrowedDate="";
                // erase the user's name who borrowed the item.
                item.BorrowedBy="";
                //print out the given message.
                writeTxtOutput((getNameOfUser(userID) + " successfully returned " + itemName));
                break;
                }
        }
        for (LibraryUsers user : libraryUsersList) {
            if (user.userID.equals(userID)) {
                // decrease the borrowed items count by 1.
                user.decreaseItemLimit();
                break;
            }
        }
        }
        public void payPenalty (String userID){
        for (LibraryUsers user : libraryUsersList) {
            if (user.userID.equals(userID)) {
                // set penalty to 0.
                user.erasePenalty();
                //print out the given message.
                writeTxtOutput((getNameOfUser(userID) + " has paid penalty"));
                break;}

        }
    }

        public void displayUsers () {
        // write all the user's information.
        for (LibraryUsers user : libraryUsersList) {
            writeTxtOutput("------ User Information for " + user.userID + " ------");

            if (user instanceof Students) {
                writeTxtOutput("Name: " + user.name + " Phone: " + user.phoneNumber);
                writeTxtOutput("Faculty: " + ((Students) user).getFaculty() + " Department: " + ((Students) user).getDepartment() +
                        " Grade: " + ((Students) user).getGrade()+"th");
            }
            if (user instanceof AcademicMembers) {
                writeTxtOutput("Name: " +(((AcademicMembers) user).getTitle())+ " " +  user.name + " Phone: " + user.phoneNumber);
                writeTxtOutput("Faculty: " + ((AcademicMembers) user).getFaculty() + " Department: " + ((AcademicMembers) user).getDepartment());
            }
            if (user instanceof Guests) {
                writeTxtOutput("Name: " + user.name + " Phone: " + user.phoneNumber);
                writeTxtOutput("Occupation: " + ((Guests) user).getOccupation());

            }
            writeTxtOutput("");

        }

        }
        public void displayItems () {
        // write all the item's information.
        for (LibraryItems item : libraryItemsList) {
            writeTxtOutput("------ Item Information for " + item.itemID + " ------");
            // if the item is available (is not borrowed) print out the following messages.
            if (item.isAvailable()) {
                if (item instanceof Books) {
                    writeTxtOutput("ID: " + item.itemID + " Name: " + item.name + " Status: Available");
                    writeTxtOutput("Author: "+((Books) item).getAuthor() + " Genre: " + ((Books) item).getGenre());
                }
                if (item instanceof DVDs) {
                    writeTxtOutput("ID: " + item.itemID + " Name: " + item.name + " Status: Available");
                    writeTxtOutput("Director: " + ((DVDs) item).getDirector()+ " Category: " + ((DVDs) item).getCategory() + " Runtime: " + ((DVDs) item).getRunTime());
                }
                if (item instanceof Magazines) {
                    writeTxtOutput("ID: " + item.itemID + " Name: " + item.name + " Status: Available");
                    writeTxtOutput("Publisher: " + ((Magazines) item).getPublisher() + " Category: " + ((Magazines) item).getCategory());
                }

            }
            // if the item is not available ( borrowed) print out the following message that includes the borrowed date and the user that borrowed the item.
            if (!item.isAvailable()) {
                if (item instanceof Books) {
                    writeTxtOutput("ID: " + item.itemID + " Name: " + item.name + " Status: Borrowed" + " Borrowed Date: " + item.BorrowedDate + " Borrowed by: "
                    + item.BorrowedBy);
                    writeTxtOutput("Author: "+((Books) item).getAuthor() + " Genre: " + ((Books) item).getGenre());
                }
                if (item instanceof DVDs) {
                    writeTxtOutput("ID: " + item.itemID + " Name: " + item.name + " Status: Borrowed" + " Borrowed Date: " + item.BorrowedDate + " Borrowed by: "
                            + item.BorrowedBy);
                    writeTxtOutput("Director: " + ((DVDs) item).getDirector()+ " Category: " + ((DVDs) item).getCategory() + " Runtime: " + ((DVDs) item).getRunTime());
                }
                if (item instanceof Magazines) {
                    writeTxtOutput("ID: " + item.itemID + " Name: " + item.name + " Status: Borrowed" + " Borrowed Date: " + item.BorrowedDate + " Borrowed by: "
                            + item.BorrowedBy);
                    writeTxtOutput("Publisher: " + ((Magazines) item).getPublisher() + " Category: " + ((Magazines) item).getCategory());
                }

            }
            writeTxtOutput("");
        }

        }
        public void AutoReturnBorrowedItem (String userID, String itemID) {
            for (LibraryItems item : libraryItemsList) {
                if (item.itemID.equals(itemID)) {
                    //change availability to true.
                    item.changeAvailability();
                    // erase the borrowed date.
                    item.BorrowedDate = "";
                    // erase the user's name that borrowed the item.
                    item.BorrowedBy="";
                    break;
                }
            }
            for (LibraryUsers user : libraryUsersList) {
                if (user.userID.equals(userID)) {
                    // decrease the borrowed item count by 1.
                    user.decreaseItemLimit();
                    break;
                }
            }
        }

        }
