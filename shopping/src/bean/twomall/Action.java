package bean.twomall;

public class Action {
	private boolean ActionType;
	private String page;
	private String message;
	
	public boolean isActionType() {
		return ActionType;
	}
	public void setActionType(boolean actionType) {
		ActionType = actionType;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
