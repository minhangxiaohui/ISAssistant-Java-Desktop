package cn.xiaolulwr.isassistant.common;

/**
 * 设置密码输入窗口的监听器
 * @author 路伟饶
 *
 */
public interface SetPasswordDialogListener {
	/**
	 * 监听器的回调方法，在设置好密码并校验过两次输入相同之后调用
	 * @param sender
	 * 回调消息发送者
	 * @param password
	 * 密码输入窗口送回的密码
	 */
	public void didSetPasswordDialogOkButtonClicked(Object sender,char[] password);
}
