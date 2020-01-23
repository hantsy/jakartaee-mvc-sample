package com.example.domain;

public class TaskNotFoundException extends RuntimeException {

	public TaskNotFoundException(Long taskId) {
		super(String.format("task id:%s not found!", taskId));
	}

}
