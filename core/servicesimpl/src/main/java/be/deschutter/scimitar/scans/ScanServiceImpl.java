package be.deschutter.scimitar.scans;

import be.deschutter.scimitar.planet.Planet;
import be.deschutter.scimitar.planet.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScanServiceImpl implements ScanService {
    private final ScanEao scanEao;
    private final PlanetService planetService;

    @Autowired
    public ScanServiceImpl(final ScanEao scanEao,
        final PlanetService planetService) {
        this.scanEao = scanEao;
        this.planetService = planetService;
    }

    @Override
    public Scan findLastScanFor(final int x, final int y, final int z,
        final ScanType scanType) {
        final Planet planet = planetService.findBy(x, y, z);
        final Scan scan = scanEao
            .findFirstByPlanetIdAndScanTypeOrderByTickDesc(planet.getId(),
                scanType);
        if (scan == null)
            throw new ScanNotFoundException(x, y, z, scanType);
        return scan;
    }

    @Override
    public List<Scan> findLast5Scans(final int x, final int y, final int z) {
        final Planet planet = planetService.findBy(x, y, z);
        final List<Scan> scans = scanEao
            .findFirst5ByPlanetIdOrderByTickDesc(planet.getId());
        if(scans == null || scans.isEmpty()) throw new NoScansFoundException(x,y,z);
        return scans;
    }
}
