public class Chimpanzee extends Animals{
    /**
     * create the Chimpanzee with given parameters
     * @param name animal name
     * @param age animal age
     */
    public Chimpanzee(String name, int age) {
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
        MealSize = ((age-10)*0.0125 +3)*dailyMeal;
        return MealSize;

    }
}
