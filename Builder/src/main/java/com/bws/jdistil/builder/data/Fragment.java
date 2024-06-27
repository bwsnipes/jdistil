package com.bws.jdistil.builder.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fragment")
@XmlAccessorType (XmlAccessType.PROPERTY)
public class Fragment {

	private String name;
	private String parentName;
	private Boolean isPaginationSupported = Boolean.FALSE;
	private Integer pageSize;
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	public Fragment() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Boolean getIsPaginationSupported() {
		return isPaginationSupported;
	}

	public void setIsPaginationSupported(Boolean isPaginationSupported) {
		this.isPaginationSupported = isPaginationSupported;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@XmlElementWrapper(name="attributes")
	@XmlElement(name="attribute")
	public List<Attribute> getAttributes() {
		
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		
		this.attributes.clear();
		
		if (attributes != null) {
			this.attributes.addAll(attributes);
		}
	}
	
	public Fragment copy() {
		
		Fragment copy = new Fragment();
		
		copy.setName(getName());
		copy.setParentName(getParentName());
		copy.setIsPaginationSupported(getIsPaginationSupported());
		copy.setPageSize(getPageSize());
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute attribute : getAttributes()) {

			attributes.add(attribute.copy());
		}
		
		copy.setAttributes(attributes);
		
		return copy;
	}
	
}
