package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.scans.ScanType;
import org.springframework.stereotype.Component;

@Component
public class JgpListener extends ShowScanListener {
    @Override
    public String getCommand() {
        return "jgp";
    }

    @Override
    public ScanType getScanType() {
        return ScanType.J;
    }
}
