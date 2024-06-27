package com.bws.jdistil.builder.data;

public enum AssociationType {
	MANY_TO_ONE("Many to One"), 
	MANY_TO_MANY("Many to Many");
	
    private final String name;       

    private AssociationType(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
       return this.name;
    }
}
