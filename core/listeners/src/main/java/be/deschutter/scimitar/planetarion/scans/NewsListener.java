package be.deschutter.scimitar.planetarion.scans;

import be.deschutter.scimitar.scans.ScanType;
import org.springframework.stereotype.Component;

@Component
public class NewsListener extends ShowScanListener {
    @Override
    public String getCommand() {
        return "news";
    }

    @Override
    public ScanType getScanType() {
        return ScanType.N;
    }
}
