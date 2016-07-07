import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by asus-pc on 5/5/2016.
 */
public class Skill extends Ability implements Cloneable{
    public static Map<String, Skill> listOfSkills = new HashMap<String, Skill>();
//    private ArrayList<Property<E>> propertiesOfRelatedSoldiers;
//    private Property<Hero> propertiesOfUser;
//    protected Map<E, Time> mapOfEffectedSoldiers = new HashMap<E, Time>();
    private int nonTargetedEnemy;
    private boolean isRepeated;
    private Time timeOfEffecting;
    private ArrayList<String> blackList;
    private int[] cooldown;
    private int remainingCooldown;
    private boolean isDependsRelatedSoldiersSelectingOnPlayer;
    private boolean canStackUp;
    private boolean isUsed;
    private int[] requiredEnergyPoint;
    private int[] requiredMagicPoint;

    private ArrayList<String> classOfEffectedSoldiers = new ArrayList<>();
    private Map<String, Tree<ArrayList<Property>>> mapOfConditionsByClass = new HashMap<>();
    private Map<String, SelectingObjectsDetail> selectingEffectedObjectsDetails = new HashMap<>();
    private Map<Property, SelectingObjectsDetail> selectingEffectingObjectsDetails = new HashMap<>();

    private Map<String, Map> mapOfEffectedObjectsByClass = new HashMap<>();
    private Map<String, ArrayList> listOfEffectedObjectsByClass = new HashMap<>();
    private Map<String, Map> mapOfEffectedPropertiesByClass = new HashMap<>();

    private ArrayList<Property> properties = new ArrayList<>();



    //---------------------------------------------------------------- Constructors

    public Skill(SkillHandler skillHandler, AbilityHandler<E> abilityHandler) {
        super(abilityHandler);
        setPropertiesOfRelatedSoldiers(skillHandler.getPropertiesOfRelatedSoldiers());
        setPropertiesOfUser(skillHandler.getPropertiesOfUser());
        setNonTargetedEnemy(skillHandler.getNonTargetedEnemy());
        setRepeated(skillHandler.isRepeated());
        setTimeOfEffecting(skillHandler.getTimeOfEffecting());
        setBlackList(skillHandler.getBlackList());
        setCooldown(skillHandler.getCooldown());
        setRemainingCooldown(0);
        setDependsRelatedSoldiersSelectingOnPlayer(skillHandler.isDependsRelatedSoldiersSelectingOnPlayer());
        setCanStackUp(skillHandler.isCanStackUp());
        setUsed(false);
        setRequiredEnergyPoint(skillHandler.getRequiredEnergyPoint());
        setRequiredMagicPoint(skillHandler.getRequiredMagicPoint());
    }

    //---------------------------------------------------------------- Functions

    protected Skill<E> clone() throws CloneNotSupportedException {
        Skill<E> skill = (Skill<E>) super.clone();
        ArrayList<Property<E>> cloneOfPropertiesOfRelatedSoldiers = new ArrayList<>();
        for (Property<E> property: this.propertiesOfRelatedSoldiers) {
            cloneOfPropertiesOfRelatedSoldiers.add(property.clone());
        }
        skill.setPropertiesOfRelatedSoldiers(cloneOfPropertiesOfRelatedSoldiers);
        if (this.propertiesOfUser != null)
            skill.setPropertiesOfUser(this.propertiesOfUser.clone());
        skill.setMapOfEffectedSoldiers(new HashMap<>());
        return skill;
    }       // Creates A Copy of This Object (Skill)

    public boolean isActivated() {
       return false;
    }

    public void useSkill(Hero userHero) {

        if (this.remainingCooldown != 0) {
            return;
        }

        boolean isEffectedOnAtLeastOnObject = false;

        for (String className: this.classOfEffectedSoldiers) {
            ArrayList<?> effectedObjects = this.choosingEffectedObjects(new ArrayList<>(), className);
            for (int i = 0; i < effectedObjects.size(); i++) {
                if ((this.listOfEffectedObjectsByClass.get(className).contains(effectedObjects.get(i))) && (this.canStackUp == false)) {
                    continue;
                }
                isEffectedOnAtLeastOnObject = true;
                this.mapOfEffectedPropertiesByClass.get(className).put(effectedObjects.get(i), this.mapOfConditionsByClass.get(className).findCorrectNode(effectedObjects.get(i)));
                for (Property property: this.mapOfConditionsByClass.get(className).findCorrectNode(effectedObjects.get(i))) {
                    ArrayList<?> effectingObjects = this.choosingEffectingObjects(new ArrayList<>(), property);
                    double effectValue = property.effect(effectedObjects.get(i), effectingObjects);
                    Display.printInEachLine(userHero.getName() + " just used " + this.name + " on " + effectedObjects.get(i).toString() +  " and effecting on " + property.getName() + " with " + Math.abs(effectValue));
                }
                if (this.listOfEffectedObjectsByClass.get(className).contains(effectedObjects.get(i)) == false) {
                    this.listOfEffectedObjectsByClass.get(className).add(effectedObjects.get(i));
                }
                this.mapOfEffectedObjectsByClass.get(className).put(effectedObjects.get(i), new Time(this.timeOfEffecting));
            }
        }

        if (isEffectedOnAtLeastOnObject == false) {
            return;
        }

        this.remainingCooldown = this.cooldown[this.currentGrade - 1];
        userHero.setCurrentEnergyPoint(userHero.getCurrentEnergyPoint() - this.requiredEnergyPoint[this.currentGrade - 1]);
        userHero.setCurrentMagic(userHero.getCurrentMagic() - this.requiredMagicPoint[this.currentGrade - 1]);
    }

    public void removeEffect() {
        for (String className: this.classOfEffectedSoldiers) {
            ArrayList<?> effectedObjects = this.listOfEffectedObjectsByClass.get(className);
            ArrayList removedEffectedObjects = new ArrayList();
            for (int i = 0; i < effectedObjects.size(); i++) {
                if (((Time)this.mapOfEffectedObjectsByClass.get(className).get(effectedObjects.get(i))).isTimePassed()) {
                    for (Property property: (ArrayList<Property>)this.mapOfEffectedPropertiesByClass.get(className).get(effectedObjects.get(i))) {
                        property.removeEffect(effectedObjects.get(i));
                    }
                    removedEffectedObjects.add(effectedObjects.get(i));
                    this.mapOfEffectedObjectsByClass.get(className).remove(effectedObjects.get(i));
                    this.mapOfEffectedPropertiesByClass.get(className).remove(effectedObjects.get(i));
                }
            }
            this.listOfEffectedObjectsByClass.get(className).removeAll(removedEffectedObjects);
        }
    }

    public void reduceTime(String typeOfTime) {
        for (int i = 0; i < this.effectedSoldiers.size(); i++) {
            E soldier = (E) this.effectedSoldiers.get(i);
            this.mapOfEffectedSoldiers.get(soldier).reduceTime(typeOfTime);
        }
    }

    public void showDescription(){
        Display.printInEachLine(this.getDescription());
    }

    public ArrayList<?> choosingRelatedSoldiers(Enemy enemy,Hero hero) {
        return null;
    }

    public boolean equals(Skill skill){
        if(this.name.equals(skill.getName())){
            return true;
        }
        return false;
    }

    public ArrayList<?> choosingEffectedObjects(ArrayList<String> fromCommandLine, String classOfEffectedSoldiers) {
        if (classOfEffectedSoldiers.equals("Hero") && this.selectingEffectedObjectsDetails.containsKey("Hero")) {
            SelectingObjectsDetail<Hero> selectingObjectsDetail = this.selectingEffectedObjectsDetails.get("Hero");
            ArrayList<Hero> effectedHeroes = new ArrayList<>();
            if (selectingObjectsDetail.isObjectsWereSelectedByDefault()) {
                effectedHeroes.addAll(selectingObjectsDetail.getSelectedObjectsByDefault());
            }
            else if (selectingObjectsDetail.isAllRelatedObjectsInvolved()) {
                effectedHeroes.addAll(GameEngine.listOfHeroes);
            }
            else if (selectingObjectsDetail.isRandomObjectsSelecting()) {
                ArrayList<Hero> heroes = new ArrayList<Hero>();
                heroes.addAll(GameEngine.listOfHeroes);
                for (int i = 0; i < selectingObjectsDetail.getNumberOfSelectedObjects(); i++) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(heroes.size());
                    effectedHeroes.add(heroes.get(randomIndex));
                    heroes.remove(randomIndex);
                }
            }
            else if (selectingObjectsDetail.isSelectedObjectsDependsOnPlayer()) {
                // TODO
            }
            return effectedHeroes;
        }
        else if (classOfEffectedSoldiers.equals("Enemy") && this.selectingEffectedObjectsDetails.containsKey("Enemy")) {
            SelectingObjectsDetail<Enemy> selectingObjectsDetail = this.selectingEffectedObjectsDetails.get("Enemy");
            ArrayList<Enemy> effectedEnemies = new ArrayList<>();
            if (selectingObjectsDetail.isObjectsWereSelectedByDefault()) {
                effectedEnemies.addAll(selectingObjectsDetail.getSelectedObjectsByDefault());
            }
            else if (selectingObjectsDetail.isAllRelatedObjectsInvolved()) {
                effectedEnemies.addAll(GameEngine.listOfEnemies);
            }
            else if (selectingObjectsDetail.isRandomObjectsSelecting()) {
                ArrayList<Enemy> Enemies = new ArrayList<>();
                Enemies.addAll(GameEngine.listOfEnemies);
                for (int i = 0; i < selectingObjectsDetail.getNumberOfSelectedObjects(); i++) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(Enemies.size());
                    effectedEnemies.add(Enemies.get(randomIndex));
                    Enemies.remove(randomIndex);
                }
            }
            else if (selectingObjectsDetail.isSelectedObjectsDependsOnPlayer()) {
                // TODO
            }
            return effectedEnemies;
        }
        return null;
    }

    public ArrayList<?> choosingEffectingObjects(ArrayList<String> fromCommandLine, Property property) {
        if (property.getClassOfEffectingObjects().equals("Hero")) {
            SelectingObjectsDetail<Hero> selectingObjectsDetail = this.selectingEffectingObjectsDetails.get(property);
            ArrayList<Hero> effectingHeroes = new ArrayList<>();
            if (selectingObjectsDetail.isObjectsWereSelectedByDefault()) {
                effectingHeroes.addAll(selectingObjectsDetail.getSelectedObjectsByDefault());
            }
            else if (selectingObjectsDetail.isAllRelatedObjectsInvolved()) {
                effectingHeroes.addAll(GameEngine.listOfHeroes);
            }
            else if (selectingObjectsDetail.isRandomObjectsSelecting()) {
                ArrayList<Hero> heroes = new ArrayList<Hero>();
                heroes.addAll(GameEngine.listOfHeroes);
                for (int i = 0; i < selectingObjectsDetail.getNumberOfSelectedObjects(); i++) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(heroes.size());
                    effectingHeroes.add(heroes.get(randomIndex));
                    heroes.remove(randomIndex);
                }
            }
            else if (selectingObjectsDetail.isSelectedObjectsDependsOnPlayer()) {
                // TODO
            }
            return effectingHeroes;
        }
        else if (property.getClassOfEffectingObjects().equals("Enemy")) {
            SelectingObjectsDetail<Enemy> selectingObjectsDetail = this.selectingEffectingObjectsDetails.get(property);
            ArrayList<Enemy> effectingEnemies = new ArrayList<>();
            if (selectingObjectsDetail.isObjectsWereSelectedByDefault()) {
                effectingEnemies.addAll(selectingObjectsDetail.getSelectedObjectsByDefault());
            }
            else if (selectingObjectsDetail.isAllRelatedObjectsInvolved()) {
                effectingEnemies.addAll(GameEngine.listOfEnemies);
            }
            else if (selectingObjectsDetail.isRandomObjectsSelecting()) {
                ArrayList<Enemy> Enemies = new ArrayList<>();
                Enemies.addAll(GameEngine.listOfEnemies);
                for (int i = 0; i < selectingObjectsDetail.getNumberOfSelectedObjects(); i++) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(Enemies.size());
                    effectingEnemies.add(Enemies.get(randomIndex));
                    Enemies.remove(randomIndex);
                }
            }
            else if (selectingObjectsDetail.isSelectedObjectsDependsOnPlayer()) {
                // TODO
            }
            return effectingEnemies;
        }
        return null;
    }

    public boolean upgrade(Player player) {

        if (this.nameOfNecessaryAbilities.get(this.currentGrade + 1) != null) {
            for (String nameOfAbility: (ArrayList<String>)this.nameOfNecessaryAbilities.get(this.currentGrade + 1)) {
                if (Ability.listOfAbilities.get(nameOfAbility).equals("skill")) {
                    for (Skill skill: Hero.mapOfHeroes.get(this.ownerName).getSkills()) {
                        if (skill.getName().equals(nameOfAbility)) {
                            if (skill.getCurrentGrade() < ((Map<String, Integer>)this.gradeOfNecessaryAbilities.get(this.currentGrade + 1)).get(skill.getName()))
                                return false;
                            break;
                        }
                    }
                }
                if (Ability.listOfAbilities.get(nameOfAbility).equals("perk")) {
                    for (Perk perk: Hero.mapOfHeroes.get(this.ownerName).getPerks()) {
                        if (perk.getName().equals(nameOfAbility)) {
                            if (perk.getCurrentGrade() < ((Map<String, Integer>)this.gradeOfNecessaryAbilities.get(this.currentGrade + 1)).get(perk.getName()))
                                return false;
                            break;
                        }
                    }
                }
            }
        }

        this.currentGrade += 1;
        if (this.isAcquire == false)
            this.isAcquire = true;

        for (Property property: this.properties) {
            property.setCurrentGrade(this.currentGrade);
        }

        return true;

    }
    //---------------------------------------------------- Getter && Setters
    

    public ArrayList<String> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<String> blackList) {
        this.blackList = blackList;
    }

    public int getNonTargetedEnemy() {
        return nonTargetedEnemy;
    }

    public void setNonTargetedEnemy(int nonTargetedEnemy) {
        this.nonTargetedEnemy = nonTargetedEnemy;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public int[] getCooldown() {
        return cooldown;
    }

    public void setCooldown(int[] cooldown) {
        this.cooldown = cooldown;
    }

    public int getRemainingCooldown() {
        return remainingCooldown;
    }

    public void setRemainingCooldown(int remainingCooldown) {
        this.remainingCooldown = remainingCooldown;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public int[] getRequiredEnergyPoint() {
        return requiredEnergyPoint;
    }

    public void setRequiredEnergyPoint(int[] requiredEnergyPoint) {
        this.requiredEnergyPoint = requiredEnergyPoint;
    }

    public int[] getRequiredMagicPoint() {
        return requiredMagicPoint;
    }

    public void setRequiredMagicPoint(int[] requiredMagicPoint) {
        this.requiredMagicPoint = requiredMagicPoint;
    }

    public Time getTimeOfEffecting() {
        return timeOfEffecting;
    }

    public void setTimeOfEffecting(Time timeOfEffecting) {
        this.timeOfEffecting = timeOfEffecting;
    }

    public boolean isDependsRelatedSoldiersSelectingOnPlayer() {
        return isDependsRelatedSoldiersSelectingOnPlayer;
    }

    public void setDependsRelatedSoldiersSelectingOnPlayer(boolean dependsRelatedSoldiersSelectingOnPlayer) {
        isDependsRelatedSoldiersSelectingOnPlayer = dependsRelatedSoldiersSelectingOnPlayer;
    }

    public boolean isCanStackUp() {
        return canStackUp;
    }

    public void setCanStackUp(boolean canStackUp) {
        this.canStackUp = canStackUp;
    }

}
