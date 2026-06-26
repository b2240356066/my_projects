import java.util.HashMap;

public abstract class People {
    /**
     * every person have their own name and id.
     */

    protected String name;
    protected String id;

    /**
     * both visitors and personals can visit animals.
     * @param animal the animal that is going to be visited.
     * @return the message which depends on who visits the animal.
     */
    public abstract String visitAnimal(Animals animal);

    /**
     * visitor cannot feed the animal but personnel can.
     * they both have different messages depending on who attempts to feed.
     * @param animal the animal that is going to be fed.
     * @param mealSize the amount of food that will be given.
     * @param user the person that is going to feed.
     * @param FoodMap the food amounts
     * @return the message
     */
    public abstract String feedAnimals(Animals animal,String mealSize,People user,  HashMap<String,Double> FoodMap);
}


class Visitor extends People{
    /**
     * create visitor with given parameters.
     * @param name person's name
     * @param id person's id
     */
    public Visitor(String name,String id) {
        super.name = name;
        super.id = id;
    }

    /**
     * write the message with given names.
     * @param animal the animal that is going to be visited.
     * @return visitorVisitAnimalMessage
     */
    @Override
    public String visitAnimal(Animals animal) {
        String visitorVisitAnimalMessage = String.format("%s tried to register for a visit to %s.%n%s successfully visited %s.",name,animal.name,name,animal.name);
        return visitorVisitAnimalMessage;
    }

    /**
     * write the message with given names.
     * @param animal the animal that is going to be fed.
     * @param mealSize the amount of food that will be given.
     * @param user the person that is going to feed.
     * @param FoodMap the food amounts
     * @return visitorFeedAnimalMessage
     */
    @Override
    public String feedAnimals(Animals animal,String mealSize,People user,  HashMap<String,Double> FoodMap) {
        String visitorFeedAnimalMessage = String.format("%s tried to feed %s%nError: Visitors do not have the authority to feed animals.",name,animal.name);
        return visitorFeedAnimalMessage;

    }

}

class Personnel extends People{
    /**
     * create personnel with given parameters.
     * @param name person's name
     * @param id person's id
     */
    public Personnel(String name,String id) {
        super.name = name;
        super.id = id;

    }

    /**
     * write the message with given names depending on type of the animal.
     * @param animal the animal that is going to be visited.
     * @return message
     */
    @Override
    public String visitAnimal(Animals animal) {
        String message = String.format("%s attempts to clean %s's habitat.%n%s started cleaning %s's habitat.%n",name,animal.name,name,animal.name);

        if(animal instanceof Lion){
            message = message + String.format("Cleaning %s's habitat: Removing bones and refreshing sand.",animal.name);
        }
        else if(animal instanceof Elephant){
            message = message + String.format("Cleaning %s's habitat: Washing the water area.",animal.name);
        }
        else if(animal instanceof Chimpanzee ){
            message = message + String.format("Cleaning %s's habitat: Sweeping the enclosure and replacing branches.",animal.name);
        }
        else if(animal instanceof Penguin){
            message = message + String.format("Cleaning %s's habitat: Replenishing ice and scrubbing walls.",animal.name);
        }
        return message;

    }

    /**
     * write the message with given names depending on type of the animal.
     * check if there are enough food for the animal.
     * if not throw an exception. (Insufficient food)
     * @param animal the animal that is going to be fed.
     * @param mealSize the amount of food that will be given.
     * @param user the person that is going to feed.
     * @param FoodMap the food amounts
     * @return message
     */
    @Override
    public String feedAnimals(Animals animal,String mealSize,People user,  HashMap<String,Double> FoodMap) {
        String message = String.format("%s attempts to feed %s.",name,animal.name);
        Double dailyMealSize = animal.MealSize(Double.parseDouble(mealSize));
        double meal;

        if(animal instanceof Lion){
                try{
                    if(FoodMap.get("Meat")>=dailyMealSize){
                    meal =FoodMap.get("Meat");
                    FoodMap.put("Meat",meal - dailyMealSize);
                    message = message + String.format("%n%s has been given %.3f kgs of meat",animal.name,dailyMealSize);
                }
                    else {
                        throw new Exceptions.NotEnoughMeat("Error: Not enough Meat");
                    }

            }
                catch(Exceptions.NotEnoughMeat e){
                    message = message + String.format("%nError: Not enough Meat");
                }

        }

        if(animal instanceof Elephant){
            try{
                if(FoodMap.get("Plant")>=dailyMealSize){
                    meal =FoodMap.get("Plant");
                    FoodMap.put("Plant",meal - dailyMealSize);
                    message = message + String.format("%n%s has been given %.3f kgs assorted fruits and hay",animal.name,dailyMealSize);
                }
                else {
                    throw new Exceptions.NotEnoughPlant("Error: Not enough Plant");
                }

            }
            catch(Exceptions.NotEnoughPlant e){
                message = message + String.format("%nError: Not enough Plant");
            }

        }

        if(animal instanceof Penguin){
            try{
                if(FoodMap.get("Fish")>=dailyMealSize){
                    meal =FoodMap.get("Fish");
                    FoodMap.put("Fish",meal - dailyMealSize);
                    message = message + String.format("%n%s has been given %.3f kgs of various kinds of fish",animal.name,dailyMealSize);
                }
                else {
                    throw new Exceptions.NotEnoughFish("Error: Not enough Fish");
                }

            }
            catch(Exceptions.NotEnoughFish e){
                message = message + String.format("%nError: Not enough Fish");
            }

        }

        if(animal instanceof Chimpanzee){
            try{
                Double mealMeat;
                Double mealPlant;
                if(FoodMap.get("Meat")>=dailyMealSize&&FoodMap.get("Plant")>=dailyMealSize){
                    mealMeat = FoodMap.get("Meat");
                    mealPlant =FoodMap.get("Plant");
                    FoodMap.put("Meat",mealMeat - dailyMealSize);
                    FoodMap.put("Plant",mealPlant - dailyMealSize);
                    message = message + String.format("%n%s has been given %.3f kgs of meat and %.3f kgs of leaves",animal.name,dailyMealSize,dailyMealSize);
                }
                else {
                    if(FoodMap.get("Plant")<dailyMealSize){
                        throw new Exceptions.NotEnoughPlant("Error: Not Enough Plant");
                    }
                    if(FoodMap.get("Meat")<dailyMealSize){
                        throw new Exceptions.NotEnoughMeat("Error: Not enough Meat");
                    }

                }

            }
            catch(Exceptions.NotEnoughMeat e){
                message = message + String.format("%nError: Not enough Meat");
            }
            catch (Exceptions.NotEnoughPlant e){
                message = message + String.format("%nError: Not enough Plant");
            }

        }
        return message;

    }
}









