Can you type here? → 



We are part of the credit team, and one of our responsibilities is to perform some validation each time that a user tries to make a payment. As part of temporary risk mitigation the Product Team requests us to apply two measures right away to prevent the company from losing money further.

No client can spend more than USD 1,000 in total.

Example, User “A”: 
Payment1: USD 400
Payment2: USD 600
Payment3: USD 50 -> This payment must be rejected since the user already spent USD 1,000. 

We've realized that when a user tries to make a payment that is at least double the amount of their highest previous payment, it's indicative of a stolen card. We must block these users.

Example, User “B”: 
Payment1: USD 150 
Payment2: USD 50 
Payment3: USD 100
Payment4: USD 401 -> This payment must be rejected and the user must be blocked. They can’t spend more money.


Whenever a user attempts a payment, the method reserveMoneyForPayment  is invoked. If this method returns false, the purchase is rejected. Otherwise, the purchase is approved, and the reservation must be saved.

Available tecnologies:
PostgreSQL
Java 11
Redis
Spring Boot/Spring JPA

public Class CreditCardPaymentDto {
    private UUID userId;
    private UUID creditCardId;
    private Double amount;
    private Instant date;
}

@Entity
@Data
Public class Account {
@Id
@GeneratedValue
	Private Double id;
	Private UUID userId;
	Private Double highestPayment;
	Private Double balance;
	Private boolean blocked;
}


@Service
@RequiredArgsConstructor
public class CreditCardPaymentService {
	
	private static final Double maxAmountToSpend = 1000;
	
	Private static final Map<UUID, Double> paymentLookup = new ConcurrentHashMap<>();
	
Private final AccountRepository repository;	

// introduce validator

public boolean reserveMoneyForPayment (CreditCardPayment creditCardPaymentDto) {
UUID userId = creditCardPaymentDto.getUserId();
Double paymentSpent = creditCardPaymentDto.getAmount();
if(paymentLookup.containsKey(userId) {

	amountSpent = paymentLookup.get(userId) + paymentSpent;
     if(amountSpent > maxAmountToSpend) {
       // log error
		Return false;
}
paymentLookup.put(userId, amountSpent);
Optional<Account> account = repository.findByUserId(userId);
if(account.isPresent() && !account.isBlocked()) {
     Double twiceHighestPayment = account.get().getHighestPayment() * 2;
Double currentPayment = 
      if(paymentSpent >= twiceHighestPayment) {
		Account updatedAccount = account.get();
   		updatedAccount.setBlocked(true);
		repository.save(updatedAccount);
		Return false;
	}

} else {
	// throw account not found
}

else {
	
}
}
	
}
