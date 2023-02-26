/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 * 
 * Permission has been explicitly granted to the University of Minnesota 
 * Software Engineering Center to use and distribute this source for 
 * educational purposes, including delivering online education through
 * Coursera or other entities.  
 * 
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including 
 * fitness for purpose.
 * 
 * 
 * Modified 20171114 by Ian De Silva -- Updated to adhere to coding standards.
 * 
 */
package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import edu.ncsu.csc326.coffeemaker.CoffeeMaker;
import edu.ncsu.csc326.coffeemaker.UICmd.AddInventory;
import edu.ncsu.csc326.coffeemaker.UICmd.CheckInventory;
import edu.ncsu.csc326.coffeemaker.UICmd.ChooseRecipe;
import edu.ncsu.csc326.coffeemaker.UICmd.ChooseService;
import edu.ncsu.csc326.coffeemaker.UICmd.Command;
import edu.ncsu.csc326.coffeemaker.UICmd.DescribeRecipe;
import edu.ncsu.csc326.coffeemaker.UICmd.InsertMoney;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains the step definitions for the cucumber tests. This parses the Gherkin
 * steps and translates them into meaningful test steps.
 */
public class TestSteps {

    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;
    private Recipe recipe4;
    private Recipe recipe5;
    private CoffeeMakerUI coffeeMakerMain;
    private CoffeeMaker coffeeMaker;
    private RecipeBook recipeBook;
    private ChooseService chooseService;
    private int selectedRecipeIndex;

    private Recipe recipeAux;

    private void initialize() {
        recipeBook = new RecipeBook();
        coffeeMaker = new CoffeeMaker(recipeBook, new Inventory());
        coffeeMakerMain = new CoffeeMakerUI(coffeeMaker);
        selectedRecipeIndex = -1;
        recipeAux = null;
    }

    @Given("an empty recipe book")
    public void an_empty_recipe_book() throws Throwable {
        initialize();
    }

    @Given("a default recipe book")
    public void a_default_recipe_book() throws Throwable {
        initialize();

        //Set up for r1
        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setAmtChocolate("0");
        recipe1.setAmtCoffee("3");
        recipe1.setAmtMilk("1");
        recipe1.setAmtSugar("1");
        recipe1.setPrice("50");

        //Set up for r2
        recipe2 = new Recipe();
        recipe2.setName("Mocha");
        recipe2.setAmtChocolate("20");
        recipe2.setAmtCoffee("3");
        recipe2.setAmtMilk("1");
        recipe2.setAmtSugar("1");
        recipe2.setPrice("75");

        //Set up for r3
        recipe3 = new Recipe();
        recipe3.setName("Latte");
        recipe3.setAmtChocolate("0");
        recipe3.setAmtCoffee("3");
        recipe3.setAmtMilk("3");
        recipe3.setAmtSugar("1");
        recipe3.setPrice("100");

        //Set up for r4
        recipe4 = new Recipe();
        recipe4.setName("Hot Chocolate");
        recipe4.setAmtChocolate("4");
        recipe4.setAmtCoffee("0");
        recipe4.setAmtMilk("1");
        recipe4.setAmtSugar("1");
        recipe4.setPrice("65");

        //Set up for r5 (added by MWW)
        recipe5 = new Recipe();
        recipe5.setName("Super Hot Chocolate");
        recipe5.setAmtChocolate("6");
        recipe5.setAmtCoffee("0");
        recipe5.setAmtMilk("1");
        recipe5.setAmtSugar("1");
        recipe5.setPrice("100");

        recipeBook.addRecipe(recipe1);
        recipeBook.addRecipe(recipe2);
        recipeBook.addRecipe(recipe3);
        recipeBook.addRecipe(recipe4);

    }

    @Given("I selected the option {int}")
    public void i_selected_the_option(Integer int1) {
        Command command = new ChooseService(int1);
        coffeeMakerMain.UI_Input(command);
    }

    @When("I select the recipe {int} to edit")
    public void i_select_the_recipe_to_edit(Integer int1) {

        Command command = new ChooseRecipe(int1);
        coffeeMakerMain.UI_Input(command);
        selectedRecipeIndex = int1;
    }
    
   @When("I enter the new recipe new price {string} coffee units {string} sugar units {string} milk units {string} and {string} chocolate units")
    public void i_enter_the_new_recipe_new_price_coffee_units_sugar_units_milk_units_and_chocolate_units(String price, String coffeUnits, String sugarUnits, String milkUnits, String chocolateUnits) {

        try {
            recipeAux = new Recipe();
            recipeAux.setName("Edicion");
            recipeAux.setPrice(price);
            recipeAux.setAmtCoffee(coffeUnits);
            recipeAux.setAmtSugar(sugarUnits);
            recipeAux.setAmtMilk(milkUnits);
            recipeAux.setAmtChocolate(chocolateUnits);
        } catch (RecipeException ex) {
            Logger.getLogger(TestSteps.class.getName()).log(Level.SEVERE, null, ex);
        }

        Command command = new DescribeRecipe(recipeAux);
        coffeeMakerMain.UI_Input(command);

    }

    @When("I check the inventory")
    public void i_check_the_inventory() {
        Command command = new CheckInventory();
        coffeeMakerMain.UI_Input(command);
    }

    @Then("the recipe {int} called {string} is modified")
    public void the_recipe_called_is_modified(Integer int1, String string) {
        Recipe actual = coffeeMakerMain.getRecipes()[int1];

        assertEquals(string, actual.getName());
        assertEquals(recipeAux.getPrice(), actual.getPrice());
        assertEquals(recipeAux.getAmtCoffee(), actual.getAmtCoffee());
        assertEquals(recipeAux.getAmtSugar(), actual.getAmtSugar());
        assertEquals(recipeAux.getAmtMilk(), actual.getAmtMilk());
        assertEquals(recipeAux.getAmtChocolate(), actual.getAmtChocolate());

    }

    @Then("the status is {string}")
    public void the_status_is(String string) {
        assertEquals(string, coffeeMakerMain.getStatus().toString());
    }

    @Then("the mode result is {string}")
    public void the_mode_result_is(String string) {
        assertEquals(string, coffeeMakerMain.getMode().toString());
    }

    @Then("the inventory results are {string} coffe units {string} sugar units {string} milk units and {string} chocolate units")
    public void the_inventory_results_are_coffe_units_sugar_units_milk_units_and_chocolate_units(String coffe, String sugar, String milk, String chocolate) {
        assertEquals("Coffee: " + coffe + "\nMilk: " + milk + "\nSugar: " + sugar + "\nChocolate: " + chocolate + "\n", coffeeMaker.checkInventory());
    }

    @When("the user presses {int}")
    public void the_user_presses(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        Command cmd = new ChooseService(int1);
        coffeeMakerMain.UI_Input(cmd);
    }

    @Then("the mode is {string}")
    public void the_mode_is(String string) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(string, coffeeMakerMain.getMode().toString());
    }

    @When("I add a recipe")
    public void i_add_a_recipe() throws RecipeException {
        // Write code here that turns the phrase above into concrete actions
        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setAmtChocolate("0");
        recipe1.setAmtCoffee("3");
        recipe1.setAmtMilk("1");
        recipe1.setAmtSugar("1");
        recipe1.setPrice("50");
        Command cmd2 = new ChooseService(1);
        coffeeMakerMain.UI_Input(cmd2);
        Command cmd = new DescribeRecipe(recipe1);
        coffeeMakerMain.UI_Input(cmd);
        
    }

    @When("I delete recipe {int}")
    public void i_delete_recipe(int numberRecipe) {
       
        Command cmd = new ChooseService(2);
        coffeeMakerMain.UI_Input(cmd);
        Command cmd2 = new ChooseService(numberRecipe);
        coffeeMakerMain.UI_Input(cmd2);
        
    }

    @Then("the number of recipes is {int}")
    public void the_number_of_recipes_is(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        Integer x = 0;
        Recipe[] recipes = coffeeMaker.getRecipes();
        for (Recipe recipe : recipes) {
            if (recipe != null) {
                x += 1;
            }
        }
        assertEquals(int1, x);
    }

    @When("I add {int} coffee, {int} milk, {int} sugar, {int} chocolate")
    public void i_add_coffee_milk_sugar_chocolate(int coffee, int milk, int sugar, int chocolate) {
        // Write code here that turns the phrase above into concrete actions
        Command cmd = new ChooseService(4);
        coffeeMakerMain.UI_Input(cmd);
        Command cmd2 = new AddInventory(coffee, milk, sugar, chocolate);
        coffeeMakerMain.UI_Input(cmd2);
    }
    
    @When("I insert {int}")
    public void i_insert(int money) {
        // Write code here that turns the phrase above into concrete actions
        Command cmd = new InsertMoney(money);
        coffeeMakerMain.defaultCommands(cmd);
        if (money >= 0) {
            assertEquals(money, coffeeMakerMain.getMoneyInserted());
        }
    }

    @When("purchases recipe {int}")
    public void purchases_recipe(int recipe) {
        // Write code here that turns the phrase above into concrete actions
        Command cmd = new ChooseService(6);
        coffeeMakerMain.UI_Input(cmd);
        Command cmd2 = new ChooseRecipe(recipe);
        coffeeMakerMain.UI_Input(cmd2);
    }

    @When("I add four recipes")
    public void i_add_four_recipes() throws RecipeException {
        // Write code here that turns the phrase above into concrete actions
        //Set up for r1
        recipe1 = new Recipe();
        recipe1.setName("Coffee");
        recipe1.setAmtChocolate("0");
        recipe1.setAmtCoffee("3");
        recipe1.setAmtMilk("1");
        recipe1.setAmtSugar("1");
        recipe1.setPrice("50");

        //Set up for r2
        recipe2 = new Recipe();
        recipe2.setName("Mocha");
        recipe2.setAmtChocolate("20");
        recipe2.setAmtCoffee("3");
        recipe2.setAmtMilk("1");
        recipe2.setAmtSugar("1");
        recipe2.setPrice("75");

        //Set up for r3
        recipe3 = new Recipe();
        recipe3.setName("Latte");
        recipe3.setAmtChocolate("0");
        recipe3.setAmtCoffee("3");
        recipe3.setAmtMilk("3");
        recipe3.setAmtSugar("1");
        recipe3.setPrice("100");

        //Set up for r4
        recipe4 = new Recipe();
        recipe4.setName("Hot chocolate");
        recipe4.setAmtChocolate("4");
        recipe4.setAmtCoffee("0");
        recipe4.setAmtMilk("1");
        recipe4.setAmtSugar("1");
        recipe4.setPrice("65");

        coffeeMaker.addRecipe(recipe1);
        coffeeMaker.addRecipe(recipe2);
        coffeeMaker.addRecipe(recipe3);

        Command cmd2 = new ChooseService(1);
        coffeeMakerMain.UI_Input(cmd2);
        Command cmd = new DescribeRecipe(recipe4);
        coffeeMakerMain.UI_Input(cmd);

        

        

    }

    @When("recipe {int} is empty")
    public void recipe_is_empty(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("I delete recipe {string}")
    public void i_delete_recipe(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the money in tray is {int}")
    public void the_money_in_tray_is(int money) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(money, coffeeMakerMain.getMoneyInTray());
    }

    @Then("the money inserted is {int}")
    public void the_money_inserted_is(int money) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(money, coffeeMakerMain.getMoneyInserted());
    }
    
    
    @Then("the inventory results are {int} coffee, {int} milk, {int} sugar, {int} chocolate")
    public void the_inventory_results_are_coffee_milk_sugar_chocolate(int coffee, int milk, int sugar, int chocolate) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals("Coffee: " + coffee + "\nMilk: " + milk + "\nSugar: " + sugar + "\nChocolate: " + chocolate + "\n", coffeeMaker.checkInventory());
    }

    @Then("the inventory results are {string} coffee units {string} sugar units {string} milk units and {string} chocolate units")
    public void the_inventory_results_are_coffee_units_sugar_units_milk_units_and_chocolate_units(String string, String string2, String string3, String string4) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals("Coffee: " + string + "\nMilk: " + string2 + "\nSugar: " + string3 + "\nChocolate: " + string4 + "\n", coffeeMaker.checkInventory());
    }

}
