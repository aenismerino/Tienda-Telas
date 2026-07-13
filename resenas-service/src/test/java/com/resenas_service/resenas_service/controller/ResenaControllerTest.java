package com.resenas_service.resenas_service.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resenas_service.resenas_service.DTO.ResenaDto;
import com.resenas_service.resenas_service.model.Resena;
import com.resenas_service.resenas_service.service.ResenasService;
import com.resenas_service.resenas_service.assemblers.ResenaModelAssembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ResenaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ResenasService resenasService;

    @Mock
    private ResenaModelAssembler assembler;

    @InjectMocks
    private ResenaControllerV2 controller;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Resena resena;
    private ResenaDto resenaDto;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setId(1L);
        resena.setTelaId(10L);
        resena.setUsuarioId(5L);
        resena.setCalificacion(5);
        resena.setComentario("Excelente tela");
        resena.setFechaCreacion(LocalDate.now());

        resenaDto = new ResenaDto();
        resenaDto.setTelaId(1L);
        resenaDto.setTelaId(10L);
        resenaDto.setUsuarioId(5L);
        resenaDto.setCalificacion(5);
        resenaDto.setComentario("Excelente tela");

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListarResenas() throws Exception {
        when(resenasService.listarTodas()).thenReturn(List.of(resena));

        EntityModel<Resena> entityModel = EntityModel.of(resena,
                linkTo(methodOn(ResenaControllerV2.class).obtenerResena(1L)).withSelfRel());

        when(assembler.toModel(any(Resena.class))).thenReturn(entityModel);

        mockMvc.perform(get("/v2/resenas"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCrearResena() throws Exception {
        when(resenasService.crearResena(any(ResenaDto.class))).thenReturn(resena);

        EntityModel<Resena> entityModel = EntityModel.of(resena,
                linkTo(methodOn(ResenaControllerV2.class).obtenerResena(1L)).withSelfRel());

        when(assembler.toModel(any(Resena.class))).thenReturn(entityModel);

        mockMvc.perform(post("/v2/resenas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resenaDto)))
                .andExpect(status().isCreated());
    }
}
