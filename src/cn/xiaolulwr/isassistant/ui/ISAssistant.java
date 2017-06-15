package cn.xiaolulwr.isassistant.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.Security;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import cn.xiaolulwr.isassistant.common.DigitalSignatureAlgorithm;
import cn.xiaolulwr.isassistant.common.HashAlgorithm;
import cn.xiaolulwr.isassistant.common.KeyStoreManager;
import cn.xiaolulwr.isassistant.common.SetPasswordDialogListener;
import cn.xiaolulwr.isassistant.common.VerifyPasswordDialogListener;
import cn.xiaolulwr.isassistant.common.HmacAlgorithm;
import cn.xiaolulwr.isassistant.crypto.CryptoCore;
import cn.xiaolulwr.isassistant.hash.DigestCore;
import cn.xiaolulwr.isassistant.mac.MacCore;
import cn.xiaolulwr.isassistant.sign.DigitalSignCore;


public class ISAssistant extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private File workingFile,keystoreFile,outputFile;
	private KeyStoreManager ksmanager;
	private JPanel contentPane;
	private JTextField textFieldCryptoFile;
	private JTextField textFieldSignFile;
	private JTextField textFieldSignValue;
	private JTextField textFieldHashMessage;
	private JTextField textFieldMacMessage;
	private JTextField textField224Value;
	private JTextField textField256Value;
	private JTextField textField384Value;
	private JTextField textField512Value;
	private JButton btnCrypto;
	private JButton btnSign;
	private JButton btnHash;
	private JButton btnMac;
	private JSlider sliderEncryptMode;
	private JCheckBox checkBoxIsVerify;
	private JComboBox<HashAlgorithm> comboBoxHash;
	private JComboBox<DigitalSignatureAlgorithm> comboBoxDigitalSign;
	private JCheckBox checkBoxIsUseSha3;
	private JLabel labelHash224;
	private JLabel labelHash256;
	private JLabel labelHash384;
	private JLabel labelHash512;
	private JTextField textFieldMacValue;
	private JComboBox<HmacAlgorithm> comboBoxMacMode;
	private JCheckBox checkBoxIsVerifyMac;
	private JTextField textFieldSaveFile;
	private JButton btnCryptoSave;
	private JButton btnCryptoSelect;
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

	public ISAssistant() {
		setResizable(false);
		setTitle("信息安全助手");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 423);
		setLocationRelativeTo(null);
		
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
		tabbedPaneMain.setBounds(6, 6, 490, 370);
		contentPane.add(tabbedPaneMain);
		
		Color bgColor=new Color(229, 229, 229);
		
		JPanel panelCrypto = new JPanel();
		panelCrypto.setBackground(bgColor);
		tabbedPaneMain.addTab("文件加密", null, panelCrypto, null);
		tabbedPaneMain.setEnabledAt(0, true);
		panelCrypto.setLayout(null);
		
		textFieldCryptoFile = new JTextField();
		textFieldCryptoFile.setEditable(false);
		textFieldCryptoFile.setBounds(92, 22, 281, 26);
		panelCrypto.add(textFieldCryptoFile);
		textFieldCryptoFile.setColumns(10);
		
		JLabel label = new JLabel("选择文件");
		label.setBounds(34, 27, 61, 16);
		panelCrypto.add(label);
		
		btnCryptoSelect = new JButton("浏览");
		btnCryptoSelect.addActionListener(this);
		btnCryptoSelect.setBounds(374, 22, 75, 29);
		panelCrypto.add(btnCryptoSelect);
		
		btnCrypto = new JButton("开始加密");
		btnCrypto.addActionListener(this);
		btnCrypto.setBounds(102, 272, 117, 29);
		panelCrypto.add(btnCrypto);
		
		sliderEncryptMode = new JSlider();
		sliderEncryptMode.setSnapToTicks(true);
		sliderEncryptMode.setPaintLabels(true);
		sliderEncryptMode.setValue(1);
		sliderEncryptMode.setMaximum(2);
		sliderEncryptMode.setBounds(133, 84, 190, 29);
		panelCrypto.add(sliderEncryptMode);
		
		JLabel lblNewLabel = new JLabel("加密强度");
		lblNewLabel.setBounds(78, 92, 61, 16);
		panelCrypto.add(lblNewLabel);
		
		JLabel lblaes = new JLabel("一般");
		lblaes.setToolTipText("使用AES-128");
		lblaes.setBounds(143, 116, 34, 16);
		panelCrypto.add(lblaes);
		
		JLabel label_4 = new JLabel("强");
		label_4.setToolTipText("使用AES");
		label_4.setBounds(222, 116, 13, 16);
		panelCrypto.add(label_4);
		
		JLabel label_5 = new JLabel("很强");
		label_5.setBounds(289, 116, 34, 16);
		panelCrypto.add(label_5);
		
		JButton btnEncryptReset = new JButton("重置");
		btnEncryptReset.addActionListener(this);
		btnEncryptReset.setBounds(241, 272, 117, 29);
		panelCrypto.add(btnEncryptReset);
		
		textFieldSaveFile = new JTextField();
		textFieldSaveFile.setEditable(false);
		textFieldSaveFile.setColumns(10);
		textFieldSaveFile.setBounds(92, 170, 281, 26);
		panelCrypto.add(textFieldSaveFile);
		
		btnCryptoSave = new JButton("位置");
		btnCryptoSave.addActionListener(this);
		btnCryptoSave.setBounds(374, 170, 75, 29);
		panelCrypto.add(btnCryptoSave);
		
		JLabel label_1 = new JLabel("保存位置");
		label_1.setBounds(34, 175, 61, 16);
		panelCrypto.add(label_1);
		
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
		textFieldSignValue.setEditable(false);
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
		
		btnHash = new JButton("开始计算");
		btnHash.addActionListener(this);
		btnHash.setBounds(102, 272, 117, 29);
		panelHash.add(btnHash);
		
		JButton btnHashReset = new JButton("重置");
		btnHashReset.addActionListener(this);
		btnHashReset.setBounds(241, 272, 117, 29);
		panelHash.add(btnHashReset);
		
		textField224Value = new JTextField();
		textField224Value.setEditable(false);
		textField224Value.setBounds(92, 96, 281, 26);
		panelHash.add(textField224Value);
		textField224Value.setColumns(10);
		
		textField256Value = new JTextField();
		textField256Value.setEditable(false);
		textField256Value.setColumns(10);
		textField256Value.setBounds(92, 134, 281, 26);
		panelHash.add(textField256Value);
		
		textField384Value = new JTextField();
		textField384Value.setEditable(false);
		textField384Value.setColumns(10);
		textField384Value.setBounds(92, 172, 281, 26);
		panelHash.add(textField384Value);
		
		textField512Value = new JTextField();
		textField512Value.setEditable(false);
		textField512Value.setColumns(10);
		textField512Value.setBounds(92, 210, 281, 26);
		panelHash.add(textField512Value);
		
		labelHash224 = new JLabel("SHA-224");
		labelHash224.setBounds(21, 101, 69, 16);
		panelHash.add(labelHash224);
		
		labelHash256 = new JLabel("SHA-256");
		labelHash256.setBounds(21, 139, 69, 16);
		panelHash.add(labelHash256);
		
		labelHash384 = new JLabel("SHA-384");
		labelHash384.setBounds(21, 177, 69, 16);
		panelHash.add(labelHash384);
		
		labelHash512 = new JLabel("SHA-512");
		labelHash512.setBounds(21, 215, 69, 16);
		panelHash.add(labelHash512);
		
		checkBoxIsUseSha3 = new JCheckBox("使用SHA-3算法");
		checkBoxIsUseSha3.addActionListener(this);
		checkBoxIsUseSha3.setBounds(170, 60, 128, 23);
		panelHash.add(checkBoxIsUseSha3);
		
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
		
		textFieldMacValue = new JTextField();
		textFieldMacValue.setEditable(false);
		textFieldMacValue.setBounds(92, 135, 281, 26);
		panelMac.add(textFieldMacValue);
		textFieldMacValue.setColumns(10);
		
		comboBoxMacMode = new JComboBox<HmacAlgorithm>();
		comboBoxMacMode.setModel(new DefaultComboBoxModel<HmacAlgorithm>(HmacAlgorithm.values()));
		comboBoxMacMode.setBounds(163, 78, 142, 27);
		panelMac.add(comboBoxMacMode);
		
		JLabel lblMac = new JLabel("MAC");
		lblMac.setBounds(44, 140, 48, 16);
		panelMac.add(lblMac);
		
		checkBoxIsVerifyMac = new JCheckBox("验证MAC");
		checkBoxIsVerifyMac.addActionListener(this);
		checkBoxIsVerifyMac.setBounds(374, 136, 87, 23);
		panelMac.add(checkBoxIsVerifyMac);
		
		JLabel lblMac_1 = new JLabel("MAC算法");
		lblMac_1.setBounds(102, 82, 61, 16);
		panelMac.add(lblMac_1);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("浏览") || e.getActionCommand().equals("打开文件")) {
			this.selectWorkingFile();
		}
		else if(e.getActionCommand().equals("新建密钥库")) {
			this.createKeyStore();
		}
		else if(e.getActionCommand().equals("打开密钥库")) {
			this.openKeyStore();
		}
		else if(e.getActionCommand().equals("重置")) {
			this.reset();
		}
		else if(e.getActionCommand().equals("关闭文件")) {
			this.closeFile();
		}
		else if(e.getActionCommand().equals("关闭密钥库")) {
			this.closeKeyStore();
		}
		else if(e.getSource()==btnCrypto) {	
			this.cryptoFile();
		}
		else if(e.getSource()==btnCryptoSave) {
			this.saveFile();
		}
		else if(e.getSource()==checkBoxIsVerify) {
			this.verifyModeChanged();
		}
		else if(e.getSource()==btnSign) {
			this.signFile();
		}
		else if(e.getSource()==checkBoxIsUseSha3) {
			this.hashModeChanged();
		}
		else if(e.getSource()==btnHash) {
			this.getHashValue();
		}
		else if(e.getSource()==checkBoxIsVerifyMac) {
			this.verifyMacModeChanged();
		}
		else if(e.getSource()==btnMac) {
			this.getMac();
		}
		else {
			System.out.println(e.getActionCommand());
		}
	}
	
	public void selectWorkingFile() {
		JFileChooser fileChooser=new JFileChooser();
		if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			workingFile=fileChooser.getSelectedFile();
			this.setFilePath(workingFile.getAbsolutePath());
			if(workingFile.getName().endsWith(".xiaolucrypto")) {
				btnCrypto.setText("开始解密");
				sliderEncryptMode.setEnabled(false);
			}
			else {
				btnCrypto.setText("开始加密");
				sliderEncryptMode.setEnabled(true);
			}
			textFieldHashMessage.setEditable(false);
			textFieldMacMessage.setEditable(false);
		}
	}
	public void saveFile() {
		JFileChooser fileChooser=new JFileChooser();
		FileFilter filter=new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) {
					return true;
				}
				else {
					String fileName=pathname.getName();
					if(btnCrypto.getText().equals("开始加密")) {
						if(fileName.endsWith(".xiaolucrypto")) {
							return true;
						}
						else {
							return false;
						}
					}
					else if(btnCrypto.getText().equals("开始解密")) {
						return true;
					}
					else {
						return false;
					}
				}
			}
			@Override
			public String getDescription() {
				if(btnCrypto.getText().equals("开始加密")) {
					return new String("加密文件(*.xiaolucrypto)");
				}
				else if(btnCrypto.getText().equals("开始解密")) {
					return null;
				}
				else {
					return null;
				}
				
			}
		};
		fileChooser.setFileFilter(filter);
		if(fileChooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
			outputFile=fileChooser.getSelectedFile();
			if(!outputFile.getName().endsWith(".xiaolucrypto") && btnCrypto.getText().equals("开始加密")) {
				outputFile=new File(outputFile.getAbsolutePath()+".xiaolucrypto");
			}
			textFieldSaveFile.setText(outputFile.getAbsolutePath());
		}
	}
	public void createKeyStore() {
		JFileChooser fileChooser=new JFileChooser();
		FileFilter filter=new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) {
					return true;
				}
				else {
					String fileName=pathname.getName();
					if(fileName.endsWith(".keystore")) {
						return true;
					}
					else {
						return false;
					}
				}
			}
			@Override
			public String getDescription() {
				return new String("密钥库文件(*.keystore)");
			}
		};
		fileChooser.setFileFilter(filter);
		if(fileChooser.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
			keystoreFile=fileChooser.getSelectedFile();
			if(!keystoreFile.getName().endsWith(".keystore")) {
				keystoreFile=new File(keystoreFile.getAbsolutePath()+".keystore");
			}
			SetPasswordDialog dialog=new SetPasswordDialog();
			dialog.addListener(new SetPasswordDialogListener() {
				public void didSetPasswordDialogOkButtonClicked(Object sender, char[] password) {
					try {
						ksmanager=KeyStoreManager.getInstance();
						ksmanager.createKeyStore(keystoreFile, password);
						JOptionPane.showMessageDialog(null,"密钥库已创建","提示",JOptionPane.INFORMATION_MESSAGE);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
	}
	public void openKeyStore() {
		JFileChooser fileChooser=new JFileChooser();
		FileFilter filter=new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) {
					return true;
				}
				else {
					String fileName=pathname.getName();
					if(fileName.endsWith(".keystore")) {
						return true;
					}
					else {
						return false;
					}
				}
			}
			@Override
			public String getDescription() {
				return new String("密钥库文件(*.keystore)");
			}
		};
		fileChooser.setFileFilter(filter);
		if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION) {
			keystoreFile=fileChooser.getSelectedFile();
			VerifyPasswordDialog dialog=new VerifyPasswordDialog();
			dialog.addListener(new VerifyPasswordDialogListener() {
				public void didVerifyPasswordDialogOkButtonClicked(Object sender, char[] password) {
					try {
						ksmanager=KeyStoreManager.getInstance();
						ksmanager.openKeyStoreFromFile(keystoreFile, password);
						JOptionPane.showMessageDialog(null,"文件已加载","提示",JOptionPane.INFORMATION_MESSAGE);
					}
					catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
	}
	public void reset() {
		workingFile=null;
		this.setFilePath("");
		sliderEncryptMode.setEnabled(true);
		textField224Value.setText("");
		textField256Value.setText("");
		textField384Value.setText("");
		textField512Value.setText("");
		textFieldSignValue.setText("");
		textFieldMacMessage.setText("");
	}
	public void setFilePath(String path) {
		textFieldCryptoFile.setText(path);
		textFieldSignFile.setText(path);
		textFieldHashMessage.setText(path);
		textFieldMacMessage.setText(path);
	}
	public void closeFile() {
		workingFile=null;
		this.setFilePath("");
		textFieldHashMessage.setEnabled(true);
		textFieldMacMessage.setEnabled(true);
		JOptionPane.showMessageDialog(this,"文件已关闭","提示",JOptionPane.INFORMATION_MESSAGE);
	}
	public void closeKeyStore() {
		keystoreFile=null;
		ksmanager=null;
		JOptionPane.showMessageDialog(this,"密钥库已关闭","提示",JOptionPane.INFORMATION_MESSAGE);
	}
	public void cryptoFile() {
		if(workingFile==null) {
			JOptionPane.showMessageDialog(this,"请选择文件","错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(outputFile==null) {
			JOptionPane.showMessageDialog(this,"请选择保存位置","错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(btnCrypto.getText().equals("开始加密")) {
			SetPasswordDialog dialog=new SetPasswordDialog();
			dialog.addListener(new SetPasswordDialogListener() {
				public void didSetPasswordDialogOkButtonClicked(Object sender, char[] password) {
					int keyLength=(sliderEncryptMode.getValue()/2+1)*16;
					CryptoCore core=CryptoCore.getInstance();
					try {
						core.encryptFile(workingFile, password, keyLength, outputFile);
						core.clean();
						JOptionPane.showMessageDialog(null,"加密完成","提示",JOptionPane.INFORMATION_MESSAGE);
					} 
					catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
		else if(btnCrypto.getText().equals("开始解密")) {
			VerifyPasswordDialog dialog=new VerifyPasswordDialog();
			dialog.addListener(new VerifyPasswordDialogListener() {
				public void didVerifyPasswordDialogOkButtonClicked(Object sender, char[] password) {
					CryptoCore core=CryptoCore.getInstance();
					try {
						core.decryptFile(workingFile, password, outputFile);
						core.clean();
						JOptionPane.showMessageDialog(null,"解密完成","提示",JOptionPane.INFORMATION_MESSAGE);
					} 
					catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
	}
	public void verifyModeChanged() {
		if(checkBoxIsVerify.isSelected()) {
			btnSign.setText("开始验证");
			textFieldSignValue.setEditable(true);
		}
		else {
			btnSign.setText("开始签名");
			textFieldSignValue.setEditable(false);
		}
	}
	public void signFile() {
		if(workingFile==null) {
			JOptionPane.showMessageDialog(this,"请选择文件","错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(keystoreFile==null || ksmanager==null) {
			JOptionPane.showMessageDialog(this,"未选择密钥库","错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		String hashAlgorithm=comboBoxHash.getSelectedItem().toString();
		String digitalSign=comboBoxDigitalSign.getSelectedItem().toString();
		String algorithm=hashAlgorithm+"with"+digitalSign;
		try {
			if(btnSign.getText().equals("开始签名")) {
				DigitalSignCore core=DigitalSignCore.getInstance(algorithm, ksmanager);
				String signValue=core.signFile(workingFile);
				textFieldSignValue.setText(signValue);
				JOptionPane.showMessageDialog(this,"签名完成","提示",JOptionPane.INFORMATION_MESSAGE);
			}
			else if(btnSign.getText().equals("开始验证")) {
				DigitalSignCore core=DigitalSignCore.getInstance(algorithm, ksmanager);
				boolean isSigned=core.verifyFile(workingFile,textFieldSignValue.getText());
				if(isSigned==true) {
					JOptionPane.showMessageDialog(this,"签名验证通过","提示",JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					throw new Exception("签名验证失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
			if(e.getMessage().equals("密钥库不合法或密码错误")) {
				this.openKeyStore();
				this.signFile();
			}
		}
	}
	public void hashModeChanged() {
		if(checkBoxIsUseSha3.isSelected()==true) {
			labelHash224.setText("SHA3-224");
			labelHash256.setText("SHA3-256");
			labelHash384.setText("SHA3-384");
			labelHash512.setText("SHA3-512");
		}
		else {
			labelHash224.setText("SHA-224");
			labelHash256.setText("SHA-256");
			labelHash384.setText("SHA-384");
			labelHash512.setText("SHA-512");
		}
	}
	public void getHashValue() {
		try {
			String algorithm;
			DigestCore core;
			if(btnHash.getText().equals("开始计算")) {
				if(workingFile==null) {
					algorithm=labelHash224.getText();
					core=DigestCore.getInstance(algorithm);
					textField224Value.setText(core.getHashValue(textFieldHashMessage.getText()));
					algorithm=labelHash256.getText();
					core=DigestCore.getInstance(algorithm);
					textField256Value.setText(core.getHashValue(textFieldHashMessage.getText()));
					algorithm=labelHash384.getText();
					core=DigestCore.getInstance(algorithm);
					textField384Value.setText(core.getHashValue(textFieldHashMessage.getText()));
					algorithm=labelHash512.getText();
					core=DigestCore.getInstance(algorithm);
					textField512Value.setText(core.getHashValue(textFieldHashMessage.getText()));
				}
				else {
					algorithm=labelHash224.getText();
					core=DigestCore.getInstance(algorithm);
					textField224Value.setText(core.getHashValue(workingFile));
					algorithm=labelHash256.getText();
					core=DigestCore.getInstance(algorithm);
					textField256Value.setText(core.getHashValue(workingFile));
					algorithm=labelHash384.getText();
					core=DigestCore.getInstance(algorithm);
					textField384Value.setText(core.getHashValue(workingFile));
					algorithm=labelHash512.getText();
					core=DigestCore.getInstance(algorithm);
					textField512Value.setText(core.getHashValue(workingFile));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
		}
	}
	public void verifyMacModeChanged() {
		if(checkBoxIsVerifyMac.isSelected()==true) {
			btnMac.setText("验证MAC");
			textFieldMacValue.setEditable(true);
		}
		else {
			btnMac.setText("计算MAC");
			textFieldMacValue.setEditable(false);
		}
	}
	public void getMac() {
		if(btnMac.getText().equals("计算MAC")) {
			SetPasswordDialog dialog=new SetPasswordDialog();
			dialog.addListener(new SetPasswordDialogListener() {
				public void didSetPasswordDialogOkButtonClicked(Object sender, char[] password) {
					String algorithm=comboBoxMacMode.getSelectedItem().toString();
					MacCore core=MacCore.getInstance(algorithm);
					try {
						if(workingFile==null) {
							String value=core.getMac(textFieldMacMessage.getText(), password);
							textFieldMacValue.setText(value);
						}
						else {
							String value=core.getMac(workingFile, password);
							textFieldMacValue.setText(value);
						}
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
		else if(btnMac.getText().equals("验证MAC")) {
			VerifyPasswordDialog dialog=new VerifyPasswordDialog();
			dialog.addListener(new VerifyPasswordDialogListener() {
				public void didVerifyPasswordDialogOkButtonClicked(Object sender, char[] password) {
					String algorithm=comboBoxMacMode.getSelectedItem().toString();
					MacCore core=MacCore.getInstance(algorithm);
					try {
						boolean isCheck;
						if(workingFile==null) {
							isCheck=core.checkMac(textFieldMacMessage.getText(), password, textFieldMacValue.getText());
						}
						else {
							isCheck=core.checkMac(workingFile, password, textFieldMacValue.getText());
						}
						if(isCheck==true) {
							JOptionPane.showMessageDialog(null,"消息认证通过","提示",JOptionPane.INFORMATION_MESSAGE);
						}
						else {
							throw new Exception("消息认证失败");
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}
	}
}
