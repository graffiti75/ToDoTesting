package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test

class StatisticsUtilsTest {

	/**
	 *  If there's no completed task and one active task,
	 *  then there are 100% percent active tasks and 0% completed tasks.
	 */
	@Test
	fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {

		// Create an active tasks (the false makes this active)
		val tasks = listOf<Task>(
			Task("title", "desc", isCompleted = false)
		)
		// Call our function
		val result = getActiveAndCompletedStats(tasks)

		// Check the result
		assertEquals(result.completedTasksPercent, 0f)
		assertEquals(result.activeTasksPercent, 100f)
	}

	/**
	 *  If there's 2 completed tasks and 3 active tasks,
	 *  then there are 40% percent completed tasks and 60% active tasks.
	 */
	@Test
	fun getActiveAndCompletedStats_both_returnsFortySixty() {

		// Create an active tasks (the false makes this active)
		val tasks = listOf<Task>(
			Task("title", "desc", isCompleted = true),
			Task("title", "desc", isCompleted = true),
			Task("title", "desc", isCompleted = false),
			Task("title", "desc", isCompleted = false),
			Task("title", "desc", isCompleted = false)
		)
		// Call our function
		val result = getActiveAndCompletedStats(tasks)

		// Check the result
		assertEquals(result.completedTasksPercent, 40f)
		assertEquals(result.activeTasksPercent, 60f)
	}

	@Test
	fun getActiveAndCompletedStats_empty_returnsZeros() {

		// Create an active tasks (the false makes this active)
		val tasks = emptyList<Task>()

		// Call our function
		val result = getActiveAndCompletedStats(tasks)

		// Check the result
		assertEquals(result.completedTasksPercent, 0f)
		assertEquals(result.activeTasksPercent, 0f)
	}

	@Test
	fun getActiveAndCompletedStats_error_returnsZeros() {

		// Create an active tasks (the false makes this active)
		val tasks = null

		// Call our function
		val result = getActiveAndCompletedStats(tasks)

		// Check the result
		assertEquals(result.completedTasksPercent, 0f)
		assertEquals(result.activeTasksPercent, 0f)
	}
}