package cn.xiaolulwr.isassistant.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import cn.xiaolulwr.isassistant.common.VerifyPasswordDialogListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * 验证密码对话框
 * @author 路伟饶
 *
 */
public class VerifyPasswordDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private VerifyPasswordDialogListener listener;

	private final JPanel contentPanel = new JPanel();
	private JPasswordField passwordFieldVerify;
	/**
	 * 构造器
	 */
	public VerifyPasswordDialog() {
		setTitle("验证密码");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("密码");
		lblNewLabel.setBounds(32, 36, 36, 16);
		contentPanel.add(lblNewLabel);
		{
			passwordFieldVerify = new JPasswordField();
			passwordFieldVerify.setBounds(68, 31, 193, 26);
			contentPanel.add(passwordFieldVerify);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("好");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						listener.didVerifyPasswordDialogOkButtonClicked(this, passwordFieldVerify.getPassword());
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
						passwordFieldVerify.setText("");
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
	public void addListener(VerifyPasswordDialogListener listener) {
		this.listener = listener;
	}
}
