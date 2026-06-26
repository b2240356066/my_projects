import java.util.ArrayList;
import java.util.HashMap;

public class Student extends People {
    /**
     * created array lists which contains student enrolled courses and completed courses
     * these arraylists contains course codes.
     * created a hashmap with completed course codes as keys and the values are the grades.
     */
    protected ArrayList<String> EnrolledCourses;
    protected ArrayList<String> CompletedCourses;
    protected HashMap<String,String> Grades;

    /**
     * create a student object with given parameters in this constructor.
     * @param id student id
     * @param name student name
     * @param email student email
     * @param department student department
     */
    Student(String id,String name,String email,String department){
        super.id=id;
        super.name=name;
        super.email=email;
        super.department=department;
        this.CompletedCourses=new ArrayList<>();
        this.EnrolledCourses=new ArrayList<>();
        this.Grades = new HashMap<>();

    }

    /**
     * gets information about student
     * @return string with information.
     */
    public String getInfo(){
        return "Student ID: "+id+"\nName: "+name+"\nEmail: "+email+"\nMajor: "+department+"\nStatus: Active\n\n";
    }

    /**
     * add the desired course to enrolled course list
     * @param course desired course id.
     */
    public void addEnrolledCourse(String course){
        EnrolledCourses.add(course);
    }

    /**
     * calculating the GPA of student based on the credit of the completed course.
     * @param CourseMap the map that contains courses codes as keys and values as course objects.
     * @return final GPA
     */
    public String calculateGrade(HashMap<String,Course> CourseMap){
        double sum=0;
        double total=0;
        for(String course:CompletedCourses){
            int credit = CourseMap.get(course).credits;
            total=total+credit;
            if(Grades.get(course).equals("A1")){
                sum+=credit*4.00;
            }
            else if(Grades.get(course).equals("A2")){
                sum+=credit*3.50;
            }
            else if(Grades.get(course).equals("B1")){
                sum+=credit*3.00;
            }
            else if(Grades.get(course).equals("B2")){
                sum+=credit*2.50;
            }
            else if(Grades.get(course).equals("C1")){
                sum+=credit*2.00;
            }
            else if(Grades.get(course).equals("C2")){
                sum+=credit*1.50;
            }
            else if(Grades.get(course).equals("D1")){
                sum+=credit*1.00;
            }
            else if(Grades.get(course).equals("D2")){
                sum+=credit*0.50;
            }
            else if(Grades.get(course).equals("F1")){
                sum+=credit*0.00;
            }
        }
        if(total>0){
            return String.format("%.2f",sum/total);
        }
        else {
            return "0.00";
        }
        }

    }

