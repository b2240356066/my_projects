public class Guests extends LibraryUsers {
    private final String occupation;


    public Guests(String name, String userID, String phoneNumber, String occupation) {
        // inherit the attributes the items have in common from upper class.(LibraryUsers)

        this.occupation = occupation;
        super.name = name;
        super.userID = userID;
        super.phoneNumber = phoneNumber;
        super.itemLimit = 0;
        super.penalty = 0;
        super.OverDueDays= 7;
        super.MaxItemLimit=1;

    }
    public String getOccupation() {
        return occupation;
    }

    //overriding the toString function to print the object without printing the memory space.
    @Override
    public String toString() {
        return " / Name: " + name + " Phone: " + phoneNumber + " Occupation: " + occupation + " ItemLimit: " + itemLimit + " Penalty: " + penalty + " / ";
    }
}
