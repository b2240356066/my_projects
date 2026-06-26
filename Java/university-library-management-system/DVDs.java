public class DVDs extends LibraryItems{
    private final String director;
    private final String category;
    private final String runTime;


    public DVDs(String ItemID, String title, String director, String category, String runTime, String type) {
        this.category = category;
        this.director = director;
        this.runTime = runTime;
        super.itemID = ItemID;
        super.name = title;
        super.type = type;
        super.available = true;
        super.BorrowedDate = "";
        super.BorrowedBy ="";
    }
    public String getCategory() {
        return category;
    }
    public String getDirector() {
        return director;
    }
    public String getRunTime() {
        return runTime;
    }

    //overriding the toString function to print the object without printing the memory space.
    @Override
    public String toString() {
        return "/ ID: " + itemID + " ,Name:" + name + " ,Director:" + director + " ,Category:" + category +
                " ,RunTime:" + runTime + " ,Type:" + type + " ,Available:" + available + " / ";
    }
}
