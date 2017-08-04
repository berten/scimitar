package be.deschutter.planetarion.katana;

@Eventje(event = "hello")
public class KatanaHelloWorldListenerImpl implements KatanaListener {


    @Override
    public String handle(KatanaEvent event) {
        return "Reading you loud an clear " + event.getIrcUserName();
    }

}