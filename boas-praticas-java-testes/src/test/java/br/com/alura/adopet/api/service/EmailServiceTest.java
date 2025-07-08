package br.com.alura.adopet.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Captor
    private ArgumentCaptor<SimpleMailMessage> simpleMailMessageCaptor;
    @InjectMocks
    private EmailServiceDev serviceDev;
    @InjectMocks
    private EmailServiceProducao serviceProd;
    @Mock
    private JavaMailSender emailSender;
    private final String messageTo = "to";
    private final String messageSubject = "subject";
    private final String messageMessage = "message";
    private final String messageFrom = "adopet@email.com.br";

    @Test
    public void deveEnviarEmail() {

        serviceProd.enviarEmail(messageTo, messageSubject, messageMessage);
        serviceDev.enviarEmail(messageTo, messageSubject, messageMessage);

        then(emailSender).should().send(simpleMailMessageCaptor.capture());

        var captor = simpleMailMessageCaptor.getValue();
        assertEquals(messageSubject, captor.getSubject());
        assertEquals(messageMessage, captor.getText());
        assertEquals(messageFrom, captor.getFrom());
        assertEquals(messageTo, Arrays.stream(captor.getTo()).findFirst().get());

    }

}
