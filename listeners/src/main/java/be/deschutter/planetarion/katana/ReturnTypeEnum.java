package be.deschutter.planetarion.katana;

public enum ReturnTypeEnum {
    CHANNEL_MSG(), PRIVATE_MSG(), NOTICE();

    ReturnTypeEnum() {

    }

    public static ReturnTypeEnum findByPrefix(String prefix) {
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
