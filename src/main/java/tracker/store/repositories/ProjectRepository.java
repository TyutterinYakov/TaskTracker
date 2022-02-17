package tracker.store.repositories;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tracker.store.entities.ProjectEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>{
	Optional<ProjectEntity> findByName(String name);
	
	
	Stream<ProjectEntity> streamAllByNameStartsWithIgnoreCase(String name);

	Stream<ProjectEntity> streamAll();
}
