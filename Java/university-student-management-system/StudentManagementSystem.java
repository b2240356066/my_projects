import java.io.*;
import java.util.*;

public class StudentManagementSystem {
    /**
     * The StudentManagementSystem class serves as the core controller for managing academic
     * entities within the application. It is responsible for storing, processing, and organizing information
     * related to students, faculty members, departments, programs, and courses.
     */
    String people;
    String departments;
    String programs;
    String courses;
    String assignments;
    String grades;
    String output;
    /**
     * created maps to store students,academic members,departments,programs and courses.
     */
    LinkedHashMap<String, Student> StudentMap;
    LinkedHashMap<String, AcademicMember> AcademicMemberMap;
    LinkedHashMap<String, Department> DepartmentsMap;
    LinkedHashMap<String, Program> ProgramsMap;
    LinkedHashMap<String, Course> CoursesMap;

    StudentManagementSystem(String people, String departments, String programs, String courses, String assignments, String grades, String output) {
        this.people = people;
        this.departments = departments;
        this.programs = programs;
        this.courses = courses;
        this.assignments = assignments;
        this.grades = grades;
        this.output = output;
    }

    /**
     * read people text and create a students map
     * also print out the appropriate exceptions
     * @param people people text file
     * @param output output text file
     * @return students map
     */

    public LinkedHashMap<String, Student> readTextStudents(String people, String output) {
        LinkedHashMap<String, Student> StudentMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(people));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
            writer.write("Reading Person Information ");
            writer.write("\n");
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                try {
                    if (parts[0].equals("S")) {
                        Student student = new Student(parts[1], parts[2], parts[3], parts[4]);
                        StudentMap.put(parts[1], student);
                    } else if (!parts[0].equals("F")) {
                        throw new Exceptions.InvalidPersonType("Invalid Person Type\n");
                    }
                } catch (Exception e) {
                    writer.write(e.getMessage());

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return StudentMap;
    }
    /**
     * read people text and create an academic members map
     * also print out the appropriate exceptions
     * @param people people text file
     * @param output output text file
     * @return academic member map
     */


    public LinkedHashMap<String, AcademicMember> readTextAcademicMember(String people, String output) {
        LinkedHashMap<String, AcademicMember> AcademicMemberMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(people));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                try {
                    if (parts[0].equals("F")) {
                        AcademicMember academicMember = new AcademicMember(parts[1], parts[2], parts[3], parts[4]);
                        AcademicMemberMap.put(parts[1], academicMember);
                    }
                } catch (Exception e) {
                    writer.write(e.getMessage());

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AcademicMemberMap;
    }

    /**
     * read departments file and create departments map
     * also print ot-ut the appropriate exceptions
     * @param departments departments text file
     * @param AcademicMemberMap academic member map
     * @param output output text file
     * @return departments map
     */

    public static LinkedHashMap<String, Department> readTextDepartment(String departments, LinkedHashMap<String, AcademicMember> AcademicMemberMap, String output) {
        LinkedHashMap<String, Department> DepartmentMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(departments));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
            writer.write("Reading Departments ");
            writer.write("\n");
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                try {

                    if (!AcademicMemberMap.containsKey(parts[3])) {
                        throw new Exceptions.NonExistingAcademicMember("Academic Member Not Found with ID " + parts[3] + "\n");
                    }
                } catch (Exception e) {
                    writer.write(e.getMessage());
                }

                Department department = new Department(parts[0], parts[1], parts[2], parts[3]);
                DepartmentMap.put(parts[0], department);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DepartmentMap;

    }

    /**
     * read programs text file and create programs map
     * @param programs programs text file
     * @param output output text file
     * @return programs map
     */

    public LinkedHashMap<String, Program> readTextProgram(String programs, String output) {
        LinkedHashMap<String, Program> ProgramMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(programs));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
            writer.write("Reading Programs ");
            writer.write("\n");
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                Program program = new Program(parts[0], parts[1], parts[2], parts[3], parts[4], Integer.parseInt(parts[5]));
                ProgramMap.put(parts[0], program);
                program.addCredits(Integer.parseInt(parts[5]));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ProgramMap;
    }

    /**
     * read courses text file and create courses map
     * also print out the appropriate exceptions
     * @param courses courses text file
     * @param ProgramsMap programs map
     * @param output output text file
     * @return courses map
     */

    public LinkedHashMap<String, Course> readTextCourse(String courses, LinkedHashMap<String, Program> ProgramsMap, String output) {
        LinkedHashMap<String, Course> CourseMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(courses));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
            writer.write("Reading Courses ");
            writer.write("\n");
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                try {
                    if (!ProgramsMap.containsKey(parts[5])) {
                        throw new Exceptions.NonExistingProgram("Program " + parts[5] + " Not Found\n");
                    }
                    Course course = new Course(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), parts[4], parts[5]);
                    CourseMap.put(parts[0], course);

                    Program program = ProgramsMap.get(parts[5]);
                    if (program != null) {
                        program.addCourse(parts[0]);
                    }
                } catch (Exceptions.NonExistingProgram e) {
                    writer.write(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CourseMap;

    }

    /**
     * read assignment text and print out appropriate exceptions
     * @param assignments assignments text file
     * @param output output text file
     * @param AcademicMemberMap academic member map
     * @param StudentMap student map
     * @param CourseMap courses map
     */

    public void readTextCourseAssignment(String assignments, String output, LinkedHashMap<String, AcademicMember> AcademicMemberMap, LinkedHashMap<String, Student> StudentMap, LinkedHashMap<String, Course> CourseMap) {
        try (BufferedReader br = new BufferedReader(new FileReader(assignments));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
            writer.write("Reading Course Assignments ");
            writer.write("\n");
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                try {
                    if (!(parts[0].equals("F") || parts[0].equals("S"))) {
                        throw new Exceptions.InvalidPersonType("Invalid person type \n");
                    }
                    else if (parts[0].equals("F") && !(AcademicMemberMap.containsKey(parts[1]))) {
                        throw new Exceptions.NonExistingAcademicMember("Academic Member Not Found with ID " + parts[1] +"\n");
                    }
                    else if (parts[0].equals("S") && !(StudentMap.containsKey(parts[1]))) {
                        throw new Exceptions.NonExistingStudent("Student Not Found with ID " + parts[1] + "\n");
                    }
                    else if (!CourseMap.containsKey(parts[2])) {
                        throw new Exceptions.NonExistingCourse("Course " + parts[2] + " Not Found\n");
                    }
                    Course course = CourseMap.get(parts[2]);
                    Student student = StudentMap.get(parts[1]);
                    if(parts[0].equals("F")){
                        course.instructor = AcademicMemberMap.get(parts[1]).name;
                    }
                    if(parts[0].equals("S")) {
                        course.addEnrolledStudent(parts[1]);
                        student.addEnrolledCourse(parts[2]);
                    }


                } catch (Exceptions.InvalidPersonType | Exceptions.NonExistingAcademicMember |
                         Exceptions.NonExistingStudent | Exceptions.NonExistingCourse e) {
                    writer.write(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * read grades text file and print out appropriate exceptions
     */

    public void readTextGrades(String grades, String output,LinkedHashMap<String, Student> StudentMap,LinkedHashMap<String, Course> CoursesMap) {
         String[] gradesList = {"A1","A2", "B1","B2", "C1","C2", "D1", "D2", "F3"};
        try (BufferedReader br = new BufferedReader(new FileReader(grades));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
            writer.write("Reading Grades ");
            writer.write("\n");

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                try{
                    if(!StudentMap.containsKey(parts[1])){
                        throw new Exceptions.NonExistingStudent("Student Not Found with ID "+ parts[1]+"\n");
                    }
                    else if(!CoursesMap.containsKey(parts[2])) {
                        throw new Exceptions.NonExistingCourse("Course " + parts[2] + " Not Found\n");
                    }
                    else if(!Arrays.asList(gradesList).contains(parts[0])){
                        throw new Exceptions.InvalidLetterGrades("Invalid Letter Grade \n");
                    }
                    Course course = CoursesMap.get(parts[2]);
                    course.addGrade(parts[0]);
                    Student student = StudentMap.get(parts[1]);
                    if(student.EnrolledCourses.contains(parts[2])){
                        student.EnrolledCourses.remove(parts[2]);
                        student.CompletedCourses.add(parts[2]);
                    }
                    student.Grades.put(parts[2], parts[0]);

                }
                catch (Exceptions.InvalidLetterGrades | Exceptions.NonExistingStudent | Exceptions.NonExistingCourse e){
                    writer.write(e.getMessage());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * read all the information
     */
    public void readAll() {
        StudentMap = readTextStudents(people,output);
        AcademicMemberMap = readTextAcademicMember(people,output);
        DepartmentsMap= readTextDepartment(departments,AcademicMemberMap,output);
        ProgramsMap=readTextProgram(programs,output);
        CoursesMap= readTextCourse(courses,ProgramsMap,output);
        readTextCourseAssignment(assignments,output,AcademicMemberMap,StudentMap,CoursesMap);
        readTextGrades(grades,output,StudentMap,CoursesMap);

    }

    /**
     * print out all the information about students, academic members etc. and reports of both students and academic members
     */

    public void getAllInfo() {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))){
            List<String> sortedKeysPrograms = new ArrayList<>(ProgramsMap.keySet());
            Collections.sort(sortedKeysPrograms);
            List<String> sortedKeysCourses = new ArrayList<>(CoursesMap.keySet());
            Collections.sort(sortedKeysCourses);
            List<String> sortedKeysDepartment = new ArrayList<>(DepartmentsMap.keySet());
            Collections.sort(sortedKeysDepartment);
            List<String> sortedKeysStudents = new ArrayList<>(StudentMap.keySet());
            Collections.sort(sortedKeysStudents);

            writer.write("----------------------------------------\n");
            writer.write("            Academic Members\n");
            writer.write("----------------------------------------\n");
            for(AcademicMember academicMember : AcademicMemberMap.values()) {
                writer.write(academicMember.getInfo());
            }
            writer.write("----------------------------------------\n\n");
            writer.write("----------------------------------------\n");
            writer.write("                STUDENTS\n");
            writer.write("----------------------------------------\n");
            for(Student student : StudentMap.values()) {
                writer.write(student.getInfo());
            }
            writer.write("----------------------------------------\n\n");
            writer.write("---------------------------------------\n");
            writer.write("              DEPARTMENTS\n");
            writer.write("---------------------------------------\n");
            for(String key : sortedKeysDepartment) {
                Department department = DepartmentsMap.get(key);
                String headID =department.headOfDepartment;
                String head;
                if(AcademicMemberMap.get(headID)==null){
                    head = "Not assigned";
                }
                else {
                    head = AcademicMemberMap.get(headID).name;
                }
                writer.write(department.getInfo());
                writer.write(head+"\n\n");
            }
            writer.write("----------------------------------------\n\n");
            writer.write("--------------------------------------\n");
            writer.write("                PROGRAMS\n");
            writer.write("---------------------------------------\n");
            for(String key : sortedKeysPrograms) {
                Program program = ProgramsMap.get(key);
                writer.write(program.getInfo());
            }
            writer.write("----------------------------------------\n\n");
            writer.write("---------------------------------------\n");
            writer.write("                COURSES\n");
            writer.write("---------------------------------------\n");
            for(String key : sortedKeysCourses) {
                Course course = CoursesMap.get(key);
                writer.write(course.getInfo());
            }
            writer.write("----------------------------------------\n\n");
            writer.write("----------------------------------------\n");
            writer.write("             COURSE REPORTS\n");
            writer.write("----------------------------------------\n");
            for(String key : sortedKeysCourses) {
                Course course = CoursesMap.get(key);
                List<String> sortedKeysGrades = new ArrayList<>(course.countGrades.keySet());
                Collections.sort(sortedKeysGrades);
                writer.write(course.getCourseReports());
                if(course.instructor==null){
                    writer.write("Not assigned\n\n");
                }
                else{
                    writer.write(course.instructor+"\n\n");
                }
                writer.write("Enrolled Students:\n");
                for(String student: course.EnrolledStudents){
                    writer.write("- "+StudentMap.get(student).name+" (ID: "+student+")\n");
                }
                writer.write("\nGrade Distribution:\n");
                for(String grade: sortedKeysGrades) {
                    if(course.countGrades.get(grade)>0){
                        writer.write(grade+": "+course.countGrades.get(grade)+"\n");
                    }
                }
                writer.write("\nAverage Grade: "+course.AverageGrade()+"\n\n");
                writer.write("----------------------------------------\n\n");
            }
            writer.write("----------------------------------------\n");
            writer.write("            STUDENT REPORTS\n");
            writer.write("----------------------------------------\n");
            for(String key : sortedKeysStudents) {
                Student student = StudentMap.get(key);
                writer.write(student.getInfo());
                writer.write("\nEnrolled Courses:\n");
                for(String enrolledCourse: student.EnrolledCourses) {
                    writer.write("- "+CoursesMap.get(enrolledCourse).name+" ("+enrolledCourse+")\n");
                }
                writer.write("\nCompleted Courses:\n");
                for(String completedCourse: student.CompletedCourses) {
                    writer.write("- "+CoursesMap.get(completedCourse).name+" ("+completedCourse+"): "+student.Grades.get(completedCourse)+"\n");
                }
                writer.write("\nGPA: "+student.calculateGrade(CoursesMap)+"\n");
                writer.write("----------------------------------------\n\n");

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}









