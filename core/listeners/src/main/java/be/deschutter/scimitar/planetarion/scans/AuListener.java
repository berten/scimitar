package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.scans.ScanType;
import org.springframework.stereotype.Component;

@Component
public class AuListener extends ShowScanListener {
    @Override
    public String getCommand() {
        return "au";
    }

    @Override
    public ScanType getScanType() {
        return ScanType.A;
    }
}
