package main;

import java.io.Serializable;

public class Message implements Serializable {
	
	protected MessageType type;
	protected MessageStatus status;
	protected String text;
	
	public Message(MessageType type) {
		this.type = type;
		this.status = MessageStatus.UNDEFINED;
	}
	
	public Message(MessageType type, MessageStatus status, String text) {
		this.type = type;
		this.status = status;
		this.text = text;
	}
}
