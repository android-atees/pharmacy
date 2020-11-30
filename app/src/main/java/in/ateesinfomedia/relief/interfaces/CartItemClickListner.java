package in.ateesinfomedia.relief.interfaces;

import in.ateesinfomedia.relief.models.cart.CartModel;

public interface CartItemClickListner {

    public void onRemoveItemClicked(int position, CartModel item);
    public void onEditQuantityClicked(int position, String type, int value, CartModel item);


}
