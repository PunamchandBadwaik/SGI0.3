package com.test.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.xml.sax.SAXException;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.payment.AtomPG;

public class TestAction {

	public static SessionFactory factory = ConnectionClass.getFactory();
	
	
	public String addFee() throws IOException, ParserConfigurationException, SAXException
	{
		
		AtomPG atm=new AtomPG();
		
		atm.PayMethod("78945", "456123", "456123", "123456", "500", "123456");
		
		
		/*FeeDetails newfee=new FeeDetails();
		FeeCombos newcombo=new FeeCombos();
		List<FeeCombos> comboList=new ArrayList<FeeCombos>();
		
		newfee.setFeeName("Application Fee");
		newfee.setForAppl(true);
		newfee.setCal_mode(false);
		newfee.setForInst(false);
		
		newcombo.setAmount(4000.021);
		newcombo.setValue_id_fk(4);
		newcombo.setCommboId(1);
		newcombo.setFeedata(newfee);
		
		comboList.add(newcombo);
		
		newcombo=new FeeCombos();
		newcombo.setAmount(4000.021);
		newcombo.setValue_id_fk(5);
		newcombo.setCommboId(1);
		newcombo.setFeedata(newfee);
		comboList.add(newcombo);
		
		newcombo=new FeeCombos();
		newcombo.setAmount(4000.021);
		newcombo.setValue_id_fk(6);
		newcombo.setCommboId(1);
		newcombo.setFeedata(newfee);
		comboList.add(newcombo);
		
		newcombo=new FeeCombos();
		newcombo.setAmount(4000.021);
		newcombo.setValue_id_fk(7);
		newcombo.setCommboId(1);
		newcombo.setFeedata(newfee);
		comboList.add(newcombo);
		
		newfee.setComboList(comboList);
		
		Session session=factory.openSession();
		session.beginTransaction();
		session.saveOrUpdate(newfee);
		session.getTransaction().commit();*/

		
		
		return "success";
		
	}
}
