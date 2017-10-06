package be.deschutter.scimitar.planetarion;

import be.deschutter.scimitar.Formatter;
import be.deschutter.scimitar.Listener;
import be.deschutter.scimitar.config.PaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@Component
public class RoidcostListener implements Listener {

    @Autowired
    private PaConfig paConfig;
    private static final NumberFormat FORMATTER = new DecimalFormat("#0");

    @Override
    public String getCommand() {
        return "roidcost";
    }

    @Override
    public String getPattern() {
        return "roids value_cost <mining_bonus>";
    }

    @Override
    public String getResult(final String... parameters) {
        if (parameters != null && parameters.length == 3) {
            try {

                return bepaalRoidCost(Formatter.deFormat(parameters[0]),
                    Formatter.deFormat(parameters[1]),
                    Double.parseDouble(parameters[2]));

            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else if (parameters != null && parameters.length == 2) {
            try {

                return bepaalRoidCost(Formatter.deFormat(parameters[0]),
                    Formatter.deFormat(parameters[1]), 0d);

            } catch (NumberFormatException e) {
                return getErrorMessage();
            }
        } else {
            return getErrorMessage();
        }
    }

    private String bepaalRoidCost(final Long roids, final Long valueCost,
        final Double miningBonus) {
        final Double ticksToMakeUpLostValue = calculateTicks(roids, valueCost,
            miningBonus);

        StringBuilder b = new StringBuilder();
        paConfig.getGovernments().stream()
            .filter(government -> government.getProductionCostBonus() != 0)
            .forEach(government -> {
                final Double ticks = ticksToMakeUpLostValue * (1 + government
                    .getProductionCostBonus());

                b.append(String
                    .format(" | %s: %s ticks (%s days)", government.getCode(),
                        FORMATTER.format(Math.ceil(ticks)),
                        FORMATTER.format(Math.ceil(ticks / 24))));
            });
        return String.format(
            "Capping %s roids at %s value with %s%% bonus will repay in %s ticks (%s days)%s",
            roids, Formatter.format(valueCost), FORMATTER.format(miningBonus),
            FORMATTER.format(Math.ceil(ticksToMakeUpLostValue)),
            FORMATTER.format(Math.ceil(ticksToMakeUpLostValue / 24)),
            b.toString());
    }

    private Double calculateTicks(final Long roids, final Long valueCost,
        final Double miningBonus) {
        final Double miningPerRoid = paConfig.getMiningPerRoid();
        final Double valuePerResource =
            paConfig.getValuePerResource();

        final Double extraResourcesPerTick =
            (roids * miningPerRoid) + ((roids * miningPerRoid) * (miningBonus
                / 100));
        final Double extraValuePerTick =
            extraResourcesPerTick * valuePerResource;
        return valueCost / extraValuePerTick;
    }

}
