package com.dexpert.feecollection.main.users.applicant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Applicable_fees_student")
public class ApplicableFeesBean {
	@GenericGenerator(name = "g1", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g1")
	private Integer id;
	private String courseName;
	private String year;
	private String applicableFeeId;
	public String getApplicableFeeId() {
		return applicableFeeId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public void setApplicableFeeId(String applicableFeeId) {
		this.applicableFeeId = applicableFeeId;
	}

}
