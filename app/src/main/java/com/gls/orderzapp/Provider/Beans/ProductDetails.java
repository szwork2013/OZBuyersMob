package com.gls.orderzapp.Provider.Beans;

import com.gls.orderzapp.CreateOrder.CreateOrderBeans.AvailableDeliveryTimingSlots;
import com.gls.orderzapp.SignUp.Location;

import java.util.List;

/**
 * Created by prajyot on 4/4/14.
 */
public class ProductDetails {
    String productid;
    String productname;
    String providerName;
    String providerid;
    String branchid;
    String foodtype;
    Min_Max_Weight max_weight = new Min_Max_Weight();
    Min_Max_Weight min_weight = new Min_Max_Weight();
    ProductConfiguration productconfiguration = new ProductConfiguration();
    String cartCount;
    String productimage;
    String messageonproduct;
    String prefereddeliverydate;
    AvailableDeliveryTimingSlots timeslot = new AvailableDeliveryTimingSlots();
    ProductLogo productlogo = new ProductLogo();
    String quantity;
    String productdescription;
    ProductPrice price = new ProductPrice();
    String orignalUom;
    String note;
    List<String> contact_supports;
    Location location = new Location();
    ProductDiscount discount = new ProductDiscount();
    DeliveryMode delivery = new DeliveryMode();
    PaymentMode paymentmode = new PaymentMode();
    DeliveryType deliveryType = new DeliveryType();

    public List<String> getContact_supports() {
        return contact_supports;
    }

    public void setContact_supports(List<String> contact_supports) {
        this.contact_supports = contact_supports;
    }

    public String getOrignalUom() {
        return orignalUom;
    }

    public void setOrignalUom(String orignalUom) {
        this.orignalUom = orignalUom;
    }

    public AvailableDeliveryTimingSlots getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(AvailableDeliveryTimingSlots timeslot) {
        this.timeslot = timeslot;
    }

    public String getPrefereddeliverydate() {
        return prefereddeliverydate;
    }

    public void setPrefereddeliverydate(String prefereddeliverydate) {
        this.prefereddeliverydate = prefereddeliverydate;
    }


    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public PaymentMode getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(PaymentMode paymentmode) {
        this.paymentmode = paymentmode;
    }

    public DeliveryMode getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryMode delivery) {
        this.delivery = delivery;
    }

    public ProductDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(ProductDiscount discount) {
        this.discount = discount;
    }

    public String getCartCount() {
        return cartCount;
    }

    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }

    public String getProviderid() {
        return providerid;
    }

    public void setProviderid(String providerid) {
        this.providerid = providerid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ProductConfiguration getProductconfiguration() {
        return productconfiguration;
    }

    public void setProductconfiguration(ProductConfiguration productconfiguration) {
        this.productconfiguration = productconfiguration;
    }

    public Min_Max_Weight getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(Min_Max_Weight max_weight) {
        this.max_weight = max_weight;
    }

    public Min_Max_Weight getMin_weight() {
        return min_weight;
    }

    public void setMin_weight(Min_Max_Weight min_weight) {
        this.min_weight = min_weight;
    }


    public String getMessageonproduct() {
        return messageonproduct;
    }

    public void setMessageonproduct(String messageonproduct) {
        this.messageonproduct = messageonproduct;
    }

//    public String getWizard() {
//        return wizard;
//    }
//
//    public void setWizard(String wizard) {
//        this.wizard = wizard;
//    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getFoodtype() {
        return foodtype;
    }

    public void setFoodtype(String foodtype) {
        this.foodtype = foodtype;
    }

    public ProductPrice getPrice() {
        return price;
    }

    public void setPrice(ProductPrice price) {
        this.price = price;
    }

    public String getProductdescription() {
        return productdescription;
    }

    public void setProductdescription(String productdescription) {
        this.productdescription = productdescription;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public ProductLogo getProductlogo() {
        return productlogo;
    }

    public void setProductlogo(ProductLogo productlogo) {
        this.productlogo = productlogo;
    }

//    public ProductPrice getPrice() {
//        return price;
//    }
//
//    public void setPrice(ProductPrice price) {
//        this.price = price;
//    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }


    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

//    public String getPrice() {
//        return price;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
}