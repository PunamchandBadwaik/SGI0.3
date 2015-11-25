package com.dexpert.calendaryear;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Calender_Year_Master")
public class CalendarBean {
	@GenericGenerator(name = "g11", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g11")
	private Integer calendarId;
	private String description;
	private String startYear;
	private Boolean isCalendarActive;
	private String endYear;
	private Integer instId;
	private String calendar_session_id;
	private String calendarName;
    private String calStartMonth;
    private String calEndMonth;
    private String calStartDay;
	private String calEndDay;
    private Integer numberOfPaymentCycle;
	

	public Integer getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(Integer calendarId) {
		this.calendarId = calendarId;
	}

	public String getCalStartMonth() {
		return calStartMonth;
	}

	public void setCalStartMonth(String calStartMonth) {
		this.calStartMonth = calStartMonth;
	}

	public String getCalEndMonth() {
		return calEndMonth;
	}

	public void setCalEndMonth(String calEndMonth) {
		this.calEndMonth = calEndMonth;
	}

	public String getCalStartDay() {
		return calStartDay;
	}

	public void setCalStartDay(String calStartDay) {
		this.calStartDay = calStartDay;
	}

	public String getCalEndDay() {
		return calEndDay;
	}

	public void setCalEndDay(String calEndDay) {
		this.calEndDay = calEndDay;
	}

	
	public Boolean getIsCalendarActive() {
		return isCalendarActive;
	}

	public void setIsCalendarActive(Boolean isCalendarActive) {
		this.isCalendarActive = isCalendarActive;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public Integer getInstId() {
		return instId;
	}

	public void setInstId(Integer instId) {
		this.instId = instId;
	}

	public String getCalendar_session_id() {
		return calendar_session_id;
	}

	public void setCalendar_session_id(String calendar_session_id) {
		this.calendar_session_id = calendar_session_id;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNumberOfPaymentCycle() {
		return numberOfPaymentCycle;
	}

	public void setNumberOfPaymentCycle(Integer numberOfPaymentCycle) {
		this.numberOfPaymentCycle = numberOfPaymentCycle;
	}

}
