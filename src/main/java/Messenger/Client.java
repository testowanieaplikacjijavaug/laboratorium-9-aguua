package Messenger;

public interface Client {
    String getEmail();
    void receive(Message message);
}
