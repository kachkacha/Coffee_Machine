package machine;

import java.util.Scanner;

public class CoffeeMachine {

    public enum STATE {
        MENU, BUY, MAKING_COFFEE, FILL, ERROR, OFF;
    }
    private STATE state;

    private enum COFFEE {
        ESPRESSO(250, 0, 16, 4),
        LATTE(350, 75, 20, 7),
        CAPPUCCINO(200, 100, 12, 6);

        public final int water;
        public final int milk;
        public final int coffee_beans;
        public final int cost;

        COFFEE(int water, int milk, int coffee_beans, int cost) {
            this.water = water;
            this.milk = milk;
            this.coffee_beans = coffee_beans;
            this.cost = cost;
        }
    }

    private int money;
    private int water;
    private int milk;
    private int coffee_beans;
    private int cups;

    private int filling_step;
    private String[] filling_stage_messages = {"\nWrite how many ml of water do you want to add: ",
            "Write how many ml of milk do you want to add: ",
            "Write how many grams of coffee beans do you want to add: ",
            "Write how many disposable cups of coffee do you want to add: "};

    private String error_message;

    public CoffeeMachine(int water, int milk, int coffee_beans, int cups, int money) {
        this.state = STATE.MENU;
        this.water = water;
        this.milk = milk;
        this.coffee_beans = coffee_beans;
        this.cups = cups;
        this.money = money;
        this.filling_step = 0;
        print_message();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CoffeeMachine coffeeMachine = new CoffeeMachine(400, 540, 120, 9, 550);
        while (coffeeMachine.get_machine_state() != CoffeeMachine.STATE.OFF) {
            coffeeMachine.user_input(scanner.nextLine());
        }
        scanner.close();
    }

    private void menu(String action) {
        switch (action) {
            case "buy":
                state = STATE.BUY;
                break;
            case "fill":
                state = STATE.FILL;
                break;
            case "take":
                take();
                break;
            case "remaining":
                state();
                break;
            case "exit":
                state = STATE.OFF;
                return;
        }
    }

    private void buy(String input) {
        switch (input) {
            case "back":
                state = STATE.MENU;
                break;
            case "1":
                if (enough_resources(COFFEE.ESPRESSO)) {
                    state = STATE.MAKING_COFFEE;
                    make_coffee(COFFEE.ESPRESSO);
                }
                else state = STATE.ERROR;
                break;
            case "2":
                if (enough_resources(COFFEE.LATTE)) {
                    state = STATE.MAKING_COFFEE;
                    make_coffee(COFFEE.LATTE);
                }
                else state = STATE.ERROR;
                break;
            case "3":
                if (enough_resources(COFFEE.CAPPUCCINO)) {
                    state = STATE.MAKING_COFFEE;
                    make_coffee(COFFEE.CAPPUCCINO);
                }
                else state = STATE.ERROR;
                break;
            default:
                state = STATE.ERROR;
                error_message = "Invalid input returning to menu\n";
        }
    }

    private void make_coffee(COFFEE coffee) {
        this.cups--;
        this.water -= coffee.water;
        this.milk -= coffee.milk;
        this.coffee_beans -= coffee.coffee_beans;
        this.money += coffee.cost;
    }

    private boolean enough_resources(COFFEE coffee) {
        if (this.cups == 0) error_message = "Sorry, not enough cups!";
        else if (this.water < coffee.water) error_message = "Sorry, not enough water!";
        else if (this.milk < coffee.milk) error_message = "Sorry, not enough milk!";
        else if (this.coffee_beans < coffee.coffee_beans) error_message = "Sorry, not enough coffee beans!";
        else return true;
        return false;
    }

    private void fill(String input) {
        try {
            int amount = Integer.parseInt(input);
            switch (filling_step++) {
                case 0:
                    water += amount;
                    break;
                case 1:
                    milk += amount;
                    break;
                case 2:
                    coffee_beans += amount;
                    break;
                case 3:
                    cups += amount;
                    filling_step = 0;
                    state = STATE.MENU;
                    break;
            }
        } catch (NumberFormatException e) {
            state = STATE.ERROR;
            error_message = "Invalid input returning to menu\n";
        }
    }

    private void take () {
        money = 0;
        System.out.println("I gave you $" + money + "\n");
    }

    private void state() {
        System.out.printf("\nThe coffee machine has: \n" +
                "%d of water\n" +
                "%d of milk\n" +
                "%d of coffee beans\n" +
                "%d of disposable cups\n" +
                "%d of money\n\n", water, milk, coffee_beans, cups, money);
    }

    public void user_input(String input) {
        switch (state) {
            case MENU:
                menu(input);
                break;
            case BUY:
                buy(input);
                break;
            case FILL:
                fill(input);
            default:
                error_message = "Invalid state, returning to menu";
                state = STATE.ERROR;
        }
        print_message();
    }

    private void print_message() {
        switch (state) {
            case MENU:
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
                break;
            case BUY:
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
                break;
            case MAKING_COFFEE:
                System.out.println("I have enough resources, making you a coffee!");
                state = STATE.MENU;
                print_message();
                break;
            case FILL:
                System.out.println(filling_stage_messages[filling_step]);
                break;
            case ERROR:
                System.out.println(error_message);
                state = STATE.MENU;
                print_message();
                break;
            case OFF:
                error_message = "Invalid state, returning to menu";
                state = STATE.ERROR;
                print_message();
        }
    }

    public STATE get_machine_state() {
        return state;
    }
}
