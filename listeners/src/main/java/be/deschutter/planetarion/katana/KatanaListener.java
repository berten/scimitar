package be.deschutter.planetarion.katana;

import org.springframework.scheduling.annotation.Async;

public interface KatanaListener {

    @Async
    public String handle(KatanaEvent message);


    default boolean canHandle(KatanaEvent event) {
        if (getClass().isAnnotationPresent(Eventje.class)) {
            Eventje eventje = getClass().getAnnotation(Eventje.class);
            return event.getCommand().startsWith(eventje.event());
        } else
            throw new IllegalStateException("should have been annoted");
    }

}
