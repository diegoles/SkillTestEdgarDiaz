package drink.blendedFruit;

import java.math.BigDecimal;
import java.util.Map;

import drink.blendedFruit.DrinkMachine.OutOfStockException;

public final class DrinkMachineUtil {

	private static DrinkMachineUtil INSTANCE;

	private DrinkMachineUtil() {
	}

	public static DrinkMachineUtil getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DrinkMachineUtil();
		}

		return INSTANCE;
	}

	public void restock(Map<Ingredient, Integer> stock) {

		for (Ingredient ingredient : Ingredient.values()) {
			if (this.getStock(stock, ingredient) < 2000) {
				stock.put(ingredient, 2000);
			}
		}
	}

	private int getStock(Map<Ingredient, Integer> stock, Ingredient ingredient) {
		return stock.containsKey(ingredient) ? stock.get(ingredient) : 0;
	}

	public void getMenu(Map<Ingredient, Integer> stock) {
		System.out.println("\n\n***********  Menu:  ************\n");
		int i = 0;
		for (Drink drink : Drink.values()) {
			String drinkName = ++i + "".concat(": ").concat(drink.toString());
			System.out.println(drinkName);

			String drinkPrices = "\tPrices --->";

			for (Size size : Size.values()) {
				if (canMake(stock, drink, size)) {
					drinkPrices = drinkPrices.concat("\t" + size.toString() + ": $")
							.concat(drink.getPrice().multiply(new BigDecimal(size.getFactor())).toString());
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
		stock.forEach((k, v) -> System.out.println("Item : " + k + " = " + v));
	}

	private boolean canMake(Map<Ingredient, Integer> stock, Drink drink, Size size) {

		for (Map.Entry<Ingredient, Integer> quantStuff : drink.getRecipe().entrySet()) {
			if (DrinkMachineUtil.getInstance().getStock(stock,
					quantStuff.getKey()) < getQuantityIngredient(size.getFactor(), quantStuff.getValue())) {
				return false;
			}
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
				order.getRecipe().forEach(
						(k, v) -> stock.put(k, getStock(stock, k) - getQuantityIngredient(size.getFactor(), v)));
			} else {
				System.out.println("Out of stock:: " + order.toString());
				throw new OutOfStockException(order);
			}

			System.out.println("******   Drink made: " + order + " " + size.toString() + "****");

		} else {
			System.out.println("Invalid input: " + inputs);
		}

	}

	private int getQuantityIngredient(int factor, Integer quantity) {
		return factor * quantity;
	}
}
