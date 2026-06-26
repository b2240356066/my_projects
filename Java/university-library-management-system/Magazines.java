public class Magazines extends LibraryItems{
    private final String publisher;
    private final String category;


    public Magazines(String ItemID, String title, String publisher, String category, String type) {
        this.publisher = publisher;
        this.category = category;
        super.itemID = ItemID;
        super.type = type;
        super.name = title;
        super.available = true;
        super.BorrowedDate = "";
        super.BorrowedBy="";
    }
    public String getPublisher() {
        return publisher;
    }
    public String getCategory() {
        return category;
    }

    //overriding the toString function to print the object without printing the memory space.
    @Override
    public String toString() {
        return "/ ID: " + itemID +" ,Name:" + name + " ,Publisher:" + publisher + " ,Category:" + category+" ,Type:" + type + " ,Available:" + available+ " / ";

    }

}
