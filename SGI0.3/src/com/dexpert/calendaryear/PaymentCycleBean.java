package com.dexpert.calendaryear;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="payment_cycle_master")
public class PaymentCycleBean {
	
	@GenericGenerator(name = "g11", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g11")
	private Integer cycleId;
	private Integer instId;
	private String cycleStratDay;
	private String cycleEndDay;
	private String cycleStartMonth;
	private String cycleEndMonth;
	private Boolean isCycleActive;

	public String getCycleStratDay() {
		return cycleStratDay;
	}

	public void setCycleStratDay(String cycleStratDay) {
		this.cycleStratDay = cycleStratDay;
	}

	public String getCycleEndDay() {
		return cycleEndDay;
	}

	public void setCycleEndDay(String cycleEndDay) {
		this.cycleEndDay = cycleEndDay;
	}

	public String getCycleStartMonth() {
		return cycleStartMonth;
	}

	public void setCycleStartMonth(String cycleStartMonth) {
		this.cycleStartMonth = cycleStartMonth;
	}

	public String getCycleEndMonth() {
		return cycleEndMonth;
	}

	public void setCycleEndMonth(String cycleEndMonth) {
		this.cycleEndMonth = cycleEndMonth;
	}

	public Integer getCycleId() {
		return cycleId;
	}

	public void setCycleId(Integer cycleId) {
		this.cycleId = cycleId;
	}

	public Integer getInstId() {
		return instId;
	}

	public void setInstId(Integer instId) {
		this.instId = instId;
	}

	public Boolean getIsCycleActive() {
		return isCycleActive;
	}

	public void setIsCycleActive(Boolean isCycleActive) {
		this.isCycleActive = isCycleActive;
	}
	@Override
	public String toString() {
		String s1 = "Cycle Start Day=::" + cycleStratDay + "::Cycle start month=::" + cycleStartMonth
				+ "::cycle end day =::" + cycleEndDay + "::cycle end month=::" + cycleEndMonth;
		return s1;
	}
	

}
