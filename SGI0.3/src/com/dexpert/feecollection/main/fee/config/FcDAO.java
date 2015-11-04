package com.dexpert.feecollection.main.fee.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.dexpert.feecollection.main.ConnectionClass;
import com.dexpert.feecollection.main.fee.PaymentDuesBean;
import com.dexpert.feecollection.main.fee.lookup.LookupDAO;
import com.dexpert.feecollection.main.users.affiliated.AffDAO;
import com.google.common.collect.ArrayListMultimap;

public class FcDAO {
	// Global Declarations
	public static SessionFactory factory = ConnectionClass.getFactory();
	static Logger log = Logger.getLogger(FcDAO.class.getName());
	AffDAO affDAO = new AffDAO();

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

	public void saveFeeStructure(FeeStructureData savedata) {
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

	public void insertFeeStructureBulk(ArrayList<FeeStructureData> savelist) {
		// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			session.beginTransaction();
			for (int i = 0; i < savelist.size(); i++) {
				FeeStructureData savebean = new FeeStructureData();
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

	@SuppressWarnings("deprecation")
	public ArrayList<FeeDetailsBean> GetFees(String filterKey, String filterValue, Integer id, ArrayList<Integer> ids,
			Integer structureId) {
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

			if (structureId != null) {
				feeCr.createAlias("configs", "conf", CriteriaSpecification.LEFT_JOIN);
				feeCr.add(Restrictions.eq("conf.structure_id", structureId));

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

	public List<FeeDetailsBean> getAllFeeDetail() {
		// TODO Auto-generated method stub
		Session session = factory.openSession();
		try {
			Criteria criteria = session.createCriteria(FeeDetailsBean.class);
			List<FeeDetailsBean> feesDetails = criteria.list();
			return feesDetails;
		} finally {
			session.close();
		}

	}

	public List<Integer> getAllFeeId() {
		Session session = factory.openSession();

		try {
			Criteria criteria = session.createCriteria(FeeDetailsBean.class);
			criteria.setProjection(Projections.id()).addOrder(Order.asc("feeId"));
			List<Integer> feeIdes = criteria.list();

			return feeIdes;

		} finally {
			session.close();
		}
	}

	public String getFeeName(Integer feeId) {
		Session session = factory.openSession();

		try {
			String feeName = (String) session.createCriteria(FeeDetailsBean.class).add(Restrictions.eq("feeId", feeId))
					.setProjection(Projections.property("feeName")).list().iterator().next();
			return feeName;
		} finally {
			session.close();
		}

	}

	public List<String> getFeeNames(List<Integer> feeIdes) {
		Session session = factory.openSession();

		try

		{
			Criteria criteria = session.createCriteria(FeeDetailsBean.class);
			if (feeIdes != null && feeIdes.size() > 0 && (!feeIdes.isEmpty())) {
				criteria.add(Restrictions.in("feeId", feeIdes)).setProjection(Projections.property("feeName"))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			} else if (feeIdes.size() < 1) {
				List<String> feeName = new ArrayList<String>();
				return feeName;
			}

			else {
				criteria.setProjection(Projections.property("feeName")).setResultTransformer(
						Criteria.DISTINCT_ROOT_ENTITY);
			}
			List<String> feeName = criteria.list();
			return feeName;
		} finally {
			session.close();

		}

	}

	public Integer getMaxStructure() {// Declarations

		// Open session from session factory
		Session session = factory.openSession();
		try {
			DetachedCriteria maxId = DetachedCriteria.forClass(FcBean.class).setProjection(
					Projections.max("structure_id"));
			Criteria cr = session.createCriteria(FcBean.class).add(Property.forName("structure_id").eq(maxId));
			FcBean temp = (FcBean) cr.list().get(0);
			Integer maxCount = temp.getStructure_id();
			log.info("new structure id is " + (maxCount + 1));
			return maxCount;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			// close session
			session.close();
		}

	}

	public List<Integer> getLookupValue(List<Integer> structureIdes) {
		Session session = factory.openSession();
		Criteria criteria = session.createCriteria(FcBean.class);
		criteria.add(Restrictions.in("structure_id", structureIdes));
		criteria.setProjection(Projections.distinct(Projections.property("valueId")));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Integer> lookupValuesId = criteria.list();

		session.close();
		return lookupValuesId;
	}

	public Integer getSequenceOfFee(Integer instId, Integer feeId) {
		Integer sequenceId = null;
		Session session = factory.openSession();
		try {
			String query = "SELECT sequenceId FROM sgi.affiliatedinstitute_feedetails where feeId=:feeId and inst_id=:insId";
			SQLQuery sqlQuery = session.createSQLQuery(query);
			sqlQuery.setParameter("feeId", feeId);
			sqlQuery.setParameter("insId", instId);
			sequenceId = (Integer) sqlQuery.list().iterator().next();
			return sequenceId;
		} catch (Exception ex) {
			return sequenceId;
		} finally {
			session.close();
		}
	}

	public Integer getFeeStructure(Integer instId, Integer feeId) {
		// Declarations
		Integer structureId = null;
		// Open session from session factory
		Session session = factory.openSession();
		try {
			Criteria cr = session.createCriteria(FeeStructureData.class);
			cr.add(Restrictions.eq("inst_id", instId));
			cr.add(Restrictions.eq("fee_id", feeId));
			FeeStructureData temp = (FeeStructureData) cr.list().get(0);
			structureId = temp.getStructure_id();
			return structureId;

		} catch (Exception e) {
			e.printStackTrace();
			return structureId;
		} finally {

			// close session
			session.close();

		}
	}

	public List<Integer> getValueIdByStructureIdes(List<Integer> structureIdes) {
		Session session = factory.openSession();

		try {
			Criteria criteria = session.createCriteria(FcBean.class);
			criteria.add(Restrictions.in("structure_id", structureIdes));
			criteria.setProjection(Projections.distinct(Projections.property("valueId")));
			List<Integer> valuesIdes = criteria.list();
			return valuesIdes;
		} finally {
			session.close();
		}

	}
	// DAO Methods End
}
