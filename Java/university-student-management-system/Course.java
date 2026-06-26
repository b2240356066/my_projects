import java.util.ArrayList;
import java.util.HashMap;
public class Course extends AcademicEntity{
    /**
     * Courses have department,credits,semester,programCode
     * created an arraylist enrolled students to store the students that enrolled in this course.
     * created an arraylist totalGrades to store the grades.
     * created a hashmap to count how many of which grade is present to calculate average grade. keys are course code and values are the grades.
     */
    protected String department;
    protected int credits;
    protected String semester;
    protected String programCode;
    protected String instructor;
    protected ArrayList<String> EnrolledStudents;
    protected ArrayList<String> TotalGrades;
    protected HashMap<String,Integer> countGrades;

    /**
     * create a course object with given parameters in this constructor.
     * @param code course code
     * @param name course name
     * @param department course department
     * @param credits course credits
     * @param semester course semester
     * @param programCode course programCode
     */
    Course(String code, String name, String department, int credits, String semester,String programCode){
        super.code = code;
        super.name = name;
        this.department = department;
        this.credits = credits;
        this.semester = semester;
        this.programCode = programCode;
        this.EnrolledStudents=new ArrayList<>();
        this.TotalGrades=new ArrayList<>();
        this.countGrades=new HashMap<>();
    }
    /**
     * get information about the course
     * @return string of information
     */
    public String getInfo(){
        return "Course Code: "+code +"\nName: "+name+"\nDepartment: "+department+"\nCredits: "+credits+"\nSemester: "+semester+"\n\n";

    }

    /**
     * get information about the course
     * @return string of information
     */
    public String getCourseReports(){
        return "Course Code: "+code +"\nName: "+name+"\nDepartment: "+department+"\nCredits: "+credits+"\nSemester: "+semester+"\n\n"+"Instructor: ";

    }

    /**
     * add the student to enrolledStudent list
     * @param student student who is enrolled in this course
     */

    public void addEnrolledStudent(String student){
        EnrolledStudents.add(student);
    }

    /**
     * calculate the average grade in this course
     * @return average grade
     */
    public String AverageGrade(){
        if(EnrolledStudents.isEmpty()){
            return "0.00";
        }

        double sum = 0;
        for(String grade : countGrades.keySet()){
            if(grade.equals("A1")){
                sum += countGrades.get(grade)*4.00;
            }
            else if(grade.equals("A2")){
                sum += countGrades.get(grade)*3.50;
            }
            else if(grade.equals("B1")){
                sum += countGrades.get(grade)*3.00;
            }
            else if(grade.equals("B2")){
                sum += countGrades.get(grade)*2.50;
            }
            else if(grade.equals("C1")){
                sum += countGrades.get(grade)*2.00;
            }
            else if(grade.equals("C2")){
                sum += countGrades.get(grade)*1.50;
            }
            else if(grade.equals("D1")){
                sum += countGrades.get(grade)*1.00;
            }
            else if(grade.equals("D2")){
                sum += countGrades.get(grade)*0.50;
            }
            else if(grade.equals("F1")){
                sum += countGrades.get(grade)*0.00;
            }
        }

        return String.format("%.2f", sum/EnrolledStudents.size()) ;
    }

    /**
     * adding the grade to totalGrade list and countGrades map.
     * @param grade the grade that is gotten in this course.
     */

    public void addGrade(String grade){
        TotalGrades.add(grade);
        countGrades.put(grade, countGrades.getOrDefault(grade,0)+1);
    }

}
