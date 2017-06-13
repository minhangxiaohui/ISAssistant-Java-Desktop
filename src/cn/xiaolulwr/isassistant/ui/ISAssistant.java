package cn.xiaolulwr.isassistant.ui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import cn.xiaolulwr.isassistant.common.DigitalSignatureAlgorithm;
import cn.xiaolulwr.isassistant.common.HashAlgorithm;
import cn.xiaolulwr.isassistant.common.KeyStoreManager;
import cn.xiaolulwr.isassistant.common.ParentFrameInterface;
import cn.xiaolulwr.isassistant.crypto.DecryptCore;
import cn.xiaolulwr.isassistant.crypto.EncryptCore;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.security.Security;
import java.util.Arrays;
import java.awt.event.ActionEvent;

import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;

public class ISAssistant extends JFrame implements ActionListener,ParentFrameInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldEncryptFile;
	private JPasswordField passwordFieldSet;
	private JPasswordField passwordFieldConfirm;
	private JTextField textFieldSignFile;
	private JTextField textFieldDecryptFile;
	private JPasswordField passwordFieldCheck;
	private JTextField textFieldSignValue;
	private JTextField textFieldHashMessage;
	private JTextField textFieldMacMessage;
	private JTextField textField224Value;
	private JTextField textField256Value;
	private JTextField textField384Value;
	private JTextField textField512Value;
	
	private File workingFile,keystoreFile;
	private KeyStoreManager ksmanager;
	private JButton btnEncrypt;
	private JButton btnSign;
	private JButton btnHash;
	private JButton btnMac;
	private JButton btnDecrypt;
	private JSlider sliderEncryptMode;
	private JCheckBox checkBoxIsVerify;
	private JComboBox<HashAlgorithm> comboBoxHash;
	private JComboBox<DigitalSignatureAlgorithm> comboBoxDigitalSign;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Security.addProvider(new BouncyCastleProvider());
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
		
		JMenuItem menuItemOpenFile = new JMenuItem("打开文件");
		menuItemOpenFile.addActionListener(this);
		menu.add(menuItemOpenFile);
		
		JMenuItem menuItemExit = new JMenuItem("退出");
		menuItemExit.addActionListener(this);
		
		JMenuItem menuItemCloseFile = new JMenuItem("关闭文件");
		menuItemCloseFile.addActionListener(this);
		menu.add(menuItemCloseFile);
		menu.add(menuItemExit);
		
		JMenu menu_2 = new JMenu("密钥库");
		menuBar.add(menu_2);
		
		JMenuItem menuItemNewKeyStore = new JMenuItem("新建密钥库");
		menuItemNewKeyStore.addActionListener(this);
		menu_2.add(menuItemNewKeyStore);
		
		JMenuItem menuItemOpenKeyStore = new JMenuItem("打开密钥库");
		menuItemOpenKeyStore.addActionListener(this);
		menu_2.add(menuItemOpenKeyStore);
		
		JMenuItem menuItemCloseKeyStore = new JMenuItem("关闭密钥库");
		menuItemCloseKeyStore.addActionListener(this);
		menu_2.add(menuItemCloseKeyStore);
		
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
		panelEncrypt.setBackground(bgColor);
		tabbedPaneMain.addTab("文件加密", null, panelEncrypt, null);
		tabbedPaneMain.setEnabledAt(0, true);
		panelEncrypt.setLayout(null);
		
		textFieldEncryptFile = new JTextField();
		textFieldEncryptFile.setEditable(false);
		textFieldEncryptFile.setBounds(92, 22, 281, 26);
		panelEncrypt.add(textFieldEncryptFile);
		textFieldEncryptFile.setColumns(10);
		
		JLabel label = new JLabel("选择文件");
		label.setBounds(34, 27, 61, 16);
		panelEncrypt.add(label);
		
		JButton btnEncryptSelect = new JButton("浏览");
		btnEncryptSelect.addActionListener(this);
		btnEncryptSelect.setBounds(374, 22, 75, 29);
		panelEncrypt.add(btnEncryptSelect);
		
		btnEncrypt = new JButton("开始加密");
		btnEncrypt.addActionListener(this);
		btnEncrypt.setBounds(102, 272, 117, 29);
		panelEncrypt.add(btnEncrypt);
		
		passwordFieldSet = new JPasswordField();
		passwordFieldSet.setBounds(92, 120, 281, 26);
		panelEncrypt.add(passwordFieldSet);
		
		JLabel label_1 = new JLabel("输入密码");
		label_1.setBounds(34, 125, 61, 16);
		panelEncrypt.add(label_1);
		
		passwordFieldConfirm = new JPasswordField();
		passwordFieldConfirm.setBounds(92, 166, 281, 26);
		panelEncrypt.add(passwordFieldConfirm);
		
		JLabel label_2 = new JLabel("确认密码");
		label_2.setBounds(34, 171, 61, 16);
		panelEncrypt.add(label_2);
		
		sliderEncryptMode = new JSlider();
		sliderEncryptMode.setSnapToTicks(true);
		sliderEncryptMode.setPaintLabels(true);
		sliderEncryptMode.setValue(1);
		sliderEncryptMode.setMaximum(2);
		sliderEncryptMode.setBounds(133, 60, 190, 29);
		panelEncrypt.add(sliderEncryptMode);
		
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
		
		JButton btnEncryptReset = new JButton("重置");
		btnEncryptReset.addActionListener(this);
		btnEncryptReset.setBounds(241, 272, 117, 29);
		panelEncrypt.add(btnEncryptReset);
		
		JPanel panelDecrypt = new JPanel();
		panelDecrypt.setBackground(bgColor);
		tabbedPaneMain.addTab("文件解密", null, panelDecrypt, null);
		panelDecrypt.setLayout(null);
		
		textFieldDecryptFile = new JTextField();
		textFieldDecryptFile.setEditable(false);
		textFieldDecryptFile.setColumns(10);
		textFieldDecryptFile.setBounds(92, 22, 281, 26);
		panelDecrypt.add(textFieldDecryptFile);
		
		passwordFieldCheck = new JPasswordField();
		passwordFieldCheck.setBounds(92, 120, 281, 26);
		panelDecrypt.add(passwordFieldCheck);
		
		JLabel label_7 = new JLabel("选择文件");
		label_7.setBounds(34, 27, 61, 16);
		panelDecrypt.add(label_7);
		
		JLabel label_8 = new JLabel("输入密码");
		label_8.setBounds(34, 125, 61, 16);
		panelDecrypt.add(label_8);
		
		btnDecrypt = new JButton("开始解密");
		btnDecrypt.addActionListener(this);
		btnDecrypt.setBounds(102, 272, 117, 29);
		panelDecrypt.add(btnDecrypt);
		
		JLabel label_9 = new JLabel("密码长度必须是6-16位");
		label_9.setBounds(166, 204, 136, 16);
		panelDecrypt.add(label_9);
		
		JButton btnDecryptSelect = new JButton("浏览");
		btnDecryptSelect.addActionListener(this);
		btnDecryptSelect.setBounds(374, 22, 75, 29);
		panelDecrypt.add(btnDecryptSelect);
		
		JButton btnDecryptReset = new JButton("重置");
		btnDecryptReset.addActionListener(this);
		btnDecryptReset.setBounds(241, 272, 117, 29);
		panelDecrypt.add(btnDecryptReset);
		
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
		
		JButton btnSignSelect = new JButton("浏览");
		btnSignSelect.addActionListener(this);
		btnSignSelect.setBounds(374, 22, 75, 29);
		panelSign.add(btnSignSelect);
		
		checkBoxIsVerify = new JCheckBox("验证签名");
		checkBoxIsVerify.addActionListener(this);
		checkBoxIsVerify.setBounds(374, 160, 84, 23);
		panelSign.add(checkBoxIsVerify);
		
		textFieldSignValue = new JTextField();
		textFieldSignValue.setBounds(92, 159, 281, 26);
		panelSign.add(textFieldSignValue);
		textFieldSignValue.setColumns(10);
		
		JLabel label_10 = new JLabel("签名值");
		label_10.setBounds(34, 164, 44, 16);
		panelSign.add(label_10);
		
		comboBoxHash = new JComboBox<HashAlgorithm>();
		comboBoxHash.setModel(new DefaultComboBoxModel<HashAlgorithm>(HashAlgorithm.values()));
		comboBoxHash.setBounds(227, 70, 111, 27);
		panelSign.add(comboBoxHash);
		
		comboBoxDigitalSign = new JComboBox<DigitalSignatureAlgorithm>();
		comboBoxDigitalSign.setModel(new DefaultComboBoxModel<DigitalSignatureAlgorithm>(DigitalSignatureAlgorithm.values()));
		comboBoxDigitalSign.setBounds(227, 116, 111, 27);
		panelSign.add(comboBoxDigitalSign);
		
		JLabel label_11 = new JLabel("消息摘要算法");
		label_11.setBounds(131, 74, 84, 16);
		panelSign.add(label_11);
		
		JLabel label_12 = new JLabel("数字签名算法");
		label_12.setBounds(131, 120, 84, 16);
		panelSign.add(label_12);
		
		btnSign = new JButton("开始签名");
		btnSign.addActionListener(this);
		btnSign.setBounds(102, 272, 117, 29);
		panelSign.add(btnSign);
		
		JButton btnSignReset = new JButton("重置");
		btnSignReset.addActionListener(this);
		btnSignReset.setBounds(241, 272, 117, 29);
		panelSign.add(btnSignReset);
		
		JPanel panelHash = new JPanel();
		panelHash.setBackground(bgColor);
		tabbedPaneMain.addTab("消息摘要", null, panelHash, null);
		panelHash.setLayout(null);
		
		textFieldHashMessage = new JTextField();
		textFieldHashMessage.setColumns(10);
		textFieldHashMessage.setBounds(92, 22, 281, 26);
		panelHash.add(textFieldHashMessage);
		
		JLabel label_13 = new JLabel("消息/文件");
		label_13.setBounds(31, 27, 59, 16);
		panelHash.add(label_13);
		
		JButton btnHashSelect = new JButton("浏览");
		btnHashSelect.addActionListener(this);
		btnHashSelect.setBounds(374, 22, 75, 29);
		panelHash.add(btnHashSelect);
		
		btnHash = new JButton("计算消息摘要");
		btnHash.addActionListener(this);
		btnHash.setBounds(102, 272, 117, 29);
		panelHash.add(btnHash);
		
		JButton btnHashReset = new JButton("重置");
		btnHashReset.addActionListener(this);
		btnHashReset.setBounds(241, 272, 117, 29);
		panelHash.add(btnHashReset);
		
		textField224Value = new JTextField();
		textField224Value.setBounds(92, 96, 281, 26);
		panelHash.add(textField224Value);
		textField224Value.setColumns(10);
		
		textField256Value = new JTextField();
		textField256Value.setColumns(10);
		textField256Value.setBounds(92, 134, 281, 26);
		panelHash.add(textField256Value);
		
		textField384Value = new JTextField();
		textField384Value.setColumns(10);
		textField384Value.setBounds(92, 172, 281, 26);
		panelHash.add(textField384Value);
		
		textField512Value = new JTextField();
		textField512Value.setColumns(10);
		textField512Value.setBounds(92, 210, 281, 26);
		panelHash.add(textField512Value);
		
		JLabel lblSha = new JLabel("SHA-224");
		lblSha.setBounds(21, 101, 69, 16);
		panelHash.add(lblSha);
		
		JLabel lblSha_1 = new JLabel("SHA-256");
		lblSha_1.setBounds(21, 139, 69, 16);
		panelHash.add(lblSha_1);
		
		JLabel lblSha_2 = new JLabel("SHA-384");
		lblSha_2.setBounds(21, 177, 69, 16);
		panelHash.add(lblSha_2);
		
		JLabel lblSha_3 = new JLabel("SHA-512");
		lblSha_3.setBounds(21, 215, 69, 16);
		panelHash.add(lblSha_3);
		
		JCheckBox checkboxIsUseSha3 = new JCheckBox("使用SHA-3算法");
		checkboxIsUseSha3.setBounds(162, 61, 128, 23);
		panelHash.add(checkboxIsUseSha3);
		
		JPanel panelMac = new JPanel();
		panelMac.setBackground(bgColor);
		tabbedPaneMain.addTab("消息认证码", null, panelMac, null);
		panelMac.setLayout(null);
		
		textFieldMacMessage = new JTextField();
		textFieldMacMessage.setColumns(10);
		textFieldMacMessage.setBounds(92, 22, 281, 26);
		panelMac.add(textFieldMacMessage);
		
		JLabel label_14 = new JLabel("消息/文件");
		label_14.setBounds(31, 27, 59, 16);
		panelMac.add(label_14);
		
		JButton btnMacSelect = new JButton("浏览");
		btnMacSelect.addActionListener(this);
		btnMacSelect.setBounds(374, 22, 75, 29);
		panelMac.add(btnMacSelect);
		
		btnMac = new JButton("计算MAC");
		btnMac.addActionListener(this);
		btnMac.setBounds(102, 272, 117, 29);
		panelMac.add(btnMac);
		
		JButton btnMacReset = new JButton("重置");
		btnMacReset.addActionListener(this);
		btnMacReset.setBounds(241, 272, 117, 29);
		panelMac.add(btnMacReset);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("浏览") || e.getActionCommand().equals("打开文件")) {
			JFileChooser fileChooser=new JFileChooser();
			if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
				workingFile=fileChooser.getSelectedFile();
				this.setFilePath(workingFile.getAbsolutePath());
			}
		}
		else if(e.getActionCommand().equals("新建密钥库")) {
			JFileChooser fileChooser=new JFileChooser();
			if(fileChooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
				keystoreFile=fileChooser.getSelectedFile();
				SetPasswordDialog dialog=new SetPasswordDialog();
				dialog.setParent(this);
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
			}
		}
		else if(e.getActionCommand().equals("打开密钥库")) {
			JFileChooser fileChooser=new JFileChooser();
			if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
				keystoreFile=fileChooser.getSelectedFile();
				VerifyPasswordDialog dialog=new VerifyPasswordDialog();
				dialog.setParent(this);
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
			}
		}
		else if(e.getActionCommand().equals("重置")) {
			workingFile=null;
			this.setFilePath("");
			textField224Value.setText("");
			textField256Value.setText("");
			textField384Value.setText("");
			textField512Value.setText("");
			passwordFieldCheck.setText("");
			passwordFieldConfirm.setText("");
			passwordFieldSet.setText("");
		}
		else if(e.getActionCommand().equals("关闭文件")) {
			workingFile=null;
			this.setFilePath("");
			JOptionPane.showMessageDialog(this,"文件已关闭","提示",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getActionCommand().equals("关闭密钥库")) {
			keystoreFile=null;
			if(ksmanager!=null) {
				try {
					ksmanager.clean();
				} catch (Exception exc) {
					exc.printStackTrace();
					JOptionPane.showMessageDialog(this,exc.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
				}
			}
			JOptionPane.showMessageDialog(this,"密钥库已关闭","提示",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource()==btnEncrypt) {	
			if(workingFile==null) {
				JOptionPane.showMessageDialog(this,"请选择文件","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			char[] password=passwordFieldSet.getPassword();
			char[] passwordConfirm=passwordFieldConfirm.getPassword();
			if(password.length<6 || password.length>16) {
				JOptionPane.showMessageDialog(this,"密码长度必须介于6-16位之间","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(!Arrays.equals(password, passwordConfirm)) {
				JOptionPane.showMessageDialog(this,"两次密码输入不一致","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			int keyLength=(sliderEncryptMode.getValue()/2+1)*16;
			EncryptCore core=EncryptCore.getInstance(passwordConfirm, keyLength);
			try {
				core.encryptFile(workingFile);
				core.clean();
				JOptionPane.showMessageDialog(this,"加密完成","温馨提示",JOptionPane.INFORMATION_MESSAGE);
			} 
			catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(this,exc.toString(),"内部错误",JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource()==btnDecrypt) {
			if(workingFile==null) {
				JOptionPane.showMessageDialog(this,"请选择文件","错误",JOptionPane.ERROR_MESSAGE);
				return;
			}
			JFileChooser fileChooser=new JFileChooser();
			File output;
			if(fileChooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
				output=fileChooser.getSelectedFile();
			}
			else {
				return;
			}
			char[] password=passwordFieldCheck.getPassword();
			DecryptCore core=DecryptCore.getInstance(password);
			try {
				core.decryptFile(workingFile,output);
				core.clean();
				JOptionPane.showMessageDialog(this,"解密完成","温馨提示",JOptionPane.INFORMATION_MESSAGE);
			} 
			catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(this,exc.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource()==checkBoxIsVerify) {
			if(checkBoxIsVerify.isSelected()) {
				btnSign.setText("开始验证");
				textFieldSignValue.setEditable(true);
			}
			else {
				btnSign.setText("开始签名");
				textFieldSignValue.setEditable(false);
			}
		}
		else if(e.getSource()==btnSign) {
			
		}
		else {
			System.out.println(e.getActionCommand());
		}
	}
	public void setFilePath(String path) {
		textFieldEncryptFile.setText(path);
		textFieldDecryptFile.setText(path);
		textFieldSignFile.setText(path);
		textFieldHashMessage.setText(path);
		textFieldMacMessage.setText(path);
	}

	public void didVerifyPasswordDialogOkButtonClicked(Object sender, char[] password) {
		try {
			ksmanager=KeyStoreManager.getInstance();
			System.out.println(password);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
		}
	}

	public void didSetPasswordDialogOkButtonClicked(Object sender, char[] password) {
		System.out.println(password);
	}
}
