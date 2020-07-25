package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock()
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;


    @Test
    void findById() {
        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));
        //when
        Speciality foundSpeciality = service.findById(1L);
        assertThat(foundSpeciality).isNotNull();
        //then
        then(specialtyRepository).should(timeout(1)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }


    @Test
    void testDeleteByObject() {
        //given
        Speciality speciality = new Speciality();
        //when
        service.delete(speciality);
        //then
        then(specialtyRepository).should().delete(any(Speciality.class));


    }


    @Test
    void deleteById() {
        //given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(100).times(2)).deleteById(1L);

    }

    @Test
    void deleteByIdAtLeastOnce() {
        //given - none

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1L);

    }

    @Test
    void deleteByIdAtMost() {
        //when
        service.deleteById(1L);
        service.deleteById(1L);
        //then
        //Con atMost no se puede encadenar el timeout
        then(specialtyRepository).should(atMost(5)).deleteById(1L);

    }

    @Test
    void deleteByIdNever() {
        //when
        service.deleteById(1L);
        //then
        then(specialtyRepository).should(never()).deleteById(5L);

    }

    @Test
    void testDelete() {
        //when
        service.delete(new Speciality());
        //then
        then(specialtyRepository).should(times(1)).delete(any() );
    }

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("Boom")).when(specialtyRepository).delete(any());
        assertThrows(RuntimeException.class,()->specialtyRepository.delete(new Speciality()));
        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIdThrows() {
        given(specialtyRepository.findById(any())).willThrow(new RuntimeException("Boom"));
        assertThrows(RuntimeException.class, ()->specialtyRepository.findById(1L));
        then(specialtyRepository).should().findById(any());
    }

    @Test
    void testDeleteBDD() {
        willThrow(new RuntimeException("Boom")).given(specialtyRepository).delete(any());
        assertThrows(RuntimeException.class,()->specialtyRepository.delete(new Speciality()));
        then(specialtyRepository).should().delete(any());
    }

    @Test
    void testSaveLambda() {
        //given
        Speciality speciality = new Speciality();
        final String DESCRIPTION= "MATCH_ME";
        speciality.setDescription(DESCRIPTION);
        speciality.setId(1L);

        given(specialtyRepository
                .save(argThat(argument -> argument.getDescription().equals("MATCH_ME"))
        )).willReturn(speciality);

        //when
        Speciality specialitySave = specialtyRepository.save(speciality);

        //then
        assertThat(specialitySave.getId()).isEqualTo(1L);
    }

    @Test
    void testSaveLambdaNotMatch() {
        //given
        Speciality speciality = new Speciality();
        final String DESCRIPTION = "not match";
        speciality.setDescription(DESCRIPTION);
        speciality.setId(1L);

        //lenien() no mandara error al no hacer match en la lambda
        lenient().when(specialtyRepository
                .save(argThat(argument -> argument.getDescription().equals("MATCH_ME"))
                )).thenReturn(speciality);

        //when
        Speciality specialitySave = specialtyRepository.save(speciality);

        //then
        assertNull(specialitySave);
    }



}

