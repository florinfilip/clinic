package com.mtiteiu.clinic.mapper;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.model.Person;
import com.mtiteiu.clinic.model.user.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = "spring", uses = {PersonMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "newPassword", qualifiedByName = "encryptPassword")
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUserDetails(UserDTO source, @MappingTarget User target, @Context BCryptPasswordEncoder passwordEncryptionService, @Context PersonMapper personMapper);

    @Named("encryptPassword")
    default String encryptPassword(String newPassword, @Context BCryptPasswordEncoder bCryptPasswordEncoder) {
        return newPassword != null ? bCryptPasswordEncoder.encode(newPassword) : null;
    }

    @AfterMapping
    default void mapPersonDetails(UserDTO source, @MappingTarget User target, @Context PersonMapper personMapper) {
        Person person = target.getPerson();
        if (person == null) {
            person = personMapper.INSTANCE.toPerson(source);
            target.setPerson(person);
        } else {
            personMapper.INSTANCE.updatePersonFromDto(source, person);
        }
    }
}

