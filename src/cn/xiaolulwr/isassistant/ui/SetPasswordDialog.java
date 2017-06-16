package cn.xiaolulwr.isassistant.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.xiaolulwr.isassistant.common.SetPasswordDialogListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;
/**
 * 设置密码对话框
 * @author 路伟饶
 *
 */
public class SetPasswordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private SetPasswordDialogListener listener;
	
	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordFieldSet;
	private JPasswordField passwordFieldConfirm;
	/**
	 * 构造器
	 */
	public SetPasswordDialog() {
		setTitle("设置密码");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 224);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("密码");
		lblNewLabel.setBounds(32, 36, 36, 16);
		contentPanel.add(lblNewLabel);
		
		passwordFieldSet = new JPasswordField();
		passwordFieldSet.setBounds(69, 31, 193, 26);
		contentPanel.add(passwordFieldSet);
		
		passwordFieldConfirm = new JPasswordField();
		passwordFieldConfirm.setBounds(69, 85, 193, 26);
		contentPanel.add(passwordFieldConfirm);
		
		JLabel label = new JLabel("确认");
		label.setBounds(32, 90, 36, 16);
		contentPanel.add(label);
		
		JLabel lblyiShi = new JLabel("警告：如果密码遗失，您的数据将丢失。");
		lblyiShi.setBounds(33, 130, 234, 16);
		contentPanel.add(lblyiShi);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("好");
				okButton.addActionListener(new ActionListener() {
					/**
					 * 点击好按钮回调方法
					 */
					public void actionPerformed(ActionEvent e) {
						char[] password=passwordFieldSet.getPassword();
						char[] passwordConfirm=passwordFieldConfirm.getPassword();
						if(password.length<6 || password.length>16) {
							setAlwaysOnTop(false);
							JOptionPane.showMessageDialog(null,"密码长度必须介于6-16位之间","错误",JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(!Arrays.equals(password, passwordConfirm)) {
							setAlwaysOnTop(false);
							JOptionPane.showMessageDialog(null,"两次密码输入不一致","错误",JOptionPane.ERROR_MESSAGE);
							return;
						}
						dispose();
//						回调方法，回传输入的密码
						listener.didSetPasswordDialogOkButtonClicked(this, passwordConfirm);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.addActionListener(new ActionListener() {
					/**
					 * 点击取消按钮回调方法
					 */
					public void actionPerformed(ActionEvent e) {
						passwordFieldSet.setText("");
						passwordFieldConfirm.setText("");
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	/**
	 * 设置监听器
	 * @param listener
	 * 监听器接口类
	 */
	public void addListener(SetPasswordDialogListener listener) {
		this.listener = listener;
	}
}
