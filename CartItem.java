package Jewel;

import java.util.ArrayList;

public class CartItem {
    private String name;
    private double price;
    private int quantity;

    private static ArrayList<CartItem> cartList = new ArrayList<>();

    // Constructor
    public CartItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Accessors
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    // Cart Management Static Methods

    public static ArrayList<CartItem> getCartList() {
        return cartList;
    }

    public static void addToCart(String name, double price) {
        for (CartItem item : cartList) {
            if (item.name.equals(name)) {
                item.incrementQuantity();
                return;
            }
        }
        cartList.add(new CartItem(name, price, 1));
    }

    public static void removeFromCart(String name) {
        cartList.removeIf(item -> item.name.equals(name));
    }

    public static void incrementQuantity(String name) {
        for (CartItem item : cartList) {
            if (item.name.equals(name)) {
                item.incrementQuantity();
                return;
            }
        }
    }

    public static void decrementQuantity(String name) {
        for (CartItem item : cartList) {
            if (item.name.equals(name)) {
                item.decrementQuantity();
                return;
            }
        }
    }

    public static void clearCart() {
        cartList.clear();
    }

    public static double getCartTotalPrice() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.getTotalPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return getName() + " - ₹" + getPrice() + " x " + getQuantity() + " = ₹" + getTotalPrice();
    }

    // Instance Methods
    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        } else {
            // Optional: remove item from cart if quantity becomes zero
            CartItem.removeFromCart(this.name);
        }
    }

//    public void decrementQuantity() {
//        this.quantity--;
//    }
}
