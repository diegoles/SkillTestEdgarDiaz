package drink.blended_fruit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import drink.blended_fruit.enums.Drink;
import drink.blended_fruit.enums.Ingredient;
import drink.blended_fruit.utils.DrinkMachineUtil;

public class DrinkMachine {

	private static Map<Ingredient, Integer> stock = new HashMap<>();

	public static void main(String[] args) {

		System.out.println("q-> Exit	r-> Restock		m->Menu 	i->Inventory");

		DrinkMachine machine = new DrinkMachine();
		DrinkMachineUtil.getInstance().restock(stock);
		DrinkMachineUtil.getInstance().getInventory(stock);
		DrinkMachineUtil.getInstance().getMenu(stock);
		machine.startIO();
	}

	public void startIO() {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.print("\nChoose an option from the menu (drink,size): \n");
			try {
				String[] inputs = reader.readLine().toLowerCase().split(",");

				switch (inputs[0].trim()) {
				case "":
					break;
				case "q":
					System.exit(0);
					break;
				case "r":
					DrinkMachineUtil.getInstance().restock(stock);
					System.out.println("Restock!!!! ");
					break;
				case "m":
					DrinkMachineUtil.getInstance().getMenu(stock);
					break;
				case "i":
					DrinkMachineUtil.getInstance().getInventory(stock);
					break;

				default:
					DrinkMachineUtil.getInstance().make(stock, inputs);
					break;
				}

			} catch (OutOfStockException e) {
				System.out.println("Please restock!!!! ");
			} catch (NumberFormatException e) {
				System.out.println("XXX  Invalid selection");
			} catch (IOException e) {
				System.out.println("XXX  Invalid input");
			}
		}
	}

	@SuppressWarnings("serial")
	public static class OutOfStockException extends RuntimeException {
		public OutOfStockException(Drink drink) {
			super(drink.toString());
		}
	}
}
