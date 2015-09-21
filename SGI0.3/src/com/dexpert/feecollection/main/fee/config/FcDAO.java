package com.dexpert.feecollection.main.fee.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.google.common.collect.ArrayListMultimap;

public class FcDAO {
	// Global Declarations
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(LookupDAO.class.getName());

	// Global Declarations End
	// DAO Methods
	public void insertFeeBulk(ArrayList<FcBean> savelist) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			for (int i = 0; i < savelist.size(); i++) {
				FcBean savebean = new FcBean();
				savebean = savelist.get(i);
				session.save(savebean);
				if (i % 20 == 0) { // 20, same as the JDBC batch size
					// flush a batch of inserts and release memory:
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// close session
			session.close();
		}
	}

	
	public void deleteFeeBulk(ArrayList<FcBean> remlist) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			for (int i = 0; i < remlist.size(); i++) {
				FcBean rembean = new FcBean();
				rembean = remlist.get(i);
				session.delete(rembean);
				if (i % 20 == 0) { // 20, same as the JDBC batch size
					// flush a batch of deletes and release memory:
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// close session
			session.close();
		}
	}
	
	public void saveFeeDetails(FeeDetailsBean savedata) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(savedata);
			session.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// close session
			session.close();
		}
	}

	public ArrayList<FeeDetailsBean> GetFees(String filterKey, String filterValue, Integer id, ArrayList<Integer> ids) {
		// Declarations
		ArrayList<FeeDetailsBean> ResultList = new ArrayList<FeeDetailsBean>();
		// Open session from session factory
		Session session = factory.openSession();
		try {
			Criteria feeCr = session.createCriteria(FeeDetailsBean.class);
			if (filterKey.contentEquals("ALL")) {

			} else if (filterKey.contentEquals("payee")) {
				if (filterValue.contentEquals("applicant")) {
					feeCr.add(Restrictions.eq("forApplicant", 1));

				}
				if (filterValue.contentEquals("institute")) {
					feeCr.add(Restrictions.eq("forInstitute", 1));
				}
			} else if (filterKey.contentEquals("name")) {
				feeCr.add(Restrictions.eq("feeName", filterValue));
			} else if (filterKey.contentEquals("id")) {
				feeCr.add(Restrictions.eq("feeId", id));
			} else if (filterKey.contentEquals("ids")) {
				feeCr.add(Restrictions.in("feeId", ids));
			}
			feeCr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			Iterator<FeeDetailsBean> feeIt = feeCr.list().iterator();
			while (feeIt.hasNext()) {
				ResultList.add(feeIt.next());
			}
			return ResultList;

		} catch (Exception e) {
			e.printStackTrace();
			return ResultList;

		} finally {
			// close session
			session.close();
		}
	}

	public Double calculateFeeStudent(Integer valueId1, Integer valueId2, Integer valueId3, Integer feeId) {
		FeeDetailsBean feeDetail = new FeeDetailsBean();
		List<FcBean> combinations = new ArrayList<FcBean>();
		List<FcBean> searchList = new ArrayList<FcBean>();
		// Get Fee From fee_details table
		try {
			feeDetail = GetFees("id", null, feeId, null).get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("Incorrect Fee ID");
			e.printStackTrace();
		}
		// Get all the possible combinations of the retrieved fee

		combinations = feeDetail.getConfigs();
		// Create a multimap with the combo ID as the key and the bean as the
		// value
		com.google.common.collect.ListMultimap<Integer, FcBean> comboMap = ArrayListMultimap.create();
		Iterator<FcBean> comboIt = combinations.iterator();
		while (comboIt.hasNext()) {
			FcBean tempbean = comboIt.next();
			comboMap.put(tempbean.getComboId(), tempbean);
		}
		// Get KeySet
		ArrayList<Integer> cKeySet = new ArrayList<Integer>(comboMap.keySet());
		// Search for the unique combo which satisfies all the three valueIds
		Iterator<Integer> keyIt = cKeySet.iterator();
		while (keyIt.hasNext()) {
			ArrayList<Integer> values = new ArrayList<Integer>();
			searchList.addAll(comboMap.get(keyIt.next()));
			for (int i = 0; i < searchList.size(); i++) {
				values.add(searchList.get(i).getValueId());
			}
			/*
			 * if ((values.get(0) == valueId1 || values.get(0) == valueId2 ||
			 * values.get(0) == valueId3) && (values.get(1) == valueId1 ||
			 * values.get(1) == valueId2 || values.get(1) == valueId3) &&
			 * (values.get(2) == valueId1 || values.get(2) == valueId2 ||
			 * values.get(2) == valueId3)) { return
			 * searchList.get(0).getAmount();
			 */
			if ((values.get(0) == valueId1 || values.get(0) == valueId2 || values.get(0) == valueId3)
					&& (values.get(1) == valueId1 || values.get(1) == valueId2 || values.get(1) == valueId3)) {
				return searchList.get(0).getAmount();
			} else {
				searchList.clear();
			}
		}
		// If no Combination found return zero
		log.info("no Unique combination found");
		return (double) 0;

	}

	public List<FeeDetailsBean> getAllFeeDetail() {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(FeeDetailsBean.class);
		List<FeeDetailsBean> feesDetails = criteria.list();
		return feesDetails;
	}

	public List<Integer> getAllFeeId() {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(FeeDetailsBean.class);
		criteria.setProjection(Projections.id()).addOrder(Order.asc("feeId"));
		List<Integer> feeIdes = criteria.list();
		return feeIdes;
	}

	public String getFeeName(Integer feeId) {
		Session session = factory.openSession();
		String feeName = (String) session.createCriteria(FeeDetailsBean.class).add(Restrictions.eq("feeId", feeId))
				.setProjection(Projections.property("feeName")).list().iterator().next();
		session.close();
		return feeName;

	}

	public List<String> getFeeNames(List<Integer> feeIdes) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(FeeDetailsBean.class);
		if (feeIdes != null && feeIdes.size() > 0 && (!feeIdes.isEmpty())) {
			criteria.add(Restrictions.in("feeId", feeIdes)).setProjection(Projections.property("feeName"))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		} else {
			criteria.setProjection(Projections.property("feeName")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		List<String> feeName = criteria.list();
		session.close();
		return feeName;
	}
	

	// DAO Methods End
}
