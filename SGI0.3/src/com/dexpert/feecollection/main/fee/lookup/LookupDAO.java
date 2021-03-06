package com.dexpert.feecollection.main.fee.lookup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.users.LoginBean;

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
			session.delete(deleteData);
			;
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

	public ArrayList<LookupBean> getLookupData(String filterKey, String filterValue, Integer id, ArrayList<Integer> Ids) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			Criteria lookupCr = session.createCriteria(LookupBean.class);
			if (filterKey.contentEquals("ALL")) {

			}
			else if (filterKey.contentEquals("ID")) {
				lookupCr.add(Restrictions.eq("lookupId", id));
			}
			else if (filterKey.contentEquals("Scope")) {
				lookupCr.add(Restrictions.eq("lookupScope", filterValue));
			}
			else if (filterKey.contentEquals("Type")) {
				lookupCr.add(Restrictions.eq("lookupType", filterValue));
			}
			else if (filterKey.contentEquals("Ids")) {
				lookupCr.add(Restrictions.in("lookupId", Ids));
			}
			else if (filterKey.contentEquals("Name")) {
				lookupCr.add(Restrictions.eq("lookupName", filterValue));
			}

			ArrayList<LookupBean> resultList = new ArrayList<LookupBean>();
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

	public List<LookupBean> getListOfLookUpValues(String lookupScope, List<Integer> lookupIdes, List<Integer> valueIdes) {
		Session session = factory.openSession();

		try {
            Criteria criteria = session.createCriteria(LookupBean.class);
			criteria.add(Restrictions.eq("lookupScope", lookupScope));
			criteria.add(Restrictions.in("lookupId",lookupIdes ));
			criteria.createAlias("fvBeansList", "fv");
			criteria.setFetchMode("fvBeansList",FetchMode.JOIN );
			criteria.add(Restrictions.in("fv.feeValueId", valueIdes));
		    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<LookupBean> list = criteria.list();
			return list;

		} finally {
			// close Session
			session.close();
		}
	}
}
