public class Exceptions extends Exception {
    public Exceptions(String message) {
        super(message);
    }

    public static class InvalidPersonType extends Exceptions{
        public InvalidPersonType(String message) {
            super(message);
        }
    }

    public static class NonExistingStudent extends Exceptions{
        public NonExistingStudent(String message) {
            super(message);
        }
    }

    public static class NonExistingAcademicMember extends Exceptions{
        public NonExistingAcademicMember(String message) {
            super(message);
        }
    }

    public static class NonExistingProgram extends Exceptions{
        public NonExistingProgram(String message) {
            super(message);
        }
    }

    public static class NonExistingDepartment extends Exceptions{
        public NonExistingDepartment(String message) {
            super(message);
        }
    }

    public static class NonExistingCourse extends Exceptions{
        public NonExistingCourse(String message) {
            super(message);
        }
    }

    public static class InvalidLetterGrades extends Exceptions{
        public InvalidLetterGrades(String message) {
            super(message);
        }
    }


}


