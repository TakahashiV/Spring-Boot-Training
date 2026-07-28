package com.internship.training.users.services;

import com.internship.training.users.models.dto.AddressDTO;
import com.internship.training.users.models.dto.UserRequestDTO;
import com.internship.training.users.models.dto.UserResponseDTO;
import com.internship.training.users.models.entities.Address;
import com.internship.training.users.models.entities.User;
import com.internship.training.users.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Injeção de dependência via construtor (melhor prática em Spring)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Criar Usuário
    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = convertToEntity(request);
        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    // Buscar todos os Usuários
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Buscar Usuário por ID
    public Optional<UserResponseDTO> getUserById(String id) {
        return userRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    // Atualizar Usuário
    public Optional<UserResponseDTO> updateUser(String id, UserRequestDTO request) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(request.name());
            
            List<Address> newAddresses = request.addresses().stream()
                    .map(addr -> new Address(addr.zipcode(), addr.phone(), addr.location()))
                    .toList();
            existingUser.setAddresses(newAddresses);
            
            User updatedUser = userRepository.save(existingUser);
            return convertToResponseDTO(updatedUser);
        });
    }

    // Deletar Usuário
    public boolean deleteUser(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Métodos Auxiliares de Mapeamento (Mappers) ---

    private User convertToEntity(UserRequestDTO dto) {
        List<Address> addresses = dto.addresses() != null ? dto.addresses().stream()
                .map(addr -> new Address(addr.zipcode(), addr.phone(), addr.location()))
                .toList() : List.of();
        
        User user = new User();
        user.setName(dto.name());
        user.setAddresses(addresses);
        return user;
    }

    private UserResponseDTO convertToResponseDTO(User entity) {
        List<AddressDTO> addresses = entity.getAddresses() != null ? entity.getAddresses().stream()
                .map(addr -> new AddressDTO(addr.getZipcode(), addr.getPhone(), addr.getLocation()))
                .toList() : List.of();
        
        return new UserResponseDTO(
                entity.getId(),
                entity.getName(),
                addresses,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
