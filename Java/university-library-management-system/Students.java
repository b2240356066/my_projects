public class Students extends LibraryUsers{
    private final String faculty;
    private final String department;
    private final String grade;

    public Students(String name, String userID, String phoneNumber, String department, String faculty, String grade) {
        // inherit the attributes the items have in common from upper class.(LibraryUsers)

        super.name = name;
        super.userID = userID;
        super.phoneNumber = phoneNumber;
        super.penalty= 0;
        super.OverDueDays = 30;
        super.MaxItemLimit=5;
        this.department = department;
        this.faculty = faculty;
        this.grade = grade;

    }
    public String getFaculty() {
        return faculty;
    }
    public String getDepartment() {
        return department;
    }
    public String getGrade() {
        return grade;
    }

    //overriding the toString function to print the object without printing the memory space.
    @Override
    public String toString() {
        return "/ Name: " + name +  " Phone: " + phoneNumber + " Faculty: " + faculty+ " Department: " + department+ " Grade: " + grade
                + " ItemLimit: " + itemLimit + " Penalty: " + penalty + " UserID: " + userID + " / ";

    }


}
