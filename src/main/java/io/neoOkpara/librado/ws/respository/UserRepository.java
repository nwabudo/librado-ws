package io.neoOkpara.librado.ws.respository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.neoOkpara.librado.ws.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUserId(String userId);

	Boolean existsByEmail(String email);

	Boolean existsByUserId(String userId);
}
