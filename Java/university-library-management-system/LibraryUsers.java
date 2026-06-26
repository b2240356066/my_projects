public class LibraryUsers {
    // all users have these attributes in common

    protected String name;
    protected String userID;
    protected String phoneNumber;
    protected int itemLimit;
    protected int penalty;
    protected int OverDueDays;
    protected int MaxItemLimit;


    public int getPenalty() {
        return penalty;
    }
    public void setPenalty() {
        penalty = penalty+2;
    }
    public void increaseItemLimit() {
        itemLimit = itemLimit+1;
    }
    public void decreaseItemLimit() {
        this.itemLimit -= itemLimit;
    }
    public void erasePenalty() {
        penalty =0;
    }
}
