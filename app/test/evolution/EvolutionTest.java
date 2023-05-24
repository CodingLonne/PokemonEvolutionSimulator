package evolution;

import static org.junit.Assert.*;


import org.junit.Test;


public class EvolutionTest {

	@Test
	public void testIntToChar() {
		proteinEncodingManager encoder = new proteinEncodingManager();
		int input = 10;
		char result = encoder.intToChar(input);
		char expected = 'A';
		assertEquals(expected, result);
	}

	@Test
	public void testCharToInt() {
		proteinEncodingManager encoder = new proteinEncodingManager();
		char c = 'b';
		int result = encoder.charToInt(c);
		int expected = 11;
		assertEquals(expected, result);
	}

	@Test
	public void testIntToBytes() {
		proteinEncodingManager encoder = new proteinEncodingManager();
		int input = 18;
		String result = encoder.intToBits(input);
		String expected = "10010";
		assertEquals(expected, result);
	}

	@Test
	public void testBytesToInt() {
		proteinEncodingManager encoder = new proteinEncodingManager();
		String input = "11000";
		int result = encoder.bitsToInt(input);
		int expected = 24;
		assertEquals(expected, result);
	}

}