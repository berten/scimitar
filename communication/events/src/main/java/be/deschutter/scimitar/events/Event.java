package be.deschutter.scimitar.events;

public class Event {
    private String currentUsername;
    private String loggedInUsername;
    private String command;
    private String[] parameters;
    private String channel;
    private ReturnType returnType;
    private String reply;

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }

    public String getCommand() {
        return command;
    }

    public String[] getParameters() {
        return parameters;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public void setReturnType(ReturnType returnType) {
        this.returnType = returnType;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
