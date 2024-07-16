package com.example.certificateGenerator.controller;

import com.example.certificateGenerator.errorhandling.PathNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UnknowPathControllerTest {

    @Mock
    private PathNotFoundException pathNotFoundException;

    @InjectMocks
    private UnknowPathController unknowPathController;

    //MockMvc mockMvc;

//    @Test
//    void unknownPath() {
//
//        //this.mockMvc = MockMvcBuilders.standaloneSetup(new UnknowPathController()).build();
//
//        PathNotFoundException result = org.junit.jupiter.api.Assertions.assertThrows(
//                PathNotFoundException.class,
//                () -> unknowPathController.unknownPath()
//        );
//
//        Assertions.assertThat(result.getMessage()).isEqualTo("Path does not exist");
//
//    }
}