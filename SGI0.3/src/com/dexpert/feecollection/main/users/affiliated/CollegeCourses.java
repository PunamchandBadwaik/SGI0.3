package com.dexpert.feecollection.main.users.affiliated;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "college_courses")
public class CollegeCourses implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer cousesId;
	private String courseName;
	@ManyToOne(targetEntity = AffBean.class)
	@JoinColumn(name = "inst_Fk", referencedColumnName = "instId")
	private AffBean affBean;

	public Integer getCousesId() {
		return cousesId;
	}

	public void setCousesId(Integer cousesId) {
		this.cousesId = cousesId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public AffBean getAffBean() {
		return affBean;
	}

	public void setAffBean(AffBean affBean) {
		this.affBean = affBean;
	}
}
