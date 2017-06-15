package cn.xiaolulwr.isassistant.common;

public interface PasswordDialogListener {
	public void didVerifyPasswordDialogOkButtonClicked(Object sender,char[] password);
	public void didSetPasswordDialogOkButtonClicked(Object sender,char[] password);
}
