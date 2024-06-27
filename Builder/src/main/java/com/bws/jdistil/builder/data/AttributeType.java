package com.bws.jdistil.builder.data;

public enum AttributeType {
	TEXT("Text"), 
	NUMERIC("Numeric"), 
	BOOLEAN("Boolean"), 
	DATE("Date"), 
	TIME("Time"), 
	LOOKUP("Lookup"), 
	PHONE("Phone"), 
	EMAIL("Email");
	
    private final String name;       

    private AttributeType(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
}
