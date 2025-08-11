package Jewel;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.sql.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JewelryApp {

    private JFrame frame;
    private JTextField nameField, priceField, typeField, designField;
    private JTextArea descriptionArea;
    private JButton addButton, uploadButton, viewButton;
    private JLabel photoLabel;
    private JFileChooser fileChooser;
    private File selectedImageFile;
    private Connection conn;
//    ArrayList<CartItem> cart = new ArrayList<>();



    public void createAndShowGUI() {
        connectToDatabase();

        frame = new JFrame("Silver Treasure - Jewelry Manager");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Set background image
        TransparentBackgroundPanel background = new TransparentBackgroundPanel("images/silver_bg.png", 1f);
        background.setBounds(0, 0, 600, 700);
        frame.setContentPane(background);


        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon("images/logo.png");
            // Change this if using another name or path
            Image img = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("Logo image not found.");
        }

// Create label with icon + text
        JLabel titleLabel;
        if (logoIcon != null) {
            titleLabel = new JLabel("Silver Treasure", logoIcon, JLabel.LEFT);
            titleLabel.setIconTextGap(10); // spacing between icon and text
        } else {
            titleLabel = new JLabel("Silver Treasure");
        }

        titleLabel.setFont(new Font("Serif", Font.BOLD, 38));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(120, 20, 450, 60);
        background.add(titleLabel);

        int y = 100;
        background.add(label("Name:", 70, y));
        nameField = field(220, y);
        background.add(nameField);

        y += 40;
        background.add(label("Price:", 70, y));
        priceField = field(220, y);
        background.add(priceField);

        y += 40;
        background.add(label("Type:", 70, y));
        typeField = field(220, y);
        background.add(typeField);

        y += 40;
        background.add(label("Design:", 70, y));
        designField = field(220, y);
        background.add(designField);

        y += 40;
        background.add(label("Description:", 70, y));
        descriptionArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(descriptionArea);
        scroll.setBounds(220, y, 200, 60);
        background.add(scroll);

        y += 70;
        uploadButton = new JButton("Upload Photo");
        uploadButton.setBounds(220, y, 200, 30);
        uploadButton.setBackground(new Color(0, 100, 120));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFont(new Font("Serif", Font.BOLD, 14));
        background.add(uploadButton);

        y += 30;
        photoLabel = new JLabel("No photo selected");
        photoLabel.setBounds(220, y, 300, 20);
        photoLabel.setForeground(Color.WHITE);
        background.add(photoLabel);

        y += 40;
        addButton = new JButton("Add Jewellery");
        addButton.setBounds(220, y, 200, 30);
        addButton.setBackground(new Color(0, 100, 120));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Serif", Font.BOLD, 14));
        background.add(addButton);


        y += 50;
        viewButton = new JButton("View All Jewellery");
        viewButton.setBounds(220, y, 200, 30);
        viewButton.setBackground(new Color(0, 100, 120));
        viewButton.setForeground(Color.WHITE);
        viewButton.setFont(new Font("Serif", Font.BOLD, 14));
        background.add(viewButton);

        addButton.addActionListener(e -> addJewelry());
        uploadButton.addActionListener(e -> uploadPhoto());
        viewButton.addActionListener(e -> viewJewelry());

        frame.setVisible(true);
    }

    class TransparentBackgroundPanel extends JPanel {
        private Image backgroundImage;
        private float opacity; // from 0.0f (fully transparent) to 1.0f (fully opaque)

        public TransparentBackgroundPanel(String imagePath, float opacity) {
            this.opacity = opacity;
            this.backgroundImage = new ImageIcon(imagePath).getImage();
            setLayout(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }
        }
    }

    private JLabel label(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 110, 25);  // increased size
        label.setFont(new Font("Serif", Font.BOLD, 20));  // bold + bigger font size
        label.setOpaque(true); // Required to make background visible
        label.setBackground(Color.LIGHT_GRAY); // Set desired color
        label.setForeground(new Color(0,100,120));
        return label;
    }

    private JTextField field(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 200, 25);
        return field;
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/jewelry_db";
        String username = "root";
        String password = "Poorvika@21";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addJewelry() {
        String name = nameField.getText();
        String priceStr = priceField.getText();
        String type = typeField.getText();
        String design = designField.getText();
        String description = descriptionArea.getText();

        if (name.isEmpty() || priceStr.isEmpty() || type.isEmpty() || design.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Fill all fields.");
            return;
        }

        double price = Double.parseDouble(priceStr);
        byte[] photo = selectedImageFile != null ? getPhotoBytes(selectedImageFile) : null;

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO silver_jewelry (name, price, type, design, description, photo) VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setString(3, type);
            ps.setString(4, design);
            ps.setString(5, description);
            ps.setBytes(6, photo);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Jewellery added.");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "DB Error: " + e.getMessage());
        }
    }

    public void showCartPage() {
        JFrame cartFrame = new JFrame("Your Cart - Silver Treasure");
        cartFrame.setSize(700, 600);
        cartFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Bar
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton backButton = new JButton("← Back");
        backButton.addActionListener(e -> {
            cartFrame.dispose();
            // You can later add: showJewelryListPage();
        });

        JLabel cartTitle = new JLabel("🛒 Cart", SwingConstants.CENTER);
        cartTitle.setFont(new Font("Serif", Font.BOLD, 22));
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(cartTitle, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Cart Items
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));

        double total = 0;

        for (CartItem item : CartItem.getCartList()) {

            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.add(new JLabel(item.getName() + "          - ₹" + item.getPrice() + " x" + item.getQuantity() + " = ₹" + item.getTotalPrice()),BorderLayout.WEST);
            total += item.getTotalPrice();

            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM silver_jewelry WHERE name = '" + item.getName() + "'");

                while (rs.next()) {
                    byte[] photoBytes = rs.getBytes("photo");
                    JLabel imageLabel = new JLabel();
                    if (photoBytes != null) {
                        ImageIcon icon = new ImageIcon(photoBytes);
                        Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(img));
                    } else {
                        imageLabel.setText("No image");
                        imageLabel.setPreferredSize(new Dimension(170, 170));
                    }

                    itemPanel.add(imageLabel






























                    );
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }

            JPanel actionPanel = new JPanel();
            JButton plusBtn = new JButton("+");
            JButton minusBtn = new JButton("-");
            JButton deleteBtn = new JButton("Delete");

            int y=40;
            int x=10;
            plusBtn.setBounds(x, y, 70, 30);
            actionPanel.add(plusBtn);
            x+=80;
            minusBtn.setBounds(x, y, 70, 30);
            actionPanel.add(minusBtn);
            x+=80;
            deleteBtn.setBounds(x, y, 70, 30);
            actionPanel.add(deleteBtn);

            plusBtn.addActionListener(e -> {
                CartItem.incrementQuantity(item.getName());
                cartFrame.dispose();
                showCartPage();
            });

            minusBtn.addActionListener(e -> {
                if (item.getQuantity() > 1) {
                    CartItem.decrementQuantity(item.getName());
                } else {
                    CartItem.removeFromCart(item.getName());
                }
                cartFrame.dispose();
                showCartPage();
            });

            deleteBtn.addActionListener(e -> {
                CartItem.removeFromCart(item.getName());
                cartFrame.dispose();
                showCartPage();
            });
            itemPanel.add(actionPanel,BorderLayout.EAST);
            cartPanel.add(itemPanel);
        }

        JScrollPane scrollPane = new JScrollPane(cartPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel totalLabel = new JLabel("Total: ₹" + CartItem.getCartTotalPrice());
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JButton checkoutBtn = new JButton("Checkout");
        checkoutBtn.setEnabled(false); // Update when you add checkout logic
        bottomPanel.add(checkoutBtn);

        JButton clearBtn = new JButton("Clear Cart");
        clearBtn.setEnabled(true); // Update when you add checkout logic
        bottomPanel.add(clearBtn, BorderLayout.EAST);

        clearBtn.addActionListener(e -> {
            CartItem.clearCart();
            cartFrame.dispose();
        });

        if(!CartItem.getCartList().isEmpty()){
            checkoutBtn.setEnabled(true);
            cartFrame.dispose();
        }

        checkoutBtn.addActionListener(e -> {
            System.out.println("Checking Out");
            JOptionPane.showMessageDialog(cartFrame, "Checking Out....");
        });

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        cartFrame.setContentPane(mainPanel);
        cartFrame.setVisible(true);
    }

    private void viewJewelry() {
        JFrame viewFrame = new JFrame("All Silver Jewellery");
        viewFrame.setSize(650, 700);
        viewFrame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("← Back");
        backButton.addActionListener(e -> {
            viewFrame.dispose();  // Close view window and go back to main
        });
        topPanel.add(backButton);
        viewFrame.add(topPanel, BorderLayout.NORTH);

        JButton viewCartBtn = new JButton("🛒 Cart");
        viewCartBtn.addActionListener(e -> showCartPage());
        topPanel.add(viewCartBtn);


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM silver_jewelry");

            while (rs.next()) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setMaximumSize(new Dimension(600, 180));

                // Load image
                byte[] photoBytes = rs.getBytes("photo");
                JLabel imageLabel = new JLabel();
                if (photoBytes != null) {
                    ImageIcon icon = new ImageIcon(photoBytes);
                    Image img = icon.getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(img));
                } else {
                    imageLabel.setText("No image");
                    imageLabel.setPreferredSize(new Dimension(170, 170));
                }

                itemPanel.add(imageLabel, BorderLayout.WEST);

                String info = "<html><b>Name:</b> " + rs.getString("name") +
                        "<br><b>Price:</b> ₹" + rs.getDouble("price") +
                        "<br><b>Type:</b> " + rs.getString("type") +
                        "<br><b>Design:</b> " + rs.getString("design") +
                        "<br><b>Description:</b> " + rs.getString("description") + "</html>";

                JLabel infoLabel = new JLabel(info);
                infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JPanel rightPanel = new JPanel(new BorderLayout());
                rightPanel.add(infoLabel, BorderLayout.CENTER);

                JPanel btnPanel = new JPanel();
                JButton updateBtn = new JButton("Update");
                JButton deleteBtn = new JButton("Delete");
                JButton cartBtn = new JButton("Add to Cart");

                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                updateBtn.addActionListener(e -> {
                    populateForUpdate(name);
                    viewFrame.dispose();
                });
                deleteBtn.addActionListener(e -> {
                    deleteJewelry(name);
                    viewFrame.dispose();
                    viewJewelry(); // Refresh
                });
                cartBtn.addActionListener(e -> {
                    boolean found = false;

                    for (CartItem item : CartItem.getCartList()) {
                        if (item.getName().equals(name)) {
                            item.incrementQuantity();
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        CartItem.addToCart(name, price);
                    }

                    JOptionPane.showMessageDialog(frame, name + " added to cart!", "Cart", JOptionPane.INFORMATION_MESSAGE);
                });


                btnPanel.add(updateBtn);
                btnPanel.add(deleteBtn);
                btnPanel.add(cartBtn);
                rightPanel.add(btnPanel, BorderLayout.SOUTH);
                itemPanel.add(rightPanel, BorderLayout.CENTER);
                panel.add(itemPanel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        viewFrame.add(scrollPane, BorderLayout.CENTER);
        viewFrame.setVisible(true);
    }


    private void deleteJewelry(String name) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM silver_jewelry WHERE name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Deleted successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error deleting: " + e.getMessage());
        }
    }

    private void populateForUpdate(String name) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM silver_jewelry WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                priceField.setText(rs.getString("price"));
                typeField.setText(rs.getString("type"));
                designField.setText(rs.getString("design"));
                descriptionArea.setText(rs.getString("description"));
                byte[] photoBytes = rs.getBytes("photo");
                if (photoBytes != null) {
                    ImageIcon icon = new ImageIcon(photoBytes);
                    Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    photoLabel.setIcon(new ImageIcon(img));
                    photoLabel.setText(""); // Remove old text
                } else {
                    photoLabel.setText("No photo available");
                    photoLabel.setIcon(null);
                }

                JButton updateBtn = new JButton("Update Now");
                updateBtn.setBounds(370, 600, 150, 30);
                frame.add(updateBtn);
                frame.repaint();

                updateBtn.addActionListener(e -> {
                    updateJewelry(name);
                    frame.remove(updateBtn);
                    frame.repaint();
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateJewelry(String name) {
        try {
            PreparedStatement ps;

            if (selectedImageFile != null) {
                ps = conn.prepareStatement(
                        "UPDATE silver_jewelry SET price = ?, type = ?, design = ?, description = ?, photo = ? WHERE name = ?"
                );
                ps.setDouble(1, Double.parseDouble(priceField.getText()));
                ps.setString(2, typeField.getText());
                ps.setString(3, designField.getText());
                ps.setString(4, descriptionArea.getText());
                ps.setBytes(5, getPhotoBytes(selectedImageFile)); // new photo
                ps.setString(6, name);
            } else {
                ps = conn.prepareStatement(
                        "UPDATE silver_jewelry SET price = ?, type = ?, design = ?, description = ? WHERE name = ?"
                );
                ps.setDouble(1, Double.parseDouble(priceField.getText()));
                ps.setString(2, typeField.getText());
                ps.setString(3, designField.getText());
                ps.setString(4, descriptionArea.getText());
                ps.setString(5, name);
            }

            ps.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Jewellery updated.");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Update Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private byte[] getPhotoBytes(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) bos.write(buffer, 0, len);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadPhoto() {
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Jewellery Photo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            photoLabel.setText("Photo: " + selectedImageFile.getName());
        }
    }

    private void resetForm() {
        nameField.setText("");
        priceField.setText("");
        typeField.setText("");
        designField.setText("");
        descriptionArea.setText("");
        photoLabel.setText("No photo selected");
        selectedImageFile = null;
        photoLabel.setText("No photo selected");
        photoLabel.setIcon(null);

    }
}
