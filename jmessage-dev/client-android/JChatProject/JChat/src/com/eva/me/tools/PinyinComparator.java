package com.eva.me.tools;

import java.util.Comparator;

import com.eva.me.entity.UserLetterBean;

public class PinyinComparator implements Comparator<UserLetterBean> {

	public int compare(UserLetterBean o1, UserLetterBean o2) {
		if (o1.getLetter().equals("@")
				|| o2.getLetter().equals("#")) {
			return -1;
		} else if (o1.getLetter().equals("#")
				|| o2.getLetter().equals("@")) {
			return 1;
		} else {
			return o1.getLetter().compareTo(o2.getLetter());
		}
	}

}
