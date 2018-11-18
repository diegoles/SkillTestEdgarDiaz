package drink.blended_fruit.utils;

import java.math.BigDecimal;
import java.util.Map;

import drink.blended_fruit.DrinkMachine.OutOfStockException;
import drink.blended_fruit.enums.Drink;
import drink.blended_fruit.enums.Ingredient;
import drink.blended_fruit.enums.Size;

public final class DrinkMachineUtil {

	private static DrinkMachineUtil INSTANCE;
	private final int minDrinks = 4;  // warning min drinks
	private final int stockAllIngredients = 2000;  // stock general

	private DrinkMachineUtil() {}

	public static DrinkMachineUtil getInstance() {
		if (INSTANCE == null) 
			INSTANCE = new DrinkMachineUtil();

		return INSTANCE;
	}

	public void restock(Map<Ingredient, Integer> stock) {
		for (Ingredient ingredient : Ingredient.values()) {
			if (getStock(stock, ingredient) < stockAllIngredients)
				stock.put(ingredient, stockAllIngredients);
		}
	}

	private int getStock(Map<Ingredient, Integer> stock, Ingredient ingredient) {
		return stock.containsKey(ingredient) ? stock.get(ingredient) : 0;
	}

	public void getMenu(Map<Ingredient, Integer> stock) {
		System.out.println("\n\n***********  Menu:  ************\n");
		int i = 0;

		for (Drink drink : Drink.values()) {

			System.out.println(++i + ": ".concat(drink.toString()));
			String drinkPrices = "\tPrices --->";

			for (Size size : Size.values()) {

				if (canMake(stock, drink, size)) {
					drinkPrices = drinkPrices.concat("\t" + size.toString() + ": $")
							.concat(((drink.getPrice().multiply(new BigDecimal(size.getFactor())))
									.setScale(2, BigDecimal.ROUND_HALF_EVEN)).toString());
				} else {
					drinkPrices = drinkPrices.concat("\t" + size.toString() + ": (out of stock)");
				}

			}
			System.out.println(drinkPrices);
		}
		
		System.out.println("\n**  Sizes:  1-> small(300mL)	2-> medium(600mL)	3-> large(900mL) **\n");
	}

	public void getInventory(Map<Ingredient, Integer> stock) {
		System.out.println("\n***********  Inventory:  ************\n");
		stock.forEach((k, v) -> {
			System.out.println("Item : " + k + " = " + v);
		});

		System.out.println("\n\t!!!! WARNING Insufficients ingredients for " + minDrinks + " Drinks --->");

		for (Drink drink : Drink.values()) {
			for (Size size : Size.values()) {
				String alertMinQuantity = alertMinQuantity(stock, drink, size);
				if (!alertMinQuantity.isEmpty()) 
					System.out.println(" " + drink + " " + size.toString() + " " + alertMinQuantity);
			}
		}
	}

	private boolean canMake(Map<Ingredient, Integer> stock, Drink drink, Size size) {

		for (Map.Entry<Ingredient, Integer> quantStuff : drink.getRecipe().entrySet()) {
			
			int stockIngredient = DrinkMachineUtil.getInstance().getStock(stock, quantStuff.getKey());
			int quantityIngredient = getQuantityIngredient(size.getFactor(), quantStuff.getValue());
			
			if (stockIngredient < quantityIngredient) 
				return false;
		}
		
		return true;
	}

	public void make(Map<Ingredient, Integer> stock, String[] inputs) throws OutOfStockException {

		int inputDrink = Integer.parseInt(inputs[0].trim());
		int inputSize = 1; // default size small

		if (inputs.length > 1)
			inputSize = Integer.parseInt(inputs[1].trim());

		if ((inputDrink > 0 && inputDrink <= Drink.values().length)
				&& (inputSize > 0 && inputSize <= Size.values().length)) {

			Drink order = Drink.values()[inputDrink - 1];
			Size size = Size.values()[inputSize - 1];

			if (canMake(stock, order, size)) {
				order.getRecipe()
				.forEach((k, v) -> stock.put(k, getStock(stock, k) - getQuantityIngredient(size.getFactor(), v)));
			} else {
				System.out.println("Out of stock:: " + order.toString());
				throw new OutOfStockException(order);
			}

			System.out.println("******   Drink made: " + order + " " + size.toString() + "****");

		} else {
			System.out.println("Invalid input: ");
		}

	}

	private int getQuantityIngredient(int factor, Integer quantity) {
		return factor * quantity;
	}

	private String alertMinQuantity(Map<Ingredient, Integer> stock, Drink drink, Size size) {
		String insufficientIngredients = "";

		for (Map.Entry<Ingredient, Integer> quantStuff : drink.getRecipe().entrySet()) {
			int stockIngredient = DrinkMachineUtil.getInstance().getStock(stock, quantStuff.getKey());
			int quantityMinIngredients = getQuantityIngredient(size.getFactor(), quantStuff.getValue()) * minDrinks;
			if (stockIngredient < quantityMinIngredients) {
				insufficientIngredients = insufficientIngredients + "  -" + quantStuff.getKey();
			}
		}
		return insufficientIngredients;
	}
}
