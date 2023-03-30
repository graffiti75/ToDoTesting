package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TasksViewModelTest {

	@get:Rule
	var mainCoroutineRule = MainCoroutineRule()

	// Use a fake repository to be injected into the viewmodel
	private lateinit var tasksRepository: FakeTestRepository

	// Subject under test
	private lateinit var tasksViewModel: TasksViewModel

	// Executes each task synchronously using Architecture Components.
	@get:Rule
	var instantExecutorRule = InstantTaskExecutorRule()

	@Before
	fun setupViewModel() {
		// We initialise the tasks to 3, with one active and two completed
		tasksRepository = FakeTestRepository()
		val task1 = Task("Title1", "Description1")
		val task2 = Task("Title2", "Description2", true)
		val task3 = Task("Title3", "Description3", true)
		tasksRepository.addTasks(task1, task2, task3)

		tasksViewModel = TasksViewModel(tasksRepository)
	}

	@Test
	fun addNewTask_setsNewTaskEvent() {
		// When adding a new task
		tasksViewModel.addNewTask()

		// Then the new task event is triggered
		val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
		assertThat(
			value.getContentIfNotHandled(), not(nullValue())
		)
	}

	// - Sets the filtering mode to ALL_TASKS
	// - Assets that the tasksAddViewVisible LiveData is true
	@Test
	fun setFilterAllTasks_tasksAddViewVisible() {
		// When the filter type is ALL_TASKS
		tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

		// Then the "Add task" action is visible
		val value = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
		assertThat(value, `is`(true))
	}

	@Test
	fun completeTask_dataAndSnackbarUpdated() {
		// Create an active task and add it to the repository.
		val task = Task("Title", "Description")
		tasksRepository.addTasks(task)

		// Mark the task as complete task.
		tasksViewModel.completeTask(task, true)

		// Verify the task is completed.
		assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

		// Assert that the snackbar has been updated with the correct text.
		val snackbarText: Event<Int> = tasksViewModel.snackbarText.getOrAwaitValue()
		assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
	}
}