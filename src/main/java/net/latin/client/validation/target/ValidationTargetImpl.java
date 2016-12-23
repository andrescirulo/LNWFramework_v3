package net.latin.client.validation.target;

public class ValidationTargetImpl extends ValidationTarget {

	private ValidationTargetListener listener;

	public ValidationTargetImpl(ValidationTargetListener listener) {
		this.listener = listener;
	}
	
	public void message( String errorMsg ) {
		if (isOk()) {
			if (!listener.isValid()) {
				this.errorMsg = errorMsg;
			}
		}
	}
	
}
