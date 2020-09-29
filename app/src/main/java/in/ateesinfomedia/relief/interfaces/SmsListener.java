package in.ateesinfomedia.relief.interfaces;

public interface SmsListener {
    public void messageReceived(String messageText, String sender);
}
