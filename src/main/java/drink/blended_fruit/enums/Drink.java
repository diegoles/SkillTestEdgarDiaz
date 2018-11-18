package drink.blended_fruit.enums;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Drink {

	BLENDED_BANANAS("Blended Bananas", "120 BANANA_G", "30 ICE_ML", "20 CONDENSED_MILK_ML", "8 SUGAR_G"),
	BLENDED_MANGOS("Blended Mangos", "140 MANGO_G", "30 ICE_ML", "20 CONDENSED_MILK_ML", "8 SUGAR_G"),
	BLENDED_STRAWBERRIES("Blended Strawberries", "100 STRAWBERRY_G", "30 ICE_ML", "20 CONDENSED_MILK_ML", "8 SUGAR_G");

	private final String name;
	private final Map<Ingredient, Integer> ingredients;
	private final BigDecimal cost;

	Drink(String name, String... recipe) {

		Map<Ingredient, Integer> map = new HashMap<>();

		BigDecimal cost = BigDecimal.ZERO;

		for (String spec : recipe) {
			String[] amountOfStuff = spec.split(" ", 2);
			int quantity = (amountOfStuff.length > 1) ? Integer.parseInt(amountOfStuff[0]) : 1;
			Ingredient ingredient = Enum.valueOf(Ingredient.class, amountOfStuff[amountOfStuff.length - 1]);

			map.put(ingredient, quantity);

			cost = cost.add(ingredient.getCost().multiply(new BigDecimal(quantity)));
		}

		this.name = name;
		this.ingredients = Collections.unmodifiableMap(map);
		this.cost = cost;
	}

	public Map<Ingredient, Integer> getRecipe() {
		return this.ingredients;
	}

	public BigDecimal getPrice() {
		return this.cost;
	}

	public String toString() {
		return this.name;
	}
}