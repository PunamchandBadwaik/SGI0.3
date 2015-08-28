package com.dexpert.feecollection.main.fee.lookup.values;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.dexpert.feecollection.main.fee.lookup.LookupBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.opensymphony.xwork2.ActionSupport;

public class FvAction extends ActionSupport {

	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	static Logger log = Logger.getLogger(FvAction.class.getName());
	LookupDAO lpDao=new LookupDAO();
	
	public String updateValues()
	{
		boolean targetFlag=false;
		try
		{
			if(request.getParameter("view").contentEquals("true"))
			{
				targetFlag=true;
			}
		}
		catch(Exception e)
		{
			targetFlag=false;
		}
		ArrayList<LookupBean>paramList=new ArrayList<LookupBean>();
		LookupBean paramBean=new LookupBean();
		Integer id=Integer.parseInt(request.getParameter("paramId"));
		paramList=lpDao.getLookupData("ID",null,id,null);
		if(paramList.size()==0)
		{
			return ERROR;
		}
		
		paramBean=paramList.get(0);
		String values=request.getParameter("values");
		List<String> ParamValList = Arrays.asList(values.split(","));
		log.info("ValuesList from request are "+ParamValList+" ParamvalList is "
				+ParamValList.size()+" to String  function gives "+ParamValList.toString()
				);
		
		if(ParamValList.toString().contentEquals("[]"))
			
		{
			log.info("Values are missing");
		}
		List<FvBean> valuesList=new ArrayList<FvBean>();
		valuesList=paramBean.getFvBeansList();
		
		Iterator<String> valuesIt=ParamValList.iterator();
		while(valuesIt.hasNext())
		{
			FvBean valuesBean=new FvBean();
			valuesBean.setValue(valuesIt.next());
			valuesBean.setLookupname(paramBean);
			valuesList.add(valuesBean);
			
		}
		paramBean.setFvBeansList(valuesList);
		paramBean=lpDao.saveLookupData(paramBean);
		request.setAttribute("reqAlertFlag", true);
		if(targetFlag)
		{
			request.setAttribute("msg", "Parameter Sucessfully Updated");
			return "view";
		}
		return SUCCESS;
	}
}
