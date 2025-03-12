package com.Batman.enums;

public enum OrderType {

	GAME("game"),
	SUBSCRIPTION("subscription"),
    EVENT("event");


	private final String type;

	OrderType(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
