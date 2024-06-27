package com.bws.jdistil.builder.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "attribute")
@XmlAccessorType (XmlAccessType.PROPERTY)
public class Attribute {

	private String name;
	private AttributeType type;
	private Boolean isRequired = Boolean.FALSE;
	private Boolean isIncludedInSearchFilter = Boolean.FALSE;
	private Boolean isIncludedInSearchResults = Boolean.FALSE;
	private Integer textMaxLength;
	private NumericType numericType;
	private Integer numericScale;
	private Integer numericPrecision;
	private String lookupCategory;
	private Boolean isLookupMultipleValues = Boolean.FALSE;

	public Attribute() {
		super();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
	
	public Boolean getIsIncludedInSearchFilter() {
		return isIncludedInSearchFilter;
	}

	public void setIsIncludedInSearchFilter(Boolean isIncludedInSearchFilter) {
		this.isIncludedInSearchFilter = isIncludedInSearchFilter;
	}

	public Boolean getIsIncludedInSearchResults() {
		return isIncludedInSearchResults;
	}

	public void setIsIncludedInSearchResults(Boolean isIncludedInSearchResults) {
		this.isIncludedInSearchResults = isIncludedInSearchResults;
	}

	public Integer getTextMaxLength() {
		return textMaxLength;
	}

	public void setTextMaxLength(Integer textMaxLength) {
		this.textMaxLength = textMaxLength;
	}

	public NumericType getNumericType() {
		return numericType;
	}

	public void setNumericType(NumericType numericType) {
		this.numericType = numericType;
	}

	public Integer getNumericScale() {
		return numericScale;
	}

	public void setNumericScale(Integer numericScale) {
		this.numericScale = numericScale;
	}

	public Integer getNumericPrecision() {
		return numericPrecision;
	}

	public void setNumericPrecision(Integer numericPrecision) {
		this.numericPrecision = numericPrecision;
	}

	public String getLookupCategory() {
		return lookupCategory;
	}

	public void setLookupCategory(String lookupCategory) {
		this.lookupCategory = lookupCategory;
	}

	public Boolean getIsLookupMultipleValues() {
		return isLookupMultipleValues;
	}

	public void setIsLookupMultipleValues(Boolean isLookupMultipleValues) {
		this.isLookupMultipleValues = isLookupMultipleValues;
	}

	public Attribute copy() {
		
		Attribute copy = new Attribute();
		
		copy.setName(getName());
		copy.setType(getType());
		copy.setIsRequired(getIsRequired());
		copy.setIsIncludedInSearchFilter(getIsIncludedInSearchFilter());
		copy.setIsIncludedInSearchResults(getIsIncludedInSearchResults());
		copy.setTextMaxLength(getTextMaxLength());
		copy.setNumericType(getNumericType());
		copy.setNumericPrecision(getNumericPrecision());
		copy.setNumericScale(getNumericScale());
		copy.setLookupCategory(getLookupCategory());
		copy.setIsLookupMultipleValues(getIsLookupMultipleValues());
		
		return copy;
	}
}
