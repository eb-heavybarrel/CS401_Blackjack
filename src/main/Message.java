package main;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;  //Eclipse suggests we need this
	protected MessageClass mClass;
	protected MessageType mType;
	protected MessageStatus mStatus;
	protected List<Object> mData; //
	
	public Message(MessageClass mClass, MessageType mType, List<Object> mData) {
		this.mClass= mClass;
		this.mType = mType;
		this.mStatus = MessageStatus.UNDEFINED;
		this.mData= mData;
	}
	
	public Message(MessageClass mClass, MessageType mType, MessageStatus mStatus, List<Object> mData) {
		this.mClass= mClass;
		this.mType = mType;
		this.mStatus = mStatus;
		this.mData = mData;
	}

	public MessageClass getmClass() {
		return mClass;
	}
	
	public MessageType getmType() {
		return mType;
	}

	public MessageStatus getmStatus() {
		return mStatus;
	}

	public List<Object> getmData() {
		return mData;
	}
	
	
}
