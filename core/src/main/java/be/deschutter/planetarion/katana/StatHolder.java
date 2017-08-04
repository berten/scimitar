package be.deschutter.planetarion.katana;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URL;

//@Component
public class StatHolder {

    private Stats stats;

    @PostConstruct
    public void loadStats() throws JAXBException, IOException {

        JAXBContext jc = JAXBContext.newInstance(Stats.class);
        Unmarshaller u = jc.createUnmarshaller();


        stats = (Stats) u.unmarshal(
            new URL("http://game.planetarion.com/manual.pl?action=statsxml"));

    }

    public Stats getStats() {
        return stats;
    }
}
