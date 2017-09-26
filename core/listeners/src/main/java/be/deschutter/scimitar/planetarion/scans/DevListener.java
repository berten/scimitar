package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.scans.ScanType;
import org.springframework.stereotype.Component;

@Component
public class DevListener extends ShowScanListener {
    @Override
    public String getCommand() {
        return "dev";
    }

    @Override
    public ScanType getScanType() {
        return ScanType.D;
    }
}
