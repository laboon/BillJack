package com.laboon.billjack.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface BillJackService extends RemoteService {
	String startGame(String name) throws IllegalArgumentException;
	String hit();
	String stay();
	String newHand();
	Integer getPlayerVal();
}
