//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
/**
 * takes text files as arguments
 */
        String people = args[0];
        String departments = args[1];
        String programs = args[2];
        String courses = args[3];
        String assignments = args[4];
        String grades = args[5];
        String output = args[6];

        StudentManagementSystem studentManagementSystem = new StudentManagementSystem(people,departments,programs,courses,assignments,grades,output);
        /**
         * reads all the information with given files
         */
        studentManagementSystem.readAll();
        /**
         * prints all the information about students,academic members,departments etc.
         */
        studentManagementSystem.getAllInfo();




        }
}