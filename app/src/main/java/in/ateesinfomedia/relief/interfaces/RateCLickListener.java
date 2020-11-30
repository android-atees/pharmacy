package in.ateesinfomedia.relief.interfaces;

/*
 *Created by Adithya T Raj on 10-11-2020
 */

import in.ateesinfomedia.relief.models.shipping.ShippingCost;

public interface RateCLickListener {

    public void itemClicked(int position, ShippingCost shipCost);

}
