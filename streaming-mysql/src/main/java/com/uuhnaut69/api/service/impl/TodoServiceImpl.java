package com.uuhnaut69.api.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uuhnaut69.api.entity.Todo;
import com.uuhnaut69.api.repository.TodoRepository;
import com.uuhnaut69.api.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class TodoServiceImpl implements TodoService {
	@PersistenceContext
	EntityManager entityManager;

	private final TodoRepository todoRepository;

	public TodoServiceImpl(TodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Override
	public void exportCsv(HttpServletResponse response) {
		response.addHeader("Content-Type", "application/csv");
		response.addHeader("Content-Disposition", "attachment; filename=todos.csv");
		response.setCharacterEncoding("UTF-8");
		try (Stream<Todo> todoStream = todoRepository.streamAll()) {
			PrintWriter out = response.getWriter();
			todoStream.forEach((todo -> {
				String line = todoToCSV(todo);
				out.write(line);
				out.write("\n");
				entityManager.detach(todo);
			}));
			out.flush();
		} catch (IOException e) {
			log.info("Exception occurred " + e.getMessage(), e);
			throw new RuntimeException("Exception occurred while exporting results", e);
		}

	}

	@Override
	@Transactional
	public Todo add(Todo todo) {
		return todoRepository.save(todo);
	}

	private String todoToCSV(Todo todo) {
		return String.join(",", "" + todo.getId(), "" + todo.getCreatedAt(), "" + todo.getDescription(),
				"" + todo.isCompleted());
	}

}
