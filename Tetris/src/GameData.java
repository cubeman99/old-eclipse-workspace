import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class GameData {
	public static final int[] LINE_SCORES = {100, 300, 500, 800};
	
	public static final int TETROMINO_COUNT = 7;
	public static ArrayList<Tetromino> templateTetrominoes;
	public static ArrayList<Tetromino> tetrominoQueue;
	public static final int TETROMINO_QUEUE_COUNT = 10;
	
	public static Image[] TILE_OVERLAY_IMAGES;
	
	public static boolean showGhosts = true;
	public static boolean showGrid   = true;
	
	public static final int MAX_LEVEL = 15;
	public static int score;
	public static int level;
	public static int levelLines;
	public static int levelLineGoal;
	public static double fallSpeed;
	
	public static void initialize() {
		System.out.println("Initializing...");
		initializeTetrominoes();
		tetrominoQueue = new ArrayList<Tetromino>();
		for (int i = 0; i < TETROMINO_QUEUE_COUNT; i++)
			tetrominoQueue.add(0, getRandomTetromino());
	
		TILE_OVERLAY_IMAGES = new Image[16];
		for (int i = 0; i < 16; i++)
			TILE_OVERLAY_IMAGES[i] = ImageLoader.getTile(ImageLoader.blockOverlay, i, 0, 24, 24);

		score = 0;
		level = 1;
		levelLines = 0;
		levelLineGoal = 10;
		fallSpeed		= 2.0d;
	}
	
	public static void addClearScore(int lines) {
		levelLines += lines;
		if (levelLines >= levelLineGoal) {
			levelLines -= levelLineGoal;
			levelUp();
		}
		score += LINE_SCORES[lines - 1] * level;
	}
	
	public static void levelUp() {
		if (level < MAX_LEVEL) {
			level += 1;
			levelLines = 0;
			fallSpeed += 0.5d;
			levelLineGoal = (int) ((double) levelLineGoal * 1.5d);
			System.out.println("LEVEL UP: level " + level + ", Line Goal = " + levelLineGoal);
		}
	}
	
	public static void update() {
		if (Game.keyPressed[KeyEvent.VK_G])
			showGhosts = !showGhosts;

//		fallSpeed += 0.0001d;
	}
	
	public static Tetromino getRandomTetromino() {
		int index = Game.random.nextInt(TETROMINO_COUNT);
		return templateTetrominoes.get(index);
	}
	
	public static Tetromino loadNextTetromino() {
		Tetromino t = tetrominoQueue.get(0);
		tetrominoQueue.remove(0);
		tetrominoQueue.add(getRandomTetromino());
		return t;
	}
	
	public static void initializeTetrominoes() {
		templateTetrominoes = new ArrayList<Tetromino>();
		
		Tetromino Z = new Tetromino("Z", 3, 2, Color.red);
		Z.grid[0][0][0] = true;// XX
		Z.grid[0][1][0] = true;//  XX
		Z.grid[0][1][1] = true;// 
		Z.grid[0][2][1] = true;// 
		Z.initializeGrids();
		templateTetrominoes.add(Z);
		
		Tetromino S = new Tetromino("S", 3, 2, Color.green);
		S.grid[0][0][1] = true;//  XX
		S.grid[0][1][1] = true;// XX
		S.grid[0][1][0] = true;// 
		S.grid[0][2][0] = true;// 
		S.initializeGrids();
		templateTetrominoes.add(S);
		
		Tetromino L = new Tetromino("L", 2, 3, Color.orange);
		L.grid[0][0][0] = true;// X
		L.grid[0][0][1] = true;// X
		L.grid[0][0][2] = true;// XX
		L.grid[0][1][2] = true;// 
		L.initializeGrids();
		templateTetrominoes.add(L);
	
		Tetromino J = new Tetromino("J", 2, 3, Color.blue);
		J.grid[0][1][0] = true;//  X
		J.grid[0][1][1] = true;//  X
		J.grid[0][1][2] = true;// XX
		J.grid[0][0][2] = true;// 
		J.initializeGrids();
		templateTetrominoes.add(J);
	
		Tetromino T = new Tetromino("T", 2, 3, Color.magenta);
		T.grid[0][0][0] = true;// X
		T.grid[0][0][1] = true;// XX
		T.grid[0][1][1] = true;// X
		T.grid[0][0][2] = true;// 
		T.initializeGrids();
		templateTetrominoes.add(T);
	
		Tetromino I = new Tetromino("I", 1, 4, Color.cyan);
		I.grid[0][0][0] = true;// X
		I.grid[0][0][1] = true;// X
		I.grid[0][0][2] = true;// X
		I.grid[0][0][3] = true;// X
		I.initializeGrids();
		templateTetrominoes.add(I);
	
		Tetromino O = new Tetromino("O", 2, 2, Color.yellow);
		O.grid[0][0][0] = true;// XX
		O.grid[0][0][1] = true;// XX
		O.grid[0][1][0] = true;// 
		O.grid[0][1][1] = true;// 
		O.initializeGrids();
		templateTetrominoes.add(O);
	}
}
