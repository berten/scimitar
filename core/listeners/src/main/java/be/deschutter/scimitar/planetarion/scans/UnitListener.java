package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.scans.ScanType;
import org.springframework.stereotype.Component;

@Component
public class UnitListener extends ShowScanListener {
    @Override
    public String getCommand() {
        return "unit";
    }

    @Override
    public ScanType getScanType() {
        return ScanType.U;
    }
}
