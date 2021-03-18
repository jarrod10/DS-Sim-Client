package Protocol;

public enum State {
    DEFAULT,
    HANDSHAKING,
    AUTHENTICATING,
    XML,
    EVENT_HANDLING,
    QUITTING
}
