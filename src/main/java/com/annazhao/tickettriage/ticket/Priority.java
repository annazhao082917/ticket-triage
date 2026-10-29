package com.annazhao.tickettriage.ticket;

/**
 * How quickly a person should look at the ticket.
 *
 * <p>Same idea as {@link Category}: the model must pick one of these names,
 * not a phrase like "soon".
 */
public enum Priority {

	/** Can wait in the normal queue. */
	LOW,

	/** Should be handled during the regular workday. */
	MEDIUM,

	/** Needs a person today. */
	HIGH,

	/** Needs a person now. */
	URGENT

}
