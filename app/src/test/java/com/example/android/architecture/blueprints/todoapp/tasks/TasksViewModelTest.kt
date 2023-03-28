package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksViewModelTest {

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
}