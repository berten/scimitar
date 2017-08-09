package be.deschutter.scimitar.events;

public enum ReturnType {
    CHANNEL_MSG(), PRIVATE_MSG(), NOTICE();

    ReturnType() {

    }

    public static ReturnType findByPrefix(String prefix) {
        switch (prefix) {
            case "!":
                return CHANNEL_MSG;
            case ".":
                return PRIVATE_MSG;
            case "-":
                return NOTICE;
            default:
                throw new UnsupportedOperationException();

        }
    }


}
