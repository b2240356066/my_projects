public class AcademicMembers extends LibraryUsers{
    private final String faculty;
    private final String department;
    private final String title;


    public AcademicMembers(String name, String userID, String phoneNumber, String department, String faculty, String title) {
        // inherit the attributes the items have in common from upper class.(LibraryUsers)

        this.department = department;
        this.faculty = faculty;
        this.title = title;
        super.phoneNumber = phoneNumber;
        super.name = name;
        super.userID = userID;
        super.itemLimit=0;
        super.penalty=0;
        super.OverDueDays=15;
        super.MaxItemLimit=3;

    }
    public String getFaculty() {
        return faculty;
    }
    public String getDepartment() {
        return department;
    }
    public String getTitle() {
        return title;
    }

    //overriding the toString function to print the object without printing the memory space.
    @Override
    public String toString() {
        return " / Name: " + title + " " + name + " Phone: " + phoneNumber +" Faculty: " + faculty + " Department: " + department +
                "itemLimit: " + itemLimit + " penalty: " + penalty + " / ";
    }
}
