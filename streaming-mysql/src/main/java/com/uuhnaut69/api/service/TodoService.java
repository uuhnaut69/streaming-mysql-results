package com.uuhnaut69.api.service;

import javax.servlet.http.HttpServletResponse;

import com.uuhnaut69.api.entity.Todo;

public interface TodoService {

	void exportCsv(HttpServletResponse response);

	Todo add(Todo todo);
}
