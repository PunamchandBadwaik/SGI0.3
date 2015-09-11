package com.test.mapping;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name="fee_details")
public class FeeDetails {

	
	@GenericGenerator(name = "g4", strategy = "increment")
	@Id
	@GeneratedValue(generator = "g4")
	private Integer feeId;
	private String feeName,ins_param,cou_param,app_param,ser_param;
	private Boolean forInst,forAppl,cal_mode;
	
	@OneToMany(cascade={CascadeType.ALL})
    @JoinColumn(name="feeId")
	private List<FeeCombos>comboList;
	
	
	
	public Integer getFeeId() {
		return feeId;
	}
	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public String getIns_param() {
		return ins_param;
	}
	public void setIns_param(String ins_param) {
		this.ins_param = ins_param;
	}
	public String getCou_param() {
		return cou_param;
	}
	public void setCou_param(String cou_param) {
		this.cou_param = cou_param;
	}
	public String getApp_param() {
		return app_param;
	}
	public void setApp_param(String app_param) {
		this.app_param = app_param;
	}
	public String getSer_param() {
		return ser_param;
	}
	public void setSer_param(String ser_param) {
		this.ser_param = ser_param;
	}
	public Boolean getForInst() {
		return forInst;
	}
	public void setForInst(Boolean forInst) {
		this.forInst = forInst;
	}
	public Boolean getForAppl() {
		return forAppl;
	}
	public void setForAppl(Boolean forAppl) {
		this.forAppl = forAppl;
	}
	public Boolean getCal_mode() {
		return cal_mode;
	}
	public void setCal_mode(Boolean cal_mode) {
		this.cal_mode = cal_mode;
	}
	public List<FeeCombos> getComboList() {
		return comboList;
	}
	public void setComboList(List<FeeCombos> comboList) {
		this.comboList = comboList;
	}
	
	
	
}
