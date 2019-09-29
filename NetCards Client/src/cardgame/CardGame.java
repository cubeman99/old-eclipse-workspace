package cardgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import client.ClientConnection;
import client.Message;
import client.MessageDecoder;
import client.Timer;
import client.main.GMath;
import client.main.Game;
import client.main.ImageLoader;
import client.main.Keyboard;
import client.main.Mouse;
import cardgame.gui.GUILayout;
import cardgame.gui.PlayerHolder;
import cardstructure.Card;
import cardstructure.CardHand;
import cardstructure.CardJudgeGroup;
import cardstructure.CardList;

public class CardGame {
	public static final int WINNER_WAIT_TIME      = 3000;
	public static final int REORGANIZE_CARDS_TIME = 400;
	public static final int DRAW_CARD_DELAY_TIME  = 400;
	public static final int DRAW_CARD_RATE        = 200;
	
	static ArrayList<CardJudgeGroup> judgementCardGroups;
	public static CardHand hand;
	public static Card categoryCard;
	public static Card nextCategoryCard;
	public static CardList mySubmission;
	public static String judgeName;
	public static String myName;
	public static int judgingScrollY = 0;
	public static boolean judgeScrolling = false;
	public static boolean ready;
	public static boolean playing = false;
	public static boolean judgementMode;
	public static boolean allGroupsFlipped;
	public static String winner;
	public static CardJudgeGroup winningJudgeGroup;
	public static Timer winnerWaitTimer;
	public static int judgeGroupViewIndex;
	public static boolean isAdmin;
	public static boolean gameStarted;
	public static boolean draggingCard;
	public static Card dragCard;
	public static Point dragOffsetPoint;
	public static Point dragStartPoint;
	public static Timer drawCardTimer;
	public static ArrayList<Card> cardsToDraw;
	public static boolean cardsOrganized;
	public static ArrayList<PlayerHolder> playerHolders;
	public static boolean started = false;
	public static boolean waitingForNextRound = false;
	public static boolean isInitialized = false;
	
	public static void initialize() {
		gameStarted         = false;
		judgementCardGroups = new ArrayList<CardJudgeGroup>();
		mySubmission        = new CardList();
		hand                = new CardHand();
		categoryCard        = null;
		nextCategoryCard    = null;
		judgeName           = "";
		judgementMode       = false;
		ready               = false;
		myName              = ClientConnection.clientName;
		allGroupsFlipped    = false;
		judgeGroupViewIndex = 0;
		drawCardTimer       = new Timer();
		winnerWaitTimer     = new Timer();
		winningJudgeGroup   = null;
		isAdmin             = ClientConnection.isAdmin;
		playerHolders       = new ArrayList<PlayerHolder>();
		draggingCard        = false;
		dragCard            = null;
		dragOffsetPoint     = new Point();
		cardsToDraw         = new ArrayList<Card>();
		cardsOrganized      = false;
		isInitialized       = true;
		
		MessageDecoder.decodeUnreadMessages();
	}
	
	public static void startGame() {
		started = true;
		playing = true;
		drawCardTimer.reset();
		drawCardTimer.start();
		cardsOrganized = false;
		
		if (GameRules.CONSOLE_SHOW_DEBUG)
			System.out.println("Starting Game...");
	}
	
	public static void nextTurn() {
		allGroupsFlipped    = false;
		ready               = false;
		waitingForNextRound = false;
		judgementMode       = false;
		judgeGroupViewIndex = 0;
		judgementCardGroups.clear();
		
		if (GameRules.CONSOLE_SHOW_DEBUG)
			System.out.println("Next Turn...");
		
		for (int i = 0; i < mySubmission.size(); i++) {
			hand.getCards().remove(mySubmission.get(i));
		}
		for (PlayerHolder ph : playerHolders) {
			ph.setReady(false);
		}
		
		
		mySubmission.clear();
		
		hand.requestNeededCards();
		
		if (!started)
			startGame();
	}
	
	
	//////////////////////
	//  UPDATE METHOD:  //
	//////////////////////
	
	public static void update() {
		
		if (playing) {
			// Update the winner screen timer:
			
			if (waitingForNextRound) {
				updateHand();
			}
			else if (winnerWaitTimer.running()) {
				if (winnerWaitTimer.pastTime(WINNER_WAIT_TIME)) {
					winnerWaitTimer.reset();
					categoryCard = nextCategoryCard;
					drawCardTimer.reset();
					drawCardTimer.start();
					cardsOrganized = false;
				}
			}
			else if (!judgementMode) {

				// Visual pleasure with drawing cards:
				updateHand();
				
				// HANDLE CLEINT SUBMISSIONS:
				if (!isJudge()) {
					// Set ready state:
					if (Keyboard.enter.pressed()) {
						setReadyState(!ready);
					}
					
					
					// Submission cards:
					for (int i = 0; i < mySubmission.size(); i++) {
						Point dp  = getSubmissionDrawPoint(i);
						Card card = mySubmission.get(i);

						if (card.mouseInArea(dp.x, dp.y) && Mouse.leftPressed()) {
							// Move card back to hand
							Card cc = mySubmission.get(i);
							mySubmission.remove(i);
							setReadyState(false);

							Point moveTo = getHandCardDrawPoint(cc.index);
							cc.setMovePosition(moveTo);
							cc.moveSpeed = GMath.distance(cc.getPoint(), moveTo) / 8.0;

							for (int j = 0; j < mySubmission.size(); j++) {
								mySubmission.get(j).moveSpeed = 20;
								mySubmission.get(j).setMovePosition(getSubmissionDrawPoint(j));
							}
						}
					}
					
					// Hand cards:
					for (int i = 0; i < hand.size(); i++) {
						Point dp  = getHandCardDrawPoint(i);
						Card card = hand.getCard(i);
						
						if (card.mouseInArea(dp.x, dp.y) && Mouse.leftPressed()) {
							if (!cardsToDraw.contains(card) && !card.isMoving() && !mySubmission.contains(card) && !submissionFull()) {
								// Move card to submission
								mySubmission.add(card);
								Point moveTo = getSubmissionDrawPoint(mySubmission.size() - 1);
								card.setMovePosition(moveTo);
								card.moveSpeed = GMath.distance(card.getPoint(), moveTo) / 8.0;
							}
						}
					}
				}
			}
			else {
				// HANDLE JUDGEMENT MODE:
				int scrollSpeed = 24;
				int goalY = judgeGroupViewIndex * (Card.DRAW_HEIGHT + GUILayout.JUDGE_CARD_YPADDING);
				judgeScrolling = (Math.abs(goalY - judgingScrollY) < scrollSpeed);
				if (judgeScrolling)
					judgingScrollY = goalY;
				else
					judgingScrollY += Math.signum(goalY - judgingScrollY) * scrollSpeed;
				
				if (allGroupsFlipped) {
					// Choose a winner:
					
					if ((Keyboard.down.pressed() || Mouse.scrollDown()) && judgeGroupViewIndex < judgementCardGroups.size() - 1)
						judgeGroupViewIndex += 1;
					if ((Keyboard.up.pressed() || Mouse.scrollUp()) && judgeGroupViewIndex > 0)
						judgeGroupViewIndex -= 1;
					
					if (isJudge()) {
						if (Keyboard.enter.pressed()) {
							// Choose Winner!
							sendMessage(new Message("setwinner", judgementCardGroups.get(judgeGroupViewIndex).getOwner()));
						}
					}
				}
				else if (isJudge()) {
					// Flip the card groups:
					
					if (Keyboard.enter.pressed()) {
						sendMessage(new Message("flipcardgroup"));
					}
				}
			}
			

			
			// Update all the hand/submission cards:
			for (Card card : hand.getCards().getCards()) {
				card.update();
			}
		}
		
		// If admin, then press enter to start the game:
		if (Keyboard.enter.pressed() && !playing && isAdmin) {
			playing = true;
			sendMessage(new Message("startgame"));
		}
	}
	
	public static void updateHand() {
		if (cardsToDraw.size() == 0 && drawCardTimer.running() && drawCardTimer.pastTime(REORGANIZE_CARDS_TIME + DRAW_CARD_DELAY_TIME)) {
			drawCardTimer.stop();
		}
		if (drawCardTimer.running()) {
			if (hand.size() == 0)
				drawCardTimer.setTime(0);
			if (!cardsOrganized && drawCardTimer.pastTime(REORGANIZE_CARDS_TIME)) {
				cardsOrganized = true;
				for (int i = 0; i < hand.size(); i++) {
					Card card = hand.getCard(i);
					card.index = i;

					if (!cardsToDraw.contains(card)) {
						Point moveTo = getHandCardDrawPoint(card.index);
						card.setMovePosition(moveTo);
						card.moveSpeed = 30;
					}
				}
			}
			if (drawCardTimer.pastTime(REORGANIZE_CARDS_TIME + DRAW_CARD_RATE + DRAW_CARD_DELAY_TIME)) {
				drawNextCard();
				drawCardTimer.setTime(REORGANIZE_CARDS_TIME + DRAW_CARD_DELAY_TIME);
			}
		}
	}
	
	public static void setReadyState(boolean state) {
		if (!drawCardTimer.running() && ready != state && (ready || mySubmission.size() == categoryCard.getBlanks())) {
			ready = state;
			getPlayerHolder(myName).setReady(ready);
			sendMessage(new Message("setready", ready));
		}
	}
	
	public static void drawNextCard() {
		Card card = cardsToDraw.get(0);
		cardsToDraw.remove(0);
		
		Point moveTo = getHandCardDrawPoint(card.index);
		card.x = Game.VIEW_WIDTH + 64;
		card.y = getHandCardDrawPoint(9).y;
		card.setMovePosition(moveTo);
		card.moveSpeed = 20;
	}
	
	public static void addHandCard(Card card) {
		cardsToDraw.add(card);
		hand.addCard(card);
		card.index = hand.size() - 1;
		card.setPosition(new Point(-1000, -1000));
	}
	
	public static void updateCardPositions() {
		for (int i = 0; i < hand.size(); i++) {
			hand.getCard(i).index = i;
			hand.getCard(i).setPosition(getHandCardDrawPoint(i));
		}
	}
	
	public static void removePlayerHolder(String holderName) {
		playerHolders.remove(getPlayerHolder(holderName));
	}
	
	public static void addNewPlayerHolder(String holderName) {
		playerHolders.add(new PlayerHolder(holderName));
	}
	
	public static PlayerHolder getPlayerHolder(String holderName) {
		for (PlayerHolder ph : playerHolders) {
			if (ph.getName().equals(holderName))
				return ph;
		}
		return null;
	}
	
	public static void setWinner(String winnerName) {
		winner = winnerName;
		winningJudgeGroup = getJudgeGroup(winner);
		winnerWaitTimer.reset();
		winnerWaitTimer.start();
		judgingScrollY = 0;
		
	}
	
	public static void checkAllGroupsFlipped() {
		allGroupsFlipped = true;
		for (CardJudgeGroup group : judgementCardGroups) {
			if (!group.getFlipped()) {
				allGroupsFlipped = false;
				break;
			}
		}
	}
	
	public static boolean isJudge() {
		return (judgeName.equals(myName));
	}
	
	private static boolean submissionFull() {
		if (categoryCard == null)
			return (mySubmission.size() >= 1);
		return (mySubmission.size() >= categoryCard.getBlanks());
	}
	
	public static void setJudge(String name, boolean onNextTurn) {
		judgeName = name;
		if (isJudge()) {
			if (!onNextTurn)
				mySubmission.clear();
			setReadyState(false);

			for (int i = 0; i < hand.size(); i++) {
				Card card = hand.getCard(i);
				if (card != null)
					card.setPosition(getHandCardDrawPoint(i));
			}
		}
	}
	
	public static void sendMessage(Message message) {
		MessageDecoder.sendMessage(message);
	}
	
	public static void setCategory(Card c) {
		if (winnerWaitTimer.running()) {
			nextCategoryCard = c;
		}
		else {
			categoryCard = c;
		}
	}
	
	public static void addJudgeGroup(CardJudgeGroup group) {
		judgementCardGroups.add(group);
	}
	
	public static void flipNextJudgeGroup() {
		for (int i = 0; i < judgementCardGroups.size(); i++) {
			if (!judgementCardGroups.get(i).getFlipped()) {
				judgementCardGroups.get(i).flip();
				if (i > 0) {
					judgeGroupViewIndex += 1;
					judgeScrolling = true;
				}
				break;
			}
		}
		checkAllGroupsFlipped();
	}
	
	public static CardJudgeGroup getJudgeGroup(String owner) {
		for (CardJudgeGroup group : judgementCardGroups) {
			if (group.isOwnedBy(owner))
				return group;
		}
		return null;
	}
	
	public static void clearJudgeGroups() {
		judgementCardGroups.clear();
	}
	
	public static Point getHandCardDrawPoint(int handCardIndex) {
		Point pos = new Point(GUILayout.HAND_POS);
		
		int dx = (handCardIndex % GUILayout.HAND_CARDS_PER_ROW) * (Card.DRAW_WIDTH + GUILayout.HAND_CARD_XPADDING);
		int dy = 0;
		if (handCardIndex >= GUILayout.HAND_CARDS_PER_ROW)
			dy += Card.DRAW_HEIGHT + GUILayout.HAND_CARD_YPADDING;
		
		pos.translate(dx, dy);
		
		return pos;
	}
	
	public static Point getSubmissionDrawPoint(int submissionIndex) {
		Point pos = new Point(GUILayout.SUBMISSION_POS);
		
		int dx = submissionIndex * (Card.DRAW_WIDTH + GUILayout.SUBMISSION_PADDING);
		pos.translate(dx, 0);
		
		return pos;
	}
	
	public static Point getJudgeCardDrawPoint(int groupIndex, int cardIndex) {
		Point pos = new Point(GUILayout.JUDGE_CATEGORYCARD_POS);
		
//		if (!winnerWaitTimer.running())
//			judgingScrollY = judgeGroupViewIndex * (Card.DRAW_HEIGHT + GUILayout.JUDGE_CARD_YPADDING);
		
		pos.x += GUILayout.JUDGE_CARD_XOFFSET + Card.DRAW_WIDTH + (cardIndex * (Card.DRAW_WIDTH + GUILayout.JUDGE_CARD_XPADDING));
		pos.y += groupIndex * (Card.DRAW_HEIGHT + GUILayout.JUDGE_CARD_YPADDING);
		pos.y -= judgingScrollY;
		
		return pos;
	}
	
	public static void drawHand(Graphics g) {
		// Draw Cards:
		for (int i = 0; i < hand.size(); i++) {
			Card card = hand.getCard(i);
			card.drawWhiteFront(g, card.x, card.y);
		}
	}
	
	////////////////////
	//  DRAW METHOD:  //
	////////////////////
	
	public static void draw(Graphics g) {
		Font font      = new Font("Arial", Font.BOLD, 15);
		Font fontLarge = new Font("Arial", Font.BOLD, 23);
		g.setFont(font);
		
		
		if (CardGame.playing) {
			if (waitingForNextRound) {
				drawHand(g);
				g.setFont(fontLarge);
				g.setColor(Color.BLACK);
				g.drawString("Waiting for next round to start...", 210, 120);
			}
			else if (winnerWaitTimer.running()) {
				// SHOW WINNER:
				
				if (categoryCard != null) {
					Point dp = GUILayout.JUDGE_CATEGORYCARD_POS;
					categoryCard.drawBlackFront(g, dp.x, dp.y);
				}

				if (winningJudgeGroup != null) {
					g.setColor(Color.BLACK);
					Point p = getJudgeCardDrawPoint(0, 0);
					g.drawString("Winner: " + winner, p.x, p.y - 6);

					for (int j = 0; j < winningJudgeGroup.size(); j++) {
						Point dp = getJudgeCardDrawPoint(0, j);
						winningJudgeGroup.getCard(j).drawWhiteFront(g, dp.x, dp.y);
					}
				}
			}
			else if (judgementMode) {
				// JUDGEMENT MODE:
				

				// Judge Background:
				if (isJudge()) {
					Point jcPos = GUILayout.JUDGE_CATEGORYCARD_POS;
					int lip = 32;
					g.setColor(GUILayout.COLOR_BACKGROUND_JUDGE);
					g.fillRect(0, jcPos.y - lip, Game.VIEW_WIDTH, Card.DRAW_HEIGHT + (lip * 2));
				}
				
				if (categoryCard != null) {
					Point dp = GUILayout.JUDGE_CATEGORYCARD_POS;
					categoryCard.drawBlackFront(g, dp.x, dp.y);
				}

				for (int i = 0; i < judgementCardGroups.size(); i++) {
					CardJudgeGroup group = judgementCardGroups.get(i);

					for (int j = 0; j < group.size(); j++) {
						Point dp = getJudgeCardDrawPoint(i, j);

						if (group.getFlipped())
							group.getCard(j).drawWhiteFront(g, dp.x, dp.y);
						else
							group.getCard(j).drawWhiteBack(g, dp.x, dp.y);
					}
				}
			}
			else {
				// SUBMISSION MODE:

				int Y = isJudge() ? GUILayout.JUDGE_CATEGORYCARD_POS.y : GUILayout.CATEGORYCARD_POS.y;
				Y = GUILayout.CATEGORYCARD_POS.y;
				
				// Ready/Judge Background:
				if (ready || isJudge()) {
					g.setColor(GUILayout.COLOR_BACKGROUND_READY);
					if (isJudge())
						g.setColor(GUILayout.COLOR_BACKGROUND_JUDGE);
					g.fillRect(0, 0, Game.VIEW_WIDTH, GUILayout.CATEGORYCARD_POS.y + Card.DRAW_HEIGHT + 32);
				}
				
				// Category Card + Backdrops:
				if (categoryCard != null) {
					Point dp = isJudge() ? GUILayout.CATEGORYCARD_CENTERED_POS : GUILayout.CATEGORYCARD_POS;
					categoryCard.drawBlackFront(g, dp.x, Y);

					// Submission Backdrops:
					if (!isJudge()) {
    					g.setColor(new Color(0, 0, 0, 30));
    					for (int i = 0; i < categoryCard.getBlanks(); i++) {
    						Point p = getSubmissionDrawPoint(i);
    						g.drawImage(ImageLoader.getImage("cardSlot"), p.x, Y, null);
    						/*
    						Point p     = getSubmissionDrawPoint(i);
    						Rectangle r = new Rectangle(p.x, Y, Card.DRAW_WIDTH, Card.DRAW_HEIGHT);
    						g.drawRect(r.x, r.y, r.width, r.height);
    						r.grow(-2, -2);
    						g.drawRect(r.x, r.y, r.width, r.height);
    						*/
    					}
					}
				}
				drawHand(g);
			}
		}
		else {
			// Waiting for game to start:
			g.setFont(fontLarge);
			g.setColor(Color.BLACK);
			g.drawString("Waiting for host", 280, 250);
			g.drawString("to start the game...", 280, 280);
		}
	}
}
