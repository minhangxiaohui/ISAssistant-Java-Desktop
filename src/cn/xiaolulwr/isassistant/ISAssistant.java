package cn.xiaolulwr.isassistant;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JCheckBox;

public class ISAssistant extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldCryptoFile;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField textFieldSignFile;
	private JTextField textField;
	private JPasswordField passwordField_2;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ISAssistant frame = new ISAssistant();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ISAssistant() {
		setResizable(false);
		setTitle("信息安全助手");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 423);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("文件");
		menuBar.add(menu);
		
		JMenuItem menuItemNewKeyStore = new JMenuItem("新建密钥库");
		menuItemNewKeyStore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		menu.add(menuItemNewKeyStore);
		
		JMenuItem menuItemOpenKeyStore = new JMenuItem("打开密钥库");
		menu.add(menuItemOpenKeyStore);
		
		JMenuItem menuItemOpenFile = new JMenuItem("打开文件");
		menu.add(menuItemOpenFile);
		
		JMenuItem menuItemExit = new JMenuItem("退出");
		menu.add(menuItemExit);
		
		JMenu menu_1 = new JMenu("帮助");
		menuBar.add(menu_1);
		
		JMenuItem menuItemOpenSource = new JMenuItem("开源许可");
		menu_1.add(menuItemOpenSource);
		
		JMenuItem menuItemEditor = new JMenuItem("联系开发者");
		menu_1.add(menuItemEditor);
		
		JMenuItem menuItemAbout = new JMenuItem("关于信息安全助手");
		menu_1.add(menuItemAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPaneMain = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneMain.setBounds(5, 5, 490, 370);
		contentPane.add(tabbedPaneMain);
		
		Color bgColor=new Color(229, 229, 229);
		
		JPanel panelEncrypt = new JPanel();
		panelEncrypt.setForeground(Color.WHITE);
		panelEncrypt.setBackground(bgColor);
		tabbedPaneMain.addTab("文件加密", null, panelEncrypt, null);
		tabbedPaneMain.setEnabledAt(0, true);
		panelEncrypt.setLayout(null);
		
		textFieldCryptoFile = new JTextField();
		textFieldCryptoFile.setEditable(false);
		textFieldCryptoFile.setBounds(92, 22, 281, 26);
		panelEncrypt.add(textFieldCryptoFile);
		textFieldCryptoFile.setColumns(10);
		
		JLabel label = new JLabel("选择文件");
		label.setBounds(34, 27, 61, 16);
		panelEncrypt.add(label);
		
		JButton buttonSelect = new JButton("浏览");
		buttonSelect.setBounds(374, 22, 75, 29);
		panelEncrypt.add(buttonSelect);
		
		JButton btnEncrypt = new JButton("开始加密");
		btnEncrypt.setBounds(102, 272, 117, 29);
		panelEncrypt.add(btnEncrypt);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(92, 120, 281, 26);
		panelEncrypt.add(passwordField);
		
		JLabel label_1 = new JLabel("输入密码");
		label_1.setBounds(34, 125, 61, 16);
		panelEncrypt.add(label_1);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(92, 166, 281, 26);
		panelEncrypt.add(passwordField_1);
		
		JLabel label_2 = new JLabel("确认密码");
		label_2.setBounds(34, 171, 61, 16);
		panelEncrypt.add(label_2);
		
		JSlider slider = new JSlider();
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		slider.setValue(1);
		slider.setMaximum(2);
		slider.setBounds(133, 60, 190, 29);
		panelEncrypt.add(slider);
		
		JLabel lblNewLabel = new JLabel("加密强度");
		lblNewLabel.setBounds(78, 68, 61, 16);
		panelEncrypt.add(lblNewLabel);
		
		JLabel label_3 = new JLabel("密码长度必须是6-16位");
		label_3.setBounds(166, 204, 136, 16);
		panelEncrypt.add(label_3);
		
		JLabel lblaes = new JLabel("一般");
		lblaes.setToolTipText("使用AES-128");
		lblaes.setBounds(143, 92, 34, 16);
		panelEncrypt.add(lblaes);
		
		JLabel label_4 = new JLabel("强");
		label_4.setToolTipText("使用AES");
		label_4.setBounds(222, 92, 13, 16);
		panelEncrypt.add(label_4);
		
		JLabel label_5 = new JLabel("很强");
		label_5.setBounds(289, 92, 34, 16);
		panelEncrypt.add(label_5);
		
		JButton button = new JButton("重置");
		button.setBounds(241, 272, 117, 29);
		panelEncrypt.add(button);
		
		JPanel panelDecrypt = new JPanel();
		panelDecrypt.setBackground(bgColor);
		tabbedPaneMain.addTab("文件解密", null, panelDecrypt, null);
		panelDecrypt.setLayout(null);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(92, 22, 281, 26);
		panelDecrypt.add(textField);
		
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(92, 120, 281, 26);
		panelDecrypt.add(passwordField_2);
		
		JLabel label_7 = new JLabel("选择文件");
		label_7.setBounds(34, 27, 61, 16);
		panelDecrypt.add(label_7);
		
		JLabel label_8 = new JLabel("输入密码");
		label_8.setBounds(34, 125, 61, 16);
		panelDecrypt.add(label_8);
		
		JButton button_1 = new JButton("开始解密");
		button_1.setBounds(110, 251, 117, 29);
		panelDecrypt.add(button_1);
		
		JLabel label_9 = new JLabel("密码长度必须是6-16位");
		label_9.setBounds(164, 185, 136, 16);
		panelDecrypt.add(label_9);
		
		JButton button_2 = new JButton("浏览");
		button_2.setBounds(374, 22, 75, 29);
		panelDecrypt.add(button_2);
		
		JButton button_3 = new JButton("重置");
		button_3.setBounds(239, 251, 117, 29);
		panelDecrypt.add(button_3);
		
		JPanel panelSign = new JPanel();
		panelSign.setBackground(bgColor);
		tabbedPaneMain.addTab("数字签名", null, panelSign, null);
		panelSign.setLayout(null);
		
		textFieldSignFile = new JTextField();
		textFieldSignFile.setEditable(false);
		textFieldSignFile.setColumns(10);
		textFieldSignFile.setBounds(92, 22, 281, 26);
		panelSign.add(textFieldSignFile);
		
		JLabel label_6 = new JLabel("选择文件");
		label_6.setBounds(34, 27, 52, 16);
		panelSign.add(label_6);
		
		JButton button_4 = new JButton("浏览");
		button_4.setBounds(374, 22, 75, 29);
		panelSign.add(button_4);
		
		JCheckBox checkBox = new JCheckBox("验证签名");
		checkBox.setBounds(374, 95, 84, 23);
		panelSign.add(checkBox);
		
		textField_1 = new JTextField();
		textField_1.setBounds(92, 94, 281, 26);
		panelSign.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel label_10 = new JLabel("签名值");
		label_10.setBounds(34, 99, 44, 16);
		panelSign.add(label_10);
		
		JPanel panelHash = new JPanel();
		panelHash.setBackground(bgColor);
		tabbedPaneMain.addTab("消息摘要", null, panelHash, null);
		panelHash.setLayout(null);
		
		JPanel panelMac = new JPanel();
		panelMac.setBackground(bgColor);
		tabbedPaneMain.addTab("消息认证码", null, panelMac, null);
		panelMac.setLayout(null);
	}
}
