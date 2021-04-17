package DSSimProtocol;

public enum ProtocolState {
    DEFAULT,
    HANDSHAKING,
    AUTHENTICATING,
    EVENT_LOOP,
    QUITTING
}
