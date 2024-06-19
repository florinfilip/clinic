package com.mtiteiu.clinic.mapper;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.model.Person;
import com.mtiteiu.clinic.model.patient.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonMapper {

    @Mapping(target = "cnp", source = "cnp")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "gender", source = "gender")
    void updatePersonFromDto(UserDTO source, @MappingTarget Person target);

    default Person toPerson(UserDTO source) {
        if (source == null) {
            return null;
        }
        Person person = new Patient();
        updatePersonFromDto(source, person);
        return person;
    }
}