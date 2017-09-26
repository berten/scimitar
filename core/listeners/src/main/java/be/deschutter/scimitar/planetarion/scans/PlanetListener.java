package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.scans.ScanType;
import org.springframework.stereotype.Component;

@Component
public class PlanetListener extends ShowScanListener {
    @Override
    public String getCommand() {
        return "planet";
    }

    @Override
    public ScanType getScanType() {
        return ScanType.P;
    }
}
