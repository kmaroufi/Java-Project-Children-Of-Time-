import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Future on 5/6/2016.
 */
public class HeroClassHandler extends Soldier implements Cloneable{
    private ArrayList<Perk> perks = new ArrayList<>();
    private ArrayList<Skill> skills = new ArrayList<>();
    private double criticalHitChance;
    private double criticalHitChanceRatio;
    private double criticalHitDamage;
    private double criticalHitDamageRatio;
    private double magicRefillRate;
    private double attackPowerRatioDuringAttack;
    private double healthRefillRateRatio;
    private double magicRefillRateRatio;
    private double energyPointRatio;
    private double attackPowerRatioOnNonTargetedEnemy;
    private double attackPowerOnNonTargetedEnemy;
    private int maximumMagic;
    private int currentMagic;
    private int numberOfNonTargetedEnemyEffected;
    private int maximumEnergyPoint;
    private int currentEnergyPoint;
    private int inventorySize;


    //---------------------------------------------------------------- Constructors


    protected HeroClassHandler clone() throws CloneNotSupportedException {
        HeroClassHandler clone = (HeroClassHandler) super.clone();
        ArrayList<Perk> cloneOfPerks = new ArrayList<>();
        for (Perk perk: this.perks) {
            cloneOfPerks.add(perk.clone());
        }
        ArrayList<Skill> cloneOfSkills = new ArrayList<>();
        for (Skill skill: this.getSkills()) {
            cloneOfSkills.add(skill.clone());
        }
        clone.setPerks(cloneOfPerks);
        clone.setSkills(cloneOfSkills);
        return clone;
    }

    public HeroClassHandler(){}
    public HeroClassHandler(String className,int maximumHealth,int attackPower,int maximumMagic,int energyPoint,int inventorySize,double healthRefillRate, double magicRefillRate){
        super(maximumHealth,attackPower,healthRefillRate, maximumHealth, className);
        this.setCurrentEnergyPoint(this.getMaximumEnergyPoint());
        this.setCurrentMagic(this.getMaximumMagic());
        this.setMaximumMagic(maximumMagic);
        this.setMaximumEnergyPoint(energyPoint);
        this.setInventorySize(inventorySize);
    }
    //---------------------------------------------------------------- Functions
    public void addSkill(Skill skill){
        this.skills.add(skill);
    }
    public void addPerk(Perk perk){
        this.perks.add(perk);
    }
    
    //---------------------------------------------------------------- Getter && Setters


    public ArrayList<Perk> getPerks() {
        return perks;
    }

    public void setPerks(ArrayList<Perk> perks) {
        this.perks = perks;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public double getCriticalHitChance() {
        return criticalHitChance;
    }

    public void setCriticalHitChance(double criticalHitChance) {
        this.criticalHitChance = criticalHitChance;
    }

    public double getCriticalHitChanceRatio() {
        return criticalHitChanceRatio;
    }

    public void setCriticalHitChanceRatio(double criticalHitChanceRatio) {
        this.criticalHitChanceRatio = criticalHitChanceRatio;
    }

    public double getCriticalHitDamage() {
        return criticalHitDamage;
    }

    public void setCriticalHitDamage(double criticalHitDamage) {
        this.criticalHitDamage = criticalHitDamage;
    }

    public double getCriticalHitDamageRatio() {
        return criticalHitDamageRatio;
    }

    public void setCriticalHitDamageRatio(double criticalHitDamageRatio) {
        this.criticalHitDamageRatio = criticalHitDamageRatio;
    }

    public double getMagicRefillRate() {
        return magicRefillRate;
    }

    public void setMagicRefillRate(double magicRefillRate) {
        this.magicRefillRate = magicRefillRate;
    }

    public double getAttackPowerRatioDuringAttack() {
        return attackPowerRatioDuringAttack;
    }

    public void setAttackPowerRatioDuringAttack(double attackPowerRatioDuringAttack) {
        this.attackPowerRatioDuringAttack = attackPowerRatioDuringAttack;
    }

    public double getHealthRefillRateRatio() {
        return healthRefillRateRatio;
    }

    public void setHealthRefillRateRatio(double healthRefillRateRatio) {
        this.healthRefillRateRatio = healthRefillRateRatio;
    }

    public double getMagicRefillRateRatio() {
        return magicRefillRateRatio;
    }

    public void setMagicRefillRateRatio(double magicRefillRateRatio) {
        this.magicRefillRateRatio = magicRefillRateRatio;
    }

    public double getEnergyPointRatio() {
        return energyPointRatio;
    }

    public void setEnergyPointRatio(double energyPointRatio) {
        this.energyPointRatio = energyPointRatio;
    }

    public double getAttackPowerRatioOnNonTargetedEnemy() {
        return attackPowerRatioOnNonTargetedEnemy;
    }

    public void setAttackPowerRatioOnNonTargetedEnemy(double attackPowerRatioOnNonTargetedEnemy) {
        this.attackPowerRatioOnNonTargetedEnemy = attackPowerRatioOnNonTargetedEnemy;
    }

    public double getAttackPowerOnNonTargetedEnemy() {
        return attackPowerOnNonTargetedEnemy;
    }

    public void setAttackPowerOnNonTargetedEnemy(double attackPowerOnNonTargetedEnemy) {
        this.attackPowerOnNonTargetedEnemy = attackPowerOnNonTargetedEnemy;
    }

    public int getMaximumMagic() {
        return maximumMagic;
    }

    public void setMaximumMagic(int maximumMagic) {
        this.maximumMagic = maximumMagic;
    }

    public int getCurrentMagic() {
        return currentMagic;
    }

    public void setCurrentMagic(int currentMagic) {
        this.currentMagic = currentMagic;
    }

    public int getNumberOfNonTargetedEnemyEffected() {
        return numberOfNonTargetedEnemyEffected;
    }

    public void setNumberOfNonTargetedEnemyEffected(int numberOfNonTargetedEnemyEffected) {
        this.numberOfNonTargetedEnemyEffected = numberOfNonTargetedEnemyEffected;
    }

    public int getMaximumEnergyPoint() {
        return maximumEnergyPoint;
    }

    public void setMaximumEnergyPoint(int maximumEnergyPoint) {
        this.maximumEnergyPoint = maximumEnergyPoint;
    }

    public int getCurrentEnergyPoint() {
        return currentEnergyPoint;
    }

    public void setCurrentEnergyPoint(int currentEnergyPoint) {
        this.currentEnergyPoint = currentEnergyPoint;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public void setInventorySize(int inventorySize) {
        this.inventorySize = inventorySize;
    }


}
