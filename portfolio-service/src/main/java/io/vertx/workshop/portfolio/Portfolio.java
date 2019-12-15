package io.vertx.workshop.portfolio;

import java.util.Map;
import java.util.TreeMap;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Structure representing a portfolio. It stores the available cash and the owned shares.
 * 
 */
@DataObject(generateConverter = true)
public class Portfolio {

    private Map<String, Integer> shares = new TreeMap<>();

    private double cash;

    /**
     * Creates a new instance of {@link Portfolio}.
     */
    public Portfolio() {
        // Empty constructor
    }

    /**
     * Creates a new instance of {@link Portfolio} by copying the other instance
     * 
     * @param other         the instance to copy
     */
    public Portfolio(Portfolio other) {
        this.shares = new TreeMap<>(other.shares);
        this.cash = other.cash;
    }

    /**
     * Creates a new instance of {@link Portfolio} from this json object.
     * 
     * @param json          the Json object
     */
    public Portfolio(JsonObject json) {
        // A converter is generated to easy the conversion from and to Json.
        PortfolioConverter.fromJson(json, this);
    }

    /**
     * return a Json representation of the portfolio computed using the converter.
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        PortfolioConverter.toJson(this, json);
        return json;
    }

    /**
     * @return the owned shared (name -> number)
     *  
     */
    public Map<String, Integer> getShares() {
        return shares;
    }

    /**
     * Sets the owned shares. Method used by the converter.
     * 
     * @param shares        the shares
     * @return the current {@link Portfolio}
     */
    public Portfolio setShares(Map<String, Integer> shares) {
        this.shares = shares;
        return this;
    }

    /**
     * @return the available cash
     */
    public double getCash() {
        return cash;
    }

    /**
     * Sets the available cash. Method used by the converter.
     * 
     * 
     * @param cash the cash
     * @return the current {@link Portfolio}
     */
    public Portfolio setCash(double cash) {
        this.cash = cash;
        return this;
    }

    // --- Additional method

    /**
     * This method is just a convenient method to get the number of owned shares of the specific company
     * (name of the company)
     * 
     * @param name      the name of the company
     * @return          the number of owned shares, {@literal 0} is none
     */
    public int getAmount(String name) {
        Integer current = shares.get(name);
        if (current == null) {
            return 0;
        }
        return current;
    }
    

}





















