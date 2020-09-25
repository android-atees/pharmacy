package in.ateesinfomedia.remedio.interfaces;

public interface SmsListener {
    public void messageReceived(String messageText, String sender);
}
