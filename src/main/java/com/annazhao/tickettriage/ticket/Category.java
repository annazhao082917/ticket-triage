package com.annazhao.tickettriage.ticket;

/**
 * The queue a ticket belongs in.
 *
 * <p>A fixed set keeps the label spelled the same way in Claude's answer and
 * in our code. A free string would let the model invent new buckets.
 */
public enum Category {

	/** Invoices, charges, refunds, and payment methods. */
	BILLING,

	/** Something in the product is broken or confusing. */
	TECHNICAL,

	/** Login, profile, password, or permissions. */
	ACCOUNT,

	/** A request for something the product does not do yet. */
	FEATURE_REQUEST,

	/** Does not fit the buckets above. */
	OTHER

}
