package ui;

import model.Category;
import model.Product;

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

        buttonsProduct = new ButtonGroup();
        int omamoriNum = manager.getProductList().returnSizeCategory(Category.valueOf("お守り"));
        int emaNum = manager.getProductList().returnSizeCategory(Category.valueOf("絵馬"));
        int gosyuinnNum = manager.getProductList().returnSizeCategory(Category.valueOf("御朱印"));
        int othersNum = manager.getProductList().returnSizeCategory(Category.valueOf("その他"));

        productButtons = new JRadioButton[omamoriNum+emaNum+gosyuinnNum+othersNum];
        int i = 0;
        for(Product p: manager.getProductList().getProductList()) {
            productButtons[i] = new JRadioButton(p.getName());
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
        JPanel productOperate = new JPanel();

        addProduct = new JButton("追加");
        seeProduct = new JButton("詳細");
        deleteProduct = new JButton("削除");
        productOperate.add(addProduct);
        productOperate.add(seeProduct);
        productOperate.add(deleteProduct);
        projectManagerContainer.add(productOperate, BorderLayout.SOUTH);

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

                            File selectedFile = fileChooser.getSelectedFile();

                            // 画像を300x300にリサイズして表示
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

                                // ファイルを指定のディレクトリにコピー
                                Path source = Paths.get(selectedFile.toURI());
                                Path destination = Paths.get("./data/image.jpg");
                                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);


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

                    }
                });

                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        inputDialog.dispose();
                    }
                });

                inputDialog.setSize(500, 400);
                inputDialog.setLocationRelativeTo(frame);
                inputDialog.setVisible(true);
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
        frame.setSize(800, 600);
        frame.setTitle("T神社頒布管理");
        frame.setVisible(true);
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

