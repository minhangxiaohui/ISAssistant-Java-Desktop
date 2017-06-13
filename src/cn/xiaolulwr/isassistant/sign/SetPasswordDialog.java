package cn.xiaolulwr.isassistant.sign;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.xiaolulwr.isassistant.common.ParentFrameInterface;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;

public class SetPasswordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private ParentFrameInterface parent;
	
	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordFieldSet;
	private JPasswordField passwordFieldConfirm;


	/**
	 * Create the dialog.
	 */
	public SetPasswordDialog() {
		setTitle("设置密码");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 200);
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
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("好");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						char[] password=passwordFieldSet.getPassword();
						char[] passwordConfirm=passwordFieldConfirm.getPassword();
						if(password.length<6 || password.length>16) {
							JOptionPane.showMessageDialog(null,"密码长度必须介于6-16位之间","错误",JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(!Arrays.equals(password, passwordConfirm)) {
							JOptionPane.showMessageDialog(null,"两次密码输入不一致","错误",JOptionPane.ERROR_MESSAGE);
							return;
						}
						dispose();
						parent.didSetPasswordDialogOkButtonClicked(this, passwordConfirm);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("取消");
				cancelButton.addActionListener(new ActionListener() {
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

	public void setParent(ParentFrameInterface parent) {
		this.parent = parent;
	}
}
