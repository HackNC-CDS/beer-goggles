package com.beergoggles;

import java.util.Comparator;


public class BangComparator implements Comparator<Beer> {
	 public int compare(Beer o1, Beer o2) {
		 	double a1,a2;
		 	a1 = o1.abv()*o1.amt()/o1.price();
		 	a2 = o2.abv()*o2.amt()/o2.price();
		 	if(a1>a2)return -1;
		 	else if(a1 < a2) return 1;
		 	return 0;
	 }
}
