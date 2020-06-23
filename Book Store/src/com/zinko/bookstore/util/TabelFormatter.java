package com.zinko.bookstore.util;

import java.text.DecimalFormat;

import javafx.util.StringConverter;

public class TabelFormatter extends StringConverter<Integer> {
private static DecimalFormat DF = new DecimalFormat("#,##0");
	@Override
	public Integer fromString(String str) {
		try {
			if(!str.isEmpty()) {
				return DF.parse(str).intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String toString(Integer data) {
		try {
			if(null != data) {
				return DF.format(data);
			}
		} catch (Exception e) {
		e.printStackTrace();
		}
		
		return null;
	}

}
