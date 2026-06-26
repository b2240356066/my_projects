public abstract class People implements getInformation{
    /**
     * every person has own name,id,email and department
     * used an interface with getInfo method to print the information about people.
     */
    protected String name;
    protected String id;
    protected String email;
    protected String department;

    public abstract String getInfo();
}




