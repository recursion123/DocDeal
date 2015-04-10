package com.gtrj.docdeal.bean;

public class ContactItem {
	private String name;
	private String number;
	private String alpha;
    private String id;

	public ContactItem() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public ContactItem(String name, String number, String alpha) {
		super();
		this.name = name;
		this.number = number;
		this.alpha = alpha;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
