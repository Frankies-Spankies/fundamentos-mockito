package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository repository;

    @InjectMocks
    VisitSDJpaService service;

    @Test
    void findAll() {
        //given
        Set<Visit> visits = LongStream.rangeClosed(1, 5).mapToObj(Visit::new).collect(Collectors.toSet());
        given(repository.findAll()).willReturn(visits);
        //when
        Set<Visit> foundVisits = service.findAll();
        //then
        assertThat(foundVisits).hasSize(5);
        then(repository).should(times(1)).findAll();
    }

    @Test
    void findById() {
        //given
        Visit visit = new Visit(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(visit));
        //when
        Visit foundVisit = service.findById(2L);
        //then
        assertThat(foundVisit).isEqualTo(new Visit(1L));
        verify(repository).findById(anyLong());
    }

    @Test
    void save() {
        //given
        Visit visit = new Visit(1L);
        given(repository.save(any(Visit.class))).willReturn(visit);
        //when
        Visit foundVisit =service.save(new Visit(1L));
        //then
        assertThat(foundVisit).isEqualTo(new Visit(1L));
        then(repository).should(times(1)).save(any(Visit.class));
    }

    @Test
    void delete() {
        //given
        Visit visit = new Visit();
        //when
        service.delete(visit);
        //then
        then(repository).should().delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        //when
        service.deleteById(1L);
        service.findById(1L);
        //then
        then(repository).should(times(1)).deleteById(1L);
    }
}