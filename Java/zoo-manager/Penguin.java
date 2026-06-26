public class Penguin extends Animals{
    /**
     * create the Penguin with given parameters
     * @param name animal name
     * @param age animal age
     */
    public Penguin(String name, int age) {
        super.name=name;
        super.age=age;
    }
    /**
     * calculate total meal
     * @param dailyMeal number of how many times animal will eat
     * @return total meal size
     */
    @Override
    public double MealSize(double dailyMeal){
        double MealSize;
        MealSize = ((age-4)*0.04 +3)*dailyMeal;
        return MealSize;

    }
}
