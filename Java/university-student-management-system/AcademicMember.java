
public class AcademicMember extends People{
    /**
     * created an academic member object with given parameters in this constructor.
     * @param id academic member id
     * @param name academic member name
     * @param email academic member email
     * @param department academic member department
     */
    AcademicMember(String id,String name,String email,String department){
        super.id=id;
        super.name=name;
        super.email=email;
        super.department=department;
    }

    /**
     * gets information of academic member.
     * @return string with information
     */
    @Override
    public String getInfo() {
        return "Faculty ID: "+id+"\nName: "+name+"\nEmail: "+email+"\nDepartment: "+department +"\n\n";

    }
}
