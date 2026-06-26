public class Books extends LibraryItems {
        private final String author;
        private final String genre;


    public Books(String ItemID, String title, String author, String genre, String type) {
        this.author = author;
        this.genre = genre;
        super.itemID = ItemID;
        super.type = type;
        super.name = title;
        super.available = true;
        super.BorrowedDate= "";
        super.BorrowedBy = "";
    }
    public String getAuthor() {
        return author;
    }
    public String getGenre() {
        return genre;
    }

    //overriding the toString function to print the object without printing the memory space.
    @Override
    public String toString() {
        return "/ ID: " + itemID +" Name: " + name + " Author: " + author + " Genre: " + genre+" ,Type:" + type + " ,Available:" + available + " / ";

    }

    }
