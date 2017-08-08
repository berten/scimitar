package be.deschutter.scimitar.ticker.galaxy;

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
public class GalaxyWriter implements ItemWriter<GalaxyStaging> {
    private final GalaxyStagingEao galaxyStagingEao;
    private JdbcTemplate jdbcTemplate;

    @Value("#{jobParameters['tick']}")
    private long tick;

    @Autowired
    public GalaxyWriter(GalaxyStagingEao galaxyStagingEao, JdbcTemplate jdbcTemplate, TickerInfoEao tickerInfoEao) {
        this.galaxyStagingEao = galaxyStagingEao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(final List<? extends GalaxyStaging> list)
            throws Exception {
        galaxyStagingEao.save(list);
        galaxyStagingEao.flush();

        jdbcTemplate.execute("INSERT into galaxy (tick,galaxy_name,score,size,value,x,xp,y,score_rank,value_rank,size_rank,xp_rank) SELECT tick,galaxy_name,score,size,value,x,xp,y,score_rank,value_rank,size_rank,xp_rank from (" +
                "  SELECT" +
                "    *," +
                "    rank()" +
                "    OVER (ORDER BY score DESC ) as score_rank," +
                "    rank()" +
                "    OVER (ORDER BY value DESC )as value_rank," +
                "    rank()" +
                "    OVER (ORDER BY size DESC )as size_rank," +
                "    rank()" +
                "    OVER (ORDER BY xp DESC )as xp_rank" +
                "  FROM galaxy_staging" +
                ") as galaxy_rank");

        jdbcTemplate.execute("update galaxy set planets=(select count(*) from planet where planet.x=galaxy.x and planet.y=galaxy.y and planet.tick=galaxy.tick) where tick=" + tick);


        galaxyStagingEao.deleteAll();

    }
}
