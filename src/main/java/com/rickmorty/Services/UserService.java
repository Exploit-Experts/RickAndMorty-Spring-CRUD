package com.rickmorty.Services;

import com.rickmorty.Models.UserModel;
import com.rickmorty.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Verifica se um e-mail já está cadastrado
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Salva um novo usuário
    public UserModel saveUser(UserModel user) {
        user.setDate_update(null); // Certifique-se de que o campo date_update não é preenchido ao criar
        return userRepository.save(user);
    }

    // Obtém todos os usuários
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtém um usuário pelo e-mail
    public Optional<UserModel> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Atualiza um usuário existente pelo e-mail
    public UserModel updateUserByEmail(String email, UserModel userDetails) {
        Optional<UserModel> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();

            // Atualiza os campos do usuário
            user.setName(userDetails.getName());
            user.setSurname(userDetails.getSurname());
            user.setPassword(userDetails.getPassword());

            // Atualiza o date_update para a data atual
            user.setDate_update(LocalDate.now());

            return userRepository.save(user);  // Salva as alterações
        } else {
            throw new RuntimeException("Usuário não encontrado com o email: " + email);
        }
    }

    // Exclui um usuário pelo e-mail (soft delete)
    public void deleteUserByEmail(String email) {
        Optional<UserModel> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            user.setDeleted_at(LocalDate.now()); // Define a data de exclusão para a data atual
            user.setActive(false); // Altera o status ativo para false
            userRepository.save(user); // Salva o usuário com a data de exclusão e status atualizado
        } else {
            throw new RuntimeException("Usuário não encontrado com o email: " + email);
        }
    }
}