package drink.blendedFruit;

import java.math.BigDecimal;

public enum Ingredient {
	
	STRAWBERRY_G("0.04"), 
	BANANA_G("0.04"), 
	MANGO_G("0.045"), 
	CONDENSED_MILK_ML("0.02"), 
	ICE_ML("0.015"), 
	SUGAR_G("0.010");

	private final String name;
	private final BigDecimal cost;

	Ingredient(String cost) {
		this.name = this.name().replace("_", " ").toLowerCase();
		this.cost = new BigDecimal(cost);
	}

	public BigDecimal getCost() {
		return this.cost;
	}

	public String toString() {
		return this.name;
	}
}