/**
 * @author Dave Perra <perra@cs.unc.edu>
 * @author Steven Love <slove13@cs.unc.edu>
 * @author Clinton Freeman <freeman@cs.unc.edu>
 */

package com.beergoggles;


public class Beer {
	public static final int RATING = 1;
	public static final int ABV = 2;
	public static final int BRO = 3;
	public static int MODE = RATING;
	
	public String beer_name = "";
	public String beer_style = "";
	public String beer_abv = "";
	public String brewery_name = "";
	public String brewery_state = "";
	public String brewery_country = "";
	public String ba_score = "";
	public String ba_rating = "";
	public String bros_score = "";
	public String bros_rating = "";
	public String ratings = "";
	public String reviews = "";
	public String rAvg = "";
	public String pDev = "";
	
	public Beer() {}
	
	@Override
	public String toString() {
		if(MODE == RATING) {
			return this.beer_name + ": " + this.ba_rating;
		} else if (MODE == ABV) {
			return this.beer_name + ": " + this.beer_abv;
		} else if (MODE == BRO) {
			return this.beer_name + ": " + this.bros_rating;
		}
		return this.beer_name;
	}
}
