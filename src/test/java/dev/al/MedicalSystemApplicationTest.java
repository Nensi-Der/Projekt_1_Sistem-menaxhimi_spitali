package dev.al;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MedicalSystemApplicationTest {

	@Test
	public void testMainMethodRunsWithoutException() {
		assertDoesNotThrow(() -> MedicalSystemApplication.main(new String[]{}));
	}
}