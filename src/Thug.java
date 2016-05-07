import java.util.Random;

/**
 * Created by asus-pc on 5/7/2016.
 */
public class Thug extends Enemy {
    public Thug(String version){
        this.setClassName("Thug");
        this.setFullName();
        if(version.equals("Weak")){
            this.setAttackPower(50);
            this.setMaximumHealth(200);
        }
        else if(version.equals("Able")){
            this.setAttackPower(90);
            this.setMaximumHealth(300);
        }
        else if(version.equals("Mighty")){
            this.setAttackPower(150);
            this.setMaximumHealth(400);
        }
    }
    public void doTurn() {                          // Chooses Random Hero From All Heroes
        Random random = new Random();
        int randomIndex = random.nextInt(Hero.listOfHeroes.size());
        Hero targetedHero = GameEngine.listOfHeroes.get(randomIndex);
        targetedHero.getDamage(this.attackPower);
        Console.printInEachLine("Thug just attacked " + targetedHero.getName() + " with " + this.getAttackPower() + " power");
        if(targetedHero.isDead()){
            GameEngine.listOfHeroes.remove(targetedHero);
        }
    }
}
