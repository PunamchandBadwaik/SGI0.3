package com.dexpert.feecollection.main.payment.studentPayment;

import java.util.Calendar;
import java.util.UUID;

public class Idgenerator {

	public static String transxId() {
		Calendar calendar = Calendar.getInstance();
		Integer dd = calendar.get(Calendar.DATE);
		Integer mm = calendar.get((Calendar.MONTH)) + 1;
		Integer min = calendar.get(Calendar.MINUTE);
		Integer second = calendar.get(Calendar.SECOND);
		UUID uid = UUID.randomUUID();
		String value = String.valueOf(uid.getMostSignificantBits());
		// String tranxId = "FD".concat(value.substring(6, value.length() -
		// 1).concat(min.toString())
		// .concat(second.toString()));
		String tranxId = "FD".concat(dd.toString()).concat(mm.toString()).concat(min.toString())
				.concat(second.toString()).concat(value.substring(6, value.length() - 3));

		return tranxId;

	}
}
