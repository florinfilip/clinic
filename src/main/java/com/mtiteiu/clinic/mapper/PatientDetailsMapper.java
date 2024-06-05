package com.mtiteiu.clinic.mapper;

import com.mtiteiu.clinic.model.patient.PatientDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
@Component
public interface PatientDetailsMapper {

    PatientDetailsMapper INSTANCE = Mappers.getMapper(PatientDetailsMapper.class);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "patient", ignore = true)
    void updatePatientDetails(PatientDetails source, @MappingTarget PatientDetails target);
}
