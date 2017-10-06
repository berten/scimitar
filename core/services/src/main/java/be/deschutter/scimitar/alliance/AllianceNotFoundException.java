package be.deschutter.scimitar.alliance;

public class AllianceNotFoundException extends RuntimeException {
    private String allianceName;

    public AllianceNotFoundException(final String allianceName) {
        super(String.format("Could not find alliance %s", allianceName));
        this.allianceName = allianceName;

    }

    public String getAllianceName() {
        return allianceName;
    }
}
