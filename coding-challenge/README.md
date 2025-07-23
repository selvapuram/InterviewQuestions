# Coding Challenge - Checkout

This exercise describes pricing schemes and requirements for enhanceability for a system
that calculates the total cost of a shopping cart based on a set of pricing rules. The items in
a supermarket are identified by using Stock Keeping Units or SKUs. For this, we will use
individual letters of the alphabet (e.g. A,B,C) and the items are priced individually. In
addition to that some items adhere to discounts: buy n of them and they will cost you y
Euro and one item has a discount on a special date.
_For example_: Item 'A' costs 40 Cent individually, but there is a special offer: buy three 'A's
and they will cost you 1 Euro.

| SKU | Unit Price |                   Special Price Rule |
|-----|:----------:|-------------------------------------:|
| A   |     40     |                            3 for 100 |
| B   |     50     |                             2 for 80 |
| C   |     25     |                                      |
| D   |     20     |                                      |   
| E   |    100     | 10% off on Black Friday (27.11.2020) |   


The checkout should accept items in any order. E.g. so if we scan **B**, **A** and another B, it will
recognize the two **B**'s and price them at 80 Cent, in order to result in a total price of 1,20
Euro.
Because the pricing can change frequently or new discount types need to be implemented,
we need to be able to pass in a set of pricing rules each time we start handling a checkout
transaction.

The interface to the checkout should look like this:
```
var checkout = new CheckOut(pricing_rules);
checkout.scan(item);
checkout.scan(item);
price = checkout.total();
```

Here is a Unit test in Java. The helper method calculatePrice let’s you specify a sequence of
items (using a string), calling the checkout's scan method for each item before returning
the total price:

```
public class TestPrice {
public int calculatePrice(String goods) {
CheckOut checkout = new CheckOut(rule);
for(int i=0; i<goods.length(); i++) {
checkout.scan(String.valueOf(goods.charAt(i)));
}
return checkout.total();
}
@Test
public void totals() {
assertEquals(0, calculatePrice(""));
assertEquals(40, calculatePrice("A"));
assertEquals(90, calculatePrice("AB"));
assertEquals(135, calculatePrice("CDBA"));
assertEquals(80, calculatePrice("AA"));
assertEquals(100, calculatePrice("AAA"));
assertEquals(140, calculatePrice("AAAA"));
assertEquals(180, calculatePrice("AAAAA"));
assertEquals(200, calculatePrice("AAAAAA"));
assertEquals(150, calculatePrice("AAAB"));
assertEquals(180, calculatePrice("AAABB"));
assertEquals(200, calculatePrice("AAABBD"));
assertEquals(200, calculatePrice("DABABA"));
}
@Test
public void incremental() {
CheckOut checkout = new CheckOut(rule);
assertEquals(0, checkout.total);
checkout.scan("A"); assertEquals(40, checkout.total);
checkout.scan("B"); assertEquals(90, checkout.total);
checkout.scan("A"); assertEquals(130, checkout.total);
checkout.scan("A"); assertEquals(150, checkout.total);
checkout.scan("B"); assertEquals(180, checkout.total);
}
}
```

The exercise doesn’t mention the format of the pricing rules:
* How can these be specified in such a way that the checkout doesn’t know about particular
items and their pricing strategies?
* How can we make the design flexible enough so that we can add new styles of pricing rules
and discounts in the future?
* Please add your own tests for sku E

## Getting Started

The code for a checkout system with a pricing schema in a supermarket is being implemented. In order to make
reviewing easier, the application uses below pre-requisites


### Prerequisites
* Java 8+
* Gradle

### Design

Here is a high level design depicts the flow and execution

![image info](./checkout.svg)

## Running the tests

As stated in the problem statement, There is a test class [TestPrice.java](./src/test/java/idealo/TestPrice.java)
is written to test the totals, incremental and for Sku E type.

The Gradle Wrapper is now available for building your project. Add it to your version control system, and everyone that clones your project can build it just the same. It can be used in the exact same way as an installed version of Gradle. Run the wrapper script to perform the build task, just like you did previously:

```
./gradlew build
```

```
./gradlew test
```

## Trade offs, Further Improvements
* A Rule Engine [JSR 94 compliant](https://www.jcp.org/en/jsr/detail?id=94) could be created to design flexibility in the rules  E.g Drools
* Rules could be configured as a privileged service so that it is isolated from checkout initialization.
* Rules could be parsed from configuration file e.g JSON files
* Discount [pro-ration](https://docs.oracle.com/cd/E95327_01/oroms/pdf/5/cws_help/by1041428.htm#Rby33786) logic could have been applied at sku level.
* Validations are missed in the current implementation, For eg: quantity > 0 and not null
