package com.base.engine.core;


public class Keyboard {
	public static final int NUM_KEYCODES        = 0xFF;
	public static final int KEY_NONE            = 0x00;
	

	// ============================================= //
	
	public static final int KEY_ESCAPE          = 0x01;
	public static final int KEY_F1              = 0x3B;
	public static final int KEY_F2              = 0x3C;
	public static final int KEY_F3              = 0x3D;
	public static final int KEY_F4              = 0x3E;
	public static final int KEY_F5              = 0x3F;
	public static final int KEY_F6              = 0x40;
	public static final int KEY_F7              = 0x41;
	public static final int KEY_F8              = 0x42;
	public static final int KEY_F9              = 0x43;
	public static final int KEY_F10             = 0x44;
	public static final int KEY_F11             = 0x57;
	public static final int KEY_F12             = 0x58;
	public static final int KEY_PRINT_SCREEN    = 0xB7;
	public static final int KEY_SCROLL_LOCK     = 0x46;
	public static final int KEY_PAUSE           = 0xC5;
	

	// ============================================= //
	
	public static final int KEY_TILDE           = 0x29; // Accent grave
	public static final int KEY_1               = 0x02;
	public static final int KEY_2               = 0x03;
	public static final int KEY_3               = 0x04;
	public static final int KEY_4               = 0x05;
	public static final int KEY_5               = 0x06;
	public static final int KEY_6               = 0x07;
	public static final int KEY_7               = 0x08;
	public static final int KEY_8               = 0x09;
	public static final int KEY_9               = 0x0A;
	public static final int KEY_0               = 0x0B;
	public static final int KEY_MINUS           = 0x0C;
	public static final int KEY_EQUALS          = 0x0D;
	public static final int KEY_BACKSPACE       = 0x0E;

	
	// ============================================= //
	
	public static final int KEY_TAB             = 0x0F;
	public static final int KEY_Q               = 0x10;
	public static final int KEY_W               = 0x11;
	public static final int KEY_E               = 0x12;
	public static final int KEY_R               = 0x13;
	public static final int KEY_T               = 0x14;
	public static final int KEY_Y               = 0x15;
	public static final int KEY_U               = 0x16;
	public static final int KEY_I               = 0x17;
	public static final int KEY_O               = 0x18;
	public static final int KEY_P               = 0x19;
	public static final int KEY_LBRACKET        = 0x1A;
	public static final int KEY_RBRACKET        = 0x1B;
	public static final int KEY_BACKSLASH       = 0x2B;
	

	// ============================================= //
	
	public static final int KEY_CAPS_LOCK       = 0x3A;
	public static final int KEY_A               = 0x1E;
	public static final int KEY_S               = 0x1F;
	public static final int KEY_D               = 0x20;
	public static final int KEY_F               = 0x21;
	public static final int KEY_G               = 0x22;
	public static final int KEY_H               = 0x23;
	public static final int KEY_J               = 0x24;
	public static final int KEY_K               = 0x25;
	public static final int KEY_L               = 0x26;
	public static final int KEY_SEMICOLON       = 0x27;
	public static final int KEY_APOSTROPHE      = 0x28;
	public static final int KEY_ENTER           = 0x1C;
	

	// ============================================= //
	
	public static final int KEY_LSHIFT          = 0x2A;
	public static final int KEY_Z               = 0x2C;
	public static final int KEY_X               = 0x2D;
	public static final int KEY_C               = 0x2E;
	public static final int KEY_V               = 0x2F;
	public static final int KEY_B               = 0x30;
	public static final int KEY_N               = 0x31;
	public static final int KEY_M               = 0x32;
	public static final int KEY_COMMA           = 0x33;
	public static final int KEY_PERIOD          = 0x34;
	public static final int KEY_SLASH           = 0x35;
	public static final int KEY_RSHIFT          = 0x36;
	

	// ============================================= //

	public static final int KEY_SPACE           = 0x39;
	public static final int KEY_LCONTROL        = 0x1D;
	public static final int KEY_LALT            = 0x38;
	public static final int KEY_LMETA           = 0xDB; // Left Windows/Option key
	public static final int KEY_LWIN            = KEY_LMETA; // Left Windows key
	public static final int KEY_RCONTROL        = 0x9D;
	public static final int KEY_RALT            = 0xB8;
	public static final int KEY_RMETA           = 0xDC; // Right Windows/Option key
	public static final int KEY_RWIN            = KEY_RMETA; // Right Windows key

	
	// ================== NUMPAD =================== //
	
	public static final int KEY_NUMLOCK         = 0x45;
	public static final int KEY_NUMPAD0         = 0x52;
	public static final int KEY_NUMPAD1         = 0x4F;
	public static final int KEY_NUMPAD2         = 0x50;
	public static final int KEY_NUMPAD3         = 0x51;
	public static final int KEY_NUMPAD4         = 0x4B;
	public static final int KEY_NUMPAD5         = 0x4C;
	public static final int KEY_NUMPAD6         = 0x4D;
	public static final int KEY_NUMPAD7         = 0x47;
	public static final int KEY_NUMPAD8         = 0x48;
	public static final int KEY_NUMPAD9         = 0x49;
	public static final int KEY_NUMPAD_DIVIDE   = 0xB5;
	public static final int KEY_NUMPAD_MULTIPLY = 0x37;
	public static final int KEY_NUMPAD_SUBTRACT = 0x4A;
	public static final int KEY_NUMPAD_ADD      = 0x4E;
	public static final int KEY_NUMPAD_ENTER    = 0x9C;
	public static final int KEY_NUMPAD_DECIMAL  = 0x53;
	public static final int KEY_NUMPAD_COMMA    = 0xB3;


	// ================= ARROW PAD ================= //
	
	public static final int KEY_INSERT          = 0xD2; /* Insert on arrow keypad */
	public static final int KEY_DELETE          = 0xD3; /* Delete on arrow keypad */
	public static final int KEY_HOME            = 0xC7; /* Home on arrow keypad */
	public static final int KEY_END             = 0xCF; /* End on arrow keypad */
	public static final int KEY_PAGE_UP         = 0xC9; /* PgUp on arrow keypad */
	public static final int KEY_PAGE_DOWN       = 0xD1; /* PgDn on arrow keypad */
	public static final int KEY_UP              = 0xC8; /* UpArrow on arrow keypad */
	public static final int KEY_DOWN            = 0xD0; /* DownArrow on arrow keypad */
	public static final int KEY_RIGHT           = 0xCD; /* RightArrow on arrow keypad */
	public static final int KEY_LEFT            = 0xCB; /* LeftArrow on arrow keypad */
	

	protected static boolean[] keys       = new boolean[NUM_KEYCODES];
	protected static boolean[] lastKeys   = new boolean[NUM_KEYCODES];
	
	
	
	// =================== ACCESSORS =================== //
	
	public static boolean isKeyDown(int keyCode) {
		return keys[keyCode];
	}

	public static boolean isKeyPressed(int keyCode) {
		return keys[keyCode] && !lastKeys[keyCode];
	}

	public static boolean isKeyReleased(int keyCode) {
		return !keys[keyCode] && lastKeys[keyCode];
	}
	


	// ==================== MUTATORS ==================== //
	
	public static void update() {
		for(int i = 0; i < NUM_KEYCODES; i++) {
			lastKeys[i] = keys[i];
			keys[i] = org.lwjgl.input.Keyboard.isKeyDown(i);
		}
	}
}
