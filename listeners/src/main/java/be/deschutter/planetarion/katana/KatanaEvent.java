package be.deschutter.planetarion.katana;

class KatanaEvent {
    private String ircUserName;
    private String loggedInUsername;
    private ReturnTypeEnum returnType;
    private String command;
    private String parameters;
    private String channel;


    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }

    public ReturnTypeEnum getReturnType() {
        return returnType;
    }

    public String getCommand() {
        return command;
    }

    public String getParameters() {
        return parameters;
    }

    public String getIrcUserName() {
        return ircUserName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    public void setIrcUserName(String ircUserName) {
        this.ircUserName = ircUserName;
    }

    public void setReturnType(ReturnTypeEnum returnType) {
        this.returnType = returnType;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
}
