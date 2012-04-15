package com.laboon.billjack.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>BillJackService</code>.
 */
public interface BillJackServiceAsync {
	void startGame(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void hit(AsyncCallback<String> callback);
	void stay(AsyncCallback<String> callback);
	void newHand(AsyncCallback<String> callback);
	void getPlayerVal(AsyncCallback<Integer> callback);
}
