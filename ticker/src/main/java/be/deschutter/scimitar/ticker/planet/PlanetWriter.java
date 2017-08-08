package be.deschutter.scimitar.ticker.planet;

import be.deschutter.scimitar.TickerInfo;
import be.deschutter.scimitar.TickerInfoEao;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class PlanetWriter implements ItemWriter<PlanetStaging> {
    private final PlanetStagingEao planetStagingEao;
    private final JdbcTemplate jdbcTemplate;
    private TickerInfoEao tickerInfoEao;


    @Value("#{jobParameters['tick']}")
    private long tick;

    @Autowired
    public PlanetWriter(PlanetStagingEao planetStagingEao, JdbcTemplate jdbcTemplate, TickerInfoEao tickerInfoEao) {
        this.planetStagingEao = planetStagingEao;
        this.jdbcTemplate = jdbcTemplate;
        this.tickerInfoEao = tickerInfoEao;
    }

    @Override
    public void write(final List<? extends PlanetStaging> list)
            throws Exception {
        planetStagingEao.save(list);
        planetStagingEao.flush();


        jdbcTemplate.execute("INSERT into planet (id,tick,planet_name,race,ruler_name,score,size,special,value,x,xp,y,z,score_rank,value_rank,size_rank) SELECT id,tick,planet_name,race,ruler_name,score,size,special,value,x,xp,y,z,score_rank,value_rank,size_rank from (" +
                "  SELECT" +
                "    *," +
                "    rank()" +
                "    OVER (ORDER BY score DESC ) as score_rank," +
                "    rank()" +
                "    OVER (ORDER BY value DESC )as value_rank," +
                "    rank()" +
                "    OVER (ORDER BY size DESC )as size_rank" +
                "  FROM planet_staging" +
                ") as planet_rank");




        planetStagingEao.deleteAll();

    }
}
