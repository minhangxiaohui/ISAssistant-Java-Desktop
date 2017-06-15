package cn.xiaolulwr.isassistant.common;

/**
 * 验证密码输入窗口的回调方法
 * @author 路伟饶
 *
 */
public interface VerifyPasswordDialogListener {
	/**
	 * 监听器的回调方法，在验证密码之后调用
	 * @param sender
	 * 回调消息发送者
	 * @param password
	 * 密码输入窗口送回的密码
	 */
	public void didVerifyPasswordDialogOkButtonClicked(Object sender,char[] password);
}
