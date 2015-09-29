package com.dexpert.feecollection.main.users.applicant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.dexpert.feecollection.main.fee.config.FcBean;
import com.dexpert.feecollection.main.fee.config.FcDAO;
import com.dexpert.feecollection.main.fee.config.FeeDetailsBean;
import com.google.common.collect.ArrayListMultimap;

public class CalculateDues {

	static FcDAO dao = new FcDAO();
	static Logger logger = Logger.getLogger(CalculateDues.class.getName());

	/*
	 * public static void main(String[] args) { ArrayList<Integer> valuList =
	 * new ArrayList<Integer>(); valuList.add(11); valuList.add(4);
	 * valuList.add(1); Collections.sort(valuList);
	 * 
	 * calculateFeeStudent(valuList, 1);
	 * 
	 * }
	 */
	// public static Double calculateFeeStudent(Integer valueId1, Integer
	// valueId2, Integer valueId3, Integer feeId) {
	public  Double calculateFeeStudent(ArrayList<Integer> list, Integer feeId, Integer strId) {
		FeeDetailsBean feeDetail = new FeeDetailsBean();
		List<FcBean> combinations = new ArrayList<FcBean>();
		List<FcBean> searchList = new ArrayList<FcBean>();
		
		logger.info("number of values send from student From" + list);
		// Get Fee From fee_details table
		logger.info("FeeIdes" + feeId);
		try {
			feeDetail = dao.GetFees("id", null, feeId, null, strId).get(0);
		} catch (Exception e) {

			// log.info("Incorrect Fee ID");
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
		ArrayList<Integer> values = new ArrayList<Integer>();
		while (keyIt.hasNext()) {
			searchList.clear();
			values.clear();
			searchList.addAll(comboMap.get(keyIt.next()));
			// valueFromDB.add(searchList.get(i).getValueId());
			for (int i = 0; i < searchList.size(); i++) {
				values.add(searchList.get(i).getValueId());
			}
			
			Collections.sort(values);
			
			if (values.containsAll(list)) {

				System.out.println("Amount is ::" + searchList.get(0).getAmount());
				return searchList.get(0).getAmount();
			} else {

				// System.out.println("Combination not Available");
			}

		}

		return (double) 0;

	}

}
