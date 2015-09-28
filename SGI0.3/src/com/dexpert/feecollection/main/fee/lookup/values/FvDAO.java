package com.dexpert.feecollection.main.fee.lookup.values;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;

public class FvDAO {
	// Global Declarations
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(FvDAO.class.getName());

	// End of Global Declarations

	// DAO Methods Start
	public FvBean saveParamValues(FvBean valuesData) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(valuesData);
			session.getTransaction().commit();
			return valuesData;
		} finally {
			// close session
			session.close();
		}
	}

	public ArrayList<FvBean> getValues(String filterKey, String filterValue, ArrayList<Integer> Ids) {
		// Declarations
		ArrayList<FvBean> resultList = new ArrayList<FvBean>();

		// Open session from session factory
		Session session = factory.openSession();
		try {
			Criteria searchCr = session.createCriteria(FvBean.class);
			if (filterKey.contentEquals("ALL")) {

			}
			if (filterKey.contentEquals("Ids")) {
				searchCr.add(Restrictions.in("feeValueId", Ids));
			}
			if (filterKey.contentEquals("LookupIds")) {
				searchCr.add(Restrictions.in("FeeLookupId_Fk", Ids));
			}
			resultList = (ArrayList<FvBean>) searchCr.list();
			return resultList;
		} finally {
			// close session
			session.close();
		}

	}

	public Integer findFeeValueId(String value, Integer feeId) {
		log.info(" value is ::" + value);
		log.info(" Fee id is ::" + feeId);

		String query = "SELECT feeValueId from sgi.fee_values_master where value=:category";// and FeeLookupId_Fk=:feeId";
		Session session = factory.openSession();
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setParameter("category", value);
	//	sqlQuery.setParameter("feeId", feeId);
		Integer id = (Integer) sqlQuery.list().iterator().next();
		session.close();
		return id;
	}

	public Integer findFeeValueId(Long value, Integer feeId) {
		log.info(" value is111 ::" + value);
		log.info(" Fee id is111 ::" + feeId);
		String query = "SELECT feeValueId from sgi.fee_values_master where value=:category" ;//and FeeLookupId_Fk=:feeId";
		Session session = factory.openSession();
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setParameter("category", value);
	//	sqlQuery.setParameter("feeId", feeId);
		Integer id = (Integer) sqlQuery.list().iterator().next();
		session.close();
		return id;
	}
	
	public Integer getCourseId(String courseName) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(FvBean.class);
		criteria.add(Restrictions.eq("value", courseName)).setProjection(Projections.id());
		Integer courseId = (Integer) criteria.list().iterator().next();
		session.close();
		return courseId;
	}
	public List<Integer> getListOfValueBeans(List<Integer> listOfValue)
	{
	Session session=factory.openSession();	
	Criteria criteria=session.createCriteria(FvBean.class);
	criteria.add(Restrictions.in("feeValueId", listOfValue));
	criteria.setProjection(Projections.groupProperty("lookupname.lookupId"));
	criteria.setProjection(Projections.property("lookupname.lookupId"));
	criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	List<Integer> lookUpparamId=criteria.list();
	log.info("look up param list"+lookUpparamId);
	session.close();
	return lookUpparamId;	
	}
	// DAO Methods End

}
