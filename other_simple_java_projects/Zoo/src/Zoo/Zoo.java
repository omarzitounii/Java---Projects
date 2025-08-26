package Zoo;

public class Zoo {
    private double food;
    private double water;
    private final int enclosureCount;
    private Animal[] enclosures;

    public Zoo(double food, double water, int enclosureCount) {
        this.food = food;
        this.water = water;
        this.enclosureCount = enclosureCount;
        enclosures = new Animal[enclosureCount];
    }

    public void addAnimal(Animal animal) {
        boolean enclosureNotFound = true;
        for (int i = 0; i < enclosureCount; i++) {
            if (enclosures[i] == null) {
                enclosures[i] = animal;
                enclosureNotFound = false;
                break;
            }
        }
        if (enclosureNotFound) {
            System.out.println("No available enclosure!");
        }
    }

    public boolean provideFood(Animal animal) {
        double neededFood = animal.eat();
        if (food >= neededFood) {
            food -= neededFood;
            return true;
        }
        return false;
    }

    public boolean provideWater(Animal animal) {
        double neededWater = animal.drink();
        if (water >= neededWater) {
            water -= neededWater;
            return true;
        }
        return false;
    }

    public double getFood() {
        return food;
    }

    public void setFood(double food) {
        this.food = food;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }

    public int getEnclosureCount() {
        return enclosureCount;
    }

    public Animal[] getEnclosures() {
        return enclosures;
    }

}
