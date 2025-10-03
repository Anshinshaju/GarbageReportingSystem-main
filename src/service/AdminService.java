package service;

import model.Admin;
import repository.AdminRepository;

public class AdminService {
    private AdminRepository adminRepository;
    
    public AdminService() {
        this.adminRepository = new AdminRepository();
    }
    
    public Admin loginAdmin(String username, String password) {
        Admin admin = adminRepository.getAdminByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }
}