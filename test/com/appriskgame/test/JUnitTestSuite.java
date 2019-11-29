package com.appriskgame.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MapOperationTest.class, MapValidationTest.class, PlayerTest.class, CardControllerTest.class,
		TournamentTest.class })

/**
 * A TestSuite class for testing all the test cases
 * 
 * @author Sahana
 *
 */
public class JUnitTestSuite {

}
