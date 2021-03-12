package Protocol;

public class Action {

    public Intent intent;
    public State state; // Only set id action is set to ProtocolHandler.Action.SWITCH_STATE
    public String message; // Only set if action is set to ProtocolHandler.Action.SEND_MESSAGE

    public Action() {
    }

    public Action(Intent intent, String message) {
        this.intent = intent;
        this.message = message;
    }

    public Action(Intent intent, State state) {
        this.intent = intent;
        this.state = state;
    }

}
