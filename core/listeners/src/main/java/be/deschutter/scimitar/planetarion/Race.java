package be.deschutter.scimitar.planetarion;

public class Race {

    private String name;
    private String code;
    private Double productionBonus;
    private Integer stealthGrowth;
    private Double salvageBonus = 0d;
    private Integer constructionBase;
    private Integer researchBase;
    private Integer maxStealth;
    private Double universeTradeTax = 0.25;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public Double getProductionBonus() {
        return productionBonus;
    }

    public void setProductionBonus(final Double productionBonus) {
        this.productionBonus = productionBonus;
    }

    public Integer getStealthGrowth() {
        return stealthGrowth;
    }

    public void setStealthGrowth(final Integer stealthGrowth) {
        this.stealthGrowth = stealthGrowth;
    }

    public Double getSalvageBonus() {
        return salvageBonus;
    }

    public void setSalvageBonus(final Double salvageBonus) {
        this.salvageBonus = salvageBonus;
    }

    public Integer getConstructionBase() {
        return constructionBase;
    }

    public void setConstructionBase(final Integer constructionBase) {
        this.constructionBase = constructionBase;
    }

    public Integer getResearchBase() {
        return researchBase;
    }

    public void setResearchBase(final Integer researchBase) {
        this.researchBase = researchBase;
    }

    public Integer getMaxStealth() {
        return maxStealth;
    }

    public void setMaxStealth(final Integer maxStealth) {
        this.maxStealth = maxStealth;
    }

    public Double getUniverseTradeTax() {
        return universeTradeTax;
    }

    public void setUniverseTradeTax(final Double universeTradeTax) {
        this.universeTradeTax = universeTradeTax;
    }

    @Override
    public String toString() {
        return "Race{" + "name='" + name + '\'' + ", code='" + code + '\''
            + ", productionBonus=" + productionBonus + ", stealthGrowth="
            + stealthGrowth + ", salvageBonus=" + salvageBonus
            + ", constructionBase=" + constructionBase + ", researchBase="
            + researchBase + ", maxStealth=" + maxStealth
            + ", universeTradeTax=" + universeTradeTax + '}';
    }
}
