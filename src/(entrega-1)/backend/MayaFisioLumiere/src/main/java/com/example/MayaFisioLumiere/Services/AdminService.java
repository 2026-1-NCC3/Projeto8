package com.example.MayaFisioLumiere.Services;

import com.example.MayaFisioLumiere.Domain.Admin.AdminRequestDTO;
import com.example.MayaFisioLumiere.Domain.Admin.AdminResponseDTO;
import com.example.MayaFisioLumiere.Entity.AdminEntity;
import com.example.MayaFisioLumiere.Entity.role.UserRole;
import com.example.MayaFisioLumiere.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

//remover a password do admin da response, fazer a parte de terminar a autenticação por meio das entities que
@Service
public class AdminService  {

    //Criar novos perfis de Administradores, tanto da Maya quanto ela criar de outros profissionais
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TokenService tokenService; //para criar o jwt nas sessões de login, logout e register de admin

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private BlacklistService blacklistService;

    public AdminResponseDTO createAdmin(AdminRequestDTO data) {
        if (this.adminRepository.findByAdminEmail(data.adminEmail()).isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado.");
        } // se já tiver um email cadastrado ele dá um errinho pra não ter duplicatas no backend

        AdminEntity newAdmin = new AdminEntity();
        newAdmin.setAdminName(data.adminName());
        newAdmin.setAdminEmail(data.adminEmail());

        String encryptedPassword = bcrypt.encode(data.adminPassword());
        newAdmin.setAdminPassword(encryptedPassword);

        newAdmin.setRole(UserRole.Admin);

        newAdmin.setTotalMinutesUsedToday(0);
        newAdmin.setLastAccessDate(java.time.LocalDate.now());

        adminRepository.save(newAdmin);

        return new AdminResponseDTO(
                newAdmin.getAdminUser_ID(),
                newAdmin.getAdminName(),
                newAdmin.getAdminEmail() // não pode retornar mais a senha quando for pro banco, questões de segurança

        );
    }

    //Atualizar email, nome ou senha do administrador, procurando o admin pelo ID dele
    //Rota
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO data){
        try {
        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil administrador não encontrado"));
        if(data.adminName() != null){
            admin.setAdminName(data.adminName());
        }

        if (data.adminEmail() != null){
            admin.setAdminEmail((data.adminEmail()));
        }

            // senha do admin com bcrypt já
            if (data.adminPassword() != null) {
                admin.setAdminPassword(bcrypt.encode(data.adminPassword()));
            }
        adminRepository.save(admin);
        return new AdminResponseDTO(
                admin.getAdminUser_ID(),
                admin.getAdminName(),
                admin.getAdminEmail()
        );
        } catch (Exception err) {
            throw new RuntimeException("Erro ao atualizar dados do administrador", err);
        }
    }

    //Buscar todos os administradores/profissionais e seus dados

    public List<AdminResponseDTO> getAllAdmins(int page, int size){
        try{
        Pageable pageable = PageRequest.of(page,size);
        Page<AdminEntity> adminsPage = this.adminRepository.findAll(pageable);
        return adminsPage.map(admin -> new AdminResponseDTO(
                admin.getAdminUser_ID(),
                admin.getAdminName(),
                admin.getAdminEmail()
                )
        ).stream().toList();
        } catch (Exception err) {
            throw new RuntimeException("Erro ao buscar administradores", err);
        }
    }

    //Buscar Administradores/Profissionais por ID (util para o login?)

    public AdminResponseDTO findById(Long id) {
        try {
        AdminEntity admin = adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin não encontrado"));

        return new AdminResponseDTO(
                admin.getAdminUser_ID(),
                admin.getAdminName(),
                admin.getAdminEmail()
        );
        } catch (Exception err) {
            throw new RuntimeException("Administrador não encontrado", err);
        }
    }

    //Buscar por email

    public List<AdminResponseDTO> findByAdminEmail(String adminEmail){
        try {
        List<AdminEntity> admins = adminRepository.findByAdminEmailContainingIgnoreCase(adminEmail);

        return admins.stream().map(admin ->
                new AdminResponseDTO(
                        admin.getAdminUser_ID(),
                        admin.getAdminName(),
                        admin.getAdminEmail()
                )
        ).toList();
        } catch (Exception err) {
            throw new RuntimeException("Não existe administrador com esse email", err);
        }
    }

    //Deletar administrador por id
    public void deleteAdminById(Long id){
        try {
        if(!adminRepository.existsById(id)){
            throw new RuntimeException("Admin não encontrado");
        }
        adminRepository.deleteById(id);
        }catch (Exception err) {
        throw new RuntimeException("Erro ao deletar administrador", err);
        }
    }

    // service para login criando um jwt quando for bem sucediddo
   /* public String loginAdmin(String email, String password) {
        //validação por meio da classe de UserDetails debaixo dos panos
        var usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        var auth = this.authManager.authenticate(usernamePassword);

        // autenticação bem sucedida -> gera token do usuário
        return tokenService.generateToken((AdminEntity) auth.getPrincipal());
    } */
    public String loginAdmin(String email, String password) {
        try {
            AdminEntity admin = adminRepository.findByAdminEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            if (!bcrypt.matches(password, admin.getAdminPassword())) {
                throw new RuntimeException("Senha inválida");
            }

            return tokenService.generateToken(admin);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer login: " + e.getMessage(), e);
        }
    }

    // toda a vez que der logout, o token vai pra uma denyList que impede de esse mesmo token ser usado denovo
    public void logoutAdmin(String token, String authorizationHeader){
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token inválido para logout");
        }

        token = authorizationHeader.replace("Bearer ", "");
        // data de expiração do token
        Long expiration = tokenService.getExpirationDate(token);

        blacklistService.addTokenToBlacklist(token, expiration);
    }

}
