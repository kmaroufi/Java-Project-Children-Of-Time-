import java.util.*;

/**
 * Created by asus-pc on 5/5/2016.
 */
public class Perk<E> extends Ability implements Cloneable{
    public static Map<String, Perk> listOfPerks = new HashMap<>();
    private ArrayList<Condition> listOfCondition;
    private ArrayList<PerkMode<E>> listOfModes;
    private Map<Condition, PerkMode<E>> mapOfCondition;
    private Map<E, PerkMode<E>> mapOfRelatedSoldiers = new HashMap<>();
    private boolean isConditionDependOnRelatedSoldier;
    private boolean isConditionDependOnUserHero;
    private String timeOfCheck; // Can equals "duringAttack", "duringDefend" and "eachActivity"

    //---------------------------------------------------------- Constructors


    public Perk(AbilityHandler<E> abilityHandler, ArrayList<Condition> listOfCondition, ArrayList<PerkMode<E>> listOfModes, Map<Condition, PerkMode<E>> mapOfCondition, boolean isConditionDependOnRelatedSoldier, boolean isConditionDependOnUserHero, String timeOfCheck) {
        super(abilityHandler);
        this.listOfCondition = listOfCondition;
        this.listOfModes = listOfModes;
        this.mapOfCondition = mapOfCondition;
        this.isConditionDependOnRelatedSoldier = isConditionDependOnRelatedSoldier;
        this.isConditionDependOnUserHero = isConditionDependOnUserHero;
        this.timeOfCheck = timeOfCheck;
    }


    public Perk() {}

    //---------------------------------------------------------- Functions


    protected Perk clone() throws CloneNotSupportedException {
        Perk<E> perk = (Perk) super.clone();
        perk.setMapOfRelatedSoldiers(new HashMap<>());
        ArrayList<PerkMode<E>> cloneOfListOfModes = new ArrayList<>();
        Map<Condition, PerkMode<E>> cloneOfMapOfConditions = new HashMap<>();
        for (Condition condition: this.listOfCondition) {
            PerkMode<E> cloneOfPerkMode = this.mapOfCondition.get(condition).clone();
            cloneOfListOfModes.add(cloneOfPerkMode);
            cloneOfMapOfConditions.put(condition, cloneOfPerkMode);
        }
        perk.setListOfModes(cloneOfListOfModes);
        perk.setMapOfCondition(cloneOfMapOfConditions);
        perk.setRelatedSoldiers(new ArrayList());
        perk.setEffectedSoldiers(new ArrayList());
        return perk;
    }

    public boolean equals(Perk perk){
        if(perk.getName().equals(this.name)){
            return true;
        }
        return false;
    }

    private Condition validCondition(E relatedSoldier) {
        for (Condition condition: this.listOfCondition) {
            if (condition.checkCondition(relatedSoldier)) {
                return condition;
            }
        }
        return null;
    }

    public void updatePerkEffect(ArrayList<E> relatedSoldiers, Hero userHero) {
        Hero owner = Hero.mapOfHeroes.get(this.ownerName);
        if (this.listOfCondition.size() == 1) {
            for (E relatedSoldier: relatedSoldiers) {
                if (this.mapOfRelatedSoldiers.containsKey(relatedSoldier))
                    continue;
                else {
                    this.mapOfRelatedSoldiers.put(relatedSoldier, this.listOfModes.get(0));
                    this.listOfModes.get(0).effect(relatedSoldier, owner, userHero);
                }

            }
            return;
        }
        for (E soldier: relatedSoldiers) {
            PerkMode perkMode;
            if (this.isConditionDependOnRelatedSoldier)
                perkMode = this.mapOfCondition.get(this.validCondition((E) relatedSoldiers));
            else if (this.isConditionDependOnUserHero)
                perkMode = this.mapOfCondition.get(this.validCondition((E) userHero));
            else {
                perkMode = this.mapOfCondition.get(this.validCondition((E) owner));
            }
            if (this.mapOfRelatedSoldiers.containsKey(soldier) == false) {
                this.mapOfRelatedSoldiers.put(soldier, perkMode);
                perkMode.effect(relatedSoldiers, owner, userHero);
                continue;
            }
            if (this.mapOfRelatedSoldiers.get(soldier) == perkMode)
                continue;
            else {
                this.mapOfRelatedSoldiers.get(soldier).removeEffect(soldier);
                this.mapOfRelatedSoldiers.put(soldier, perkMode);
                perkMode.effect(relatedSoldiers, owner, userHero);
            }
        }

    }

    public void choosingRelatedSoldiers(Enemy enemy,Hero hero) {  // in method dar moghe attack ya defend call mishavad;
        this.relatedSoldiers.clear();
        if (this.hasEffectedOnEnemy) {
            this.relatedSoldiers.add(enemy);
            return;
        }
        this.relatedSoldiers.add(hero);
    }

    public void showDescription(){
        Display.printInEachLine(this.getDescription());
    }

    public void choosingRelatedSoldiers() {
        this.relatedSoldiers.clear();
        if (this.hasEffectedOnEnemy) {
            this.relatedSoldiers.addAll(GameEngine.listOfEnemies);
            return;
        }
        if (this.numberOfRelatedSoldiers == 1) {
            this.relatedSoldiers.add(Hero.mapOfHeroes.get(this.ownerName));
            return;
        }
        this.relatedSoldiers.addAll(GameEngine.listOfHeroes);
    }
    //---------------------------------------------------------- Getter && Setters

    public ArrayList<Condition> getListOfCondition() {
        return listOfCondition;
    }

    public void setListOfCondition(ArrayList<Condition> listOfCondition) {
        this.listOfCondition = listOfCondition;
    }

    public ArrayList<PerkMode<E>> getListOfModes() {
        return listOfModes;
    }

    public void setListOfModes(ArrayList<PerkMode<E>> listOfModes) {
        this.listOfModes = listOfModes;
    }

    public Map<Condition, PerkMode<E>> getMapOfCondition() {
        return mapOfCondition;
    }

    public void setMapOfCondition(Map<Condition, PerkMode<E>> mapOfCondition) {
        this.mapOfCondition = mapOfCondition;
    }

    public Map<E, PerkMode<E>> getMapOfRelatedSoldiers() {
        return mapOfRelatedSoldiers;
    }

    public void setMapOfRelatedSoldiers(Map<E, PerkMode<E>> mapOfRelatedSoldiers) {

    }

    public boolean isConditionDependOnRelatedSoldier() {
        return isConditionDependOnRelatedSoldier;
    }

    public void setConditionDependOnRelatedSoldier(boolean conditionDependOnRelatedSoldier) {
        isConditionDependOnRelatedSoldier = conditionDependOnRelatedSoldier;
    }

    public boolean isConditionDependOnUserHero() {
        return isConditionDependOnUserHero;
    }

    public void setConditionDependOnUserHero(boolean conditionDependOnUserHero) {
        isConditionDependOnUserHero = conditionDependOnUserHero;
    }

    public String getTimeOfCheck() {
        return timeOfCheck;
    }

    public void setTimeOfCheck(String timeOfCheck) {
        this.timeOfCheck = timeOfCheck;
    }
}
