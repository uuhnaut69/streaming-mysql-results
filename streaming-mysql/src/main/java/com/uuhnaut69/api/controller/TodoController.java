package com.uuhnaut69.api.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uuhnaut69.api.entity.Todo;
import com.uuhnaut69.api.service.TodoService;

@RestController
public class TodoController {

	private final TodoService todoService;

	public TodoController(TodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping("/todo.csv")
	public void exportCSV(HttpServletResponse response) {
		todoService.exportCsv(response);
	}

	@PostMapping
	public Todo add(@RequestBody Todo todo) {
		return todoService.add(todo);
	}
}
