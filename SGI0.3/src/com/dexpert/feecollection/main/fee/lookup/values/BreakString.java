package com.dexpert.feecollection.main.fee.lookup.values;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BreakString {

	public static void main(String[] args) {
		String k = ",B.Ph.FY,B.Ph.FY,OBC,B.Ph.FY,B.Ph.FY,OBC,2013-2014";
		BreakString x = new BreakString();
		String aa=x.breakString(k);
		x.bb(aa);
	}

	public String breakString(String k)

	{

		String[] x = k.split(",");
		Set<String> set = new HashSet<String>();
		String string = "";
		for (int i = 0; i < x.length; i++) {
			String y = x[i].toString();
			set.add(y);
			// System.out.println(y);
		}
		String z = "";
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			string = (String) iterator.next();

			z=z+string.concat("~");
			
		}
		String d=z.substring(1, z.length()-1);
		System.out.println(d);
		return d;

	}

	
	
	public void bb(String s)
	{
		String []x=s.split("~");
		for (int i = 0; i < x.length; i++) {
			String xx=x[i].toString();
			System.out.println("Splited String :"+xx);
			
		}
		
		
	}
	
	
	
}
