package be.deschutter.scimitar.planetarion;

public class Government {
    private String name;
    private String code;
    private Double productionTimeBonus;
    private Double alertBonus;
    private Double stealthBonus;
    private Double researchBonus;
    private Double productionCostBonus;
    private Double miningBonus;
    private Double constructionBonus;

    @Override
    public String toString() {
        return "Government{" + "name='" + name + '\'' + ", code='" + code + '\''
            + ", productionTimeBonus=" + productionTimeBonus + ", alertBonus="
            + alertBonus + ", stealthBonus=" + stealthBonus + ", researchBonus="
            + researchBonus + ", productionCostBonus=" + productionCostBonus
            + ", miningBonus=" + miningBonus + ", constructionBonus="
            + constructionBonus + '}';
    }

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

    public Double getProductionTimeBonus() {
        return productionTimeBonus;
    }

    public void setProductionTimeBonus(final Double productionTimeBonus) {
        this.productionTimeBonus = productionTimeBonus;
    }

    public Double getAlertBonus() {
        return alertBonus;
    }

    public void setAlertBonus(final Double alertBonus) {
        this.alertBonus = alertBonus;
    }

    public Double getStealthBonus() {
        return stealthBonus;
    }

    public void setStealthBonus(final Double stealthBonus) {
        this.stealthBonus = stealthBonus;
    }

    public Double getResearchBonus() {
        return researchBonus;
    }

    public void setResearchBonus(final Double researchBonus) {
        this.researchBonus = researchBonus;
    }

    public Double getProductionCostBonus() {
        return productionCostBonus;
    }

    public void setProductionCostBonus(final Double productionCostBonus) {
        this.productionCostBonus = productionCostBonus;
    }

    public Double getMiningBonus() {
        return miningBonus;
    }

    public void setMiningBonus(final Double miningBonus) {
        this.miningBonus = miningBonus;
    }

    public Double getConstructionBonus() {
        return constructionBonus;
    }

    public void setConstructionBonus(final Double constructionBonus) {
        this.constructionBonus = constructionBonus;
    }
}
