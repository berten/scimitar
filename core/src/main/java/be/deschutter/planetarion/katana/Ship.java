package be.deschutter.planetarion.katana;

import javax.xml.bind.annotation.XmlElement;

public class Ship {

    private String name;
    private String race;
    //@XmlElement(name = "class")
    private String clazz;
    private String target1;
    private String target2;
    private String target3;
    private String type;
    private int initiative;
    private int guns;
    private int armor;
    private int damage;
    private int empres;
    private int metal;
    private int crystal;
    private int eonium;
    private int armorcost;
    private int damagecost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }


    public String getTarget1() {
        return target1;
    }

    public void setTarget1(String target1) {
        this.target1 = target1;
    }

    public String getTarget2() {
        return target2;
    }

    public void setTarget2(String target2) {
        this.target2 = target2;
    }

    public String getTarget3() {
        return target3;
    }

    public void setTarget3(String target3) {
        this.target3 = target3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getGuns() {
        return guns;
    }

    public void setGuns(int guns) {
        this.guns = guns;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getEmpres() {
        return empres;
    }

    public void setEmpres(int empres) {
        this.empres = empres;
    }

    public int getMetal() {
        return metal;
    }

    public void setMetal(int metal) {
        this.metal = metal;
    }

    public int getCrystal() {
        return crystal;
    }

    public void setCrystal(int crystal) {
        this.crystal = crystal;
    }

    public int getEonium() {
        return eonium;
    }

    public void setEonium(int eonium) {
        this.eonium = eonium;
    }

    public int getArmorcost() {
        return armorcost;
    }

    public void setArmorcost(int armorcost) {
        this.armorcost = armorcost;
    }

    public int getDamagecost() {
        return damagecost;
    }

    public void setDamagecost(int damagecost) {
        this.damagecost = damagecost;
    }


    public String getClazz() {
        return clazz;
    }

    public void setClazz(final String clazz) {
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "Ship: " + name + "(" + race + ")" +
                " | Class: " + clazz +
                " | T1: " + target1 +
                (target2.equals("-") ? "" : " | T2: " + target2) +
                (target3.equals("-") ? "" : " | T3: " + target3) +
                " | type: " + type +
                " | Init: " + initiative +
                " | EMPres: " + empres +
                " | A/C: " + armorcost +
                " | D/C: " + damagecost;
    }
}
