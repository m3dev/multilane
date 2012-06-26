package com.m3.multilane.action;

import com.m3.multilane.action.NoInput.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class NoInputTest {

	@Test
	public void type() throws Exception {
		assertThat(NoInput.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		NoInput target = new NoInput();
		assertThat(target, notNullValue());
	}

}
