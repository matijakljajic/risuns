package com.matijakljajic.music_catalog.repository;

import com.matijakljajic.music_catalog.model.Role;
import com.matijakljajic.music_catalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  List<User> findByUsernameContainingIgnoreCaseOrDisplayNameContainingIgnoreCase(String username, String displayName);

  @Query("""
    select u from User u
    where (:q is null or lower(u.username) like lower(concat('%', :q, '%'))
       or (:q is not null and lower(u.displayName) like lower(concat('%', :q, '%'))))
      and (:role is null or u.role = :role)
      and (:enabled is null or u.enabled = :enabled)
    order by lower(u.username)
  """)
  List<User> search(@Param("q") String query,
                    @Param("role") Role role,
                    @Param("enabled") Boolean enabled);
}
