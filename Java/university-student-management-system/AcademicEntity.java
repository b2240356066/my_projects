public abstract class AcademicEntity implements getInformation{
    /**
     * Academic entities have own code,name and description
     */
    protected String code;
    protected String name;
    protected String description;

    /**
     * used an interface with getInfo method to print the information about academic entity.
     * @return information about academic entity.
     */
    public abstract String getInfo();

}
