package Zoo;

public abstract class Animal {
    protected String name;
    protected int size;
    protected int foodNeed;
    protected int waterNeed;

    public Animal(String name, int size, int foodNeed, int waterNeed) {
        this.name = name;
        this.size = size;
        this.foodNeed = foodNeed;
        this.waterNeed = waterNeed;
    }

    public double eat() {
        return size * foodNeed;
    }

    public double drink() {
        return size * waterNeed;
    }

    abstract void makeSound();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFoodNeed() {
        return foodNeed;
    }

    public void setFoodNeed(int foodNeed) {
        this.foodNeed = foodNeed;
    }

    public int getWaterNeed() {
        return waterNeed;
    }

    public void setWaterNeed(int waterNeed) {
        this.waterNeed = waterNeed;
    }
}
