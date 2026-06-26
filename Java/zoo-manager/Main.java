
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        /**
         * Takes text files as arguments.
         */
        String animals = args[0]; // animals text file
        String people = args[1]; // people text file
        String foods = args[2]; // foods text file
        String commands = args[3]; // commands text file
        String output = args[4]; // output text file


        //the map that contains the animals (name,age)
        LinkedHashMap<String, Animals> AnimalsMap = readTextAnimals(animals);
        //the map that contains the people (name,id)
        LinkedHashMap<String, People> PeopleMap = readTextPeople(people);
        //the map that contains foods (type,stock)
        LinkedHashMap<String, Double> FoodMap = readTextFoods(foods);

        /**
         * create an instance ZooManagement with th maps that are created with given text files.
         * read the commands file and execute subsequent operations.
         * close the output file when all command lines are executed.
         */

        ZooManagement ZooManagement = new ZooManagement(AnimalsMap,PeopleMap,FoodMap,output);
        ZooManagement.readTxtCommands(commands,AnimalsMap,PeopleMap,FoodMap);
        ZooManagement.closeWriter();
    }

    public static LinkedHashMap<String, Animals> readTextAnimals(String animals){
        LinkedHashMap<String, Animals> AnimalsMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(animals))) {
            String line;
            while((line = br.readLine()) != null){String[] parts = line.split(",");

                if (parts[0].equals("Lion")){
                    // if the first word is "Lion".
                    Animals lion = new Lion(parts[1],Integer.parseInt(parts[2])); // parts[1] => name, parts[2] => age,
                    // add the animal to AnimalsMap.
                    AnimalsMap.put(parts[1],lion);


                }
                else if (parts[0].equals("Elephant")){
                    // if the first word is "Elephant".
                    Animals elephant = new Elephant(parts[1],Integer.parseInt(parts[2])); // parts[1] => name, parts[2] => age,
                    // add the animal to AnimalsMap.
                    AnimalsMap.put(parts[1],elephant);

                }
                else if (parts[0].equals("Penguin")){
                    // if the first word is "Penguin".
                    Animals penguin = new Penguin(parts[1],Integer.parseInt(parts[2])); // parts[1] => name, parts[2] => age,
                    // add the animal to AnimalsMap.
                    AnimalsMap.put(parts[1],penguin);

                }
                else if (parts[0].equals("Chimpanzee")){
                    //if the first word is "Chimpanzee".
                    Animals chimpanzee = new Chimpanzee(parts[1],Integer.parseInt(parts[2])); // parts[1] => name, parts[2] => age,
                    // add the animal to AnimalsMap.
                    AnimalsMap.put(parts[1],chimpanzee);

                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return AnimalsMap;

    }
    public static LinkedHashMap<String, People> readTextPeople(String people){
        LinkedHashMap<String, People> PeopleMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(people))) {
            String line;
            while((line = br.readLine()) != null){String[] parts = line.split(",");

                if (parts[0].equals("Personnel")){
                    // if the first word is "Personnel".
                    People personnel = new Personnel(parts[1],parts[2]); // parts[1] => name, parts[2] => id,
                    // add the person.
                    PeopleMap.put(parts[2],personnel);
                }
                else if (parts[0].equals("Visitor")){
                    // if he first word is "Visitor".
                    People visitor = new Visitor(parts[1],parts[2]); // parts[1] => name, parts[2] => id,
                    // add the person.
                    PeopleMap.put(parts[2],visitor);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return PeopleMap;
    }


    public static  LinkedHashMap<String, Double> readTextFoods(String foods){
        LinkedHashMap<String, Double> FoodMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(foods))) {
            String line;
            while((line = br.readLine()) != null){String[] parts = line.split(",");

                if (parts[0].equals("Plant")){
                    //if the first word is "Plant".
                    // put food in the map.
                    FoodMap.put(parts[0],Double.parseDouble(parts[1]));

                }
                else if (parts[0].equals("Meat")){
                    // if the first word is "Meat".
                    // put food in the map.
                    FoodMap.put(parts[0],Double.parseDouble(parts[1]));

                }
                else if (parts[0].equals("Fish")){
                    // if the first word is "Fish".
                    // put food in the map.
                    FoodMap.put(parts[0],Double.parseDouble(parts[1]));
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return FoodMap;

    }

}
