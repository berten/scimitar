package be.deschutter.scimitar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "pa")
public class PaConfig {
    @NestedConfigurationProperty
    private List<Race> races = new ArrayList<>();
    @NestedConfigurationProperty
    private List<Government> governments = new ArrayList<>();
    private Double miningPerRoid;
    private Double maxCap;
    private Double minCap;
    private Double warBonus;
    private Double valuePerResource;
    private String scanurl;
    private Integer protection;

    public List<Race> getRaces() {
        return races;
    }

    public List<Government> getGovernments() {
        return governments;
    }

    @PostConstruct
    public void stuffz() {
        races.forEach(System.out::println);
        governments.forEach(System.out::println);
        System.out.println("Mining per roid: " + miningPerRoid);
        System.out.println("Maxcap: " + maxCap);
        System.out.println("Warbonus: " + warBonus);
        System.out.println("value per resource: " + valuePerResource);
        System.out.println("Scanurl: " + scanurl);
    }

    public Double getMiningPerRoid() {
        return miningPerRoid;
    }

    public void setMiningPerRoid(final Double miningPerRoid) {
        this.miningPerRoid = miningPerRoid;
    }

    public Double getValuePerResource() {
        return valuePerResource;
    }

    public void setValuePerResource(final Double valuePerResource) {
        this.valuePerResource = valuePerResource;
    }

    public Double getWarBonus() {
        return warBonus;
    }

    public void setWarBonus(final Double warBonus) {
        this.warBonus = warBonus;
    }

    public Double getMaxCap() {
        return maxCap;
    }

    public void setMaxCap(final Double maxCap) {
        this.maxCap = maxCap;
    }

    public String getScanurl() {
        return scanurl;
    }

    public void setScanurl(final String scanurl) {
        this.scanurl = scanurl;
    }

    public Double getMinCap() {
        return minCap;
    }

    public void setMinCap(final Double minCap) {
        this.minCap = minCap;
    }

    public Integer getProtection() {
        return protection;
    }

    public void setProtection(final Integer protection) {
        this.protection = protection;
    }
}
