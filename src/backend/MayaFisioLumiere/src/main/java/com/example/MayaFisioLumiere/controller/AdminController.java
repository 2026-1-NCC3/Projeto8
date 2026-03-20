package com.example.MayaFisioLumiere.controller;

import com.example.MayaFisioLumiere.Domain.Admin.AdminRequestDTO;
import com.example.MayaFisioLumiere.Domain.Admin.AdminResponseDTO;
import com.example.MayaFisioLumiere.Services.AdminService;
import com.example.MayaFisioLumiere.entity.AdminEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adminAccess")

public class AdminController {
    @Autowired
    private AdminService adminService;


    //Atualizar email, nome ou senha
    //http://localhost:8081/api/adminAccess/updateAdmin/2
    @PutMapping("/updateAdmin/{id}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(
            @PathVariable Long id,
            @RequestBody AdminRequestDTO data) {
        AdminResponseDTO admin = adminService.updateAdmin(id, data);
        return ResponseEntity.ok(admin);
    }

    //Buscar todos os Administradores/Profissionais
    //Rota http://localhost:8081/api/adminAccess/all/admins
    @GetMapping("/all/admins")
    public ResponseEntity<List<AdminResponseDTO>> getAdmins(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        List<AdminResponseDTO> allAdmins = this.adminService.getAllAdmins(page, size);
        return ResponseEntity.ok(allAdmins);
    }

    //Buscar Administradores/Profissionais por Id
    //Rota http://localhost:8081/api/adminAccess/ID/2
    @GetMapping("/ID/{id}")
    public ResponseEntity<AdminResponseDTO> searchById(
            @PathVariable Long id) {
        AdminResponseDTO admins = adminService.findById(id);
        return ResponseEntity.ok(admins);
    }

    //Buscar por email
    //Rota http://localhost:8081/api/adminAccess/adminEmail/bot
    @GetMapping("/adminEmail/{adminEmail}")
    public ResponseEntity<List<AdminResponseDTO>> searchByEmail(
            @PathVariable String adminEmail) {
        List<AdminResponseDTO> admin = adminService.findByAdminEmail(adminEmail);
        return ResponseEntity.ok(admin);
    }

    //Deletar administrador por id
    //Rota http://localhost:8081/api/adminAccess/deleteAdminId/2
    @DeleteMapping("/deleteAdminId/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id){
        adminService.deleteAdminById(id);
        return ResponseEntity.ok("Administrador deletado com sucesso");
    }
}