package com.dexpert.feecollection.main.fee.lookup.values;

import java.util.Iterator;
import java.util.LinkedHashSet;


public class BreakString {

	/*
	 * public static void main(String[] args) { String k =
	 * ",B.Ph.FY,B.Ph.FY,OBC,B.Ph.FY,B.Ph.FY,OBC,2013-2014"; BreakString x = new
	 * BreakString(); String aa = x.breakString(k); x.getYear(aa); String course
	 * = x.getCourse(aa); x.getYearCode(course); }
	 */
	
//	OBC~B.Ph.FY~2013-2014

	public String breakString(String k)

	{
		String[] x = k.split(",");
		LinkedHashSet<String> set = new LinkedHashSet<String>();
		String string = "";
		for (int i = 0; i < x.length; i++) {
			String y = x[i].toString();

			if (y == "" || y.equals(null) || y.equals("null")) {

			} else {
				// System.out.println(y);
				set.add(y);

			}

		}

		// List<String> list = new ArrayList<String>(set);

		String z = "";
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			string = (String) iterator.next();
			

			if (string == "" || string.equals(null) || string.equals("null")) {

			}

			else {
				System.out.println("String is ::" + string);
				z = z + string.concat("~");
			}
		}
		String d = z.substring(1, z.length() - 1);
		System.out.println("Break String is ::" + d);
		return d;

	}

	
	public String getCategory(String s) {
		String[] x = s.split("~");
		String xx = "";
		for (int i = 0; i < x.length; i++) {

			if (i == 0) {
				xx = x[i].toString();

			}

		}
		System.out.println("Category String :" + xx);
		return xx;
	}
	
	public String getYear(String s) {
		String[] x = s.split("~");
		String xx = "";
		for (int i = 0; i < x.length; i++) {

			if (i == 2) {
				xx = x[i].toString();
				System.out.println("Year String is ::" + xx);
				xx = xx.substring(0, 4);
			}

		}
		System.out.println("Year String :" + xx);
		return xx;
	}

	
	
	
	
	public String getCourse(String s) {
		String[] x = s.split("~");
		String xx = "";
		for (int i = 0; i < x.length; i++) {

			if (i == 1) {
				xx = x[i].toString();

			}

		}
		System.out.println("COurse String :" + xx);
		return xx;
	}

	public String getYearCode(String course) {
		String yearCode = "";

		if (course.contentEquals("FE")) {

			yearCode = "10";

		} else if (course.contentEquals("SE(Direct)") || course.contentEquals("SE")) {

			yearCode = "20";

		} else if (course.contentEquals("ME")) {
			yearCode = "50";

		} else if (course.contentEquals("MBA")) {
			yearCode = "60";

		}

		else if (course.contentEquals("B.Ph.FY")) {
			yearCode = "10";

		} else if (course.contentEquals("B.Ph.SY(Direct)") || course.contentEquals("B.Ph.SY")) {
			yearCode = "20";

		} else if (course.contentEquals("B.Ph.TY")) {
			yearCode = "30";

		}

		else if (course.contentEquals("B.Ph.Final")) {
			yearCode = "40";

		} else if (course.contentEquals("M.Ph.FY")) {

			yearCode = "50";
		} else if (course.contentEquals("M.Ph.Final")) {

			yearCode = "60";
		}
		System.out.println("Year Code is ::" + yearCode);
		return yearCode;

	}

}