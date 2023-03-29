package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Result.Error
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking

class FakeAndroidTestRepository : TasksRepository {

	var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()
	private var shouldReturnError = false
	private val observableTasks = MutableLiveData<Result<List<Task>>>()

	override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
		if (shouldReturnError) {
			return Result.Error(Exception("Test exception"))
		}
		return Success(tasksServiceData.values.toList())
	}

	override suspend fun refreshTasks() {
		observableTasks.value = getTasks()
	}

	override fun observeTasks(): LiveData<Result<List<Task>>> {
		TODO("Not yet implemented")
	}

	override suspend fun refreshTask(taskId: String) {
		TODO("Not yet implemented")
	}

	override fun observeTask(taskId: @JvmSuppressWildcards String?): LiveData<Result<Task>> {
		runBlocking { refreshTasks() }
		return observableTasks.map { tasks ->
			when (tasks) {
				is Result.Loading -> Result.Loading
				is Error -> Error(tasks.exception)
				is Success -> {
					val task = tasks.data.firstOrNull() { it.id == taskId }
						?: return@map Result.Error(Exception("Not found"))
					Success(task)
				}
			}
		}
	}

	override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
		TODO("Not yet implemented")
	}

	override suspend fun saveTask(task: Task) {
		tasksServiceData[task.id] = task
	}

	override suspend fun completeTask(task: Task) {
		TODO("Not yet implemented")
	}

	override suspend fun completeTask(taskId: String) {
		TODO("Not yet implemented")
	}

	override suspend fun activateTask(task: Task) {
		TODO("Not yet implemented")
	}

	override suspend fun activateTask(taskId: String) {
		TODO("Not yet implemented")
	}

	override suspend fun clearCompletedTasks() {
		TODO("Not yet implemented")
	}

	override suspend fun deleteAllTasks() {
		TODO("Not yet implemented")
	}

	override suspend fun deleteTask(taskId: String) {
		TODO("Not yet implemented")
	}

	fun addTasks(vararg tasks: Task) {
		for (task in tasks) {
			tasksServiceData[task.id] = task
		}
		runBlocking {
			refreshTasks()
		}
	}
}