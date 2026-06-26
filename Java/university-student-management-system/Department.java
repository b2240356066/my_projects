public class Department extends AcademicEntity{
    protected String headOfDepartment;

    /**
     * create a department object with given parameters in this constructor.
     * @param code department code
     * @param name department name
     * @param description department descripton
     * @param headOfDepartment head of the department
     */
    Department(String code, String name, String description,String headOfDepartment) {
        super.code=code;
        super.name=name;
        super.description=description;
        this.headOfDepartment=headOfDepartment;
    }

    /**
     * get information about the department
     * @return string of information
     */
    public String getInfo(){
        return "Department Code: "+code+ "\nName: " + name+ "\nHead: ";
    }
}
