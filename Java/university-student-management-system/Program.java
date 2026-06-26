import java.util.ArrayList;
public class Program extends AcademicEntity{
    protected String department;
    protected String degreeLevel;
    protected int totalCredits;
    //created an arraylist ProgramsList to store the course codes.
    protected ArrayList<String> ProgramsList;
    protected static int allCredits;

    /**
     * create a program object with given parameters in this constructor
     * @param code program code
     * @param name program name
     * @param description program description
     * @param department program department
     * @param degreeLevel which level is the program
     * @param totalCredits total credits in this program
     */
    Program(String code, String name, String description,String department,String degreeLevel, int totalCredits) {
        super.code=code;
        super.name=name;
        super.description=description;
        this.department=department;
        this.degreeLevel=degreeLevel;
        this.totalCredits=totalCredits;
        this.ProgramsList=new ArrayList<>();
    }
    /**
     * get information about the department
     * @return string of information
     */
    public String getInfo(){
        return "Program Code: "+code+"\nName: "+name+"\nDepartment: "+ department + "\nDegree Level: "+degreeLevel+"\nRequired Credits: "+totalCredits+"\nCourses: "+printPrograms(ProgramsList)+"\n\n";
    }

    /**
     * add the desired course to the program
     * @param courseID desired course
     */

    public void addCourse(String courseID){
        this.ProgramsList.add(courseID);
    }

    /**
     * increase all credits with given credit.
     * @param credits given credit
     */
    public void addCredits(int credits){
        allCredits+=credits;
    }

    /**
     * to print the course names with curly braces
     * @param ProgramsList list that contains course codes.
     * @return curly braces course codes.
     */

    public String printPrograms(ArrayList<String> ProgramsList){
        if(ProgramsList.isEmpty()){
            return "- ";
        }
        else {
            return "{"+String.join(",",ProgramsList) + "}";
        }

    }

}
