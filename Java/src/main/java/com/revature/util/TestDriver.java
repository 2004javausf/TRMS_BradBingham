package com.revature.util;

public class TestDriver {
	public static void main(String[] args) {
		String test = "2021-05-20";
		//20-MAY-2021
		String newDate = formatDate(test);
		System.out.println(newDate);
	}

	private static String formatDate(String test) {
		StringBuilder sb = new StringBuilder();
		sb.append(test.substring(8, 10)+"-");
		String key = test.substring(5,7);
		switch (key) {
		case "01": sb.append("JAN-"); break;
		case "02": sb.append("FEB-"); break;
		case "03": sb.append("MAR-"); break;
		case "04": sb.append("APR-"); break;
		case "05": sb.append("MAY-"); break;
		case "06": sb.append("JUN-"); break;
		case "07": sb.append("JUL-"); break;
		case "08": sb.append("AUG-"); break;
		case "09": sb.append("SEP-"); break;
		case "10": sb.append("OCT-"); break;
		case "11": sb.append("NOV-"); break;
		case "12": sb.append("DEC-"); break;
		default:break;
		}
		sb.append(test.substring(0, 4));
		return sb.toString();
	}
	
}
