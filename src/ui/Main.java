package ui;

import model.Category;
import model.Product;
import model.ProductList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends JFrame {

    private JFrame frame;

    private JPanel salesManager;
    private JPanel productManager;
    private JPanel salesHistory;
    private JPanel omamoriPanel;
    private JPanel emaPanel;
    private JPanel gosyuinPanel;
    private JPanel otherPanel;
    private JPanel projectManagerContainer;
    private JPanel productOperate;

    private JButton newSales;
    private JButton loadSales;
    private JButton addProduct;
    private JButton seeProduct;
    private JButton deleteProduct;

    private JMenuBar menuBar;

    private JLabel dateLabel7;

    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JMenuItem menuItem4;
    private JMenuItem menuItem5;

    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JScrollPane scrollPane3;
    private JScrollPane scrollPane4;

    private Manager manager;

    private ButtonGroup buttonsProduct;
    private JRadioButton[] productButtons;

    private File selectedFile;

    private Product selectProduct;

    public Main() {

        frame = new JFrame();
        ImageIcon icon = new ImageIcon( "./data/images/torii.jpg");
        frame.setIconImage(icon.getImage());

        manager = new Manager();

        manager.loadProductInfo();
        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        dateLabel7 = new JLabel();
        dateLabel7.setBounds(0, 0, 50, 50);
        //to create new data and save data
        menuBar = new JMenuBar();

        JMenu file = new JMenu("ファイル");
        menuBar.add(file);
        //create file buttons
        createMenuItem();
        file.add(menuItem1);
        file.add(menuItem2);
        file.add(menuItem3);
        file.add(menuItem4);
        file.add(menuItem5);

        productManager = new JPanel();
        projectManagerContainer = new JPanel();
        omamoriPanel = new JPanel();
        emaPanel = new JPanel();
        gosyuinPanel = new JPanel();
        otherPanel = new JPanel();

        omamoriPanel.setLayout(new BoxLayout(omamoriPanel, BoxLayout.Y_AXIS));
        emaPanel.setLayout(new BoxLayout(emaPanel, BoxLayout.Y_AXIS));
        gosyuinPanel.setLayout(new BoxLayout(gosyuinPanel, BoxLayout.Y_AXIS));
        otherPanel.setLayout(new BoxLayout(otherPanel, BoxLayout.Y_AXIS));

        buttonsProduct = new ButtonGroup();
        int omamoriNum = manager.getProductList().returnSizeCategory(Category.valueOf("お守り"));
        int emaNum = manager.getProductList().returnSizeCategory(Category.valueOf("絵馬"));
        int gosyuinnNum = manager.getProductList().returnSizeCategory(Category.valueOf("御朱印"));
        int othersNum = manager.getProductList().returnSizeCategory(Category.valueOf("その他"));

        productButtons = new JRadioButton[omamoriNum+emaNum+gosyuinnNum+othersNum];
        int i = 0;
        for(Product p: manager.getProductList().getProductList()) {
            productButtons[i] = new JRadioButton(p.getName());
            productButtons[i].setToolTipText(p.getName());
            buttonsProduct.add(productButtons[i]);
            addButtonToCategory(p.getCategory(), productButtons[i]);
            i++;
        }




        scrollPane1 = new JScrollPane(omamoriPanel);
        scrollPane2 = new JScrollPane(emaPanel);
        scrollPane3 = new JScrollPane(gosyuinPanel);
        scrollPane4 = new JScrollPane(otherPanel);
        scrollPane1.setPreferredSize(new Dimension(400, 200)); // Adjust as needed
        scrollPane2.setPreferredSize(new Dimension(400, 200)); // Adjust as needed
        scrollPane3.setPreferredSize(new Dimension(400, 200)); // Adjust as needed
        scrollPane4.setPreferredSize(new Dimension(400, 200)); // Adjust as needed

        productManager.setLayout(new GridLayout(2,2));
        productManager.add(scrollPane1);
        productManager.add(scrollPane2);
        productManager.add(scrollPane3);
        productManager.add(scrollPane4);
        projectManagerContainer.add(productManager,BorderLayout.CENTER);
        productOperate = new JPanel();

        addProduct = new JButton("追加");
        seeProduct = new JButton("詳細");
        deleteProduct = new JButton("削除");
        productOperate.add(addProduct);
        productOperate.add(seeProduct);
        productOperate.add(deleteProduct);
        projectManagerContainer.add(productOperate, BorderLayout.SOUTH);

        seeProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(buttonsProduct.getSelection() == null) {
                    JOptionPane.showMessageDialog(frame, "エラー: ボタンを選択してください", "エラー", JOptionPane.ERROR_MESSAGE);
                } else {
                    //get selected product
                    String selectedProduct = "";
                    for (int i = 0; i < manager.getProductList().getProductList().size(); i++) {
                        if (productButtons[i].isSelected()) {
                            selectedProduct = productButtons[i].getText();
                        }
                    }
                    ProductList temp = manager.getProductList();
                    //This is the selected product
                    selectProduct = temp.getProductFromName(selectedProduct);


                    JDialog inputDialog = new JDialog(frame, "頒布物編集", true);
                    inputDialog.setLayout(new GridLayout(9, 2));
                    JLabel dateLabel = new JLabel("頒布物名: ");
                    JTextField textField = new JTextField();
                    textField.setText(selectProduct.getName());

                    JLabel dateLabel2 = new JLabel("価格 : ");
                    JTextField textField2 = new JTextField();
                    textField2.setText(String.valueOf(selectProduct.getPrice()));

                    JLabel dateLabel3 = new JLabel("数量 : ");
                    JTextField textField3 = new JTextField();
                    textField3.setText(String.valueOf(selectProduct.getQuantity()));
                    JLabel dateLabel4 = new JLabel("カテゴリ : ");
                    String[] dataLabelOptions4 = {"絵馬", "お守り", "御朱印", "その他"};
                    JComboBox<String> dataComboBox4 = new JComboBox<>(dataLabelOptions4);
                    dataComboBox4.setSelectedItem(selectProduct.getCategory().name());
                    JLabel dateLabel5 = new JLabel("画像 : ");
                    JLabel dateLabel6 = new JLabel("");
                    JButton imageButton = new JButton("画像を選択");
                    JLabel space = new JLabel("");
                    JLabel space1 = new JLabel("");

                    String imageP = selectProduct.getImagePath();
                    ImageIcon iconEdit = resizeImage(imageP, 50, 50);
                    dateLabel7.setIcon(iconEdit);
                    dateLabel7.setHorizontalAlignment(JLabel.CENTER);

                    imageButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.setDialogTitle("画像を選択");

                            int userSelection = fileChooser.showOpenDialog(null);

                            if (userSelection == JFileChooser.APPROVE_OPTION) {

                                selectedFile = fileChooser.getSelectedFile();

                                try {
                                    BufferedImage originalImage = ImageIO.read(selectedFile);
                                    int originalWidth = originalImage.getWidth();
                                    int originalHeight = originalImage.getHeight();
                                    int newWidth, newHeight;

                                    if (originalWidth > originalHeight) {
                                        newWidth = 50;
                                        newHeight = (int) ((double) originalHeight / originalWidth * 50);
                                    } else {
                                        newWidth = (int) ((double) originalWidth / originalHeight * 50);
                                        newHeight = 50;
                                    }

                                    BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                                    Graphics2D g = resizedImage.createGraphics();
                                    g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                                    g.dispose();

                                    ImageIcon icon = new ImageIcon(resizedImage);
                                    dateLabel7.setHorizontalAlignment(JLabel.CENTER);
                                    dateLabel7.setIcon(icon);


                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                    dateLabel7.setPreferredSize(new Dimension(50, 50));
                    dateLabel7.setVerticalAlignment(SwingConstants.TOP);

                    inputDialog.add(dateLabel);
                    inputDialog.add(textField);
                    inputDialog.add(dateLabel2);
                    inputDialog.add(textField2);
                    inputDialog.add(dateLabel3);
                    inputDialog.add(textField3);
                    inputDialog.add(dateLabel4);
                    inputDialog.add(dataComboBox4);
                    inputDialog.add(dateLabel5);
                    inputDialog.add(dateLabel6);
                    inputDialog.add(dateLabel7);
                    inputDialog.add(imageButton);
                    inputDialog.add(space);
                    inputDialog.add(space1);


                    JButton okButton = new JButton("変更確定");
                    JButton cancelButton = new JButton("キャンセル");
                    // Use FlowLayout for the third row to center the button
                    inputDialog.add(okButton);
                    inputDialog.add(cancelButton);

                    okButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                manager.getProductList().removeProduct(selectProduct);
                                if (selectedFile != null) {
                                    Path source = Paths.get(selectedFile.toURI());
                                    String dataFile = "./data/ProductImages/" + selectedFile.getName();
                                    Path destination = Paths.get(dataFile);

                                    if (!Files.exists(destination)) {
                                        try {
                                            Files.createDirectories(destination);
                                        } catch (IOException exception) {
                                            exception.printStackTrace();
                                        }
                                    }

                                    Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);


                                    String name = textField.getText();
                                    int price = Integer.valueOf(textField2.getText());
                                    int num = Integer.valueOf(textField3.getText());
                                    String categoryString = (String) dataComboBox4.getSelectedItem();
                                    Category category = Category.valueOf(categoryString);
                                    selectProduct = new Product(name, num, price, category, dataFile);
                                    manager.getProductList().addProduct(selectProduct);
                                    manager.saveProductInfo(manager.getProductList());
                                } else {

                                    String name = textField.getText();
                                    int price = Integer.valueOf(textField2.getText());
                                    int num = Integer.valueOf(textField3.getText());
                                    String categoryString = (String) dataComboBox4.getSelectedItem();
                                    Category category = Category.valueOf(categoryString);
                                    selectProduct = new Product(name, num, price, category);
                                    manager.getProductList().addProduct(selectProduct);
                                    manager.saveProductInfo(manager.getProductList());
                                }

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            dateLabel7 = new JLabel();
                            dateLabel7.setBounds(0, 0, 50, 50);
                            dateLabel7.setHorizontalAlignment(JLabel.CENTER);
                            dateLabel7.setPreferredSize(new Dimension(50, 50));
                            dateLabel7.setVerticalAlignment(SwingConstants.TOP);
                            productReload();
                            inputDialog.dispose();
                        }
                    });

                    cancelButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dateLabel7 = new JLabel();
                            dateLabel7.setBounds(0, 0, 50, 50);
                            dateLabel7.setHorizontalAlignment(JLabel.CENTER);
                            dateLabel7.setPreferredSize(new Dimension(50, 50));
                            dateLabel7.setVerticalAlignment(SwingConstants.TOP);
                            inputDialog.dispose();
                        }
                    });

                    inputDialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {

                            dateLabel7 = new JLabel();
                            dateLabel7.setBounds(0, 0, 50, 50);
                            dateLabel7.setHorizontalAlignment(JLabel.CENTER);
                            dateLabel7.setPreferredSize(new Dimension(50, 50));
                            dateLabel7.setVerticalAlignment(SwingConstants.TOP);
                            inputDialog.dispose();
                        }
                    });


                    inputDialog.setSize(500, 500);
                    inputDialog.setLocationRelativeTo(frame);
                    inputDialog.setVisible(true);
                }
            }
        });

        addProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog inputDialog = new JDialog(frame, "頒布物追加", true);
                inputDialog.setLayout(new GridLayout(9,2));
                JLabel dateLabel = new JLabel("頒布物名: ");
                JTextField textField = new JTextField();
                JLabel dateLabel2 = new JLabel("価格 : ");
                JTextField textField2 = new JTextField();
                JLabel dateLabel3 = new JLabel("数量 : ");
                JTextField textField3 = new JTextField();
                JLabel dateLabel4 = new JLabel("カテゴリ : ");
                String[] dataLabelOptions4 = {"絵馬", "お守り", "御朱印","その他"};
                JComboBox<String> dataComboBox4 = new JComboBox<>(dataLabelOptions4);
                JLabel dateLabel5 = new JLabel("画像 : ");
                JLabel dateLabel6 = new JLabel("");
                JButton imageButton = new JButton("画像を選択");
                JLabel space = new JLabel("");
                JLabel space1 = new JLabel("");

                imageButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("画像を選択");

                        int userSelection = fileChooser.showOpenDialog(null);

                        if (userSelection == JFileChooser.APPROVE_OPTION) {

                            selectedFile = fileChooser.getSelectedFile();

                            try {
                                BufferedImage originalImage = ImageIO.read(selectedFile);
                                int originalWidth = originalImage.getWidth();
                                int originalHeight = originalImage.getHeight();
                                int newWidth, newHeight;

                                if (originalWidth > originalHeight) {
                                    newWidth = 50;
                                    newHeight = (int) ((double) originalHeight / originalWidth * 50);
                                } else {
                                    newWidth = (int) ((double) originalWidth / originalHeight * 50);
                                    newHeight = 50;
                                }

                                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g = resizedImage.createGraphics();
                                g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
                                g.dispose();

                                ImageIcon icon = new ImageIcon(resizedImage);
                                dateLabel7.setHorizontalAlignment(JLabel.CENTER);
                                dateLabel7.setIcon(icon);




                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
                dateLabel7.setPreferredSize(new Dimension(50, 50));
                dateLabel7.setVerticalAlignment(SwingConstants.TOP);

                inputDialog.add(dateLabel);
                inputDialog.add(textField);
                inputDialog.add(dateLabel2);
                inputDialog.add(textField2);
                inputDialog.add(dateLabel3);
                inputDialog.add(textField3);
                inputDialog.add(dateLabel4);
                inputDialog.add(dataComboBox4);
                inputDialog.add(dateLabel5);
                inputDialog.add(dateLabel6);
                inputDialog.add(dateLabel7);
                inputDialog.add(imageButton);
                inputDialog.add(space);
                inputDialog.add(space1);


                JButton okButton = new JButton("OK");
                JButton cancelButton = new JButton("キャンセル");
                // Use FlowLayout for the third row to center the button
                inputDialog.add(okButton);
                inputDialog.add(cancelButton);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if(selectedFile != null) {
                                Path source = Paths.get(selectedFile.toURI());
                                String dataFile = "./data/ProductImages/" + selectedFile.getName();
                                Path destination = Paths.get(dataFile);

                                if (!Files.exists(destination)) {
                                    try {
                                        Files.createDirectories(destination);
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }
                                }

                                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);


                                String name = textField.getText();
                                int price = Integer.valueOf(textField2.getText());
                                int num = Integer.valueOf(textField3.getText());
                                String categoryString = (String) dataComboBox4.getSelectedItem();
                                Category category = Category.valueOf(categoryString);
                                Product newProduct = new Product(name, num, price, category, dataFile);
                                manager.getProductList().addProduct(newProduct);
                                manager.saveProductInfo(manager.getProductList());
                            } else {
                                String name = textField.getText();
                                int price = Integer.valueOf(textField2.getText());
                                int num = Integer.valueOf(textField3.getText());
                                String categoryString = (String) dataComboBox4.getSelectedItem();
                                Category category = Category.valueOf(categoryString);
                                Product newProduct = new Product(name, num, price, category);
                                manager.getProductList().addProduct(newProduct);
                                manager.saveProductInfo(manager.getProductList());
                            }

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        dateLabel7 = new JLabel();
                        dateLabel7.setBounds(0, 0, 50, 50);
                        dateLabel7.setHorizontalAlignment(JLabel.CENTER);
                        dateLabel7.setPreferredSize(new Dimension(50, 50));
                        dateLabel7.setVerticalAlignment(SwingConstants.TOP);
                        productReload();
                        inputDialog.dispose();
                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        inputDialog.dispose();
                    }
                });

                inputDialog.setSize(500, 500);
                inputDialog.setLocationRelativeTo(frame);
                inputDialog.setVisible(true);
            }
        });

        deleteProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(buttonsProduct.getSelection() == null) {
                    JOptionPane.showMessageDialog(frame, "エラー: ボタンを選択してください", "エラー", JOptionPane.ERROR_MESSAGE);
                } else {
                    String selectedProduct = "";
                    for (int i = 0; i < manager.getProductList().getProductList().size(); i++) {
                        if (productButtons[i].isSelected()) {
                            selectedProduct = productButtons[i].getText();
                        }
                    }
                    ProductList temp = manager.getProductList();
                    temp.removeProduct(temp.getProductFromName(selectedProduct));
                    manager.saveProductInfo(manager.getProductList());
                    productReload();
                }
            }
        });

        // Add panels to the tabbed pane
        tabbedPane.addTab("奉納額管理", salesManager);
        tabbedPane.addTab("奉納物管理", projectManagerContainer);
        tabbedPane.addTab("奉納記録", salesHistory);

        // Add tabbed pane to the frame
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 600);
        frame.setTitle("T神社頒布管理");
        frame.setVisible(true);
    }


    private void productReload() {
        int omamoriNum = manager.getProductList().returnSizeCategory(Category.valueOf("お守り"));
        int emaNum = manager.getProductList().returnSizeCategory(Category.valueOf("絵馬"));
        int gosyuinnNum = manager.getProductList().returnSizeCategory(Category.valueOf("御朱印"));
        int othersNum = manager.getProductList().returnSizeCategory(Category.valueOf("その他"));

        omamoriPanel.removeAll();
        emaPanel.removeAll();
        gosyuinPanel.removeAll();
        otherPanel.removeAll();
        productButtons = new JRadioButton[omamoriNum+emaNum+gosyuinnNum+othersNum];
        int i = 0;
        for(Product p: manager.getProductList().getProductList()) {
            productButtons[i] = new JRadioButton(p.getName());
            productButtons[i].setToolTipText(p.getName());
            buttonsProduct.add(productButtons[i]);
            addButtonToCategory(p.getCategory(), productButtons[i]);
            i++;
        }
        frame.revalidate();
        frame.repaint();
    }

    private static ImageIcon resizeImage(String imagePath, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            // 画像を指定サイズにリサイズ
            BufferedImage resizedImage = new BufferedImage(width, height, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, width, height, null);
            g.dispose();

            return new ImageIcon(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addButtonToCategory(Category category, JRadioButton button) {
        if (category.equals(Category.valueOf("絵馬"))) {
            emaPanel.add(button);
        } else if (category.equals(Category.valueOf("お守り"))) {
            omamoriPanel.add(button);
        } else if (category.equals(Category.valueOf("御朱印"))) {
            gosyuinPanel.add(button);
        } else {
            otherPanel.add(button);
        }
    }

    private void createMenuItem() {
        menuItem1 = new JMenuItem("新しいファイル");
        menuItem2 = new JMenuItem("保存");
        menuItem3 = new JMenuItem("ロード");
        menuItem4 = new JMenuItem("バックアップをとる");
        menuItem5 = new JMenuItem("バックアップからデータを戻す");

        //add action listeners

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog inputDialog = new JDialog(frame, "新しいファイルを作成", true);
                inputDialog.setLayout(new GridLayout(3,1));
                JLabel dateLabel = new JLabel("ファイルの名前: ");
                JTextField textField = new JTextField();

                inputDialog.add(dateLabel);
                inputDialog.add(textField);


                JButton okButton = new JButton("ファイルを作成");
                // Use FlowLayout for the third row to center the button
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.add(okButton);
                inputDialog.add(buttonPanel);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = textField.getText();
                        String fileName = "./data/" + name + ".json";
                        manager.changeSalesRecordName(fileName);
                        manager.reset();
                    }
                });

                inputDialog.setSize(500, 150);
                inputDialog.setLocationRelativeTo(frame);
                inputDialog.setVisible(true);
            }
        });

        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.loadSalesRecords();
            }
        });

        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog inputDialog = new JDialog(frame, "バックアップ作成", true);
                inputDialog.setLayout(new GridLayout(3,1));
                JLabel dateLabel = new JLabel("バックアップファイル名: ");
                JTextField textField = new JTextField();

                inputDialog.add(dateLabel);
                inputDialog.add(textField);

                JButton okButton = new JButton("バックアップを作成");
                // Use FlowLayout for the third row to center the button
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.add(okButton);
                inputDialog.add(buttonPanel);

                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = textField.getText();
                        String fileName = "./data/BackUp/" + name + ".json";
                        manager.backUpSales(manager.getSalesRecord(), fileName);

                        // Close the dialog after creating the backup
                        inputDialog.dispose();
                    }
                });

                inputDialog.setSize(500, 150);
                inputDialog.setLocationRelativeTo(frame);
                inputDialog.setVisible(true);

            }
        });

        menuItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



    }


    //EFFECTS: call Main constructor to start application
    public static void main(String[] args) {

        new Main();
    }

}

