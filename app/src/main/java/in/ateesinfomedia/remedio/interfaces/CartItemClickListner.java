package in.ateesinfomedia.remedio.interfaces;

public interface CartItemClickListner {

    public void onRemoveItemClicked(int position);
    public void onEditQuantityClicked(int position, String type, int value);


}
