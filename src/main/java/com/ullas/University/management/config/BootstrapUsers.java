package com.ullas.University.management.config;

import com.ullas.University.management.dto.*;
import com.ullas.University.management.repository.AppUserRepository;
import com.ullas.University.management.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class BootstrapUsers implements ApplicationRunner {
    private final AppUserRepository users;
    private final AdminService admins;
    private final TeacherService teachers;
    private final StudentService students;
    private final String adminPassword, teacherPassword, studentPassword;

    public BootstrapUsers(AppUserRepository users, AdminService admins, TeacherService teachers,
            StudentService students,
            @Value("${app.seed.admin-password}") String adminPassword,
            @Value("${app.seed.teacher-password}") String teacherPassword,
            @Value("${app.seed.student-password}") String studentPassword) {
        this.users = users;
        this.admins = admins;
        this.teachers = teachers;
        this.students = students;
        this.adminPassword = adminPassword;
        this.teacherPassword = teacherPassword;
        this.studentPassword = studentPassword;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments arguments) {
        if (users.count() != 0)
            return;
        AdminDTO a = new AdminDTO();
        fill(a, "admin", "Demo Admin", adminPassword);
        a.setOffice("Administration");
        admins.saveAdmin(a);
        TeacherDTO t = new TeacherDTO();
        fill(t, "teacher", "Demo Teacher", teacherPassword);
        t.setDesignation("Lecturer");
        teachers.saveTeacher(t);
        StudentDTO s = new StudentDTO();
        fill(s, "student", "Demo Student", studentPassword);
        s.setRoll("CSE-001");
        students.saveStudent(s);
    }

    private void fill(PersonDTO dto, String username, String name, String password) {
        if (!password.matches("[ -~]{6,64}"))
            throw new IllegalArgumentException("Seed passwords must be 6-64 printable ASCII characters");
        dto.setUsername(username);
        dto.setName(name);
        dto.setEmail(username + "@example.test");
        dto.setPassword(password);
    }
}
