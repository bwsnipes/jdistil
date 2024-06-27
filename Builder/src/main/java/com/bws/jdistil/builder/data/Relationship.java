package com.bws.jdistil.builder.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "relationship")
@XmlAccessorType (XmlAccessType.PROPERTY)
public class Relationship {

	private String sourceFragmentName;
	private String sourceAttributeName;
	private Boolean isSourceIncludedInView = Boolean.FALSE;
	private AssociationType association;
	private Boolean isBidirectional = Boolean.FALSE;
	private String targetFragmentName;
	private String targetAttributeName;
	private Boolean isTargetRequired;
	private Boolean isTargetIncludedInView;

	public Relationship() {
		super();
	}

	public String getSourceFragmentName() {
		return sourceFragmentName;
	}

	public void setSourceFragmentName(String sourceFragmentName) {
		this.sourceFragmentName = sourceFragmentName;
	}

	public String getSourceAttributeName() {
		return sourceAttributeName;
	}

	public void setSourceAttributeName(String sourceAttributeName) {
		this.sourceAttributeName = sourceAttributeName;
	}

	public Boolean getIsSourceIncludedInView() {
		return isSourceIncludedInView;
	}

	public void setIsSourceIncludedInView(Boolean isSourceIncludedInView) {
		this.isSourceIncludedInView = isSourceIncludedInView;
	}

	public AssociationType getAssociation() {
		return association;
	}

	public void setAssociation(AssociationType association) {
		this.association = association;
	}

	public Boolean getIsBidirectional() {
		return isBidirectional;
	}

	public void setIsBidirectional(Boolean isBidirectional) {
		this.isBidirectional = isBidirectional;
	}

	public String getTargetFragmentName() {
		return targetFragmentName;
	}

	public void setTargetFragmentName(String targetFragmentName) {
		this.targetFragmentName = targetFragmentName;
	}

	public String getTargetAttributeName() {
		return targetAttributeName;
	}

	public void setTargetAttributeName(String targetAttributeName) {
		this.targetAttributeName = targetAttributeName;
	}

	public Boolean getIsTargetRequired() {
		return isTargetRequired;
	}

	public void setIsTargetRequired(Boolean isTargetRequired) {
		this.isTargetRequired = isTargetRequired;
	}

	public Boolean getIsTargetIncludedInView() {
		return isTargetIncludedInView;
	}

	public void setIsTargetIncludedInView(Boolean isTargetIncludedInView) {
		this.isTargetIncludedInView = isTargetIncludedInView;
	}

	public static String createReferenceName(String sourceName, String targetName) {
		
		// Ensure names are not null
		if (sourceName == null) {
			sourceName = "";
		}
		if (targetName == null) {
			targetName = "";
		}
		
		// Build unique reference name
		String uniqueReferenceName = sourceName.concat(".").concat(targetName);

		return uniqueReferenceName;
	}
	
	public Relationship copy() {
		
		Relationship copy = new Relationship();
		
		copy.setSourceFragmentName(getSourceFragmentName());
		copy.setSourceAttributeName(getSourceAttributeName());
		copy.setIsSourceIncludedInView(getIsSourceIncludedInView());
		copy.setAssociation(getAssociation());
		copy.setIsBidirectional(getIsBidirectional());
		copy.setTargetFragmentName(getTargetFragmentName());
		copy.setTargetAttributeName(getTargetAttributeName());
		copy.setIsTargetRequired(getIsTargetRequired());
		copy.setIsTargetIncludedInView(getIsTargetIncludedInView());
		
		return copy;
	}
	
}
