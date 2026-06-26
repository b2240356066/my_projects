public class Lion extends Animals{
    /**
     * create the Lion with given parameters
     * @param name animal name
     * @param age animal age
     */
    public Lion(String name, int age) {
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
        MealSize = ((age-5)*0.05 +5)*dailyMeal;
        return MealSize;

    }
}
