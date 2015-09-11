package com.dexpert.feecollection.main.fee.lookup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;

public class LookupDAO {
	// Global Variables
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(LookupDAO.class.getName());

	// --------------------------------

	// DAO Methods
	public LookupBean saveLookupData(LookupBean saveData) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(saveData);
			session.getTransaction().commit();
			return saveData;
		} catch (Exception e) {
			e.printStackTrace();
			return saveData;
		} finally {

			// close session
			session.close();
		}
	}
	
	public LookupBean removeLookupData(LookupBean deleteData) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			session.delete(deleteData);;
			session.getTransaction().commit();
			return deleteData;
		} catch (Exception e) {
			e.printStackTrace();
			return deleteData;
		} finally {

			// close session
			session.close();
		}
	}

	public ArrayList<LookupBean> getLookupData(String filterKey,String filterValue,Integer id,ArrayList<Integer>Ids) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			Criteria lookupCr = session.createCriteria(LookupBean.class);
			if(filterKey.contentEquals("ALL"))
			{
				
			}
			if(filterKey.contentEquals("ID"))
			{
			lookupCr.add(Restrictions.eq("lookupId", id));
			}
			if(filterKey.contentEquals("Scope"))
			{
			lookupCr.add(Restrictions.eq("lookupScope", filterValue));
			}
			if(filterKey.contentEquals("Type"))
			{
				lookupCr.add(Restrictions.eq("lookupType", filterValue));
			}
			if(filterKey.contentEquals("Ids"))
			{
				lookupCr.add(Restrictions.in("lookupId",Ids));
			}
			
			ArrayList<LookupBean>resultList=new ArrayList<LookupBean>();
			LookupBean resultBean = new LookupBean();
			Iterator<LookupBean> lookupIt = lookupCr.list().iterator();
			if (lookupIt.hasNext()) {
				while (lookupIt.hasNext()) {
					resultBean = lookupIt.next();
					resultList.add(resultBean);
				}
			} 
			return resultList;

		} finally {
			// close session
			session.close();
		}
	}
	// ----------------------------

	public List<LookupBean> getListOfLookUpValues() {
		Session session = factory.openSession();
		try {

			Criteria criteria = session.createCriteria(LookupBean.class);
			List<LookupBean> list = criteria.list();
			return list;

		} finally {
			// close Session
			session.close();
		}
	}
}
