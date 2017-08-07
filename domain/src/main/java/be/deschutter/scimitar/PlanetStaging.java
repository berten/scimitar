package be.deschutter.scimitar;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PlanetStaging {
@Id
    private String id;
    private int x;
    private int y;
    private int z;
    private String planetName;
    private String rulerName;
    private String race;
    private int size;
    private long score;
    private long value;
    private long xp;
    private String special;
}
