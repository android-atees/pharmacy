package in.ateesinfomedia.relief.interfaces;

/*
 *Created by Adithya T Raj on 11-11-2020
 */

import in.ateesinfomedia.relief.models.orders.OrdersModel;

public interface OrderClickListener {

    public void onTrackClicked(int position, OrdersModel item);

}
