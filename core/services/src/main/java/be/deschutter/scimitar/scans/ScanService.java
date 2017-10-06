package be.deschutter.scimitar.scans;

import java.util.List;

public interface ScanService {
    Scan findLastScanFor(int x, int y, int z, ScanType scanType);

    List<Scan> findLast5Scans(int x, int y, int z);
}
