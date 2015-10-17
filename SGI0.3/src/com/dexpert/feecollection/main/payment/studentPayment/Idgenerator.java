package com.dexpert.feecollection.main.payment.studentPayment;

import java.util.Calendar;
import java.util.UUID;

public class Idgenerator {

	public static String transxId() {
		Calendar calendar = Calendar.getInstance();
		Integer min = calendar.get(Calendar.MINUTE);
		Integer second = calendar.get(Calendar.SECOND);
		UUID uid = UUID.randomUUID();
		String value = String.valueOf(uid.getMostSignificantBits());
		String tranxId = "FD".concat(value.substring(4, value.length()).concat(min.toString())
				.concat(second.toString()));

		return tranxId;

	}

}
