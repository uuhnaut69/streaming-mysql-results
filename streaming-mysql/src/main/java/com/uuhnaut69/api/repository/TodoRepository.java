package com.uuhnaut69.api.repository;

import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;
import com.uuhnaut69.api.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

	@QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
	@Query(value = "select t from Todo t")
	Stream<Todo> streamAll();
}
