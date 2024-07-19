package com.bws.jdistil.builder.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "project")
@XmlAccessorType (XmlAccessType.PROPERTY)
public class Project {

	private String name;
	private String basePackageName;
	private LocalDateTime created;
	private LocalDateTime updated;
	private LocalDateTime lastGenerated;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private List<Relationship> relationships = new ArrayList<Relationship>();

	public Project() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	@XmlJavaTypeAdapter(DateTimeAdapter.class)
	public LocalDateTime getLastGenerated() {
		return lastGenerated;
	}

	public void setLastGenerated(LocalDateTime lastGenerated) {
		this.lastGenerated = lastGenerated;
	}

	public String getBasePackageName() {
		return basePackageName;
	}

	public void setBasePackageName(String basePackageName) {
		this.basePackageName = basePackageName;
	}

	@XmlElementWrapper(name="fragments")
	@XmlElement(name="fragment")
	public List<Fragment> getFragments() {
		return fragments;
	}

	public void setFragments(List<Fragment> fragments) {
		
		this.fragments.clear();
		
		if (fragments != null) {
			this.fragments.addAll(fragments);
		}
	}

	@XmlElementWrapper(name="relationships")
	@XmlElement(name="relationship")
	public List<Relationship> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<Relationship> relationships) {
		
		this.relationships.clear();
		
		if (relationships != null) {
			this.relationships.addAll(relationships);
		}
	}
	
	public List<Fragment> getDependentFragments(String fragmentName) {
		
		return fragments.stream().filter(f -> f.getParentName() != null && f.getParentName().equalsIgnoreCase(fragmentName)).toList();
	}
	
	public List<Relationship> getSourceFragmentRelationships(String fragmentName) {
		
		return relationships.stream().filter(r -> r.getSourceFragmentName().equalsIgnoreCase(fragmentName)).toList();
	}

	public List<Relationship> getTargetFragmentRelationships(String fragmentName) {
		
		return relationships.stream().filter(r -> r.getTargetFragmentName().equalsIgnoreCase(fragmentName)).toList();
	}

}
