import java.io.*;
import java.util.LinkedHashMap;

public class ZooManagement {
    private LinkedHashMap<String,Animals> AnimalsMap;
    private LinkedHashMap<String,People> PeopleMap;
    private LinkedHashMap<String,Double> FoodMap;
    private BufferedWriter writer;

    /**
     * initialize the maps and the output text file
     * @param AnimalsMap the map that contains the animals
     * @param PeopleMap the map that contains people
     * @param FoodMap the map that contains foods
     * @param output the output file
     */

    public ZooManagement(LinkedHashMap<String,Animals> AnimalsMap,LinkedHashMap<String,People> PeopleMap, LinkedHashMap<String,Double> FoodMap, String output) {
        this.AnimalsMap = AnimalsMap;
        this.PeopleMap = PeopleMap;
        this.FoodMap = FoodMap;
        try {
            this.writer = new BufferedWriter(new FileWriter(output,true)); // initialize writer
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // close the file when all the lines are printed out.
    public void closeWriter() {
        try {
            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write the output to output.txt file.
    public void writeTxtOutput(String message) {
        try {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read the commands and execute the necessary operations
     * @param commands commands text file
     * @param AnimalsMap the map that contains the animals
     * @param PeopleMap the map that contains people
     * @param FoodMap the map that contains foods
     */
    public void readTxtCommands(String commands,LinkedHashMap<String,Animals> AnimalsMap,LinkedHashMap<String,People> PeopleMap, LinkedHashMap<String,Double> FoodMap) {
        {
            try (BufferedReader br = new BufferedReader(new FileReader(
                    commands))) {
                String line;
                Initialize(AnimalsMap,PeopleMap,FoodMap);
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    boolean flag = true;

                    switch (parts[0]) {
                        // if parts[0] is List Food Stock
                        case "List Food Stock" :{
                            ListFoodStock(FoodMap);
                            break;
                        }
                        // if parts[0] is Animal Visitation
                        case "Animal Visitation" :{
                            visitingAnimal(parts[1],parts[2],AnimalsMap,PeopleMap);
                            break;

                        }
                        // if the parts[0] is Feed Animal
                        case "Feed Animal":{
                            try{
                                /**
                                 * if the part[3] the part where it determines how many times animal will be eating is not an integer throw an exception
                                 */
                                if(!isANumber(parts[3])){
                                    writeTxtOutput("***********************************");
                                    writeTxtOutput("***Processing new Command***");
                                    throw new Exceptions.CannotProcessCommand("Error processing command: " + line +"\nError:For input string: \""+parts[3]+"\"" );
                                }
                            }
                            catch(Exceptions.CannotProcessCommand e){
                                writeTxtOutput(e.getMessage());
                                flag = false;

                            }
                            if(flag){
                                feedingAnimal(parts[1],parts[2],parts[3],AnimalsMap,PeopleMap,FoodMap);

                            }
                            break;

                        }
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * to check if parts[3] is an integer.
     * @param string parts[3]
     * @return if it is return true else return false
     */
    public boolean isANumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }

    }

    /**
     * write the available food stock
     * @param FoodMap the map that contains foods
     */
    public void ListFoodStock(LinkedHashMap<String,Double> FoodMap) {
        String plant = String.format("Plant: %.3f kgs",FoodMap.get("Plant"));
        String meat = String.format("Meat: %.3f kgs",FoodMap.get("Meat"));
        String fish = String.format("Fish: %.3f kgs",FoodMap.get("Fish"));
        writeTxtOutput("***********************************");
        writeTxtOutput("***Processing new Command***");
        writeTxtOutput("Listing available Food Stock:");
        writeTxtOutput(plant);
        writeTxtOutput(fish);
        writeTxtOutput(meat);

    }

    /**
     * write all information about animals, people and foods
     * @param AnimalsMap the map that contains the animals
     * @param PeopleMap the map that contains people
     * @param FoodMap the map that contains foods
     */
    public void Initialize(LinkedHashMap<String,Animals> AnimalsMap,LinkedHashMap<String,People> PeopleMap, LinkedHashMap<String,Double> FoodMap){

        String Plant = String.format("There are %.3f kg of Plant in stock",FoodMap.get("Plant"));
        String Meat = String.format("There are %.3f kg of Meat in stock",FoodMap.get("Meat"));
        String Fish = String.format("There are %.3f kg of Fish in stock",FoodMap.get("Fish"));

        writeTxtOutput("***********************************");
        writeTxtOutput("***Initializing Animal information***");
        for (Animals animal : AnimalsMap.values()) {
            if(animal instanceof Lion){
                writeTxtOutput("Added new Lion with name "+animal.name + " aged "+animal.age + ".");
            }
            if(animal instanceof Elephant){
                writeTxtOutput("Added new Elephant with name "+animal.name + " aged "+animal.age + ".");
            }
            if(animal instanceof Penguin){
                writeTxtOutput("Added new Penguin with name "+animal.name + " aged "+animal.age + ".");
            }
            if(animal instanceof Chimpanzee){
                writeTxtOutput("Added new Chimpanzee with name "+animal.name + " aged "+animal.age + ".");
            }
        }
        writeTxtOutput("***********************************");
        writeTxtOutput("***Initializing Visitor and Personnel information***");
        for (People people : PeopleMap.values()) {
            if(people instanceof Personnel){
                writeTxtOutput("Added new Personnel with id " + people.id + " and name "+ people.name+ ".");
            }
            if(people instanceof Visitor){
                writeTxtOutput("Added new Visitor with id " + people.id + " and name "+ people.name + ".");
            }
        }
        writeTxtOutput("***********************************");
        writeTxtOutput("***Initializing Food Stock***");

        writeTxtOutput(Meat);
        writeTxtOutput(Fish);
        writeTxtOutput(Plant);


    }

    /**
     * visit the animal depending on type of the person
     * @param userID person's id
     * @param animalName animal's name
     * @param AnimalsMap the map that contains animals
     * @param PeopleMap the map that contains people
     */

    public void visitingAnimal(String userID, String animalName, LinkedHashMap<String,Animals> AnimalsMap,LinkedHashMap<String,People> PeopleMap) {

        People user = PeopleMap.get(userID);
        Animals animal = AnimalsMap.get(animalName);

        try {
            if (!AnimalsMap.containsKey(animalName)) {
                writeTxtOutput("***********************************");
                writeTxtOutput("***Processing new Command***");
                if(PeopleMap.get(userID) instanceof Visitor) {
                    String visitor = String.format("%s tried to register a visit to %s",PeopleMap.get(userID).name,animalName);
                    writeTxtOutput(visitor);
                }
                if(PeopleMap.get(userID) instanceof Personnel) {
                    String personnel = String.format("%s attempts to clean %s's habitat.",PeopleMap.get(userID).name,animalName);
                    writeTxtOutput(personnel);
                }
                throw new Exceptions.AnimalNotFound("Error: There are no animals with the name " + animalName +".");
            }
        }
        catch (Exceptions.AnimalNotFound e) {
            writeTxtOutput(e.getMessage());
            return;
        }

        try {
            if (!PeopleMap.containsKey(userID)) {
                writeTxtOutput("***********************************");
                writeTxtOutput("***Processing new Command***");
                throw new Exceptions.UserNotFound("Error: There are no visitors or personnel with the id "+ userID);
            }
        }
        catch (Exceptions.UserNotFound e) {
            writeTxtOutput(e.getMessage());
            return;

        }

        if(user instanceof Visitor) {
            writeTxtOutput("***********************************");
            writeTxtOutput("***Processing new Command***");
             writeTxtOutput(user.visitAnimal(animal));
        }
        if(user instanceof Personnel) {
            writeTxtOutput("***********************************");
            writeTxtOutput("***Processing new Command***");
            writeTxtOutput(user.visitAnimal(animal));
        }

    }

    /**
     * feed the animal if person is authorized.
     * @param userID person's id
     * @param animalName animal's id.
     * @param mealSize meal size the animal is going to eat
     * @param AnimalsMap map that contains animals
     * @param PeopleMap map that contains people
     * @param FoodMap map that contains foods.
     */
    public void feedingAnimal(String userID, String animalName,String mealSize,LinkedHashMap<String,Animals> AnimalsMap,LinkedHashMap<String,People> PeopleMap, LinkedHashMap<String,Double> FoodMap) {

        People user = PeopleMap.get(userID);
        Animals animal = AnimalsMap.get(animalName);

        try {
            if (!PeopleMap.containsKey(userID)) {
                writeTxtOutput("***********************************");
                writeTxtOutput("***Processing new Command***");
                throw new Exceptions.UserNotFound("Error: There are no visitors or personnel with the id "+ userID);
            }
        }
        catch (Exceptions.UserNotFound e) {
            writeTxtOutput(e.getMessage());
            return;
        }

        try {
            if (!AnimalsMap.containsKey(animalName)) {
                writeTxtOutput("***********************************");
                writeTxtOutput("***Processing new Command***");
                if(user instanceof Visitor) {
                    writeTxtOutput(user.feedAnimals(animal,mealSize,user,FoodMap));
                }
                if(user instanceof Personnel) {
                    String personnel = String.format("%s attempts to feed %s.",PeopleMap.get(userID).name,animalName);
                    writeTxtOutput(personnel);
                }
                throw new Exceptions.AnimalNotFound("Error: There are no animals with the name " + animalName + ".");
            }
        }
        catch (Exceptions.AnimalNotFound e) {
            writeTxtOutput(e.getMessage());
            return;
        }

        writeTxtOutput("***********************************");
        writeTxtOutput("***Processing new Command***");
        writeTxtOutput(user.feedAnimals(animal,mealSize,user,FoodMap));

    }



}
