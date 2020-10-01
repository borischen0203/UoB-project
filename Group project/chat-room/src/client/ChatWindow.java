package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import common.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatWindow {
	private User client, friend;
	private Stage stage;
	private VBox talkHistory;

	public ChatWindow(User client, User friend) {
		this.client = client;
		this.friend = friend;
		this.stage = new Stage();
		this.talkHistory = new VBox();
		createChatWindowStage();
	}

	public ChatWindow() {
	}

	private ScrollPane scrollPane;

	// private TextArea chatText;

	public void createChatWindowStage() {
		Group group = new Group();

		VBox allcontent = new VBox();

		VBox top = new VBox();
		top.getStyleClass().add("top");
		Label friendName = new Label(friend.getUserName());
		friendName.getStyleClass().add("friendName");
		HBox keyword = new HBox();
		keyword.setAlignment(Pos.CENTER);
		keyword.getStyleClass().add("keyword");
		TextField keywordField = new TextField();
		Button keywordButton = new Button("search keyword");
		keyword.getChildren().add(keywordField);
		keyword.getChildren().add(keywordButton);
		top.getChildren().add(friendName);
		top.getChildren().add(keyword);

		VBox middle = new VBox();
		talkHistory.setId("talkHistoryTo" + friend.getUserID());
		talkHistory.getStyleClass().add("talkHistory");
		// ScrollPane scrollPane = new ScrollPane(talkHistory);
		scrollPane = new ScrollPane(talkHistory);
		scrollPane.setFitToHeight(true);
		scrollPane.setVvalue(100.0);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		middle.getChildren().add(scrollPane);

		VBox bottom = new VBox();
		bottom.getStyleClass().add("bottom");
		HBox bottomHBox = new HBox();
		bottomHBox.getStyleClass().add("bottomHBox");
		TextArea chatText = new TextArea();
		// chatText = new TextArea();
		chatText.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					postMessage(chatText.getText());
					chatText.setText("");
					setScrollPaneLocation();
					ke.consume();
				}
			}
		});
		chatText.setWrapText(true);
		chatText.getStyleClass().add("chatText");
		Button emojiButton = new Button();
		emojiButton.getStyleClass().add("emojiButton");
		emojiButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				postMessage("emoji");
				setScrollPaneLocation();
			}
		});

		Button messageButton = new Button();
		messageButton.getStyleClass().add("messageButton");
		/**
		 * Send message function.
		 */
		messageButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				postMessage(chatText.getText());
				chatText.setText("");
				setScrollPaneLocation();
			}
		});

		// Add function here! 2020-03-15
		Button reloadButton = new Button();
		reloadButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				// reloadPreviousTalk();
			}

		});
		bottomHBox.getChildren().add(emojiButton);
		bottomHBox.getChildren().add(messageButton);
		bottomHBox.getChildren().add(reloadButton);
		bottom.getChildren().add(bottomHBox);
		bottom.getChildren().add(chatText);

		allcontent.getChildren().add(top);
		allcontent.getChildren().add(middle);
		allcontent.getChildren().add(bottom);

		group.getChildren().add(allcontent);

		Scene scene2 = new Scene(group, 450, 600);
		scene2.getStylesheets().add(getClass().getResource("./css/chat.css").toExternalForm());
		this.stage = new Stage();
		this.stage.setScene(scene2);
		this.stage.setTitle("From:" + client.getUserID() + " / To:" + friend.getUserID());
	}

	public Stage getStage() {
		return this.stage;
	}

	public void setScrollPaneLocation() {
		this.scrollPane.setVvalue(1.0);
		System.out.println(this.scrollPane.getVvalue());
	}

	private boolean canReload = true;

	void reloadPreviousTalk(ArrayList<Message> previousTalk) {
		System.out.println("reloadPreviousTalk(), size:" + previousTalk.size());
		if (canReload) {
			canReload = false;
			previousTalk.forEach(talk -> {
				System.out.println(talk.getSender() + " : " + friend.getUserID());
				if (talk.getSender().equals(friend.getUserID())) {
					receiveMessage(talk.getContain());
				} else {
					sendMessage(talk.getContain());
				}

			});
		}
	}

	public void postMessage(String message) {
		talkHistory.getChildren().add(createSpeechBubble(message, Pos.BASELINE_RIGHT));
		Message m = new Message();

		m.setMessageType(MessageType.message_comm_mes);
		m.setUser(client);
		m.setReceiveUser(friend);
		m.setSender(client.getUserID());
		m.setRecipient(friend.getUserID());// friednName
		m.setContain(message); // message contain
		m.setSendTime();
		System.out.println("-----\nFrom:" + client + "\nTo: " + friend + "\nMessage: " + message + "\n-----");
		try {
			ObjectOutputStream mouth = new ObjectOutputStream(
					(ClientController.getServerThread(m.getSender()).getS()).getOutputStream());
			mouth.writeObject(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String message) {
		final String string = message;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update UI here.
				talkHistory.getChildren().add(createSpeechBubble(string, Pos.BASELINE_RIGHT));
			}
		});
		setScrollPaneLocation();
	}

	public void receiveMessage(String message) {
		final String string = message;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Update UI here.
				talkHistory.getChildren().add(createSpeechBubble(string, Pos.BASELINE_LEFT));
			}
		});
		setScrollPaneLocation();
	}

	public VBox createSpeechBubble(String message, Pos position) {
		VBox textPhrase = new VBox();
		if (!message.equals("")) {
			textPhrase.getStyleClass().add("bubble");
			textPhrase.setAlignment(position);
			switch (message) {
				case "emoji": {
					Image image = null;
					try {
						image = new Image(new FileInputStream("./bin/client/images/thumbUp_180x102.png"));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ImageView imageView = new ImageView(image);
					textPhrase.getChildren().add(imageView);
					break;
				}
				default: {
					Label text = new Label(message);
					if (position == Pos.BASELINE_RIGHT)
						text.getStyleClass().add("clientBubbleSpeech");
					else
						text.getStyleClass().add("friendBubbleSpeech");
					textPhrase.getChildren().add(text);
					break;
				}
			}
		}
		return textPhrase;
	}
}
