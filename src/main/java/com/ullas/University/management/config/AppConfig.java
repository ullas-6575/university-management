package com.ullas.University.management.config;

import com.ullas.University.management.dto.*;
import com.ullas.University.management.entity.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.*;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.typeMap(AdminDTO.class, Admin.class).addMappings(m -> {
            m.skip(Admin::setId);
            m.skip(Admin::setUser);
        });
        mapper.typeMap(TeacherDTO.class, Teacher.class).addMappings(m -> {
            m.skip(Teacher::setId);
            m.skip(Teacher::setUser);
        });
        mapper.typeMap(StudentDTO.class, Student.class).addMappings(m -> {
            m.skip(Student::setId);
            m.skip(Student::setUser);
        });
        return mapper;
    }
}
