package in.ateesinfomedia.relief.interfaces;

/*
 *Created by Adithya T Raj on 07-11-2020
 */

import in.ateesinfomedia.relief.models.login.CustomerAddress;

public interface AddressClickListner {

    public void onAddressItemClicked(int position, CustomerAddress address);
    public void onAddressEditClicked(int position, CustomerAddress address);
    public void onAddressDeleteClicked(int position, CustomerAddress address);

}
