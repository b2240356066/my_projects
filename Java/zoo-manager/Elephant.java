public class Elephant extends Animals{
    /**
     * create the Elephant with given parameters
     * @param name animal name
     * @param age animal age
     */
    public Elephant(String name, int age) {
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
        MealSize = ((age-20)*0.015 +10)*dailyMeal;
        return MealSize;

    }
}
