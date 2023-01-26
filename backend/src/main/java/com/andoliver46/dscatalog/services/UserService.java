package com.andoliver46.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andoliver46.dscatalog.dto.RoleDTO;
import com.andoliver46.dscatalog.dto.UserDTO;
import com.andoliver46.dscatalog.dto.UserInsertDTO;
import com.andoliver46.dscatalog.dto.UserUpdateDTO;
import com.andoliver46.dscatalog.entities.Role;
import com.andoliver46.dscatalog.entities.User;
import com.andoliver46.dscatalog.repositories.RoleRepository;
import com.andoliver46.dscatalog.repositories.UserRepository;
import com.andoliver46.dscatalog.services.exceptions.DatabaseException;
import com.andoliver46.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repo.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repo.findById(id);
		User entity = obj
				.orElseThrow(() -> new ResourceNotFoundException("O usuario de Id: " + id + " não foi encontrado."));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		copyDtoToEntity(dto, entity);
		entity = repo.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(UserUpdateDTO dto, Long id) {
		try {
			User entity = repo.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repo.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("O usuario de Id: " + id + " não foi encontrado.");
		}

	}

	public void delete(Long id) {
		try {
			repo.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("O usuario de Id: " + id + " não foi encontrado.");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Impossível excluir, Integrity violation");
		}

	}

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepo.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}
	}
}
