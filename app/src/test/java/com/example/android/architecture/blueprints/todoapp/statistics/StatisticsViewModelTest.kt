package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.tasks.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

	// Executes each task synchronously using Architecture Components.
	@get:Rule
	var instantExecutorRule = InstantTaskExecutorRule()

	// Set the main coroutines dispatcher for unit testing.
	@ExperimentalCoroutinesApi
	@get:Rule
	var mainCoroutineRule = MainCoroutineRule()

	// Subject under test
	private lateinit var statisticsViewModel: StatisticsViewModel

	// Use a fake repository to be injected into the view model.
	private lateinit var tasksRepository: FakeTestRepository

	@Before
	fun setupStatisticsViewModel() {
		// Initialise the repository with no tasks.
		tasksRepository = FakeTestRepository()

		statisticsViewModel = StatisticsViewModel(tasksRepository)
	}

	/**
	 * Source:
	 * https://medium.com/androiddevelopers/migrating-to-the-new-coroutines-1-6-test-apis-b99f7fc47774
	 */
	@Test
	fun loadTasks_loading() = runTest {
		// Set Main dispatcher to not run coroutines eagerly, for just this one test
		Dispatchers.setMain(StandardTestDispatcher())

//		mainCoroutineRule.pauseDispatcher()

		// Load the task in the view model.
		statisticsViewModel.refresh()

		// Then progress indicator is shown.
		assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(true))

		// Execute pending coroutines actions.
//		mainCoroutineRule.resumeDispatcher()
		advanceUntilIdle()

		// Then progress indicator is hidden.
		assertThat(statisticsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
	}
}