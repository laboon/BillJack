package com.laboon.billjack.client;

import com.laboon.billjack.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BillJack implements EntryPoint {
	
	private String _name = "foo";
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side BillJack service.
	 */
	private final BillJackServiceAsync billJackService = GWT
			.create(BillJackService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox htmlBox = new DialogBox();
		htmlBox.setPopupPosition(300, 300);
		htmlBox.setModal(false);
		htmlBox.setWidth("1000px");
		htmlBox.setVisible(true);
		htmlBox.setTitle("Game");
		// dialogBox.setAnimationEnabled(true);
		final Button stayButton = new Button("Stay");
		final Button hitButton = new Button("Hit");
		final Button doubleDownButton = new Button("Double Down");
		final Button splitButton = new Button("Split");
		final Button newHandButton = new Button("New Hand");
		final Button newGameButton = new Button("New Game");
		stayButton.getElement().setId("stayButton");
		hitButton.getElement().setId("hitButton");
		newHandButton.getElement().setId("newHandButton");
		newGameButton.getElement().setId("newGameButton");
		newHandButton.setVisible(false);
		newGameButton.setVisible(false);
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		HorizontalPanel dialogVPanel = new HorizontalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(hitButton);
		dialogVPanel.add(stayButton);
		dialogVPanel.add(newHandButton);
		dialogVPanel.add(newGameButton);
		htmlBox.setWidget(dialogVPanel);
		
		newHandButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				billJackService.newHand(
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								htmlBox.setTitle("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								htmlBox.show();
								stayButton.setFocus(true);
							}

							// This is what happens after the first deal
							
							public void onSuccess(String result) {
								htmlBox.setTitle("Blackjack Game");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								htmlBox.show();
								
								if (result.contains("blackjack")) {
									hitButton.setVisible(false);
									stayButton.setVisible(true);
									doubleDownButton.setVisible(false);
									
								}
								stayButton.setVisible(true);
								hitButton.setVisible(true);
								doubleDownButton.setVisible(true);
								if (result.contains("split")) {
									splitButton.setVisible(true);
								} else {
									splitButton.setVisible(false);
								}
								newHandButton.setVisible(false);
								newHandButton.setFocus(true);
								stayButton.setFocus(true);
							}
						});
			}
		});
		
		newGameButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				billJackService.startGame(_name,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								htmlBox.setTitle("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								htmlBox.show();
								stayButton.setFocus(true);
							}

							// This is the first hand of a new game
							
							public void onSuccess(String result) {
								htmlBox.setTitle("Blackjack Game");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								htmlBox.show();
								
								stayButton.setVisible(true);
								hitButton.setVisible(true);
								newHandButton.setVisible(false);
								stayButton.setFocus(true);
								newGameButton.setVisible(false);
							
							}
						});
			}
		});

		
		hitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				billJackService.hit(
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								htmlBox.setTitle("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								htmlBox.show();
								stayButton.setFocus(true);
							}

							// This is any non-first deal
							
							public void onSuccess(String result) {
								htmlBox.setTitle("Blackjack Game");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								htmlBox.show();
								// Split and double down are only available on first dealt card
								splitButton.setVisible(false);
								doubleDownButton.setVisible(false);
								if (result.contains("money")) {
									stayButton.setVisible(false);
									hitButton.setVisible(false);
									newHandButton.setVisible(false);
									newGameButton.setVisible(true);
									newGameButton.setFocus(true);
								} else {
									if (result.contains("BUSTED")) {
										stayButton.setVisible(false);
										hitButton.setVisible(false);
										newHandButton.setVisible(true);
										newHandButton.setFocus(true);
										newGameButton.setVisible(false);
									} else {
										stayButton.setVisible(true);
										hitButton.setVisible(true);
										newHandButton.setVisible(false);
										stayButton.setFocus(true);
										newGameButton.setVisible(false);
									}
								}
							}
						});
			}
		});
		
		// Add a handler to close the DialogBox
		
		stayButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				billJackService.stay(
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								htmlBox.setTitle("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								htmlBox.show();
								stayButton.setFocus(true);
							}

							public void onSuccess(String result) {
								htmlBox.setTitle("Blackjack Game");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								htmlBox.show();
								// Split and double down are only available on first dealt card
								splitButton.setVisible(false);
								doubleDownButton.setVisible(false);
								
								if (result.contains("money")) {
									stayButton.setVisible(false);
									hitButton.setVisible(false);
									newHandButton.setVisible(false);
									newGameButton.setVisible(true);
									newGameButton.setFocus(true);
								} else {
									if (result.contains("BUSTED")) {
										stayButton.setVisible(false);
										hitButton.setVisible(false);
										newHandButton.setVisible(true);
										newHandButton.setFocus(true);
										newGameButton.setVisible(false);
									} else {
										stayButton.setVisible(false);
										hitButton.setVisible(false);
										newHandButton.setVisible(true);
										newHandButton.setFocus(true);
										newGameButton.setVisible(false);
									}
								}
							}
						});
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}
			
			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				System.out.println("Sending name to server...");
				
				// First, we validate the input.
				// errorLabel.setText("Chirp");
				String textToServer = nameField.getText();
				_name = textToServer;
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least one character.");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				billJackService.startGame(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								htmlBox.setTitle("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								htmlBox.show();
								stayButton.setFocus(true);
							}

							public void onSuccess(String result) {
								htmlBox.setTitle("Blackjack Game");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								htmlBox.show();
								stayButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
