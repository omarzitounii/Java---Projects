package Zoo;

public class Elephant extends Animal {

    public Elephant(String name, int size, int foodNeed, int waterNeed){
        super(name, size, foodNeed, waterNeed);
    }

    @Override
    void makeSound() {
        System.out.println("Trumpet!");
    }

}
