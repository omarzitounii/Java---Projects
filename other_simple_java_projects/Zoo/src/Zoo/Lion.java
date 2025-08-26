package Zoo;

public class Lion extends Animal{

    public Lion(String name, int size, int foodNeed, int waterNeed){
        super(name, size, foodNeed, waterNeed);
    }

    @Override
    void makeSound() {
        System.out.println("Roar!");
    }
}
