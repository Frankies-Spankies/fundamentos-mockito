package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    //Es buena practica hacer constantes los resultados esperados de una prueba para evitar cambiarlos accidentalmente
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";
    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String OWNERS_FIND_OWNERS = "owners/findOwners";
    private static final String OWNERS_OWNERS_LIST = "owners/ownersList";

    @Mock
    OwnerService service;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OwnerController controller;

    @Captor
    ArgumentCaptor<String> argumentCaptor;

    @Mock
    Model model;

    //En este conjunto de pruebas se ocupa la anotacion @Captor para ArgumentCaptor
    @Nested
    class ProcessFindFormWithMockAnswer{
        @BeforeEach
        void setUp() {
            given(service.findAllByLastNameLike(argumentCaptor.capture())).willAnswer(invocation -> {
                        String name = invocation.getArgument(0);
                        List<Owner> owners = new ArrayList<>();
                        if (name.equals("%One%")) {
                            owners = Arrays.asList(new Owner(5l, "Franki", "Rivers"));
                        } else if (name.equals("%Many%")) {
                            owners = IntStream.rangeClosed(1, 5)
                                    .mapToObj(i -> new Owner(Long.valueOf(i), "many", String.valueOf(i)))
                                    .collect(Collectors.toList());
                        }
                        return owners;
                    }
            );

        }

        @Test
        void processFindFormWildCardStringAnnotationOne() {
            //given
            Owner owner = new Owner(5l, "Franki", "One");
            //when
            String result = controller.processFindForm(owner, bindingResult, null);
            //then
            assertThat(result).isEqualTo(REDIRECT_OWNERS_5);
            verifyZeroInteractions(model);
        }

        @Test
        void processFindFormWildCardStringAnnotationMany() {
            //given
            Owner owner = new Owner(5l, "Franki", "Many");
            //Verifica que los mocks que se le pasan como parametro se ejecuten en orden
            InOrder inOrder = inOrder(service,model);
            //when
            String result = controller.processFindForm(owner, bindingResult, model);
            //then
            assertThat(result).isEqualTo(OWNERS_OWNERS_LIST);
            //Aqui se verifica el orden en que son llamados los mocks
            inOrder.verify(service).findAllByLastNameLike(anyString());
            inOrder.verify(model).addAttribute(anyString(),anyList());
            verifyNoMoreInteractions(model);
        }

        @Test
        void processFindFormWildCardStringAnnotationEmpty() {
            //given
            Owner owner = new Owner(5l, "Franki", "Empty");
            //when
            String result = controller.processFindForm(owner, bindingResult, null);
            //then
            assertThat(result).isEqualTo(OWNERS_FIND_OWNERS);
            verifyZeroInteractions(model);
        }

    }



    @Test
    void processFindFormWildCardString() {
        //given
        Owner owner = new Owner(5l, "Franki", "Rivers");
        List<Owner> owners = new ArrayList<>();
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(service.findAllByLastNameLike(captor.capture())).willReturn(owners);//El captor se apodera de lo que se le pasa al mock como parametro
        //Una funcion es ver como se transforma el parametro; en este caso Rivers -> %Rivers%x

        verifyNoMoreInteractions(service);
        //when
        String result = controller.processFindForm(owner, bindingResult, null);
        //then
        assertThat("%Rivers%").isEqualTo(captor.getValue());
    }



    @Test
    void processCreationForm() {
        //given
        Owner owner = new Owner(5l, "Franki", "Rivers");
        given(service.save(owner)).willReturn(owner);

        //when
        String result = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(result).isEqualTo(REDIRECT_OWNERS_5);
        then(service).should(times(1)).save(any());

    }

    @Test
    void processCreationFormErrors() {
        //given
        Owner wrong = new Owner(1l, "franki", "rivers");
        given(bindingResult.hasErrors()).willReturn(true);

        //when
        String result = controller.processCreationForm(wrong, bindingResult);

        //then
        assertThat(result).isEqualTo(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
        then(bindingResult).should(times(1)).hasErrors();
    }
}