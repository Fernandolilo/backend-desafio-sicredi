package com.systempro.sessao.controllerTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.systempro.sessao.controller.AgendaController;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(AssociedController.class) // Especificando explicitamente o controller a ser testado
@AutoConfigureMockMvc
public class AssociedControllerTest {

}
