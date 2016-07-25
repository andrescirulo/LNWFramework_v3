package net.latin.client.widget.msg;

/**
 * Message
 * @author Fernando Diaz
 *
 */
public class GwtMsg {
	
	private static final int DEFAULT_TTL = 1;
	private String text;
	private int type;
	
	/**
	 * Time to live
	 */
	private int ttl;
	
	
	public GwtMsg() {
	}
	
	public GwtMsg( int type,String message) {
		this.type = type;
		this.text = message;
		this.ttl = DEFAULT_TTL;
	}
	public GwtMsg( int type,String message, int ttl) {
		this.type = type;
		this.text = message;
		this.ttl = ttl;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public void reduce() {
		this.ttl--;
	}

	public boolean isAlive() {
		return this.getTtl() > 0;
	}
}
