package com.qeevee.gq.tests.gamedata;

import static com.qeevee.gq.tests.gamedata.TestGameDataUtil.checkAllReposAndQuests;
import static com.qeevee.gq.tests.gamedata.TestGameDataUtil.shouldHaveRepositories;
import static com.qeevee.gq.tests.util.TestUtils.callMethod;
import static com.qeevee.gq.tests.util.TestUtils.startApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.widget.ListView;

import com.qeevee.gq.tests.robolectric.GQTestRunner;
import com.qeevee.gq.tests.robolectric.WithAssets;
import com.xtremelabs.robolectric.Robolectric;

import edu.bonn.mobilegaming.geoquest.GameListActivity;
import edu.bonn.mobilegaming.geoquest.GeoQuestApp;
import edu.bonn.mobilegaming.geoquest.RepoListActivity;
import edu.bonn.mobilegaming.geoquest.gameaccess.GameDataManager;
import edu.bonn.mobilegaming.geoquest.gameaccess.GameItem;
import edu.bonn.mobilegaming.geoquest.gameaccess.RepositoryItem;

/**
 * Tests the case when the GeoQuest app does not come with any preloaded quests.
 * In particular when the assets directory is completely empty.
 * 
 * @author muegge
 * 
 */
@Ignore
@RunWith(GQTestRunner.class)
@WithAssets("../GQ_Android/test/testassets/r3q312/")
public class InAppGameDeployment_r3q312_Test {

	RepoListActivity repoListAct;
	GameListActivity gameListAct;

	Map<String, String[]> expectedReposAndQuests = new HashMap<String, String[]>();
	Map<String, String[]> expectedReposAndQuestFileNames = new HashMap<String, String[]>();

	@Before
	public void setupReposAndQuests() {
		expectedReposAndQuests.put("repo1", new String[] {
				"r3q312-test-game-1_1", "r3q312-test-game-1_2",
				"r3q312-test-game-1_3" });
		expectedReposAndQuests.put("repo2",
				new String[] { "r3q312-test-game-2_1" });
		expectedReposAndQuests.put("repo3", new String[] {
				"r3q312-test-game-3_1", "r3q312-test-game-3_2" });

		expectedReposAndQuestFileNames.put("repo1", new String[] { "quest1_1",
				"quest1_2", "quest1_3" });
		expectedReposAndQuestFileNames
				.put("repo2", new String[] { "quest2_1" });
		expectedReposAndQuestFileNames.put("repo3", new String[] { "quest3_1",
				"quest3_2" });
	}

	// === TESTS FOLLOW =============================================

	@Test
	public void findReposAndQuests() {
		// GIVEN:
		// nothing

		// WHEN:
		startApp();

		// THEN:
		shouldHaveRepositories(expectedReposAndQuests.keySet());
		checkAllReposAndQuests(expectedReposAndQuests);
	}

	@Test
	public void checkRepoFeatures() {
		// GIVEN:
		startApp();

		// WHEN:
		// start repo list activity:
		repoListAct = new RepoListActivity();
		repoListAct.onCreate(null);

		// THEN:
		repoShouldBeOnClient(1);
	}

	@Test
	public void checkReposShownInRepoView() {
		// GIVEN:
		startApp();
		repoListAct = new RepoListActivity();

		// WHEN:
		repoListAct.onCreate(null);

		// THEN:
		shouldShowRepositories(3, new String[] { "repo1", "repo2", "repo3" });
	}

	@Test
	public void checkQuestsShownInGameListView() {
		// GIVEN:
		startApp();
		repoListAct = new RepoListActivity();
		repoListAct.onCreate(null);

		// WHEN:
		selectRepo("repo1");

		// THEN:
		shoudShowQuests(3, new String[] { "r3q312-test-game-1_1",
				"r3q312-test-game-1_2", "r3q312-test-game-1_3" });

		// WHEN:
		selectRepo("repo2");

		// THEN:
		shoudShowQuests(1, new String[] { "r3q312-test-game-2_1" });

		// WHEN:
		selectRepo("repo3");

		// THEN:
		shoudShowQuests(2, new String[] { "r3q312-test-game-3_1",
				"r3q312-test-game-3_2" });
	}

	@Test
	public void checkGameFeatures() {
		// GIVEN:
		startApp();
		repoListAct = new RepoListActivity();
		repoListAct.onCreate(null);

		// WHEN:
		selectRepo("repo1");

		// THEN:
		gameShouldBeOnClient("repo1", 1);
		gamesShouldHaveCorrectFilenames(expectedReposAndQuestFileNames);
	}

	private void gamesShouldHaveCorrectFilenames(
			Map<String, String[]> expectedReposAndQuestFileNames) {
		// TODO Auto-generated method stub

	}

	// === HELPERS FOLLOW =============================================

	private void shouldShowRepositories(int expectedNumberOfShownRepos,
			String... expectedRepoNames) {
		if (expectedRepoNames.length != expectedNumberOfShownRepos)
			throw new IllegalArgumentException(
					"Number of expected repos differs from given array of expected repo names.");
		ListView lv = (ListView) repoListAct.findViewById(android.R.id.list);
		assertEquals(expectedNumberOfShownRepos, lv.getChildCount());
		for (int i = 0; i < expectedRepoNames.length; i++) {
			assertEquals(expectedRepoNames[i], lv.getItemAtPosition(i)
					.toString());
		}
	}

	private void selectRepo(String repoName) {
		Intent intent = new Intent(GeoQuestApp.getContext(),
				edu.bonn.mobilegaming.geoquest.GameListActivity.class);
		intent.putExtra("edu.bonn.mobilegaming.geoquest.REPO", repoName);
		gameListAct = new GameListActivity();
		Robolectric.shadowOf(gameListAct).setIntent(intent);
		gameListAct.onCreate(null);
		callMethod(gameListAct, "onResume", null, null);
	}

	private void shoudShowQuests(int expectedNumberOfShownGames,
			String... expectedGameNames) {
		if (expectedGameNames.length != expectedNumberOfShownGames)
			throw new IllegalArgumentException(
					"Number of expected games differs from given array of expected game names.");
		ListView lv = (ListView) gameListAct.findViewById(android.R.id.list);
		assertEquals(expectedNumberOfShownGames, lv.getChildCount());
	}

	private void repoShouldBeOnClient(int repoNr) {
		ListView lv = (ListView) repoListAct.findViewById(android.R.id.list);
		String shownNameOfRepo = (String) lv.getItemAtPosition(repoNr);
		assertNotNull(GameDataManager.getRepository(shownNameOfRepo));
		assertTrue(GameDataManager.getRepository(shownNameOfRepo).isOnClient());
	}

	private void gameShouldBeOnClient(String repoName, int gameNr) {
		RepositoryItem repo = GameDataManager.getRepository(repoName);
		assertNotNull(repo);
		for (Iterator<GameItem> iterator = repo.getGames().iterator(); iterator
				.hasNext();) {
			GameItem gameItem = (GameItem) iterator.next();
			assertTrue(gameItem.isOnClient());
			assertFalse(gameItem.isDownloadNeeded());
		}
	}
}