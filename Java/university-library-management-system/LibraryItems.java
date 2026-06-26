public class LibraryItems {
    // all items have these attributes in common.

    protected String name;
    protected String itemID;
    protected String type;
    protected boolean available;
    protected String BorrowedDate;
    protected String BorrowedBy;

    public boolean isAvailable() {
        return available;
    }
    public void changeAvailability(){
        available = !available;
    }
    public String getType(){
        return type;
    }
}
