package Zoo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Simulation {
    public static void main(String[] args) {
        Zoo myZoo = new Zoo(10000, 10000, 2);

        Lion Simba = new Lion("Simba", 1, 500, 500);
        Elephant Bumba = new Elephant("Bumba", 1, 500, 500);

        myZoo.addAnimal(Simba);
        myZoo.addAnimal(Bumba);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        String date = sdf.format(cal.getTime());
        System.out.println("Simulation starting date: " + date);

        boolean running = true;
        int daysCalculator = 0;
        while (running) {
            for (Animal animal : myZoo.getEnclosures()) {
                if (animal == null) continue;
                boolean fed = myZoo.provideFood(animal);
                boolean watered = myZoo.provideWater(animal);
                if (!fed || !watered) {
                    running = false;
                    break;
                }
            }
            if (running) {
                daysCalculator++;
            }
        }

        cal.add(Calendar.DAY_OF_MONTH, daysCalculator);
        String supplyDate = sdf.format(cal.getTime());
        System.out.println("Next supply date: " +supplyDate);
    }
}
