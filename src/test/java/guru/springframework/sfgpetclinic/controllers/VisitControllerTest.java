package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.VisitService;
import guru.springframework.sfgpetclinic.services.map.PetMapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

/*    @Mock
    VisitService visitService;*/

    @Spy
    PetMapService petMapService;

    @InjectMocks
    VisitController controller;

    @Test
    void loadPetWithVisit() {
        //given
        Map<String,Object> model = new HashMap<>();
        Pet pet1 = new Pet();
        Pet pet2 = new Pet();

        petMapService.save(pet1);
        petMapService.save(pet2);

        given(petMapService.findById(anyLong())).willCallRealMethod(); //Pide que regrese lo que regresaria la implementacion real

        //when
        Visit visit = controller.loadPetWithVisit(1L,model);

        //then
        assertThat(visit.getPet().getId()).isEqualTo(1L);
    }

    @Test
    void loadPetWithVisitWithStubbing() {
        //given
        Map<String, Object> model = new HashMap<>();
        Pet pet1 = new Pet();
        Pet pet2 = new Pet();

        petMapService.save(pet1);
        petMapService.save(pet2);

        given(petMapService.findById(anyLong())).willReturn(pet1); //Intercepta a la implementacion real y regresa lo que se pide

        //when
        Visit visit = controller.loadPetWithVisit(1L, model);

        //then
        assertThat(visit.getPet().getId()).isEqualTo(1L);
    }
}