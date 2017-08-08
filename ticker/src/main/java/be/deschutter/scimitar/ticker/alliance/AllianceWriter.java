package be.deschutter.scimitar.ticker.alliance;

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
public class AllianceWriter implements ItemWriter<AllianceStaging> {
    private final AllianceStagingEao allianceStagingEao;
    private JdbcTemplate jdbcTemplate;

    @Value("#{jobParameters['tick']}")
    private long tick;
    private TickerInfoEao tickerInfoEao;

    @Autowired
    public AllianceWriter(AllianceStagingEao allianceStagingEao, JdbcTemplate jdbcTemplate, TickerInfoEao tickerInfoEao) {
        this.allianceStagingEao = allianceStagingEao;
        this.jdbcTemplate = jdbcTemplate;
        this.tickerInfoEao = tickerInfoEao;
    }

    @Override
    public void write(final List<? extends AllianceStaging> list)
            throws Exception {
        allianceStagingEao.save(list);
        allianceStagingEao.flush();

        jdbcTemplate.execute("INSERT INTO alliance (alliance_name, tick,counted_score,members,points,counted_score_rank,size,total_score,total_value,score_rank,value_rank,size_rank,points_rank) SELECT alliance_name, tick,counted_score,members,points,counted_score_rank,size,total_score,total_value,score_rank,value_rank,size_rank,points_rank from (" +
                "  SELECT" +
                "    alliance_name, tick,counted_score,members,points,rank as counted_score_rank,size,total_score,total_value," +
                "    rank()" +
                "    OVER (ORDER BY total_score DESC ) as score_rank," +
                "    rank()" +
                "    OVER (ORDER BY total_value DESC ) as value_rank," +
                "    rank()" +
                "    OVER (ORDER BY size DESC ) as size_rank," +
                "    rank()" +
                "    OVER (ORDER BY points DESC ) as points_rank" +
                "  FROM alliance_staging" +
                ") as alliance_rank");


        allianceStagingEao.deleteAll();
    }
}
